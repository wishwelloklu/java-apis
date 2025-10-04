# Use a Maven base image for development
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy the Maven configuration and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Run the application in development mode
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.arguments=--spring.devtools.restart.enabled=true"]