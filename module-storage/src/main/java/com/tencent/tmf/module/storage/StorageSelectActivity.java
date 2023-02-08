package com.tencent.tmf.module.storage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.storage.db.DBActivity;
import com.tencent.tmf.module.storage.file.FileActivity;
import com.tencent.tmf.module.storage.kv.KVSimpleActivity;
import com.tencent.tmf.storage.TMFStorage;

public class StorageSelectActivity extends TopBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.storage_module));
        findViewById(R.id.btn_db).setOnClickListener(this);
        findViewById(R.id.btn_kv).setOnClickListener(this);
        findViewById(R.id.btn_file).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_storage, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        if (id == R.id.btn_db) {
            intent = new Intent(this, DBActivity.class);
        } else if (id == R.id.btn_kv) {
            intent = new Intent(this, KVSimpleActivity.class);
        } else if (id == R.id.btn_file) {
            intent = new Intent(this, FileActivity.class);
        }
        if (intent != null) {
            intent.putExtra(StorageActivity.KEY_STORAGE_TYPE, getIntent().getStringExtra(StorageActivity.KEY_STORAGE_TYPE));
            intent.putExtra(StorageActivity.KEY_STORAGE_UID, getIntent().getStringExtra(StorageActivity.KEY_STORAGE_UID));
            startActivity(intent);
        }
    }
}
