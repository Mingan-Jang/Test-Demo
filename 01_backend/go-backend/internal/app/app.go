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
)

// App 应用结构体
type App struct {
Engine *gin.Engine
DB     *gorm.DB
}

// LoadConfig 加载配置
func LoadConfig() *Config {
return Load()
}

// Initialize 初始化应用
func Initialize() (*App, error) {
cfg := Load()
db, err := InitDatabase(cfg)
if err != nil {
return nil, err
}
return New(db), nil
}

// New 创建应用实例
func New(db *gorm.DB) *App {
app := &App{
Engine: gin.Default(),
DB:     db,
}

// 使用中间件
app.Engine.Use(middleware.CORS())
app.Engine.Use(middleware.ErrorHandler())

// 设置路由
app.setupRoutes()

return app
}

// setupRoutes 设置路由
func (a *App) setupRoutes() {
// 健康检查
a.Engine.GET("/health", handler.Health)

// Swagger UI 路由
a.Engine.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

// 创建仓储
holidayRepo := repository.NewHolidayRepository(a.DB)
customHolidayRepo := repository.NewCustomHolidayRepository(a.DB)
disasterHolidayRepo := repository.NewDisasterHolidayRepository(a.DB)

// 创建服务
holidayService := service.NewHolidayService(holidayRepo, customHolidayRepo, disasterHolidayRepo)
customHolidayService := service.NewCustomHolidayService(customHolidayRepo)
disasterHolidayService := service.NewDisasterHolidayService(disasterHolidayRepo)

// 创建处理器
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

// 自定义假日
v1.GET("/custom-holidays", customHolidayHandler.GetCustomHolidays)
v1.POST("/custom-holidays", customHolidayHandler.CreateCustomHoliday)
v1.PUT("/custom-holidays/:id", customHolidayHandler.UpdateCustomHoliday)
v1.DELETE("/custom-holidays/:id", customHolidayHandler.DeleteCustomHoliday)

// 天灾假日
v1.GET("/disaster-holidays", disasterHolidayHandler.GetDisasterHolidays)
v1.POST("/disaster-holidays", disasterHolidayHandler.CreateDisasterHoliday)
}
}

// MigrateDB 迁移数据库
func (a *App) MigrateDB() error {
return a.DB.AutoMigrate(
&domain.HolidayOperator{},
&domain.HolidayOperatorLoct{},
&domain.HolidayOperatorCustom{},
&domain.HolidayDisaster{},
)
}

// Run 运行应用
func (a *App) Run(addr string) error {
return a.Engine.Run(addr)
}
