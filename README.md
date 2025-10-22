# MediaConduit - API

Uma API robusta e escalável construída com Spring Boot, projetada para gerenciar e processar tarefas de extração e download de metadados e conteúdo de mídia (como vídeos do YouTube) de forma assíncrona usando o `yt-dlp`.

---

### 🌟 Visão Geral

O **MediaConduit-API** oferece endpoints para que clientes possam solicitar a extração de metadados ou o download de mídias longas. Todo o processamento de I/O pesado com `yt-dlp` é delegado a um *pool* de *threads* dedicado, permitindo que as requisições HTTP retornem imediatamente com um `jobId` para rastreamento de status.

O serviço garante a concorrência segura do I/O através de um `ExecutorService` e persiste o estado de cada tarefa (`ExtractionTask`) usando Spring Data JPA, fornecendo rastreabilidade completa e alta disponibilidade durante operações de longa duração.

### ⚙️ Tecnologias Utilizadas

| Categoria | Tecnologia | Versão Principal |
| :--- | :--- |:-----------------|
| **Backend** | Java | 21+              |
| **Framework** | Spring Boot | 3.x              |
| **Gerenciamento** | Gradle | 8.x              |
| **Persistência** | Spring Data JPA / Hibernate | -                |
| **Banco de Dados** | PostgreSQL (Recomendado) | -                |
| **Mapeamento** | MapStruct, Lombok | 1.5+, 1.18+      |
| **Processamento CLI** | yt-dlp | `2025.10.14`     |
| **Documentação** | SpringDoc OpenAPI (Swagger UI) | -                |

---

### 🚀 Começando

Estas instruções permitirão que você obtenha uma cópia do projeto em execução em sua máquina local para fins de desenvolvimento e teste.

#### Pré-requisitos

Você precisará ter o seguinte software instalado em sua máquina:

* **JDK 21 ou superior**
* **Gradle** (Geralmente incluído no projeto com o wrapper `gradlew`)
* **Docker** e **Docker Compose** (Altamente recomendado para configuração de DB)
* **`yt-dlp`** (O binário deve estar acessível via PATH do sistema operacional para que o `CommandExecutor` possa executá-lo.)

#### Instalação Local

1.  **Clone o repositório:**
    ```bash
    git clone [URL_DO_SEU_REPOSITÓRIO]
    cd MediaConduit-API
    ```

2.  **Configurar o Banco de Dados (PostgreSQL via Docker):**
    Assumindo a existência de um `docker-compose.yml` que provisiona um PostgreSQL:
    ```bash
    docker-compose up -d
    ```

3.  **Configurar `application.properties`:**
    Atualize o arquivo `src/main/resources/application.properties` (ou `application.yml`) com as credenciais do seu banco de dados e configurações de ambiente.

    ```properties
    # Exemplo de configuração do DB
    spring.datasource.url=jdbc:postgresql://localhost:5432/mediadb
    spring.datasource.username=mediauser
    spring.datasource.password=mediapassword
    # Configuração de Logs para Debug
    logging.level.io.github.mgluizbrito=DEBUG
    ```

4.  **Compilar e Executar:**
    Use o wrapper Gradle para construir e executar a aplicação:
    ```bash
    ./gradlew clean build
    java -jar build/libs/MediaConduit-API-*.jar
    # --- OU, para desenvolvimento rápido ---
    ./gradlew bootRun
    ```
    A API estará acessível em `http://localhost:8080`.

---

### 🗺️ Endpoints da API

A documentação completa e interativa dos endpoints é gerada automaticamente pelo SpringDoc e está disponível via **Swagger UI**.

| Ferramenta | URL de Acesso |
| :--- | :--- |
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` |
| **OpenAPI Docs (JSON)** | `http://localhost:8080/v3/api-docs` |

#### Endpoints de Serviço

| Método | Caminho | Descrição |
| :--- | :--- | :--- |
| **POST** | `/downloads/extract` | Inicia uma nova tarefa de extração/download. Retorna um `jobId` e um `trackingUrl`. |
| **GET** | `/downloads/status/{jobId}` | Verifica o status, progresso e URL de acesso do arquivo final (se `COMPLETED`). |
| **GET** | `/downloaded-media/{filename}` | **Resource Handler:** Fornece acesso direto e estático aos arquivos de mídia baixados. |

#### Exemplo de Requisição (Download)

```bash
curl -X POST http://localhost:8080/downloads/extract \
-H 'Content-Type: application/json' \
-d '{
    "mediaIdentifier": "[https://www.youtube.com/watch?v=dQw4w9WgXcQ](https://www.youtube.com/watch?v=dQw4w9WgXcQ)",
    "format": "mp4",
    "config": {
        "dataType": "YOUTUBE_CONFIG",
        "quality": 5 
    }
}'
```