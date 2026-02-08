// Code scaffolded by goctl. Safe to edit.
// goctl 1.9.2

package svc

import (
	"go-zero/internal/config"
	"go-zero/internal/core"
)

type ServiceContext struct {
	Config config.Config
	DB     *core.DB
}

func NewServiceContext(c config.Config) *ServiceContext {
	db, err := core.NewDB(c.Mysql.DataSoruce)
	if err != nil {
		panic(err)
	}

	return &ServiceContext{
		Config: c,
		DB:     db,
	}
}
