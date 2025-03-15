

# **`graphql.md`**
```markdown
# GraphQL API Documentation

## Overview
The **GraphQL API** provides flexible and efficient data retrieval for the Streaming Platform Catalog Service. Clients can query only the fields they need, reducing over-fetching and improving performance.

## API Endpoint
- **GraphQL Playground:** `http://localhost:8082/graphql`
- **GraphiQL (Development Only):** `http://localhost:8082/graphiql`

## Queries

### Fetch a Movie by ID
```graphql
query {
  movie(id: 1) {
    title
    releaseYear
    genres
    episodes {
      title
      duration
    }
  }
}
```
### List All Sales 
```sh
query {
  series {
    id
    title
    seasons {
      seasonNumber
      episodes {
        title
      }
    }
  }
}
```

## Mutations 
### Create a new episode 
mutation {
  createEpisode(seasonId: 2, episodeInput: {
    title: "New Episode"
    duration: 45
  }) {
    id
    title
    duration
  }
}
```

### Update a movie
mutation {
  updateMovie(id: 1, movieInput: {
    title: "Updated Title"
    releaseYear: 2023
  }) {
    id
    title
  }
}
```

## Authentication & Authorization
All GraphQL queries and mutations require authentication.
### Uses OAuth2 scopes:
SCOPE_read for queries
SCOPE_write for mutations

## Performance Optimization
DataLoader is used to prevent N+1 query issues.
Batch Loaders improve database efficiency.
Rate limiting can be configured in application.yml.


## Error Handling
GraphQL follows a standard error response format:


```sh
{
  "errors": [
    {
      "message": "Unauthorized",
      "extensions": {
        "code": "UNAUTHORIZED"
      }
    }
  ]
}
Further Reading
*GraphQL Specification*
*Spring GraphQL Documentation*
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
```http
GET /api/v1/movies
Response:

json
Copy code
[
  {
    "id": 1,
    "title": "Inception",
    "releaseYear": 2010
  }
]
Get a Movie by ID
http
Copy code
GET /api/v1/movies/{id}
Response:

json
Copy code
{
  "id": 1,
  "title": "Inception",
  "releaseYear": 2010
}
Create a Movie
http
Copy code
POST /api/v1/movies
Content-Type: application/json
Request Body:

json
Copy code
{
  "title": "New Movie",
  "releaseYear": 2023
}
Response:

json
Copy code
{
  "id": 2,
  "title": "New Movie",
  "releaseYear": 2023
}
Update a Movie
http
Copy code
PUT /api/v1/movies/{id}
Content-Type: application/json
Request Body:

jsonyaml
```


