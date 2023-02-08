package com.tencent.tmf.module.icdp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.icdp.BuildConfig;

@Destination(
        url = "portal://com.tencent.tmf.module.icdp/icdp-activity",
        launcher = Launcher.ACTIVITY,
        description = "智慧投放测试页面"
)
public class IcdpActivity extends TopBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getResources().getString(R.string.icdp_activity_title);
        mTopBar.setTitle(title);
        initView();
    }


    private void initView() {
        findViewById(R.id.app_demo_item).setOnClickListener(v -> {
            startActivity(new Intent(IcdpActivity.this, AppDemoActivity.class));
        });
        findViewById(R.id.h5_demo_item).setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://119.29.98.229:7070/index.html?appid=tmf_icdp_demo")));
        });
        TextView version = findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.demo_activity_icdp, null);
    }
}
