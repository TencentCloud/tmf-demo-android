package com.tencent.tmf.h5container.jsapi.jsapi;

import android.view.View;
import android.widget.ImageView;
import com.tencent.tmf.webview.api.Constants;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.impl.jsapi.showOptionMenu;

public class JsApiShowOptionMenu extends showOptionMenu {

    public ImageView mMore;

    public JsApiShowOptionMenu(ImageView more) {
        mMore = more;
    }

    @Override
    public String method() {
        return "showOptionMenu";
    }

    @Override
    public void handle(BaseTMFWeb webView, JsCallParam jsCallParam) {
        try {
            mMore.setVisibility(View.VISIBLE);
        } catch (Throwable e) {
            jsCallParam.mCallback.errMsg = Constants.ERR_MSG_ERR_PARAMS;
            jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
        }

        jsCallParam.mCallback.callback(webView, null);
    }
}
