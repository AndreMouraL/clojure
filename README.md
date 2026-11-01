# Todo List em Clojure/ClojureScript

**Alunos:** André Moura Lima e Arthur Sampaio Pereira

**Tutorial original:** https://lambdageo-edu.github.io/lambdageo-tutorials/clojure/
("Tutorial Clojure/ClojureScript: Construindo uma Aplicação Persistente e Reativa")

## Descrição

Aplicação *full-stack* de lista de tarefas (Todo List), construída de forma incremental
como atividade da Unidade 2 (Clojure) da disciplina de Programação Funcional.

- **Backend:** Clojure, Ring + Jetty (servidor), Reitit (rotas), API REST em JSON
- **Frontend:** ClojureScript com Reagent e React, compilado pelo Shadow-CLJS
- **Persistência (fase final):** SQLite via `next.jdbc`
- **Funcionalidades (fase final):** criar, listar, marcar como concluída (toggle) e excluir tarefas

## Status do desenvolvimento

| Fase | Descrição | Status |
|---|---|---|
| 0 | Setup inicial do projeto com `.gitignore` | Concluída |
| 1 | Servidor "Hello World" com Jetty e Reitit | Concluída |
| 2 | API REST de `todos` com banco em memória (atom) | Concluída |
| 3 | UI do frontend com estado local (Reagent, sem API) | Concluída |
| 4 | Conexão do frontend com a API (CORS corrigido) | Pendente |
| 5 | Persistência com SQLite (`next.jdbc`) | Pendente |
| 6 | Toggle e delete (CRUD completo) | Pendente |

## Estrutura do projeto

```
.
├── deps.edn                      # dependências Clojure e alias :run
├── shadow-cljs.edn               # configuração do build do frontend
├── package.json                  # dependências npm (shadow-cljs, react)
├── resources/public/index.html   # página base do frontend
└── src/todo/
    ├── backend/
    │   ├── core.clj              # servidor, rotas e middlewares
    │   ├── handler.clj           # handlers HTTP
    │   └── db.clj                # banco de dados (atual: em memória)
    └── frontend/
        └── core.cljs             # interface Reagent
```

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
git clone https://github.com/AndreMouraL/clojure.git
cd clojure

# 2. Instalar as dependências do frontend
npm install
```

Abra **dois terminais**, ambos na raiz do projeto:

```bash
# Terminal 1: backend (API na porta 3000)
clj -M:run

# Terminal 2: frontend (Shadow-CLJS, porta 8000)
npx shadow-cljs watch app
```

Espere o Shadow-CLJS mostrar `Build completed` (a primeira compilação demora alguns
minutos) e acesse **http://localhost:8000**.

> **Windows:** o Shadow-CLJS e o Clojure precisam rodar com Java 11 ou superior.
> Confira com `java -version` em cada terminal novo.

## Testando a API

Backend rodando na porta 3000:

```bash
curl http://localhost:3000/api/hello
curl http://localhost:3000/api/todos
curl -X POST http://localhost:3000/api/todos \
     -H "Content-Type: application/json" \
     -d '{"title": "Testar a API", "description": "com curl"}'
```

No PowerShell:

```powershell
Invoke-RestMethod http://localhost:3000/api/todos
Invoke-RestMethod -Method Post -Uri http://localhost:3000/api/todos -ContentType "application/json" -Body '{"title":"Testar a API"}'
```

### Rotas disponíveis

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/hello` | Retorna `Hello, World!` |
| GET | `/api/todos` | Lista todos os todos |
| POST | `/api/todos` | Cria um todo (`title` obrigatório; 400 se vazio) |

## Histórico de desenvolvimento

O repositório foi construído em fases, com commits incrementais:

1. `feat: setup inicial do projeto com .gitignore`
2. `feat: implementa servidor 'Hello World' com Jetty e Reitit`
3. `feat: implementa API REST de 'todos' com banco em memória`
4. `feat: implementa UI do frontend com estado local (sem API)`
5. *(pendente)* conexão do frontend com a API
6. *(pendente)* persistência com SQLite
7. *(pendente)* toggle e delete