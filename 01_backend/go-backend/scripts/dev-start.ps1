# Dev Environment Startup Script (PowerShell)
# Usage: powershell -ExecutionPolicy Bypass -File .\scripts\dev-start.ps1

Write-Host ""
Write-Host "================================" -ForegroundColor Cyan
Write-Host "Starting Dev Environment" -ForegroundColor Cyan
Write-Host "- PostgreSQL container" -ForegroundColor Cyan
Write-Host "- Local API (hot reload, debugging)" -ForegroundColor Cyan
Write-Host "- Swagger documentation" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# Get current directory
$projectRoot = Split-Path -Parent $PSScriptRoot
$deployDevDir = Join-Path $projectRoot "deploy\dev"

# 1. Start PostgreSQL container
Write-Host "[1/5] Starting PostgreSQL container..." -ForegroundColor Yellow
Push-Location $deployDevDir
docker-compose up -d
Pop-Location

if ($LASTEXITCODE -eq 0) {
    Write-Host "[OK] PostgreSQL started successfully" -ForegroundColor Green
    Start-Sleep -Seconds 2
} else {
    Write-Host "[ERROR] PostgreSQL start failed" -ForegroundColor Red
    exit 1
}

# 2. Verify Swagger documentation
Write-Host "`n[2/5] Checking Swagger documentation..." -ForegroundColor Yellow
if (Test-Path "$projectRoot\docs\swagger.json") {
    Write-Host "[OK] Swagger documentation found" -ForegroundColor Green
} else {
    Write-Host "[WARN] Swagger documentation not found. Generating..." -ForegroundColor Yellow
    swag init -g cmd/api/main.go
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] Swagger generated successfully" -ForegroundColor Green
    } else {
        Write-Host "[WARN] Swagger generation failed (may need: go install github.com/swaggo/swag/cmd/swag@latest)" -ForegroundColor Yellow
    }
}

# 3. Compile API
Write-Host "`n[3/5] Compiling API..." -ForegroundColor Yellow
Push-Location $projectRoot
go build -o api.exe ./cmd/api/main.go
Pop-Location

if ($LASTEXITCODE -eq 0) {
    Write-Host "[OK] API compiled successfully" -ForegroundColor Green
} else {
    Write-Host "[ERROR] API compilation failed" -ForegroundColor Red
    exit 1
}

# 4. Set environment variables
Write-Host "`n[4/5] Setting up environment variables..." -ForegroundColor Yellow
$env:HOLIDAY_SERVER_PORT = "8081"
$env:HOLIDAY_SERVER_HOST = "0.0.0.0"
$env:HOLIDAY_DATABASE_HOST = "localhost"
$env:HOLIDAY_DATABASE_PORT = "5432"
$env:HOLIDAY_DATABASE_USERNAME = "postgres"
$env:HOLIDAY_DATABASE_PASSWORD = "postgres"
$env:HOLIDAY_DATABASE_DBNAME = "holiday_system"
$env:HOLIDAY_DATABASE_SSLMODE = "disable"
Write-Host "[OK] Environment variables set" -ForegroundColor Green

# 5. Done - Ready for debugging
Write-Host "`n[5/5] Dev environment setup complete!" -ForegroundColor Yellow
Write-Host "`n" -ForegroundColor Green
Write-Host "SUCCESS! Dev environment is ready for development." -ForegroundColor Green
Write-Host ""
Write-Host "Access URLs:" -ForegroundColor Cyan
Write-Host "  API Health: http://localhost:8081/health" -ForegroundColor White
Write-Host "  Swagger UI: http://localhost:8081/swagger/index.html" -ForegroundColor White
Write-Host ""
Write-Host "Database Configuration:" -ForegroundColor Cyan
Write-Host "  Host: localhost" -ForegroundColor White
Write-Host "  Port: 5432" -ForegroundColor White
Write-Host "  User: postgres" -ForegroundColor White
Write-Host "  Database: holiday_system" -ForegroundColor White
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Cyan
Write-Host "  1. Open VS Code: code ." -ForegroundColor White
Write-Host "  2. Set breakpoints in cmd/api/main.go or other files" -ForegroundColor White
Write-Host "  3. Press F5 to start debugging (or use Run -> Start Debugging)" -ForegroundColor White
Write-Host "  4. API will run on http://localhost:8081" -ForegroundColor White
Write-Host ""
Write-Host "Development Tips:" -ForegroundColor Cyan
Write-Host "  - Hot reload: Use go run ./cmd/api/main.go in VS Code terminal" -ForegroundColor White
Write-Host "  - Update Swagger: swag init -g cmd/api/main.go" -ForegroundColor White
Write-Host "  - View DB logs: cd deploy/dev && docker-compose logs -f" -ForegroundColor White
Write-Host "  - Stop DB: cd deploy/dev && docker-compose down" -ForegroundColor White
Write-Host ""
Write-Host "PostgreSQL is ready at localhost:5432" -ForegroundColor Green
Write-Host ""
