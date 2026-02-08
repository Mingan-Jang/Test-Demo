package main

import (
	"fmt"
	"log"

	"holiday-system/internal/app"
	"holiday-system/internal/domain"
)

// 數據庫遷移工具
func main() {
	fmt.Println("正在初始化應用...")

	// 初始化應用 (包括配置、數據庫、遷移)
	application, err := app.Initialize()
	if err != nil {
		log.Fatalf("應用初始化失敗: %v", err)
	}

	fmt.Println("✓ 應用初始化成功")

	// 獲取數據庫實例進行驗證
	db := application.GetDB()

	// 驗證表
	var tables []string
	result := db.Raw(`
		SELECT table_name
		FROM information_schema.tables
		WHERE table_schema = 'sys'
	`).Scan(&tables)

	if result.Error != nil {
		log.Fatalf("查詢表失敗: %v", result.Error)
	}

	fmt.Println("\n已創建的表:")
	for _, table := range tables {
		fmt.Printf("  - %s\n", table)
	}

	// 驗證模型
	fmt.Println("\n驗證模型結構...")
	models := []interface{}{
		&domain.HolidayOperator{},
		&domain.HolidayOperatorLoct{},
		&domain.HolidayOperatorCustom{},
		&domain.HolidayDisaster{},
	}

	for _, model := range models {
		if err := db.AutoMigrate(model); err != nil {
			log.Printf("遷移 %T 失敗: %v", model, err)
		} else {
			fmt.Printf("✓ %T 驗證成功\n", model)
		}
	}

	fmt.Println("\n✓ 所有遷移完成!")
}
