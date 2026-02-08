package service

import (
	"go-backend/internal/domain"
	"go-backend/internal/repository"
	"time"

	"github.com/google/uuid"
)

// HolidayService ?‡æ—¥æ¥­å??è¼¯
type HolidayService struct {
	holidayRepo         *repository.HolidayRepository
	customHolidayRepo   *repository.CustomHolidayRepository
	disasterHolidayRepo *repository.DisasterHolidayRepository
}

// NewHolidayService ?µå»º?‡æ—¥?å?å¯¦ä?
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

// DetermineHoliday ?¤æ–·?‡ä»£ç¢¼é?è¼¯ï??ªè? > å¤©ç½ > ?ºå?
func (s *HolidayService) DetermineHoliday(
	baseHoliday bool,
	disaster bool,
	customRule *string,
) string {
	// ?ªè?è¦å??€é«˜å„ª??
	if customRule != nil {
		return *customRule
	}

	// å¤©ç½?‡æ—¥æ¬¡å„ª??
	if disaster {
		return "holiday"
	}

	// ?€å¾Œä??ºå?æ±ºå?
	if baseHoliday {
		return "holiday"
	}
	return "workday"
}

// GetHolidayInfo ?¥è©¢?‡æ—¥è³‡è?
func (s *HolidayService) GetHolidayInfo(date time.Time, operator string) (*domain.HolidayOperator, error) {
	return s.holidayRepo.GetHolidayByDate(date, operator)
}

// GetHolidaysByRange ?¥è©¢?‡æ—¥ç¯„å?
func (s *HolidayService) GetHolidaysByRange(startDate, endDate time.Time, operator string) ([]domain.HolidayOperator, error) {
	return s.holidayRepo.GetHolidaysByDateRange(startDate, endDate, operator)
}

// CreateHoliday ?µå»º?‡æ—¥
func (s *HolidayService) CreateHoliday(holiday *domain.HolidayOperator) error {
	if holiday.IsActive == "" {
		holiday.IsActive = "Y"
	}
	return s.holidayRepo.CreateHoliday(holiday)
}

// UpdateHoliday ?´æ–°?‡æ—¥
func (s *HolidayService) UpdateHoliday(holiday *domain.HolidayOperator) error {
	return s.holidayRepo.UpdateHoliday(holiday)
}

// DeleteHoliday ?ªé™¤?‡æ—¥
func (s *HolidayService) DeleteHoliday(id int64) error {
	return s.holidayRepo.DeleteHoliday(id)
}

// CustomHolidayService ?ªè?ç¾©å??¥æ¥­?™é?è¼?
type CustomHolidayService struct {
	repo *repository.CustomHolidayRepository
}

// NewCustomHolidayService ?µå»º?ªè?ç¾©å??¥æ??™å¯¦ä¾?
func NewCustomHolidayService(repo *repository.CustomHolidayRepository) *CustomHolidayService {
	return &CustomHolidayService{repo: repo}
}

// GetCustomHolidaysByOperator ?¥è©¢?ªè?ç¾©å???
func (s *CustomHolidayService) GetCustomHolidaysByOperator(operationID string) ([]domain.HolidayOperatorCustom, error) {
	return s.repo.GetCustomHolidaysByOperator(operationID)
}

// CreateCustomHoliday ?µå»º?ªè?ç¾©å???
func (s *CustomHolidayService) CreateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
	if holiday.ID == "" {
		holiday.ID = uuid.New().String()
	}
	if holiday.IsActive == "" {
		holiday.IsActive = "Y"
	}
	return s.repo.CreateCustomHoliday(holiday)
}

// UpdateCustomHoliday ?´æ–°?ªè?ç¾©å???
func (s *CustomHolidayService) UpdateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
	return s.repo.UpdateCustomHoliday(holiday)
}

// DeleteCustomHoliday ?ªé™¤?ªè?ç¾©å???
func (s *CustomHolidayService) DeleteCustomHoliday(id string) error {
	return s.repo.DeleteCustomHoliday(id)
}

// DisasterHolidayService å¤©ç½?‡æ—¥æ¥­å??è¼¯
type DisasterHolidayService struct {
	repo *repository.DisasterHolidayRepository
}

// NewDisasterHolidayService ?µå»ºå¤©ç½?‡æ—¥?å?å¯¦ä?
func NewDisasterHolidayService(repo *repository.DisasterHolidayRepository) *DisasterHolidayService {
	return &DisasterHolidayService{repo: repo}
}

// GetDisasterHolidaysByDate ?¥è©¢å¤©ç½?‡æ—¥
func (s *DisasterHolidayService) GetDisasterHolidaysByDate(date time.Time) ([]domain.HolidayDisaster, error) {
	return s.repo.GetDisasterHolidaysByDate(date)
}

// CreateDisasterHoliday ?µå»ºå¤©ç½?‡æ—¥
func (s *DisasterHolidayService) CreateDisasterHoliday(holiday *domain.HolidayDisaster) error {
	if holiday.IsActive == "" {
		holiday.IsActive = "Y"
	}
	return s.repo.CreateDisasterHoliday(holiday)
}

// UpdateDisasterHoliday ?´æ–°å¤©ç½?‡æ—¥
func (s *DisasterHolidayService) UpdateDisasterHoliday(holiday *domain.HolidayDisaster) error {
	return s.repo.UpdateDisasterHoliday(holiday)
}
