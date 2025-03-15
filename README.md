# Streaming Catalog Microservice

## Overview

The `streaming-catalog-microservice` is responsible for managing and serving metadata about movies, TV shows, and other streaming content. It provides a microservice containing endpoints and optionally supports GraphQL for flexible querying.

## Features

- RESTful API for retrieving catalog information
- Optional GraphQL support for flexible querying
- Integration with PostGreSQL or MongoDB for data storage
- Caching with Redis for performance optimization ( Actually, Cache doesn't implement good on GraphQL due to queries in GraphQL are dynamic and can't be cached)

## Prerequisites

Ensure you have the following installed:

- Java 17+
- Gradle
- Docker & Docker Compose
- PostgreSQL or MongoDB

## Setup

### Clone the Repository

```bash
git clone https://github.com/adil-faiyaz98/streaming-platform.git
cd streaming-platform
```

### Build the Application

```bash
./gradlew :streaming-catalog-service:clean :streaming-catalog-service:build :streaming-catalog-service:bootRun
```

### Run with Docker

```bash
docker-compose up -d
```

```bash
curl -X GET http://localhost:8080/api/movies
```

#### Get a Movie by ID

```bash
curl -X GET http://localhost:8080/api/movies/{id}
```

### GraphQL API

If GraphQL is enabled, access it via:

```bash
http://localhost:8082/api-docs
http://localhost:8082/swagger-ui.html
```

Example Query:

```graphql
query {
  movie(id: "123") {
    title
    director
    releaseYear
  }
}
```

## PS
I initially planned an AI-powered test generation by training on the repository and thought of implementing a microservice through GraphQL. The models required for the training I wanted would need high computational GPU servers (A100/H100) and significant time, making local training infeasible.

So, just putting this as an example as a reference for GraphQL-based API implementation. Negatives are more than the positives.
- Clients can send nested queries which can lead to performance issues
- Clients can send multiple queries in a single request which overload the server
- GraphQL queries are not cached easily due to their dynamic nature and custom caching logic is required
- Each query could lead to multiple database calls, in addition to resolvers, which can lead to performance issues

- Unlike REST, they can take a large amount of data in one go which can expose the system to DDOS attacks / harder to have rate limiting
- They introduce resolvers which can lead high latency,
- Architecture complexity which is unnecessary. 
- Since each field needs a resolver, a single request can hit multiple resolvers which adds alot of overhead. 
- RBA (Role Based Access) is harder to implement in GraphQL

- Overkill for simple APIs
- Has higher latency than REST due to the need for resolvers and likelyhood of multiple database calls
- Lower throughput than REST

## ------------------------------------------------

- There are benefits to using GraphQL, such as:
    - Clients can request only the data they need ( preventing over-fetching and under-fetching )
    - Clients can request multiple resources in a single query ( preventing multiple round trips )
    - Clients can request nested resources
    - Clients can request related resources in a single query 
    - Clients can request multiple resources in a single query 



If someone wants to experiment to build or just simply gain AI skills, they could build microservices on top 
- User Management Service
- Payment Service
- Subscription Service
- Content Recommendation Service - AI-based Content Recommendation Service through AutoEncoders / Transformers / RL
- AI-based Churn Prediction Service - AI-based Churn Prediction through Gradient Boosting ( XGBoost / LightGBM )

## License
This project is licensed under the MIT License.