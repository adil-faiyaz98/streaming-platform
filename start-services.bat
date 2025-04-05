@echo off
echo Setting up environment...

REM Create database directories if they don't exist
mkdir -p data\postgres
mkdir -p data\redis

REM Create test users
echo Creating test users...

REM Start the catalog service
echo Starting catalog service...
start cmd /k "cd streaming-catalog-service && gradlew bootRun"

REM Wait for catalog service to start
timeout /t 30

REM Start the recommendation service
echo Starting recommendation service...
start cmd /k "cd recommendation-service && gradlew bootRun"

echo Services are starting up. Please wait...
echo.
echo Catalog Service: http://localhost:8082
echo Recommendation Service: http://localhost:8083
echo.
echo Test credentials:
echo Username: admin
echo Password: admin123
echo.
echo Username: user
echo Password: user123
