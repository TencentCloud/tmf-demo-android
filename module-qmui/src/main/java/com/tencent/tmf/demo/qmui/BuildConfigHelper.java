package com.tencent.tmf.demo.qmui;

import java.lang.reflect.Field;

public class BuildConfigHelper {

    private static Boolean sIsDebug = null;

    public static boolean getLogDebugValue() {
        if (sIsDebug == null) {
            try {
                Class<?> clazz = Class.forName(UIContextHolder.sContext.getPackageName() + ".BuildConfig");
                Field field = clazz.getField("LOG_DEBUG");
                sIsDebug = (Boolean) field.get(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sIsDebug != null ? sIsDebug : false;
    }
}
