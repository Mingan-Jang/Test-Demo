package domain

import (
	"time"
)

// HolidayOperator 假日表（營運機構層級）
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
	IsActive      string    `gorm:"type:char(1);default:'Y'" json:"is_active"`
	UpdatedAt     time.Time `gorm:"autoUpdateTime" json:"updated_at"`
	CreatedAt     time.Time `gorm:"autoCreateTime" json:"created_at"`
}

// TableName 指定表名
func (HolidayOperator) TableName() string {
	return "sys.holiday_operator"
}

// HolidayOperatorLoct 營運機構位置表
type HolidayOperatorLoct struct {
	ID            int64     `gorm:"primaryKey" json:"id"`
	Operator      string    `gorm:"type:varchar(100);index" json:"operator"`
	RegionVillage string    `gorm:"type:varchar(100)" json:"region_village"`
	RegionTown    string    `gorm:"type:varchar(100)" json:"region_town"`
	RegionCity    string    `gorm:"type:varchar(100)" json:"region_city"`
	CreatedAt     time.Time `gorm:"autoCreateTime" json:"created_at"`
	UpdatedAt     time.Time `gorm:"autoUpdateTime" json:"updated_at"`
}

func (HolidayOperatorLoct) TableName() string {
	return "sys.holiday_operator_loct"
}

// HolidayOperatorCustom 自訂義假日表
type HolidayOperatorCustom struct {
	ID          string    `gorm:"primaryKey;type:varchar(36)" json:"id"`
	OperationID string    `gorm:"type:varchar(100);index" json:"operation_id"`
	Date        time.Time `gorm:"type:date;index" json:"date"`
	Type        string    `gorm:"type:varchar(20)" json:"type"` // 'holiday' or 'workday'
	Comment     string    `gorm:"type:varchar(255)" json:"comment"`
	IsActive    string    `gorm:"type:char(1);default:'Y'" json:"is_active"`
	CreatedAt   time.Time `gorm:"autoCreateTime" json:"created_at"`
	UpdatedAt   time.Time `gorm:"autoUpdateTime" json:"updated_at"`
}

func (HolidayOperatorCustom) TableName() string {
	return "sys.holiday_operator_custom"
}

// HolidayDisaster 天災公告表
type HolidayDisaster struct {
	ID             int64     `gorm:"primaryKey" json:"id"`
	DisasterDate   time.Time `gorm:"type:date;index" json:"disaster_date"`
	RegionCity     string    `gorm:"type:varchar(100)" json:"region_city"`
	RegionDistrict string    `gorm:"type:varchar(100)" json:"region_district"`
	Description    string    `gorm:"type:varchar(255)" json:"description"`
	AnnouncedAt    time.Time `json:"announced_at"`
	IsActive       string    `gorm:"type:char(1);default:'Y'" json:"is_active"`
	CreatedAt      time.Time `gorm:"autoCreateTime" json:"created_at"`
	UpdatedAt      time.Time `gorm:"autoUpdateTime" json:"updated_at"`
}

func (HolidayDisaster) TableName() string {
	return "sys.holiday_disaster"
}
