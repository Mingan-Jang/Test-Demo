package repository

import (
	"holiday-system/internal/domain"
	"time"

	"gorm.io/gorm"
)

// HolidayRepository 假日倉儲
type HolidayRepository struct {
	db *gorm.DB
}

// NewHolidayRepository 創建假日倉儲實例
func NewHolidayRepository(db *gorm.DB) *HolidayRepository {
	return &HolidayRepository{db: db}
}

// GetHolidayByDate 根據日期和營運機構查詢假日
func (r *HolidayRepository) GetHolidayByDate(date time.Time, operator string) (*domain.HolidayOperator, error) {
	var holiday domain.HolidayOperator
	query := r.db.Where("date = ? AND is_active = 'Y'", date)
	if operator != "" {
		query = query.Where("operator = ?", operator)
	}
	if err := query.First(&holiday).Error; err != nil {
		return nil, err
	}
	return &holiday, nil
}

// GetHolidaysByDateRange 查詢日期範圍內的假日
func (r *HolidayRepository) GetHolidaysByDateRange(startDate, endDate time.Time, operator string) ([]domain.HolidayOperator, error) {
	var holidays []domain.HolidayOperator
	query := r.db.Where("date BETWEEN ? AND ? AND is_active = 'Y'", startDate, endDate)
	if operator != "" {
		query = query.Where("operator = ?", operator)
	}
	if err := query.Find(&holidays).Error; err != nil {
		return nil, err
	}
	return holidays, nil
}

// CreateHoliday 創建假日記錄
func (r *HolidayRepository) CreateHoliday(holiday *domain.HolidayOperator) error {
	return r.db.Create(holiday).Error
}

// UpdateHoliday 更新假日記錄
func (r *HolidayRepository) UpdateHoliday(holiday *domain.HolidayOperator) error {
	return r.db.Save(holiday).Error
}

// DeleteHoliday 軟刪除假日記錄
func (r *HolidayRepository) DeleteHoliday(id int64) error {
	return r.db.Model(&domain.HolidayOperator{}).Where("id = ?", id).Update("is_active", "N").Error
}

// CustomHolidayRepository 自訂義假日倉儲
type CustomHolidayRepository struct {
	db *gorm.DB
}

// NewCustomHolidayRepository 創建自訂義假日倉儲實例
func NewCustomHolidayRepository(db *gorm.DB) *CustomHolidayRepository {
	return &CustomHolidayRepository{db: db}
}

// GetCustomHolidaysByOperator 查詢營運機構的自訂假日
func (r *CustomHolidayRepository) GetCustomHolidaysByOperator(operationID string) ([]domain.HolidayOperatorCustom, error) {
	var holidays []domain.HolidayOperatorCustom
	if err := r.db.Where("operation_id = ? AND is_active = 'Y'", operationID).Find(&holidays).Error; err != nil {
		return nil, err
	}
	return holidays, nil
}

// GetCustomHolidayByDate 查詢特定日期的自訂假日
func (r *CustomHolidayRepository) GetCustomHolidayByDate(operationID string, date time.Time) (*domain.HolidayOperatorCustom, error) {
	var holiday domain.HolidayOperatorCustom
	if err := r.db.Where("operation_id = ? AND date = ? AND is_active = 'Y'", operationID, date).First(&holiday).Error; err != nil {
		return nil, err
	}
	return &holiday, nil
}

// CreateCustomHoliday 創建自訂義假日
func (r *CustomHolidayRepository) CreateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
	return r.db.Create(holiday).Error
}

// UpdateCustomHoliday 更新自訂義假日
func (r *CustomHolidayRepository) UpdateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
	return r.db.Save(holiday).Error
}

// DeleteCustomHoliday 軟刪除自訂義假日
func (r *CustomHolidayRepository) DeleteCustomHoliday(id string) error {
	return r.db.Model(&domain.HolidayOperatorCustom{}).Where("id = ?", id).Update("is_active", "N").Error
}

// DisasterHolidayRepository 天災假日倉儲
type DisasterHolidayRepository struct {
	db *gorm.DB
}

// NewDisasterHolidayRepository 創建天災假日倉儲實例
func NewDisasterHolidayRepository(db *gorm.DB) *DisasterHolidayRepository {
	return &DisasterHolidayRepository{db: db}
}

// GetDisasterHolidaysByDate 查詢特定日期的天災假日
func (r *DisasterHolidayRepository) GetDisasterHolidaysByDate(date time.Time) ([]domain.HolidayDisaster, error) {
	var holidays []domain.HolidayDisaster
	if err := r.db.Where("disaster_date = ? AND is_active = 'Y'", date).Find(&holidays).Error; err != nil {
		return nil, err
	}
	return holidays, nil
}

// GetDisasterHolidaysByDateRange 查詢日期範圍內的天災假日
func (r *DisasterHolidayRepository) GetDisasterHolidaysByDateRange(startDate, endDate time.Time) ([]domain.HolidayDisaster, error) {
	var holidays []domain.HolidayDisaster
	if err := r.db.Where("disaster_date BETWEEN ? AND ? AND is_active = 'Y'", startDate, endDate).Find(&holidays).Error; err != nil {
		return nil, err
	}
	return holidays, nil
}

// CreateDisasterHoliday 創建天災假日
func (r *DisasterHolidayRepository) CreateDisasterHoliday(holiday *domain.HolidayDisaster) error {
	return r.db.Create(holiday).Error
}

// UpdateDisasterHoliday 更新天災假日
func (r *DisasterHolidayRepository) UpdateDisasterHoliday(holiday *domain.HolidayDisaster) error {
	return r.db.Save(holiday).Error
}
