package main

import (
	"go-backend/internal/app"
	"go-backend/internal/domain"
)

func main() {

	application, err := app.Initialize()
	if err != nil {
	}

	db := application.GetDB()

	var tables []string
	result := db.Raw(`
		SELECT table_name
		FROM information_schema.tables
		WHERE table_schema = 'sys'
	`).Scan(&tables)

	if result.Error != nil {
	}

	models := []interface{}{
		&domain.HolidayOperator{},
		&domain.HolidayOperatorLoct{},
		&domain.HolidayOperatorCustom{},
		&domain.HolidayDisaster{},
	}

	for _, model := range models {
		if err := db.AutoMigrate(model); err != nil {
		} else {
		}
	}

}
