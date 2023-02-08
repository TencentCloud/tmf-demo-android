package com.tencent.tmf.module.offline;

//import com.tencent.tmf.demo.location.AMapH5LocationActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;
import com.tencent.tmf.common.gen.ModuleLocationConst;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.webview.api.Constants;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.api.utils.EmptyUtils;
import com.tencent.tmf.webview.impl.jsapi.openLocation;

public class JsApiOpenBrowser extends openLocation {
    InnerParam param;
    Activity activity;

    public JsApiOpenBrowser(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String method() {
        return "openBrowser";
    }

    @Override
    public void handle(BaseTMFWeb webView, JsCallParam jsCallParam) {
        param = JsCallParam.fromJson(jsCallParam.paramStr, InnerParam.class);
        Uri uri = Uri.parse(param.url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);

        jsCallParam.mCallback.callback(webView, null);
    }

    protected class InnerParam {
        @SerializedName("url")
        public String url;
    }
}
