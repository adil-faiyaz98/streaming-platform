# Testing the Streaming Platform

This document provides instructions for testing the streaming platform locally.

## Prerequisites

- Java 17 or later
- PostgreSQL
- Redis (optional, for caching)

## Starting the Services

### Option 1: Using Gradle

1. Start PostgreSQL and create the required databases:
   ```
   createdb streaming_platform_db
   createdb recommendation_db
   ```

2. Load the test data:
   ```
   psql -f generate-test-data.sql
   ```

3. Start the catalog service:
   ```
   cd streaming-catalog-service
   ./gradlew bootRun
   ```

4. In a new terminal, start the recommendation service:
   ```
   cd recommendation-service
   ./gradlew bootRun
   ```

### Option 2: Using Docker (if available)

```
docker-compose up -d
```

## Service URLs

- **Catalog Service**: http://localhost:8082
- **Recommendation Service**: http://localhost:8083

## API Documentation

- **Catalog Service Swagger UI**: http://localhost:8082/swagger-ui.html
- **Recommendation Service Swagger UI**: http://localhost:8083/swagger-ui.html

## Test Credentials

### Admin User
- **Username**: admin
- **Password**: admin123
- **Roles**: ROLE_ADMIN, ROLE_USER

### Regular User
- **Username**: user
- **Password**: user123
- **Roles**: ROLE_USER

## Testing the Authentication

To get a JWT token:

```
curl -X POST http://localhost:8082/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Use the returned token in subsequent requests:

```
curl -X GET http://localhost:8082/api/v1/catalog/movies \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Testing Recommendations

1. First, authenticate to get a token as shown above.

2. Get personalized recommendations for a user:
   ```
   curl -X GET "http://localhost:8082/api/v2/movies/1/recommendations?userId=admin" \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

3. Get similar content recommendations:
   ```
   curl -X GET "http://localhost:8082/api/v2/movies/1/recommendations" \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

4. Get trending content directly from the recommendation service:
   ```
   curl -X GET "http://localhost:8083/api/v1/recommendations/trending" \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

## Recording User Interactions

To improve recommendations, record user interactions:

```
curl -X POST "http://localhost:8083/api/v1/interactions/rating" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "admin",
    "itemId": "1",
    "itemType": "MOVIE",
    "value": 5.0
  }'
```
