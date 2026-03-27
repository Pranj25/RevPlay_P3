@echo off
title RevPlay Microservices - Production Startup
echo.
echo ========================================
echo Starting RevPlay Microservices
echo ========================================
echo.

echo [1/10] Starting Config Server (Port 8888)...
cd config-server
start "Config Server" cmd /k "mvn spring-boot:run -Dspring.profiles.active=dev"
cd ..
timeout /t 5

echo.
echo [2/10] Starting Eureka Server (Port 8761)...
cd eureka-server
start "Eureka Server" cmd /k "mvn spring-boot:run"
cd ..
timeout /t 5

echo.
echo [3/10] Starting User Service (Port 8085)...
cd user-service
start "User Service" cmd /k "mvn spring-boot:run -Dspring.profiles.active=dev"
cd ..
timeout /t 5

echo.
echo [4/10] Starting Catalog Service (Port 8082)...
cd catalog-service
start "Catalog Service" cmd /k "mvn spring-boot:run -Dspring.profiles.active=dev"
cd ..
timeout /t 5

echo.
echo [5/10] Starting API Gateway (Port 9080)...
cd api-gateway
start "API Gateway" cmd /k "mvn spring-boot:run -Dspring.profiles.active=dev"
cd ..
timeout /t 5

echo.
echo [6/10] Starting Playlist Service (Port 8086)...
cd playlist-service
start "Playlist Service" cmd /k "mvn spring-boot:run -Dspring.profiles.active=dev"
cd ..
timeout /t 5

echo.
echo [7/10] Starting Favourite Service (Port 8087)...
cd favourite-service
start "Favourite Service" cmd /k "mvn spring-boot:run -Dspring.profiles.active=dev"
cd ..
timeout /t 5

echo.
echo [8/10] Starting Playback Service (Port 8088)...
cd playback-service
start "Playback Service" cmd /k "mvn spring-boot:run -Dspring.profiles.active=dev"
cd ..
timeout /t 5

echo.
echo [9/10] Starting Analytics Service (Port 8089)...
cd analytics-service
start "Analytics Service" cmd /k "mvn spring-boot:run -Dspring.profiles.active=dev"
cd ..
timeout /t 5

echo.
echo [10/10] Starting Frontend (Port 4200)...
cd revplay-frontend
start "Frontend" cmd /k "ng serve"
cd ..
timeout /t 10

echo.
echo ========================================
echo 🎉 ALL SERVICES STARTED
echo ========================================
echo.
echo 📊 Access URLs:
echo - Config Server:    http://localhost:8888
echo - Eureka Dashboard: http://localhost:8761
echo - User Service:     http://localhost:8085/swagger-ui.html
echo - Catalog Service:   http://localhost:8082
echo - Playlist Service:  http://localhost:8086
echo - Favourite Service: http://localhost:8087
echo - Playback Service:  http://localhost:8088
echo - Analytics Service: http://localhost:8089
echo - API Gateway:      http://localhost:9080
echo - Frontend:         http://localhost:4200
echo.
echo 🌐 API Endpoints (via Gateway):
echo - Authentication:   http://localhost:9080/api/users/**
echo - Catalog:        http://localhost:9080/api/catalog/**
echo - Playlist:       http://localhost:9080/api/playlist/**
echo - Favourites:     http://localhost:9080/api/favourite/**
echo - Playback:       http://localhost:9080/api/playback/**
echo - Analytics:      http://localhost:9080/api/analytics/**
echo.
echo 🔍 Verification Checklist:
echo - [ ] Check Eureka Dashboard for service registration
echo - [ ] Test User Service Swagger UI
echo - [ ] Verify API Gateway routing
echo - [ ] Test Frontend authentication flow
echo.
echo ========================================
echo Press any key to open verification URLs...
pause

start "" http://localhost:8761
start "" http://localhost:9080
start "" http://localhost:8085/swagger-ui.html
start "" http://localhost:4200

echo.
echo Services are running! Monitor terminal windows for logs.
echo Close this window to stop all services.
pause
