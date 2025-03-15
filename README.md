# Streaming Platform Catalog Service

## Overview
The **Streaming Platform Catalog Service** is a microservice responsible for managing movies, series, seasons, and episodes. It provides both **GraphQL and REST APIs**, allowing flexible data retrieval for different clients.

## Available APIs
- **[GraphQL API Documentation](GRAPHQL.md)**
- **[REST API Documentation](RESTAPI.md)**

## Features
- **GraphQL API** for efficient, flexible queries.
- **REST API** for standard HTTP-based integrations.
- **PostgreSQL Database** for persistence.
- **Spring Boot** backend with Spring Data JPA.
- **Dockerized Deployment** with multi-stage builds.
- **Authentication & Authorization** using Spring Security.
- **Batch Loading with DataLoader** to optimize GraphQL queries.


## Tech Stack
- **Backend:** Java 17, Spring Boot, Spring GraphQL, Spring Security
- **Database:** PostgreSQL with JPA/Hibernate
- **APIs:** GraphQL and REST
- **Build & Deployment:** Gradle, Docker, GitHub Actions CI/CD

## Getting Started

### Prerequisites
- Java 17+
- Gradle
- Docker & Docker Compose
- PostgreSQL

### Running Locally

#### Clone the Repository
```sh
git clone https://github.com/your-repo/streaming-platform.git
cd streaming-platform
```


## Start Database with Docker 
```sh
docker-compose up -d
```

## Build and run the application 
```sh
./gradlew bootRun
```


## Access the APIs
### GraphQL Playground: 
```sh
http://localhost:8082/graphql
```


### REST API Base URL: 
```sh
http://localhost:8082/api/v1
```


### Swagger API Docs: 
```sh
http://localhost:8082/swagger-ui
```

### Configuration
Modify _application.yml_ for environment-specific configurations, including:

Database connection settings
Security credentials
API rate limits

## Build a Docker Image 
```sh
docker build -t streaming-catalog .
```

## Run the container
```sh
docker run -p 8082:8082 streaming-catalog

## License
The project is licensed under the MIT license 
