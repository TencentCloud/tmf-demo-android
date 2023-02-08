package com.tencent.tmf.module.offline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.log.L;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.weboffline.api.OfflineManager;
import com.tencent.tmf.weboffline.api.SimpleCallback;
import com.tencent.tmf.weboffline.api.entitiy.TMFWebResourceResponse;
import com.tencent.tmf.webview.api.ITMFWeb;
import com.tencent.tmf.webview.api.TMFWeb;
import com.tencent.tmf.webview.x5.webclient.DefaultTMFX5WebChromeClient;
import com.tencent.tmf.webview.x5.webclient.DefaultTMFX5WebViewClient;
import com.tencent.tmf.webview.x5.webview.DefaultTMFX5WebView;

@Destination(
        url = "portal://com.tencent.tmf.module.offline/show-offline-activity",
        launcher = Launcher.ACTIVITY,
        description = "离线包页面展示"
)
public class ShowOfflineActivity extends TopBarActivity {

    private static String TAG = "DemoActivity";
    private OfflineManager mOfflineManager;

    private ITMFWeb mWebContainer;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            mTopBar.setTitle(title);
        }

        mOfflineManager = new OfflineManager(getApplicationContext());

        // init webview
        WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setLoadWithOverviewMode(true);

        mWebContainer = TMFWeb.withX5(this)
                .setWebView(new DefaultTMFX5WebView(webView))//必须设置
                .setWebViewClient(new TestWebViewClient())//必须设置，须是DefaultTMFX5WebViewClient的子类
                .setWebChromeClient(new DefaultTMFX5WebChromeClient())//必须设置，须是DefaultTMFX5WebChromeClient的子类
                .build();
        mWebContainer.addJsApi(new JsApiOpenBrowser(this));
        // 从Web容器中获取WebView视图并添加到容器中
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ((RelativeLayout) mContentView).addView(webView, params);

        loadOfflineUrl();
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_weboffline_demo, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebContainer != null) {
            mWebContainer.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mWebContainer != null) {
            mWebContainer.onStop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebContainer != null) {
            mWebContainer.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebContainer != null) {
            mWebContainer.onDestroy();
        }
    }

    private void loadOfflineUrl() {
        final String url = getIntent().getStringExtra("url");
        mOfflineManager.loadUrlAysn(url, new SimpleCallback<String>() {
            @Override
            public void callback(String transedUrl) {
                // 从Web容器中获取WebView视图
                final WebView webView = (WebView) mWebContainer.getWebViewHolder().getWebView();
                if (webView != null) {
                    webView.loadUrl(transedUrl);
                }
            }
        });
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    // Web视图
    private class TestWebViewClient extends DefaultTMFX5WebViewClient {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            long start = System.currentTimeMillis();
            TMFWebResourceResponse tmfWebResourceResponse = mOfflineManager.shouldInterceptRequest(url);
            long cost = System.currentTimeMillis() - start;

            if (tmfWebResourceResponse != null && tmfWebResourceResponse.getResourceResponse() != null) {
                ToastUtil.showToast(getStringById(R.string.offline_activity_check_update_tip_17) + url);
                L.d("intercept and use local ，cost(ms): {}, {}", cost, url);
                android.webkit.WebResourceResponse response = tmfWebResourceResponse.getResourceResponse();
                WebResourceResponse x5Response = new WebResourceResponse();
                x5Response.setMimeType(response.getMimeType());
                x5Response.setEncoding(response.getEncoding());
                x5Response.setData(response.getData());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    x5Response.setResponseHeaders(response.getResponseHeaders());
                    x5Response.setStatusCodeAndReasonPhrase(response.getStatusCode(), response.getReasonPhrase());
                }
                return x5Response;
            } else {
                L.d("no local resource，cost(ms): " + cost + " " + url);
            }

            return super.shouldInterceptRequest(view, url);
        }
    }
}
