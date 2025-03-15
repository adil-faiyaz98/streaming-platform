# Streaming Platform Catalog Service

## Overview
The **Streaming Platform Catalog Service** is a microservice responsible for managing movies, series, seasons, and episodes. It provides both **GraphQL and REST APIs**, allowing flexible data retrieval for different clients.

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
