
# REST API Documentation

## Overview
The **REST API** provides standard HTTP-based access to the Streaming Platform Catalog Service. It supports CRUD operations for movies, series, seasons, and episodes.

## Base URL
- `http://localhost:8082/api/v1`

## Endpoints
- Upon running the application locally, all endpoints would be visible on SwaggerAPI with sample requests and responses
  `http://localhost:8082/swagger-api/index.html`

### Movies

#### Get All Movies
```bash
GET /api/v1/movies
```

```bash
[
  {
    "id": 1,
    "title": "Inception",
    "releaseYear": 2010
  }
]
```
#### Get Movie By ID 

```bash
GET /api/v1/movies/{id}
```

```bash
{
  "id": 1,
  "title": "Inception",
  "releaseYear": 2010
}
```


#### Create a Movie

```bash 
POST /api/v1/movies
Content-Type: application/json
```

```bash
{
  "id": 2,
  "title": "New Movie",
  "releaseYear": 2023
}
```

## Update a movie 
```bash
PUT /api/v1/movies/{id}
Content-Type: application/json
```

```bash
{
  "title": "Updated Movie Title"
}
```

## Delete a movie

```bash
DELETE /api/v1/movies/{id}
```
```bash
{
  "message": "Movie deleted successfully"
}
```

## Authentication & Security
#### OAuth2 Authentication: Required for all API endpoints.

Scopes:
SCOPE_read → Read operations
SCOPE_write → Create/update operations
SCOPE_delete → Deletion operations

Example Header:

```bash
Authorization: Bearer YOUR_ACCESS_TOKEN
```


## API Documentation
### Swagger UI: http://localhost:8082/swagger-ui
### OpenAPI JSON Spec: http://localhost:8082/api-docs


## Further Reading
Spring REST Documentation
OpenAPI Specification













