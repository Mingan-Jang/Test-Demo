# Holiday System - GCP 部署指南

## 前置要求

- Google Cloud Platform 帳戶
- `gcloud` CLI 工具已安裝並配置
- Docker 已安裝
- kubectl 已安裝

## GCP 資源配置

### 1. 創建 GCP 專案

```bash
# 設置項目 ID
export PROJECT_ID=holiday-system-prod
export REGION=asia-east1

# 創建項目
gcloud projects create $PROJECT_ID

# 設置為活動項目
gcloud config set project $PROJECT_ID

# 啟用必要的 API
gcloud services enable container.googleapis.com
gcloud services enable compute.googleapis.com
gcloud services enable cloudsql.googleapis.com
gcloud services enable artifactregistry.googleapis.com
```

### 2. 設置 Cloud SQL (PostgreSQL)

```bash
# 創建 PostgreSQL 實例
gcloud sql instances create holiday-db \
  --database-version=POSTGRES_15 \
  --tier=db-f1-micro \
  --region=$REGION \
  --availability-type=REGIONAL

# 設置根密碼
gcloud sql users set-password postgres \
  --instance=holiday-db \
  --password=YOUR_SECURE_PASSWORD

# 創建數據庫
gcloud sql databases create holiday_system \
  --instance=holiday-db

# 獲取實例連接信息
gcloud sql instances describe holiday-db
```

### 3. 設置 Container Registry

```bash
# 啟用 Container Registry
gcloud services enable containerregistry.googleapis.com

# 配置 Docker 認證
gcloud auth configure-docker gcr.io

# 構建並推送鏡像
export IMAGE=gcr.io/$PROJECT_ID/holiday-api:latest

docker build -t $IMAGE .
docker push $IMAGE
```

或使用 Cloud Build 構建：

```bash
gcloud builds submit --tag=$IMAGE
```

### 4. 創建 GKE 集群

```bash
# 創建 Kubernetes 集群
gcloud container clusters create holiday-cluster \
  --region=$REGION \
  --num-nodes=2 \
  --machine-type=e2-medium \
  --enable-autoscaling \
  --min-nodes=2 \
  --max-nodes=5

# 獲取集群憑證
gcloud container clusters get-credentials holiday-cluster --region=$REGION
```

### 5. 部署應用到 GKE

#### 5.1 創建 ConfigMap 和 Secret

```bash
# 創建 namespace
kubectl create namespace holiday-system

# 創建 ConfigMap
kubectl create configmap holiday-config \
  --from-literal=server.port=8080 \
  --from-literal=server.host=0.0.0.0 \
  -n holiday-system

# 創建 Secret（包含敏感信息）
kubectl create secret generic holiday-secret \
  --from-literal=db-host=CLOUD_SQL_INSTANCE_IP \
  --from-literal=db-password=YOUR_SECURE_PASSWORD \
  -n holiday-system
```

#### 5.2 部署應用

使用以下 Kubernetes 清單 (`k8s/deployment.yaml`)：

```yaml
apiVersion: v1
kind: Service
metadata:
  name: holiday-api
  namespace: holiday-system
spec:
  type: LoadBalancer
  ports:
    - port: 80
      targetPort: 8080
  selector:
    app: holiday-api

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: holiday-api
  namespace: holiday-system
spec:
  replicas: 2
  selector:
    matchLabels:
      app: holiday-api
  template:
    metadata:
      labels:
        app: holiday-api
    spec:
      containers:
        - name: api
          image: gcr.io/PROJECT_ID/holiday-api:latest
          ports:
            - containerPort: 8080
          env:
            - name: HOLIDAY_SERVER_PORT
              value: "8080"
            - name: HOLIDAY_DATABASE_HOST
              valueFrom:
                secretKeyRef:
                  name: holiday-secret
                  key: db-host
            - name: HOLIDAY_DATABASE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: holiday-secret
                  key: db-password
            - name: HOLIDAY_DATABASE_DBNAME
              value: holiday_system
          resources:
            requests:
              memory: "256Mi"
              cpu: "250m"
            limits:
              memory: "512Mi"
              cpu: "500m"
          livenessProbe:
            httpGet:
              path: /health
              port: 8080
            initialDelaySeconds: 10
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: /health
              port: 8080
            initialDelaySeconds: 5
            periodSeconds: 5
```

