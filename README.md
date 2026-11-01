# Todo List em Clojure/ClojureScript

**Aluno(a):** [SEU NOME COMPLETO]

**Tutorial original:** https://lambdageo-edu.github.io/lambdageo-tutorials/clojure/
("Tutorial Clojure/ClojureScript: Construindo uma Aplicação Persistente e Reativa")

## Descrição

Aplicação *full-stack* de lista de tarefas (Todo List), construída de forma incremental
como atividade da Unidade 2 (Clojure) da disciplina de Programação Funcional.

- **Backend:** Clojure, Ring + Jetty (servidor), Reitit (rotas), API REST em JSON
- **Persistência:** SQLite via `next.jdbc`
- **Frontend:** ClojureScript com Reagent, compilado pelo Shadow-CLJS
- **Funcionalidades:** criar, listar, marcar como concluída (toggle) e excluir tarefas

## Pré-requisitos

| Ferramenta | Versão |
|---|---|
| Java JDK | 11 ou superior (17 ou 21 recomendado) |
| Clojure CLI (`clj` ou `clojure`) | 1.11+ |
| Node.js e `npm` | 18 ou superior |
| Git | qualquer versão recente |

> Se `clj` reclamar de `rlwrap`, use `clojure` no lugar de `clj` em todos os comandos.

## Como rodar

```bash
# 1. Baixar o projeto
git clone https://github.com/SEU-USUARIO/todo-app.git
cd todo-app

# 2. Instalar as dependências do frontend
npm install
```

Abra **dois terminais**, ambos na raiz do projeto:

```bash
# Terminal 1: backend (API na porta 3000)
clj -M:run

# Terminal 2: frontend (Shadow-CLJS)
npx shadow-cljs watch app
```

Depois, acesse o endereço do frontend indicado pelo Shadow-CLJS no terminal
(por padrão, http://localhost:8280 ou o configurado em `shadow-cljs.edn`).

Teste rápido da API:

```bash
curl http://localhost:3000/api/hello
```

O banco SQLite (`prod.db`) é criado automaticamente na primeira execução e fica
fora do Git (veja o `.gitignore`).

## Histórico de desenvolvimento

O repositório foi construído em fases, com commits incrementais:

1. Setup inicial do projeto com `.gitignore`
2. Servidor "Hello World" com Jetty e Reitit
3. API REST de `todos` com banco em memória
4. UI do frontend com estado local (sem API)
5. Conexão do frontend com a API (CORS corrigido)
6. Persistência com SQLite
7. Toggle e delete (CRUD completo)
