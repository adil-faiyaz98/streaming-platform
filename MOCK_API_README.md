# Streaming Platform Mock API

Since we're having issues setting up the actual services, I've created a simple mock API that simulates the behavior of our streaming platform. This mock API runs entirely in your browser, so it doesn't require any server setup.

## How to Use the Mock API

1. Simply open the `mock-api.html` file in your web browser.
2. You'll see a user interface with buttons to test different API endpoints.

## Test Credentials

The mock API includes two pre-configured users:

### Admin User
- **Username**: admin
- **Password**: admin123
- **Roles**: ROLE_ADMIN, ROLE_USER

### Regular User
- **Username**: user
- **Password**: user123
- **Roles**: ROLE_USER

## Available Features

### Authentication
- Login with username and password to get a mock JWT token

### Catalog API
- Get all movies
- Get a specific movie by ID

### Recommendation API
- Get personalized recommendations for a user
- Get similar content recommendations
- Get trending content recommendations

### User Interactions
- Record a rating for a movie
- Record a view for a movie

## How It Works

This mock API simulates the behavior of our streaming platform using JavaScript in the browser:

1. It stores a small set of sample movies
2. It tracks user interactions (ratings, views)
3. It generates recommendations based on those interactions
4. It simulates authentication with mock JWT tokens

## Limitations

Since this is a simple mock, it has several limitations:

1. Data is not persistent (refreshing the page will reset everything)
2. It only includes a small subset of the actual API functionality
3. The recommendation algorithms are simplified versions of what would be in the real system
4. There's no actual security (the JWT tokens are just for demonstration)

## Next Steps

Once you have the proper environment set up (Java, Docker, etc.), you can proceed with setting up the actual services as described in the main documentation.
