# Fluxo do Sistema MediaConduit API

## Arquitetura do Sistema

```
Cliente → ExtractController → DownloadService → CommandExecutor → yt-dlp
                ↓                    ↓              ↓
         ExtractionTask         FileSystemService  Processo Assíncrono
                ↓                    ↓
         ExtractionTaskRepository  Diretório ./media/downloaded/
```

## Fluxo de Download

### 1. Solicitação de Download
1. Cliente faz POST para `/extract/{service}` com `ExtractRequestDTO`
2. `ExtractController` valida o serviço e cria `ExtractionTask`
3. `DownloadService` salva a task no banco com status `PENDING`
4. `CommandExecutor` submete a task para execução assíncrona
5. Retorna `ExtractResponseDTO` com `jobId` e `trackingURL`

### 2. Processamento Assíncrono
1. `CommandExecutor` executa `yt-dlp` em thread separada
2. Captura progresso em tempo real do output do `yt-dlp`
3. Atualiza status da task: `PENDING` → `IN_PROGRESS` → `COMPLETED/FAILED`
4. Salva caminho do arquivo final quando concluído

### 3. Tracking de Status
1. Cliente faz GET para `/extract/status/{jobId}`
2. `ExtractController` busca task no banco
3. Retorna `TaskStatusDTO` com status atual, progresso e informações

### 4. Download do Arquivo
1. Cliente faz GET para `/downloads/{jobId}`
2. `DownloadsController` verifica se task está `COMPLETED`
3. Retorna arquivo como `FileSystemResource`

## Componentes Principais

### Controllers
- **ExtractController**: Gerencia extração e status
- **DownloadsController**: Gerencia download de arquivos

### Services
- **DownloadService**: Lógica de negócio para downloads
- **ExtractService**: Extração de metadados
- **FileSystemService**: Gerenciamento de arquivos

### CLI
- **CommandExecutor**: Execução assíncrona de comandos
- **YtDlpProcessBuilder**: Construção de comandos yt-dlp

### Repository
- **ExtractionTaskRepository**: Persistência de tasks

## Configurações Suportadas

### ExtractConfigDTO
- **quality**: 1-5 (360p a 4K)
- **dataType**: Tipo de configuração específica

### YoutubeExtractConfigDTO
- **includeSubtitles**: Incluir legendas
- **playlistIndex**: Índice da playlist

## Tratamento de Erros

- Tasks com erro são marcadas como `FAILED`
- Mensagem de erro é salva em `errorMessage`
- Logs detalhados para debugging
- Validação de entrada com Bean Validation
