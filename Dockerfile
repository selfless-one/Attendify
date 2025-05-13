FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory in the container
WORKDIR /app

# Copy pom.xml to the container
COPY pom.xml .

# Download the dependencies offline
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY . .

# Build the application
RUN mvn clean package

# Set the entry point for your application
CMD ["java", "-jar", "target/your-app.jar"]
