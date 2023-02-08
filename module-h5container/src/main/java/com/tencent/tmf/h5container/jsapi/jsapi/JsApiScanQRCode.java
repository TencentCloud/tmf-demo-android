package com.tencent.tmf.h5container.jsapi.jsapi;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.tencent.tmf.h5container.R;
import com.tencent.tmf.portal.ActivityResultCallback;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.Response;
import com.tencent.tmf.common.gen.ModuleScanConst;
import com.tencent.tmf.common.log.L;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.CallbackH5;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.api.utils.GsonHelper;
import rx.functions.Action1;

public class JsApiScanQRCode extends JsApi {

    private static final String TAG = "ScanQRCode";
    public static final int REQUEST_SCAN_QRCODE_JS_API = 2334;
    public static final String EXTRA_SCAN_QRCODE_RESULT = "extra_scan_qrcode_result";
    private Activity activity;

    private BaseTMFWeb mBaseTMFWeb;
    private CallbackH5 mCallback;

    public JsApiScanQRCode(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String method() {
        return "scanQRCode";
    }

    @Override
    public void handle(BaseTMFWeb baseTMFWeb, JsCallParam jsCallParam) {
        mBaseTMFWeb = baseTMFWeb;
        mCallback = jsCallParam.mCallback;

        Portal.from(activity)
                .url(ModuleScanConst.U_Q_R_CODE_SCAN_MAIN_ACTIVITY)
                .startActivityWithCallback((resultCode, data) -> {
                    switch (resultCode) {
                        case Activity.RESULT_OK: {
                            if (data != null) {
                                String result = data.getStringExtra(ModuleScanConst.R_SCAN_RESULT);
                                onScanQRCode(result);
                            }
                            break;
                        }
                        case Activity.RESULT_CANCELED: {
                            break;
                        }
                        default:
                            break;
                    }
                })
                .param(ModuleScanConst.P_GET_RESULT, true)
                .launch();
    }

    private void onScanQRCode(String result) {
        L.d("onScanQRCode:" + result);
        if (!TextUtils.isEmpty(result)) {
            ResultData resultData = new ResultData();
            resultData.result = result;
            mCallback.ret = ErrorCode.ERROR_NONE;
            mCallback.callback(mBaseTMFWeb, GsonHelper.getInstance().toJson(resultData));
        } else {
            mCallback.ret = ErrorCode.ERROR_BUSINESS;
            String msg = mBaseTMFWeb.getContext().getResources().getString(R.string.js_api_scan_no_data);
            mCallback.errMsg = msg;
            mCallback.callback(mBaseTMFWeb, null);
        }
    }

    public static class ResultData {

        public String result;
    }

}
