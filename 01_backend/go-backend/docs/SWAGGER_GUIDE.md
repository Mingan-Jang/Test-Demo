# Swagger API 文檔配置指南

本文檔說明如何配置和使用 Swagger API 文檔。

## 概述

本項目集成了 Swaggo (`swaggo/swag`) 和 Gin-Swagger (`swaggo/gin-swagger`)，提供自動生成和展示 API 文檔的能力。

### 重要文件

- `internal/docs/docs.go`: Swagger 元數據配置
- `internal/handler/handler.go`: 包含所有 API 端點及其 Swagger 註解
- `internal/app/app.go`: 應用程序中的 Swagger 路由配置

## 快速訪問

啟動應用後，訪問以下地址查看 API 文檔：

- **Swagger UI**: http://localhost:8080/swagger/index.html
- **Swagger JSON**: http://localhost:8080/swagger.json
- **Swagger YAML**: http://localhost:8080/swagger.yaml

## Swagger 標籤說明

### 1. 基本標籤

在 handler 函數上方添加以下註解：

```go
// @Summary 簡要描述
// @Description 詳細描述
// @Tags 標籤名稱
// @Produce json
// @Param 參數定義
// @Success 成功回應
// @Router 路由定義
func (h *HolidayHandler) GetHolidays(c *gin.Context) {
    // 實現
}
```

### 2. 完整標籤參考

#### @Summary

簡要描述端點的功能

```go
// @Summary 查詢假日資訊
```

#### @Description

詳細描述，可包含多行

```go
// @Description 根據日期和營運機構查詢假日信息，支持單日查詢和範圍查詢
```

#### @Tags

端點所屬的標籤，可指定多個

```go
// @Tags holidays
// @Tags holidays,查詢操作
```

#### @Produce

定義端點返回的內容類型

```go
// @Produce json
// @Produce xml
// @Produce text/plain
```

#### @Accept

定義端點接受的內容類型

```go
// @Accept json
// @Accept multipart/form-data
```

#### @Param

定義請求參數

**格式:**

```
// @Param paramName paramType paramLocation dataType required description "notes"
```

**參數類型 (paramType):**

- `query`: URL 查詢字符串參數
- `path`: URL 路徑參數
- `header`: HTTP 請求頭
- `body`: 請求體
- `form`: 表單數據

**數據類型 (dataType):**

- 基本類型: `string`, `integer`, `number`, `boolean`
- 複雜類型: `object`, `array`

**示例:**

```go
// 查詢參數
// @Param date query string false "查詢日期 (YYYY-MM-DD)"
// @Param operator query string false "營運機構名稱"
// @Param limit query integer false "返回結果數量限制" default(10)
// @Param offset query integer false "分頁偏移量" default(0)

// 路徑參數
// @Param id path string true "假日 ID"

// 請求頭
// @Param Authorization header string false "認證令牌" required

// 請求體
// @Param request body CreateHolidayRequest true "創建假日請求體"
```

#### @Success

定義成功響應

**格式:**

```
// @Success httpCode {returnType} returnStructName "description"
```

**示例:**

```go
// 成功返回數組
// @Success 200 {array} HolidayResponse "返回假日列表"

// 成功返回對象
// @Success 201 {object} HolidayResponse "成功創建假日"

// 成功返回簡單類型
// @Success 204 "成功刪除"

// 成功返回 map
// @Success 200 {object} map[string]interface{} "自定義響應格式"
```

#### @Failure

定義失敗響應

```go
// @Failure 400 {object} ErrorResponse "請求參數錯誤"
// @Failure 401 {object} ErrorResponse "未授權"
// @Failure 404 {object} ErrorResponse "資源不存在"
// @Failure 500 {object} ErrorResponse "服務器內部錯誤"
```

#### @Router

定義路由

**格式:**

```
// @Router /path [method]
```

**HTTP 方法:** `get`, `post`, `put`, `patch`, `delete`, `head`, `options`, `trace`

**示例:**

```go
// @Router /api/v1/holidays [get]
// @Router /api/v1/holidays [post]
// @Router /api/v1/holidays/{id} [put]
// @Router /api/v1/holidays/{id} [delete]
```

#### @ID

為操作定義唯一 ID

```go
// @ID getHolidaysByDate
```

#### @Deprecated

標記端點為已棄用

```go
// @Deprecated
```

#### @Security

定義安全方案

```go
// @Security ApiKeyAuth
// @Security OAuth2Implicit[read, write]
```

## 數據結構文檔

### 定義 Request/Response 結構

在結構體字段上添加註解：

