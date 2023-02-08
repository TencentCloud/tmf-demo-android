package com.tencent.tmf.h5container.jsapi.jsapi;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.impl.jsapi.showLoading;

public class JsApiShowLoading extends showLoading {

    InnerParam param = null;

    public QMUITipDialog mQMUITipDialog;

    @Override
    public String method() {
        return "showLoading";
    }

    @Override
    public void handle(BaseTMFWeb webView, JsCallParam jsCallParam) {
        param = JsCallParam.fromJson(jsCallParam.paramStr, InnerParam.class);
        mQMUITipDialog = new QMUITipDialog.Builder(webView.getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(param.detail)
                .create();
        mQMUITipDialog.show();
        jsCallParam.mCallback.callback(webView, null);
    }

    private class InnerParam {

        public String title;
        public String detail;
    }
}
