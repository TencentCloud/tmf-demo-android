package com.tencent.tmf.module.storage.db;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.storage.R;
import com.tencent.tmf.module.storage.StorageActivity;
import com.tencent.tmf.module.storage.db.cruddb.CRUDActivity;
import com.tencent.tmf.module.storage.db.encryptdb.EncryptDbActivity;
import com.tencent.tmf.module.storage.db.repairdb.RepairDbActivity;

public class DBActivity extends TopBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.name_db));
        findViewById(R.id.btn_db_crud).setOnClickListener(this);
        findViewById(R.id.btn_db_encrypt).setOnClickListener(this);
        findViewById(R.id.btn_db_repair).setOnClickListener(this);
//        findViewById(R.id.btn_db_room).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_db, null);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();
        if (id == R.id.btn_db_crud) { //跳转移动网关
            intent = new Intent(this, CRUDActivity.class);
        } else if (id == R.id.btn_db_encrypt) {
            intent = new Intent(this, EncryptDbActivity.class);
        } else if (id == R.id.btn_db_repair) {
            intent = new Intent(this, RepairDbActivity.class);
        }
        if (intent != null) {
            intent.putExtra(StorageActivity.KEY_STORAGE_TYPE, getIntent().getStringExtra(StorageActivity.KEY_STORAGE_TYPE));
            intent.putExtra(StorageActivity.KEY_STORAGE_UID, getIntent().getStringExtra(StorageActivity.KEY_STORAGE_UID));
            startActivity(intent);
        }
    }
}
