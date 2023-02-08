package com.tencent.tmf.common.log;

import android.util.Log;

import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.log.format.FormattingTuple;
import com.tencent.tmf.common.log.format.MessageFormatter;

public class L {
    private static final String TAG = Constant.TAG;

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String format, Object... objects) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, objects);
        Log.e(TAG, tuple.getMessage());
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void w(String format, Object... objects) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, objects);
        Log.w(TAG, tuple.getMessage());
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void d(String format, Object... objects) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, objects);
        Log.d(TAG, tuple.getMessage());
    }
}