```go
type CreateHolidayRequest struct {
	// 營運機構 ID
	OperatorID string `json:"operator_id" binding:"required" example:"op001"`

	// 假日日期 (YYYY-MM-DD)
	Date string `json:"date" binding:"required" example:"2024-12-25"`

	// 假日原因
	Reason string `json:"reason" binding:"required" example:"聖誕節"`

	// 假日類型 (1=自訂, 2=天災, 3=谷歌日曆, 4=周末)
	HolidayType int `json:"holiday_type" binding:"required" example:"1"`
}

type HolidayResponse struct {
	// 假日 ID
	ID string `json:"id" example:"h001"`

	// 營運機構 ID
	OperatorID string `json:"operator_id" example:"op001"`

	// 假日日期
	Date string `json:"date" example:"2024-12-25"`

	// 假日原因
	Reason string `json:"reason" example:"聖誕節"`

	// 創建時間
	CreatedAt string `json:"created_at" example:"2024-01-01T00:00:00Z"`
}

type ErrorResponse struct {
	// 錯誤代碼
	Code int `json:"code" example:"400"`

	// 錯誤信息
	Message string `json:"message" example:"請求參數無效"`
}
```

## 現有 API 端點文檔

### 1. 健康檢查

```go
// @Summary 健康檢查
// @Description 檢查服務健康狀態
// @Tags health
// @Produce json
// @Success 200 {object} map[string]string "服務正常"
// @Router /health [get]
func Health(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{"status": "ok"})
}
```

### 2. 查詢假日

```go
// @Summary 查詢假日資訊
// @Description 根據日期和營運機構查詢假日，支持單日查詢和範圍查詢
// @ID getHolidaysByDate
// @Tags holidays
// @Produce json
// @Param date query string false "查詢日期 (YYYY-MM-DD)" example("2024-12-25")
// @Param operator query string false "營運機構名稱" example("台灣鐵路管理局")
// @Param start_date query string false "開始日期 (YYYY-MM-DD)" example("2024-12-01")
// @Param end_date query string false "結束日期 (YYYY-MM-DD)" example("2024-12-31")
// @Success 200 {object} map[string]interface{} "查詢成功"
// @Failure 400 {object} ErrorResponse "請求參數無效"
// @Failure 404 {object} ErrorResponse "假日不存在"
// @Failure 500 {object} ErrorResponse "服務器錯誤"
// @Router /api/v1/holidays [get]
func (h *HolidayHandler) GetHolidays(c *gin.Context) {
	// 實現
}
```

### 3. 創建假日

```go
// @Summary 創建假日
// @Description 創建新的假日記錄
// @ID createHoliday
// @Tags holidays
// @Accept json
// @Produce json
// @Param request body CreateHolidayRequest true "創建假日請求體"
// @Success 201 {object} HolidayResponse "成功創建"
// @Failure 400 {object} ErrorResponse "請求參數無效"
// @Failure 500 {object} ErrorResponse "服務器錯誤"
// @Router /api/v1/holidays [post]
func (h *HolidayHandler) CreateHoliday(c *gin.Context) {
	// 實現
}
```

### 4. 更新假日

```go
// @Summary 更新假日
// @Description 更新現有假日記錄
// @ID updateHoliday
// @Tags holidays
// @Accept json
// @Produce json
// @Param id path string true "假日 ID" example("h001")
// @Param request body UpdateHolidayRequest true "更新假日請求體"
// @Success 200 {object} HolidayResponse "成功更新"
// @Failure 400 {object} ErrorResponse "請求參數無效"
// @Failure 404 {object} ErrorResponse "假日不存在"
// @Failure 500 {object} ErrorResponse "服務器錯誤"
// @Router /api/v1/holidays/{id} [put]
func (h *HolidayHandler) UpdateHoliday(c *gin.Context) {
	// 實現
}
```

### 5. 刪除假日

```go
// @Summary 刪除假日
// @Description 刪除假日記錄
// @ID deleteHoliday
// @Tags holidays
// @Produce json
// @Param id path string true "假日 ID" example("h001")
// @Success 204 "成功刪除"
// @Failure 404 {object} ErrorResponse "假日不存在"
// @Failure 500 {object} ErrorResponse "服務器錯誤"
// @Router /api/v1/holidays/{id} [delete]
func (h *HolidayHandler) DeleteHoliday(c *gin.Context) {
	// 實現
}
```

## 生成 Swagger 文檔

### 安裝 swag CLI

```bash
go install github.com/swaggo/swag/cmd/swag@latest
```

### 生成文檔

