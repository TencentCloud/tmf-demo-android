package com.tencent.tmf.common.utils;

import android.util.Log;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

public class JsonUtil {

    public static Map<String, String> json2Map(JSONObject jo) {
        Map<String, String> map = new HashMap<>();
        if (jo == null) {
            return map;
        }

        try {
            Iterator<String> keysIt = jo.keys();
            while (keysIt.hasNext()) {
                String key = keysIt.next();
                String value = jo.opt(key).toString();
                map.put(key, value);
            }
        } catch (Exception e) {
            Log.w("JsonUtil", "json2Map exception: " + e.getMessage());
        }

        return map;
    }

    public static JSONObject map2json(Map<String, String> map) {
        JSONObject jo = new JSONObject();
        if (map == null) {
            return jo;
        }

        try {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                String value = map.get(key);
                jo.put(key, value);
            }
        } catch (Exception e) {
            Log.w("JsonUtil", "map2json exception: " + e.getMessage());
        }

        return jo;
    }

    public static String toJson(Object o) {
        if (o == null) {
            return "";
        }
        return new Gson().toJson(o);
    }
}
