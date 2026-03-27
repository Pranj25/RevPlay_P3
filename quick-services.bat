@echo off
setlocal enabledelayedexpansion

if "%1"=="start" goto quick_start
if "%1"=="stop" goto quick_stop
if "%1"=="status" goto quick_status
if "%1"=="build" goto quick_build

echo Usage: quick-services.bat [start^|stop^|status^|build]
echo.
echo   start  - Start all services
echo   stop   - Stop all services  
echo   status - Check service status
echo   build  - Build all services
echo.
goto end

:quick_start
echo Starting all RevPlay services...
start "Config Server" cmd /k "cd config-server && mvn spring-boot:run"
timeout /t 2 >nul
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
timeout /t 2 >nul
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"
timeout /t 2 >nul
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"
timeout /t 2 >nul
start "Catalog Service" cmd /k "cd catalog-service && mvn spring-boot:run"
timeout /t 2 >nul
start "Playlist Service" cmd /k "cd playlist-service && mvn spring-boot:run"
timeout /t 2 >nul
start "Favourite Service" cmd /k "cd favourite-service && mvn spring-boot:run"
timeout /t 2 >nul
start "Playback Service" cmd /k "cd playback-service && mvn spring-boot:run"
timeout /t 2 >nul
start "Analytics Service" cmd /k "cd analytics-service && mvn spring-boot:run"
echo All services started!
goto end

:quick_stop
echo Stopping all RevPlay services...
taskkill /F /IM java.exe >nul 2>&1
echo All services stopped!
goto end

:quick_status
echo Checking RevPlay services status...
echo.
curl -s http://localhost:8888/actuator/health >nul 2>&1 && echo [RUNNING] Config Server || echo [STOPPED] Config Server
curl -s http://localhost:8761/actuator/health >nul 2>&1 && echo [RUNNING] Eureka Server || echo [STOPPED] Eureka Server
curl -s http://localhost:9080/actuator/health >nul 2>&1 && echo [RUNNING] API Gateway || echo [STOPPED] API Gateway
curl -s http://localhost:8081/actuator/health >nul 2>&1 && echo [RUNNING] User Service || echo [STOPPED] User Service
curl -s http://localhost:8082/actuator/health >nul 2>&1 && echo [RUNNING] Catalog Service || echo [STOPPED] Catalog Service
curl -s http://localhost:8083/actuator/health >nul 2>&1 && echo [RUNNING] Playlist Service || echo [STOPPED] Playlist Service
curl -s http://localhost:8087/actuator/health >nul 2>&1 && echo [RUNNING] Favourite Service || echo [STOPPED] Favourite Service
curl -s http://localhost:8085/actuator/health >nul 2>&1 && echo [RUNNING] Playback Service || echo [STOPPED] Playback Service
curl -s http://localhost:8086/actuator/health >nul 2>&1 && echo [RUNNING] Analytics Service || echo [STOPPED] Analytics Service
goto end

:quick_build
echo Building all RevPlay services...
cd common && mvn clean install && cd ..
cd config-server && mvn clean compile && cd ..
cd eureka-server && mvn clean compile && cd ..
cd api-gateway && mvn clean compile && cd ..
cd user-service && mvn clean compile && cd ..
cd catalog-service && mvn clean compile && cd ..
cd playlist-service && mvn clean compile && cd ..
cd favourite-service && mvn clean compile && cd ..
cd playback-service && mvn clean compile && cd ..
cd analytics-service && mvn clean compile && cd ..
echo All services built!
goto end

:end
