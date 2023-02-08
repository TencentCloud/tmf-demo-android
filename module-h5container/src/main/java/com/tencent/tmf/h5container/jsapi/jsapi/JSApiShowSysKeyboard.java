package com.tencent.tmf.h5container.jsapi.jsapi;

import android.util.Log;

import com.tencent.tmf.common.utils.KeyboardUtil;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.param.JsCallParam;

/**
 * 显示系统键盘
 */
public class JSApiShowSysKeyboard extends JsApi {
    @Override
    public String method() {
        return JSApiShowSysKeyboard.class.getSimpleName();
    }

    @Override
    public void handle(final BaseTMFWeb baseTMFWeb, JsCallParam jsCallParam) {
        Log.d("xiao1", "JSApiShowSysKeyboard_handle");

        KeyboardUtil.showSysKeyboard(baseTMFWeb.getContext(), baseTMFWeb.getWebViewHolder());
        jsCallParam.mCallback.callback(baseTMFWeb, null);
    }


}
