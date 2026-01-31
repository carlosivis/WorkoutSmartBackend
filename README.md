# WorkoutSmart Backend (MVP)

Backend desenvolvido em **Kotlin** com **Ktor**, servindo a API para a aplicação mobile **WorkoutSmart**. Este projeto encontra-se em fase de MVP (Minimum Viable Product), focado na gestão de grupos de treino, rankings e registo de atividades.

O Frontend Mobile (Kotlin Multiplatform) pode ser encontrado aqui:
> 🔗 **[Link para o Repositório do Frontend KMP]** *(https://github.com/carlosivis/KMP_WorkoutSmart.git)*

## 🛠 Tech Stack

* **Linguagem:** Kotlin 2.2.21
* **Framework Web:** Ktor 3.0.1 (Netty)
* **Base de Dados:** PostgreSQL 16
* **ORM:** Exposed
* **Autenticação:** Firebase Admin SDK (Validação de Token)
* **Infraestrutura:** Docker & Docker Compose

## 🚀 Funcionalidades Atuais

* **Autenticação:** Sincronização de usuarios via Firebase Auth.
* **Grupos:** Criação de grupos, sistema de convites (código único) e listagem.
* **Ranking:** Visualização de classificações (Leaderboard) baseadas em pontuação dentro dos grupos.
* **Atividades:** Registo de treinos (ex: Ginásio, Corrida) que atribuem pontos automáticos.

## ⚙️ Como rodar o Projeto

### Pré-requisitos
* JDK 21+
* Docker & Docker Compose

### Configuração
1.  Renomeie o arquivo `.env.example` para `.env` na raiz do projeto e ajuste as variáveis se necessário:
    ```properties
    PORT=8080
    DB_URL=jdbc:postgresql://localhost:5432/workout_db
    DB_USER=seu_usuario
    DB_PASSWORD=sua_senha
    ```
2.  Adicione o arquivo `firebase-admin.json` (Service Account Key) na pasta `src/main/resources/` para que a autenticação funcione.

### Executar com Docker (Recomendado)
Para subir a base de dados e a aplicação simultaneamente:
```bash
docker-compose up --build