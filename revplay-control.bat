@echo off
setlocal enabledelayedexpansion

title RevPlay Platform Controller

:main_menu
cls
echo.
echo  ================================================================
echo           REVPLAY PLATFORM CONTROLLER
echo  ================================================================
echo.
echo  Backend Services + Frontend Management
echo.
echo  1. Build All Services + Frontend
echo  2. Start All Services + Frontend
echo  3. Stop All Services + Frontend
echo  4. Check Status of All Services + Frontend
echo  5. Build Backend Only
echo  6. Start Backend Only
echo  7. Stop Backend Only
echo  8. Check Backend Status Only
echo  9. Frontend Only Operations
echo  10. Exit
echo.
set /p choice="Select an option (1-10): "

if "%choice%"=="1" goto build_all
if "%choice%"=="2" goto start_all
if "%choice%"=="3" goto stop_all
if "%choice%"=="4" goto check_all_status
if "%choice%"=="5" goto build_backend_only
if "%choice%"=="6" goto start_backend_only
if "%choice%"=="7" goto stop_backend_only
if "%choice%"=="8" goto check_backend_status_only
if "%choice%"=="9" goto frontend_only
if "%choice%"=="10" goto exit

:build_all
echo.
echo ========================================
echo     BUILDING ALL SERVICES + FRONTEND
echo ========================================
echo.

echo [1/11] Building Common Module...
cd common
call mvn clean install
if %errorlevel% neq 0 (
    echo [ERROR] Common module build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Common module built successfully.

echo [2/11] Building Config Server...
cd ..\config-server
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Config Server build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Config Server built successfully.

echo [3/11] Building Eureka Server...
cd ..\eureka-server
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Eureka Server build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Eureka Server built successfully.

echo [4/11] Building API Gateway...
cd ..\api-gateway
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] API Gateway build failed!
    pause
    goto main_menu
)
echo [SUCCESS] API Gateway built successfully.

echo [5/11] Building User Service...
cd ..\user-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] User Service build failed!
    pause
    goto main_menu
)
echo [SUCCESS] User Service built successfully.

echo [6/11] Building Catalog Service...
cd ..\catalog-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Catalog Service build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Catalog Service built successfully.

echo [7/11] Building Playlist Service...
cd ..\playlist-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Playlist Service build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Playlist Service built successfully.

echo [8/11] Building Favourite Service...
cd ..\favourite-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Favourite Service build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Favourite Service built successfully.

echo [9/11] Building Playback Service...
cd ..\playback-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Playback Service build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Playback Service built successfully.

echo [10/11] Building Analytics Service...
cd ..\analytics-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Analytics Service build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Analytics Service built successfully.

echo [11/11] Building Frontend...
cd ..\revplay-frontend
call npm install --legacy-peer-deps
if %errorlevel% neq 0 (
    echo [ERROR] Frontend npm install failed!
    pause
    goto main_menu
)
echo [SUCCESS] Frontend dependencies installed.

call npm run build
if %errorlevel% neq 0 (
    echo [ERROR] Frontend build failed!
    pause
    goto main_menu
)
echo [SUCCESS] Frontend built successfully.

echo.
echo ========================================
echo     BUILD COMPLETE - ALL SUCCESS!
echo ========================================
echo.
pause
goto main_menu

:start_all
echo.
echo ========================================
echo     STARTING ALL SERVICES + FRONTEND
echo ========================================
echo.

echo [1/11] Starting Config Server (8888)...
start "Config Server" cmd /k "cd config-server && mvn spring-boot:run"
timeout /t 3 >nul

echo [2/11] Starting Eureka Server (8761)...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
timeout /t 3 >nul

echo [3/11] Starting API Gateway (9080)...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"
timeout /t 3 >nul

echo [4/11] Starting User Service (8081)...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [5/11] Starting Catalog Service (8082)...
start "Catalog Service" cmd /k "cd catalog-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [6/11] Starting Playlist Service (8083)...
start "Playlist Service" cmd /k "cd playlist-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [7/11] Starting Favourite Service (8087)...
start "Favourite Service" cmd /k "cd favourite-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [8/11] Starting Playback Service (8085)...
start "Playback Service" cmd /k "cd playback-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [9/11] Starting Analytics Service (8086)...
start "Analytics Service" cmd /k "cd analytics-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [10/11] Starting Frontend Dev Server (4200)...
start "Frontend" cmd /k "cd revplay-frontend && npm start"
timeout /t 3 >nul

echo [11/11] All services and frontend started!
echo.
echo ========================================
echo         ACCESS POINTS
echo ========================================
echo.
echo Backend Services:
echo   Config Server: http://localhost:8888
echo   Eureka Server: http://localhost:8761
echo   API Gateway: http://localhost:9080
echo   User Service: http://localhost:8081
echo   Catalog Service: http://localhost:8082
echo   Playlist Service: http://localhost:8083
echo   Favourite Service: http://localhost:8087
echo   Playback Service: http://localhost:8085
echo   Analytics Service: http://localhost:8086
echo.
echo Frontend:
echo   Angular App: http://localhost:4200
echo   Eureka Dashboard: http://localhost:8761
echo.
pause
goto main_menu

:stop_all
echo.
echo ========================================
echo     STOPPING ALL SERVICES + FRONTEND
echo ========================================
echo.

echo Terminating Java processes...
taskkill /F /IM java.exe /T >nul 2>&1

echo Terminating Node.js processes...
taskkill /F /IM node.exe /T >nul 2>&1

