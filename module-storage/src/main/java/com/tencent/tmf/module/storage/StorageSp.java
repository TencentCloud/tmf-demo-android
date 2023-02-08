package com.tencent.tmf.module.storage;

import android.content.Context;

import com.tencent.tmf.common.ContextHolder;
import com.tencent.tmf.common.storage.sp.BaseSp;


public class StorageSp extends BaseSp {
    /**
     * SharedPreferences文件名
     */
    private static final String FILE_NAME = "tmf_storage";
    private static final String KEY_STRING_DB_ENCRYPT = "db_en";

    private static volatile StorageSp mInstatnce;

    private StorageSp() {
        Context context = ContextHolder.sContext;
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized StorageSp getInstance() {
        if (mInstatnce == null) {
            synchronized (StorageSp.class) {
                if (mInstatnce == null) {
                    mInstatnce = new StorageSp();
                }
            }
        }

        return mInstatnce;
    }

    public synchronized void putDbEncrypt(boolean value) {
        putBoolean(mEditor, KEY_STRING_DB_ENCRYPT, value);
    }

    public synchronized boolean getDbEncrypt() {
        return getBoolean(mSharedPreferences, KEY_STRING_DB_ENCRYPT, false);
    }

    public synchronized void clearAll() {
        clear(mEditor);
    }
}
