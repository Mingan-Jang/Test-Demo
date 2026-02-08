package app

import (
	"holiday-system/internal/domain"
	"holiday-system/internal/handler"
	"holiday-system/internal/middleware"
	"holiday-system/internal/repository"
	"holiday-system/internal/service"

	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
	"gorm.io/gorm"

	_ "holiday-system/docs"
)

// App 應用結構
type App struct {
	Engine *gin.Engine
	DB     *gorm.DB
}

// New 創建新應用實例
func New(db *gorm.DB) *App {
	app := &App{
		Engine: gin.Default(),
		DB:     db,
	}

	// 應用中間件
	app.Engine.Use(middleware.CORS())
	app.Engine.Use(middleware.ErrorHandler())

	// 初始化路由
	app.setupRoutes()

	return app
}

// setupRoutes 設置路由
func (a *App) setupRoutes() {
	// 健康檢查
	a.Engine.GET("/health", handler.Health)

	// Swagger UI 路由
	a.Engine.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	// 初始化倉儲
	holidayRepo := repository.NewHolidayRepository(a.DB)
	customHolidayRepo := repository.NewCustomHolidayRepository(a.DB)
	disasterHolidayRepo := repository.NewDisasterHolidayRepository(a.DB)

	// 初始化服務
	holidayService := service.NewHolidayService(holidayRepo, customHolidayRepo, disasterHolidayRepo)
	customHolidayService := service.NewCustomHolidayService(customHolidayRepo)
	disasterHolidayService := service.NewDisasterHolidayService(disasterHolidayRepo)

	// 初始化處理器
	holidayHandler := handler.NewHolidayHandler(holidayService)
	customHolidayHandler := handler.NewCustomHolidayHandler(customHolidayService)
	disasterHolidayHandler := handler.NewDisasterHolidayHandler(disasterHolidayService)

	// API v1 路由
	v1 := a.Engine.Group("/api/v1")
	{
		// 假日管理
		v1.GET("/holidays", holidayHandler.GetHolidays)
		v1.POST("/holidays", holidayHandler.CreateHoliday)
		v1.PUT("/holidays/:id", holidayHandler.UpdateHoliday)
		v1.DELETE("/holidays/:id", holidayHandler.DeleteHoliday)

		// 自訂義假日
		v1.GET("/custom-holidays", customHolidayHandler.GetCustomHolidays)
		v1.POST("/custom-holidays", customHolidayHandler.CreateCustomHoliday)
		v1.PUT("/custom-holidays/:id", customHolidayHandler.UpdateCustomHoliday)
		v1.DELETE("/custom-holidays/:id", customHolidayHandler.DeleteCustomHoliday)

		// 天災假日
		v1.GET("/disaster-holidays", disasterHolidayHandler.GetDisasterHolidays)
		v1.POST("/disaster-holidays", disasterHolidayHandler.CreateDisasterHoliday)
	}
}

// MigrateDB 遷移數據庫
func (a *App) MigrateDB() error {
	return a.DB.AutoMigrate(
		&domain.HolidayOperator{},
		&domain.HolidayOperatorLoct{},
		&domain.HolidayOperatorCustom{},
		&domain.HolidayDisaster{},
	)
}

// CreateSchemas 創建數據庫 schema
func (a *App) CreateSchemas() error {
	return a.DB.Exec("CREATE SCHEMA IF NOT EXISTS sys").Error
}

// Run 運行應用
func (a *App) Run(addr string) error {
	return a.Engine.Run(addr)
}

// GetDB 獲取數據庫實例
func (a *App) GetDB() *gorm.DB {
	return a.DB
}

// Initialize 初始化應用（統一初始化入口，消除重複代碼）
func Initialize() (*App, error) {
	// 加載配置
	cfg := Load()

	// 初始化數據庫
	db, err := InitDatabase(cfg)
	if err != nil {
		return nil, err
	}

	// 創建應用實例
	application := New(db)

	// 創建 schema
	if err := application.CreateSchemas(); err != nil {
		return nil, err
	}

	// 執行數據庫遷移
	if err := application.MigrateDB(); err != nil {
		return nil, err
	}

	return application, nil
}

// LoadConfig 加載配置（便利函數）
func LoadConfig() *Config {
	return Load()
}
