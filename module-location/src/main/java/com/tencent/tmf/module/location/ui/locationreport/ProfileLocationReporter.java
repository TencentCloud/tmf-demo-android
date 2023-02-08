package com.tencent.tmf.module.location.ui.locationreport;

import android.content.Context;
import android.util.Log;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.tmf.module.location.ui.TMapLocationDelegate;
import com.tencent.tmf.profile.api.ProfileManager;

/**
 * Profile上报地理位置，单次定位后上报结果（腾讯地图）
 * Created by winnieyzhou on 2019/7/5.
 *
 * 调用方法：ProfileLocationReporter.getInstance().reportLocationToProfile(context);
 *
 * 特别说明：腾讯地图sdk的定位接口仅支持单个定位结果回调上报，
 * 本接口使用的TencentLocationListener会覆盖掉其它回调listener，
 * 即:如果正在持续定位的过程中调用此上报接口，则会打断原来的定位回调，
 * 定位结果就直接返回到这边，而原来的定位回调接口将不再收到回调，
 * 因此建议不在正在定位的过程中调用此上报接口，如有需求，可选择使用高德地图上报接口
 */
public class ProfileLocationReporter {

    private static final String TAG = "TMF_LocationReportPF";

    private ProfileLocationReporter() {
    }

    private static class Holder {

        private static ProfileLocationReporter INSTANCE = new ProfileLocationReporter();
    }

    public static ProfileLocationReporter getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 腾讯地图定位示例
     */
    private TMapLocationDelegate mTMapLocationDelegate = TMapLocationDelegate.getInstance();
    private TencentLocationListener mTencentLocationListener = new TencentLocationListener() {
        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
            mTMapLocationDelegate.stopLocation();
            mTMapLocationDelegate.destroyLocation();
            //无论是否定位成功都直接返回结果
            if (null != tencentLocation) {
                if (error == TencentLocation.ERROR_OK) {
                    ProfileManager.setLocation(tencentLocation.getNation(), tencentLocation.getProvince(),
                            tencentLocation.getCity());
                    Log.e(TAG, "reportLocationToProfile country=" + tencentLocation.getNation() + ",province="
                            + tencentLocation.getProvince() + ",city=" + tencentLocation.getCity());
                } else {
                    Log.e(TAG, "reportLocationToProfile errorCode=" + error);
                }
            }
        }

        @Override
        public void onStatusUpdate(String name, int status, String desc) {
            String message = "{name=" + name + ", new status=" + status + ", desc=" + desc + "}";

            if (status == STATUS_DENIED) {
                // 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用很可能无法定位
                // 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
                Log.d(TAG, "locate permission not avi!");
            } else {
                Log.d(TAG, message);
            }
        }
    };

    /**
     * 定位结果上报Profile的调用入口
     *
     * @param context
     */
    public void reportLocationToProfile(Context context) {
        mTMapLocationDelegate.initLocation(context, mTencentLocationListener);
        mTMapLocationDelegate.startLocation();
    }
}
