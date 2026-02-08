// Code scaffolded by goctl. Safe to edit.
// goctl 1.9.2

package svc

import (
	"go-zero/internal/config"
	"go-zero/models"

	"github.com/zeromicro/go-zero/core/stores/sqlx"
)

type ServiceContext struct {
	Config     config.Config
	FocusModel models.FocusModel
}

func NewServiceContext(c config.Config) *ServiceContext {
	// 初始化 SQL 连接
	conn := sqlx.NewSqlConn("mysql", c.Mysql.DataSoruce)

	// 初始化 FocusModel（包含 CRUD 方法，不使用缓存）
	focusModel := models.NewFocusModel(conn, nil)

	return &ServiceContext{
		Config:     c,
		FocusModel: focusModel,
	}
}
