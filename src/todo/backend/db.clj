(ns todo.backend.db
  "Este namespace gerencia os dados dos 'todos' em memória."
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

;; Especificação da conexão SQLite (cria/usa o arquivo todo.db na raiz do projeto)
(def db-spec {:dbtype "sqlite" :dbname "todo.db"})

;; Criação da tabela de tarefas caso ainda não exista
(defn init-db! []
  (jdbc/execute!
   db-spec
   ["CREATE TABLE IF NOT EXISTS todos (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       title TEXT NOT NULL,
       completed BOOLEAN NOT NULL DEFAULT 0
     )"]))

;; Buscar todas as tarefas
(defn get-all-todos []
  (jdbc/execute!
   db-spec
   ["SELECT id, title, completed FROM todos ORDER BY id ASC"]
   {:builder-fn rs/as-unqualified-maps}))

;; Inserir uma nova tarefa
(defn create-todo! [title]
  (jdbc/execute-one!
   db-spec
   ["INSERT INTO todos (title, completed) VALUES (?, 0) RETURNING id, title, completed" title]
   {:builder-fn rs/as-unqualified-maps}))

;; Alternar status de concluído (toggle)
(defn toggle-todo! [id]
  (jdbc/execute-one!
   db-spec
   ["UPDATE todos SET completed = CASE WHEN completed = 1 THEN 0 ELSE 1 END WHERE id = ? RETURNING id, title, completed" id]
   {:builder-fn rs/as-unqualified-maps}))

;; Deletar uma tarefa por ID
(defn delete-todo! [id]
  (jdbc/execute!
   db-spec
   ["DELETE FROM todos WHERE id = ?" id]))