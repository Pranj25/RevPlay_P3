@echo off
title RevPlay Microservices - Stop All Services
echo.
echo ========================================
echo Stopping RevPlay Microservices
echo ========================================
echo.

echo Stopping all Java processes...
taskkill /F /IM java.exe /T 2>NUL

echo Stopping Node.js processes...
taskkill /F /IM node.exe /T 2>NUL

echo.
echo ========================================
echo  ALL SERVICES STOPPED
echo ========================================
echo.
echo Services have been terminated.
echo You can now restart them using start-all-services.bat
echo.
pause
