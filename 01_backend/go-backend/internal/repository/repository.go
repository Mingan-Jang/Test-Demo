package repository

import (
"go-backend/internal/domain"

"gorm.io/gorm"
)

// HolidayRepository 假日仓储接口
type HolidayRepository interface {
GetAll() ([]domain.HolidayOperator, error)
GetByID(id string) (*domain.HolidayOperator, error)
Create(holiday *domain.HolidayOperator) error
Update(holiday *domain.HolidayOperator) error
Delete(id string) error
}

// holidayRepo 假日仓储实现
type holidayRepo struct {
db *gorm.DB
}

// NewHolidayRepository 创建假日仓储
func NewHolidayRepository(db *gorm.DB) HolidayRepository {
return &holidayRepo{db: db}
}

// GetAll 获取所有假日
func (r *holidayRepo) GetAll() ([]domain.HolidayOperator, error) {
var holidays []domain.HolidayOperator
if err := r.db.Find(&holidays).Error; err != nil {
return nil, err
}
return holidays, nil
}

// GetByID 按ID获取假日
func (r *holidayRepo) GetByID(id string) (*domain.HolidayOperator, error) {
var holiday domain.HolidayOperator
if err := r.db.First(&holiday, id).Error; err != nil {
return nil, err
}
return &holiday, nil
}

// Create 创建假日
func (r *holidayRepo) Create(holiday *domain.HolidayOperator) error {
return r.db.Create(holiday).Error
}

// Update 更新假日
func (r *holidayRepo) Update(holiday *domain.HolidayOperator) error {
return r.db.Save(holiday).Error
}

// Delete 删除假日
func (r *holidayRepo) Delete(id string) error {
return r.db.Delete(&domain.HolidayOperator{}, id).Error
}

// CustomHolidayRepository 自定义假日仓储接口
type CustomHolidayRepository interface {
GetAll() ([]domain.HolidayOperatorCustom, error)
GetByID(id string) (*domain.HolidayOperatorCustom, error)
Create(holiday *domain.HolidayOperatorCustom) error
Update(holiday *domain.HolidayOperatorCustom) error
Delete(id string) error
}

// customHolidayRepo 自定义假日仓储实现
type customHolidayRepo struct {
db *gorm.DB
}

// NewCustomHolidayRepository 创建自定义假日仓储
func NewCustomHolidayRepository(db *gorm.DB) CustomHolidayRepository {
return &customHolidayRepo{db: db}
}

// GetAll 获取所有自定义假日
func (r *customHolidayRepo) GetAll() ([]domain.HolidayOperatorCustom, error) {
var holidays []domain.HolidayOperatorCustom
if err := r.db.Find(&holidays).Error; err != nil {
return nil, err
}
return holidays, nil
}

// GetByID 按ID获取自定义假日
func (r *customHolidayRepo) GetByID(id string) (*domain.HolidayOperatorCustom, error) {
var holiday domain.HolidayOperatorCustom
if err := r.db.First(&holiday, id).Error; err != nil {
return nil, err
}
return &holiday, nil
}

// Create 创建自定义假日
func (r *customHolidayRepo) Create(holiday *domain.HolidayOperatorCustom) error {
return r.db.Create(holiday).Error
}

// Update 更新自定义假日
func (r *customHolidayRepo) Update(holiday *domain.HolidayOperatorCustom) error {
return r.db.Save(holiday).Error
}

// Delete 删除自定义假日
func (r *customHolidayRepo) Delete(id string) error {
return r.db.Delete(&domain.HolidayOperatorCustom{}, id).Error
}

// DisasterHolidayRepository 天灾假日仓储接口
type DisasterHolidayRepository interface {
GetAll() ([]domain.HolidayDisaster, error)
GetByID(id string) (*domain.HolidayDisaster, error)
Create(holiday *domain.HolidayDisaster) error
Update(holiday *domain.HolidayDisaster) error
Delete(id string) error
}

// disasterHolidayRepo 天灾假日仓储实现
type disasterHolidayRepo struct {
db *gorm.DB
}

// NewDisasterHolidayRepository 创建天灾假日仓储
func NewDisasterHolidayRepository(db *gorm.DB) DisasterHolidayRepository {
return &disasterHolidayRepo{db: db}
}

// GetAll 获取所有天灾假日
func (r *disasterHolidayRepo) GetAll() ([]domain.HolidayDisaster, error) {
var holidays []domain.HolidayDisaster
if err := r.db.Find(&holidays).Error; err != nil {
return nil, err
}
return holidays, nil
}

// GetByID 按ID获取天灾假日
func (r *disasterHolidayRepo) GetByID(id string) (*domain.HolidayDisaster, error) {
var holiday domain.HolidayDisaster
if err := r.db.First(&holiday, id).Error; err != nil {
return nil, err
}
return &holiday, nil
}

// Create 创建天灾假日
func (r *disasterHolidayRepo) Create(holiday *domain.HolidayDisaster) error {
return r.db.Create(holiday).Error
}

// Update 更新天灾假日
func (r *disasterHolidayRepo) Update(holiday *domain.HolidayDisaster) error {
return r.db.Save(holiday).Error
}

// Delete 删除天灾假日
func (r *disasterHolidayRepo) Delete(id string) error {
return r.db.Delete(&domain.HolidayDisaster{}, id).Error
}
