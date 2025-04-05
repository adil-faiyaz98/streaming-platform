@echo off
set /p TOKEN=Enter your JWT token: 

echo.
echo Testing personalized recommendations...
curl -X GET "http://localhost:8082/api/v2/movies/1/recommendations?userId=admin" ^
  -H "Authorization: Bearer %TOKEN%"

echo.
echo.
echo Testing similar content recommendations...
curl -X GET "http://localhost:8082/api/v2/movies/1/recommendations" ^
  -H "Authorization: Bearer %TOKEN%"

echo.
echo.
echo Testing trending content...
curl -X GET "http://localhost:8083/api/v1/recommendations/trending" ^
  -H "Authorization: Bearer %TOKEN%"

echo.
echo.
echo Recording a rating interaction...
curl -X POST "http://localhost:8083/api/v1/interactions/rating?userId=admin&itemId=1&itemType=MOVIE&rating=5.0" ^
  -H "Authorization: Bearer %TOKEN%"

echo.
echo.
echo Done testing recommendations!
