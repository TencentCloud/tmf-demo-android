package com.tencent.tmf.module.qapm.performance.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.qapm.R;
import com.tencent.tmf.module.qapm.performance.Config;
import com.tencent.tmf.module.qapm.performance.GPSManager;
import com.tencent.tmf.module.qapm.performance.IOTest;


@Destination(
        url = "portal://com.tencent.tmf.module.qapm/performance-main-activity",
        launcher = Launcher.ACTIVITY,
        description = "qapm"
)
public class PerformanceMainActivity extends TopBarActivity implements View.OnClickListener {

    private static final String TAG = "PerformanceMainActivity";
    private IOTest mIoTest;
    private Handler mWorkHandler;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private GPSManager mGpsManager;
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.CHANGE_NETWORK_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
            };

            if (checkCallingOrSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 0);
            }
        }

        mIoTest = new IOTest(getApplicationContext());
        mGpsManager = new GPSManager(this);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "qapmapp::MyWakelockTag");

        if (null == mWorkHandler) {
            HandlerThread ht = new HandlerThread("TestMain");
            ht.start();
            mWorkHandler = new Handler(ht.getLooper());
            MainRun mainrun = new MainRun();
            mWorkHandler.postDelayed(mainrun, 1500);
        }

        mTopBar.setTitle(R.string.performance);
        findViewById(R.id.btntestdb).setOnClickListener(this);
        findViewById(R.id.btntestlistview).setOnClickListener(this);
        findViewById(R.id.btntestactivityleak).setOnClickListener(this);
        findViewById(R.id.btntestfile).setOnClickListener(this);
        findViewById(R.id.btntestbettary).setOnClickListener(this);
        findViewById(R.id.btnTestJavaCrash).setOnClickListener(this);
        findViewById(R.id.btnPrams).setOnClickListener(this);
    }

    private class MainRun implements Runnable {

        //启动后自动运行测试，目的是在wetest上能够保证基本功能都会执行到
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                mIoTest.sqLiteDatabaseTest();
                mIoTest.fileTest();
                testGetGps();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_performance_main, null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btntestdb) {
            mIoTest.sqLiteDatabaseTest();
            HandlerThread ht = new HandlerThread("DbTest");
            ht.start();
            Handler h = new Handler(ht.getLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 3; i++) {
                            mIoTest.sqLiteDatabaseTest();
                            Thread.sleep(1000);
                        }
                        Toast.makeText(PerformanceMainActivity.this, "test DB success", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (id == R.id.btntestlistview) {
            Intent intent = new Intent(this, DropFrameActivity.class);
            startActivity(intent);
        } else if (id == R.id.btntestactivityleak) {
            startActivity(new Intent(this, MemoryLeakActivity.class));
        } else if (id == R.id.btntestbettary) {
            testWakeLock();
            testGetGps();
            Toast.makeText(PerformanceMainActivity.this, "test battery success", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btntestfile) {
            mIoTest.fileTest();
            HandlerThread ht = new HandlerThread("IOTest");
            ht.start();
            Handler h = new Handler(ht.getLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 3; i++) {
                            mIoTest.fileTest();
                        }
                        Toast.makeText(PerformanceMainActivity.this, "test file io success", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (id == R.id.btnTestJavaCrash) {
            if (Config.IS_FOR_PRIVACY_CHECK) {
                return;
            }
            Intent intent = new Intent(this, CrashActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnPrams) {
            Intent intent = new Intent(this, ParamsActivity.class);
            startActivity(intent);
        }
//        switch (view.getId()) {
//            case R.id.btntestdb: {
//                mIoTest.SQLiteDatabaseTest();
//                HandlerThread ht = new HandlerThread("DbTest");
//                ht.start();
//                Handler h = new Handler(ht.getLooper());
//                h.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            for (int i = 0; i < 3; i++) {
//                                mIoTest.SQLiteDatabaseTest();
//                                Thread.sleep(1000);
//                            }
//                            Toast.makeText(PerformanceMainActivity.this, "测试DB成功", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                break;
//            }
//            case R.id.btntestlistview: {
//                Intent intent = new Intent(this, DropFrameActivity.class);
//                startActivity(intent);
//                break;
//            }
//            case R.id.btntestactivityleak: {
//                startActivity(new Intent(this, MemoryLeakActivity.class));
//                break;
//            }
//            case R.id.btntestbettary:
//                testWakeLock();
//                mGpsManager.getLocation();
//                Toast.makeText(PerformanceMainActivity.this, "测试电量成功", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btntestfile: {
//                mIoTest.FileTest();
//                HandlerThread ht = new HandlerThread("IOTest");
//                ht.start();
//                Handler h = new Handler(ht.getLooper());
//                h.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            for (int i = 0; i < 3; i++) {
//                                mIoTest.FileTest();
//                            }
//                            Toast.makeText(PerformanceMainActivity.this, "测试文件IO成功", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                break;
//            }
//            case R.id.btnTestJavaCrash: {
//                Intent intent = new Intent(this, CrashActivity.class);
//                startActivity(intent);
//                break;
//            }
//        }
    }

    private void testWakeLock() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = powerManager
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "apmtest::MyWakelockTag");
        try {
            wakeLock.acquire();
            mWorkHandler.postDelayed(new Runnable() {
                public void run() {
                    wakeLock.release();
                }
            }, 5000);
            final WifiManager.WifiLock wifiLock = ((WifiManager) getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
            wifiLock.acquire();
            mWorkHandler.postDelayed(new Runnable() {
                public void run() {
                    wifiLock.release();
                }
            }, 9000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * TMFDemo 隐私合规改造
     */
    private void testGetGps() {
        if (!Config.IS_FOR_PRIVACY_CHECK) {
            mGpsManager.getLocation();
        }
    }
}
