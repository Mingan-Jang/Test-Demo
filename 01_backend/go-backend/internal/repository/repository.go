package repository

import (
	"go-backend/internal/domain"
	"time"

	"gorm.io/gorm"
)

// HolidayRepository ?‡æ—¥?‰å„²
type HolidayRepository struct {
	db *gorm.DB
}

// NewHolidayRepository ?µå»º?‡æ—¥?‰å„²å¯¦ä?
func NewHolidayRepository(db *gorm.DB) *HolidayRepository {
	return &HolidayRepository{db: db}
}

// GetHolidayByDate ?¹æ??¥æ??Œç??‹æ?æ§‹æŸ¥è©¢å???
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

// GetHolidaysByDateRange ?¥è©¢?¥æ?ç¯„å??§ç??‡æ—¥
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

// CreateHoliday ?µå»º?‡æ—¥è¨˜é?
func (r *HolidayRepository) CreateHoliday(holiday *domain.HolidayOperator) error {
	return r.db.Create(holiday).Error
}

// UpdateHoliday ?´æ–°?‡æ—¥è¨˜é?
func (r *HolidayRepository) UpdateHoliday(holiday *domain.HolidayOperator) error {
	return r.db.Save(holiday).Error
}

// DeleteHoliday è»Ÿåˆª?¤å??¥è???
func (r *HolidayRepository) DeleteHoliday(id int64) error {
	return r.db.Model(&domain.HolidayOperator{}).Where("id = ?", id).Update("is_active", "N").Error
}

// CustomHolidayRepository ?ªè?ç¾©å??¥å€‰å„²
type CustomHolidayRepository struct {
	db *gorm.DB
}

// NewCustomHolidayRepository ?µå»º?ªè?ç¾©å??¥å€‰å„²å¯¦ä?
func NewCustomHolidayRepository(db *gorm.DB) *CustomHolidayRepository {
	return &CustomHolidayRepository{db: db}
}

// GetCustomHolidaysByOperator ?¥è©¢?Ÿé?æ©Ÿæ??„è‡ªè¨‚å???
func (r *CustomHolidayRepository) GetCustomHolidaysByOperator(operationID string) ([]domain.HolidayOperatorCustom, error) {
	var holidays []domain.HolidayOperatorCustom
	if err := r.db.Where("operation_id = ? AND is_active = 'Y'", operationID).Find(&holidays).Error; err != nil {
		return nil, err
	}
	return holidays, nil
}

// GetCustomHolidayByDate ?¥è©¢?¹å??¥æ??„è‡ªè¨‚å???
func (r *CustomHolidayRepository) GetCustomHolidayByDate(operationID string, date time.Time) (*domain.HolidayOperatorCustom, error) {
	var holiday domain.HolidayOperatorCustom
	if err := r.db.Where("operation_id = ? AND date = ? AND is_active = 'Y'", operationID, date).First(&holiday).Error; err != nil {
		return nil, err
	}
	return &holiday, nil
}

// CreateCustomHoliday ?µå»º?ªè?ç¾©å???
func (r *CustomHolidayRepository) CreateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
	return r.db.Create(holiday).Error
}

// UpdateCustomHoliday ?´æ–°?ªè?ç¾©å???
func (r *CustomHolidayRepository) UpdateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
	return r.db.Save(holiday).Error
}

// DeleteCustomHoliday è»Ÿåˆª?¤è‡ªè¨‚ç¾©?‡æ—¥
func (r *CustomHolidayRepository) DeleteCustomHoliday(id string) error {
	return r.db.Model(&domain.HolidayOperatorCustom{}).Where("id = ?", id).Update("is_active", "N").Error
}

// DisasterHolidayRepository å¤©ç½?‡æ—¥?‰å„²
type DisasterHolidayRepository struct {
	db *gorm.DB
}

// NewDisasterHolidayRepository ?µå»ºå¤©ç½?‡æ—¥?‰å„²å¯¦ä?
func NewDisasterHolidayRepository(db *gorm.DB) *DisasterHolidayRepository {
	return &DisasterHolidayRepository{db: db}
}

// GetDisasterHolidaysByDate ?¥è©¢?¹å??¥æ??„å¤©?½å???
func (r *DisasterHolidayRepository) GetDisasterHolidaysByDate(date time.Time) ([]domain.HolidayDisaster, error) {
	var holidays []domain.HolidayDisaster
	if err := r.db.Where("disaster_date = ? AND is_active = 'Y'", date).Find(&holidays).Error; err != nil {
		return nil, err
	}
	return holidays, nil
}

// GetDisasterHolidaysByDateRange ?¥è©¢?¥æ?ç¯„å??§ç?å¤©ç½?‡æ—¥
func (r *DisasterHolidayRepository) GetDisasterHolidaysByDateRange(startDate, endDate time.Time) ([]domain.HolidayDisaster, error) {
	var holidays []domain.HolidayDisaster
	if err := r.db.Where("disaster_date BETWEEN ? AND ? AND is_active = 'Y'", startDate, endDate).Find(&holidays).Error; err != nil {
		return nil, err
	}
	return holidays, nil
}

// CreateDisasterHoliday ?µå»ºå¤©ç½?‡æ—¥
func (r *DisasterHolidayRepository) CreateDisasterHoliday(holiday *domain.HolidayDisaster) error {
	return r.db.Create(holiday).Error
}

// UpdateDisasterHoliday ?´æ–°å¤©ç½?‡æ—¥
func (r *DisasterHolidayRepository) UpdateDisasterHoliday(holiday *domain.HolidayDisaster) error {
	return r.db.Save(holiday).Error
}
