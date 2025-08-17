FROM openjdk:17-jdk-slim


WORKDIR /app

ARG APP_JAR=app.jar

COPY ${APP_JAR} app.jar

ENTRYPOINT ["java", "-Duser.timezone=Europe/Moscow", "-jar", "app.jar"]
