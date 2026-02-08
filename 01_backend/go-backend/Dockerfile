# 構建階段
FROM golang:1.21-alpine AS builder

WORKDIR /app

# 安裝構建依賴
RUN apk add --no-cache git

# 複製 go mod 文件
COPY go.mod go.sum ./

# 下載依賴
RUN go mod download

# 複製源代碼
COPY . .

# 安裝 swag CLI 並生成文檔
RUN go install github.com/swaggo/swag/cmd/swag@latest && \
    $(go env GOPATH)/bin/swag init -g cmd/api/main.go

# 構建應用
RUN CGO_ENABLED=0 GOOS=linux go build -o holiday-api ./cmd/api/main.go

# 運行階段
FROM alpine:latest

WORKDIR /app

# 安裝時區數據
RUN apk --no-cache add tzdata

# 從構建階段複製二進制文件
COPY --from=builder /app/holiday-api .

# 複製配置文件
COPY config/ ./config/

# 複製 Swagger 文檔
COPY docs/swagger.json ./docs/
COPY docs/swagger.yaml ./docs/
COPY docs/docs.go ./docs/

# 暴露端口
EXPOSE 8080

# 健康檢查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/health || exit 1

# 運行應用
CMD ["./holiday-api"]
