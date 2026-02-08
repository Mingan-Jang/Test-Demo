# 本地開發和測試完整指南

本文檔提供關於如何使用 Docker 和 Swagger 進行完整的開發和測試工作流程。

## 目錄

1. [環境設置](#環境設置)
2. [開發工作流](#開發工作流)
3. [API 測試](#api-測試)
4. [數據庫測試](#數據庫測試)
5. [性能測試](#性能測試)
6. [集成測試](#集成測試)
7. [故障排除](#故障排除)

## 環境設置

### 第一步: 驗證前置條件

```bash
# 驗證 Go 版本
go version
# 預期: go version go1.21 或更高

# 驗證 Docker
docker --version
# 預期: Docker version 20.x 或更高

# 驗證 docker-compose
docker-compose --version
# 預期: Docker Compose version 2.x 或更高
```

### 第二步: 配置項目

```bash
# 進入項目目錄
cd c:\Projects\Test-Demo\01_backend\go-backend

# 驗證項目結構
ls -la
# 應包含: cmd/, internal/, config/, docker-compose.yml, Dockerfile, go.mod
```

### 第三步: 構建 Docker 鏡像

```bash
# 構建應用鏡像
docker build -t holiday-system-api:latest .

# 驗證鏡像
docker image ls | grep holiday-system
```

## 開發工作流

### 快速開發循環

#### 方式 1: 使用 Docker Compose (推薦)

```bash
# 1. 啟動所有服務
docker-compose up -d

# 2. 驗證服務
docker-compose ps

# 3. 查看日誌 (實時跟蹤)
docker-compose logs -f

# 4. 修改代碼...

# 5. 重新構建鏡像
docker-compose build

# 6. 重新啟動服務
docker-compose up -d

# 7. 驗證更改
curl http://localhost:8080/health
```

#### 方式 2: 本地編譯 + Docker 數據庫

適合快速迭代開發：

```bash
# 1. 啟動 PostgreSQL 容器
docker-compose up -d postgres

# 2. 驗證 PostgreSQL 連接
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db -c "\dt"

# 3. 本地編譯
go build -o api.exe .\cmd\api\

# 4. 本地運行
$env:DATABASE_URL="postgres://postgres:postgres@localhost:5432/holiday_db?sslmode=disable"
.\api.exe

# 5. 測試 API
curl http://localhost:8080/health
```

### 編輯 → 構建 → 測試循環

```bash
#!/bin/bash
# 完整開發循環腳本

echo "1. 編譯代碼..."
go build -o api.exe .\cmd\api\

echo "2. 運行單元測試..."
go test ./...

echo "3. 代碼格式化..."
go fmt ./...

echo "4. 代碼分析..."
go vet ./...

echo "5. 構建 Docker 鏡像..."
docker build -t holiday-system-api:dev .

echo "6. 啟動服務..."
docker-compose up -d

echo "7. 等待服務啟動..."
sleep 5

echo "8. 驗證服務..."
curl http://localhost:8080/health

echo "✓ 開發循環完成"
```

## API 測試

### 通過 Swagger UI 測試 (最簡單)

1. **啟動應用**

   ```bash
   docker-compose up -d
   ```

2. **打開瀏覽器**

   - 訪問: http://localhost:8080/swagger/index.html

3. **測試端點**
   - 選擇端點 (如 `GET /api/v1/holidays`)
   - 點擊 "Try it out"
   - 填入參數 (如 `date=2024-12-25`)
   - 點擊 "Execute"
   - 查看響應

### 通過 curl 測試 (自動化)

#### 基本操作

```bash
# 健康檢查
curl -X GET http://localhost:8080/health

# 查詢假日 (單日)
curl -X GET "http://localhost:8080/api/v1/holidays?date=2024-12-25"

# 查詢假日 (範圍)
curl -X GET "http://localhost:8080/api/v1/holidays?start_date=2024-12-01&end_date=2024-12-31"

# 查詢假日 (按營運機構)
curl -X GET "http://localhost:8080/api/v1/holidays?date=2024-12-25&operator=taiwan-railway"
```

#### 創建數據

```bash
# 創建自訂假日
curl -X POST http://localhost:8080/api/v1/custom-holidays \
  -H "Content-Type: application/json" \
  -d '{
    "operator_id": "op001",
    "holiday_date": "2024-12-31",
    "reason": "年度調整"
  }'

# 創建天災假日
curl -X POST http://localhost:8080/api/v1/disaster-holidays \
  -H "Content-Type: application/json" \
  -d '{
    "location": "台北市",
    "holiday_date": "2024-12-15",
    "disaster_type": "颱風",
    "description": "颱風來襲"
  }'
```

#### 更新數據

```bash
# 更新自訂假日
curl -X PUT http://localhost:8080/api/v1/custom-holidays/custom001 \
  -H "Content-Type: application/json" \
  -d '{
    "operator_id": "op001",
    "holiday_date": "2024-12-31",
    "reason": "年度業務調整"
  }'
```

#### 刪除數據

```bash
# 刪除自訂假日
curl -X DELETE http://localhost:8080/api/v1/custom-holidays/custom001

# 刪除天災假日
curl -X DELETE http://localhost:8080/api/v1/disaster-holidays/disaster001
```

### 通過 Postman 測試 (專業方式)

1. **導入 API 定義**

   - 打開 Postman
   - File → Import → URL
   - 輸入: `http://localhost:8080/swagger.json`
   - 點擊 Import

2. **配置環境變數**

   - Environments → New Environment
   - 添加變數:
     ```
     {
       "base_url": "http://localhost:8080",
       "api_version": "v1",
       "operator_id": "op001",
       "date": "2024-12-25"
     }
     ```

3. **創建測試集合**

   - 新建 Collection: "Holiday System Tests"
   - 添加 Requests
   - 配置 Tests (驗證)

4. **示例 Test 腳本**

   ```javascript
   // Tests tab 中
   pm.test("Status code is 200", function () {
     pm.response.to.have.status(200);
   });

   pm.test("Response has correct structure", function () {
     var jsonData = pm.response.json();
     pm.expect(jsonData).to.have.property("data");
   });

   pm.test("Response time is acceptable", function () {
     pm.expect(pm.response.responseTime).to.be.below(1000);
   });
   ```

## 數據庫測試

### 連接數據庫

#### 方式 1: 使用 docker exec

```bash
# 進入 PostgreSQL 客戶端
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db

# 常用命令
\dt                              # 列出所有表
\d holiday_operator              # 查看表結構
SELECT * FROM holiday_operator;  # 查詢數據
\q                               # 退出
```

#### 方式 2: 使用本地 psql

```bash
# 連接配置
psql \
  --host=localhost \
  --port=5432 \
  --username=postgres \
  --dbname=holiday_db
```

#### 方式 3: 使用 GUI 工具 (DBeaver)

1. 下載並安裝 [DBeaver Community](https://dbeaver.io/)
2. 創建新連接:
   - Database Type: PostgreSQL
   - Host: localhost
   - Port: 5432
   - Database: holiday_db
   - Username: postgres
   - Password: postgres
3. 連接並瀏覽數據

### 初始化測試數據

#### 腳本 1: 基本數據初始化

```bash
# 創建文件: init-test-data.sql
cat > init-test-data.sql << 'EOF'
-- 清除現有數據 (測試環境)
DELETE FROM sys.holiday_operator_custom;
DELETE FROM sys.holiday_disaster;
DELETE FROM sys.holiday_operator_loct;
DELETE FROM sys.holiday_operator;

-- 插入測試營運機構
INSERT INTO sys.holiday_operator (id, operator_name, created_at)
VALUES
  ('op001', '台灣鐵路管理局', NOW()),
  ('op002', '高速公路局', NOW()),
  ('op003', '台北捷運公司', NOW()),
  ('op004', '台中捷運公司', NOW()),
  ('op005', '高雄捷運公司', NOW());

-- 插入自訂假日
INSERT INTO sys.holiday_operator_custom
  (id, operator_id, holiday_date, reason, created_at)
VALUES
  ('custom001', 'op001', '2024-12-31', '年度業務調整', NOW()),
  ('custom002', 'op002', '2024-12-31', '特別假日', NOW()),
  ('custom003', 'op001', '2024-01-01', '開國紀念日', NOW());

-- 插入天災假日
INSERT INTO sys.holiday_disaster
  (id, location, holiday_date, disaster_type, description, created_at)
VALUES
  ('disaster001', '台北市', '2024-12-15', '颱風', '颱風來襲', NOW()),
  ('disaster002', '台中市', '2024-12-16', '地震', '大地震', NOW());

-- 驗證數據
SELECT COUNT(*) as total_operators FROM sys.holiday_operator;
SELECT COUNT(*) as total_custom FROM sys.holiday_operator_custom;
SELECT COUNT(*) as total_disaster FROM sys.holiday_disaster;
EOF

# 執行初始化腳本
docker exec -i go-backend-postgres-1 psql -U postgres -d holiday_db < init-test-data.sql
```

#### 腳本 2: 性能測試數據

```bash
# 創建大量測試數據
cat > init-perf-data.sql << 'EOF'
-- 插入 1000 條自訂假日記錄用於性能測試
INSERT INTO sys.holiday_operator_custom
  (id, operator_id, holiday_date, reason, created_at)
SELECT
  'custom_' || generate_series || '_' || random()::text,
  'op00' || (generate_series % 5 + 1)::text,
  '2024-01-01'::date + (generate_series % 365) * INTERVAL '1 day',
  '測試假日 ' || generate_series,
  NOW()
FROM generate_series(1, 1000);

-- 驗證
SELECT COUNT(*) FROM sys.holiday_operator_custom;
EOF

docker exec -i go-backend-postgres-1 psql -U postgres -d holiday_db < init-perf-data.sql
```

### 數據庫驗證查詢

```sql
-- 查看所有假日信息
SELECT
  id,
  operator_id,
  holiday_date,
  reason,
  created_at
FROM sys.holiday_operator_custom
ORDER BY holiday_date DESC;

-- 查看特定日期的假日
SELECT * FROM sys.holiday_operator_custom
WHERE holiday_date = '2024-12-31';

-- 按營運機構統計假日數
SELECT
  ho.operator_name,
  COUNT(hoc.id) as holiday_count
FROM sys.holiday_operator ho
LEFT JOIN sys.holiday_operator_custom hoc ON ho.id = hoc.operator_id
GROUP BY ho.operator_name;

-- 查看數據庫大小
SELECT pg_database.datname, pg_size_pretty(pg_database_size(pg_database.datname))
FROM pg_database
WHERE datname = 'holiday_db';

-- 查看表空間使用
SELECT
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables
WHERE schemaname = 'sys';
```

## 性能測試

### Apache Bench (ab) 測試

```bash
# 安装 (Windows)
# 或使用 WSL/Git Bash

# 運行 100 個請求，10 個並發
ab -n 100 -c 10 http://localhost:8080/health

# 詳細輸出
ab -n 1000 -c 50 -g results.tsv http://localhost:8080/api/v1/holidays?date=2024-12-25
```

### 使用 Go 進行性能測試

```go
// file: load_test.go
package main

import (
	"fmt"
	"sync"
	"testing"
	"time"

	"github.com/valyala/fasthttp"
)

func BenchmarkHolidayAPI(b *testing.B) {
	client := &fasthttp.Client{}

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		req := fasthttp.AcquireRequest()
		req.SetRequestURI("http://localhost:8080/api/v1/holidays?date=2024-12-25")

		resp := fasthttp.AcquireResponse()
		client.Do(req, resp)

		fasthttp.ReleaseRequest(req)
		fasthttp.ReleaseResponse(resp)
	}
}

func TestConcurrentLoad(t *testing.T) {
	const (
		numGoroutines = 100
		requestsPerGo = 50
	)

	var wg sync.WaitGroup
	start := time.Now()

	for g := 0; g < numGoroutines; g++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			client := &fasthttp.Client{}
			for r := 0; r < requestsPerGo; r++ {
				req := fasthttp.AcquireRequest()
				req.SetRequestURI("http://localhost:8080/api/v1/holidays?date=2024-12-25")
				resp := fasthttp.AcquireResponse()
				client.Do(req, resp)
				fasthttp.ReleaseRequest(req)
				fasthttp.ReleaseResponse(resp)
			}
		}()
	}

	wg.Wait()
	elapsed := time.Since(start)
	totalRequests := numGoroutines * requestsPerGo
	rps := float64(totalRequests) / elapsed.Seconds()
	fmt.Printf("Total: %d requests in %.2fs, %.0f req/s\n", totalRequests, elapsed.Seconds(), rps)
}
```

運行測試:

```bash
go test -bench=. -benchmem -run=^$
go test -v -run TestConcurrentLoad
```

## 集成測試

### E2E 測試場景

```bash
#!/bin/bash
# file: e2e-test.sh

set -e

BASE_URL="http://localhost:8080"
API_URL="$BASE_URL/api/v1"

echo "🧪 開始 E2E 測試..."

# 1. 健康檢查
echo "1️⃣  測試健康檢查..."
HEALTH=$(curl -s "$BASE_URL/health")
echo "✓ 健康檢查通過: $HEALTH"

# 2. 創建自訂假日
echo "2️⃣  測試創建自訂假日..."
CUSTOM_ID=$(curl -s -X POST "$API_URL/custom-holidays" \
  -H "Content-Type: application/json" \
  -d '{
    "operator_id": "op001",
    "holiday_date": "2024-12-31",
    "reason": "測試假日"
  }' | jq -r '.id // empty')

if [ -z "$CUSTOM_ID" ]; then
  echo "✗ 創建自訂假日失敗"
  exit 1
fi
echo "✓ 成功創建自訂假日: $CUSTOM_ID"

# 3. 查詢剛創建的假日
echo "3️⃣  測試查詢假日..."
HOLIDAY=$(curl -s "$API_URL/holidays?date=2024-12-31")
echo "✓ 查詢成功: $HOLIDAY"

# 4. 更新假日
echo "4️⃣  測試更新假日..."
curl -s -X PUT "$API_URL/custom-holidays/$CUSTOM_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "operator_id": "op001",
    "holiday_date": "2024-12-31",
    "reason": "更新後的假日"
  }' > /dev/null
echo "✓ 更新成功"

# 5. 刪除假日
echo "5️⃣  測試刪除假日..."
curl -s -X DELETE "$API_URL/custom-holidays/$CUSTOM_ID" > /dev/null
echo "✓ 刪除成功"

echo ""
echo "✅ 所有 E2E 測試通過!"
```

運行:

```bash
chmod +x e2e-test.sh
./e2e-test.sh
```

## 故障排除

### 常見問題

#### 1. PostgreSQL 連接失敗

```bash
# 檢查容器狀態
docker-compose ps

# 查看日誌
docker-compose logs postgres

# 檢查端口
netstat -ano | findstr 5432

# 重新啟動
docker-compose down
docker-compose up -d postgres
```

#### 2. API 無法啟動

```bash
# 查看詳細日誌
docker-compose logs -f api

# 檢查構建
docker-compose build --no-cache api

# 進入容器調試
docker run -it go-backend-api:latest sh
```

#### 3. Swagger UI 不可用

```bash
# 檢查路由
curl http://localhost:8080/swagger/index.html

# 檢查日誌
docker-compose logs api | grep -i swagger

# 重新構建
go build ./cmd/api
docker-compose build
```

## 快速參考

| 任務         | 命令                                                                   |
| ------------ | ---------------------------------------------------------------------- |
| 啟動環境     | `docker-compose up -d`                                                 |
| 查看日誌     | `docker-compose logs -f`                                               |
| 停止環境     | `docker-compose down`                                                  |
| 健康檢查     | `curl http://localhost:8080/health`                                    |
| Swagger 文檔 | http://localhost:8080/swagger/index.html                               |
| 連接數據庫   | `docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db` |
| 本地編譯     | `go build -o api.exe ./cmd/api`                                        |
| 運行測試     | `go test ./...`                                                        |
