package main

import (
	"fmt"
	"log"

	"holiday-system/internal/app"
)

func main() {
	// 初始化應用 (包括配置、數據庫、遷移)
	application, err := app.Initialize()
	if err != nil {
		log.Fatalf("應用初始化失敗: %v", err)
	}

	// 啟動服務器
	cfg := app.LoadConfig()
	addr := fmt.Sprintf("%s:%d", cfg.Server.Host, cfg.Server.Port)
	log.Printf("服務器啟動在 http://%s", addr)

	if err := application.Run(addr); err != nil {
		log.Fatalf("服務器啟動失敗: %v", err)
	}
}
