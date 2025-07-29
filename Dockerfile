# Use a Maven base image for development
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy the Maven configuration and source code
COPY pom.xml .
COPY src ./src

# Run the application in development mode
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.arguments=--spring.devtools.restart.enabled=true"]