@echo off
title RevPlay - Git Operations
echo.
echo ========================================
echo Git Operations for RevPlay
echo ========================================
echo.

echo Adding all changes to git...
git add .
echo.

echo Committing changes...
git commit -m "Clean up project files - add startup and stop scripts"

echo.
echo Pushing to remote repository...
git push origin main

echo.
echo ========================================
echo ✅ Git Operations Complete!
echo ========================================
echo.
echo Changes pushed to GitHub repository.
echo.
pause
