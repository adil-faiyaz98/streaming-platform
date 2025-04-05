# Build stage
FROM gradle:8.3-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create directory for models
RUN mkdir -p /app/models

# Copy the built JAR file
COPY --from=build /app/build/libs/recommendation-service.jar /app/recommendation-service.jar

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xms512m -Xmx1g"

# Expose the application port
EXPOSE 8083

# Run the application
ENTRYPOINT ["java", "-jar", "/app/recommendation-service.jar"]
