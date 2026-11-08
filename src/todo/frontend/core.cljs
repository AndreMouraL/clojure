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

;; A Lista (lê diretamente do app-state)
(defn todo-list []
  (if (:loading? @app-state)
    [:p "A carregar tarefas..."]
    [:ul.todo-list
     (for [todo (:todos @app-state)]
       ^{:key (:id todo)}
       [:li.todo-item
        {:style {:text-decoration (if (:completed todo) "line-through" "none")}}
        (:title todo)])]))

;; O App Principal
(defn app []
  [:div.todo-app
   [:h1 "Todo App (ClojureScript + SQLite)"]
   [todo-form]
   [todo-list]])

;; --- A Inicialização ---
(defn ^:export init []
  (println "Frontend integrado com API REST inicializado...")
  (fetch-todos!)
  (let [root (rdom/create-root (js/document.getElementById "app"))]
    (.render root (r/as-element [app]))))