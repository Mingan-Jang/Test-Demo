package handler

import (
	"go-backend/internal/domain"
	"go-backend/internal/service"
	"net/http"
	"strconv"
	"time"

	"github.com/gin-gonic/gin"
)

// HolidayHandler ?‡æ—¥?•ç???
type HolidayHandler struct {
	service *service.HolidayService
}

// NewHolidayHandler ?µå»º?‡æ—¥?•ç???
func NewHolidayHandler(svc *service.HolidayService) *HolidayHandler {
	return &HolidayHandler{service: svc}
}

// GetHolidays ?¥è©¢?‡æ—¥
// @Summary ?¥è©¢?‡æ—¥è³‡è?
// @Description ?¹æ??¥æ??Œç??‹æ?æ§‹æŸ¥è©¢å???
// @Tags holidays
// @Produce json
// @Param date query string false "?¥è©¢?¥æ? (YYYY-MM-DD)"
// @Param operator query string false "?Ÿé?æ©Ÿæ??ç¨±"
// @Param start_date query string false "?‹å??¥æ? (YYYY-MM-DD)"
// @Param end_date query string false "çµæ??¥æ? (YYYY-MM-DD)"
// @Success 200 {object} map[string]interface{}
// @Router /api/v1/holidays [get]
func (h *HolidayHandler) GetHolidays(c *gin.Context) {
	date := c.Query("date")
	operator := c.Query("operator")
	startDate := c.Query("start_date")
	endDate := c.Query("end_date")

	if startDate != "" && endDate != "" {
		// ç¯„å??¥è©¢
		start, _ := time.Parse("2006-01-02", startDate)
		end, _ := time.Parse("2006-01-02", endDate)
		holidays, err := h.service.GetHolidaysByRange(start, end, operator)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}
		c.JSON(http.StatusOK, gin.H{"data": holidays})
	} else if date != "" {
		// ?®æ—¥?¥è©¢
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

// CreateHoliday ?µå»º?‡æ—¥
// @Summary ?µå»º?‡æ—¥
// @Description ?µå»º?°ç??‡æ—¥è¨˜é?
// @Tags holidays
// @Accept json
// @Produce json
// @Param holiday body domain.HolidayOperator true "?‡æ—¥è³‡è?"
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

// UpdateHoliday ?´æ–°?‡æ—¥
// @Summary ?´æ–°?‡æ—¥
// @Description ?´æ–°?‡æ—¥è¨˜é?
// @Tags holidays
// @Accept json
// @Produce json
// @Param id path int true "?‡æ—¥ID"
// @Param holiday body domain.HolidayOperator true "?‡æ—¥è³‡è?"
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

// DeleteHoliday ?ªé™¤?‡æ—¥
// @Summary ?ªé™¤?‡æ—¥
// @Description è»Ÿåˆª?¤å??¥è???
// @Tags holidays
// @Produce json
// @Param id path int true "?‡æ—¥ID"
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

// CustomHolidayHandler ?ªè?ç¾©å??¥è??†å™¨
type CustomHolidayHandler struct {
	service *service.CustomHolidayService
}

// NewCustomHolidayHandler ?µå»º?ªè?ç¾©å??¥è??†å™¨
func NewCustomHolidayHandler(svc *service.CustomHolidayService) *CustomHolidayHandler {
	return &CustomHolidayHandler{service: svc}
}

// GetCustomHolidays ?¥è©¢?ªè?ç¾©å???
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

// CreateCustomHoliday ?µå»º?ªè?ç¾©å???
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

// UpdateCustomHoliday ?´æ–°?ªè?ç¾©å???
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

// DeleteCustomHoliday ?ªé™¤?ªè?ç¾©å???
func (h *CustomHolidayHandler) DeleteCustomHoliday(c *gin.Context) {
	id := c.Param("id")
	if err := h.service.DeleteCustomHoliday(id); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	c.Status(http.StatusNoContent)
}

// DisasterHolidayHandler å¤©ç½?‡æ—¥?•ç???
type DisasterHolidayHandler struct {
	service *service.DisasterHolidayService
}

// NewDisasterHolidayHandler ?µå»ºå¤©ç½?‡æ—¥?•ç???
func NewDisasterHolidayHandler(svc *service.DisasterHolidayService) *DisasterHolidayHandler {
	return &DisasterHolidayHandler{service: svc}
}

// GetDisasterHolidays ?¥è©¢å¤©ç½?‡æ—¥
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

// CreateDisasterHoliday ?µå»ºå¤©ç½?‡æ—¥
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

// Health ?¥åº·æª¢æŸ¥
func Health(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{"status": "ok"})
}
