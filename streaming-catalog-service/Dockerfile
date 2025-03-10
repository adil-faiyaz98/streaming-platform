# --- Stage 1: Build Application ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Gradle files for caching dependencies
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies --no-daemon

# Copy application source
COPY src ./src

# Build the executable JAR without running tests
RUN ./gradlew clean bootJar -x test --no-daemon

# --- Stage 2: Run Application ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy built JAR file
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8082

# Start application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
