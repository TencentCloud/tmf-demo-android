package com.tencent.tmf.module.hybrid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.hybrid.TMFHybridManager;
import com.tencent.tmf.hybrid.UISettings;
import com.tencent.tmf.weboffline.api.OfflineManager;
import com.tencent.tmf.weboffline.api.SimpleCallback;

@Destination(
        url = "portal://com.tencent.tmf.module.hybrid/hybrid-activity",
        launcher = Launcher.ACTIVITY,
        description = "离线包API优化测试"
)
public class FastApiAccessActivity extends TopBarActivity implements View.OnClickListener {
    private static final String DEFAULT_BID = "WebOffline_Demo_1";

    private EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getResources().getString(R.string.hybrid_access_title);
        String hit = getResources().getString(R.string.hybrid_access_title_hint);
        mTopBar.setTitle(title);
        mEditText = mContentView.findViewById(R.id.edit_app_id_or_url);

        mEditText.setHint(hit);
        mEditText.setText(DEFAULT_BID);

        mContentView.findViewById(R.id.btn_start_offline_app).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_set_virtual_url).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_set_url).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_set_custom_view).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_start_h5_container).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_set_default_title_view).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_h5_container_set).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_h5_embedview_test).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_h5_set_remote).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_h5_set_common_res).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_h5_remove_all_offline_resouces).setOnClickListener(this);
        TMFHybridManager.getInstance().init(getApplicationContext());
    }

    boolean isGlobalViewProviderSet;//UI provider 是否设置
    boolean isCustomUISetting;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_start_offline_app) {
            String id = mEditText.getText().toString().trim();
            Log.d("TMF", "get fast api id or url " + id);
            String text = getResources().getString(R.string.hybrid_access_open_url,id);
            ToastUtil.showToast(text);
            TMFHybridManager.getInstance().startAppById(id);
        } else if (view.getId() == R.id.btn_set_url) {
            String text = "https://www.qq.com/h5/index.html?_bid=optmizetest";
            mEditText.setText(text);
        } else if (view.getId() == R.id.btn_set_virtual_url) {
            TMFHybridManager.getInstance().setVirtualAddress("https://www.qq.com");
            String text = getResources().getString(R.string.hybrid_access_global_host_set_success);

            ToastUtil.showToast(text);
        } else if (view.getId() == R.id.btn_start_h5_container) {
            String url = mEditText.getText().toString().trim();
            if (TextUtils.isEmpty(url)) {
                String text = getResources().getString(R.string.hybrid_access_global_specify_bid_or_url);
                ToastUtil.showToast(text);
                return;
            }
            TMFHybridManager.getInstance().startAppByUrl(url);
        } else if (view.getId() == R.id.btn_set_custom_view) {
            QMUIRoundButton button = findViewById(R.id.btn_set_custom_view);

            if (!isGlobalViewProviderSet) {
                TMFHybridManager.getInstance().setCustomView(new TestCustomViewProvider());
                String text = getResources().getString(R.string.hybrid_access_global_custom_global_view_setted);
                String text2 = getResources().getString(R.string.hybrid_access_global_recover_global_view_setted);
                ToastUtil.showToast(text);
                button.setText(text2);
            } else {
                String text = getResources().getString(R.string.hybrid_access_global_remove_global_view_setted);
                String text2 = getResources().getString(R.string.hybrid_access_global_set_custom_global_view);
                TMFHybridManager.getInstance().setCustomView(null);
                ToastUtil.showToast(text);
                button.setText(text2);
            }
            isGlobalViewProviderSet = !isGlobalViewProviderSet;

        } else if (view.getId() == R.id.btn_set_default_title_view) {
            String url = mEditText.getText().toString().trim();
            QMUIRoundButton button = findViewById(R.id.btn_set_default_title_view);
            UISettings uiSettings = UISettings.getDefault();
            if (!isCustomUISetting) {
                uiSettings = getCustomUiSettings();
                String text = getResources().getString(R.string.hybrid_access_global_resume_ui);
                String text2 = getResources().getString(R.string.hybrid_access_global_set_ui);
                button.setText(text);
                ToastUtil.showToast(text2);
            } else {
                //自定义UISettings
                String text = getResources().getString(R.string.hybrid_access_global_custom_uid);
                String text2 = getResources().getString(R.string.hybrid_access_global_resume_ui_success);
                button.setText(text);
                ToastUtil.showToast(text2);
            }
            isCustomUISetting = !isCustomUISetting;
            TMFHybridManager.getInstance().startAppByUrl(url, uiSettings);

        } else if (view.getId() == R.id.btn_h5_container_set) {
            Intent intent = new Intent(this, OpenAppWithParamsActivity.class);
            startActivity(intent);

        } else if (view.getId() == R.id.btn_h5_embedview_test) {
            String url = mEditText.getText().toString().trim();
            if (TextUtils.isEmpty(url)) {
                String text = getResources().getString(R.string.hybrid_access_global_specify_bid_or_url);
                ToastUtil.showToast(text);
                return;
            }
            Intent intent = new Intent(this, EmbedViewTestActivity.class);
            intent.putExtra("key_bid_url", mEditText.getText().toString().trim());
            startActivity(intent);

        } else if (view.getId() == R.id.btn_h5_set_remote) {
            QMUIRoundButton button = findViewById(R.id.btn_h5_set_remote);
            if (!isRemoteSet) {
                TMFHybridManager.getInstance().setHostForOnlineApp("http://www.qq.com");
                String text = getResources().getString(R.string.hybrid_access_global_remote_host_reset);
                button.setText(text);
            } else {
                TMFHybridManager.getInstance().setHostForOnlineApp("");
                String text = getResources().getString(R.string.hybrid_access_global_remote_host_specify);
                button.setText(text);
            }
            isRemoteSet = !isRemoteSet;
            String text3 = getResources().getString(R.string.hybrid_access_global_remote_host_set,button.getText().toString().trim());
            ToastUtil.showToast(text3 );
        } else if (view.getId() == R.id.btn_h5_set_common_res) {
            String id = mEditText.getText().toString().trim();
            TMFHybridManager.getInstance().addCommonResource(id);
            String text4 = getResources().getString(R.string.hybrid_access_global_remote_host_set,id);
            ToastUtil.showToast(text4);

        } else if (view.getId() == R.id.btn_h5_remove_all_offline_resouces) {
            deleteAllOfflineData(view);
        }

    }

    boolean isRemoteSet = false;

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_fast_api, null);
    }

    private UISettings getCustomUiSettings() {
        return new UISettings.Builder()
                .fullScreen(false)
                .dividerColor(getResources().getColor(R.color.poc_gray_D8D8D8))
                .showDivider(false)
                .showMainTitle(true)
                .mainTitleText("Main Title")
                .mainTitleTextColor(getResources().getColor(R.color.qmui_config_color_red))
                .showSubTitle(true)
                .subTitleText("Progressing")
                .subTitleTextColor(getResources().getColor(R.color.app_color_blue))
                .setTitleViewBackGroundColor(getResources().getColor(R.color.color_default_black))
                .setBackBtnIcon(R.mipmap.h5_default_back)
                .showOptionMenu(true)
                .optionMenuIcon0(R.drawable.brand_icon)
                .optionMenuIcon1(R.mipmap.about_logo)
                .showProgress(true)
                .progressColor(getResources().getColor(R.color.qmui_config_color_red))
                .build();
    }

    /**
     * 清除所有离线包
     */
    public void deleteAllOfflineData(View view) {
        OfflineManager.deleteAllOfflineData(this, new SimpleCallback<Integer>() {
            @Override
            public void callback(Integer ret) {
                ToastUtil.showToast("clear" + (ret == 0 ? "success" : "failed"));
            }
        });
    }
}
