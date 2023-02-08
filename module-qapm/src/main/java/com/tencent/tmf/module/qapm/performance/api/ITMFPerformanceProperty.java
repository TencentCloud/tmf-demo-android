package com.tencent.tmf.module.qapm.performance.api;

/**
 * Author: jinfazhang
 * Date: 2019/4/22 10:35
 */
public interface ITMFPerformanceProperty {

    /**
     * 获取APPKEY
     *
     * @return
     */
    String getAppKey();

    /**
     * 获取APP的版本号
     *
     * @return
     */
    String getAppVersion();

    /**
     * 获取符号表ID
     *
     * @return
     */
    String getSymbolId();

    /**
     * 获取用户ID
     *
     * @return
     */
    String getUserId();

    /**
     * 获取上报域名
     *
     * @return
     */
    String getKeyHost();

    /**
     * 获取行为上报域名
     * @return
     */
    String getKeyAthenaHost();
    /**
     * 获取设备ID
     *
     * @return
     */

    String getDeviceId();

    /**
     * 获取设备型号
     *
     * @return
     */
    String getModel();
}
