package com.tencent.tmf.module.hybrid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import androidx.annotation.Nullable;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.hybrid.EmbedView;
import com.tencent.tmf.hybrid.TMFHybridManager;

public class EmbedViewTestActivity extends TopBarActivity {

    private EmbedView embedView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String bidOrUrl = "";
        if (null != bundle) {
            bidOrUrl = bundle.getString("key_bid_url");
        }
        Log.e("SEED", "get bidOrUrl from intent : " + bidOrUrl);
        embedView = TMFHybridManager.getInstance().getOfflineContentView(this, bidOrUrl);
        FrameLayout frameLayout = findViewById(R.id.embed_view_container);
        frameLayout.addView(embedView,
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        embedView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        embedView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        embedView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        embedView.onDestroy();
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_embed_view_test, null, false);
    }
}
