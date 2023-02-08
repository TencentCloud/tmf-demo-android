package com.tencent.tmf.module.qapm.performance;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {


    private static volatile Config instance;

    public static Config getInstance(Context context) {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private SharedPreferences sharedPreferences;

    public Config(Context context) {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public void setUid(String uid) {
        sharedPreferences.edit().putString("uid", uid).apply();
    }

    public String getUid() {
        return sharedPreferences.getString("uid", PerformanceConstants.USER_ID);
    }

    public void setQapmKey(String appkey) {
        sharedPreferences.edit().putString("qapmKey", appkey).apply();
    }

    public String getQapmKey() {
        return sharedPreferences.getString("qapmKey", "33e15431-1024");
    }

    public void setKeyHost(String appkey) {
        sharedPreferences.edit().putString("keyHost", appkey).apply();
    }

    public String geKeyHost() {
        return sharedPreferences.getString("keyHost", "https://ten.sngapm.qq.com");
    }

    public void setKeyAthenaHost(String keyAthenaHost) {
        sharedPreferences.edit().putString("KeyAthenaHost", keyAthenaHost).apply();
    }

    public String getkeyAthenaHost() {
        return sharedPreferences.getString("KeyAthenaHost", "");
    }

    //隐私合规检测发包时设置为true（防止多次获取位置信息），其余为false，
    public static boolean IS_FOR_PRIVACY_CHECK = false;


}
