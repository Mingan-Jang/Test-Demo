package service

import (
"go-backend/internal/domain"
"go-backend/internal/repository"
)

// HolidayService 假日服务接口
type HolidayService interface {
GetHolidays() ([]domain.HolidayOperator, error)
GetHolidayByID(id string) (*domain.HolidayOperator, error)
CreateHoliday(holiday *domain.HolidayOperator) error
UpdateHoliday(id string, holiday *domain.HolidayOperator) error
DeleteHoliday(id string) error
}

// holidayService 假日服务实现
type holidayService struct {
repo repository.HolidayRepository
}

// NewHolidayService 创建假日服务
func NewHolidayService(repo repository.HolidayRepository, customRepo repository.CustomHolidayRepository, disasterRepo repository.DisasterHolidayRepository) HolidayService {
return &holidayService{repo: repo}
}

// GetHolidays 获取所有假日
func (s *holidayService) GetHolidays() ([]domain.HolidayOperator, error) {
return s.repo.GetAll()
}

// GetHolidayByID 按ID获取假日
func (s *holidayService) GetHolidayByID(id string) (*domain.HolidayOperator, error) {
return s.repo.GetByID(id)
}

// CreateHoliday 创建假日
func (s *holidayService) CreateHoliday(holiday *domain.HolidayOperator) error {
return s.repo.Create(holiday)
}

// UpdateHoliday 更新假日
func (s *holidayService) UpdateHoliday(id string, holiday *domain.HolidayOperator) error {
return s.repo.Update(holiday)
}

// DeleteHoliday 删除假日
func (s *holidayService) DeleteHoliday(id string) error {
return s.repo.Delete(id)
}

// CustomHolidayService 自定义假日服务接口
type CustomHolidayService interface {
GetCustomHolidays() ([]domain.HolidayOperatorCustom, error)
CreateCustomHoliday(holiday *domain.HolidayOperatorCustom) error
UpdateCustomHoliday(id string, holiday *domain.HolidayOperatorCustom) error
DeleteCustomHoliday(id string) error
}

// customHolidayService 自定义假日服务实现
type customHolidayService struct {
repo repository.CustomHolidayRepository
}

// NewCustomHolidayService 创建自定义假日服务
func NewCustomHolidayService(repo repository.CustomHolidayRepository) CustomHolidayService {
return &customHolidayService{repo: repo}
}

// GetCustomHolidays 获取所有自定义假日
func (s *customHolidayService) GetCustomHolidays() ([]domain.HolidayOperatorCustom, error) {
return s.repo.GetAll()
}

// CreateCustomHoliday 创建自定义假日
func (s *customHolidayService) CreateCustomHoliday(holiday *domain.HolidayOperatorCustom) error {
return s.repo.Create(holiday)
}

// UpdateCustomHoliday 更新自定义假日
func (s *customHolidayService) UpdateCustomHoliday(id string, holiday *domain.HolidayOperatorCustom) error {
return s.repo.Update(holiday)
}

// DeleteCustomHoliday 删除自定义假日
func (s *customHolidayService) DeleteCustomHoliday(id string) error {
return s.repo.Delete(id)
}

// DisasterHolidayService 天灾假日服务接口
type DisasterHolidayService interface {
GetDisasterHolidays() ([]domain.HolidayDisaster, error)
CreateDisasterHoliday(holiday *domain.HolidayDisaster) error
}

// disasterHolidayService 天灾假日服务实现
type disasterHolidayService struct {
repo repository.DisasterHolidayRepository
}

// NewDisasterHolidayService 创建天灾假日服务
func NewDisasterHolidayService(repo repository.DisasterHolidayRepository) DisasterHolidayService {
return &disasterHolidayService{repo: repo}
}

// GetDisasterHolidays 获取所有天灾假日
func (s *disasterHolidayService) GetDisasterHolidays() ([]domain.HolidayDisaster, error) {
return s.repo.GetAll()
}

// CreateDisasterHoliday 创建天灾假日
func (s *disasterHolidayService) CreateDisasterHoliday(holiday *domain.HolidayDisaster) error {
return s.repo.Create(holiday)
}
