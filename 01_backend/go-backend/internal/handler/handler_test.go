package handler

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
)

func TestHealth(t *testing.T) {
	// ?µå»ºæ¸¬è©¦ä¸Šä???
	w := httptest.NewRecorder()
	c, _ := gin.CreateTestContext(w)

	// èª¿ç”¨ Health ?•ç???
	Health(c)

	// é©—è??€?‹ç¢¼
	assert.Equal(t, http.StatusOK, w.Code)
	assert.Contains(t, w.Body.String(), "ok")
}
