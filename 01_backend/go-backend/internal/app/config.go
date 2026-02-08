package app

import (
	"fmt"
	"os"

	"github.com/spf13/viper"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

// Config ?‰ç”¨?ç½®
type Config struct {
	Server   ServerConfig
	Database DatabaseConfig
}

// ServerConfig ?å??¨é?ç½?
type ServerConfig struct {
	Port int
	Host string
}

// DatabaseConfig ?¸æ?åº«é?ç½?
type DatabaseConfig struct {
	Host     string
	Port     int
	Username string
	Password string
	DBName   string
	SSLMode  string
}

// Load ? è??ç½®
func Load() *Config {
	viper.SetConfigName("config")
	viper.SetConfigType("yaml")
	viper.AddConfigPath("./config")
	viper.AddConfigPath(".")

	// è¨­ç½®?°å?è®Šæ•¸?ç¶´
	viper.SetEnvPrefix("HOLIDAY")

	// ç¶å??°å?è®Šæ•¸?°é?ç½®éµ
	viper.BindEnv("server.port", "HOLIDAY_SERVER_PORT")
	viper.BindEnv("server.host", "HOLIDAY_SERVER_HOST")
	viper.BindEnv("database.host", "HOLIDAY_DATABASE_HOST")
	viper.BindEnv("database.port", "HOLIDAY_DATABASE_PORT")
	viper.BindEnv("database.username", "HOLIDAY_DATABASE_USERNAME")
	viper.BindEnv("database.password", "HOLIDAY_DATABASE_PASSWORD")
	viper.BindEnv("database.dbname", "HOLIDAY_DATABASE_DBNAME")
	viper.BindEnv("database.sslmode", "HOLIDAY_DATABASE_SSLMODE")

	viper.AutomaticEnv()

	// è¨­ç½®é»˜è???
	setDefaults()

	// è®€?–é?ç½®æ?ä»?
	if err := viper.ReadInConfig(); err != nil {
		if _, ok := err.(viper.ConfigFileNotFoundError); !ok {
			panic(fmt.Sprintf("è®€?–é?ç½®æ?ä»¶å‡º?? %v", err))
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

// InitDatabase ?å??–æ•¸?šåº«??¥
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

// GetDSN ?²å??¸æ?åº«é€?¥å­—ç¬¦ä¸?
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

// GetEnvString å¾ç’°å¢ƒè??¸ç²?–å?ç¬¦ä¸²??
func GetEnvString(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}

// GetEnvInt å¾ç’°å¢ƒè??¸ç²?–æ•´?¸å€?
func GetEnvInt(key string, defaultValue int) int {
	if value, exists := os.LookupEnv(key); exists {
		var intVal int
		if _, err := fmt.Sscanf(value, "%d", &intVal); err == nil {
			return intVal
		}
	}
	return defaultValue
}
