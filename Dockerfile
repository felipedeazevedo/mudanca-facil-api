# Usa imagem Java 21 com suporte a JAR
FROM eclipse-temurin:21-jdk

# Define o diretório da aplicação
WORKDIR /app

# Copia o JAR gerado para o container
COPY target/mudancafacil-*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
