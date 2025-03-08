# Streaming Catalog Service

A microservice that manages the catalog of movies, TV series, seasons, and episodes for a streaming platform.

## Features

- CRUD operations for movies, series, seasons, and episodes
- RESTful API endpoints for all resources
- GraphQL API for flexible queries
- Search and filter functionality by title, genre, rating, etc.
- Pagination and sorting support
- Caching with Redis for improved performance
- PostgreSQL database for persistence
- Flyway for database migrations
- OpenAPI documentation
- Docker support for easy deployment

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring GraphQL
- PostgreSQL
- Redis (for caching)
- Flyway (for DB migrations)
- MapStruct
- Docker & Docker Compose
- OpenAPI (Swagger)

## API Documentation

### RESTful Endpoints

| HTTP Method | Endpoint                                 | Description                   |
|-------------|------------------------------------------|-------------------------------|
| GET         | /api/v1/movies                           | Get all movies (paginated)    |
| GET         | /api/v1/movies/{id}                      | Get movie by ID               |
| GET         | /api/v1/movies/search?title={title}      | Search movies by title        |
| GET         | /api/v1/movies/genre/{genre}             | Get movies by genre           |
| GET         | /api/v1/movies/top-rated                 | Get top-rated movies          |
| POST        | /api/v1/movies                           | Create a new movie            |
| PUT         | /api/v1/movies/{id}                      | Update a movie                |
| DELETE      | /api/v1/movies/{id}                      | Delete a movie                |
| GET         | /api/v1/tv-shows                         | Get all TV shows (paginated)  |
| GET         | /api/v1/tv-shows/{id}                    | Get TV show by ID             |
| POST        | /api/v1/tv-shows                         | Create a new TV show          |
| PUT         | /api/v1/tv-shows/{id}                    | Update a TV show              |
| DELETE      | /api/v1/tv-shows/{id}                    | Delete a TV show              |
| GET         | /api/v1/tv-shows/{tvShowId}/seasons      | Get seasons for a TV show     |
| GET         | /api/v1/seasons/{id}                     | Get season by ID              |
| POST        | /api/v1/tv-shows/{tvShowId}/seasons      | Create a new season           |
| PUT         | /api/v1/seasons/{id}                     | Update a season               |
| DELETE      | /api/v1/seasons/{id}                     | Delete a season               |
| GET         | /api/v1/seasons/{seasonId}/episodes      | Get episodes for a season     |
| GET         | /api/v1/episodes/{id}                    | Get episode by ID             |
| POST        | /api/v1/seasons/{seasonId}/episodes      | Create a new episode          |
| PUT         | /api/v1/episodes/{id}                    | Update an episode             |
| DELETE      | /api/v1/episodes/{id}                    | Delete an episode             |

### GraphQL API

GraphQL endpoint: `/graphql`

Example queries:
```graphql
# Get a movie by ID
query {
  getMovie(id: 1) {
    id
    title
    description
    releaseYear
    genres
    director
    averageRating
  }
}

# Get paginated list of movies
query {
  getMovies(page: 0, size: 10) {
    content {
      id
      title
      description
    }
    totalElements
    totalPages
    size
    number
  }
}

# Create a new movie
mutation {
  createMovie(movieInput: {
    title: "The Matrix"
    description: "A computer hacker learns about the true nature of reality"
    releaseYear: 1999
    genres: ["Action", "Sci-Fi"]
    director: "Wachowski Sisters"
    duration: 136
  }) {
    id
    title
    averageRating
  }
}
```

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.6+ or Docker

### Using Maven
```bash
git clone https://github.com/adil-faiyaz98/streaming-platform.git
cd streaming-platorm
```