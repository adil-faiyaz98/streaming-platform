

# GraphQL API Documentation

## Overview
The **GraphQL API** provides flexible and efficient data retrieval for the Streaming Platform Catalog Service. Clients can query only the fields they need, reducing over-fetching and improving performance.

## API Endpoint
- **GraphQL Playground:** `http://localhost:8082/graphql`
- **GraphiQL (Development Only):** `http://localhost:8082/graphiql`

## Queries

### Fetch a Movie by ID
```bash
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
```bash
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
```bash
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
```bash
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


```bash
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
```
Further Reading
*GraphQL Specification*
*Spring GraphQL Documentation*
