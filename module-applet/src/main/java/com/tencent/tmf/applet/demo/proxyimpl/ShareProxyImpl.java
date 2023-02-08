package com.tencent.tmf.applet.demo.proxyimpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tmfmini.sdk.annotation.ProxyService;
import com.tencent.tmfmini.sdk.launcher.AppLoaderFactory;
import com.tencent.tmfmini.sdk.launcher.core.IMiniAppContext;
import com.tencent.tmfmini.sdk.launcher.core.proxy.BaseShareProxy;
import com.tencent.tmfmini.sdk.launcher.core.proxy.ShareProxy;
import com.tencent.tmfmini.sdk.launcher.model.ShareData;
import com.tencent.tmfmini.sdk.launcher.ui.MoreItem;
import com.tencent.tmfmini.sdk.ui.MorePanel;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

@ProxyService(proxy = ShareProxy.class)
public class ShareProxyImpl extends BaseShareProxy {

    private static final String TAG = "ShareProxyImpl";

    private static final String QQ_APP_ID = "1108836394";

    // this init is so slow...
    private static volatile Tencent tencent;

    private static Tencent getTencent() {
        if (tencent == null) {
            synchronized (ShareProxyImpl.class) {
                if (tencent == null) {
                    tencent = Tencent
                            .createInstance(QQ_APP_ID, AppLoaderFactory.g().getContext());
                }
            }
        }
        return tencent;
    }

    // 扩展按钮的ID需要设置为[100, 200]这个区间中的值，否则，添加无效。
    public static final int OTHER_MORE_ITEM_1 = 101;
    public static final int OTHER_MORE_ITEM_2 = 102;
    public static final int OTHER_MORE_ITEM_INVALID = 201;

    private IUiListener mQQShareUiListener;

    /**
     * 分享
     *
     * @param shareData 分享数据
     */
    @Override
    public void share(Activity activity, ShareData shareData) {
        switch (shareData.shareTarget) {
            case ShareData.ShareTarget.QQ:
                Toast.makeText(activity, "QQ", Toast.LENGTH_SHORT).show();
                shareToQQ(activity, shareData);
                return;
            case ShareData.ShareTarget.QZONE:
                Toast.makeText(activity, "QZONE", Toast.LENGTH_SHORT).show();
                shareToQZone(activity, shareData);
                return;
            case ShareData.ShareTarget.WECHAT_FRIEND:
                Toast.makeText(activity, "WECHAT_FRIEND", Toast.LENGTH_SHORT).show();
                return;
            case ShareData.ShareTarget.WECHAT_MOMENTS:
                Toast.makeText(activity, "WECHAT_MOMENTS", Toast.LENGTH_SHORT).show();
                return;
            default:
                break;
        }

        if (MoreItem.isValidExtendedItemId(shareData.shareTarget)) {
            shareToOther(activity, shareData);
        }
    }

    /**
     * 启动第三方分享结果的返回
     */
    @Override
    public void onShareActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mQQShareUiListener);
    }

    /**
     * 调用互联SDK，分享到QQ
     */
    public void shareToQQ(Activity activity, ShareData shareData) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareData.title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareData.summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, TextUtils.isEmpty(shareData.targetUrl) ? "https://www.qq.com" : shareData.targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareData.sharePicPath);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "MiniSDKDemo");

        mQQShareUiListener = new QQShareListener(activity, shareData);
        getTencent().shareToQQ(activity, params, null);
    }

    /**
     * 调用互联SDK，分享到QQ空间
     */
    public void shareToQZone(Activity activity, ShareData shareData) {
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareData.title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareData.summary);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, TextUtils.isEmpty(shareData.targetUrl) ? "https://www.qq.com" : shareData.targetUrl);//必填
        ArrayList imageUrlList = new ArrayList();
        imageUrlList.add(shareData.sharePicPath);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrlList);

        mQQShareUiListener = new QQShareListener(activity, shareData);
        getTencent().shareToQzone(activity, params, mQQShareUiListener);
    }

    /**
     * 调用第三方分享
     */
    public void shareToOther(Activity activity, ShareData shareData) {
        switch (shareData.shareItemId) {
            case OTHER_MORE_ITEM_1:
                if (shareData.shareInMiniProcess) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "custom menu click", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case OTHER_MORE_ITEM_2:
            default:
                Toast.makeText(activity, "custom share", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class QQShareListener implements IUiListener {

        private Context mContext;
        private ShareData mShareData;

        QQShareListener(Context context, ShareData shareData) {
            mContext = context;
            mShareData = shareData;
        }

        @Override
        public void onComplete(Object o) {
//            Toast.makeText(mContext, "share Complete", Toast.LENGTH_SHORT).show();
            mShareData.notifyShareResult(mContext, ShareData.ShareResult.SUCCESS);
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(mContext, "share Error: " + uiError.errorCode + uiError.errorMessage, Toast.LENGTH_SHORT).show();
            mShareData.notifyShareResult(mContext, ShareData.ShareResult.FAIL);
        }

        @Override
        public void onCancel() {
//            Toast.makeText(mContext, "share Cancel", Toast.LENGTH_SHORT).show();
            mShareData.notifyShareResult(mContext, ShareData.ShareResult.CANCEL);
        }

        @Override
        public void onWarning(int i) {

        }
    }

    @Override
    public void showSharePanel(IMiniAppContext miniAppContext) {
        //固定写如下代码即可
        MorePanel.show(miniAppContext);
    }
}
