# 開發文檔索引

本項目包含完整的開發文檔。請根據您的需求選擇相應的指南。

## 📚 文檔總覽

### 核心文檔

| 文檔                                         | 目的                    | 適用場景                         |
| -------------------------------------------- | ----------------------- | -------------------------------- |
| [DOCKER_DEV_SETUP.md](./DOCKER_DEV_SETUP.md) | Docker 本地開發環境設置 | 想要使用 Docker 搭建開發環境     |
| [SWAGGER_GUIDE.md](./SWAGGER_GUIDE.md)       | Swagger API 文檔配置    | 想要了解 API 文檔和 Swagger 配置 |
| [TESTING_GUIDE.md](./TESTING_GUIDE.md)       | 完整測試指南            | 想要進行 API、數據庫和性能測試   |

### 項目文檔

| 文檔                                       | 內容               |
| ------------------------------------------ | ------------------ |
| [README.md](./README.md)                   | 項目概述和快速開始 |
| [QUICKSTART.md](./QUICKSTART.md)           | 快速開始指南       |
| [ARCHITECTURE.md](./ARCHITECTURE.md)       | 項目架構設計       |
| [GETTING_STARTED.md](./GETTING_STARTED.md) | 詳細入門指南       |
| [DEPLOYMENT.md](./DEPLOYMENT.md)           | GCP 部署指南       |
| [CHANGELOG.md](./CHANGELOG.md)             | 版本更新日誌       |

## 🚀 快速開始路徑

### 場景 1: 第一次使用，想快速啟動應用

```
1. 閱讀: README.md (2 分鐘)
2. 按照: QUICKSTART.md (5 分鐘)
3. 訪問: http://localhost:8080/swagger/index.html
```

### 場景 2: 想要深入學習架構

```
1. 閱讀: ARCHITECTURE.md (10 分鐘)
2. 閱讀: DOCKER_DEV_SETUP.md (15 分鐘)
3. 開始開發
```

### 場景 3: 想要編寫和測試 API

```
1. 閱讀: SWAGGER_GUIDE.md (15 分鐘)
2. 按照: TESTING_GUIDE.md (20 分鐘)
3. 使用 Swagger UI 進行交互式測試
```

### 場景 4: 想要部署到生產環境

```
1. 閱讀: DEPLOYMENT.md (20 分鐘)
2. 根據步驟在 GCP 上部署
```

## 📖 詳細文檔說明

### 1. Docker 本地開發環境設置 (DOCKER_DEV_SETUP.md)

**包含內容:**

- 前置條件驗證
- 快速開始 (3 步啟動)
- Docker Compose 配置詳解
- 常用命令參考
- 數據庫管理
- 故障排除
- 性能優化
- 與 CI/CD 集成

**何時使用:**

- 第一次搭建本地環境
- 想要了解 Docker Compose 配置
- 需要設置 PostgreSQL 本地開發數據庫
- 遇到 Docker 相關問題

**快速命令:**

```bash
# 啟動環境
docker-compose up -d

# 查看狀態
docker-compose ps

# 查看日誌
docker-compose logs -f

# 停止環境
docker-compose down
```

### 2. Swagger API 文檔配置 (SWAGGER_GUIDE.md)

**包含內容:**

- Swagger 概述和訪問地址
- 所有 Swagger 標籤的詳細說明
- 數據結構文檔編寫方法
- 現有 API 端點的完整文檔
- 安裝和生成文檔
- 進階配置 (認證、分頁、枚舉)
- 在 Swagger UI、Postman 中測試
- 常見問題解答

**何時使用:**

- 想要了解 API 有哪些端點
- 想要編寫 API 註解和文檔
- 需要在 Swagger UI 中測試 API
- 想要在 Postman 中導入 API 定義
- 需要自定義 API 文檔

**快速訪問:**

- Swagger UI: http://localhost:8080/swagger/index.html
- Swagger JSON: http://localhost:8080/swagger.json

### 3. 完整測試指南 (TESTING_GUIDE.md)

**包含內容:**

- 環境設置驗證
- 開發工作流 (三種方式)
- 編輯-構建-測試循環
- API 測試 (Swagger UI、curl、Postman)
- 數據庫測試和驗證
- 性能測試 (Apache Bench、Go 測試)
- E2E 集成測試
- 故障排除

**何時使用:**

- 想要編寫和運行測試
- 需要初始化測試數據
- 想要進行性能測試
- 需要完整的 E2E 測試
- 遇到測試相關問題

**快速命令:**

```bash
# API 測試
curl http://localhost:8080/health

# 數據庫連接
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db

# 運行單元測試
go test ./...
```

## 🔍 按用途查找文檔

### 我想要...

#### ...啟動開發環境

