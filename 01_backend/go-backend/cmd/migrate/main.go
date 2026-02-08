package main

import (
	"go-backend/internal/app"
	"go-backend/internal/domain"
)

func main() {
	application, err := app.Initialize()
	if err != nil {
		panic(err)
	}

	db := application.GetDB()

	models := []interface{}{
		&domain.HolidayOperator{},
		&domain.HolidayOperatorLoct{},
		&domain.HolidayOperatorCustom{},
		&domain.HolidayDisaster{},
	}

	for _, model := range models {
		if err := db.AutoMigrate(model); err != nil {
			panic(err)
		}
	}
}
