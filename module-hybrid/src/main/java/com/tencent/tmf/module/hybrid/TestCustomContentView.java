package com.tencent.tmf.module.hybrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.tencent.smtt.sdk.WebView;
import com.tencent.tmf.hybrid.ui.IH5AppLoadingViewProvider;
import com.tencent.tmf.hybrid.ui.IH5ContentViewProvider;
import com.tencent.tmf.hybrid.ui.IH5ProgressBarProvider;


public class TestCustomContentView implements IH5ContentViewProvider {

    private View mRoot;
    private View mWebView;

    public TestCustomContentView(Context context) {
        mRoot = LayoutInflater.from(context).inflate(R.layout.layout_test_web_content, null, false);
        WebView webView = new WebView(context);
        FrameLayout frameLayout = mRoot.findViewById(R.id.custom_webview_container);
        frameLayout.addView(webView,
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.mWebView = webView;
    }

    @Override
    public IH5ProgressBarProvider getH5ProgressView() {
        return null;
    }

    @Override
    public void setProviderIcon(Bitmap icon) {

    }

    @Override
    public void setProviderName(String providerName) {

    }

    @Override
    public void setBackground(int resId) {

    }

    @Override
    public void setBackground(Bitmap bitmap) {

    }

    @Override
    public View getWebViewParent() {
        return mRoot;
    }

    @Override
    public View getWebView() {
        return mWebView;
    }

    @Override
    public IH5AppLoadingViewProvider getAppLoadingView() {
        return null;
    }
}
