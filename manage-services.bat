@echo off
setlocal enabledelayedexpansion

title RevPlay Microservices Manager

:menu
cls
echo.
echo  ===============================================
echo          REVPLAY MICROSERVICES MANAGER
echo  ===============================================
echo.
echo  1. Start All Services
echo  2. Stop All Services  
echo  3. Check Service Status
echo  4. Build All Services
echo  5. Start Individual Service
echo  6. Stop Individual Service
echo  7. Exit
echo.
set /p choice="Select an option (1-7): "

if "%choice%"=="1" goto start_all
if "%choice%"=="2" goto stop_all
if "%choice%"=="3" goto check_status
if "%choice%"=="4" goto build_all
if "%choice%"=="5" goto start_individual
if "%choice%"=="6" goto stop_individual
if "%choice%"=="7" goto exit

:start_all
echo.
echo Starting all RevPlay microservices...
echo.

echo [1/10] Starting Config Server...
start "Config Server" cmd /k "cd config-server && mvn spring-boot:run"
timeout /t 3 >nul

echo [2/10] Starting Eureka Server...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
timeout /t 3 >nul

echo [3/10] Starting API Gateway...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"
timeout /t 3 >nul

echo [4/10] Starting User Service...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [5/10] Starting Catalog Service...
start "Catalog Service" cmd /k "cd catalog-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [6/10] Starting Playlist Service...
start "Playlist Service" cmd /k "cd playlist-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [7/10] Starting Favourite Service...
start "Favourite Service" cmd /k "cd favourite-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [8/10] Starting Playback Service...
start "Playback Service" cmd /k "cd playback-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [9/10] Starting Analytics Service...
start "Analytics Service" cmd /k "cd analytics-service && mvn spring-boot:run"
timeout /t 3 >nul

echo [10/10] All services started!
echo.
echo Access points:
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
pause
goto menu

:stop_all
echo.
echo Stopping all RevPlay microservices...
echo.

taskkill /F /IM java.exe /T >nul 2>&1
echo All Java processes terminated.

echo Cleaning up any remaining processes...
for /f "tokens=1" %%a in ('tasklist /FI "IMAGENAME eq cmd.exe" /FO "TABLE" /NH ^| find "cmd"') do (
    taskkill /PID %%a /F >nul 2>&1
)

echo All services stopped.
echo.
pause
goto menu

:check_status
echo.
echo Checking RevPlay microservices status...
echo.
echo =====================================
echo       SERVICE STATUS CHECK
echo =====================================
echo.

echo Checking Config Server (8888)...
curl -s http://localhost:8888/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Config Server - http://localhost:8888
) else (
    echo [STOPPED]  Config Server - http://localhost:8888
)

echo Checking Eureka Server (8761)...
curl -s http://localhost:8761/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Eureka Server - http://localhost:8761
) else (
    echo [STOPPED]  Eureka Server - http://localhost:8761
)

echo Checking API Gateway (9080)...
curl -s http://localhost:9080/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  API Gateway - http://localhost:9080
) else (
    echo [STOPPED]  API Gateway - http://localhost:9080
)

echo Checking User Service (8081)...
curl -s http://localhost:8081/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  User Service - http://localhost:8081
) else (
    echo [STOPPED]  User Service - http://localhost:8081
)

echo Checking Catalog Service (8082)...
curl -s http://localhost:8082/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Catalog Service - http://localhost:8082
) else (
    echo [STOPPED]  Catalog Service - http://localhost:8082
)

echo Checking Playlist Service (8083)...
curl -s http://localhost:8083/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Playlist Service - http://localhost:8083
) else (
    echo [STOPPED]  Playlist Service - http://localhost:8083
)

echo Checking Favourite Service (8087)...
curl -s http://localhost:8087/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Favourite Service - http://localhost:8087
) else (
    echo [STOPPED]  Favourite Service - http://localhost:8087
)

echo Checking Playback Service (8085)...
curl -s http://localhost:8085/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Playback Service - http://localhost:8085
) else (
    echo [STOPPED]  Playback Service - http://localhost:8085
)

echo Checking Analytics Service (8086)...
curl -s http://localhost:8086/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [RUNNING]  Analytics Service - http://localhost:8086
) else (
    echo [STOPPED]  Analytics Service - http://localhost:8086
)

echo.
echo =====================================
echo       STATUS CHECK COMPLETE
echo =====================================
echo.
pause
goto menu

:build_all
echo.
echo Building all RevPlay microservices...
echo.

echo [1/10] Building Common Module...
cd common
call mvn clean install
if %errorlevel% neq 0 (
    echo [ERROR] Common module build failed!
    pause
    goto menu
)
echo [SUCCESS] Common module built successfully.

echo [2/10] Building Config Server...
cd ..\config-server
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Config Server build failed!
    pause
    goto menu
)
echo [SUCCESS] Config Server built successfully.

