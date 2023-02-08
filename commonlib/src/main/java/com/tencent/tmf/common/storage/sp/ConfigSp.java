package com.tencent.tmf.common.storage.sp;

import android.content.Context;

import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.ContextHolder;
import com.tencent.tmf.common.log.L;
import com.tencent.tmf.common.utils.GsonUtils;
import java.util.ArrayList;
import java.util.List;


public class ConfigSp extends BaseSp {

    /**
     * SharedPreferences文件名
     */
    private static final String FILE_NAME = "tmf_config";
    private static final String KEY_STRING_TMF_CONFIG_PATH = "tmf_config_path";
    private static final String KEY_IS_NEW_PROTOCAL = "is_new_protocal";
    private static final String KEY_MAIN_INPUT = "main_input";
    private static final String KEY_SHARK_API_NAME_INPUT = "shark_api_name_input";
    private static final String KEY_USE_FCM = "user_fcm";

    private static volatile ConfigSp mInstatnce;

    private ConfigSp() {
        Context context = ContextHolder.sContext;
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized ConfigSp getInstance() {
        if (mInstatnce == null) {
            synchronized (ConfigSp.class) {
                if (mInstatnce == null) {
                    mInstatnce = new ConfigSp();
                }
            }
        }

        return mInstatnce;
    }

//    public synchronized void putTmfConfig(String value) {
//        putString(mEditor, KEY_STRING_TMF_CONFIG, value);
//    }
//
//    public synchronized String getTmfConfig() {
//        return getString(mSharedPreferences, KEY_STRING_TMF_CONFIG, "");
//    }

    public synchronized void putTMFConfigPATH(String path) {
        putString(mEditor, KEY_STRING_TMF_CONFIG_PATH, path);
    }

    public synchronized String getTMFConfigPATH() {
        return getString(mSharedPreferences, KEY_STRING_TMF_CONFIG_PATH, "");
    }

    public synchronized void putNewProtocal(boolean value) {
        putBoolean(mEditor, KEY_IS_NEW_PROTOCAL, value);
    }

    public synchronized boolean isNewProtocal() {
        boolean isNewProtocal = getBoolean(mSharedPreferences, KEY_IS_NEW_PROTOCAL,
                BuildConfig.PROTO_TYPE_SHARK);
        L.d("isNewProtocal = {}", isNewProtocal);
        return isNewProtocal;
    }

    public synchronized boolean userFcm(){
        boolean useFcm = getBoolean(mSharedPreferences, KEY_USE_FCM, false);
        L.d("useFcm = {}", useFcm);
        return useFcm;
    }

    public synchronized void putUseFcm(boolean val){
        putBoolean(mEditor, KEY_USE_FCM, val);
    }

    public synchronized void putMainInput(List<String> value) {
        String s = GsonUtils.obj2String(value);
        L.d("putMainInput = {}", s);
        putString(mEditor, KEY_MAIN_INPUT, s);
    }

    public synchronized List<String> getMainInput() {
        String str = getString(mSharedPreferences, KEY_MAIN_INPUT, "");
        List<String> list = GsonUtils.fromJson(str, List.class);
        return list == null ? new ArrayList<>() : list;
    }

    public synchronized void putSharkApiName(List<String> value) {
        String s = GsonUtils.obj2String(value);
        L.d("putMainInput = {}", s);
        putString(mEditor, KEY_SHARK_API_NAME_INPUT, s);
    }

    public synchronized List<String> getSharkApiName() {
        String str = getString(mSharedPreferences, KEY_SHARK_API_NAME_INPUT, "");
        List<String> list = GsonUtils.fromJson(str, List.class);
        return list == null ? new ArrayList<>() : list;
    }

    public synchronized void clearAll() {
        clear(mEditor);
    }
}
