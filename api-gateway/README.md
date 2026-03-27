# API Gateway

## Overview
The API Gateway is the entry point for all client requests in the RevPlay microservices architecture. It routes requests to the appropriate microservices and handles cross-cutting concerns like CORS, authentication, and load balancing.

## Configuration
- **Port**: 9080
- **Service Registration**: Eureka Server (localhost:8761)
- **Configuration Server**: Config Server (localhost:8888)

## Routes
The gateway routes requests as follows:
- `/api/users/**` → User Service
- `/api/catalog/**` → Catalog Service  
- `/api/music/**` → Music Service

## CORS Configuration
Configured to allow requests from:
- http://localhost:4200 (Angular frontend)
- http://localhost:3000 (React frontend)

## Running the API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

## Testing
Once running, you can test the gateway at:
- http://localhost:9080/api/users/...
- http://localhost:9080/api/catalog/...
- http://localhost:9080/api/music/...
