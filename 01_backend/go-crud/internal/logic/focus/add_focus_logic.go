// Code scaffolded by goctl. Safe to edit.
// goctl 1.9.2

package focus

import (
	"context"

	"go-crud/internal/svc"
	"go-crud/internal/types"

	"github.com/zeromicro/go-zero/core/logx"
)

type AddFocusLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewAddFocusLogic(ctx context.Context, svcCtx *svc.ServiceContext) *AddFocusLogic {
	return &AddFocusLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *AddFocusLogic) AddFocus(req *types.AddFocusRequest) (resp *types.FocusResp, err error) {
	// todo: add your logic here and delete this line

	return
}


