package com.tencent.tmf.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tencent.tmf.common.ContextHolder;

public class ToastUtil {

    public static void showToast(final String toast) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ContextHolder.sContext, "" + toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
