# User API Application

This project is a Spring Boot application that provides an API for creating and retrieving user data. It includes endpoints for user creation and fetching user details based on their ID and access token.

## Features

- Create users with personal details.
- Fetch user details with appropriate security checks.
- SHA1 hashing for user ID generation.
- JWT token generation for access control.
- In-memory H2 database for data persistence.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 8
- Maven

### Installing

To get a development environment running, follow these steps:

1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Run the application using Maven:

mvn spring-boot:run


The application will start and by default will be accessible at `http://localhost:8080`.

### Using the Application

#### Create User

- **Endpoint:** `/user`
- **Method:** POST
- **Body:**
```json
{
 "firstName": "John",
 "lastName": "Doe",
 "email": "john.doe@example.com",
 "marketingConsent": true
}
Get User

    Endpoint: /user/{id}
    Method: GET
    Parameters:
        id: User's ID
        accessToken: JWT access token

Built With

    Spring Boot - The web framework used
    Maven - Dependency Management
    H2 Database - In-memory database

Authors

    Mohamed Said
