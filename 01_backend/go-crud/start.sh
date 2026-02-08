#!/bin/bash

# Go-Zero Hello API Docker 启动脚本

set -e

echo "=== Go-Zero Hello API with MySQL ==="
echo ""

# 检查 docker-compose 是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "❌ docker-compose is not installed"
    exit 1
fi

# 检查 docker 是否运行
if ! docker info &> /dev/null; then
    echo "❌ Docker daemon is not running"
    exit 1
fi

echo "✅ Docker environment check passed"
echo ""

# 菜单
echo "Select action:"
echo "1) Build and start services"
echo "2) Stop services"
echo "3) View logs"
echo "4) Clean up (remove containers and volumes)"
echo "5) View service status"
echo ""
read -p "Enter choice (1-5): " choice

case $choice in
    1)
        echo "Starting services..."
        docker-compose up -d
        echo ""
        echo "✅ Services started successfully"
        echo "API endpoint: http://localhost:8888/hello/world"
        echo "MySQL: localhost:3306 (root/12345)"
        echo ""
        echo "View logs with: docker-compose logs -f"
        ;;
    2)
        echo "Stopping services..."
        docker-compose stop
        echo "✅ Services stopped"
        ;;
    3)
        echo "Showing logs..."
        docker-compose logs -f
        ;;
    4)
        echo "Cleaning up..."
        docker-compose down -v
        echo "✅ All resources cleaned up"
        ;;
    5)
        echo "Service status:"
        docker-compose ps
        ;;
    *)
        echo "Invalid choice"
        exit 1
        ;;
esac
