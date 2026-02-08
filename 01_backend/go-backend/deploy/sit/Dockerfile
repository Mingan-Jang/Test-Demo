# SIT 环境 Dockerfile
# Swagger 文档应该在 dev 环境提前生成
# 这个 Dockerfile 只负责打包 API 和预生成的文档

FROM golang:1.21-alpine AS builder

WORKDIR /app

# 安装构建依赖
RUN apk add --no-cache git

# 复制 go mod 文件
COPY go.mod go.sum ./

# 下载依赖
RUN go mod download

# 复制源代码
COPY . .

# 构建应用（不生成 Swagger，应该提前生成）
RUN CGO_ENABLED=0 GOOS=linux go build -o holiday-api ./cmd/api/main.go

# 运行阶段
FROM alpine:latest

WORKDIR /app

# 安装时区数据
RUN apk --no-cache add tzdata wget

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/health || exit 1

# 运行应用
CMD ["./holiday-api"]
