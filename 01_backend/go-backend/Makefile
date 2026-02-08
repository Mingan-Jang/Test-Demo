# Makefile for Holiday System

.PHONY: help build run test clean docker-build docker-run docker-compose-up docker-compose-down gcp-deploy migrate lint

help:
	@echo "節假日系統 - 可用命令"
	@echo ""
	@echo "本地開發:"
	@echo "  make build          - 構建應用"
	@echo "  make run            - 運行應用"
	@echo "  make test           - 運行測試"
	@echo "  make clean          - 清理構建文件"
	@echo "  make migrate        - 執行數據庫遷移"
	@echo ""
	@echo "Docker:"
	@echo "  make docker-build   - 構建 Docker 鏡像"
	@echo "  make docker-run     - 運行 Docker 容器"
	@echo "  make docker-compose-up   - 使用 docker-compose 啟動"
	@echo "  make docker-compose-down - 停止 docker-compose"
	@echo ""
	@echo "GCP 部署:"
	@echo "  make gcp-deploy     - 部署到 GCP"
	@echo ""
	@echo "代碼質量:"
	@echo "  make lint           - 運行 linter"
	@echo "  make fmt            - 格式化代碼"

build:
	@echo "🔨 構建應用..."
	go build -o holiday-api ./cmd/api/main.go
	@echo "✅ 構建完成: ./holiday-api"

run: build
	@echo "🚀 運行應用..."
	./holiday-api

test:
	@echo "🧪 運行測試..."
	go test -v ./...
	@echo "✅ 測試完成"

clean:
	@echo "🧹 清理文件..."
	rm -f holiday-api
	go clean
	@echo "✅ 清理完成"

migrate:
	@echo "📊 執行數據庫遷移..."
	go run ./cmd/migrate/main.go
	@echo "✅ 遷移完成"

docker-build:
	@echo "🐳 構建 Docker 鏡像..."
	docker build -t holiday-system:latest .
	@echo "✅ Docker 鏡像構建完成"

docker-run: docker-build
	@echo "🚀 運行 Docker 容器..."
	docker run -p 8080:8080 \
		-e HOLIDAY_DATABASE_HOST=localhost \
		-e HOLIDAY_DATABASE_PORT=5432 \
		-e HOLIDAY_DATABASE_USERNAME=postgres \
		-e HOLIDAY_DATABASE_PASSWORD=postgres \
		-e HOLIDAY_DATABASE_DBNAME=holiday_system \
		holiday-system:latest

docker-compose-up:
	@echo "🐳 使用 docker-compose 啟動..."
	docker-compose up -d
	@echo "✅ 容器已啟動"
	@echo "📍 API: http://localhost:8080"
	@echo "📍 數據庫: localhost:5432"

docker-compose-down:
	@echo "🛑 停止 docker-compose..."
	docker-compose down
	@echo "✅ 容器已停止"

docker-compose-logs:
	@echo "📋 查看日誌..."
	docker-compose logs -f

gcp-deploy:
	@echo "☁️  部署到 GCP..."
	bash deploy/gcp/deploy.sh
	@echo "✅ 部署完成"

gcp-cleanup:
	@echo "🧹 清理 GCP 資源..."
	bash deploy/gcp/cleanup.sh

lint:
	@echo "🔍 運行 linter..."
	golangci-lint run ./...
	@echo "✅ Lint 檢查完成"

fmt:
	@echo "📝 格式化代碼..."
	go fmt ./...
	@echo "✅ 代碼格式化完成"

dev: docker-compose-down docker-compose-up
	@echo "✅ 開發環境已啟動"
	@echo "📊 數據庫遷移..."
	sleep 2
	docker-compose exec -T api go run ./cmd/migrate/main.go

deps:
	@echo "📦 下載依賴..."
	go mod download
	go mod tidy
	@echo "✅ 依賴已更新"

version:
	@echo "版本信息"
	@go version
	@echo ""
	@echo "依賴："
	@go list -m all | head -10
