# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 as builder

# Set work directory
WORKDIR /app

# Copy pom and download dependencies first (better cache usage)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy all source files
COPY . .

RUN mvn vaadin:build-frontend

# Build app and skip tests to speed up
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jdk-jammy

# Set work directory
WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]




