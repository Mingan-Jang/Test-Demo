package main

import (
	"fmt"
	"log"

	"go-backend/internal/app"
	"go-backend/internal/domain"
)

// ?¸æ?åº«é·ç§»å·¥??
func main() {
	fmt.Println("æ­?œ¨?å??–æ???..")

	// ?å??–æ???(?…æ‹¬?ç½®?æ•¸?šåº«?é·ç§?
	application, err := app.Initialize()
	if err != nil {
		log.Fatalf("?‰ç”¨?å??–å¤±?? %v", err)
	}

	fmt.Println("???‰ç”¨?å??–æ???)

	// ?²å??¸æ?åº«å¯¦ä¾‹é€²è?é©—è?
	db := application.GetDB()

	// é©—è?è¡?
	var tables []string
	result := db.Raw(`
		SELECT table_name
		FROM information_schema.tables
		WHERE table_schema = 'sys'
	`).Scan(&tables)

	if result.Error != nil {
		log.Fatalf("?¥è©¢è¡¨å¤±?? %v", result.Error)
	}

	fmt.Println("\nå·²å‰µå»ºç?è¡?")
	for _, table := range tables {
		fmt.Printf("  - %s\n", table)
	}

	// é©—è?æ¨¡å?
	fmt.Println("\né©—è?æ¨¡å?çµæ?...")
	models := []interface{}{
		&domain.HolidayOperator{},
		&domain.HolidayOperatorLoct{},
		&domain.HolidayOperatorCustom{},
		&domain.HolidayDisaster{},
	}

	for _, model := range models {
		if err := db.AutoMigrate(model); err != nil {
			log.Printf("?·ç§» %T å¤±æ?: %v", model, err)
		} else {
			fmt.Printf("??%T é©—è??å?\n", model)
		}
	}

	fmt.Println("\n???€?‰é·ç§»å???")
}
