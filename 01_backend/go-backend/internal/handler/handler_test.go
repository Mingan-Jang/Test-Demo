package handler

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
)

func TestHealth(t *testing.T) {
	// 創建測試上下文
	w := httptest.NewRecorder()
	c, _ := gin.CreateTestContext(w)

	// 調用 Health 處理器
	Health(c)

	// 驗證狀態碼
	assert.Equal(t, http.StatusOK, w.Code)
	assert.Contains(t, w.Body.String(), "ok")
}
