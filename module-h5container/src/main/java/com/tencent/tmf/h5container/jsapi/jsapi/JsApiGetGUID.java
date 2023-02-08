package com.tencent.tmf.h5container.jsapi.jsapi;

import android.text.TextUtils;
import android.util.Log;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.shark.api.IGuidCallback;
import com.tencent.tmf.shark.api.IShark;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.api.utils.GsonHelper;

public class JsApiGetGUID extends JsApi {

    @Override
    public String method() {
        return "getGUID";
    }

    @Override
    public void handle(final BaseTMFWeb webView, final JsCallParam jsCallParam) {

        Log.d("xiao1", "getGUID.handle:" + Thread.currentThread().getName());

        final ResultData result = new ResultData();
        try {
            IShark shark = SharkService.getSharkWithInit();
            String guid = shark.getGuid();
            if (!TextUtils.isEmpty(guid)) {
                result.guid = guid;
                jsCallParam.mCallback.callback(webView, GsonHelper.getInstance().toJson(result));
                Log.i("getGUID", "callback use syn result: " + guid);
            } else {
                shark.getGuidAsyn(new IGuidCallback() {
                    @Override
                    public void onCallback(int retCode, String guid) {
                        if (retCode == 0 && !TextUtils.isEmpty(guid)) {
                            result.guid = guid;
                        } else {
                            jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
                            jsCallParam.mCallback.errMsg = "getGuidAsyn, retCode=" + retCode;
                        }
                        jsCallParam.mCallback.callback(webView, GsonHelper.getInstance().toJson(result));
                        Log.i("getGUID", "callback use asyn result: " + guid);
                    }
                });
            }

        } catch (Throwable e) {
            jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
            jsCallParam.mCallback.errMsg = e.getMessage();
            jsCallParam.mCallback.callback(webView, GsonHelper.getInstance().toJson(result));
            Log.w("getGUID", "callback with exception info: " + e.getMessage());
        }
    }

    public class ResultData {

        public String guid;
    }
}
