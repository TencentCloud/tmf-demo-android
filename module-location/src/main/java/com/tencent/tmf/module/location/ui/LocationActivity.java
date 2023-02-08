package com.tencent.tmf.module.location.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.amap.api.location.AMapLocationListener;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.location.R;

@Destination(
        url = "portal://com.tencent.tmf.module.location/location-activity",
        launcher = Launcher.ACTIVITY,
        description = "定位"
)
public class LocationActivity extends TopBarActivity implements View.OnClickListener {

    private static final String TAG = "TMF_LocationActivity";

    private QMUICommonListItemView cbAMap;
    private QMUICommonListItemView cbTencent;
    private TextView tvResult;
    private AMapLocationDelegate aMapLocationDelegate = new AMapLocationDelegate();
    private TMapLocationDelegate tMapLocationDelegate = TMapLocationDelegate.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getResources().getString(R.string.location_module));
        findViewById(R.id.btn_current_location).setOnClickListener(this);
        findViewById(R.id.btn_last_location).setOnClickListener(this);
        findViewById(R.id.btn_start_location).setOnClickListener(this);
        findViewById(R.id.btn_stop_location).setOnClickListener(this);
        tvResult = findViewById(R.id.tv_result);

        cbAMap = findViewById(R.id.checkBox_amap);
        String amp = getResources().getString(R.string.locate_a_map);
        cbAMap.setText(amp);
        cbAMap.getSwitch().setChecked(false);
        cbAMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbAMap.getSwitch().toggle();
                cbTencent.getSwitch().toggle();
                tvResult.setText("");
                aMapLocationDelegate.stopLocation();
                tMapLocationDelegate.stopLocation();
            }
        });

        cbTencent = findViewById(R.id.checkBox_tencent);
        String tmp = getResources().getString(R.string.locate_t_map);
        cbTencent.setText(tmp);
        cbTencent.getSwitch().setChecked(true);
        cbTencent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbAMap.getSwitch().toggle();
                cbTencent.getSwitch().toggle();
                tvResult.setText("");
                aMapLocationDelegate.stopLocation();
                tMapLocationDelegate.stopLocation();
            }
        });

        aMapLocationDelegate.initLocation(this, aMapLocationListener);
        tMapLocationDelegate.initLocation(this, tencentLocationListener);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_location_demo, null);
    }

    @Override
    public void onClick(View v) {
        tvResult.setText("");
        int id = v.getId();
        if (id == R.id.btn_current_location) {
            if (cbAMap.getSwitch().isChecked()) {
                aMapLocationDelegate.getCurrentLocation();
            } else if (cbTencent.getSwitch().isChecked()) {
                tMapLocationDelegate.getCurrentLocation();
            }
        } else if (id == R.id.btn_last_location) {
            String lastLocation = "";
            if (cbAMap.getSwitch().isChecked()) {
                lastLocation = aMapLocationDelegate.getLastLocation();
            } else if (cbTencent.getSwitch().isChecked()) {
                lastLocation = tMapLocationDelegate.getLastLocation();
            }
            tvResult.setText(lastLocation);
        } else if (id == R.id.btn_start_location) {
            if (cbAMap.getSwitch().isChecked()) {
                aMapLocationDelegate.startLocation();
            } else if (cbTencent.getSwitch().isChecked()) {
                tMapLocationDelegate.startLocation();
            }
        } else if (id == R.id.btn_stop_location) {
            if (cbAMap.getSwitch().isChecked()) {
                aMapLocationDelegate.stopLocation();
            } else if (cbTencent.getSwitch().isChecked()) {
                tMapLocationDelegate.stopLocation();
            }
        }
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }

    /**
     * 定位监听
     */
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(com.amap.api.location.AMapLocation location) {
            if (null != location) {
                tvResult.setText(AMapUtil.getLocationStr(location));
            } else {
                tvResult.setText(getStringById(R.string.locate_failed)+"loc is null");
            }
        }
    };

    TencentLocationListener tencentLocationListener = new TencentLocationListener() {
        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
            if (error == TencentLocation.ERROR_OK) {
                // 定位成功
                if (null != tencentLocation) {
                    tvResult.setText(TencentMapUtil.getLocationStr(tencentLocation));
                }
            } else {
                // 定位失败
                tvResult.setText(getStringById(R.string.locate_failed) + reason);
            }
        }

        @Override
        public void onStatusUpdate(String name, int status, String desc) {
            String message = "{name=" + name + ", new status=" + status + ", desc=" + desc + "}";

            if (status == STATUS_DENIED) {
                // 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用很可能无法定位
                // 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
                Log.d(TAG, "locate permission not given!");
            } else {
                Log.d(TAG, message);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapLocationDelegate.stopLocation();
        aMapLocationDelegate.destroyLocation();
        tMapLocationDelegate.stopLocation();
        tMapLocationDelegate.destroyLocation();
    }

}
