package com.tencent.tmf.common.storage.sp;

import android.content.Context;

import com.tencent.tmf.common.ContextHolder;


/**
 * 离线包Sp存储记录
 *
 * @author robincxiao 2019/9/3 11:11
 */
public class SharkSp extends BaseSp {
    /**
     * SharedPreferences文件名
     */
    private static final String FILE_NAME = "demo_shark_config";
    private static final String KEY_SHARK_TCP_CONNECT = "shark_connect";
    private static final String KEY_SHARK_TCP_CONFIG = "shark_tcp";
    private static final String KEY_SHARK_HTTP_CONFIG = "shark_http";
    private static final String KEY_SHARK_IP_THROUGH = "shark_ip_through";

    private static volatile SharkSp mInstatnce;

    private SharkSp() {
        Context context = ContextHolder.sContext;
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized SharkSp getInstance() {
        if (mInstatnce == null) {
            synchronized (SharkSp.class) {
                if (mInstatnce == null) {
                    mInstatnce = new SharkSp();
                }
            }
        }

        return mInstatnce;
    }

    public synchronized void putSharkTcpConnect(boolean value) {
        putBoolean(mEditor, KEY_SHARK_TCP_CONNECT, value);
    }

    public synchronized boolean getSharkTcpConnect() {
        return getBoolean(mSharedPreferences, KEY_SHARK_TCP_CONNECT, true);
    }

    public synchronized void putSharkTcp(boolean value) {
        putBoolean(mEditor, KEY_SHARK_TCP_CONFIG, value);
//        currentSharTcp = value;
    }

    public synchronized boolean getSharkTcp() {
        return getBoolean(mSharedPreferences, KEY_SHARK_TCP_CONFIG, false);
//        return currentSharTcp;
    }


    public synchronized void putIpPassThrough(boolean value) {
        putBoolean(mEditor, KEY_SHARK_IP_THROUGH, value);
    }

    public synchronized boolean getIpPathThrough() {
        return getBoolean(mSharedPreferences, KEY_SHARK_IP_THROUGH, false);
    }


    public synchronized void putSharkHttp(boolean value) {
        putBoolean(mEditor, KEY_SHARK_HTTP_CONFIG, value);
    }

    public synchronized boolean getSharkHttp() {
        return getBoolean(mSharedPreferences, KEY_SHARK_HTTP_CONFIG, true);
    }

    public synchronized void clearAll() {
        clear(mEditor);
    }
}
