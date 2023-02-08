package com.tencent.tmf.h5container.jsapi.jsapi;

//import com.tencent.tmf.demo.location.AMapH5LocationActivity;

import android.app.Activity;

import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.common.gen.ModuleLocationConst;
import com.tencent.tmf.webview.api.Constants;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.api.utils.EmptyUtils;
import com.tencent.tmf.webview.impl.jsapi.openLocation;

public class JsApiOpenLocation extends openLocation {
    InnerParam param;
    Activity activity;

    public JsApiOpenLocation(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String method() {
        return "openLocation";
    }

    @Override
    public void handle(BaseTMFWeb webView, JsCallParam jsCallParam) {
        param = JsCallParam.fromJson(jsCallParam.paramStr, InnerParam.class);
        if (EmptyUtils.isEmpty(param)) {
            //oo 解析参数异常
            jsCallParam.mCallback.errMsg = Constants.ERR_MSG_ERR_PARAMS;
            jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
        } else {
            Portal.from(activity)
                    .url(ModuleLocationConst.U_A_MAP_H_5_LOCATION_ACTIVITY)
                    .launch();
        }
        jsCallParam.mCallback.callback(webView, null);
    }

    protected class InnerParam {
        public double latitude;   // double，必选，纬度，范围为 90 ~ -90
        public double longitude;  // double，必选，经度，范围为 180 ~ -180
        public String name;        // string，可选，位置名
        public String address;     // string，可选，地址详情说明
        public double scale;      // double，可选，地图缩放级别，默认为 1.0
    }
}
