package com.tencent.tmf.module.location.ui;

import android.content.Context;
import android.util.Log;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 高德地图定位代理
 * Created by winnieyzhou on 2019/5/7.
 */
public class AMapLocationDelegate {

    private static final String TAG = "TMF_Location";

    private AMapLocationClient mAMapLocationClient = null;
    private AMapLocationClientOption mAMapLocationClientOption = null;
    private AMapLocationListener mAMapLocationListener = null;

    public AMapLocationDelegate() {
    }

    /**
     * 初始化定位
     */
    public void initLocation(Context context, AMapLocationListener locationListener) {
        Log.i(TAG, "initLocation");
        //初始化client
        if (mAMapLocationClient == null) {
            mAMapLocationClient = new AMapLocationClient(context);
        }
        mAMapLocationListener = locationListener;
        mAMapLocationClientOption = getDefaultOption();
        // 设置定位监听
        mAMapLocationClient.setLocationListener(mAMapLocationListener);
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(
                AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(
                AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        Log.i(TAG, "startLocation");
        if (null != mAMapLocationClient) {
            // 设置定位参数
            mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
            // 启动定位
            mAMapLocationClient.startLocation();
        } else {
            Log.e(TAG, "mAMapLocationClient is already null");
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        Log.i(TAG, "stopLocation");
        if (null != mAMapLocationClient) {
            mAMapLocationClient.stopLocation();
        } else {
            Log.e(TAG, "mAMapLocationClient is already null");
        }
    }

    /**
     * 销毁定位
     */
    public void destroyLocation() {
        Log.i(TAG, "destroyLocation");
        if (null != mAMapLocationClient) {
            /*
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mAMapLocationClient.onDestroy();
            mAMapLocationClient = null;
            mAMapLocationClientOption = null;
        }
    }

    /**
     * 获取当前定位（单次定位）
     */
    public void getCurrentLocation() {
        Log.i(TAG, "getCurrentLocation");
        if (null != mAMapLocationClient) {
            AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
            aMapLocationClientOption.setOnceLocation(true);
            // 设置定位参数
            mAMapLocationClient.setLocationOption(aMapLocationClientOption);
            // 启动定位
            mAMapLocationClient.startLocation();
        } else {
            Log.e(TAG, "mAMapLocationClient is already null");
        }
    }

    /**
     * 获取最后一次定位
     */
    public String getLastLocation() {
        Log.i(TAG, "getLastLocation");
        if (null == mAMapLocationClient) {
            Log.e(TAG, "mAMapLocationClient is already null");
            return "";
        }
        return AMapUtil.getLocationStr(mAMapLocationClient.getLastKnownLocation());
    }
}
