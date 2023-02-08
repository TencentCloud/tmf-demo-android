package com.tencent.tmf.demo.apshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.tencent.tmf.share.api.TMFShareService;

/**
 * 支付宝分享回调的activity
 * Author: jinfazhang
 * Date: 2019/4/22 22:00
 */
public class ShareEntryActivity extends Activity {

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper());
        TMFShareService.getInstance().handleAliPayIntent(getIntent());
        finishDelay();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        TMFShareService.getInstance().handleAliPayIntent(intent);
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
