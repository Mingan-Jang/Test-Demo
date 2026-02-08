package service

import (
	"holiday-system/internal/domain"
	"holiday-system/internal/repository"
	"time"

	"github.com/google/uuid"
)

// HolidayService 假日業務邏輯
type HolidayService struct {
	holidayRepo         *repository.HolidayRepository
	customHolidayRepo   *repository.CustomHolidayRepository
	disasterHolidayRepo *repository.DisasterHolidayRepository
}

// NewHolidayService 創建假日服務實例
func NewHolidayService(
	holidayRepo *repository.HolidayRepository,
	customHolidayRepo *repository.CustomHolidayRepository,
	disasterHolidayRepo *repository.DisasterHolidayRepository,
) *HolidayService {
	return &HolidayService{
		holidayRepo:         holidayRepo,
		customHolidayRepo:   customHolidayRepo,
		disasterHolidayRepo: disasterHolidayRepo,
	}
}

// DetermineHoliday 判斷假代碼邏輯：自訂 > 天災 > 基底
func (s *HolidayService) DetermineHoliday(
	baseHoliday bool,
	disaster bool,
	customRule *string,
) string {
	// 自訂規則最高優先
	if customRule != nil {
		return *customRule
	}

	// 天災假日次優先
	if disaster {
		return "holiday"
	}

	// 最後依基底決定
	if baseHoliday {
		return "holiday"
	}
	return "workday"
}

// GetHolidayInfo 查詢假日資訊
func (s *HolidayService) GetHolidayInfo(date time.Time, operator string) (*domain.HolidayOperator, error) {
	return s.holidayRepo.GetHolidayByDate(date, operator)
}

// GetHolidaysByRange 查詢假日範圍
func (s *HolidayService) GetHolidaysByRange(startDate, endDate time.Time, operator string) ([]domain.HolidayOperator, error) {
	return s.holidayRepo.GetHolidaysByDateRange(startDate, endDate, operator)
}

// CreateHoliday 創建假日
func (s *HolidayService) CreateHoliday(holiday *domain.HolidayOperator) error {
	if holiday.IsActive == "" {
		holiday.IsActive = "Y"
	}
	return s.holidayRepo.CreateHoliday(holiday)
}

// UpdateHoliday 更新假日
func (s *HolidayService) UpdateHoliday(holiday *domain.HolidayOperator) error {
	return s.holidayRepo.UpdateHoliday(holiday)
}

// DeleteHoliday 刪除假日
func (s *HolidayService) DeleteHoliday(id int64) error {
	return s.holidayRepo.DeleteHoliday(id)
}

// CustomHolidayService 自訂義假日業務邏輯
type CustomHolidayService struct {
	repo *repository.CustomHolidayRepository
}

// NewCustomHolidayService 創建自訂義假日服務實例
func NewCustomHolidayService(repo *repository.CustomHolidayRepository) *CustomHolidayService {
	return &CustomHolidayService{repo: repo}
}

// GetCustomHolidaysByOperator 查詢自訂義假日
func (s *CustomHolidayService) GetCustomHolidaysByOperator(operationID string) ([]domain.HolidayOperatorCustom, error) {
	return s.repo.GetCustomHolidaysByOperator(operationID)
}

// CreateCustomHoliday 創建自訂義假日
func (s *CustomHolidayService) CreateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
	if holiday.ID == "" {
		holiday.ID = uuid.New().String()
	}
	if holiday.IsActive == "" {
		holiday.IsActive = "Y"
	}
	return s.repo.CreateCustomHoliday(holiday)
}

// UpdateCustomHoliday 更新自訂義假日
func (s *CustomHolidayService) UpdateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
	return s.repo.UpdateCustomHoliday(holiday)
}

// DeleteCustomHoliday 刪除自訂義假日
func (s *CustomHolidayService) DeleteCustomHoliday(id string) error {
	return s.repo.DeleteCustomHoliday(id)
}

// DisasterHolidayService 天災假日業務邏輯
type DisasterHolidayService struct {
	repo *repository.DisasterHolidayRepository
}

// NewDisasterHolidayService 創建天災假日服務實例
func NewDisasterHolidayService(repo *repository.DisasterHolidayRepository) *DisasterHolidayService {
	return &DisasterHolidayService{repo: repo}
}

// GetDisasterHolidaysByDate 查詢天災假日
func (s *DisasterHolidayService) GetDisasterHolidaysByDate(date time.Time) ([]domain.HolidayDisaster, error) {
	return s.repo.GetDisasterHolidaysByDate(date)
}

// CreateDisasterHoliday 創建天災假日
func (s *DisasterHolidayService) CreateDisasterHoliday(holiday *domain.HolidayDisaster) error {
	if holiday.IsActive == "" {
		holiday.IsActive = "Y"
	}
	return s.repo.CreateDisasterHoliday(holiday)
}

// UpdateDisasterHoliday 更新天災假日
func (s *DisasterHolidayService) UpdateDisasterHoliday(holiday *domain.HolidayDisaster) error {
	return s.repo.UpdateDisasterHoliday(holiday)
}
