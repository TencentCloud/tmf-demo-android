package com.tencent.tmf.h5container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.common.activity.TopBarActivity;

@Destination(
        url = "portal://com.tencent.tmf.module.h5/h5-container-activity",
        launcher = Launcher.ACTIVITY,
        description = "H5容器"
)
public class H5ContainerActivity extends TopBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String text = getResources().getString(R.string.h5_container_activity);
        mTopBar.setTitle(text);
        findViewById(R.id.btn_jsapi).setOnClickListener(this);
        findViewById(R.id.btn_doc).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_h5container, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_jsapi) {
            String text = getResources().getString(R.string.h5_container_jsapi_title);
            Portal.from(this)
                    .url(ModuleH5containerConst.U_J_SAPI_ACTIVITY)
                    .param(ModuleH5containerConst.P_URL, "file:///android_asset/demo.htm")
                    .param(ModuleH5containerConst.P_TITLE, text)
                    .launch();
        } else if (id == R.id.btn_doc) {
            Portal.from(this)
                    .url(ModuleH5containerConst.U_X_5_DOC_LIST_ACTIVITY)
                    .launch();
        }
    }
}
