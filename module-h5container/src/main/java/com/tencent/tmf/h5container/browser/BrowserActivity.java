package com.tencent.tmf.h5container.browser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.portal.annotations.Parameter;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.h5container.R;
import com.tencent.tmf.webview.api.ITMFWeb;
import com.tencent.tmf.webview.api.TMFWeb;
import com.tencent.tmf.webview.x5.webclient.DefaultTMFX5WebChromeClient;
import com.tencent.tmf.webview.x5.webclient.DefaultTMFX5WebViewClient;
import com.tencent.tmf.webview.x5.webview.DefaultTMFX5WebView;

@Destination(
        url = "portal://com.tencent.tmf.module.h5/browser-activity",
        launcher = Launcher.ACTIVITY,
        description = "http页面展示",
        parameters = {
                @Parameter(name = "url", optional = false, type = String.class, description = "url"),
                @Parameter(name = "title", optional = true, type = String.class, description = "标题"),
                @Parameter(name = "showTitle", optional = true, type = Boolean.class, description = "是否显示title"),
        }
)
public class BrowserActivity extends TopBarActivity {

    private ITMFWeb mWebContainer;
    private ProgressBar mPageLoadingProgressBar = null;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String title = intent.getStringExtra(ModuleH5containerConst.P_TITLE);
        if (!TextUtils.isEmpty(title)) {
            mTopBar.setTitle(title);
        }

        initProgressBar();

        WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setLoadWithOverviewMode(true);
        WebView.setWebContentsDebuggingEnabled(true);

        mWebContainer = TMFWeb.withX5(this)
                .setWebView(new DefaultTMFX5WebView(webView))//必须设置
                .setWebViewClient(new TestDefaultTMFX5WebViewClient())//必须设置，须是DefaultTMFX5WebViewClient的子类
                .setWebChromeClient(new TestDefaultTMFX5WebChromeClient())//必须设置，须是DefaultTMFX5WebChromeClient的子类
                .build();
        // 从Web容器中获取WebView视图并添加到容器中
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ViewGroup container = findViewById(R.id.container);
        container.addView(webView, params);

        String url = intent.getStringExtra(ModuleH5containerConst.P_URL);
        mWebContainer.getWebViewHolder().loadUrl(url);
    }

    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.color_progressbar));
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_browser, null);
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

    public class TestDefaultTMFX5WebViewClient extends DefaultTMFX5WebViewClient {

        public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
            if (mPageLoadingProgressBar != null) {
                mPageLoadingProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mPageLoadingProgressBar != null) {
                mPageLoadingProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public class TestDefaultTMFX5WebChromeClient extends DefaultTMFX5WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mPageLoadingProgressBar.setProgress(newProgress);
            if (mPageLoadingProgressBar != null && newProgress >= 100) {
                mPageLoadingProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
