@echo off
echo Requesting JWT token for testing...

curl -X POST http://localhost:8082/api/v1/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"

echo.
echo.
echo Copy the token from the response above and use it in your API requests:
echo curl -X GET http://localhost:8082/api/v1/catalog/movies -H "Authorization: Bearer YOUR_TOKEN_HERE"
echo.
