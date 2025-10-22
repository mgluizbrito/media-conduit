# Exemplo de Uso da API MediaConduit

## Como solicitar um download

### 1. Solicitar Download

**POST** `/extract/{service}`

Exemplo para YouTube:
```bash
curl -X POST http://localhost:8080/extract/youtube \
  -H "Content-Type: application/json" \
  -d '{
    "mediaIdentifier": "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    "format": "mp4",
    "notificationEmail": "user@example.com",
    "config": {
      "dataType": "YOUTUBE_CONFIG",
      "quality": 3,
      "includeSubtitles": true,
      "playlistIndex": 1
    }
  }'
```

**Resposta:**
```json
{
  "jobId": "123e4567-e89b-12d3-a456-426614174000",
  "status": "PENDING",
  "trackingURL": "http://localhost:8080/extract/status/123e4567-e89b-12d3-a456-426614174000"
}
```

### 2. Verificar Status do Download

**GET** `/extract/status/{jobId}`

```bash
curl http://localhost:8080/extract/status/123e4567-e89b-12d3-a456-426614174000
```

**Resposta:**
```json
{
  "jobId": "123e4567-e89b-12d3-a456-426614174000",
  "mediaIdentifier": "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
  "status": "IN_PROGRESS",
  "progress": 45,
  "startTime": "2024-01-15T10:30:00",
  "finishTime": null,
  "fileUrl": null,
  "errorMessage": null
}
```

### 3. Baixar Arquivo (quando concluído)

**GET** `/downloads/{jobId}`

```bash
curl -O http://localhost:8080/downloads/123e4567-e89b-12d3-a456-426614174000
```

## Status Possíveis

- `PENDING`: Task criada, aguardando processamento
- `IN_PROGRESS`: Download em andamento
- `COMPLETED`: Download concluído com sucesso
- `FAILED`: Download falhou

## Configurações de Qualidade

- `1`: 360p (qualidade mais baixa)
- `2`: 480p
- `3`: 720p (HD)
- `4`: 1080p (Full HD)
- `5`: 2160p (4K - qualidade mais alta)

## Estrutura de Arquivos

Os arquivos baixados são salvos em:
```
[diretorio-do-programa]/media/downloaded/[titulo-do-video].[extensao]
```

Exemplo: `./media/downloaded/Rick Astley - Never Gonna Give You Up.mp4`
