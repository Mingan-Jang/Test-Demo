// Code scaffolded by goctl. Safe to edit.
// goctl 1.9.2

package focus

import (
	"context"

	"go-hello-zero/internal/svc"
	"go-hello-zero/internal/types"

	"github.com/zeromicro/go-zero/core/logx"
)

type UpdateFocusLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewUpdateFocusLogic(ctx context.Context, svcCtx *svc.ServiceContext) *UpdateFocusLogic {
	return &UpdateFocusLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *UpdateFocusLogic) UpdateFocus(req *types.UpdateFocusRequest) (resp *types.FocusResp, err error) {
	// todo: add your logic here and delete this line

	return
}
