# Backend: Sistema de Gestão de Funcionários e Departamentos

Este projeto contém a API REST para o Sistema de Gestão, incluindo módulos de Funcionários e Departamentos, desenvolvida com Spring Boot.

## Dependências

* Java 17
* Spring Boot 3.5.6  *(Nota: Usamos 3.3.0 para compatibilidade com Swagger, verifique sua versão no pom.xml)*
* Spring Data JPA
* H2 Database (em arquivo)
* Springdoc OpenAPI (Swagger)
* Maven

## Passos para Execução

1.  Abra este projeto (a pasta `/backend`) na sua IDE (ex: IntelliJ).
2.  Aguarde o Maven baixar todas as dependências.
3.  Localize e execute a classe principal: `GestaofuncionariosApplication.java`.
4.  Como alternativa, pelo terminal (dentro da pasta `/backend`):
    ```bash
    mvn spring-boot:run
    ```

## Portas Utilizadas

* **API:** `http://localhost:8080`
* **H2 Console:** `http://localhost:8080/h2-console`

### Configuração do Banco (H2 Console)
Ao acessar o console H2, utilize os seguintes dados:

* **JDBC URL:** `jdbc:h2:file:./data/db-api`
* **User Name:** `sa`
* **Password:** (deixe em branco)

## Documentação da API (Swagger UI)
A documentação completa dos endpoints está disponível via Swagger e pode ser acessada em:

* **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

## Endpoints

### Funcionários (`/api/funcionarios`)
* `POST /api/funcionarios` - Cadastra um novo funcionário (vinculado a um departamento).
* `GET /api/funcionarios` - Lista/filtra todos os funcionários (exibindo o departamento).
* `GET /api/funcionarios/{id}` - Busca um funcionário por ID.
* `PUT /api/funcionarios/{id}` - Atualiza um funcionário existente.
* `PATCH /api/funcionarios/{id}/inativar` - Inativa um funcionário (soft delete).

### Departamentos (`/api/departamentos`)
* `POST /api/departamentos` - Cria um novo departamento
* `GET /api/departamentos` - Lista todos os departamentos
* `GET /api/departamentos/ativos` - Lista apenas os departamentos ativos
* `GET /api/departamentos/{id}` - Busca um departamento por ID.
* `PUT /api/departamentos/{id}` - Atualiza dados de um departamento
* `PATCH /api/departamentos/{id}/inativar` - Inativa um departamento
