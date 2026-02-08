package app

import (
	"go-backend/internal/domain"
	"go-backend/internal/handler"
	"go-backend/internal/middleware"
	"go-backend/internal/repository"
	"go-backend/internal/service"

	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
	"gorm.io/gorm"

	_ "go-backend/docs"
)

// App ?‰ç”¨çµæ?
type App struct {
	Engine *gin.Engine
	DB     *gorm.DB
}

// New ?µå»º?°æ??¨å¯¦ä¾?
func New(db *gorm.DB) *App {
	app := &App{
		Engine: gin.Default(),
		DB:     db,
	}

	// ?‰ç”¨ä¸­é?ä»?
	app.Engine.Use(middleware.CORS())
	app.Engine.Use(middleware.ErrorHandler())

	// ?å??–è·¯??
	app.setupRoutes()

	return app
}

// setupRoutes è¨­ç½®è·¯ç”±
func (a *App) setupRoutes() {
	// ?¥åº·æª¢æŸ¥
	a.Engine.GET("/health", handler.Health)

	// Swagger UI è·¯ç”±
	a.Engine.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	// ?å??–å€‰å„²
	holidayRepo := repository.NewHolidayRepository(a.DB)
	customHolidayRepo := repository.NewCustomHolidayRepository(a.DB)
	disasterHolidayRepo := repository.NewDisasterHolidayRepository(a.DB)

	// ?å??–æ???
	holidayService := service.NewHolidayService(holidayRepo, customHolidayRepo, disasterHolidayRepo)
	customHolidayService := service.NewCustomHolidayService(customHolidayRepo)
	disasterHolidayService := service.NewDisasterHolidayService(disasterHolidayRepo)

	// ?å??–è??†å™¨
	holidayHandler := handler.NewHolidayHandler(holidayService)
	customHolidayHandler := handler.NewCustomHolidayHandler(customHolidayService)
	disasterHolidayHandler := handler.NewDisasterHolidayHandler(disasterHolidayService)

	// API v1 è·¯ç”±
	v1 := a.Engine.Group("/api/v1")
	{
		// ?‡æ—¥ç®¡ç?
		v1.GET("/holidays", holidayHandler.GetHolidays)
		v1.POST("/holidays", holidayHandler.CreateHoliday)
		v1.PUT("/holidays/:id", holidayHandler.UpdateHoliday)
		v1.DELETE("/holidays/:id", holidayHandler.DeleteHoliday)

		// ?ªè?ç¾©å???
		v1.GET("/custom-holidays", customHolidayHandler.GetCustomHolidays)
		v1.POST("/custom-holidays", customHolidayHandler.CreateCustomHoliday)
		v1.PUT("/custom-holidays/:id", customHolidayHandler.UpdateCustomHoliday)
		v1.DELETE("/custom-holidays/:id", customHolidayHandler.DeleteCustomHoliday)

		// å¤©ç½?‡æ—¥
		v1.GET("/disaster-holidays", disasterHolidayHandler.GetDisasterHolidays)
		v1.POST("/disaster-holidays", disasterHolidayHandler.CreateDisasterHoliday)
	}
}

// MigrateDB ?·ç§»?¸æ?åº?
func (a *App) MigrateDB() error {
	return a.DB.AutoMigrate(
		&domain.HolidayOperator{},
		&domain.HolidayOperatorLoct{},
		&domain.HolidayOperatorCustom{},
		&domain.HolidayDisaster{},
	)
}

// CreateSchemas ?µå»º?¸æ?åº?schema
func (a *App) CreateSchemas() error {
	return a.DB.Exec("CREATE SCHEMA IF NOT EXISTS sys").Error
}

// Run ?‹è??‰ç”¨
func (a *App) Run(addr string) error {
	return a.Engine.Run(addr)
}

// GetDB ?²å??¸æ?åº«å¯¦ä¾?
func (a *App) GetDB() *gorm.DB {
	return a.DB
}

// Initialize ?å??–æ??¨ï?çµ±ä??å??–å…¥???æ¶ˆé™¤?è?ä»?¢¼ï¼?
func Initialize() (*App, error) {
	// ? è??ç½®
	cfg := Load()

	// ?å??–æ•¸?šåº«
	db, err := InitDatabase(cfg)
	if err != nil {
		return nil, err
	}

	// ?µå»º?‰ç”¨å¯¦ä?
	application := New(db)

	// ?µå»º schema
	if err := application.CreateSchemas(); err != nil {
		return nil, err
	}

	// ?·è??¸æ?åº«é·ç§?
	if err := application.MigrateDB(); err != nil {
		return nil, err
	}

	return application, nil
}

// LoadConfig ? è??ç½®ï¼ˆä¾¿?©å‡½?¸ï?
func LoadConfig() *Config {
	return Load()
}
