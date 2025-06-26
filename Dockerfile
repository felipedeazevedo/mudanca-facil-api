# Etapa de build usando Maven + Java 21
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de produção com Java 21 slim
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
