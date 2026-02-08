package handler

import (
"net/http"

"go-backend/internal/domain"
"go-backend/internal/service"

"github.com/gin-gonic/gin"
)

// Health 健康检查
func Health(c *gin.Context) {
c.JSON(http.StatusOK, gin.H{
"status": "alive",
})
}

// HolidayHandler 假日处理器
type HolidayHandler struct {
service service.HolidayService
}

// NewHolidayHandler 创建假日处理器
func NewHolidayHandler(s service.HolidayService) *HolidayHandler {
return &HolidayHandler{service: s}
}

// GetHolidays 获取假日列表
func (h *HolidayHandler) GetHolidays(c *gin.Context) {
holidays, err := h.service.GetHolidays()
if err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, holidays)
}

// CreateHoliday 创建假日
func (h *HolidayHandler) CreateHoliday(c *gin.Context) {
var holiday domain.HolidayOperator
if err := c.ShouldBindJSON(&holiday); err != nil {
c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
return
}
if err := h.service.CreateHoliday(&holiday); err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, holiday)
}

// UpdateHoliday 更新假日
func (h *HolidayHandler) UpdateHoliday(c *gin.Context) {
id := c.Param("id")
var holiday domain.HolidayOperator
if err := c.ShouldBindJSON(&holiday); err != nil {
c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
return
}
if err := h.service.UpdateHoliday(id, &holiday); err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, holiday)
}

// DeleteHoliday 删除假日
func (h *HolidayHandler) DeleteHoliday(c *gin.Context) {
id := c.Param("id")
if err := h.service.DeleteHoliday(id); err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, gin.H{"message": "deleted"})
}

// CustomHolidayHandler 自定义假日处理器
type CustomHolidayHandler struct {
service service.CustomHolidayService
}

// NewCustomHolidayHandler 创建自定义假日处理器
func NewCustomHolidayHandler(s service.CustomHolidayService) *CustomHolidayHandler {
return &CustomHolidayHandler{service: s}
}

// GetCustomHolidays 获取自定义假日列表
func (h *CustomHolidayHandler) GetCustomHolidays(c *gin.Context) {
holidays, err := h.service.GetCustomHolidays()
if err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, holidays)
}

// CreateCustomHoliday 创建自定义假日
func (h *CustomHolidayHandler) CreateCustomHoliday(c *gin.Context) {
var holiday domain.HolidayOperatorCustom
if err := c.ShouldBindJSON(&holiday); err != nil {
c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
return
}
if err := h.service.CreateCustomHoliday(&holiday); err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, holiday)
}

// UpdateCustomHoliday 更新自定义假日
func (h *CustomHolidayHandler) UpdateCustomHoliday(c *gin.Context) {
id := c.Param("id")
var holiday domain.HolidayOperatorCustom
if err := c.ShouldBindJSON(&holiday); err != nil {
c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
return
}
if err := h.service.UpdateCustomHoliday(id, &holiday); err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, holiday)
}

// DeleteCustomHoliday 删除自定义假日
func (h *CustomHolidayHandler) DeleteCustomHoliday(c *gin.Context) {
id := c.Param("id")
if err := h.service.DeleteCustomHoliday(id); err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, gin.H{"message": "deleted"})
}

// DisasterHolidayHandler 天灾假日处理器
type DisasterHolidayHandler struct {
service service.DisasterHolidayService
}

// NewDisasterHolidayHandler 创建天灾假日处理器
func NewDisasterHolidayHandler(s service.DisasterHolidayService) *DisasterHolidayHandler {
return &DisasterHolidayHandler{service: s}
}

// GetDisasterHolidays 获取天灾假日列表
func (h *DisasterHolidayHandler) GetDisasterHolidays(c *gin.Context) {
holidays, err := h.service.GetDisasterHolidays()
if err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, holidays)
}

// CreateDisasterHoliday 创建天灾假日
func (h *DisasterHolidayHandler) CreateDisasterHoliday(c *gin.Context) {
var holiday domain.HolidayDisaster
if err := c.ShouldBindJSON(&holiday); err != nil {
c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
return
}
if err := h.service.CreateDisasterHoliday(&holiday); err != nil {
c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
return
}
c.JSON(http.StatusOK, holiday)
}
