# Banco Watts

## Descrição

O projeto Banco Watt-S é uma API REST desenvolvida para simular operações bancárias, incluindo funcionalidades como criação de contas, depósitos, saques, transferências e consultas de saldo.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.3.1
- Spring Data JPA
- Spring Boot Starter Validation
- Flyway
- MySQL
- Lombok
- Maven

## Requisitos

- Java 17
- Maven 3.6+
- MySQL

## Configuração do Projeto

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/banco-watt-s.git
```

2. Navegue até o diretório do projeto:

```bash
cd banco-watt-s
```

3. Configure o banco de dados MySQL no `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banco_watt_s
spring.datasource.username=seu-usuario
spring.datasource.password=sua-senha
```

4. Execute o projeto:

```bash
mvn spring-boot:run
```

## Endpoints

### Contas

- **Listar Contas Ativas**

`GET /contas`

Parâmetros:
- `page`: Número da página (opcional)
- `size`: Tamanho da página (opcional)
- `sort`: Campo de ordenação (opcional)

- **Consultar Saldo de Conta**

`GET /contas/{id}/saldo`

- **Criar Conta**

`POST /contas`

Body:
```json
{
"agencia": "0001",
"tipoConta": "CORRENTE",
"titular": "Nome do Titular",
"saldo": 1000.0,
"ativa": true
}
```

- **Depositar em Conta**

`POST /contas/{id}/deposito`

Body:
```json
{
  "deposito": 500.0
}

```

- **Sacar de Conta**

`POST /contas/{id}/saque`

Body:
```json
{
  "saque": 300.0
}
```

- **Transferir entre Contas**

`POST /contas/transferencia`
- **Parâmetros**
-`idOrigem`: ID da conta de origem
-`idDestino`: ID da conta de destino

Body:
```json
{
  "transferencia": 300.0
}
```

- **Detalhar Conta**

`GET /contas/{id}`

- **Encerrar Conta**

`DELETE /contas/{id}/encerrar`

## Contribuições
Contribuições são bem-vindas! Sinta-se à vontade para abrir uma issue ou enviar um pull request.