package com.tencent.tmf.module.icdp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.icdp.HotSplashManager;
import com.tencent.tmf.icdp.HotSplashObserver;
import com.tencent.tmf.icdp.ICDPBannerView;
import com.tencent.tmf.icdp.ICDPBroadcastView;
import com.tencent.tmf.icdp.ICDPImageView;
import com.tencent.tmf.icdp.ICDPListView;
import com.tencent.tmf.icdp.ICDPOnAdClickListener;
import com.tencent.tmf.icdp.ICDPOnLoadBoothInfoListener;
import com.tencent.tmf.icdp.ICDPPopupView;
import com.tencent.tmf.icdp.ICDPWebViewActivity;
import com.tencent.tmf.icdp.utils.DpUtils;

public class BoothActivity extends AppCompatActivity {

    public static final String INTENT_KEY_TYPE = "INTENT_KEY_TYPE";
    public static final String INTENT_TYPE_VALUE_BROADCAST = "broadcast";
    public static final String INTENT_TYPE_VALUE_IMAGE = "image";
    public static final String INTENT_TYPE_VALUE_BANNER = "banner";
    public static final String INTENT_TYPE_VALUE_POPUP = "popup";
    public static final String INTENT_TYPE_VALUE_LIST = "list";
    public static final String INTENT_TYPE_VALUE_HOT_SPLASH = "hot_splash";

    private AppCompatActivity activity;
    private FrameLayout container;


    private static String TAG = "BoothActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        container = new FrameLayout(activity);
        container.setPadding(15, 25, 15, 15);
        setContentView(container);
        init();
    }

    private void init() {
        String intentKeyType = getIntent().getStringExtra(INTENT_KEY_TYPE);

        switch (intentKeyType) {
            case INTENT_TYPE_VALUE_BROADCAST:
                showBroadCastView();
                break;
            case INTENT_TYPE_VALUE_IMAGE:
                showImageView();
                break;
            case INTENT_TYPE_VALUE_BANNER:
                showBannerView();
                break;
            case INTENT_TYPE_VALUE_POPUP:
                showPopView();
                break;
            case INTENT_TYPE_VALUE_LIST:
                showListView();
                break;
            case INTENT_TYPE_VALUE_HOT_SPLASH:
                showHotSplash();
                break;
            default:
                Log.e(TAG, "error type");
                break;

        }

    }

    private void showHotSplash() {
        TextView textView = new TextView(activity);
        textView.setTextColor(Color.BLACK);
        String title = getResources().getString(R.string.icdp_booth_activity_tips);

        textView.setText(title);
        textView.setTextSize(20F);
        container.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, DpUtils.dp2px(this, 200f));
    }

    private void showListView() {
        ICDPListView icdpListView = new ICDPListView(activity);
        icdpListView.setBoothIdentityId(Config.getInstance(activity).getList());
        container.addView(icdpListView, ViewGroup.LayoutParams.MATCH_PARENT, DpUtils.dp2px(this, 200f));
        icdpListView.setOnAdClickListener(icdpOnAdClickListener);
    }


    // 关闭白板 TODO
    private void showPopView() {
        ICDPPopupView.showAsDialogFragmentAsync(Config.getInstance(activity).getPopup(), activity,
                new ICDPPopupView.DialogListener() {

                    @Override
                    public boolean onShowDialog(@NonNull ICDPPopupView.DialogFragment dialogFragment) {

                        runOnUiThread(() -> {
                            dialogFragment.show(getSupportFragmentManager(), "");
                            dialogFragment.setOnAdClickListener(icdpOnAdClickListener);
                            dialogFragment.setOnCloseClickListener((boothIdentityId, view) -> {
                                Log.w(TAG, "pop click close,boothIdentityId:" + boothIdentityId);
                                finish();
                            });
                        });
                        return false;
                    }

                    @Override
                    public void onFailed(String reason) {
                        Log.e(TAG, "onFailed:" + reason);
                    }
                });
    }

    private void showBannerView() {
        ICDPBannerView icdpBannerView = new ICDPBannerView(activity);
        icdpBannerView.setBoothIdentityId(Config.getInstance(activity).getBanner());
        container.addView(icdpBannerView, ViewGroup.LayoutParams.MATCH_PARENT, DpUtils.dp2px(this, 150f));
        icdpBannerView.setOnAdClickListener(icdpOnAdClickListener);
        icdpBannerView.setOnLoadBoothInfoListener(iCDPOnLoadBoothInfoListener);
    }

    private void showImageView() {
        ICDPImageView icdpImageView = new ICDPImageView(activity);
        icdpImageView.setBoothIdentityId(Config.getInstance(activity).getImage());
        container.addView(icdpImageView, ViewGroup.LayoutParams.MATCH_PARENT, DpUtils.dp2px(this, 300));
        icdpImageView.setOnAdClickListener(icdpOnAdClickListener);
        icdpImageView.setOnLoadBoothInfoListener(iCDPOnLoadBoothInfoListener);
    }

    ICDPOnLoadBoothInfoListener iCDPOnLoadBoothInfoListener = new ICDPOnLoadBoothInfoListener() {

        @Override
        public void onLoadComplete(@NonNull String boothIdentityId, @NonNull View icdpView) {
            Log.d(TAG, "onLoadComplete boothIdentityId:" + boothIdentityId);
        }

        @Override
        public void onLoadFailed(@NonNull String boothIdentityId, @NonNull View view, @NonNull String reason) {
            Log.d(TAG, "onLoadFailed boothIdentityId:" + boothIdentityId + " reason:" + reason);
        }
    };

    ICDPOnAdClickListener icdpOnAdClickListener = (boothIdentityId, url, icdpInstance, ext) -> {
        Log.e(TAG, "click boothIdentityId:" + boothIdentityId);
        startWebActivity(url);
    };


    private void showBroadCastView() {
        ICDPBroadcastView icdpBroadcastView = new ICDPBroadcastView(activity);
        icdpBroadcastView.setBoothIdentityId(Config.getInstance(activity).getBroadcast());
        container.addView(icdpBroadcastView, ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(activity, 30));
        icdpBroadcastView.setOnAdClickListener((boothIdentityId, url, icdpInstance, ext) -> {
            Log.d(TAG, "click announce");
            startWebActivity(url);
        });
    }

    private void startWebActivity(String url) {
        Intent intent = new Intent(activity, ICDPWebViewActivity.class);
        intent.putExtra(ICDPWebViewActivity.KEY_URL, url);
        startActivity(intent);
    }
}
