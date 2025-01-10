# Sistema de Votação Cooperativa

Este sistema digital possibilita a realização de votações online dentro de cooperativas. Através dele, os associados podem participar ativamente das decisões da cooperativa, votando em diversas pautas ##de forma simples e segura.

### Importante
Na classe CpfValidationService, o método checkCpfEligibility, responsável por validar o CPF através de uma API externa, está atualmente comentado. Isso ocorre porque, se a URL https://user-info.herokuapp.com/users/{cpf} estiver inacessível, o método sempre retornará um erro HTTP 404, interrompendo o funcionamento normal do sistema.

Caso a URL esteja ativa e funcional, você pode descomentar o método para habilitar a validação do CPF.

## Tecnologias Utilizadas

* Java 17: Linguagem de programação principal para desenvolvimento da aplicação.

* Spring Framework 3.3.7: Framework para criação de aplicações robustas e orientadas a microserviços.

* Log4j: Ferramenta de logging para monitoramento e depuração.

* Lombok: Redução de boilerplate no código.

* JUnit: Framework para testes unitários.

* Mockito: Framework para criação de mocks em testes unitários.

* PostgreSQL 15: Banco de dados relacional.

* Flyway: Controle de migração de esquemas no banco de dados.

* Kafka: Plataforma de streaming distribuído para mensageria.

* Spring Actuator: Monitoramento e gerenciamento da aplicação.

* Swagger: Documentação automática de APIs.

* Prometheus e Grafana: Monitoração e visualização de métricas.

* Docker: Contenerização para facilitação do ambiente de execução.

## Requisitos

* WSL (Windows Subsystem for Linux): Necessário para sistemas Windows.

   * Caso não tenha instalado, execute o seguinte comando no terminal e reinicie o computador:
   ```bash
   wsl --install
   ```

* Docker: Ferramenta para execução de containers.

## Como Executar

### 1. Preparar o Ambiente

1. Certifique-se de que o WSL (caso esteja em Windows) e o Docker estão devidamente instalados e configurados.

2. Clone este repositório:

```bash
git clone https://github.com/wallace-rocha/voting-system.git
```
```
cd <path-do-seu-projeto>
```

### 2. Executar o Projeto com Docker

1. Na pasta raiz do projeto, execute o seguinte comando:
```bash
 docker-compose up --build
```
2. O Docker irá baixar as imagens necessárias e levantar os containers para executar a aplicação.

## Endpoints Disponíveis

Os endpoints estão documentados automaticamente pelo Swagger. Após iniciar o projeto, acesse:

* Swagger UI: http://localhost:8080/swagger-ui/index.html

## Monitoração e Métricas

* Actuator: Informações de monitoramento estão disponíveis em http://localhost:8080/actuator.

* Prometheus: Métricas estão acessíveis em http://localhost:9090.

* Grafana: Visualização de métricas em http://localhost:3000.

### Configuração do Dashboard no Grafana

1. Acesse o Grafana em http://localhost:3000.

2. O usuário e senha para o primeiro acesso é "admin" para ambos.

3. No menu lateral, clique em Connections -> Data sources -> Add new datasources.

   * Selecione a opção prometheus.
   * Em Prometheus server URL coloque: http://prometheus:9090.
   * Role para o final da página e clique em Save & test.

4. No menu lateral, clique em Dashboards -> New -> Import.

5. Insira o ID 14430 no campo correspondente e clique em Load -> Import.

6. Dentro do dashboard clique em Edit -> Settings. 
 
7. Acesse Variables e selecione Application.

   * Retire os filtros que estão em Metric e Label filters.

8. Volte em Variables e selecione Instance.

   * Em Label Filters adicione: application = $application.

## Estrutura do Projeto

 O projeto está organizado em pacotes conforme segue:

* controller: Classes responsáveis por expor os endpoints da API.

* service: Contém a lógica de negócio da aplicação.

* repository: Interfaces para acesso ao banco de dados.

* model: Definição das entidades do sistema.

* exceptionhandler: Tratamento centralizado de exceções.

* util: Classes utilitárias.

## Considerações

* Em caso de problemas com o Docker ou o WSL, verifique se as versões instaladas são compatíveis.