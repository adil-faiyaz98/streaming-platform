
```markdown
# Streaming Catalog Service

## Overview

The Streaming Catalog Service is a Spring Boot application that provides a catalog of series, seasons, and episodes.

## Prerequisites

- Java 21
- Gradle
- Docker 

## Getting Started

### Clone the repository

```sh
git clone https://github.com/adil-faiyaz98/streaming-platform.git
cd streaming-catalog-service
```

### Build the project

```sh
./gradlew :streaming-catalog-service:clean :streaming-catalog-service:build
```

### Run the application

```sh
./gradlew :streaming-catalog-service:bootRun
```

### Run tests ( NOT )

```sh
./gradlew test
```

## Docker

### Build Docker image

```sh
docker build -t streaming-catalog-service .
```

### Run Docker container

```sh
docker run -p 8080:8080 streaming-catalog-service
```

## Configuration

### Application.yml file


### Database

The application uses PostgreSQL as the primary database

## Endpoints

### Series

- `GET /series` - Get all series
- `GET /series/{id}` - Get series by ID
- `POST /series` - Create a new series
- `PUT /series/{id}` - Update a series
- `DELETE /series/{id}` - Delete a series

### Seasons

- `GET /seasons` - Get all seasons
- `GET /seasons/{id}` - Get season by ID
- `POST /seasons` - Create a new season
- `PUT /seasons/{id}` - Update a season
- `DELETE /seasons/{id}` - Delete a season

### Episodes

- `GET /episodes` - Get all episodes
- `GET /episodes/{id}` - Get episode by ID
- `POST /episodes` - Create a new episode
- `PUT /episodes/{id}` - Update an episode
- `DELETE /episodes/{id}` - Delete an episode

## License

This project is licensed under the MIT License.
```

