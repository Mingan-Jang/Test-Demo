package main

import (
	"fmt"
	"log"

	"go-backend/internal/app"
)

func main() {
	// ?å??–æ???(?…æ‹¬?ç½®?æ•¸?šåº«?é·ç§?
	application, err := app.Initialize()
	if err != nil {
		log.Fatalf("?‰ç”¨?å??–å¤±?? %v", err)
	}

	// ?Ÿå??å???
	cfg := app.LoadConfig()
	addr := fmt.Sprintf("%s:%d", cfg.Server.Host, cfg.Server.Port)
	log.Printf("?å??¨å??•åœ¨ http://%s", addr)

	if err := application.Run(addr); err != nil {
		log.Fatalf("?å??¨å??•å¤±?? %v", err)
	}
}
