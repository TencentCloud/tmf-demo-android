package com.tencent.tmf.h5container.jsapi.jsapi;

import android.text.TextUtils;

import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.webview.api.Constants;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.api.utils.EmptyUtils;
import com.tencent.tmf.webview.impl.jsapi.openContainer;

public class JsApiOpenPage extends openContainer {

    @Override
    public String method() {
        return "openContainer";
    }

    @Override
    public void handle(final BaseTMFWeb webView, final JsCallParam jsCallParam) {
        mOpenParam = JsCallParam.fromJson(jsCallParam.paramStr, openParam.class);
        if (EmptyUtils.isEmpty(mOpenParam) || TextUtils.isEmpty(mOpenParam.url)) {
            //oo 解析参数异常
            jsCallParam.mCallback.errMsg = Constants.ERR_MSG_ERR_PARAMS;
            jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
        } else {
            Portal.from(webView.mActivity)
                    .url(ModuleH5containerConst.U_J_SAPI_ACTIVITY)
                    .param(ModuleH5containerConst.P_URL, mOpenParam.url)
                    .param(ModuleH5containerConst.P_TITLE, mOpenParam.params.defaultTitle)
                    .param(ModuleH5containerConst.P_SHOW_TITLE, mOpenParam.params.showsContainerTitle)
                    .launch();
        }
        Runnable callbackTask = new Runnable() {
            @Override
            public void run() {
                jsCallParam.mCallback.callback(webView, null);
            }
        };
        webView.getWebViewHolder().getHandler().postDelayed(callbackTask, 500);
    }
}
