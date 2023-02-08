package com.tencent.tmf.h5container.jsapi;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.widget.PopupMenu;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.portal.annotations.Parameter;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.demo.qmui.base.BaseFragmentActivity;
import com.tencent.tmf.h5container.R;
import com.tencent.tmf.h5container.doc.CustomDocShowActivity;
import com.tencent.tmf.h5container.jsapi.jsapi.JSApiCloseSysKeyboard;
import com.tencent.tmf.h5container.jsapi.jsapi.JSApiShowSysKeyboard;
import com.tencent.tmf.h5container.jsapi.jsapi.JSSendSharkSafeKeyboard;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiGetGUID;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiHideFinish;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiHideOptionMenu;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiLoadBigImage;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiOpenLocation;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiOpenPage;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiSaveBigImage;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiScanQRCode;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiSetOptionMenu;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiSetTitle;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiShareApp;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiShowFinish;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiShowOptionMenu;
import com.tencent.tmf.h5container.jsapi.jsapi.JsApiShowToast;
import com.tencent.tmf.keyboard.component.keyboard.CustomKeyboard;
import com.tencent.tmf.keyboard.component.keyboard.CustomKeyboardWrapper;
import com.tencent.tmf.webview.api.ITMFWeb;
import com.tencent.tmf.webview.api.TMFWeb;
import com.tencent.tmf.webview.impl.jsapi.closeSafeKeyboard;
import com.tencent.tmf.webview.impl.jsapi.safeKeyboardDoubleCheck;
import com.tencent.tmf.webview.impl.jsapi.safeKeyboardWeakCheck;
import com.tencent.tmf.webview.impl.jsapi.showSafeKeyboard;
import com.tencent.tmf.webview.x5.webclient.DefaultTMFX5WebChromeClient;
import com.tencent.tmf.webview.x5.webclient.DefaultTMFX5WebViewClient;
import com.tencent.tmf.webview.x5.webview.DefaultTMFX5WebView;
import com.tencent.tmf.x5docpreview.api.PreviewDocImpl;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

