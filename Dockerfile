# Stage 1 - Build Stage
FROM --platform=linux/amd64 maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /src

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2 - Runtime Stage
FROM --platform=linux/amd64 eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /src/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

