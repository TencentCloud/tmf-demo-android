package com.tencent.tmf.module.icdp;

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

    public String getSplash() {
        return sharedPreferences.getString("splash", "splash_test1");
    }

    public void setSplash(String arg) {
        sharedPreferences.edit().putString("splash", arg).apply();
    }

    public String getHotSplash() {
        return sharedPreferences.getString("hot_splash", "hotsplash_test1");
    }

    public void setHotSplash(String arg) {
        sharedPreferences.edit().putString("hot_splash", arg).apply();
    }

    public String getBroadcast() {
        return sharedPreferences.getString("broadcast", "broadcast_test1");
    }

    public void setBroadcast(String arg) {
        sharedPreferences.edit().putString("broadcast", arg).apply();
    }

    public String getBanner() {
        return sharedPreferences.getString("banner", "banner_test1");
    }

    public void setBanner(String arg) {
        sharedPreferences.edit().putString("banner", arg).apply();
    }

    public String getImage() {
        return sharedPreferences.getString("image", "image_test1");
    }

    public void setImage(String arg) {
        sharedPreferences.edit().putString("image", arg).apply();
    }

    public String getPopup() {
        return sharedPreferences.getString("popup", "pop_test1");
    }

    public void setPopup(String arg) {
        sharedPreferences.edit().putString("popup", arg).apply();
    }

    public String getList() {
        return sharedPreferences.getString("list", "list_test1");
    }

    public void setList(String arg) {
        sharedPreferences.edit().putString("list", arg).apply();
    }

    public String getUid() {
        return sharedPreferences.getString("userId", "abc123");
    }

    public void setUid(String uid) {
        sharedPreferences.edit().putString("userId", uid).apply();
    }

    public String getServerHost() {
        return sharedPreferences.getString("server", ParamsEnvActivity.POCICDPSERVER);
    }

    public void setServerHost(String arg) {
        sharedPreferences.edit().putString("server", arg).apply();
    }

    public String getCustomServerHost() {
        return sharedPreferences.getString("custom_server", getServerHost());
    }

    public void setCustomServerHost(String arg) {
        sharedPreferences.edit().putString("custom_server", arg).apply();
    }

    public void setEnv(String arg) {
        sharedPreferences.edit().putString("env", arg).apply();
    }

    public String getEnv() {
        return sharedPreferences.getString("env", "poc");
    }

}
