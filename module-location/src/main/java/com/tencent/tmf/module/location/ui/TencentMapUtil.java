package com.tencent.tmf.module.location.ui;

import static com.tencent.map.geolocation.TencentLocation.GPS_PROVIDER;
import static com.tencent.map.geolocation.TencentLocationListener.GPS;
import static com.tencent.map.geolocation.TencentLocationListener.STATUS_DENIED;
import static com.tencent.map.geolocation.TencentLocationListener.STATUS_DISABLED;
import static com.tencent.map.geolocation.TencentLocationListener.STATUS_ENABLED;
import static com.tencent.map.geolocation.TencentLocationListener.STATUS_GPS_AVAILABLE;
import static com.tencent.map.geolocation.TencentLocationListener.STATUS_GPS_UNAVAILABLE;
import static com.tencent.map.geolocation.TencentLocationListener.STATUS_LOCATION_SWITCH_OFF;
import static com.tencent.map.geolocation.TencentLocationListener.WIFI;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentPoi;
import com.tencent.tmf.module.location.R;
import java.util.List;

/**
 * 辅助工具类
 */
public class TencentMapUtil {

    /**
     * 根据定位结果返回定位信息的字符串
     * @param location
     * @return
     */
    public static synchronized String getLocationStr(TencentLocation location) {
        if (null == location) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(getStringById(R.string.locate_success) + "\n");
        sb.append(getStringById(R.string.locate_longitude) + location.getLongitude() + "\n");
        sb.append(getStringById(R.string.locate_latitude) + location.getLatitude() + "\n");
        sb.append(getStringById(R.string.locate_currency) + location.getAccuracy() + getStringById(R.string.locate_meter) + "\n");
        sb.append(getStringById(R.string.locate_provider) + location.getProvider() + "\n");
        if (location.getProvider().equals(GPS_PROVIDER)) {
            sb.append(getStringById(R.string.locate_gps_signal)).append(getGPSStatusString(location.getGPSRssi())).append("\n");
        }

        sb.append(getStringById(R.string.locate_speed) + location.getSpeed() + getStringById(R.string.locate_miles_per_second) + "\n");
        sb.append(getStringById(R.string.locate_degree) + location.getBearing() + "\n");
        // 获取当前提供定位服务的卫星个数
        sb.append(getStringById(R.string.locate_zbx) + location.getCoordinateType() + "\n");
        sb.append(getStringById(R.string.locate_direction) + location.getDirection() + "\n");
        sb.append(getStringById(R.string.locate_country) + location.getNation() + "\n");
        sb.append(getStringById(R.string.locate_province) + location.getProvince() + "\n");
        sb.append(getStringById(R.string.locate_city) + location.getCity() + "\n");
        sb.append(getStringById(R.string.locate_city_code) + location.getCityCode() + "\n");
        sb.append(getStringById(R.string.locate_city_distinct) + location.getDistrict() + "\n");
        sb.append(getStringById(R.string.locate_city_distinct_code) + location.getAreaStat() + "\n");
        sb.append(getStringById(R.string.locate_street)+ location.getStreet() + "\n");
        sb.append(getStringById(R.string.locate_street_code) + location.getStreetNo() + "\n");
        sb.append(getStringById(R.string.locate_town) + location.getTown() + "\n");
        sb.append(getStringById(R.string.locate_villege) + location.getVillage() + "\n");
        sb.append(getStringById(R.string.locate_city_address) + location.getAddress() + "\n");
        sb.append(getStringById(R.string.locate_name) + location.getName() + "\n");
        sb.append(getStringById(R.string.locate_city_poi) + getPoiInfo(location) + "\n");

        //定位完成的时间
        sb.append(getStringById(R.string.locate_time) + Util.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        //定位之后的回调时间
        sb.append(getStringById(R.string.locate_callback_time) + Util.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

        //解析定位结果，
        return sb.toString();
    }

    private static String getPoiInfo(TencentLocation location) {
        StringBuffer sb = new StringBuffer();
        List<TencentPoi> poiList = location.getPoiList();
        int size = poiList.size();
        for (int i = 0, limit = 3; i < limit && i < size; i++) {
            sb.append("\n");
            sb.append("poi[" + i + "]=")
                    .append(poiList.get(i).toString()).append(",");
        }
        return sb.toString();
    }

    public static String getLocationStatus(String name, int status) {
        StringBuffer sb = new StringBuffer();
        if (WIFI.equals(name)) {
            sb.append("WiFi状态：");
            switch (status) {
                case STATUS_DENIED:
                    sb.append("无WIFI权限").append("\n");
                    break;
                case STATUS_DISABLED:
                    sb.append("WIFI未开启").append("\n");
                    break;
                case STATUS_ENABLED:
                    sb.append("WIFI已开启").append("\n");
                    break;
                case STATUS_LOCATION_SWITCH_OFF:
                    sb.append("定位服务关闭").append("\n");
                    break;
                default:
                    break;
            }
        } else if (GPS.equals(name)) {
            sb.append("GPS状态：");
            switch (status) {
                case STATUS_DENIED:
                    sb.append("无GPS权限");
                    break;
                case STATUS_DISABLED:
                    sb.append("GPS未开启");
                    break;
                case STATUS_ENABLED:
                    sb.append("GPS已开启");
                    break;
                case STATUS_GPS_UNAVAILABLE:
                    sb.append("GPS不可用");
                    break;
                case STATUS_GPS_AVAILABLE:
                    sb.append("GPS可用");
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 获取GPS状态的字符串
     * @param statusCode GPS状态码
     * @return
     */
    private static String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case 0:
                str = getStringById(R.string.locate_gps_status_no);
                break;
            case 1:
                str = getStringById(R.string.locate_gps_status_weak);
                break;
            case 2:
                str = getStringById(R.string.locate_gps_status_mid);
                break;
            case 3:
                str = getStringById(R.string.locate_gps_status_strong);
                break;
            default:
                break;
        }
        return str;
    }

    /**
     * 返回坐标系名称
     */
    public static String toString(int coordinateType) {
        if (coordinateType == TencentLocationManager.COORDINATE_TYPE_GCJ02) {
            return "国测局坐标(火星坐标)";
        } else if (coordinateType == TencentLocationManager.COORDINATE_TYPE_WGS84) {
            return "WGS84坐标(GPS坐标, 地球坐标)";
        } else {
            return "非法坐标";
        }
    }

    /**
     * 返回 manifest 中的 key
     */
    public static String getKey(Context context) {
        String key = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;
            if (metaData != null) {
                key = metaData.getString("TencentMapSDK");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TencentLocation",
                    "Location Manager: no key found in manifest file");
            key = "";
        }
        return key;
    }

    public static String getStringById(int id){
        return AppContext.sContext.getString(id);
    }
}
