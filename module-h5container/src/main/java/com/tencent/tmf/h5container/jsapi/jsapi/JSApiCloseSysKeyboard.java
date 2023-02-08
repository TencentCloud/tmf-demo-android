package com.tencent.tmf.h5container.jsapi.jsapi;

import com.tencent.tmf.common.utils.KeyboardUtil;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.param.JsCallParam;

/**
 * 关闭系统键盘
 */
public class JSApiCloseSysKeyboard extends JsApi {
    @Override
    public String method() {
        return JSApiCloseSysKeyboard.class.getSimpleName();
    }

    @Override
    public void handle(BaseTMFWeb baseTMFWeb, JsCallParam jsCallParam) {
        KeyboardUtil.closeSysKeyboard(baseTMFWeb.getContext(), baseTMFWeb.getWebViewHolder());
        jsCallParam.mCallback.callback(baseTMFWeb, null);
    }
}
