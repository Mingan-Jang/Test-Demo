package app

import (
	"fmt"
	"os"

	"github.com/spf13/viper"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

// Config 應用配置
type Config struct {
	Server   ServerConfig
	Database DatabaseConfig
}

// ServerConfig 服務器配置
type ServerConfig struct {
	Port int
	Host string
}

// DatabaseConfig 數據庫配置
type DatabaseConfig struct {
	Host     string
	Port     int
	Username string
	Password string
	DBName   string
	SSLMode  string
}

// Load 加載配置
func Load() *Config {
	viper.SetConfigName("config")
	viper.SetConfigType("yaml")
	viper.AddConfigPath("./config")
	viper.AddConfigPath(".")

	// 設置環境變數前綴
	viper.SetEnvPrefix("HOLIDAY")

	// 綁定環境變數到配置鍵
	viper.BindEnv("server.port", "HOLIDAY_SERVER_PORT")
	viper.BindEnv("server.host", "HOLIDAY_SERVER_HOST")
	viper.BindEnv("database.host", "HOLIDAY_DATABASE_HOST")
	viper.BindEnv("database.port", "HOLIDAY_DATABASE_PORT")
	viper.BindEnv("database.username", "HOLIDAY_DATABASE_USERNAME")
	viper.BindEnv("database.password", "HOLIDAY_DATABASE_PASSWORD")
	viper.BindEnv("database.dbname", "HOLIDAY_DATABASE_DBNAME")
	viper.BindEnv("database.sslmode", "HOLIDAY_DATABASE_SSLMODE")

	viper.AutomaticEnv()

	// 設置默認值
	setDefaults()

	// 讀取配置文件
	if err := viper.ReadInConfig(); err != nil {
		if _, ok := err.(viper.ConfigFileNotFoundError); !ok {
			panic(fmt.Sprintf("讀取配置文件出錯: %v", err))
		}
	}

	cfg := &Config{
		Server: ServerConfig{
			Port: viper.GetInt("server.port"),
			Host: viper.GetString("server.host"),
		},
		Database: DatabaseConfig{
			Host:     viper.GetString("database.host"),
			Port:     viper.GetInt("database.port"),
			Username: viper.GetString("database.username"),
			Password: viper.GetString("database.password"),
			DBName:   viper.GetString("database.dbname"),
			SSLMode:  viper.GetString("database.sslmode"),
		},
	}

	return cfg
}

func setDefaults() {
	viper.SetDefault("server.port", 8080)
	viper.SetDefault("server.host", "localhost")
	viper.SetDefault("database.host", "localhost")
	viper.SetDefault("database.port", 5432)
	viper.SetDefault("database.username", "postgres")
	viper.SetDefault("database.password", "postgres")
	viper.SetDefault("database.dbname", "holiday_system")
	viper.SetDefault("database.sslmode", "disable")
}

// InitDatabase 初始化數據庫連接
func InitDatabase(cfg *Config) (*gorm.DB, error) {
	dsn := fmt.Sprintf(
		"host=%s port=%d user=%s password=%s dbname=%s sslmode=%s",
		cfg.Database.Host,
		cfg.Database.Port,
		cfg.Database.Username,
		cfg.Database.Password,
		cfg.Database.DBName,
		cfg.Database.SSLMode,
	)

	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		return nil, err
	}

	return db, nil
}

// GetDSN 獲取數據庫連接字符串
func (d *DatabaseConfig) GetDSN() string {
	return fmt.Sprintf(
		"host=%s port=%d user=%s password=%s dbname=%s sslmode=%s",
		d.Host,
		d.Port,
		d.Username,
		d.Password,
		d.DBName,
		d.SSLMode,
	)
}

// GetEnvString 從環境變數獲取字符串值
func GetEnvString(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}

// GetEnvInt 從環境變數獲取整數值
func GetEnvInt(key string, defaultValue int) int {
	if value, exists := os.LookupEnv(key); exists {
		var intVal int
		if _, err := fmt.Sscanf(value, "%d", &intVal); err == nil {
			return intVal
		}
	}
	return defaultValue
}
