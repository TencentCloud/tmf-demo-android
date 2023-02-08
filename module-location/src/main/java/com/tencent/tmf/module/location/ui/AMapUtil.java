package com.tencent.tmf.module.location.ui;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationQualityReport;
import com.tencent.tmf.base.api.utils.AppUtil;
import com.tencent.tmf.module.location.R;

/**
 * 辅助工具类
 */
public class AMapUtil {

    public static final String URL_H5LOCATION = "file:///android_asset/sdkLoc.html";

    /**
     * 根据定位结果返回定位信息的字符串
     * @param location
     * @return
     */
    public static synchronized String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append(getStringById(R.string.locate_success) + "\n");
            sb.append(getStringById(R.string.locate_type) + location.getLocationType() + "\n");
            sb.append(getStringById(R.string.locate_longitude) + location.getLongitude() + "\n");
            sb.append(getStringById(R.string.locate_latitude) + location.getLatitude() + "\n");
            sb.append(getStringById(R.string.locate_currency) + location.getAccuracy() + getStringById(R.string.locate_meter) + "\n");
            sb.append(getStringById(R.string.locate_provider) + location.getProvider() + "\n");

            sb.append(getStringById(R.string.locate_speed)+ location.getSpeed() + getStringById(R.string.locate_miles_per_second) + "\n");
            sb.append(getStringById(R.string.locate_degree) + location.getBearing() + "\n");
            // 获取当前提供定位服务的卫星个数
            sb.append(getStringById(R.string.locate_satellite) + location.getSatellites() + "\n");
            sb.append(getStringById(R.string.locate_country) + location.getCountry() + "\n");
            sb.append(getStringById(R.string.locate_province) + location.getProvince() + "\n");
            sb.append(getStringById(R.string.locate_city) + location.getCity() + "\n");
            sb.append(getStringById(R.string.locate_city_code) + location.getCityCode() + "\n");
            sb.append(getStringById(R.string.locate_city_distinct) + location.getDistrict() + "\n");
            sb.append(getStringById(R.string.locate_city_distinct_code) + location.getAdCode() + "\n");
            sb.append(getStringById(R.string.locate_city_address) + location.getAddress() + "\n");
            sb.append(getStringById(R.string.locate_city_poi) + location.getPoiName() + "\n");
            //定位完成的时间
            sb.append(getStringById(R.string.locate_time) + Util.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        } else {
            //定位失败
            sb.append(getStringById(R.string.locate_failed) + "\n");
            sb.append(getStringById(R.string.locate_err_code) + location.getErrorCode() + "\n");
            sb.append(getStringById(R.string.locate_err_msg) + location.getErrorInfo() + "\n");
            sb.append(getStringById(R.string.locate_err_desc) + location.getLocationDetail() + "\n");
        }
        sb.append(getStringById(R.string.locate_pro)).append("\n");
        sb.append(getStringById(R.string.locate_wifi)).append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
        sb.append(getStringById(R.string.locate_gps_state)).append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus()))
                .append("\n");
        sb.append(getStringById(R.string.locate_gps_start)).append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
        sb.append(getStringById(R.string.locate_net_type) + location.getLocationQualityReport().getNetworkType()).append("\n");
        sb.append(getStringById(R.string.locate_net_cost) + location.getLocationQualityReport().getNetUseTime()).append("\n");
        sb.append("****************").append("\n");
        //定位之后的回调时间
        sb.append(getStringById(R.string.locate_callback_time) + Util.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

        //解析定位结果，
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
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = getStringById(R.string.locate_gps_status_normal);
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = getStringById(R.string.locate_gps_status_no_provider);
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = getStringById(R.string.locate_gps_status_off);
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = getStringById(R.string.locate_gps_status_mode_saving);
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = getStringById(R.string.locate_gps_status_no_permission);
                break;
            default:
                break;
        }
        return str;
    }

    public static String getStringById(int id){
        return AppContext.sContext.getString(id);
    }
}
