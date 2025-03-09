# Streaming Catalog Service ( )

## Overview
A GraphQL and REST-based microservice for managing streaming content catalogs including movies, TV shows, series, seasons, and episodes. This service provides a comprehensive API for content management in a streaming platform architecture.

## Technologies Used

### Core Framework
- **Spring Boot 3.x** - Modern Java framework for building production-ready applications
- **Spring Data JPA** - Data access layer with repository pattern
- **Spring Validation** - Input validation

### API Technologies
- **GraphQL** - For flexible, efficient API queries
    - Spring Boot GraphQL starter
    - GraphQL Extended Scalars for advanced data types (DateTime, etc.)
- **REST** - Traditional REST endpoints for CRUD operations
- **OpenAPI/Swagger** - API documentation and testing interface

### Database
- **PostgreSQL** - Relational database for storing catalog data

### Development Tools
- **Lombok** - Reduces boilerplate code (getters, setters, constructors)
- **MapStruct** - Type-safe bean mapping between entities and DTOs
- **Spring Boot DevTools** - Developer productivity tools

### Testing
- **Spring Boot Test** - Testing framework
- **TestContainers** - Integration testing with containerized dependencies
- **JUnit 5** - Test runner

## Features
- GraphQL API for efficient querying of related content
- RESTful CRUD operations for all content types
- Entity relationships (TV Shows → Series → Seasons → Episodes)
- Pagination and sorting support
- Swagger UI for API documentation and testing
- Docker containerization

## API Documentation
- **GraphiQL Interface**: http://localhost:8082/graphiql
- **Swagger UI**: http://localhost:8082/swagger-ui/index.html
- **OpenAPI Specification**: http://localhost:8082/api-docs

## Getting Started

### Prerequisites
- JDK 17 or later
- Docker and Docker Compose (optional)
- PostgreSQL (if running without Docker)

### Running with Docker
```bash
# Start the entire stack (PostgreSQL + Application)
docker-compose up -d
```

### Running locally
```bash
# Build the application
./gradlew :streaming-catalog-service:build

# Run the application
./gradlew :streaming-catalog-service:bootRun
```

## Sample GraphQL Queries

```graphql
# Get all movies with their details
{
  movies {
    id
    title
    description
    releaseDate
    duration
  }
}

# Get a TV show with its series and seasons
{
  tvShow(id: 1) {
    title
    description
    series {
      title
      seasons {
        seasonNumber
        title
        episodes {
          episodeNumber
          title
        }
      }
    }
  }
}
```

## Development Notes
- The service uses MapStruct for object mapping between entities and DTOs
- DateTime values are handled with custom GraphQL scalars
- Entity relationships are managed via JPA annotations

## Deployment
The service can be deployed:
- As a standalone Spring Boot application
- Using Docker/Docker Compose
- On Kubernetes using the provided container

## Contributing
This project is an example microservice showcasing Spring Boot with GraphQL.
