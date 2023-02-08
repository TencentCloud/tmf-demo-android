package com.tencent.tmf.module.qapm.performance.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.crash.test.api.NativeCrashTest;
import com.tencent.tmf.module.qapm.R;
import java.util.ArrayList;

/**
 * Author: jinfazhang
 * Date: 2019/4/18 12:35
 */
public class CrashActivity extends TopBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btnTestRuntime = (Button) findViewById(R.id.btn_java_runtime);
        btnTestRuntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newJavaCrash();
            }
        });

        Button btnOOMCrash = (Button) findViewById(R.id.btn_oom);
        btnOOMCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOOMCrash();
            }
        });

        Button btnNullPoonter = (Button) findViewById(R.id.btn_null_pointer);
        btnNullPoonter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nullPoint();
            }
        });

        Button btnLoadSo = (Button) findViewById(R.id.btn_load_library);
        btnLoadSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSoError();
            }
        });

        Button btnNativeNullPointer = (Button) findViewById(R.id.btn_native_crash);
        btnNativeNullPointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.loadLibrary("performanceNativeCrashTest");
                new NativeCrashTest().testNativeCrash();
            }
        });

        mTopBar.setTitle(R.string.performance_test_crash);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_performance_crash, null);
    }

    private void onOOMCrash() {

        ArrayList<Object[]> objects = new ArrayList<>();
        int i = 0;
        while (true) {
            i++;
            if (i % 1000 == 0) {
                Log.d("total memory：", String.valueOf(Runtime.getRuntime().totalMemory()));
            }
            Object[] object = new Object[1024 * 1024 * 100];
            objects.add(object);
        }
    }

    private void nullPoint() {
        String a = null;
        a.equals("hahah");
    }

    private void loadSoError() {
        System.loadLibrary("aab");
    }

    private void newJavaCrash() {
        throw new RuntimeException("this is a crash demo.");
    }
}
