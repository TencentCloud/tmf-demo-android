package com.tencent.tmf.module.storage.kv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.tencent.mmkv.MMKV;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.storage.R;
import com.tencent.tmf.module.storage.StorageActivity;
import com.tencent.tmf.storage.TMFSharedPreferences;
import com.tencent.tmf.storage.TMFStorage;

import java.util.HashSet;

public class  KVSimpleActivity extends TopBarActivity implements View.OnClickListener {

    private TMFSharedPreferences sharedPreferences;
    private static final String KEY_USE_MMKV = "use_mmkv";
    private static final String CRYPTO_KEY = "crypto_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.name_kv));
        findViewById(R.id.btn_save_mmkv).setOnClickListener(this);
        findViewById(R.id.btn_read_mmkv).setOnClickListener(this);
        findViewById(R.id.btn_sp_migrate).setOnClickListener(this);
        findViewById(R.id.btn_detail_demo).setOnClickListener(this);

        String storageType = getIntent().getStringExtra(StorageActivity.KEY_STORAGE_TYPE);
        String storageUID = getIntent().getStringExtra(StorageActivity.KEY_STORAGE_UID);
        if (StorageActivity.STORAGE_TYPE_TEMP.equals(storageType)) {
            sharedPreferences = TMFStorage.getTemporary().sp();
            ;
        } else if (StorageActivity.STORAGE_TYPE_USER.equals(storageType) && !TextUtils.isEmpty(storageUID)) {
            sharedPreferences = TMFStorage.getByUser(storageUID).sp();
        } else {
            sharedPreferences = TMFStorage.getDefault().sp();
        }
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_kv_simple, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_save_mmkv) {
            sharedPreferences.edit().putLong(KEY_USE_MMKV, System.currentTimeMillis()).commit();
            Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_37), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_read_mmkv) {
            long time = sharedPreferences.getLong(KEY_USE_MMKV, 0);
            Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_38) + time, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_sp_migrate) {
            MMKV kv = testImportSharedPreferences();
            String str = "bool: " + kv.getBoolean("bool", false) + ",int: " + kv.getInt("int", 0)
                    + ",long: " + kv.getLong("long", 0) + ",float: " + kv.getFloat("float", 0)
                    + ",double: " + kv.decodeDouble("double") + ",string: " + kv.getString("string", null)
                    + ",string-set: " + kv.getStringSet("string-set", null);
            Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_39) + str, Toast.LENGTH_LONG).show();
        } else if (id == R.id.btn_detail_demo) {
            startActivity(new Intent(this, KVActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MMKV issure #544
        // Native Crash With : signal 11 (SIGSEGV), code 1 (SEGV_MAPERR)
        //You can't use any MMKV instances or call any MMKV functions after calling MMKV.onExit().
//        MMKV.onExit();//App程序退出时，退出KV存储组件
    }

    private MMKV testImportSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("sharedPreferences_migrate", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("bool", true);
        editor.putInt("int", Integer.MIN_VALUE);
        editor.putLong("long", Long.MAX_VALUE);
        editor.putFloat("float", -3.14f);
        editor.putString("string", "sharedPreferences migrate");
        HashSet<String> set = new HashSet<String>();
        set.add("W");
        set.add("e");
        set.add("C");
        set.add("h");
        set.add("a");
        set.add("t");
        editor.putStringSet("string-set", set);
        editor.commit();

        MMKV kv = MMKV.mmkvWithID("sharedPreferences_migrate");
        kv.importFromSharedPreferences(preferences);
        editor.clear().commit();
        return kv;
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }
}
