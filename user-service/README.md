# User Service

## Overview
The User Service handles authentication and user management for the RevPlay application. It provides user registration, login, and JWT token generation.

## Features
- **User Registration**: Create new user accounts with username, email, and password
- **User Login**: Authenticate users and generate JWT tokens
- **JWT Token Generation**: Generate secure JSON Web Tokens for authentication
- **Token Validation**: Validate JWT tokens for protected routes
- **User Roles**: Support for LISTENER, ARTIST, and ADMIN roles

## Configuration
- **Port**: 8085
- **Database**: MySQL (revplay_user_db)
- **Service Registration**: Eureka Server (localhost:8761)
- **Configuration Server**: Config Server (localhost:8888)

## API Endpoints

### Authentication Endpoints
- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login user and get JWT token
- `POST /auth/validate` - Validate JWT token

### Request Examples

#### Register User
```json
POST /auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Login User
```json
POST /auth/login
{
  "username": "john_doe",
  "password": "password123"
}
```

#### Validate Token
```http
POST /auth/validate
Authorization: Bearer <jwt_token>
```

## Response Examples

### Successful Login
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "email": "john@example.com",
  "role": "LISTENER",
  "message": "Login successful"
}
```

### Error Response
```json
{
  "message": "Invalid password"
}
```

## Running the User Service
```bash
cd user-service
mvn spring-boot:run
```

## Database Schema
The service creates the following tables:
- `users` - User information
- `playlists` - User playlists

## JWT Configuration
- **Secret**: Configured in application.yml
- **Expiration**: 24 hours (86400000 ms)
- **Algorithm**: HS256
