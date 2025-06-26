Necessário instalar:

JDK 21
Maven 3.9.4
Docker Desktop

Para baixar as dependências do projeto, execute

mvn clean install

Swagger (Documentação da API com todos os endpoints e body's das requisições): 

http://localhost:8080/swagger-ui/index.html

Neste projeto, temos testes unitário e testes de mutação.

Testes Unitários:
São testes que verificam o comportamento isolado de uma classe ou método, sem depender de componentes externos como banco de dados, APIs ou outras classes.

Tecnologias:

JUnit: biblioteca principal para criar testes em Java.

Mockito: usado para simular (mockar) comportamentos de dependências.

AssertJ: ajudam a escrever asserções mais legíveis.

Testes de Mutação:
São testes que verificam a qualidade dos testes unitários. O mecanismo:

Modifica levemente o código (ex: troca == por !=, remove if, etc.)

Roda os testes para ver se detectam o erro (mutação).

Se o teste não falha, o mutante “sobreviveu” — e o teste é considerado fraco.

Tecnologia:

PITEST: ferramenta de teste de mutação mais usada no ecossistema Java.