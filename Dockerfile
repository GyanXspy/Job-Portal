# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files (leverage Docker cache)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Pre-fetch dependencies for faster builds
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Set server port (change Spring Boot port to 3333)
ENV SERVER_PORT=3333

# Expose port 3333
EXPOSE 3333

# Run the application
CMD ["java", "-jar", "target/candlesty-0.0.1-SNAPSHOT.jar"]
