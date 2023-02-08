package com.tencent.tmf.stat;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.profile.api.ProfileManager;
import com.tencent.tmf.stat.api.Address;
import com.tencent.tmf.stat.api.TMFStatService;

@Destination(
        url = "portal://com.tencent.tmf.stat/analyse-activity",
        launcher = Launcher.ACTIVITY,
        description = "TMF移动分析页面"
)
public class TestMainActivity extends TopBarActivity implements OnClickListener {

    private TencentLocationManager mTencentLocationManager;
    private TencentLocationRequest mTencentLocationRequest;
    private TencentLocationListener mTencentLocationListener;

    private String mCountry = "";
    private String mProvince = "";
    private String mCity = "";
    private boolean mHasLocation = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getString(R.string.analyse_test_main_3));
        mCountry = getString(R.string.analyse_test_main_0);
        mProvince = getString(R.string.analyse_test_main_1);
        mCity = getString(R.string.analyse_test_main_2);
        initLocation(this);
        initClickEvent();
        plantPageObserver();
        ((TextView) findViewById(R.id.set_default_location))
                .setText(String.format(getString(R.string.analyse_set_default_location), mCountry, mProvince, mCity));
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_test_main, null);
    }

    public void initLocation(Context context) {
        // 初始化
        if (mTencentLocationManager == null) {
            mTencentLocationManager = TencentLocationManager.getInstance(context);
        }
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mTencentLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        // 设置 wgs84 坐标系。无网络 + 有GPS 条件下, 使用 WGS84 坐标可定位, 而使用 GCJ-02 坐标无法定位!
//        mTencentLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_WGS84);
        mTencentLocationRequest = getDefaultRequest();
        mTencentLocationListener = new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
                mTencentLocationManager.removeUpdates(mTencentLocationListener);
                if (tencentLocation != null) {
                    mHasLocation = true;
                    mCountry = tencentLocation.getNation();
                    mProvince = tencentLocation.getProvince();
                    mCity = tencentLocation.getCity();
                    ((TextView) findViewById(R.id.set_default_location)).setText(
                            String.format(getString(R.string.analyse_set_default_location), mCountry, mProvince,
                                    mCity));
                }
            }

            @Override
            public void onStatusUpdate(String name, int status, String desc) {
                if (status == STATUS_DENIED) {
                    // 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用很可能无法定位
                    // 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
                    showToast(getString(R.string.analyse_test_main_4));
                }
            }
        };
        if (isLocationEnabled(this)) {
            mTencentLocationManager
                    .requestSingleFreshLocation(mTencentLocationRequest, mTencentLocationListener, getMainLooper());
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(getString(R.string.analyse_test_main_5));
                }
            });
        }
    }

    private TencentLocationRequest getDefaultRequest() {
        TencentLocationRequest mRequest = TencentLocationRequest.create();
        //REQUEST_LEVEL_ADMIN_AREA:3号定位接口, 包含经纬度, 行政区划，位置地址和位置名称.
        //REQUEST_LEVEL_GEO: 0号定位接口, 仅包含经纬度坐标表示的地位置(经纬度).
        //REQUEST_LEVEL_NAME: 1号定位接口, 包含经纬度, 位置名称, 位置地址.
        //REQUEST_LEVEL_POI: 4号定位接口, 包含经纬度, 行政区划, 附近的POI.
        mRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);//可选，设置定位Level
        mRequest.setAllowGPS(true);//可选，设置是否允许使用GPS进行定位,默认允许.
        mRequest.setInterval(2000);//可选，设置定位间隔。默认为10秒,建议将 定位周期 设置为 5000-10000ms
        mRequest.setAllowDirection(false);//可选，设置是否使用传感器。默认是false
        //  mRequest.setAllowCache(true); //可选，设置是否使用缓存定位，默认为true
        return mRequest;
    }

    private void initClickEvent() {
        findViewById(R.id.click_login).setOnClickListener(this);
        findViewById(R.id.click_logout).setOnClickListener(this);
        findViewById(R.id.click_app_use_time).setOnClickListener(this);
        findViewById(R.id.click_open_page).setOnClickListener(this);
        findViewById(R.id.click_start_app).setOnClickListener(this);
        findViewById(R.id.set_default_location).setOnClickListener(this);
        findViewById(R.id.custom_event_report).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.click_login) {
            EditText editText = findViewById(R.id.et_input_value);
            TMFStatService.login(editText.getText().toString().trim());
            showToast(getString(R.string.analyse_test_main_6) + editText.getText().toString().trim());
        } else if (id == R.id.click_logout) {
            TMFStatService.login("");
            showToast(getString(R.string.analyse_test_main_7));
        } else if (id == R.id.click_app_use_time) {//模拟上报一个应用使用时长事件use_time
        } else if (id == R.id.click_open_page) {
            Intent intent = new Intent(getApplicationContext(), PageJumpActivity.class);
            startActivity(intent);
        } else if (id == R.id.click_start_app) {//模拟上报一个打开页面时长的事件page_view

        } else if (id == R.id.set_default_location) {
            if (mHasLocation) {
                Address address = new Address(mCountry, mProvince, mCity);
                TMFStatService.setAddress(address);
                showToast(getString(R.string.analyse_test_main_8) + mCountry + ":" + mProvince + ":" + mCity);
                ProfileManager.setLocation(mCountry, mProvince, mCity);
            } else {
                showToast(getString(R.string.analyse_test_main_9));
            }
        } else if (id == R.id.custom_event_report) {
            Intent intent = new Intent(getApplicationContext(), AnalyseCustomEventReportActivity.class);
            startActivity(intent);
        }
    }

    private void plantPageObserver() {
        TMFStatService.setPageId(this, TestMainActivity.class.getSimpleName());
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private static boolean isLocationEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null) {
            boolean gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean network = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            return gps || network;
        }
        return false;
    }
}
