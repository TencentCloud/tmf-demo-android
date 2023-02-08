package com.tencent.tmf.module.portal;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.portal.interceptors.DestinationInterceptor;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;

@Destination(url = PortalConst.INTERCEPTOR_ACTIVITY,
        launcher = Launcher.ACTIVITY,
        interceptors = {DestinationInterceptor.class},
        description = "Portal演示")
public class InterceptorActivity extends TopBarActivity {

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_interceptor, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getStringById(R.string.module_portal_10));

        Bundle params = getIntent().getExtras();
        if (params.containsKey("param")) {
            ((TextView) findViewById(R.id.parameter)).setText(getStringById(R.string.module_portal_11) + params.getString("param"));
        }
    }

    private String getStringById(int di){
        return getResources().getString(di);
    }
}