package com.tencent.tmf.module.icdp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.icdp.ICDPOnAdClickListener;
import com.tencent.tmf.icdp.ICDPOnLoadBoothInfoListener;
import com.tencent.tmf.icdp.ICDPSplashActivity;

import java.lang.ref.WeakReference;

public class AppDemoActivity extends TopBarActivity {

    private Activity activity;
    private static String TAG = "AppDemoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        String title = getResources().getString(R.string.icdp_title);
        mTopBar.setTitle(title);
        initView();
    }

    private void initView() {

        findViewById(R.id.item_online).setOnClickListener(v -> {
            startActivity(new Intent(activity, AdOnlineActivity.class));
        });

        findViewById(R.id.item_params)
                .setOnClickListener(v -> startActivity(new Intent(activity, ParamsActivity.class)));

        findViewById(R.id.item_splash).setOnClickListener(v -> {
            startSplashActivity();
        });
        findViewById(R.id.item_broadcast).setOnClickListener(v -> {
            Intent intent = new Intent(activity, BoothActivity.class);
            intent.putExtra(BoothActivity.INTENT_KEY_TYPE, BoothActivity.INTENT_TYPE_VALUE_BROADCAST);
            startActivity(intent);
        });
        findViewById(R.id.item_image).setOnClickListener(v -> {
            Intent intent = new Intent(activity, BoothActivity.class);
            intent.putExtra(BoothActivity.INTENT_KEY_TYPE, BoothActivity.INTENT_TYPE_VALUE_IMAGE);
            startActivity(intent);
        });
        findViewById(R.id.item_banner).setOnClickListener(v -> {
            Intent intent = new Intent(activity, BoothActivity.class);
            intent.putExtra(BoothActivity.INTENT_KEY_TYPE, BoothActivity.INTENT_TYPE_VALUE_BANNER);
            startActivity(intent);
        });
        findViewById(R.id.item_popup).setOnClickListener(v -> {
            Intent intent = new Intent(activity, BoothActivity.class);
            intent.putExtra(BoothActivity.INTENT_KEY_TYPE, BoothActivity.INTENT_TYPE_VALUE_POPUP);
            startActivity(intent);
        });

        findViewById(R.id.item_list).setOnClickListener(v -> {
            Intent intent = new Intent(activity, BoothActivity.class);
            intent.putExtra(BoothActivity.INTENT_KEY_TYPE, BoothActivity.INTENT_TYPE_VALUE_LIST);
            startActivity(intent);
        });
        findViewById(R.id.item_hot_splash).setOnClickListener(v -> {
            Intent intent = new Intent(activity, BoothActivity.class);
            intent.putExtra(BoothActivity.INTENT_KEY_TYPE, BoothActivity.INTENT_TYPE_VALUE_HOT_SPLASH);
            startActivity(intent);
        });


    }


    class SplashListener implements ICDPOnAdClickListener, ICDPOnLoadBoothInfoListener {

        @Override
        public void onClick(@NonNull String boothIdentityId, @Nullable String url, @NonNull View view,
                String icdpInstance) {
            Log.d(TAG, "onClick boothIdentityId:" + boothIdentityId + " url:" + url);
        }

        @Override
        public void onLoadComplete(@NonNull String boothIdentityId, @NonNull View icdpView) {
            Log.i(TAG, "onLoadComplete boothIdentityId:" + boothIdentityId);
        }

        @Override
        public void onLoadFailed(@NonNull String boothIdentityId, @NonNull View icdpView, @NonNull String reason) {
            Log.e(TAG, "onLoadFailed boothIdentityId:" + boothIdentityId + " reason:" + reason);
        }
    }

    private void startSplashActivity() {
        SplashListener splashListener = new SplashListener();
        ICDPSplashActivity.registerOnAdClickListener(new WeakReference<>(splashListener));
        ICDPSplashActivity.registerOnLoadBoothInfoListener(new WeakReference<>(splashListener));
        Intent intent = new Intent(activity, ICDPSplashActivity.class);
        intent.putExtra(ICDPSplashActivity.EXTRA_KEY_BOOTH_ID, Config.getInstance(activity).getSplash());
        startActivity(intent);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.demo_activity_app, null);
    }
}
