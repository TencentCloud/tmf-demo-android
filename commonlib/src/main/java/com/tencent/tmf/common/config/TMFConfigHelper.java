package com.tencent.tmf.common.config;

import android.text.TextUtils;
import com.tencent.tmf.base.api.utils.FileUtil;
import com.tencent.tmf.common.CommonApp;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

public class TMFConfigHelper {

    public static String getCurrentConfigJson() {
        String path = ConfigSp.getInstance().getTMFConfigPATH();
        InputStream is = null;
        try {
            if (!TextUtils.isEmpty(path)) {
                is = new FileInputStream(path);
                return FileUtil.readFileContent(is);
            } else {
                is = CommonApp.get().getApplication().getAssets().open("tmf-android-configurations.json");
                return FileUtil.readFileContent(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String readConfigJsonFromFile(String filePath) {
        InputStream is = null;
        try {
            if (!TextUtils.isEmpty(filePath)) {
                is = new FileInputStream(filePath);
                return FileUtil.readFileContent(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static boolean isValidConfig(JSONObject configObj) {
        JSONObject sharkObj = configObj.optJSONObject("shark");
        return sharkObj != null
                && configObj.optInt("productId") > 0
                && !TextUtils.isEmpty(configObj.optString("customId"))
                && !TextUtils.isEmpty(sharkObj.optString("appKey"))
                && !TextUtils.isEmpty(sharkObj.optString("httpUrl"))
                && !TextUtils.isEmpty(sharkObj.optString("tcpHost"));
    }
}
