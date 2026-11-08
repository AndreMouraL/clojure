<div align="center">

# Todo List em Clojure/ClojureScript

Aplicação full-stack de lista de tarefas construída de forma incremental,<br>
com backend em Clojure, frontend reativo em ClojureScript e persistência em SQLite.

![Clojure](https://img.shields.io/badge/Clojure-5881D8?style=for-the-badge&logo=clojure&logoColor=white)
![ClojureScript](https://img.shields.io/badge/ClojureScript-63B132?style=for-the-badge&logo=clojure&logoColor=white)
![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

</div>

---

## Sobre o projeto

Atividade da **Unidade 2 (Clojure)** da disciplina de Programação Funcional.
O projeto replica, fase a fase, o tutorial
[Clojure/ClojureScript: Construindo uma Aplicação Persistente e Reativa](https://lambdageo-edu.github.io/lambdageo-tutorials/clojure/),
seguindo um histórico de commits incremental.

**Alunos:** André Moura Lima e Arthur Sampaio Pereira

## Tecnologias

| Camada | Tecnologia | Função |
|---|---|---|
| Backend | Clojure, Ring + Jetty | Servidor HTTP |
| Backend | Reitit | Roteamento da API REST |
| Backend | ring-json | Conversão JSON <-> mapas Clojure |
| Frontend | ClojureScript + Reagent | Interface reativa sobre React |
| Frontend | Shadow-CLJS | Compilador e servidor de desenvolvimento |
| Banco de dados | SQLite + next.jdbc | Persistência (fases finais) |
| Versionamento | Git + GitHub | Histórico incremental com Conventional Commits |

## Status do desenvolvimento

| Fase | Descrição | Status |
|---|---|---|
| 0 | Setup inicial do projeto com `.gitignore` | ✅ Concluída |
| 1 | Servidor "Hello World" com Jetty e Reitit | ✅ Concluída |
| 2 | API REST de `todos` com banco em memória (atom) | ✅ Concluída |
| 3 | UI do frontend com estado local (Reagent) | ✅ Concluída |
| 4 | Conexão do frontend com a API (CORS corrigido) | ✅ Concluída |
| 5 | Persistência com SQLite (`next.jdbc`) | ✅ Concluída |
| 6 | Toggle e delete (CRUD completo) | ✅ Concluída |

## Estrutura do projeto

```
.
├── deps.edn                      # dependências Clojure e alias :run
├── shadow-cljs.edn               # configuração do build do frontend
├── package.json                  # dependências npm (shadow-cljs, react)
├── requirements.txt              # resumo dos requisitos do projeto
├── todo.db                       # banco de dados SQLite
├── resources/public/
│   ├── index.html                # página base do frontend
│   └── style.css                 # estilos (opcional)
└── src/todo/
    ├── backend/
    │   ├── core.clj              # servidor, rotas e middlewares
    │   ├── handler.clj           # handlers HTTP
    │   └── db.clj                # banco de dados (SQLite com next.jdbc)
    └── frontend/
        └── core.cljs             # interface Reagent integrada a API REST
```

## Pré-requisitos

| Ferramenta | Versão |
|---|---|
| Java JDK | 11 ou superior (17 ou 21 recomendado) |
| Clojure CLI (`clj` ou `clojure`) | 1.11+ |
| Node.js e `npm` | 18 ou superior |
| Git | qualquer versão recente |

> Se `clj` reclamar de `rlwrap`, use `clojure` no lugar de `clj`.

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

Aguarde o `Build completed` (a primeira compilação demora alguns minutos) e acesse
**http://localhost:8000**.

> **Windows:** confira com `java -version` se o terminal está usando Java 11 ou superior.
> Versões antigas (Java 8) fazem o Shadow-CLJS falhar ao carregar.

## API

Servidor em `http://localhost:3000`.

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/hello` | Retorna `Hello, World!` |
| GET | `/api/todos` | Lista todos os todos |
| POST | `/api/todos` | Cria um todo (`title` obrigatório; retorna 400 se vazio) |
| PUT | `/api/todos/:id` | Alterna o status da tarefa (completed 0 <-> 1) |
| DELETE | `/api/todos/:id` | Deleta uma tarefa por ID |

Exemplos:

```bash
curl http://localhost:3000/api/todos

curl -X POST http://localhost:3000/api/todos \
     -H "Content-Type: application/json" \
     -d '{"title": "Estudar Clojure"}'

curl -X PUT http://localhost:3000/api/todos/1

curl -X DELETE http://localhost:3000/api/todos/1
```

No PowerShell:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:3000/api/todos `
  -ContentType "application/json" -Body '{"title":"Estudar Clojure"}'
```

## Histórico de commits

O repositório segue a convenção [Conventional Commits](https://www.conventionalcommits.org/):

1. `feat: setup inicial do projeto com .gitignore`
2. `feat: implementa servidor 'Hello World' com Jetty e Reitit`
3. `feat: implementa API REST de 'todos' com banco em memória`
4. `feat: implementa UI do frontend com estado local (sem API)`
5. `feat: implementa persistencia SQLite com next.jdbc para as tarefas`
6. `feat: inicializa tabela SQLite no bootstrap e conecta handlers a persistencia`
7. `feat: adiciona endpoints de toggle e delete para as tarefas`
8. `feat: conecta Reagent a API REST backend para listar e criar tarefas`
9. `feat: adiciona acoes de toggle e delete integradas a API REST`

## Licença

Projeto acadêmico, sem fins comerciais.