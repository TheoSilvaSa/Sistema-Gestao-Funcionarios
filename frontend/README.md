# Frontend: Sistema de Gestão de Funcionários e Departamentos

Este projeto contém a interface de usuário (UI) para o Sistema de Gestão, incluindo módulos de Funcionários e Departamentos, desenvolvida com Angular 17.

## Módulos

* **Funcionários:** Permite cadastrar, listar (com departamento), editar e inativar funcionários.
* **Departamentos:** Permite cadastrar, listar, editar e inativar departamentos.

## Dependências

* Node.js (v18+)
* Angular CLI (v17+)
* PrimeNG 17
* PrimeFlex
* PrimeIcons

## Passos para Execução

1.  Navegue para esta pasta (`/frontend`) no seu terminal.
2.  Instale as dependências necessárias:
    ```bash
    npm install
    ```
3.  Inicie o servidor de desenvolvimento:
    ```bash
    ng serve
    ```

## Portas Utilizadas

* **Aplicação:** `http://localhost:4200`

A aplicação irá se conectar automaticamente à API do backend (rodando na porta `8080`).

## Navegação

* Acesse `http://localhost:4200/funcionarios` para o módulo de Funcionários.
* Acesse `http://localhost:4200/departamentos` para o módulo de Departamentos.
* Botões de navegação estão disponíveis nas telas de listagem para alternar entre os módulos.
