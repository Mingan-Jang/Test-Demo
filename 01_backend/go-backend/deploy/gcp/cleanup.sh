#!/bin/bash

# 節假日系統清理腳本

set -e

PROJECT_ID=${1:-"holiday-system-prod"}
REGION=${2:-"asia-east1"}
CLUSTER_NAME="holiday-cluster"

echo "🧹 開始清理資源..."
echo "📍 項目 ID: $PROJECT_ID"

# 確認
read -p "⚠️  確認要刪除所有資源嗎? (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
  echo "❌ 已取消"
  exit 1
fi

# 設置項目
gcloud config set project $PROJECT_ID

# 1. 刪除 Kubernetes 資源
echo ""
echo "1️⃣  正在刪除 Kubernetes 資源..."
kubectl delete namespace holiday-system --ignore-not-found

# 2. 刪除 GKE 集群
echo ""
echo "2️⃣  正在刪除 GKE 集群..."
gcloud container clusters delete $CLUSTER_NAME --region=$REGION --quiet || true

# 3. 刪除 Cloud SQL
echo ""
echo "3️⃣  正在刪除 Cloud SQL..."
gcloud sql instances delete holiday-db --quiet || true

# 4. 刪除 Container Registry 鏡像
echo ""
echo "4️⃣  正在刪除 Container Registry 鏡像..."
for image in $(gcloud container images list --repository gcr.io/$PROJECT_ID 2>/dev/null || true); do
  gcloud container images delete $image --quiet
done

echo ""
echo "✅ 清理完成!"
echo ""
