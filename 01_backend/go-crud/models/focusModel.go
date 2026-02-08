package models

import (
	"github.com/zeromicro/go-zero/core/stores/cache"
	"github.com/zeromicro/go-zero/core/stores/sqlx"
)

var _ FocusModel = (*customFocusModel)(nil)

type (
	// FocusModel is an interface to be customized, add more methods here,
	// and implement the added methods in customFocusModel.
	FocusModel interface {
		focusModel
	}

	customFocusModel struct {
		*defaultFocusModel
	}
)

// NewFocusModel returns a model for the database table.
func NewFocusModel(conn sqlx.SqlConn, c cache.CacheConf, opts ...cache.Option) FocusModel {
	return &customFocusModel{
		defaultFocusModel: newFocusModel(conn, c, opts...),
	}
}