部署：

```bash
# 替換 PROJECT_ID
sed 's/PROJECT_ID/your-actual-project-id/g' k8s/deployment.yaml | kubectl apply -f -

# 驗證部署
kubectl get deployments -n holiday-system
kubectl get services -n holiday-system
```

### 6. Cloud SQL 代理配置（推薦）

使用 Cloud SQL Proxy 提高安全性：

```yaml
# 在 Deployment 中添加側車
spec:
  containers:
    - name: cloud-sql-proxy
      image: gcr.io/cloudsql-docker/cloud-sql-proxy:1.33.2
      command:
        - "/cloud_sql_proxy"
        - "-instances=PROJECT_ID:REGION:holiday-db=tcp:5432"
      securityContext:
        runAsNonRoot: true
```

## 部署驗證

```bash
# 檢查 Pod 狀態
kubectl get pods -n holiday-system -w

# 查看 Pod 日誌
kubectl logs -n holiday-system -l app=holiday-api -f

# 獲取外部 IP
kubectl get service holiday-api -n holiday-system

# 測試 API
curl http://EXTERNAL_IP/health
```

## 監控和日誌

### Cloud Logging

```bash
# 查看日誌
gcloud logging read "resource.type=k8s_container AND resource.labels.namespace_name=holiday-system" \
  --limit 50 \
  --format json
```

### Cloud Monitoring

```bash
# 創建告警（示例）
gcloud alpha monitoring policies create \
  --notification-channels=CHANNEL_ID \
  --display-name="Holiday API Error Rate" \
  --condition-display-name="Error Rate > 5%" \
  --condition-threshold-value=0.05
```

## 成本優化

1. **使用 Autopilot GKE**：自動管理基礎設施
2. **啟用 Workload Identity**：避免使用服務帳戶密鑰
3. **配置水平自動縮放 (HPA)**
4. **使用 Preemptible 節點降低成本**

## 故障排除

### Pod 無法啟動

```bash
# 檢查 Pod 事件
kubectl describe pod POD_NAME -n holiday-system

# 查看容器日誌
kubectl logs POD_NAME -n holiday-system
```

### 數據庫連接問題

```bash
# 測試連接
gcloud sql connect holiday-db --user=postgres

# 檢查防火牆規則
gcloud sql instances describe holiday-db | grep ipAddresses
```

### 應用性能

```bash
# 查看資源使用
kubectl top nodes
kubectl top pods -n holiday-system
```

## CI/CD 設置

### 使用 Cloud Build

創建 `cloudbuild.yaml`：

```yaml
steps:
  # 構建 Docker 鏡像
  - name: "gcr.io/cloud-builders/docker"
    args: ["build", "-t", "gcr.io/$PROJECT_ID/holiday-api:$SHORT_SHA", "."]

  # 推送到 Container Registry
  - name: "gcr.io/cloud-builders/docker"
    args: ["push", "gcr.io/$PROJECT_ID/holiday-api:$SHORT_SHA"]

  # 部署到 GKE
  - name: "gcr.io/cloud-builders/kubectl"
    args:
      [
        "set",
        "image",
        "deployment/holiday-api",
        "api=gcr.io/$PROJECT_ID/holiday-api:$SHORT_SHA",
      ]
    env:
      [
        "CLOUDSDK_COMPUTE_REGION=$_REGION",
        "CLOUDSDK_CONTAINER_CLUSTER=$_CLUSTER_NAME",
      ]

images:
  - "gcr.io/$PROJECT_ID/holiday-api:$SHORT_SHA"
```

## 清理資源

```bash
# 刪除 Kubernetes 資源
kubectl delete namespace holiday-system

# 刪除 GKE 集群
gcloud container clusters delete holiday-cluster --region=$REGION

# 刪除 Cloud SQL
gcloud sql instances delete holiday-db

# 刪除 Docker 鏡像
gcloud container images delete gcr.io/$PROJECT_ID/holiday-api:latest
```

## 生產建議

1. **啟用 Binary Authorization**
2. **配置 Pod Security Policy**
3. **使用 Private GKE Cluster**
4. **啟用 Network Policy**
5. **配置備份和災難恢復**
6. **使用 Secrets Manager 管理敏感信息**
7. **定期更新依賴和補丁**
8. **監控成本並設置預算告警**
