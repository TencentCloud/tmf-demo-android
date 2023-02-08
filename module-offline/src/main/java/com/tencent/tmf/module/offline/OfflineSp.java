package com.tencent.tmf.module.offline;

import android.content.Context;

import com.tencent.tmf.common.ContextHolder;
import com.tencent.tmf.common.storage.sp.BaseSp;


public class OfflineSp extends BaseSp {
    /**
     * SharedPreferences文件名
     */
    private static final String FILE_NAME = "tmf_offline";
    private static final String KEY_STRING_OFFLINE_P_K = "offline_p_k";

    private static volatile OfflineSp mInstatnce;

    private OfflineSp() {
        Context context = ContextHolder.sContext;
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized OfflineSp getInstance() {
        if (mInstatnce == null) {
            synchronized (OfflineSp.class) {
                if (mInstatnce == null) {
                    mInstatnce = new OfflineSp();
                }
            }
        }

        return mInstatnce;
    }

    public synchronized void putOfflinePublickKey(String value) {
        putString(mEditor, KEY_STRING_OFFLINE_P_K, value);
    }

    public synchronized String getOfflinePublickKey() {
        return getString(mSharedPreferences, KEY_STRING_OFFLINE_P_K, "");
    }

    public synchronized void clearAll() {
        clear(mEditor);
    }
}
