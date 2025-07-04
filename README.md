# 📦 app-ordermanager

Uma aplicação Java Spring Boot responsável por receber pedidos via REST API e publicá-los em uma fila SQS.
A aplicação também faz o consumo de mensagens da fila para processamento de pedidos.

---

## 🚀 Tecnologias utilizadas

- Java 21
- Spring Boot
- AWS SDK v2 (SQS)
- LocalStack (Simulação AWS local)
- Docker & Docker Compose
- Maven

---

## ⚙️ Como rodar o projeto

### 1. Fazer um clone do projeto

```bash
git clone https://github.com/caaiobomfim/app-ordermanager.git
```

Pre-requisitos:
- Instalação do JDK 21.
- Executar mvn clean package para geração do jar do projeto.

### 2. Subir ambiente com Docker Compose

O docker-compose.yml foi desenvolvido para iniciar o container da aplicação e o container do localstack que simula os serviços da AWS.

```bash
docker-compose up -d --build
```

### 3. Criação da fila SQS no LocalStack

Após execução do docker compose, é necessária a execução do script abaixo para a criação da fila sqs que será utilizada para consumo e postagem.

```bash
./scripts/create-queue.sh
```

### 4. Enviar requisição de criação de um pedido

Após criação da fila, podemos fazer uma requisição para a API exposta na porta 8080 do container da aplicação.

```bash
POST http://localhost:8080/pedidos
Content-Type: application/json

{
    "clientId": "123e4567-e89b-12d3-a456-426614174000",
    "items": [ "item1", "item2"]
}
```

A resposta deverá ser parecida com o exemplo abaixo:

```bash
{
	"id": "3033248c-a7c9-428c-afbf-1724af8057ad",
	"clientId": "123e4567-e89b-12d3-a456-426614174000",
	"items": [
		"item1",
		"item2"
	],
	"status": "PENDENTE"
}
```

### 5. Enviar requisição de consulta de um pedido

Após o envio do pedido, podemos agora consultar o status do pedido com base no exemplo abaixo.

```bash
GET http://localhost:8080/pedidos/3033248c-a7c9-428c-afbf-1724af8057ad
```

A resposta deverá ser parecida com o exemplo abaixo:

```bash
{
	"id": "3033248c-a7c9-428c-afbf-1724af8057ad",
	"clientId": "123e4567-e89b-12d3-a456-426614174000",
	"items": [
		"item1",
		"item2"
	],
	"status": "PROCESSADO"
}
```

Observação: podemos visualizar os logs da aplicação diretamente no container docker com o comando abaixo:

```bash
docker logs -f app_ordermanager
```

---

## 🚀 Bibliotecas, Componentes, Frameworks e Padrões utilizados

- **Spring Boot Starter Web:** Criação de APIs REST.
- **Spring Boot Starter Validation:** Validação automática de requisições com anotações.
- **Spring Boot Starter Test:** Suporte a testes unitários com JUnit, Mockito e outras biliotecas.
- **AWS SDK v2 - SQS:** Integração com o Amazon SQS para envio e consumo de mensagens.
- **Jackson:** Serialização e desserialização de objetos Java para JSON.
- **Lombok:**	Redução de códigos repetitivos (getters, setters, construtores, entre outros).
- **Docker & Docker Compose:** Containerização da aplicação e do ambiente de testes.
- **LocalStack:**	Emular AWS SQS localmente.
- **AWS CLI:** Criação da fila SQS via script.
- **Maven:** Build, gerenciamento de dependências e execução de testes.
- **MapStruct:** Facilitar a conversão entre DTOs e entidades de domínio.
- **Clean Architecture:** Projeto organizado seguindo princípios de Clean Architecture para melhor separação de responsabilidades, testabilidade e manutenibilidade.

---