package com.tencent.tmf.module.icdp;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.PrivacyFitUtil;
import com.tencent.tmf.icdp.BuildConfig;
import com.tencent.tmf.icdp.ICDPApplication;
import com.tencent.tmf.icdp.utils.GuidManager;

public class ParamsPhoneActivity extends TopBarActivity {

    String phoneVersion;
    String deviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String text5 = getResources().getString(R.string.icdp_params_phone_activity_title);
        mTopBar.setTitle(text5);
        initData();
        initView();

    }

    private void initData() {
        phoneVersion = PrivacyFitUtil.BRAND + "  " + PrivacyFitUtil.MODEL + "  " + Build.VERSION.SDK_INT;
        deviceId = GuidManager.getCustomGuid(getApplicationContext());

    }

    private void initView() {
        View itemParamsPhoneVersion = findViewById(R.id.item_params_phone_version);
        String text5 = getResources().getString(R.string.icdp_params_phone_activity_phone_type);

        setItemTitle(itemParamsPhoneVersion, text5);
        setItemValue(itemParamsPhoneVersion, phoneVersion);
        String text6 = getResources().getString(R.string.icdp_params_phone_activity_device_id);

        View itemParamsPhoneDeviceId = findViewById(R.id.item_params_phone_device_id);
        setItemTitle(itemParamsPhoneDeviceId, text6);
        setItemValue(itemParamsPhoneDeviceId, deviceId);

        /*View item_params_phone_user_id = findViewById(R.id.item_params_phone_user_id);
        setItemTitle(item_params_phone_user_id,"用户ID");
        setItemValue(item_params_phone_user_id,Config.getInstance(this).getUid());*/
        String text7 = getResources().getString(R.string.icdp_params_phone_activity_version);

        View itemParamsPhoneAppVersion = findViewById(R.id.item_params_phone_app_version);
        setItemTitle(itemParamsPhoneAppVersion, text7);
        setItemValue(itemParamsPhoneAppVersion, BuildConfig.VERSION_NAME);
        String text8 = getResources().getString(R.string.icdp_params_phone_activity_city);

        View itemParamsPhoneCity = findViewById(R.id.item_params_phone_city);
        setItemTitle(itemParamsPhoneCity, text8);
        setItemValue(itemParamsPhoneCity, ICDPApplication.getCity());


    }


    private void setItemValue(View v, String text) {
        TextView itemEdit = v.findViewById(R.id.item_edit);
        if (itemEdit != null) {
            itemEdit.setText(text);
        }
    }

    void setItemTitle(View v, String text) {
        TextView itemTitle = v.findViewById(R.id.item_title);
        if (itemTitle != null) {
            itemTitle.setText(text);
            itemTitle.setEnabled(false);
        }
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.demo_activity_params_phone, null);
    }
}