→ [DOCKER_DEV_SETUP.md](./DOCKER_DEV_SETUP.md#快速開始)

#### ...查看 API 文檔

→ 打開瀏覽器訪問 http://localhost:8080/swagger/index.html

#### ...測試某個 API 端點

→ [TESTING_GUIDE.md](./TESTING_GUIDE.md#api-測試)

#### ...初始化測試數據

→ [TESTING_GUIDE.md](./TESTING_GUIDE.md#初始化測試數據)

#### ...編寫 API 端點註解

→ [SWAGGER_GUIDE.md](./SWAGGER_GUIDE.md#swagger-標籤說明)

#### ...連接到 PostgreSQL 數據庫

→ [DOCKER_DEV_SETUP.md](./DOCKER_DEV_SETUP.md#連接到數據庫)

#### ...進行性能測試

→ [TESTING_GUIDE.md](./TESTING_GUIDE.md#性能測試)

#### ...部署到 GCP

→ [DEPLOYMENT.md](./DEPLOYMENT.md)

#### ...解決 Docker 問題

→ [DOCKER_DEV_SETUP.md](./DOCKER_DEV_SETUP.md#故障排除)

#### ...理解項目架構

→ [ARCHITECTURE.md](./ARCHITECTURE.md)

## 📋 常用命令速查表

### Docker 相關

```bash
# 啟動所有服務
docker-compose up -d

# 查看服務狀態
docker-compose ps

# 查看日誌
docker-compose logs -f api

# 進入 PostgreSQL
docker exec -it go-backend-postgres-1 psql -U postgres -d holiday_db

# 重啟服務
docker-compose restart

# 停止並清理
docker-compose down -v
```

### Go 編譯和測試

```bash
# 編譯 API
go build -o api.exe ./cmd/api

# 運行測試
go test ./...

# 代碼格式化
go fmt ./...

# 代碼分析
go vet ./...
```

### API 測試

```bash
# 健康檢查
curl http://localhost:8080/health

# 查詢假日
curl "http://localhost:8080/api/v1/holidays?date=2024-12-25"

# 創建自訂假日
curl -X POST http://localhost:8080/api/v1/custom-holidays \
  -H "Content-Type: application/json" \
  -d '{"operator_id":"op001","holiday_date":"2024-12-31","reason":"測試"}'
```

### 數據庫操作

```bash
# 連接數據庫
psql -h localhost -U postgres -d holiday_db

# 查看所有表
\dt

# 查詢數據
SELECT * FROM holiday_operator LIMIT 10;

# 查看表結構
\d holiday_operator
```

## 🛠️ 開發環境要求

### 最小要求

- **Go**: 1.21 或更高
- **PostgreSQL**: 15 或更高
- **Docker**: 20.x 或更高
- **docker-compose**: 2.x 或更高

### 驗證安裝

```bash
go version
docker --version
docker-compose --version
psql --version
```

## 📞 獲取幫助

### 文檔查找流程

1. **問題描述** → 在本文檔中搜索關鍵詞
2. **根據場景** → 選擇相應的文檔
3. **查看目錄** → 快速找到相關章節
4. **常見問題** → 查看 FAQ 和故障排除

### 常見問題

**Q: 我應該先閱讀哪個文檔?**
A: 建議先讀 [README.md](./README.md)，然後根據需要選擇其他文檔。

**Q: 文檔多久更新一次?**
A: 每當有新功能或重要更改時更新。查看 [CHANGELOG.md](./CHANGELOG.md) 了解最新更新。

**Q: 文檔是否涵蓋所有功能?**
A: 是的，核心功能都有涵蓋。查看相應文檔的目錄獲取完整列表。

## 🎓 學習路徑

### 初級 (1-2 小時)

1. README.md - 了解項目
2. QUICKSTART.md - 快速啟動
3. 在 Swagger UI 中測試 API

### 中級 (2-4 小時)

1. ARCHITECTURE.md - 了解架構
2. DOCKER_DEV_SETUP.md - 深入 Docker 配置
3. SWAGGER_GUIDE.md - 編寫 API 文檔

### 高級 (4+ 小時)

1. TESTING_GUIDE.md - 完整測試
2. DEPLOYMENT.md - 部署到生產
3. 源代碼分析和自定義開發

## 📊 文檔統計

| 文檔                | 行數       | 章節數  | 預估閱讀時間  |
| ------------------- | ---------- | ------- | ------------- |
| README.md           | ~200       | 8       | 5 分鐘        |
| QUICKSTART.md       | ~150       | 6       | 5 分鐘        |
| ARCHITECTURE.md     | ~300       | 10      | 10 分鐘       |
| DOCKER_DEV_SETUP.md | ~800       | 15      | 30 分鐘       |
| SWAGGER_GUIDE.md    | ~600       | 12      | 25 分鐘       |
| TESTING_GUIDE.md    | ~900       | 20      | 40 分鐘       |
| DEPLOYMENT.md       | ~400       | 10      | 20 分鐘       |
| GETTING_STARTED.md  | ~350       | 12      | 15 分鐘       |
| **總計**            | **~3,700** | **~93** | **~150 分鐘** |

## 🔗 相關資源

### 官方文檔

- [Go Documentation](https://golang.org/doc/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)
- [Gin Web Framework](https://gin-gonic.com/)
- [GORM](https://gorm.io/)

### 工具

- [Swagger Editor](https://editor.swagger.io/)
- [Postman](https://www.postman.com/)
- [DBeaver](https://dbeaver.io/)
- [VSCode](https://code.visualstudio.com/)

### 社區

- GitHub Issues
- Gin 社區
- PostgreSQL 社區

## ✅ 清單

使用此清單確保您已完成所有必要步驟：

- [ ] 閱讀 README.md
- [ ] 安裝所有前置條件
- [ ] 按照 QUICKSTART.md 啟動應用
- [ ] 訪問 Swagger UI 查看 API
- [ ] 初始化測試數據
- [ ] 運行所有測試
- [ ] 閱讀 ARCHITECTURE.md 了解設計
- [ ] 准備部署文檔 (如需)

---

**最後更新**: 2024 年
**文檔版本**: 1.0
**適用版本**: Holiday System v0.1.0+
