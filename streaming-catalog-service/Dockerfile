# --- Stage 1: Build Application ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Gradle Wrapper and Build Files
COPY gradlew .
COPY gradle gradle/
COPY settings.gradle .
COPY build.gradle .
COPY streaming-catalog-service/build.gradle streaming-catalog-service/

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# Copy the microservice source code
COPY streaming-catalog-service ./streaming-catalog-service

# Copy the GraphQL schema directory before building the JAR
COPY streaming-catalog-service/src/main/resources/schema ./streaming-catalog-service/src/main/resources/schema

# Build the JAR (without running tests)
RUN ./gradlew :streaming-catalog-service:bootJar -x test

# --- Stage 2: Run Application ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/streaming-catalog-service/build/libs/*.jar app.jar

# Copy the GraphQL schema into the runtime container
COPY --from=builder /app/streaming-catalog-service/src/main/resources/schema /app/resources/schema

# Set active profile to 'docker' (ensures application-docker.yml is used)
ENV SPRING_PROFILES_ACTIVE=docker

# Expose application port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
