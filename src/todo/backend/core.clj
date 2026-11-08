(ns todo.backend.core
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.cors :refer [wrap-cors]]
            [todo.backend.handler :as handler])
  (:gen-class))

;; Helper para aplicar o wrap-cors com os parâmetros necessários no Reitit
(defn wrap-cors-middleware [handler-fn]
  (wrap-cors handler-fn
             :access-control-allow-origin [#".*"]
             :access-control-allow-methods [:get :post :put :delete :options]
             :access-control-allow-headers ["Content-Type"]))

;; --- 1. Definição das Rotas ---
(def app-routes
  (ring/router
   ;; Aninhamos tudo sob "/api"
   ["/api"
    ["/hello" {:get {:handler handler/hello-handler}}]

    ["/todos"
     {:get  {:handler handler/list-todos-handler}      ;; GET  chama 'list'
      :post {:handler handler/create-todo-handler}}]]))  ;; POST chama 'create'

;; --- 2. Definição da Aplicação (App) ---
(def app
  (ring/ring-handler
   app-routes                     ;; Nossas rotas
   (ring/create-default-handler)  ;; Handler padrão para 404 (Not Found)

   ;; --- A "Linha de Montagem" de Middlewares ---
   ;; A requisição percorre o vetor DE CIMA PARA BAIXO até chegar
   ;; ao handler; a resposta volta DE BAIXO PARA CIMA.
   {:middleware [;; 0. Permite requisições Cross-Origin (CORS) do Frontend
                 wrap-cors-middleware
                 
                 ;; 1. Converte a *resposta* (nosso mapa) em JSON
                 wrap-json-response

                 ;; 2. Converte o *corpo* da requisição (JSON)
                 ;;    em um mapa Clojure e o coloca em :body
                 [wrap-json-body {:keywords? true}]

                 ;; 3. Lê parâmetros de URL e os coloca em :query-params
                 wrap-params

                 ;; 4. Converte as chaves-string dos parâmetros em keywords.
                 ;;    Precisa vir DEPOIS do wrap-params!
                 wrap-keyword-params]}))

;; --- 3. Função para Iniciar o Servidor ---
(defn start-server [port]
  (println (str "Servidor iniciado na porta " port))
  ;; #'app passa a "var" da nossa app para o Jetty (útil no desenvolvimento)
  ;; :join? false evita que o servidor bloqueie a thread principal.
  (jetty/run-jetty #'app {:port port :join? false}))

;; --- 4. Ponto de Entrada Principal (-main) ---
(defn -main [& args]
  ;; Permite passar a porta como argumento (ex: clj -M:run 8080)
  ;; ou usa "3000" como padrão.
  (let [port (Integer/parseInt (or (first args) "3000"))]
    (start-server port)))