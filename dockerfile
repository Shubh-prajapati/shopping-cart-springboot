# Use Java Base File Name
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Shopping-Cart-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar", "app.jar"]


