package com.tencent.tmf.module.portal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.portal.annotations.Parameter;

@Destination(url = PortalConst.NO_RESULT_ACTIVITY,
        launcher = Launcher.ACTIVITY,
        parameters = {@Parameter(name = "param", type = Integer.class, optional = true, description = "参数")},
        description = "Portal演示")
public class NoResultActivity extends TopBarActivity {

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_no_result, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.module_portal_12));
        ((TextView) findViewById(R.id.content)).setText(getResources().getString(R.string.module_portal_13) + ModulePortal.getInitMessage());

        Bundle params = getIntent().getExtras();
        if (params.containsKey("param")) {
            ((TextView) findViewById(R.id.parameter)).setText(getResources().getString(R.string.module_portal_14) + params.getInt("param", -1));
        }
    }


}