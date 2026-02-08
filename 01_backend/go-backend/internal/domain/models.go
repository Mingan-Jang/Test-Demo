package domain

import (
"time"
)

// HolidayOperator 假日表-框架层数据
type HolidayOperator struct {
ID            int64     `gorm:"primaryKey" json:"id"`
Date          time.Time `gorm:"type:date;index" json:"date"`
Type          string    `gorm:"type:varchar(20)" json:"type"`   // 'holiday' or 'workday'
Source        string    `gorm:"type:varchar(50)" json:"source"` // 'weekend', 'disaster', 'google', 'custom'
Operator      string    `gorm:"type:varchar(100);index" json:"operator"`
RegionVillage string    `gorm:"type:varchar(100)" json:"region_village"`
RegionTown    string    `gorm:"type:varchar(100)" json:"region_town"`
RegionCity    string    `gorm:"type:varchar(100);index" json:"region_city"`
Description   string    `gorm:"type:varchar(255)" json:"description"`
IsActive      string    `gorm:"type:char(1);default:'"'"'Y'"'"'" json:"is_active"`
UpdatedAt     time.Time `gorm:"autoUpdateTime" json:"updated_at"`
CreatedAt     time.Time `gorm:"autoCreateTime" json:"created_at"`
}

// TableName 映射表名
func (HolidayOperator) TableName() string {
return "sys.holiday_operator"
}

// HolidayOperatorLoct 框架层-位置表
type HolidayOperatorLoct struct {
ID            int64     `gorm:"primaryKey" json:"id"`
OperatorID    int64     `gorm:"type:bigint;index"`
LocationKey   string    `gorm:"type:varchar(100);uniqueIndex:uniq_operator_location"`
LocationValue string    `gorm:"type:text"`
UpdatedAt     time.Time `gorm:"autoUpdateTime" json:"updated_at"`
CreatedAt     time.Time `gorm:"autoCreateTime" json:"created_at"`
}

// TableName 映射表名
func (HolidayOperatorLoct) TableName() string {
return "sys.holiday_operator_loct"
}

// HolidayOperatorCustom 自定义假日表
type HolidayOperatorCustom struct {
ID          int64     `gorm:"primaryKey" json:"id"`
OperatorID  int64     `gorm:"type:bigint;index"`
Name        string    `gorm:"type:varchar(100);index" json:"name"`
Date        time.Time `gorm:"type:date;index" json:"date"`
Type        string    `gorm:"type:varchar(20)" json:"type"`
IsActive    string    `gorm:"type:char(1);default:'"'"'Y'"'"'" json:"is_active"`
UpdatedAt   time.Time `gorm:"autoUpdateTime" json:"updated_at"`
CreatedAt   time.Time `gorm:"autoCreateTime" json:"created_at"`
}

// TableName 映射表名
func (HolidayOperatorCustom) TableName() string {
return "sys.holiday_operator_custom"
}

// HolidayDisaster 天灾假日表
type HolidayDisaster struct {
ID          int64     `gorm:"primaryKey" json:"id"`
OperatorID  int64     `gorm:"type:bigint;index"`
Name        string    `gorm:"type:varchar(100);index" json:"name"`
StartDate   time.Time `gorm:"type:date;index" json:"start_date"`
EndDate     time.Time `gorm:"type:date;index" json:"end_date"`
Region      string    `gorm:"type:varchar(100);index" json:"region"`
Type        string    `gorm:"type:varchar(20)" json:"type"`
IsActive    string    `gorm:"type:char(1);default:'"'"'Y'"'"'" json:"is_active"`
UpdatedAt   time.Time `gorm:"autoUpdateTime" json:"updated_at"`
CreatedAt   time.Time `gorm:"autoCreateTime" json:"created_at"`
}

// TableName 映射表名
func (HolidayDisaster) TableName() string {
return "sys.holiday_disaster"
}