echo [3/10] Building Eureka Server...
cd ..\eureka-server
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Eureka Server build failed!
    pause
    goto menu
)
echo [SUCCESS] Eureka Server built successfully.

echo [4/10] Building API Gateway...
cd ..\api-gateway
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] API Gateway build failed!
    pause
    goto menu
)
echo [SUCCESS] API Gateway built successfully.

echo [5/10] Building User Service...
cd ..\user-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] User Service build failed!
    pause
    goto menu
)
echo [SUCCESS] User Service built successfully.

echo [6/10] Building Catalog Service...
cd ..\catalog-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Catalog Service build failed!
    pause
    goto menu
)
echo [SUCCESS] Catalog Service built successfully.

echo [7/10] Building Playlist Service...
cd ..\playlist-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Playlist Service build failed!
    pause
    goto menu
)
echo [SUCCESS] Playlist Service built successfully.

echo [8/10] Building Favourite Service...
cd ..\favourite-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Favourite Service build failed!
    pause
    goto menu
)
echo [SUCCESS] Favourite Service built successfully.

echo [9/10] Building Playback Service...
cd ..\playback-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Playback Service build failed!
    pause
    goto menu
)
echo [SUCCESS] Playback Service built successfully.

echo [10/10] Building Analytics Service...
cd ..\analytics-service
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERROR] Analytics Service build failed!
    pause
    goto menu
)
echo [SUCCESS] Analytics Service built successfully.

echo.
echo =====================================
echo     ALL SERVICES BUILT SUCCESSFULLY!
echo =====================================
echo.
pause
goto menu

:start_individual
echo.
echo Select service to start:
echo.
echo 1. Config Server (8888)
echo 2. Eureka Server (8761)
echo 3. API Gateway (9080)
echo 4. User Service (8081)
echo 5. Catalog Service (8082)
echo 6. Playlist Service (8083)
echo 7. Favourite Service (8087)
echo 8. Playback Service (8085)
echo 9. Analytics Service (8086)
echo 0. Back to Main Menu
echo.
set /p service_choice="Select service (0-9): "

if "%service_choice%"=="1" (
    start "Config Server" cmd /k "cd config-server && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="2" (
    start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="3" (
    start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="4" (
    start "User Service" cmd /k "cd user-service && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="5" (
    start "Catalog Service" cmd /k "cd catalog-service && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="6" (
    start "Playlist Service" cmd /k "cd playlist-service && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="7" (
    start "Favourite Service" cmd /k "cd favourite-service && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="8" (
    start "Playback Service" cmd /k "cd playback-service && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="9" (
    start "Analytics Service" cmd /k "cd analytics-service && mvn spring-boot:run"
    goto start_individual
)
if "%service_choice%"=="0" goto menu

:stop_individual
echo.
echo Select service to stop:
echo.
echo 1. Config Server
echo 2. Eureka Server
echo 3. API Gateway
echo 4. User Service
echo 5. Catalog Service
echo 6. Playlist Service
echo 7. Favourite Service
echo 8. Playback Service
echo 9. Analytics Service
echo 0. Stop All Services
echo 99. Back to Main Menu
echo.
set /p stop_choice="Select service (0,1-9,99): "

if "%stop_choice%"=="0" goto stop_all
if "%stop_choice%"=="99" goto menu
if "%stop_choice%"=="1" (
    taskkill /F /IM "Config Server.exe" >nul 2>&1
    goto stop_individual
)
if "%stop_choice%"=="2" (
    taskkill /F /IM "Eureka Server.exe" >nul 2>&1
    goto stop_individual
)
if "%stop_choice%"=="3" (
    taskkill /F /IM "API Gateway.exe" >nul 2>&1
    goto stop_individual
)
if "%stop_choice%"=="4" (
    taskkill /F /IM "User Service.exe" >nul 2>&1
    goto stop_individual
)
if "%stop_choice%"=="5" (
    taskkill /F /IM "Catalog Service.exe" >nul 2>&1
    goto stop_individual
)
if "%stop_choice%"=="6" (
    taskkill /F /IM "Playlist Service.exe" >nul 2>&1
    goto stop_individual
)
if "%stop_choice%"=="7" (
    taskkill /F /IM "Favourite Service.exe" >nul 2>&1
    goto stop_individual
)
if "%stop_choice%"=="8" (
    taskkill /F /IM "Playback Service.exe" >nul 2>&1
    goto stop_individual
)
if "%stop_choice%"=="9" (
    taskkill /F /IM "Analytics Service.exe" >nul 2>&1
    goto stop_individual
)

goto stop_individual

:exit
echo.
echo Thank you for using RevPlay Microservices Manager!
echo.
timeout /t 2 >nul
exit
