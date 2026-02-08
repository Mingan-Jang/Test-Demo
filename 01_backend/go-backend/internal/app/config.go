package app

import (
"fmt"
"log"

"github.com/spf13/viper"
"gorm.io/driver/mysql"
"gorm.io/gorm"
)

// Config 应用配置
type Config struct {
Server struct {
Host string `yaml:"host"`
Port int    `yaml:"port"`
} `yaml:"server"`
Database struct {
Driver   string `yaml:"driver"`
Host     string `yaml:"host"`
Port     int    `yaml:"port"`
Database string `yaml:"database"`
User     string `yaml:"user"`
Password string `yaml:"password"`
} `yaml:"database"`
}

// Load 加载配置
func Load() *Config {
viper.SetConfigName("config")
viper.SetConfigType("yaml")
viper.AddConfigPath("./config")

if err := viper.ReadInConfig(); err != nil {
log.Fatalf("读取配置文件失败: %v", err)
}

var config Config
if err := viper.Unmarshal(&config); err != nil {
log.Fatalf("解析配置文件失败: %v", err)
}

return &config
}

// InitDatabase 初始化数据库
func InitDatabase(cfg *Config) (*gorm.DB, error) {
dsn := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s?charset=utf8mb4&parseTime=True&loc=Local",
cfg.Database.User,
cfg.Database.Password,
cfg.Database.Host,
cfg.Database.Port,
cfg.Database.Database,
)

db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
if err != nil {
return nil, err
}

return db, nil
}
