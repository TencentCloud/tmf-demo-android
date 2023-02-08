package com.tencent.tmf.demo.ddshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.tencent.tmf.share.api.TMFShareService;

/**
 * 钉钉分享回调的activity
 */
public class DDShareActivity extends Activity {

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper());
        TMFShareService.getInstance().handleDingIntent(getIntent());
        finishDelay();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        TMFShareService.getInstance().handleDingIntent(intent);
        finishDelay();
    }

    private void finishDelay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 100L);
    }
}
