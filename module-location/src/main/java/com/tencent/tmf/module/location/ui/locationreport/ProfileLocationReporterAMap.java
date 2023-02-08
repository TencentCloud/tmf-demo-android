package com.tencent.tmf.module.location.ui.locationreport;

import android.content.Context;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.tencent.tmf.module.location.ui.AMapLocationDelegate;
import com.tencent.tmf.profile.api.ProfileManager;

/**
 * Profile上报地理位置，单次定位并上报结果（高德地图）
 * Created by winnieyzhou on 2019/7/5.
 *
 * 调用方法：ProfileLocationReporter_AMap.getInstance().reportLocationToProfile(context);
 */
public class ProfileLocationReporterAMap {

    private static final String logTAG = "TMF_LocationReportPF";

    private ProfileLocationReporterAMap() {
    }

    private static class Holder {

        private static ProfileLocationReporterAMap INSTANCE = new ProfileLocationReporterAMap();
    }

    public static ProfileLocationReporterAMap getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 高德地图定位示例
     */
    private AMapLocationDelegate mAMapLocationDelegate = new AMapLocationDelegate();
    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            mAMapLocationDelegate.stopLocation();
            mAMapLocationDelegate.destroyLocation();
            if (null != aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    ProfileManager
                            .setLocation(aMapLocation.getCountry(), aMapLocation.getProvince(), aMapLocation.getCity());
                    Log.e(logTAG,
                            "reportLocationToProfile country=" + aMapLocation.getCountry() + ",province=" + aMapLocation
                                    .getProvince() + ",city=" + aMapLocation.getCity());
                } else {
                    Log.e(logTAG, "reportLocationToProfile errorCode=" + aMapLocation.getErrorCode());
                }
            }
        }
    };

    /**
     * 定位结果上报Profile的调用入口
     *
     * @param context
     */
    public void reportLocationToProfile(Context context) {
        mAMapLocationDelegate.initLocation(context, mAMapLocationListener);
        mAMapLocationDelegate.getCurrentLocation();
    }
}
