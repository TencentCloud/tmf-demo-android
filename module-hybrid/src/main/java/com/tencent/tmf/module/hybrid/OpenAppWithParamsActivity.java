package com.tencent.tmf.module.hybrid;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.hybrid.TMFHybridManager;
import com.tencent.tmf.hybrid.TMFHybridManager.OfflineAppBundleKey;
import com.tencent.tmf.hybrid.UISettings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OpenAppWithParamsActivity extends TopBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String text4 = getResources().getString(R.string.hybrid_access_global_open_with_params);

        mTopBar.setTitle(text4);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_open_offline_app) {
            EditText indexPathEdit = mContentView.findViewById(R.id.indexPath_input);

            Map<String, String> ulrParams = new HashMap<>();
            ulrParams.put("p1", "data1");
            ulrParams.put("p2", "data2");
            ulrParams.put("p3", "data3");
            String stringOfUrlParams = JsonUtil.getJsonStringFromMap(ulrParams);
            Bundle bundle = new Bundle();

            String entryPath = TextUtils.isEmpty(indexPathEdit.getText().toString().trim()) ? "index.html"
                    : indexPathEdit.getText().toString().trim();
            String[] commonRes = getCommonResource();
            bundle.putString(OfflineAppBundleKey.KEY_ENTRANCE_PATH, entryPath);
            bundle.putStringArray(OfflineAppBundleKey.KEY_COMMON_RESOURCES, commonRes);
            bundle.putString(OfflineAppBundleKey.KEY_URL_PARAMS, stringOfUrlParams);

            EditText bidEdit = mContentView.findViewById(R.id.bid_input);
            String id = bidEdit.getText().toString().trim();
            ToastUtil.showToast("open with param ："
                    + "\nbid :" + id
                    + "\n entryPath : " + entryPath
                    + "\n url param " + stringOfUrlParams
                    + "\n common package " + Arrays.toString(commonRes)
            );

            TMFHybridManager.getInstance().startAppById(id, bundle, UISettings.getDefault());

        }


    }

    @Override
    protected View getContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_open_app_with_params, null);
        view.findViewById(R.id.btn_open_offline_app).setOnClickListener(this);
        return view;
    }

    private String[] getCommonResource() {
        EditText resourceEdit = findViewById(R.id.resource_input);
        String resource = resourceEdit.getText().toString().trim();
        if (TextUtils.isEmpty(resource)) {
            return null;
        }
        try {
            String[] res = resource.split(",");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
