## GraphQL Microservices Backend
### Java | Spring Boot | GraphQL (Netflix DGS Framework)

This project `streaming-platform` consists of multiple microservices designed and developed using Java, Spring Boot, and Netflix's Domain Graph Service (DGS) GraphQL framework. 

---

## Overview

The backend is structured as modular microservices, each responsible for specific functionality:

- **streaming-catalog-service** --> This is the only implemented service as of now.

The following microservices can be practiced later
- **user-profile-service**
- **subscription-billing-service**
- **watchlist-recommendation-service**
- **analytics-service**
- **notification-service**
- **content-search-service**
- **review-rating-service**

Each microservice exposes GraphQL APIs, ensuring efficient querying and manipulation of data.

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
### 2. User Profile & Authentication Service

**Functionality:** Manages user authentication, registration, profiles, and preferences.

**GraphQL Endpoints:**

- Mutation: login(credentials: AuthInput!): AuthPayload
- Mutation: registerUser(details: UserRegistrationInput!): User
- Mutation: updateProfile(userId: ID!, input: ProfileInput!): Profile
- Query: getUserSettings(userId: ID!): UserSettings
- Mutation: changePassword(userId: ID!, newPassword: String!): Status

### 3. Subscription & Billing Service

**Functionality:** Handles subscription plans, billing, and payment processing.

**GraphQL Endpoints:**

- Query: getSubscriptionDetails(userId: ID!): Subscription
- Mutation: updatePaymentMethod(userId: ID!, paymentInput: PaymentInput!): PaymentMethod
- Mutation: subscribeToPlan(userId: ID!, planId: ID!): Subscription
- Mutation: cancelSubscription(userId: ID!): Status
- Query: billingHistory(userId: ID!, limit: Int): [BillingRecord]

### 4. Watchlist & Recommendations Service

**Functionality:** Manages user watchlists and personalized recommendations.

**GraphQL Endpoints:**

- Query: getWatchlist(userId: ID!): [ContentItem]
- Mutation: addToWatchlist(userId: ID!, contentId: ID!): Watchlist
- Mutation: removeFromWatchlist(userId: ID!, contentId: ID!): Status
- Query: getPersonalizedSuggestions(userId: ID!): [ContentItem]

### 5. Analytics & Metrics Service

**Functionality:** Provides analytics on content viewership, user engagement, and global trends.

**GraphQL Endpoints:**

- Query: getContentStats(contentId: ID!): ContentStats
- Query: getUserEngagementStats(userId: ID!): UserEngagement
- Mutation: trackContentPlay(userId: ID!, contentId: ID!): PlayEvent
- Query: getGlobalTrending(limit: Int): [ContentItem]

### 6. Notification Service

**Functionality:** Delivers notifications for content updates, billing, and personalized alerts.

**GraphQL Endpoints:**

- Query: getNotifications(userId: ID!): [Notification]
- Mutation: sendNotification(input: NotificationInput!): NotificationStatus
- Mutation: markNotificationAsRead(notificationId: ID!): Status
- Mutation: updateNotificationPreferences(userId: ID!, input: PreferencesInput!): Preferences

### 7. Content Review & Rating Service

**Functionality:** Enables users to review and rate content.

**GraphQL Endpoints:**

- Mutation: submitReview(userId: ID!, contentId: ID!, review: ReviewInput!): Review
- Mutation: updateReview(reviewId: ID!, input: ReviewUpdateInput!): Review
- Query: getReviews(contentId: ID!, filter: ReviewFilter!): [Review]
- Query: getAverageRating(contentId: ID!): Float

### 8. Content Search Service

**Functionality:** Facilitates advanced content search with various filters and sorting options.

**GraphQL Endpoints:**

- Query: searchContent(query: String!, filter: SearchFilter): [SearchResult]
- Query: getRecentSearches(userId: ID!): [SearchHistory]
- Mutation: deleteSearchHistory(userId: ID!): Status

---

## Testing Approach

### API Testing (REST Assured)

All GraphQL endpoints can be tested thoroughly through REST Assured, verifying responses, schema correctness, error handling, and boundary conditions.

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