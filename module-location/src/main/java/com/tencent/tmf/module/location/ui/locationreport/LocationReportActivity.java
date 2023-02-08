package com.tencent.tmf.module.location.ui.locationreport;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.location.R;
import com.tencent.tmf.module.location.ui.AMapH5LocationActivity;
import com.tencent.tmf.module.location.ui.LocationActivity;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Destination(
        url = "portal://com.tencent.tmf.module.location/location-report-activity",
        launcher = Launcher.ACTIVITY,
        description = "定位"
)
public class LocationReportActivity extends TopBarActivity implements View.OnClickListener {

    private static final String TAG = "TMF_LocationReport";

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSION_REQUESTCODE = 0;
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.location_module));
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }
        findViewById(R.id.btn_profile_report).setOnClickListener(this);
        findViewById(R.id.btn_location_demo).setOnClickListener(this);
        findViewById(R.id.btn_map_location_demo).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_location_main, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_profile_report) {
            ProfileLocationReporter.getInstance().reportLocationToProfile(this);
            String amp = getResources().getString(R.string.locate_report_to_profile);
            Toast.makeText(this, amp, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_location_demo) {
            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_map_location_demo) {
            Intent intent = new Intent(this, AMapH5LocationActivity.class);
            startActivity(intent);
        }
    }

    //----------以下权限相关---------------

    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});

                    method.invoke(this, array, PERMISSION_REQUESTCODE);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            try {
                for (String perm : permissions) {
                    Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = getClass()
                            .getMethod("shouldShowRequestPermissionRationale",
                                    String.class);
                    Object checkSelfObject = checkSelfMethod.invoke(this, perm);
                    Object shouldShowRequestObject = shouldShowRequestPermissionRationaleMethod.invoke(this, perm);
                    if ((null != checkSelfObject && (Integer) checkSelfObject != PackageManager.PERMISSION_GRANTED)
                            || (null != shouldShowRequestObject && (Boolean) shouldShowRequestObject)) {
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
            String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSION_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
