// mongo-init/init-alerts.js
// 初始化腳本：建立 testdb.alerts collection（包含 validator）、建立 testAlert 帳號，並插入假資料

// 指定資料庫
db = db.getSiblingDB("testdb");

// ---------- 1) 建立或更新 alerts collection（含 validator） ----------
const collName = "alerts";

const validator = {
  $jsonSchema: {
    bsonType: "object",
    required: ["eventId", "message", "createdAt"],
    properties: {
      eventId: { bsonType: "string", description: "事件ID" },
      message: { bsonType: "string", description: "訊息內容" },
      createdAt: { bsonType: "date", description: "建立時間" },
      level: { enum: ["INFO", "WARN", "ERROR"], description: "事件等級" },
      metadata: {
        bsonType: "object",
        properties: {
          source: { bsonType: "string" },
          environment: { bsonType: "string" },
        },
      },
      tags: {
        bsonType: "array",
        items: { bsonType: "string" },
      },
      attachment: {
        bsonType: "binData",
        description: "二進制附件資料",
      },
      categories: {
        bsonType: "array",
        uniqueItems: true,
        items: { bsonType: "string" },
      },
    },
  },
};

if (!db.getCollectionNames().includes(collName)) {
  print("createCollection " + collName);
  db.createCollection(collName, {
    validator: validator,
    validationLevel: "strict",
    validationAction: "error",
    capped: false,
  });
} else {
  // 如果已存在，嘗試用 collMod 更新 validator（若需要）
  try {
    print("collection exists -> running collMod to update validator");
    db.runCommand({
      collMod: collName,
      validator: validator,
      validationLevel: "strict",
      validationAction: "error",
    });
  } catch (e) {
    print("collMod failed (maybe server version?), continuing. Error: " + e);
  }
}

// ---------- 2) 建立專用帳號 testAlert（若不存在） ----------
const username = "testAlert";
const password = "123456";

const existingUser = db.getUser(username);
if (!existingUser) {
  print("createUser: " + username);
  db.createUser({
    user: username,
    pwd: password,
    roles: [{ role: "readWrite", db: "testdb" }],
  });
} else {
  print("user already exists: " + username);
}

// ---------- 3) 插入假資料（若 collection 為空） ----------
const docCount = db.getCollection(collName).countDocuments();
if (docCount === 0) {
  print("inserting sample alerts...");
  db.getCollection(collName).insertMany([
    {
      _id: ObjectId("69104e32686b14178ace5f47"),
      eventId: "EVT001",
      message: "系統啟動",
      createdAt: ISODate("2025-11-09T08:17:54.854Z"),
      level: "INFO",
      metadata: { source: "backend-service", environment: "production" },
      tags: ["startup", "system", "automatic"],
      attachment: new BinData(0, "SGVsbG8gV29ybGQ="),
      categories: ["system", "infrastructure"],
    },
    {
      _id: ObjectId("69104e3b686b14178ace5f48"),
      eventId: "EVT002",
      message: "使用者登入",
      createdAt: ISODate("2025-11-09T08:18:03.863Z"),
      level: "INFO",
      metadata: { source: "auth-service", environment: "production" },
      tags: ["auth", "user", "login"],
      attachment: new BinData(0, "5L2/55So6ICF55m75YWl57SA6YyE"),
      categories: ["security", "user-management"],
    },
    {
      _id: ObjectId("69104e3b686b14178ace5f49"),
      eventId: "EVT003",
      message: "錯誤事件",
      createdAt: ISODate("2025-11-09T08:18:03.863Z"),
      level: "ERROR",
      metadata: { source: "payment-service", environment: "production" },
      tags: ["payment", "error", "critical"],
      attachment: new BinData(0, "6Yyv6Kqk6Kmz57Sw6LOH6KiK"),
      categories: ["error", "payment"],
    },
    {
      _id: ObjectId("69104e3b686b14178ace5f4a"),
      eventId: "EVT004",
      message: "警告事件",
      createdAt: ISODate("2025-11-09T08:18:03.863Z"),
      level: "WARN",
      metadata: { source: "monitoring", environment: "staging" },
      tags: ["monitor", "warning", "performance"],
      attachment: new BinData(0, "57O757Wx5pWI6IO96K2m5ZGK"),
      categories: ["monitoring", "performance"],
    },
    {
      _id: ObjectId("69104e466ed74377f50d016d"),
      eventId: "EVT006",
      message: "事件更新",
      createdAt: ISODate("2025-11-09T08:21:59.708Z"),
      level: "WARN",
      metadata: { source: "update-service", environment: "testing" },
      tags: ["updated", "updated"],
      categories: ["maintenance"],
    },
  ]);
} else {
  print(
    "alerts collection already has " + docCount + " documents; skipping insert."
  );
}

print("init-alerts.js done.");
