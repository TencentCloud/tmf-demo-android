package com.tencent.tmf.module.qapm.performance.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.module.qapm.R;
import com.tencent.tmf.module.qapm.performance.Config;

public class ParamsActivity extends TopBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle("参数设置");
        initView();
    }

    EditText etUid;
    EditText etQapmkey;
    EditText etKeyHost;
    EditText etAthenaHost;

    private void initView() {

        View itemParamsUid = findViewById(R.id.item_params_uid);
        setItemTitle(itemParamsUid, "uid");
        etUid = setItemValue(itemParamsUid, Config.getInstance(this).getUid());

        View itemParamsQapmKey = findViewById(R.id.item_params_qapmKey);
        setItemTitle(itemParamsQapmKey, "qapm_key");
        etQapmkey = setItemValue(itemParamsQapmKey, Config.getInstance(this).getQapmKey());

        View itemParamsQapmServer = findViewById(R.id.item_params_qapm_server);
        setItemTitle(itemParamsQapmServer, "上报地址");
        etKeyHost = setItemValue(itemParamsQapmServer, Config.getInstance(this).geKeyHost());

        View itemParamsQapmathenahostServer = findViewById(R.id.item_params_qapmathenahost_server);
        setItemTitle(itemParamsQapmathenahostServer, "行为地址");
        etAthenaHost = setItemValue(itemParamsQapmathenahostServer, Config.getInstance(this).getkeyAthenaHost());

        findViewById(R.id.save_params).setOnClickListener(v -> {
            Config.getInstance(this).setUid(etUid.getText().toString().trim());
            Config.getInstance(this).setQapmKey(etQapmkey.getText().toString().trim());
            Config.getInstance(this).setKeyHost(etKeyHost.getText().toString().trim());
            Config.getInstance(this).setKeyAthenaHost(etAthenaHost.getText().toString().trim());
            ToastUtil.showToast("重启生效");
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
        return LayoutInflater.from(this).inflate(R.layout.activity_qapm_params, null);
    }
}
