package com.tencent.tmf.module.portal;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.tencent.tmf.common.activity.TopBarActivity;

public class DynamicRouteActivity extends TopBarActivity {

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_dynamic_route, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.module_portal_9));
    }
}