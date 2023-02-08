package com.tencent.tmf.module.location.ui;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.util.Log;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * 腾讯地图定位代理
 * Created by winnieyzhou on 2019/5/7.
 */
public class TMapLocationDelegate {

    private static final String TAG = "TMF_Location";

    private TencentLocationManager mTencentLocationManager;
    private TencentLocationRequest mTencentLocationRequest;
    private TencentLocationListener mTencentLocationListener;

    private TMapLocationDelegate() {

    }

    private static class Holder {

        private static TMapLocationDelegate INSTANCE = new TMapLocationDelegate();
    }

    public static TMapLocationDelegate getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 初始化定位
     */
    public void initLocation(Context context, TencentLocationListener locationListener) {
        Log.i(TAG, "initLocation");
        // 初始化
        if (mTencentLocationManager == null) {
            mTencentLocationManager = TencentLocationManager.getInstance(context);
        }
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mTencentLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        // 设置 wgs84 坐标系。无网络 + 有GPS 条件下, 使用 WGS84 坐标可定位, 而使用 GCJ-02 坐标无法定位!
//        mTencentLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_WGS84);
        mTencentLocationRequest = getDefaultRequest();
        mTencentLocationListener = locationListener;
    }

    /**
     * 默认的定位参数
     */
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

    /**
     * 开始定位
     */
    public void startLocation() {
        Log.i(TAG, "startLocation");
        if (null != mTencentLocationManager) {
            mTencentLocationManager
                    .requestLocationUpdates(mTencentLocationRequest, mTencentLocationListener, getMainLooper());
        } else {
            Log.e(TAG, "mTencentLocationManager is already null");
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        Log.i(TAG, "stopLocation");
        if (null != mTencentLocationManager) {
            mTencentLocationManager.removeUpdates(mTencentLocationListener);
        } else {
            Log.e(TAG, "mTencentLocationManager is already null");
        }
    }

    /**
     * 销毁定位
     */
    public void destroyLocation() {
        Log.i(TAG, "destroyLocation");
        if (null != mTencentLocationManager) {
            mTencentLocationManager = null;
        }
    }

    /**
     * 获取当前定位（单次定位）
     */
    public void getCurrentLocation() {
        Log.i(TAG, "getCurrentLocation");
        // 获取当前单次定位，该方法可在未启动周期性回调时获取一次最新位置
        // 也可以在启动周期性回调时，强制刷新得到一次最新的位置信息
        // 该接口在单次定位时，获取位置结果为1号定位接口(REQUEST_LEVEL_NAME)
        // 在启动周期性回调时，结果跟随TencentLocationRequest中设置的定位接口号码。
        mTencentLocationManager
                .requestSingleFreshLocation(mTencentLocationRequest, mTencentLocationListener, getMainLooper());
    }

    /**
     * 获取最后一次定位
     */
    public String getLastLocation() {
        Log.i(TAG, "getLastLocation");
        if (null == mTencentLocationManager) {
            Log.e(TAG, "mTencentLocationManager is already null");
            return "";
        }
        return TencentMapUtil.getLocationStr(mTencentLocationManager.getLastKnownLocation());
    }
}
