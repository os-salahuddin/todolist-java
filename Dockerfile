# Base image: Java 17 JDK with Maven
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Build the application using Maven, skipping tests
RUN mvn clean package -DskipTests

# ---- Second stage: run the application ----
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR from the first stage
COPY --from=build /app/target/todo-0.0.1-SNAPSHOT.jar todo-app.jar

# Expose the port that Spring Boot will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "todo-app.jar"]
