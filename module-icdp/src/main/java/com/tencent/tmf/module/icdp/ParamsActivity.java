package com.tencent.tmf.module.icdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.tmf.common.activity.TopBarActivity;

public class ParamsActivity extends TopBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getResources().getString(R.string.icdp_activity_param_set);
        mTopBar.setTitle(title);
        initView();
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.demo_activity_params, null);
    }

    private void initView() {
        findViewById(R.id.item_params_phone).setOnClickListener(v ->
                startActivity(new Intent(this, ParamsPhoneActivity.class)));
        findViewById(R.id.item_params_booth).setOnClickListener(v ->
                startActivity(new Intent(this, ParamsBoothActivity.class)));
        findViewById(R.id.item_params_env).setOnClickListener(v ->
                startActivity(new Intent(this, ParamsEnvActivity.class)));
    }


}
