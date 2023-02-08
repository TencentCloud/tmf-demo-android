package com.tencent.tmf.module.storage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.storage.TMFStorage;
import com.tencent.tmf.storage.TMFStorageConfig;

@Destination(
        url = "portal://com.tencent.tmf.module.storage/storage-activity",
        launcher = Launcher.ACTIVITY,
        description = "存储组件"
)
public class StorageActivity extends TopBarActivity implements View.OnClickListener {

    public static final String KEY_STORAGE_TYPE = "storage_type";
    public static final String KEY_STORAGE_UID = "storage_uid";
    public static final String STORAGE_TYPE_DEFAULT = "default";
    public static final String STORAGE_TYPE_USER = "user";
    public static final String STORAGE_TYPE_TEMP = "temp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.storage_module));
        TMFStorage.initialize(getApplicationContext(), new TMFStorageConfig.Builder().spCryptKey("crypto_key").build());
        findViewById(R.id.btn_default).setOnClickListener(this);
        findViewById(R.id.btn_user).setOnClickListener(this);
        findViewById(R.id.btn_temp).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_storage_type, null);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, StorageSelectActivity.class);
        int id = v.getId();
        if (id == R.id.btn_default) {
            intent.putExtra(KEY_STORAGE_TYPE, STORAGE_TYPE_DEFAULT);
        } else if (id == R.id.btn_user) {
            String userId = ((EditText)findViewById(R.id.input_user_id)).getText().toString();
            if (TextUtils.isEmpty(userId)) {
                Toast.makeText(v.getContext(), R.string.storage_uid_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra(KEY_STORAGE_UID, userId);
            intent.putExtra(KEY_STORAGE_TYPE, STORAGE_TYPE_USER);
        } else if (id == R.id.btn_temp) {
            intent.putExtra(KEY_STORAGE_TYPE, STORAGE_TYPE_TEMP);
        }
        startActivity(intent);
    }
}
