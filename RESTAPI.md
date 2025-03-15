
---

### **`restapi.md`**
```markdown
# REST API Documentation

## Overview
The **REST API** provides standard HTTP-based access to the Streaming Platform Catalog Service. It supports CRUD operations for movies, series, seasons, and episodes.

## Base URL
- `http://localhost:8082/api/v1`

## Endpoints

### Movies

#### Get All Movies
```sh
GET /api/v1/movies
```

```json
[
  {
    "id": 1,
    "title": "Inception",
    "releaseYear": 2010
  }
]

```
#### Get Movie By ID 
```sh
GET /api/v1/movies/{id}
```

```json
{
  "id": 1,
  "title": "Inception",
  "releaseYear": 2010
}


#### Create a Movie
```sh 
POST /api/v1/movies
Content-Type: application/json
```

```sh 
{
  "id": 2,
  "title": "New Movie",
  "releaseYear": 2023
}
```

## Update a movie 
```sh 
PUT /api/v1/movies/{id}
Content-Type: application/json
```

```json

{
  "title": "Updated Movie Title"
}
```

## Delete a movie 
```sh
DELETE /api/v1/movies/{id}
```
```json
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
```sh
Authorization: Bearer YOUR_ACCESS_TOKEN
```

## Pagination & Filtering
Pagination: Supports size and page parameters.
Sorting: Use sort=field,asc or sort=field,desc.
Example:

```sh 

GET /api/v1/movies?page=1&size=10&sort=releaseYear,desc
Error Handling
Standard error format:
```

```json
{
  "timestamp": "2024-03-15T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied"
}
```




## API Documentation
### Swagger UI: http://localhost:8082/swagger-ui
### OpenAPI JSON Spec: http://localhost:8082/v3/api-docs


## Further Reading
Spring REST Documentation
OpenAPI Specification













