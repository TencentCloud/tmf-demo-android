package com.tencent.tmf.module.icdp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.tmf.common.activity.TopBarActivity;

public class ParamsBoothActivity extends TopBarActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getResources().getString(R.string.icdp_activity_param_set_info);
        mTopBar.setTitle(title);
        initView();
    }

    EditText etBoothBroadcast;
    EditText etBoothBanner;
    EditText etBoothImage;
    EditText etBoothSplash;
    EditText etBoothPopup;
    EditText etBoothList;
    EditText etBoothHotSplash;

    private void initView() {
        View itemParamsBoothBroadcast = findViewById(R.id.item_params_booth_broadcast);
        String text1 = getResources().getString(R.string.icdp_params_activity_announcement);
        setItemTitle(itemParamsBoothBroadcast, text1);
        etBoothBroadcast = setItemValue(itemParamsBoothBroadcast, Config.getInstance(this).getBroadcast());

        View itemParamsBoothBanner = findViewById(R.id.item_params_booth_banner);
        String text2 = getResources().getString(R.string.icdp_params_activity_banner);
        setItemTitle(itemParamsBoothBanner, text2);
        etBoothBanner = setItemValue(itemParamsBoothBanner, Config.getInstance(this).getBanner());

        View itemParamsBoothImage = findViewById(R.id.item_params_booth_image);
        String text3 = getResources().getString(R.string.icdp_params_activity_image);
        setItemTitle(itemParamsBoothImage, text3);
        etBoothImage = setItemValue(itemParamsBoothImage, Config.getInstance(this).getImage());

        View itemParamsBoothSplash = findViewById(R.id.item_params_booth_splash);
        String text4 = getResources().getString(R.string.icdp_params_activity_splash);
        setItemTitle(itemParamsBoothSplash, text4);
        etBoothSplash = setItemValue(itemParamsBoothSplash, Config.getInstance(this).getSplash());

        View itemParamsBoothPopup = findViewById(R.id.item_params_booth_popup);
        String text5 = getResources().getString(R.string.icdp_params_activity_popup);
        setItemTitle(itemParamsBoothPopup, text5);
        etBoothPopup = setItemValue(itemParamsBoothPopup, Config.getInstance(this).getPopup());

        View itemParamsBoothList = findViewById(R.id.item_params_booth_list);
        String text6 = getResources().getString(R.string.icdp_params_activity_list);
        setItemTitle(itemParamsBoothList, text6);
        etBoothList = setItemValue(itemParamsBoothList, Config.getInstance(this).getList());

        View itemParamsBoothHotsplash = findViewById(R.id.item_params_booth_hotsplash);
        String text7 = getResources().getString(R.string.icdp_params_activity_hot_splash);
        setItemTitle(itemParamsBoothHotsplash, text7);
        etBoothHotSplash = setItemValue(itemParamsBoothHotsplash, Config.getInstance(this).getHotSplash());

        findViewById(R.id.save_params).setOnClickListener(v -> {

            Config.getInstance(this).setBroadcast(etBoothBroadcast.getText().toString().trim());
            Config.getInstance(this).setBanner(etBoothBanner.getText().toString().trim());
            Config.getInstance(this).setImage(etBoothImage.getText().toString().trim());
            Config.getInstance(this).setSplash(etBoothSplash.getText().toString().trim());
            Config.getInstance(this).setPopup(etBoothPopup.getText().toString().trim());
            Config.getInstance(this).setList(etBoothList.getText().toString().trim());
            Config.getInstance(this).setHotSplash(etBoothHotSplash.getText().toString().trim());

        });
    }

    void setItemTitle(View v, String text) {
        TextView itemTitle = v.findViewById(R.id.item_title);
        if (itemTitle != null) {
            itemTitle.setText(text);
        }
    }

    private EditText setItemValue(View v, String text) {
        EditText itemEdit = v.findViewById(R.id.item_edit);
        itemEdit.setText(text);
        return itemEdit;
    }


    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.demo_activity_params_booth, null);
    }

}
