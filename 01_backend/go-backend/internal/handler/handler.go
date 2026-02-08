package handler

import (
	"holiday-system/internal/domain"
	"holiday-system/internal/service"
	"net/http"
	"strconv"
	"time"

	"github.com/gin-gonic/gin"
)

// HolidayHandler 假日處理器
type HolidayHandler struct {
	service *service.HolidayService
}

// NewHolidayHandler 創建假日處理器
func NewHolidayHandler(svc *service.HolidayService) *HolidayHandler {
	return &HolidayHandler{service: svc}
}

// GetHolidays 查詢假日
// @Summary 查詢假日資訊
// @Description 根據日期和營運機構查詢假日
// @Tags holidays
// @Produce json
// @Param date query string false "查詢日期 (YYYY-MM-DD)"
// @Param operator query string false "營運機構名稱"
// @Param start_date query string false "開始日期 (YYYY-MM-DD)"
// @Param end_date query string false "結束日期 (YYYY-MM-DD)"
// @Success 200 {object} map[string]interface{}
// @Router /api/v1/holidays [get]
func (h *HolidayHandler) GetHolidays(c *gin.Context) {
	date := c.Query("date")
	operator := c.Query("operator")
	startDate := c.Query("start_date")
	endDate := c.Query("end_date")

	if startDate != "" && endDate != "" {
		// 範圍查詢
		start, _ := time.Parse("2006-01-02", startDate)
		end, _ := time.Parse("2006-01-02", endDate)
		holidays, err := h.service.GetHolidaysByRange(start, end, operator)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}
		c.JSON(http.StatusOK, gin.H{"data": holidays})
	} else if date != "" {
		// 單日查詢
		t, _ := time.Parse("2006-01-02", date)
		holiday, err := h.service.GetHolidayInfo(t, operator)
		if err != nil {
			c.JSON(http.StatusNotFound, gin.H{"error": "holiday not found"})
			return
		}
		c.JSON(http.StatusOK, gin.H{"data": holiday})
	} else {
		c.JSON(http.StatusBadRequest, gin.H{"error": "date or date range is required"})
	}
}

// CreateHoliday 創建假日
// @Summary 創建假日
// @Description 創建新的假日記錄
// @Tags holidays
// @Accept json
// @Produce json
// @Param holiday body domain.HolidayOperator true "假日資訊"
// @Success 201 {object} domain.HolidayOperator
// @Router /api/v1/holidays [post]
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

	c.JSON(http.StatusCreated, holiday)
}

// UpdateHoliday 更新假日
// @Summary 更新假日
// @Description 更新假日記錄
// @Tags holidays
// @Accept json
// @Produce json
// @Param id path int true "假日ID"
// @Param holiday body domain.HolidayOperator true "假日資訊"
// @Success 200 {object} domain.HolidayOperator
// @Router /api/v1/holidays/{id} [put]
func (h *HolidayHandler) UpdateHoliday(c *gin.Context) {
	var holiday domain.HolidayOperator
	if err := c.ShouldBindJSON(&holiday); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	if err := h.service.UpdateHoliday(&holiday); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, holiday)
}

// DeleteHoliday 刪除假日
// @Summary 刪除假日
// @Description 軟刪除假日記錄
// @Tags holidays
// @Produce json
// @Param id path int true "假日ID"
// @Success 204
// @Router /api/v1/holidays/{id} [delete]
func (h *HolidayHandler) DeleteHoliday(c *gin.Context) {
	id, _ := strconv.ParseInt(c.Param("id"), 10, 64)
	if err := h.service.DeleteHoliday(id); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	c.Status(http.StatusNoContent)
}

// CustomHolidayHandler 自訂義假日處理器
type CustomHolidayHandler struct {
	service *service.CustomHolidayService
}

// NewCustomHolidayHandler 創建自訂義假日處理器
func NewCustomHolidayHandler(svc *service.CustomHolidayService) *CustomHolidayHandler {
	return &CustomHolidayHandler{service: svc}
}

// GetCustomHolidays 查詢自訂義假日
func (h *CustomHolidayHandler) GetCustomHolidays(c *gin.Context) {
	operationID := c.Query("operation_id")
	if operationID == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "operation_id is required"})
		return
	}

	holidays, err := h.service.GetCustomHolidaysByOperator(operationID)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": holidays})
}

// CreateCustomHoliday 創建自訂義假日
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

	c.JSON(http.StatusCreated, holiday)
}

// UpdateCustomHoliday 更新自訂義假日
func (h *CustomHolidayHandler) UpdateCustomHoliday(c *gin.Context) {
	var holiday domain.HolidayOperatorCustom
	if err := c.ShouldBindJSON(&holiday); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	if err := h.service.UpdateCustomHoliday(&holiday); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, holiday)
}

// DeleteCustomHoliday 刪除自訂義假日
func (h *CustomHolidayHandler) DeleteCustomHoliday(c *gin.Context) {
	id := c.Param("id")
	if err := h.service.DeleteCustomHoliday(id); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	c.Status(http.StatusNoContent)
}

// DisasterHolidayHandler 天災假日處理器
type DisasterHolidayHandler struct {
	service *service.DisasterHolidayService
}

// NewDisasterHolidayHandler 創建天災假日處理器
func NewDisasterHolidayHandler(svc *service.DisasterHolidayService) *DisasterHolidayHandler {
	return &DisasterHolidayHandler{service: svc}
}

// GetDisasterHolidays 查詢天災假日
func (h *DisasterHolidayHandler) GetDisasterHolidays(c *gin.Context) {
	date := c.Query("date")
	if date == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "date is required"})
		return
	}

	t, _ := time.Parse("2006-01-02", date)
	holidays, err := h.service.GetDisasterHolidaysByDate(t)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": holidays})
}

// CreateDisasterHoliday 創建天災假日
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

	c.JSON(http.StatusCreated, holiday)
}

// Health 健康檢查
func Health(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{"status": "ok"})
}
