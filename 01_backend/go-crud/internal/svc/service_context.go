// Code scaffolded by goctl. Safe to edit.
// goctl 1.9.2

package svc

import (
	"go-crud/internal/config"
	"go-crud/models"

	"github.com/zeromicro/go-zero/core/stores/sqlx"
)

type ServiceContext struct {
	Config     config.Config
	FocusModel models.FocusModel
}

func NewServiceContext(c config.Config) *ServiceContext {
	// ????SQL 餈
	conn := sqlx.NewSqlConn("mysql", c.Mysql.DataSoruce)

	// ????FocusModel嚗???CRUD ?寞?嚗?雿輻蝻?嚗?
	focusModel := models.NewFocusModel(conn, nil)

	return &ServiceContext{
		Config:     c,
		FocusModel: focusModel,
	}
}


