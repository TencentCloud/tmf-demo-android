package com.tencent.tmf.stat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.stat.api.TMFStatService;

public class PageJumpActivity extends TopBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plantPageObserver();
        mTopBar.setTitle(getString(R.string.analyse_page_jump_title));
        findViewById(R.id.jump_back).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TestMainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_page_jump, null);
    }

    private void plantPageObserver() {
        TMFStatService.setPageId(this, PageJumpActivity.class.getSimpleName());
    }
}
