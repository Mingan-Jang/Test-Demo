#!/bin/bash

# 節假日系統構建和部署腳本

set -e

# 配置
PROJECT_ID=${1:-"holiday-system-prod"}
REGION=${2:-"asia-east1"}
IMAGE_NAME="holiday-api"
CLUSTER_NAME="holiday-cluster"

echo "🚀 開始部署節假日系統"
echo "📍 項目 ID: $PROJECT_ID"
echo "🌍 區域: $REGION"

# 1. 設置 GCP 項目
echo ""
echo "1️⃣  正在配置 GCP 項目..."
gcloud config set project $PROJECT_ID

# 2. 啟用 API
echo ""
echo "2️⃣  正在啟用必要的 API..."
gcloud services enable container.googleapis.com
gcloud services enable compute.googleapis.com
gcloud services enable cloudsql.googleapis.com
gcloud services enable artifactregistry.googleapis.com
gcloud services enable containerregistry.googleapis.com

# 3. 設置 Docker 認證
echo ""
echo "3️⃣  正在配置 Docker 認證..."
gcloud auth configure-docker gcr.io

# 4. 構建 Docker 鏡像
echo ""
echo "4️⃣  正在構建 Docker 鏡像..."
docker build -t gcr.io/$PROJECT_ID/$IMAGE_NAME:latest .

# 5. 推送到 Container Registry
echo ""
echo "5️⃣  正在推送鏡像到 Container Registry..."
docker push gcr.io/$PROJECT_ID/$IMAGE_NAME:latest

# 6. 創建 Cloud SQL 實例
echo ""
echo "6️⃣  正在創建 Cloud SQL 實例..."
if ! gcloud sql instances describe holiday-db --region=$REGION > /dev/null 2>&1; then
  gcloud sql instances create holiday-db \
    --database-version=POSTGRES_15 \
    --tier=db-f1-micro \
    --region=$REGION \
    --availability-type=REGIONAL

  # 設置密碼
  gcloud sql users set-password postgres \
    --instance=holiday-db \
    --password=Holiday2024Secure!

  # 創建數據庫
  gcloud sql databases create holiday_system \
    --instance=holiday-db

  echo "✓ Cloud SQL 實例已創建"
else
  echo "✓ Cloud SQL 實例已存在"
fi

# 7. 創建 GKE 集群
echo ""
echo "7️⃣  正在創建 GKE 集群..."
if ! gcloud container clusters describe $CLUSTER_NAME --region=$REGION > /dev/null 2>&1; then
  gcloud container clusters create $CLUSTER_NAME \
    --region=$REGION \
    --num-nodes=2 \
    --machine-type=e2-medium \
    --enable-autoscaling \
    --min-nodes=2 \
    --max-nodes=5 \
    --enable-stackdriver-kubernetes

  echo "✓ GKE 集群已創建"
else
  echo "✓ GKE 集群已存在"
fi

# 8. 獲取集群憑證
echo ""
echo "8️⃣  正在獲取集群憑證..."
gcloud container clusters get-credentials $CLUSTER_NAME --region=$REGION

# 9. 創建命名空間
echo ""
echo "9️⃣  正在創建命名空間..."
kubectl create namespace holiday-system --dry-run=client -o yaml | kubectl apply -f -

# 10. 創建 Secret
echo ""
echo "🔟 正在創建 Secret..."
DB_IP=$(gcloud sql instances describe holiday-db --format='get(ipAddresses[0].ipAddress)')
kubectl create secret generic holiday-secret \
  --from-literal=db-host=$DB_IP \
  --from-literal=db-password="Holiday2024Secure!" \
  -n holiday-system \
  --dry-run=client -o yaml | kubectl apply -f -

# 11. 部署應用
echo ""
echo "1️⃣1️⃣  正在部署應用..."
sed "s/PROJECT_ID/$PROJECT_ID/g" deploy/gcp/k8s/deployment.yaml | kubectl apply -f -

# 12. 等待部署就緒
echo ""
echo "1️⃣2️⃣  等待部署就緒..."
kubectl rollout status deployment/holiday-api -n holiday-system --timeout=5m

# 13. 獲取外部 IP
echo ""
echo "1️⃣3️⃣  正在獲取服務信息..."
sleep 10
EXTERNAL_IP=$(kubectl get service holiday-api -n holiday-system -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null || echo "待分配")

echo ""
echo "========================================="
echo "✅ 部署完成!"
echo "========================================="
echo ""
echo "📊 服務信息:"
echo "  - 外部 IP: http://$EXTERNAL_IP"
echo "  - 健康檢查: http://$EXTERNAL_IP/health"
echo ""
echo "📝 查看日誌:"
echo "  kubectl logs -n holiday-system -l app=holiday-api -f"
echo ""
echo "🧹 清理資源:"
echo "  bash deploy/gcp/cleanup.sh $PROJECT_ID $REGION"
echo ""
