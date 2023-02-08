package com.tencent.tmf.h5container.jsapi.jsapi;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.share.ShareManager;
import com.tencent.tmf.common.share.ShareParam;
import com.tencent.tmf.h5container.R;
import com.tencent.tmf.share.api.ITMFShareListener;
import com.tencent.tmf.share.api.TMFShareContants;
import com.tencent.tmf.share.api.TMFShareService;
import com.tencent.tmf.webview.api.Constants;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.CallbackH5;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.api.utils.EmptyUtils;
import com.tencent.tmf.webview.impl.jsapi.shareApp;

/**
 * H5分享的实现
 */
public class JsApiShareApp extends shareApp implements ITMFShareListener {

    private static final String TAG = "H5_ShareApp";
    private static final String APPKEY_WX_TIMELINE = "wx_timeline";
    private static final String APPKEY_WX_FRIEND = "wx_message";
    private static final String APPKEY_CWX_FRIEND = "cwx_message";
    private static final String APPKEY_QQ_FRIEND = "qq_message";
    private static final String APPKEY_WEIBO = "weibo_message";
    private static final String APPKEY_ALIPAY_TIMELINE = "ali_timeline";
    private static final String APPKEY_ALIPAY_FRIEND = "ali_message";
    private static final String APPKEY_DING_DING = "dding_message";

    private InnerParam mParam;
    private Activity mActivity;
    private CallbackH5 mCallback;
    private BaseTMFWeb mWebView;

    public JsApiShareApp(Activity activity) {
        mActivity = activity;
    }

    @Override
    public String method() {
        return "shareApp";
    }

    @Override
    public void handle(BaseTMFWeb baseTMFWeb, JsCallParam jsCallParam) {
        mWebView = baseTMFWeb;
        mCallback = jsCallParam.mCallback;
        mParam = JsCallParam.fromJson(jsCallParam.paramStr, InnerParam.class);
        if (EmptyUtils.isEmpty(mParam)) {
            callback(ErrorCode.ERROR_BUSINESS, Constants.ERR_MSG_ERR_PARAMS);
            return;
        }

        ShareParam shareParam = new ShareParam();
        shareParam.appKeys = mParam.appKeys;
        shareParam.title = mParam.title;
        shareParam.desc = mParam.desc;
        shareParam.link = mParam.link;
        shareParam.imgUrl = mParam.imgUrl;

        if (TextUtils.equals(mParam.appKeys, APPKEY_QQ_FRIEND)) {
            ShareManager.shareToQQ(mActivity, this, shareParam);
        } else if (TextUtils.equals(mParam.appKeys, APPKEY_WX_FRIEND)) {
            ShareManager.shareToWx(mActivity, this, false, shareParam);
        } else if (TextUtils.equals(mParam.appKeys, APPKEY_WX_TIMELINE)) {
            ShareManager.shareToWx(mActivity, this, true, shareParam);
        } else if (TextUtils.equals(mParam.appKeys, APPKEY_CWX_FRIEND)) {
            ShareManager.shareToCwx(mActivity, this, true, shareParam);
        } else if (TextUtils.equals(mParam.appKeys, APPKEY_WEIBO)) {
            ShareManager.shareToWeibo(mActivity, this, shareParam);
        } else if (TextUtils.equals(mParam.appKeys, APPKEY_ALIPAY_TIMELINE)) {
            ShareManager.shareToAlipay(mActivity, this, true, shareParam);
        } else if (TextUtils.equals(mParam.appKeys, APPKEY_ALIPAY_FRIEND)) {
            ShareManager.shareToAlipay(mActivity, this, false, shareParam);
        } else if (TextUtils.equals(mParam.appKeys, APPKEY_DING_DING)) {
            ShareManager.shareToDing(mActivity, this, shareParam);
        } else {
            callback(ErrorCode.ERROR_BUSINESS, Constants.ERR_MSG_ERR_PARAMS);
        }
    }

    @Override
    public void onSuccess(int shareType, Object object) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG,
                    "share success, type: " + shareType + " callback: " + (object != null ? object.toString() : "object is null"));
        }
        String text = mActivity.getResources().getString(R.string.js_api_share_success);
        Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
        mCallback.callback(mWebView, null);
    }

    @Override
    public void onError(int shareType, int errCode, String errMessage) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "share failed, type: " + shareType + " errCode: " + errCode + " errMsg：" + errMessage);
        }
        switch (errCode) {
            case TMFShareContants.ErrorCode.APP_NO_INSTALL:
                String text = mActivity.getResources().getString(R.string.js_api_share_app_not_install);
                Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
                callback(ErrorCode.ERROR_BUSINESS, "shareApp not installed");
                break;
            case TMFShareContants.ErrorCode.USER_CANCEL:
                String text1 = mActivity.getResources().getString(R.string.js_api_share_app_user_cancel);
                Toast.makeText(mActivity, text1, Toast.LENGTH_SHORT).show();
                callback(ErrorCode.ERROR_CANCEL, "user cancel");
                break;
            case TMFShareContants.ErrorCode.AUTH_FAIL:
                String text2 = mActivity.getResources().getString(R.string.js_api_share_app_verify_failed);
                Toast.makeText(mActivity, text2, Toast.LENGTH_SHORT).show();
                callback(ErrorCode.ERROR_FAIL, "auth fail");
                break;
            case TMFShareContants.ErrorCode.UNVALID_PARAM:
                String text3 = mActivity.getResources().getString(R.string.js_api_share_app_bad_params);
                Toast.makeText(mActivity, text3, Toast.LENGTH_SHORT).show();
                callback(ErrorCode.ERROR_BUSINESS, Constants.ERR_MSG_ERR_PARAMS);
                break;
            case TMFShareContants.ErrorCode.INTERFACE_NOT_SUPPORT:

                String text4 = mActivity.getResources().getString(R.string.js_api_share_app_not_support);
                Toast.makeText(mActivity, text4, Toast.LENGTH_SHORT).show();
                callback(ErrorCode.ERROR_BUSINESS, "share api not supported");
                break;
            case TMFShareContants.ErrorCode.SEND_REQ_FAIL:
                String text5 = mActivity.getResources().getString(R.string.js_api_share_app_not_support);

                Toast.makeText(mActivity, text5, Toast.LENGTH_SHORT).show();
                callback(ErrorCode.ERROR_BUSINESS, "share request error");
                break;
            default:
                String text6 = mActivity.getResources().getString(R.string.js_api_share_unknown_error);

                Toast.makeText(mActivity, text6, Toast.LENGTH_SHORT).show();
                callback(ErrorCode.ERROR_BUSINESS, "unknow error");
                break;
        }
    }


    public void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "handleActivityResult, requestCode = " + requestCode + " resultCode = " + resultCode);
        }
        TMFShareService.getInstance().handleQQIntent(requestCode, resultCode, data);
        TMFShareService.getInstance().handleWeiboIntent(data);
    }

    public void release() {
        TMFShareService.getInstance().release();
    }

    private void callback(int errCode, String errMessage) {
        if (mCallback != null && mWebView != null) {
            mCallback.errMsg = errMessage;
            mCallback.ret = errCode;
            mCallback.callback(mWebView, null);
        }
    }
}
