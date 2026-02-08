// Code scaffolded by goctl. Safe to edit.
// goctl 1.9.2

package focus

import (
	"go-crud/internal/logic/focus"
	"go-crud/internal/svc"
	"go-crud/internal/types"
	"net/http"

	"github.com/zeromicro/go-zero/rest/httpx"
)

func AddFocusHandler(svcCtx *svc.ServiceContext) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var req types.AddFocusRequest
		if err := httpx.Parse(r, &req); err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
			return
		}

		l := focus.NewAddFocusLogic(r.Context(), svcCtx)
		resp, err := l.AddFocus(&req)
		if err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
		} else {
			httpx.OkJsonCtx(r.Context(), w, resp)
		}
	}
}


