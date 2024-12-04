# Use Maven with Java 21 for building and running tests
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

# Use OpenJDK 21 for running the application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/receipt-processor-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

