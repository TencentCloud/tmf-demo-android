package com.tencent.tmf.applet.demo.sp.impl;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.tencent.tmf.applet.demo.ModuleApplet;
import com.tencent.tmf.applet.demo.sp.BaseSp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author robincxiao 2019/9/3 11:11
 */
public class CommonSp extends BaseSp {

    /**
     * SharedPreferences文件名
     */
    public static final String FILE_NAME = "app_common";
    /**
     * 配置文件路径
     */
    private static final String KEY_CONFIG_FILE_PATH = "config_file_path";

    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_SKIP_LOGIN = "skip_login";
    private static final String KEY_OPERATE_USER = "operate_user";
    private static final String KEY_USER = "user";
    private static final String KEY_LAST_USER = "last_user";


    private static volatile CommonSp mInstatnce;

    private CommonSp() {
        Context context = ModuleApplet.sApp;
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized CommonSp getInstance() {
        if (mInstatnce == null) {
            synchronized (CommonSp.class) {
                if (mInstatnce == null) {
                    mInstatnce = new CommonSp();
                }
            }
        }

        return mInstatnce;
    }

    public String getConfigFilePath() {
        return getString(mSharedPreferences, KEY_CONFIG_FILE_PATH, "");
    }

    public void putConfigFilePath(String path) {
        putString(mEditor, KEY_CONFIG_FILE_PATH, path);
    }

    public void removeConfigFilePath() {
        remove(mEditor, KEY_CONFIG_FILE_PATH);
    }

    public String getUserName() {
        return getString(mSharedPreferences, KEY_USER_NAME, "");
    }

    public void putUserName(String name) {
        putString(mEditor, KEY_USER_NAME, name);
    }

    public void putSkipLogin(boolean isSkipLogin) {
        putBoolean(mEditor, KEY_SKIP_LOGIN, isSkipLogin);
    }

    public boolean isSkipLogin() {
        return getBoolean(mSharedPreferences, KEY_SKIP_LOGIN, false);
    }

    public void removeSkipLogin() {
        remove(mEditor, KEY_SKIP_LOGIN);
    }

    public synchronized void putUser(String name, String pwd) {
        String string = getString(mSharedPreferences, KEY_USER, "");
        try {
            JSONArray jsonArray = new JSONArray();
            if (!TextUtils.isEmpty(string)) {
                jsonArray = new JSONArray(string);
            }

            int length = jsonArray.length();
            boolean isFind = false;
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String name1 = jsonObject.optString("name");
                String pwd1 = jsonObject.optString("pwd");
                if (name1.equalsIgnoreCase(name) && pwd1.equalsIgnoreCase(pwd)) {
                    isFind = true;
                    break;
                }
            }

            if (!isFind) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", name.trim());
                jsonObject.put("pwd", pwd.trim());
                jsonArray.put(jsonObject);
            }

            putString(mEditor, KEY_USER, jsonArray.toString());


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name.trim());
            jsonObject.put("pwd", pwd.trim());
            putString(mEditor, KEY_LAST_USER, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<String> getUsers() {
        String string = getString(mSharedPreferences, KEY_USER, "");
        List<String> strings = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String name = jsonObject.optString("name");
                String pwd = jsonObject.optString("pwd");
                strings.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return strings;
    }

    public synchronized String getPwd(String name) {
        String string = getString(mSharedPreferences, KEY_USER, "");
        try {
            JSONArray jsonArray = new JSONArray(string);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String n = jsonObject.optString("name");
                if (name.equalsIgnoreCase(n)) {
                    return jsonObject.optString("pwd");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public synchronized JSONObject getLastUser() {
        String string = getString(mSharedPreferences, KEY_LAST_USER, "");
        if(TextUtils.isEmpty(string)) {
            return null;
        }

        try {
            return new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 清除整个文件数据
     */
    public void clearAll() {
        clear(mEditor);
    }

    public static class User {
        @SerializedName("name")
        public String name;
        @SerializedName("pwd")
        public String pwd;
    }
}
