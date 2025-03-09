## GraphQL Microservices Backend
### Java | Spring Boot | GraphQL (Netflix DGS Framework)

This project `streaming-platform` consists of multiple microservices designed and developed using Java, Spring Boot, and Netflix's Domain Graph Service (DGS) GraphQL framework. 

---

## Overview

- **streaming-catalog-service** 

---

## Technology Stack

### Backend

- Java (JDK 21)
- Spring Boot (v3.x)
- Netflix DGS Framework (GraphQL)
- Spring Data JPA/Hibernate
- PostgreSQL (database)
- Gradle (build management)

### Testing

- REST Assured (API endpoint testing)
- JUnit 5 (unit and integration tests)

### Continuous Integration & Deployment (CI/CD)

- GitHub Actions
- Docker
---

## Microservices and API Endpoints

### 1. Streaming Catalog Service

**Functionality:** Manages the catalog of movies, series, and episodes.

**GraphQL Endpoints:**

- Query: getMovies(filter: MovieFilter): [Movie]
- Query: getSeries(id: ID!): Series
- Query: getRecommendations(userId: ID!): [Content]
- Mutation: addContent(input: ContentInput!): Content
- Mutation: updateEpisodeDetails(id: ID!, input: EpisodeInput!): Episode

### --- BELOW IS NOT DONE AND CAN BE IMPLEMENTED LATER BY ANYONE FOR PRACTICE ---

---

## Project Setup & Execution

### Clone Repository
```
git clone https://github.com/adil-faiyaz98/streaming-platform.git
```

### Compile & Package the application
#### Using Gradle Wrapper (Recommended) to ensure consistency across environments
```
cd project-directory
./gradlew clean build
```

### Run Spring Boot Application locally
```
./gradlew bootRun
```

### Execute Tests using Gradle
```
./gradlew test
```

---

### Additional Gradle Commands 
#### Generate Executable JAR
```
./gradlew bootJar
```

#### Clean the build directory 
```
./gradlew clean
```


## Continuous Integration & Deployment
This project supports containerization (Docker) and can be integrated into CI/CD pipelines using GitHub Actions or Jenkins. Kubernetes manifests may be included for scalable deployments.
---

## Documentation & Support
Detailed documentation and further assistance are available in the project wiki or through contacting maintainers directly.
---

## Contributors

- Adil Faiyaz - https://www.linkedin.com/in/adil-faiyaz/

---

## License
This project is licensed under the MIT License. See `LICENSE` for details.
---
