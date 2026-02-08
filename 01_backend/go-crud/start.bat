@echo off
REM Go-Zero Hello API Docker 启动脚本 (Windows)

echo.
echo === Go-Zero Hello API with MySQL ===
echo.

REM 检查 docker-compose 是否安装
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo X docker-compose is not installed
    exit /b 1
)

REM 检查 docker 是否运行
docker info >nul 2>&1
if errorlevel 1 (
    echo X Docker daemon is not running
    exit /b 1
)

echo [OK] Docker environment check passed
echo.

echo Select action:
echo 1) Build and start services
echo 2) Stop services
echo 3) View logs
echo 4) Clean up (remove containers and volumes)
echo 5) View service status
echo 6) Connect to MySQL shell
echo.
set /p choice="Enter choice (1-6): "

if "%choice%"=="1" (
    echo Starting services...
    docker-compose up -d
    echo.
    echo [OK] Services started successfully
    echo API endpoint: http://localhost:8888/hello/world
    echo MySQL: localhost:3306 (root/12345)
    echo.
    echo View logs with: docker-compose logs -f
) else if "%choice%"=="2" (
    echo Stopping services...
    docker-compose stop
    echo [OK] Services stopped
) else if "%choice%"=="3" (
    echo Showing logs...
    docker-compose logs -f
) else if "%choice%"=="4" (
    echo Cleaning up...
    docker-compose down -v
    echo [OK] All resources cleaned up
) else if "%choice%"=="5" (
    echo Service status:
    docker-compose ps
) else if "%choice%"=="6" (
    echo Connecting to MySQL shell...
    docker exec -it hello-mysql mysql -u root -p12345 hello_db
) else (
    echo Invalid choice
    exit /b 1
)
