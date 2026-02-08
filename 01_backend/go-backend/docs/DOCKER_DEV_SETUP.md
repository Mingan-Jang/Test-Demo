# Docker 本地開發環境設置指南

本文檔說明如何使用 Docker 和 docker-compose 在本地快速搭建開發環境進行測試。

## 前置條件

1. **安裝 Docker**: https://www.docker.com/products/docker-desktop
2. **安裝 docker-compose**: 通常與 Docker Desktop 一起安裝
3. **Go 1.21+** (本地開發編譯時需要)
4. **PostgreSQL 客戶端工具** (可選，用於直接查詢數據庫)

### 驗證安裝

```bash
docker --version
docker-compose --version
```

## 快速開始

### 1. 啟動開發環境

```bash
# 進入項目目錄
cd 01_backend/go-backend

# 啟動所有服務 (PostgreSQL + API)
docker-compose up -d

# 查看服務狀態
docker-compose ps
```

**預期輸出:**

```
NAME                COMMAND                  SERVICE             STATUS              PORTS
go-backend-api-1    "/app/api"              api                 Up 2 seconds        0.0.0.0:8080->8080/tcp
go-backend-postgres-1   "docker-entrypoint.s…"   postgres        Up 3 seconds        0.0.0.0:5432->5432/tcp
```

### 2. 驗證服務

#### 查看 API 健康狀態

```bash
# 健康檢查
curl http://localhost:8080/health

# 預期回應:
# {"status":"ok"}
```

#### 查看 Swagger API 文檔

在瀏覽器打開: http://localhost:8080/swagger/index.html

#### 檢查 PostgreSQL 連接

```bash
# 使用 Docker 執行 PostgreSQL 客戶端
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db -c "\dt"

# 或使用本地 psql 工具
psql -h localhost -U postgres -d holiday_db -c "\dt"
```

**連接參數:**

- Host: `localhost`
- Port: `5432`
- User: `postgres`
- Password: `postgres`
- Database: `holiday_db`

## Docker Compose 配置詳解

### 服務定義 (docker-compose.yml)

#### PostgreSQL 服務

```yaml
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: holiday_db
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
```

**重要參數:**

- `POSTGRES_DB`: 初始數據庫名稱
- `volumes`: 持久化存儲位置 (本機路徑: `postgres_data`)
- `ports`: 容器內 5432 映射到本機 5432

#### API 服務

```yaml
api:
  build:
    context: .
    dockerfile: Dockerfile
  environment:
    DATABASE_URL: "postgres://postgres:postgres@postgres:5432/holiday_db?sslmode=disable"
    SERVER_HOST: "0.0.0.0"
    SERVER_PORT: "8080"
  ports:
    - "8080:8080"
  depends_on:
    - postgres
```

**重要參數:**

- `depends_on`: 確保 PostgreSQL 先啟動
- `DATABASE_URL`: 容器內的數據庫連接字符串 (使用服務名 `postgres`)
- `SERVER_HOST`: `0.0.0.0` 允許外部訪問

## 常用 Docker 命令

### 查看日誌

```bash
# 查看所有服務日誌
docker-compose logs -f

# 只查看 API 服務日誌
docker-compose logs -f api

# 只查看 PostgreSQL 日誌
docker-compose logs -f postgres

# 查看最後 100 行
docker-compose logs --tail=100
```

### 進入容器

```bash
# 進入 API 容器的 shell
docker exec -it go-backend-api-1 sh

# 進入 PostgreSQL 容器
docker exec -it go-backend-postgres-1 bash

# 在 PostgreSQL 中執行查詢
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db
```

### 重啟服務

```bash
# 重啟所有服務
docker-compose restart

# 重啟單個服務
docker-compose restart api

# 停止服務
docker-compose stop

# 啟動已停止的服務
docker-compose start

# 完全刪除容器和卷 (謹慎操作)
docker-compose down -v
```

### 清理資源

```bash
# 移除未使用的容器
docker container prune -f

# 移除未使用的鏡像
docker image prune -f

# 移除所有未使用資源
docker system prune -a -f
```

## 數據庫管理

### 連接到數據庫

#### 使用 Docker exec

```bash
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db
```

#### 使用本地 psql

```bash
psql -h localhost -U postgres -d holiday_db
```

#### 使用 DBeaver/pgAdmin (GUI)

**連接配置:**

- 主機: `localhost`
- 端口: `5432`
- 用戶名: `postgres`
- 密碼: `postgres`
- 數據庫: `holiday_db`

### 常用數據庫命令

