# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim as build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml (or build.gradle) and the source code to the container
COPY pom.xml .

# Download the dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a smaller OpenJDK image for the runtime
FROM openjdk:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage to the container
COPY --from=build /app/target/*.jar /app/app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
