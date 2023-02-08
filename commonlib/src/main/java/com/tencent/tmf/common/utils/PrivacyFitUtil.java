package com.tencent.tmf.common.utils;

import com.tencent.tmf.base.api.TMFBase;
import com.tencent.tmf.base.api.utils.DeviceUtil;

/**
 * 隐私合规的适配
 */
public class PrivacyFitUtil {

    public static String MODEL = DeviceUtil.getSystemModel(TMFBase.getContext());
    public static String BRAND = DeviceUtil.getDeviceBrand(TMFBase.getContext());
    public static String BOARD = DeviceUtil.getDeviceBoard(TMFBase.getContext());

}
