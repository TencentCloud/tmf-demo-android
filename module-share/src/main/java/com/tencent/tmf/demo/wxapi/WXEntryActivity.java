package com.tencent.tmf.demo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.tencent.tmf.share.api.TMFShareService;

import androidx.annotation.Nullable;

/**
 * 微信分享回调的activity
 * Author: jinfazhang
 * Date: 2019/4/22 22:00
 */
public class WXEntryActivity extends Activity {

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper());
        TMFShareService.getInstance().handleWxIntent(getIntent());
        finishDelay();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        TMFShareService.getInstance().handleWxIntent(intent);
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
