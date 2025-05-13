FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the full source only after dependencies are cached
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Set the entry point
CMD ["java", "-jar", "target/your-app.jar"]
