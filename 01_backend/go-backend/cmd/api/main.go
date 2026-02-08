package main

import (
	"fmt"

	"go-backend/internal/app"

)

func main() {
	application, err := app.Initialize()
	if err != nil {
	}

	cfg := app.LoadConfig()
	addr := fmt.Sprintf("%s:%d", cfg.Server.Host, cfg.Server.Port)

	if err := application.Run(addr); err != nil {
	}
}