echo Cleaning up remaining processes...
for /f "tokens=1" %%a in ('tasklist /FI "IMAGENAME eq cmd.exe" /FO "TABLE" /NH ^| find "cmd"') do (
    taskkill /PID %%a /F >nul 2>&1
)

echo All services and frontend stopped!
echo.
pause
goto main_menu

:check_all_status
echo.
echo ========================================
echo     CHECKING ALL SERVICES + FRONTEND
echo ========================================
echo.

echo Backend Services Status:
echo ------------------------
curl -s http://localhost:8888/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Config Server - http://localhost:8888
) else (
    echo [STOPPED]  Config Server - http://localhost:8888
)

curl -s http://localhost:8761/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Eureka Server - http://localhost:8761
) else (
    echo [STOPPED]  Eureka Server - http://localhost:8761
)

curl -s http://localhost:9080/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  API Gateway - http://localhost:9080
) else (
    echo [STOPPED]  API Gateway - http://localhost:9080
)

curl -s http://localhost:8081/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  User Service - http://localhost:8081
) else (
    echo [STOPPED]  User Service - http://localhost:8081
)

curl -s http://localhost:8082/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Catalog Service - http://localhost:8082
) else (
    echo [STOPPED]  Catalog Service - http://localhost:8082
)

curl -s http://localhost:8083/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Playlist Service - http://localhost:8083
) else (
    echo [STOPPED]  Playlist Service - http://localhost:8083
)

curl -s http://localhost:8087/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Favourite Service - http://localhost:8087
) else (
    echo [STOPPED]  Favourite Service - http://localhost:8087
)

curl -s http://localhost:8085/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Playback Service - http://localhost:8085
) else (
    echo [STOPPED]  Playback Service - http://localhost:8085
)

curl -s http://localhost:8086/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Analytics Service - http://localhost:8086
) else (
    echo [STOPPED]  Analytics Service - http://localhost:8086
)

echo.
echo Frontend Status:
echo ------------------------
curl -s http://localhost:4200 >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Frontend - http://localhost:4200
) else (
    echo [STOPPED]  Frontend - http://localhost:4200
)

echo.
echo ========================================
echo         STATUS CHECK COMPLETE
echo ========================================
echo.
pause
goto main_menu

:build_backend_only
echo.
echo ========================================
echo     BUILDING BACKEND SERVICES ONLY
echo ========================================
echo.

cd common && call mvn clean install && cd ..
cd config-server && call mvn clean compile && cd ..
cd eureka-server && call mvn clean compile && cd ..
cd api-gateway && call mvn clean compile && cd ..
cd user-service && call mvn clean compile && cd ..
cd catalog-service && call mvn clean compile && cd ..
cd playlist-service && call mvn clean compile && cd ..
cd favourite-service && call mvn clean compile && cd ..
cd playback-service && call mvn clean compile && cd ..
cd analytics-service && call mvn clean compile && cd ..

echo Backend services build complete!
echo.
pause
goto main_menu

:start_backend_only
echo.
echo ========================================
echo     STARTING BACKEND SERVICES ONLY
echo ========================================
echo.

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

echo Backend services started!
echo.
pause
goto main_menu

:stop_backend_only
echo.
echo ========================================
echo     STOPPING BACKEND SERVICES ONLY
echo ========================================
echo.

taskkill /F /IM java.exe /T >nul 2>&1
echo Backend services stopped!
echo.
pause
goto main_menu

:check_backend_status_only
echo.
echo ========================================
echo     CHECKING BACKEND SERVICES STATUS
echo ========================================
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

echo.
pause
goto main_menu

:frontend_only
echo.
echo ========================================
echo       FRONTEND OPERATIONS
echo ========================================
echo.
echo 1. Build Frontend Only
echo 2. Start Frontend Only
echo 3. Stop Frontend Only
echo 4. Check Frontend Status Only
echo 5. Back to Main Menu
echo.
set /p frontend_choice="Select option (1-5): "

if "%frontend_choice%"=="1" goto build_frontend_only
if "%frontend_choice%"=="2" goto start_frontend_only
if "%frontend_choice%"=="3" goto stop_frontend_only
if "%frontend_choice%"=="4" goto check_frontend_status_only
if "%frontend_choice%"=="5" goto main_menu

:build_frontend_only
echo.
echo Building Frontend...
cd revplay-frontend
call npm install --legacy-peer-deps
if %errorlevel% neq 0 (
    echo [ERROR] Frontend npm install failed!
    pause
    goto frontend_only
)
call npm run build
if %errorlevel% neq 0 (
    echo [ERROR] Frontend build failed!
    pause
    goto frontend_only
)
echo Frontend built successfully!
echo.
pause
goto frontend_only

:start_frontend_only
echo.
echo Starting Frontend...
start "Frontend" cmd /k "cd revplay-frontend && npm start"
echo Frontend started on http://localhost:4200
echo.
pause
goto frontend_only

:stop_frontend_only
echo.
echo Stopping Frontend...
taskkill /F /IM node.exe /T >nul 2>&1
echo Frontend stopped!
echo.
pause
goto frontend_only

:check_frontend_status_only
echo.
echo Checking Frontend status...
curl -s http://localhost:4200 >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING] Frontend - http://localhost:4200
) else (
    echo [STOPPED] Frontend - http://localhost:4200
)
echo.
pause
goto frontend_only

:exit
echo.
echo Thank you for using RevPlay Platform Controller!
echo.
timeout /t 2 >nul
exit
