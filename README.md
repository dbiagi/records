# Lançamentos

## Infra

- Banco Postgres
- Mensageria com RabbitMQ

## Endpoints

### Consulta de saldo por id do cliente :heavy_check_mark:
GET /clients/{client-id}/balance :heavy_check_mark:

```json
{
    "balance": 500,
    "from": "2022-12-01T00:00:00"
}
```

### Consulta de lançamentos por id do cliente :heavy_check_mark:
GET /clients/{client-id}/records?since=2022-12-01T00-00-00&until=2022-12-02T00:00:00 :heavy_check_mark:

```json
{
    "records": {
        "2022-12-01": [
            {
                "description": "bar do jose",
                "value": -10
            },
            {
                "description": "mercado",
                "value": 100
            }
        ]
    }
}
```

### Solicita criação de arquivo de lançamentos antigos (WIP)
GET /clients/{client-id}/records/generate?since 

```json
{
    "file": "https://localhost:8080/clients/{client-id}/records/file/{id}"
}
```

### Download de arquivo com lançamentos antigos (WIP)
GET /clients/{client-id}/records/file/{id}


## Requisitos de negócios

- Armazenar saldo já calculado :heavy_check_mark:
- Api deve ter paginação e filtros :heavy_check_mark:
- Lançamentos devem armazenar somente os últimos 90 dias :heavy_check_mark:
- Job que remove os lançamentos mais antigos que 90 dias da tabela quente e joga para uma tabela fria :heavy_check_mark:
- Endpoint para solicitar lançamentos mais antigos que 90 dias asincronamente
- Endpoint para fazer download dessa solicitação
- Job de expurgo dos arquivos gerados de consultas antigas
- Endpoint para upload de arquivos para adicionar lançamentos
- Listener para adicionar lançamentos
- Registros idempotentes, lançamento é unico de acordo com a data, descrição e valor
- Lançamentos com mais de 3 anos devem ser expurgados permanentemente

## Requisitos técnicos

- Na primeira versão a aplicação deve ser um monolito :heavy_check_mark:
- Na segunda versão a aplicação deverá ser serviços separados e escaláveis


## Referências

- Declarando filas e dead letter exchange no rabbitmq: https://www.springcloud.io/post/2022-03/notes-rabbitmq-spring-boot

