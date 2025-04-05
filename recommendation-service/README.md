# AI-Powered Recommendation Service

This microservice provides AI-powered content recommendations for the streaming platform.

## Features

- **Collaborative Filtering**: Recommends content based on user behavior similarities
- **Content-Based Filtering**: Recommends content based on item attributes
- **Hybrid Recommendations**: Combines multiple recommendation strategies
- **Trending Content**: Identifies trending content based on recent interactions
- **Similar Content**: Finds similar content based on attributes

## Architecture

The recommendation service is built as a standalone microservice that communicates with the catalog service via REST APIs. It uses:

- **Spring Boot**: For the application framework
- **PostgreSQL**: For storing user interactions and content metadata
- **Apache Mahout**: For collaborative filtering algorithms
- **DeepLearning4J**: For neural network-based recommendations (optional)
- **Spring Security**: For securing the API endpoints
- **Spring Actuator**: For monitoring and health checks

## API Endpoints

### Recommendations

- `GET /api/v1/recommendations/user/{userId}`: Get personalized recommendations for a user
- `GET /api/v1/recommendations/similar/{itemId}`: Get similar content recommendations
- `GET /api/v1/recommendations/trending`: Get trending content
- `GET /api/v1/recommendations/popular`: Get popular content

### User Interactions

- `POST /api/v1/interactions`: Record a user interaction
- `POST /api/v1/interactions/view`: Record a view interaction
- `POST /api/v1/interactions/rating`: Record a rating interaction
- `POST /api/v1/interactions/watch-time`: Record a watch time interaction

### Content Items

- `GET /api/v1/content-items/{id}`: Get a content item
- `POST /api/v1/content-items`: Save or update a content item

## Recommendation Algorithms

### Collaborative Filtering

Recommends items based on user behavior similarities. Users who have similar viewing or rating patterns are likely to have similar preferences.

### Content-Based Filtering

Recommends items based on item attributes (genres, actors, keywords). Items with similar attributes to those a user has liked are recommended.

### Hybrid Approach

Combines collaborative and content-based filtering to provide more accurate recommendations.

## Data Collection

The service collects the following user interaction data:

- **Views**: When a user views content
- **Ratings**: When a user rates content
- **Watch Time**: How long a user watches content
- **Likes/Dislikes**: User preferences
- **Add to List**: When a user adds content to their watchlist

## Model Training

The recommendation models are trained using:

1. **Batch Processing**: Regular batch jobs to train models
2. **Incremental Updates**: Real-time updates for new interactions
3. **A/B Testing**: Testing different recommendation strategies

## Deployment

The service can be deployed using Docker:

```bash
docker-compose up -d recommendation-service
```

## Configuration

Key configuration options in `application.yml`:

```yaml
recommendation:
  model:
    path: models/
    update-schedule: "0 0 2 * * ?" # 2 AM every day
  features:
    collaborative-filtering: true
    content-based: true
    hybrid: true
  algorithm:
    neighborhood-size: 50
    similarity-threshold: 0.1
```

## Monitoring

The service exposes metrics via Spring Boot Actuator:

- `GET /actuator/health`: Health check
- `GET /actuator/metrics`: Metrics
- `GET /actuator/prometheus`: Prometheus metrics

## Integration with Catalog Service

The catalog service integrates with the recommendation service via the `RecommendationClient`, which provides:

- Circuit breaker pattern for resilience
- Retry mechanism for transient failures
- Fallback recommendations when the service is unavailable
