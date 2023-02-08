package com.tencent.tmf.module.hybrid;

import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

public class JsonUtil {

    public static Map<String, String> getMapFromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject object = new JSONObject(json);
            HashMap<String, String> ret = new HashMap<>();
            Iterator<String> keySet = object.keys();
            String key;
            String value;
            while (keySet.hasNext()) {
                key = keySet.next();
                value = object.getString(key);
                ret.put(key, value);
            }
            return ret;
        } catch (Exception e) {
            Log.e("TMFDemo", "get map from json failed " + e);
            e.printStackTrace();
        }
        return null;
    }

    public static String getJsonStringFromMap(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            try {
                jsonObject.put(key, map.get(key));
            } catch (Exception e) {
                Log.e("TMFDemo", "get Json from map Failed " + e);
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }


}
