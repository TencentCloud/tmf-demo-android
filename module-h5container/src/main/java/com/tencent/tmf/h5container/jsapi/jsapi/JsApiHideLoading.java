package com.tencent.tmf.h5container.jsapi.jsapi;

import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.impl.jsapi.hideLoading;
import com.tencent.tmf.webview.impl.jsapi.showLoading;

public class JsApiHideLoading extends hideLoading {

    @Override
    public String method() {
        return "hideLoading";
    }

    @Override
    public void handle(BaseTMFWeb webView, JsCallParam jsCallParam) {

        try {
            JsApiShowLoading loading = (JsApiShowLoading) webView.findJsApi(showLoading.class.getSimpleName());
            if (loading.mQMUITipDialog != null && loading.mQMUITipDialog.isShowing()) {
                loading.mQMUITipDialog.dismiss();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        jsCallParam.mCallback.callback(webView, null);
    }
}
