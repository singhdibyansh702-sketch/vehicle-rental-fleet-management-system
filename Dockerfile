# Multi-stage build for Spring Boot application with Java 25
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Install maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build production jar skipping tests
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy built artifact
COPY --from=build /app/target/vehicle-rental-system-1.0.0.jar app.jar

# Expose server port
EXPOSE 8081

# Run Spring Boot app
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8081}", "-jar", "app.jar"]
