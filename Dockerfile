# Base image: Java 17 JDK with Maven
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Copy the source code to the container
COPY . .

# Build the application using Maven
RUN mvn clean install -X

# ---- Second stage: run the application ----
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR from the first stage
COPY --from=build /app/target/todo-app.jar /app/todo-app.jar

# Expose the port that Spring Boot will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/todo-app.jar"]

CMD ["mvn", "spring-boot:run"]
