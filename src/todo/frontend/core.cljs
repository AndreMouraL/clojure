(ns todo.frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [clojure.string :as str]))

;; --- O "Cérebro" Reativo ---
(defonce app-state (r/atom {:input-text ""
                            :todos []
                            :loading? false}))

(def api-url "http://localhost:3000/api/todos")

;; --- Requisições à API Backend (js/fetch) ---

(defn fetch-todos! []
  (swap! app-state assoc :loading? true)
  (-> (js/fetch api-url)
      (.then (fn [res] (.json res)))
      (.then (fn [data]
               (let [todos (js->clj (.-todos data) :keywordize-keys true)]
                 (swap! app-state assoc :todos todos :loading? false))))
      (.catch (fn [err]
                (js/console.error "Erro ao carregar tarefas:" err)
                (swap! app-state assoc :loading? false)))))

(defn adicionar-todo-api []
  (let [novo-titulo (:input-text @app-state)]
    (when-not (str/blank? novo-titulo)
      (-> (js/fetch api-url
                    #js {:method "POST"
                         :headers #js {"Content-Type" "application/json"}
                         :body (js/JSON.stringify #js {:title novo-titulo})})
          (.then (fn [res] (.json res)))
          (.then (fn [_]
                   (swap! app-state assoc :input-text "")
                   (fetch-todos!)))
          (.catch (fn [err]
                    (js/console.error "Erro ao adicionar tarefa:" err)))))))

(defn toggle-todo-api [id]
  (-> (js/fetch (str api-url "/" id)
                #js {:method "PUT"})
      (.then (fn [res] (.json res)))
      (.then (fn [_] (fetch-todos!)))
      (.catch (fn [err]
                (js/console.error "Erro ao alternar status da tarefa:" err)))))

(defn delete-todo-api [id]
  (-> (js/fetch (str api-url "/" id)
                #js {:method "DELETE"})
      (.then (fn [res] (.json res)))
      (.then (fn [_] (fetch-todos!)))
      (.catch (fn [err]
                (js/console.error "Erro ao deletar tarefa:" err)))))

;; --- Componentes ---

;; O Formulário (conectado ao app-state)
(defn todo-form []
  [:div.todo-input
   [:input
    {:type "text"
     :placeholder "O que precisa ser feito?"
     ;; (Leitura): o valor do input vem do app-state
     :value (:input-text @app-state)
     ;; (Escrita): o on-change atualiza o app-state a cada tecla
     :on-change #(swap! app-state assoc :input-text (-> % .-target .-value))
     :on-key-down #(when (= (.-key %) "Enter") (adicionar-todo-api))}]

   [:button
    ;; (Ação): o on-click chama nossa lógica local
    {:on-click adicionar-todo-api}
    "Adicionar"]])

(defn todo-item [todo]
  [:li.todo-item
   {:style {:display "flex"
            :align-items "center"
            :justify-content "space-between"
            :padding "8px 0"
            :border-bottom "1px solid #eee"}}
   [:div {:style {:display "flex" :align-items "center" :gap "10px"}}
    [:input {:type "checkbox"
             :checked (boolean (:completed todo))
             :on-change #(toggle-todo-api (:id todo))}]
    [:span {:style {:text-decoration (if (:completed todo) "line-through" "none")
                    :color (if (:completed todo) "#888" "#000")}}
     (:title todo)]]
   [:button {:on-click #(delete-todo-api (:id todo))
             :style {:background "#ff4d4f"
                     :color "white"
                     :border "none"
                     :padding "4px 8px"
                     :border-radius "4px"
                     :cursor "pointer"}}
    "Excluir"]])

;; A Lista (lê diretamente do app-state)
(defn todo-list []
  (if (:loading? @app-state)
    [:p "Carregando tarefas..."]
    [:ul.todo-list {:style {:list-style "none" :padding 0}}
     (for [todo (:todos @app-state)]
       ^{:key (:id todo)}
       [todo-item todo])]))

;; O App Principal
(defn app []
  [:div.todo-app {:style {:max-width "500px" :margin "40px auto" :font-family "sans-serif"}}
   [:h1 "Todo App (ClojureScript + SQLite)"]
   [todo-form]
   [todo-list]])

;; --- A Inicialização ---
(defn ^:export init []
  (println "Frontend integrado com API REST inicializado...")
  (fetch-todos!)
  (let [root (rdom/create-root (js/document.getElementById "app"))]
    (.render root (r/as-element [app]))))