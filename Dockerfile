# BUILD JAR PACKAGE
# Dockerfile for building a Java application using Maven and Amazon Corretto 21 (JDK 21)
FROM maven:3.9.9-amazoncorretto-21-debian AS build

# Set the working directory in the container to /app
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package -DskipTests

#BUILD DOCKER IMAGE
# Use Amazon Corretto 21 as the base image for the runtime
FROM amazoncorretto:21-al2023-jdk

# Set the working directory in the container to /app
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Set the command to run the application
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]