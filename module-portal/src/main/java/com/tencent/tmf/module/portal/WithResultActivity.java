package com.tencent.tmf.module.portal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;

@Destination(url = PortalConst.WITH_RESULT_ACTIVITY,
        launcher = Launcher.ACTIVITY,
        description = "Portal演示")
public class WithResultActivity extends TopBarActivity {

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_with_result, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.module_portal_57));

        findViewById(R.id.parameter).setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra("result", getResources().getString(R.string.module_portal_58));
            setResult(RESULT_OK, data);
            finish();
        });
    }
}