```bash
# 從源代碼生成 Swagger 文檔
swag init -g cmd/api/main.go

# 生成後會創建 docs/swagger.json 和 docs/swagger.yaml
```

### 自動生成

在 CI/CD 流程中自動執行：

```bash
# 在 build 前生成文檔
swag init -g cmd/api/main.go
go build ./cmd/api
```

## 進階配置

### 1. 認證配置

```go
// @securityDefinitions.apikey ApiKeyAuth
// @in header
// @name Authorization

// @securityDefinitions.basic BasicAuth

// @securityDefinitions.oauth2 OAuth2Implicit
// @authorizationUrl https://example.com/oauth/authorize
// @tokenUrl https://example.com/oauth/token
// @scope.read 讀取權限
// @scope.write 寫入權限

// 在特定端點使用
// @Security ApiKeyAuth
// @Security BasicAuth
// @Security OAuth2Implicit[read, write]
```

### 2. 響應頭

```go
// @Success 200 {object} HolidayResponse "成功"
// @Header 200 {string} X-Rate-Limit "請求速率限制"
// @Header 200 {string} X-Total-Count "總數"
```

### 3. 枚舉值

```go
type HolidayType int

const (
	// @enums
	// - 1
	// - 2
	// - 3
	// - 4
	Custom    HolidayType = 1 // 自訂假日
	Disaster  HolidayType = 2 // 天災假日
	GoogleCal HolidayType = 3 // 谷歌日曆
	Weekend   HolidayType = 4 // 周末
)
```

### 4. 分頁響應

```go
type PagedResponse struct {
	// 數據
	Data interface{} `json:"data"`

	// 分頁信息
	Pagination struct {
		// 當前頁碼
		Page int `json:"page"`

		// 每頁數量
		PageSize int `json:"page_size"`

		// 總記錄數
		Total int `json:"total"`

		// 總頁數
		TotalPages int `json:"total_pages"`
	} `json:"pagination"`
}

// @Success 200 {object} PagedResponse "查詢成功"
```

## 測試 API 文檔

### 1. 在 Swagger UI 中測試

1. 打開 http://localhost:8080/swagger/index.html
2. 選擇端點
3. 點擊 "Try it out"
4. 填入參數
5. 點擊 "Execute"
6. 查看響應

### 2. 在 Postman 中導入

1. 打開 Postman
2. File → Import → URL
3. 輸入: `http://localhost:8080/swagger.json`
4. 導入並測試

### 3. 通過 curl 測試

```bash
# 獲取 Swagger 定義
curl http://localhost:8080/swagger.json | jq

# 使用 Swagger 定義進行驗證
curl http://localhost:8080/swagger.json | \
  docker run -i swaggerapi/swagger-ui
```

## 常見問題

### Q1: 為什麼看不到 Swagger UI？

**A:** 檢查以下幾點：

1. 應用已啟動: `http://localhost:8080/health`
2. 導入正確: `_ "holiday-system/internal/docs"` 在 app.go 中
3. 路由已註冊: `a.Engine.GET("/swagger/*any", ginSwagger.WrapHandler(...))`

### Q2: 文檔沒有更新？

**A:** 需要重新生成文檔：

```bash
swag init -g cmd/api/main.go
go build ./cmd/api
```

### Q3: 參數在 Swagger 中顯示不正確？

**A:** 檢查參數註解的格式：

```go
// 正確
// @Param id path string true "假日 ID"

// 錯誤 (缺少參數名稱)
// @Param path string true "假日 ID"
```

### Q4: 如何隱藏某些端點？

**A:** 使用 `@deprecated` 或在模型上使用 `@exclude` 標籤：

```go
// @Deprecated
// @Summary 已棄用的端點
```

## 最佳實踐

1. **一致的命名**: 使用統一的端點名稱和參數名稱
2. **清晰的描述**: 提供具體的 @Summary 和 @Description
3. **完整的例子**: 使用 `example` 標籤提供示例值
4. **錯誤處理**: 定義所有可能的 @Failure 響應
5. **版本控制**: 在 @version 中標記 API 版本
6. **定期更新**: 每次修改 API 時更新文檔
7. **自動化測試**: 使用 API 文檔生成的 Postman collection 進行測試

## 相關資源

- [Swaggo 官方文檔](https://github.com/swaggo/swag)
- [Swagger/OpenAPI 規範](https://swagger.io/specification/)
- [Gin-Swagger 文檔](https://github.com/swaggo/gin-swagger)
- [OpenAPI 3.0 完整指南](https://swagger.io/docs/specification/3-0-0/)