```sql
-- 查看所有表
\dt

-- 查看特定表結構
\d holiday_operator

-- 查看表數據
SELECT * FROM holiday_operator LIMIT 10;

-- 查看表數據量
SELECT COUNT(*) FROM holiday_operator;

-- 查看所有 schema
\dn

-- 退出 psql
\q
```

### 初始化測試數據

```bash
# 連接到 PostgreSQL
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db

# 執行插入命令
INSERT INTO sys.holiday_operator (id, operator_name, created_at)
VALUES
  ('op001', '台灣鐵路管理局', NOW()),
  ('op002', '高速公路局', NOW()),
  ('op003', '台北捷運公司', NOW());

INSERT INTO sys.holiday_operator_custom (id, operator_id, holiday_date, reason, created_at)
VALUES
  ('custom001', 'op001', '2024-12-31', '年度業務調整', NOW()),
  ('custom002', 'op002', '2025-01-01', '特別假日', NOW());
```

## API 測試指南

### 通過 Swagger UI 測試

1. 打開: http://localhost:8080/swagger/index.html
2. 選擇要測試的端點
3. 點擊 "Try it out"
4. 填入參數
5. 點擊 "Execute"

### 通過 curl 測試

#### 健康檢查

```bash
curl -X GET http://localhost:8080/health
```

#### 查詢假日信息

```bash
curl -X GET "http://localhost:8080/api/v1/holidays?date=2024-12-31&operator=op001"
```

#### 創建自訂假日

```bash
curl -X POST http://localhost:8080/api/v1/custom-holidays \
  -H "Content-Type: application/json" \
  -d '{
    "operator_id": "op001",
    "holiday_date": "2024-12-31",
    "reason": "年度調整"
  }'
```

#### 查詢日期範圍內的假日

```bash
curl -X GET "http://localhost:8080/api/v1/holidays?start_date=2024-12-01&end_date=2024-12-31"
```

### 通過 Postman 測試

1. 導入 Swagger 定義: `http://localhost:8080/swagger.json`
2. 在 Postman 中創建新的 collection
3. 配置環境變數:
   - `base_url`: `http://localhost:8080`
   - `api_version`: `v1`
4. 創建請求並測試

## 開發工作流

### 本地編譯和測試

```bash
# 編譯 API
go build -o api ./cmd/api

# 編譯數據庫遷移工具
go build -o migrate ./cmd/migrate

# 運行單元測試
go test ./...

# 運行覆蓋率測試
go test -cover ./...
```

### 使用 Docker 進行開發

#### 方式 1: 使用本地編譯的二進制

```bash
# 1. 編譯
go build -o api ./cmd/api

# 2. 停止現有容器
docker-compose down

# 3. 重建鏡像
docker-compose build

# 4. 啟動新容器
docker-compose up -d

# 5. 檢查日誌
docker-compose logs -f api
```

#### 方式 2: 使用開發環境容器

創建 `docker-compose.dev.yml`:

```yaml
version: "3.8"

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: holiday_db
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - holiday-network

  # Go 開發容器 (不運行應用，只提供環境)
  go-dev:
    image: golang:1.21-alpine
    working_dir: /app
    volumes:
      - .:/app
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      CGO_ENABLED: 0
      GOOS: linux
      GOARCH: amd64
    networks:
      - holiday-network

volumes:
  postgres_data:

networks:
  holiday-network:
    driver: bridge
```

使用開發環境:

```bash
# 啟動開發環境
docker-compose -f docker-compose.dev.yml up -d

# 進入 Go 容器
docker exec -it go-backend-go-dev-1 sh

# 在容器內編譯和運行
go run ./cmd/api/main.go
```

## 故障排除

### 問題 1: 端口被佔用

**症狀:** `Error starting userland proxy: listen tcp4 0.0.0.0:5432`

**解決方案:**

```bash
# 查找佔用端口的進程
lsof -i :5432

# 或在 Windows 上
netstat -ano | findstr :5432

# 停止占用的進程，或使用不同的端口
# 編輯 docker-compose.yml，將 5432:5432 改為 5433:5432
```

### 問題 2: 數據庫連接失敗

**症狀:** `connection refused` 或 `database does not exist`

**解決方案:**

```bash
# 1. 檢查 PostgreSQL 容器是否運行
docker-compose ps

# 2. 查看 PostgreSQL 日誌
docker-compose logs postgres

# 3. 驗證數據庫是否創建
docker exec -it go-backend-postgres-1 psql -U postgres -l

# 4. 如果需要重新初始化
docker-compose down -v
docker-compose up -d
```

### 問題 3: API 容器無法啟動

**症狀:** API 容器立即退出

**解決方案:**

