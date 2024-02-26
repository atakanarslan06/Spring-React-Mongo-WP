FROM openjdk:17-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ./target/chatapp-0.0.1-SNAPSHOT.jar app.jar
ADD ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/app.jar"]