FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/notificador-0.0.1.jar
COPY ${JAR_FILE} app_notificador.jar

EXPOSE 8085
ENTRYPOINT ["java","-jar","/app_notificador.jar"]