@Destination(
        url = "portal://com.tencent.tmf.module.h5/jsapi-activity",
        launcher = Launcher.ACTIVITY,
        description = "JSAPI",
        parameters = {
                @Parameter(name = "url", optional = false, type = String.class, description = "url"),
                @Parameter(name = "title", optional = true, type = String.class, description = "标题"),
                @Parameter(name = "showTitle", optional = true, type = Boolean.class, description = "是否显示title"),
        }
)
public class JSapiActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ViewGroup mContainer;
    private WebView mWebView;
    private ITMFWeb mWebContainer;
    private QMUITopBarLayout mTopBar;
    //更多按钮
    private ImageView mMoreImageView;
    private ImageView mBackImageView;
    private View mLineView;
    private ImageView mFinishImageView;
    private PopupMenu mPopupMenu;
    private JsApiSetOptionMenu mJsApiOptionMenu;
    private JsApiShareApp mJsApiShareApp;
    private JsApiLoadBigImage mJsApiLoadBigImage;
    private ProgressBar mPageLoadingProgressBar = null;

    protected int getContextViewId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsapi);

        initUi();
        initJsApi();
        initDoc();
        loadUrl();
    }

    private void initUi() {
        mContainer = findViewById(R.id.container);
        mTopBar = findViewById(R.id.topbar);
        mMoreImageView = findViewById(R.id.iv_more);
        mBackImageView = findViewById(R.id.iv_back);
        mLineView = findViewById(R.id.view_line);
        mFinishImageView = findViewById(R.id.iv_finish);
        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mBackImageView.setOnClickListener(this);
        mFinishImageView.setOnClickListener(this);
        mMoreImageView.setOnClickListener(this);

        //创建右上角菜单栏
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this, mMoreImageView);
            Menu menu = mPopupMenu.getMenu();
            String menu1 = getResources().getString(R.string.js_api_activiity_menu_0);
            String menu2 = getResources().getString(R.string.js_api_activiity_menu_1);
            String menu3 = getResources().getString(R.string.js_api_activiity_menu_2);
            menu.add(Menu.NONE, Menu.FIRST + 0, 0, menu1);
            menu.add(Menu.NONE, Menu.FIRST + 1, 0, menu2);
            menu.add(Menu.NONE, Menu.FIRST + 2, 0, menu3);
            mPopupMenu.setOnMenuItemClickListener(getOnMenuItemClickListener());
        }

        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.color_progressbar));
    }

    private void initJsApi() {
        mWebView = new WebView(this);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setLoadWithOverviewMode(true);

        mWebContainer = TMFWeb.withX5(this)
                .setWebView(new DefaultTMFX5WebView(mWebView))//必须设置
                .setWebViewClient(new TestWebViewClient(mTopBar))
                .setWebChromeClient(new TestDefaultTMFX5WebChromeClient())
                .build();

        //UI相关
        mWebContainer.addJsApi(new JsApiSetTitle(mTopBar));
        mJsApiOptionMenu = new JsApiSetOptionMenu(this);
        mWebContainer.addJsApi(mJsApiOptionMenu);
        mWebContainer.addJsApi(new JsApiShowOptionMenu(mMoreImageView));
        mWebContainer.addJsApi(new JsApiHideOptionMenu(mMoreImageView));
        mWebContainer.addJsApi(new JsApiShowFinish(mFinishImageView));
        mWebContainer.addJsApi(new JsApiHideFinish(mFinishImageView));
        mWebContainer.addJsApi(new JsApiShowToast());
        //    mWebContainer.addJsApi(new JsApiShowLoading());
        //     mWebContainer.addJsApi(new JsApiHideLoading());
        //上下文
        mWebContainer.addJsApi(new JsApiOpenPage());
        //基础信息
        mWebContainer.addJsApi(new JsApiGetGUID());
        //   mWebContainer.addJsApi(new JsApiGetSharedStorage());
        //    mWebContainer.addJsApi(new JsApiSetSharedStorage());
        //    mWebContainer.addJsApi(new JsApiRemoveSharedStorage());
        //网关
        // mWebContainer.addJsApi(new JsApiSendShark());
        //定位
        mWebContainer.addJsApi(new JsApiOpenLocation(this));
        //分享
        mJsApiShareApp = new JsApiShareApp(this);
        mWebContainer.addJsApi(mJsApiShareApp);
        //扫一扫
        mWebContainer.addJsApi(new JsApiScanQRCode(this));
        //加载图片
        mJsApiLoadBigImage = new JsApiLoadBigImage();
        mWebContainer.addJsApi(mJsApiLoadBigImage);
        mWebContainer.addJsApi(new JsApiSaveBigImage(this));
        //安全键盘
        mWebContainer.addJsApi(new JSApiShowSysKeyboard());
        mWebContainer.addJsApi(new JSApiCloseSysKeyboard());
        CustomKeyboard customKeyboard = new CustomKeyboard(this);
        CustomKeyboardWrapper wrapper = new CustomKeyboardWrapper(this, customKeyboard);
        /*
         * 安全键盘 属性设置
         */
        // wrapper.setBrandName("XXX安全键盘");
        // wrapper.setBrandIcon();
        mWebContainer.addJsApi(new showSafeKeyboard(this, wrapper));
        mWebContainer.addJsApi(new JSSendSharkSafeKeyboard());
        mWebContainer.addJsApi(new closeSafeKeyboard());

        /*
         * 添加 安全键盘密码强度检测/两次输入一致性检测JSAPI
         */
        mWebContainer.addJsApi(new safeKeyboardWeakCheck(SharkService.getSharkWithInit()));
        mWebContainer.addJsApi(new safeKeyboardDoubleCheck(SharkService.getSharkWithInit()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mContainer.addView((ViewGroup) mWebContainer.getWebViewHolder().getWebView(), params);
    }

    private void initDoc() {
        PreviewDocImpl previewDoc = new PreviewDocImpl.Builder()
                .with(this)
                .setContentActivity(CustomDocShowActivity.class)
                .create();
        mWebView.setDownloadListener(previewDoc);
    }

    private void loadUrl() {
        boolean isShowTitle = getIntent().getBooleanExtra(ModuleH5containerConst.P_SHOW_TITLE, true);
        if (isShowTitle) {
            String title = getIntent().getStringExtra(ModuleH5containerConst.P_TITLE);
            if (!TextUtils.isEmpty(title)) {
                mTopBar.setTitle(title);
            }
        }
        String url = getIntent().getStringExtra(ModuleH5containerConst.P_URL);
        //注意设置UserAgent必须在loadUrl之前,否则会导致url被加载两次
        mWebContainer.getWebViewHolder().loadUrl(url);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 菜单点击
     *
     * @return
     */
    private PopupMenu.OnMenuItemClickListener getOnMenuItemClickListener() {
        PopupMenu.OnMenuItemClickListener onMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case Menu.FIRST + 0:
                        String menu1 = getResources().getString(R.string.js_api_activiity_native_h5);

                        JsonObject refreshObject = new JsonObject();
                        refreshObject.addProperty("index", 0);
                        refreshObject.addProperty("tips", menu1);
                        mWebContainer.nativeCallJs("onNavigationItemClick", refreshObject);
                        mJsApiOptionMenu.onItemClick(0);
                        return true;
                    case Menu.FIRST + 1:
                        String tex = getResources().getString(R.string.js_api_activiity_copy_clip);

                        JsonObject copyObject = new JsonObject();
                        copyObject.addProperty("index", 1);
                        copyObject.addProperty("tips", tex + mWebContainer.getWebViewHolder().getUrl());
                        mWebContainer.nativeCallJs("onNavigationItemClick", copyObject);

                        mJsApiOptionMenu.onItemClick(1);
                        //获取剪贴版
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", mWebContainer.getWebViewHolder().getUrl());
                        clipboard.setPrimaryClip(clip);
                        return true;
                    case Menu.FIRST + 2:
                        String tex2 = getResources().getString(R.string.js_api_activiity_open_browser);

                        JsonObject defaultBrowserObject = new JsonObject();
                        defaultBrowserObject.addProperty("index", 2);
                        defaultBrowserObject.addProperty("tips", tex2);
                        mWebContainer.nativeCallJs("onNavigationItemClick", defaultBrowserObject);
                        mJsApiOptionMenu.onItemClick(2);

                        Uri uri = Uri.parse("https://www.qq.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        };
        return onMenuItemClickListener;
    }

    public void updatePopupMenu(ArrayList<JsApiSetOptionMenu.ParamItem> params) {
        if (params == null || params.size() == 0) {
            return;
        }
        mPopupMenu = new PopupMenu(this, mMoreImageView);
        Menu menu = mPopupMenu.getMenu();
        for (JsApiSetOptionMenu.ParamItem item : params) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(item.title);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor(item.titleColor));
            stringBuilder.setSpan(foregroundColorSpan, 0, item.title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            menu.add(Menu.NONE, Menu.FIRST + item.index, 0, stringBuilder);
        }
        mPopupMenu.setOnMenuItemClickListener(getOnMenuItemClickListener());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_more) {
            if (mPopupMenu != null) {
                mPopupMenu.show();
            }
        } else if (id == R.id.iv_back) {
            onBack();
        } else if (id == R.id.iv_finish) {
            finish();
        }
    }

    private void onBack() {
        if (mWebView.canGoBack()) {
            String text = getResources().getString(R.string.js_api_activiity_show_jsapi);
            mTopBar.setTitle(text);
            mFinishImageView.setVisibility(View.INVISIBLE);
            mMoreImageView.setVisibility(View.INVISIBLE);
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case JsApiLoadBigImage.REQUEST_CODE_BASE64:
            case JsApiLoadBigImage.REQUEST_CODE_URL:
                if (resultCode != Activity.RESULT_OK) {
                    String tex = getResources().getString(R.string.js_api_activiity_cancel);

                    ToastUtil.showToast(tex);
                    return;
                }
                Uri originalUri = data.getData(); // 获得图片的uri
                if (originalUri == null) {
                    String tex = getResources().getString(R.string.js_api_activiity_uri_get_failed);

                    ToastUtil.showToast(tex);
                    return;
                }
                String filePath = parseFilePath(originalUri);
                mJsApiLoadBigImage.callbackData(filePath, requestCode);
                break;
            default:
                break;
        }
        mJsApiShareApp.handleActivityResult(requestCode,resultCode,data);
    }



    private String parseFilePath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String picturePath = null;
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        } else if (!TextUtils.isEmpty(uri.getPath())) {
            picturePath = uri.getPath();
        }
        return picturePath;
    }

    class TestWebViewClient extends DefaultTMFX5WebViewClient {

        private QMUITopBarLayout mTopBar;

        public TestWebViewClient(QMUITopBarLayout topBar) {
            mTopBar = topBar;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (SpecialHandle.shouldOverrideUrlLoading(view, url)) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
            //注意super.onPageStarted不能删除
            super.onPageStarted(view, url, favicon);
            if (mPageLoadingProgressBar != null) {
                mPageLoadingProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //注意super.onPageFinished不能删除
            super.onPageFinished(view, url);

            if (view.getProgress() == 100) {
                if (mPageLoadingProgressBar != null) {
                    mPageLoadingProgressBar.setVisibility(View.GONE);
                }

                //解析加载的url设置的title
                url = URLDecoder.decode(url);
                String[] arrUrl = url.split("\\?");
                if (arrUrl != null && arrUrl.length > 1) {
                    String[] arrArg = arrUrl[1].split("&");
                    HashMap<String, String> mapArg = new HashMap<String, String>();
                    if (arrArg != null) {
                        for (int i = 0; i < arrArg.length; i++) {
                            String[] tmp = arrArg[i].split("=");
                            mapArg.put(tmp[0], tmp[1]);
                        }
                    }

                    String title = mapArg.get("title");
                    if (!TextUtils.isEmpty(title) && mTopBar != null) {
                        mTopBar.setTitle(title);
                    }
                }
            }

        }
    }

    public class TestDefaultTMFX5WebChromeClient extends DefaultTMFX5WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //注意super.onProgressChanged不能删除
            super.onProgressChanged(view, newProgress);
            mPageLoadingProgressBar.setProgress(newProgress);
            if (mPageLoadingProgressBar != null && newProgress >= 100) {
                mPageLoadingProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
