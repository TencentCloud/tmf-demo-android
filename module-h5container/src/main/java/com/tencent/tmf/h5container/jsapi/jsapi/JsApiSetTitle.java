package com.tencent.tmf.h5container.jsapi.jsapi;

import android.graphics.Color;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.tencent.tmf.webview.api.Constants;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.api.utils.EmptyUtils;


public class JsApiSetTitle extends JsApi {

    public InnerParam param;
    public QMUITopBarLayout mQMUITopBar;

    public JsApiSetTitle(QMUITopBarLayout topBar) {
        mQMUITopBar = topBar;
    }

    @Override
    public String method() {
        return "setTitle";
    }

    @Override
    public void handle(BaseTMFWeb webView, JsCallParam jsCallParam) {
        param = JsCallParam.fromJson(jsCallParam.paramStr, InnerParam.class);
        if (EmptyUtils.isEmpty(param)) {
            //oo 解析参数异常
            jsCallParam.mCallback.errMsg = Constants.ERR_MSG_ERR_PARAMS;
            jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
        } else {
            try {
                mQMUITopBar.setTitle(param.title)
                        .setTextColor(Color.parseColor(param.titleColor));
            } catch (Throwable e) {
                jsCallParam.mCallback.errMsg = Constants.ERR_MSG_ERR_PARAMS;
                jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
            }
        }

        jsCallParam.mCallback.callback(webView, null);
    }

    public class InnerParam {
        public String title;
        public String titleColor;
    }
}
