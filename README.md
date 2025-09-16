🚀 Projeto Mudança Fácil — API

📦 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:

JDK 21

Maven 3.9.4

Docker Desktop

⚙️ Configuração do projeto

Para baixar as dependências e compilar o projeto:

mvn clean install

📖 Documentação da API

A documentação está disponível via Swagger, incluindo todos os endpoints e exemplos de requisição/resposta:

👉 http://localhost:8080/swagger-ui/index.html

✅ Testes

O projeto inclui testes unitários e testes de mutação para garantir a qualidade do código.

🔹 Testes Unitários

Validam o comportamento isolado de classes e métodos.

Não dependem de banco de dados, APIs externas ou outros módulos.

Tecnologias utilizadas:

JUnit → biblioteca principal para criação de testes em Java.

Mockito → simulação de dependências (mocks).

AssertJ → escrita de asserções mais fluídas e legíveis.

🔹 Testes de Mutação

Avaliam a qualidade dos testes unitários.

Funcionam modificando o código (ex.: trocar == por !=, remover if, etc.).

Se os testes não falharem, o mutante sobrevive → sinal de teste fraco.

Tecnologia utilizada:

PITEST → ferramenta mais popular de mutação no ecossistema Java.

🛠️ Tecnologias do projeto

Java 21

Spring Boot (se for o caso, você pode adicionar aqui)

Maven

JUnit / Mockito / AssertJ

PITEST

Docker