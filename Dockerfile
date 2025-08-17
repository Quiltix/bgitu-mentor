# --- СТАДИЯ 1: СБОРКА ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# --- СТАДИЯ 2: ЗАПУСК ---
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Duser.timezone=Europe/Moscow", "-jar", "app.jar"]