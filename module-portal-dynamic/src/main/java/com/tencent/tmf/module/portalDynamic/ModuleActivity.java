package com.tencent.tmf.module.portalDynamic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;

@Destination(url = "portal://com.tencent.tmf.module.portal/portal-module-activity",
        launcher = Launcher.ACTIVITY,
        description = "Portal演示")
public class ModuleActivity extends TopBarActivity {

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_module, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopBar.setTitle(getResources().getString(R.string.module_portal_dynamic));
    }
}