```bash
# 查看詳細日誌
docker-compose logs api

# 檢查構建日誌
docker-compose build --no-cache api

# 進入容器進行調試
docker run -it --rm \
  --network host \
  -e DATABASE_URL="postgres://postgres:postgres@localhost:5432/holiday_db" \
  go-backend-api:latest sh
```

### 問題 4: 持久化存儲問題

**症狀:** 重啟後數據丟失

**解決方案:**

```bash
# 檢查卷是否存在
docker volume ls

# 查看卷詳細信息
docker volume inspect go-backend_postgres_data

# 重新創建卷
docker-compose down -v
docker-compose up -d
```

## 性能優化

### 1. 資源限制

在 `docker-compose.yml` 中添加:

```yaml
services:
  api:
    # ... 其他配置
    deploy:
      resources:
        limits:
          cpus: "1"
          memory: 512M
        reservations:
          cpus: "0.5"
          memory: 256M

  postgres:
    # ... 其他配置
    deploy:
      resources:
        limits:
          cpus: "2"
          memory: 1G
        reservations:
          cpus: "1"
          memory: 512M
```

### 2. 優化 PostgreSQL

```bash
# 進入 PostgreSQL 容器
docker exec -it go-backend-postgres-1 psql -U postgres

# 檢查當前配置
SHOW shared_buffers;
SHOW effective_cache_size;

# 查看慢查詢
SELECT * FROM pg_stat_statements;
```

### 3. 監控資源使用

```bash
# 實時監控容器資源使用
docker stats

# 查看容器詳細信息
docker inspect go-backend-api-1 | grep -A 20 '"HostConfig"'
```

## 高級配置

### 自定義環境變數

創建 `.env` 文件:

```env
# Database
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=holiday_db

# Server
SERVER_HOST=0.0.0.0
SERVER_PORT=8080

# Logging
LOG_LEVEL=debug

# API
API_TIMEOUT=30s
```

在 `docker-compose.yml` 中引用:

```yaml
services:
  api:
    environment:
      - POSTGRES_USER=${POSTGRES_USER}
      - POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
      - POSTGRES_DB=${POSTGRES_DB}
      - SERVER_HOST=${SERVER_HOST}
      - SERVER_PORT=${SERVER_PORT}
```

### 使用多個配置文件

```bash
# 使用多個 compose 文件
docker-compose \
  -f docker-compose.yml \
  -f docker-compose.override.yml \
  up -d
```

## 與持續集成的集成

### GitHub Actions 示例

```yaml
name: Test with Docker

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest

    services:
      postgres:
        image: postgres:15-alpine
        env:
          POSTGRES_PASSWORD: postgres
          POSTGRES_DB: holiday_db
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432

    steps:
      - uses: actions/checkout@v3

      - name: Set up Go
        uses: actions/setup-go@v4
        with:
          go-version: "1.21"

      - name: Run tests
        run: go test -v ./...
        env:
          DATABASE_URL: postgres://postgres:postgres@localhost:5432/holiday_db?sslmode=disable
```

## 完整工作流示例

### 日常開發流程

```bash
# 1. 啟動環境
docker-compose up -d

# 2. 檢查服務
docker-compose ps

# 3. 查看日誌
docker-compose logs -f

# 4. 在本地編輯代碼...

# 5. 重新構建容器
docker-compose build

# 6. 重啟服務
docker-compose up -d

# 7. 測試 API
curl http://localhost:8080/health

# 8. 查看 Swagger 文檔
# 打開瀏覽器: http://localhost:8080/swagger/index.html

# 9. 停止環境
docker-compose down
```

### 完整測試流程

```bash
# 1. 清理環境
docker-compose down -v

# 2. 構建新鏡像
docker-compose build --no-cache

# 3. 啟動服務
docker-compose up -d

# 4. 等待服務就緒
sleep 5

# 5. 檢查健康狀態
curl http://localhost:8080/health

# 6. 初始化測試數據
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db << EOF
INSERT INTO sys.holiday_operator (id, operator_name, created_at)
VALUES ('op001', 'Test Operator', NOW());
EOF

# 7. 運行測試
go test -v -race ./...

# 8. 查看 Swagger
open http://localhost:8080/swagger/index.html

# 9. 清理
docker-compose down
```

## 更多資源

- [Docker 官方文檔](https://docs.docker.com/)
- [docker-compose 官方文檔](https://docs.docker.com/compose/)
- [PostgreSQL 官方文檔](https://www.postgresql.org/docs/)
- [Gin Web Framework](https://gin-gonic.com/)
- [GORM 文檔](https://gorm.io/)
