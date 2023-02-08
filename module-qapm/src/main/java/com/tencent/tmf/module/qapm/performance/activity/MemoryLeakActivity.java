package com.tencent.tmf.module.qapm.performance.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.annotation.Nullable;

import com.tencent.tmf.module.qapm.R;

/**
 * Author: jinfazhang
 * Date: 2019/4/16 21:37
 */
public class MemoryLeakActivity extends Activity {

    private static ArrayList<Activity> sLeakList = new ArrayList<Activity>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_performance_memory_leak);

        sLeakList.add(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //此处直接退出页面，制造的泄漏
                finish();
                Toast.makeText(MemoryLeakActivity.this, "TMFMemoryLeakActivity memory leak", Toast.LENGTH_SHORT).show();
            }
        }, 1000L);
    }
}
