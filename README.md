# Serviço de Gerenciamento de Pedidos

## Visão Geral

Este projeto implementa um serviço de "pedidos" que gerencia e calcula o valor total dos pedidos recebidos de um produto externo A. Após o processamento, os pedidos são disponibilizados para um produto externo B. O serviço foi projetado para lidar com um alto volume de pedidos, variando de 150.000 a 200.000 pedidos por dia.

## Objetivos

- **Gerenciamento de Pedidos**: Receber pedidos do produto externo A, calcular o valor total de cada pedido e gerenciar o status do pedido.
- **Integração**: Fornecer pontos de integração para os produtos externos A e B.
- **Alta Disponibilidade**: Garantir que o serviço permaneça disponível sob alta carga.
- **Consistência dos Dados**: Manter a consistência dos dados e lidar com concorrência de forma eficaz.

## Funcionalidades

- **Cálculo de Pedidos**: Somar o valor de cada produto dentro de um pedido para calcular o valor total do pedido.
- **Gerenciamento de Status do Pedido**: Acompanhar e atualizar o status de cada pedido.
- **Verificação de Pedidos Duplicados**: Implementar verificações para evitar pedidos duplicados.
- **Manuseio de Alto Volume**: Garantir que o serviço possa lidar com até 200.000 pedidos por dia sem degradação de desempenho.

## Tecnologias

- **Java 21**: O sistema é construído usando Java 21 com Spring Boot.
- **MySQL**: Utilizado como banco de dados padrão para armazenamento de dados.
- **RabbitMQ**: Utilizado para gerenciamento de filas de pedidos.
- **@Async**: Utilizado para requisições assíncronas à fila, garantindo melhor desempenho e escalabilidade.
- **SOLID**: Princípios de design SOLID foram aplicados para garantir um código limpo, modular e de fácil manutenção.

## Soluções Propostas

- **Protocolos**: Usar protocolos de comunicação apropriados para integração com produtos externos.
- **Boas Práticas**: Seguir as melhores práticas em desenvolvimento de software e design de sistemas.
- **Escalabilidade**: Projetar o sistema para escalar horizontalmente e lidar com altos volumes de pedidos.
- **Consistência**: Implementar estratégias para garantir a consistência dos dados e lidar com acesso concorrente.

## Entregáveis

- **Implementação do Código**: O código pode ser apresentado durante a entrevista ou disponibilizado em um repositório Git.
- **Instância em Execução**: O serviço deve estar rodando na máquina local com exemplos de integrações com os produtos externos A e B.
- **Design da Solução**: Fornecer um design final da solução proposta, incluindo quaisquer ajustes feitos durante o desenvolvimento.

## Começando

### Pré-requisitos

- Docker
- Docker Compose

### Instalação e Execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/seuusuario/servico-gerenciamento-pedidos.git
    ```
2. Acesse o diretório do projeto:
3. Execute o comando:
   ```bash
   docker-compose up --build -d
   ```
4. O serviço estará disponível em `http://localhost:8080`.
5. O swagger estará disponível em `http://localhost:8080/swagger-ui.html`.
6. O Enpoint para integração com o produto externo A estará disponível em `http://localhost:8080/api/v1/producer/send/order`.
6.1. O Payload para integração com o produto externo A é:
```json
{
   "id": "order-123",
   "idempotencyKey": "idemp-key-123",
   "products": [
      {
         "id": "product-1",
         "name": "Notebook",
         "price": 120.50
      },
      {
         "id": "product-2",
         "name": "Notebook-2",
         "price": 120.50
      }
   ]
}
```
```
+-------------------+
|  Produto Externo A |
|  (Envia Pedidos)   |
+-------------------+
         |
         v
+-------------------+
|  Serviço Order    |
|  (Spring Boot)    |
+-------------------+
        |
        v
+-------------------+
|  RabbitMQ         |
|  (Fila de Pedidos)|
+-------------------+
        |
        v
+-------------------+
|  @Async           |
|  (Processamento   |
|   Assíncrono)     |
+-------------------+
         |
         V 
+-------------------+
|  MySQL            |
|  (Armazenamento)  |
+-------------------+
         |
         v
+-------------------+
|  Produto Externo B|
|  (Recebe Pedidos) |
+-------------------+
```