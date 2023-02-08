package com.tencent.tmf.h5container.jsapi.jsapi;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.tmf.webview.api.Constants;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.api.utils.EmptyUtils;

public class JsApiShowToast extends JsApi {

    InnerParam param = null;

    @Override
    public String method() {
        return "showToast";
    }

    @Override
    public void handle(BaseTMFWeb webView, JsCallParam jsCallParam) {
        //解析参数
        param = JsCallParam.fromJson(jsCallParam.paramStr, InnerParam.class);

        //参数校验
        if (EmptyUtils.isEmpty(param)) {
            //oo 解析参数异常
            jsCallParam.mCallback.errMsg = Constants.ERR_MSG_ERR_PARAMS;
            jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
        } else {
            //业务处理
            param.duration = (param.duration < 2.0) ? 2 : param.duration;

            final QMUITipDialog tipDialog = new QMUITipDialog.Builder(webView.getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord(param.detail)
                    .create();
            tipDialog.show();
            webView.getWebViewHolder().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tipDialog.dismiss();
                }
            }, (long) param.duration * 1000);
        }

        //回调H5页面
        jsCallParam.mCallback.callback(webView, null);
    }

    private class InnerParam {

        public String title;
        public String detail;
        public double duration;
    }
}
