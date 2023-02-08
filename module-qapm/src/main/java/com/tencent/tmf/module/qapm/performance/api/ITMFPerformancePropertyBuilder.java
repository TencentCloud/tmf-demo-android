package com.tencent.tmf.module.qapm.performance.api;

/**
 * Author: jinfazhang
 * Date: 2019/4/22 10:35
 */
public interface ITMFPerformancePropertyBuilder {
    /**
     * 设置APPKEY
     *
     * @param appKey APP的唯一标识，在控制台可查看
     * @return
     */
    ITMFPerformancePropertyBuilder setAppKey(String appKey);

    /**
     * 设置APP版本号
     *
     * @param appVersion 产品版本（必需）
     * @return
     */
    ITMFPerformancePropertyBuilder setAppVersion(String appVersion);

    /**
     * 设置符号id
     *
     * @param symbolId 设置UUID，用于拉取被混淆堆栈的mapping （必需）
     * @return
     */
    ITMFPerformancePropertyBuilder setSymbolId(String symbolId);

    /**
     * 设置用户ID
     *
     * @param userId
     * @return
     */
    ITMFPerformancePropertyBuilder setUserId(String userId);

    /**
     * 设置设备ID
     *
     * @param deviceId
     * @return
     */
    ITMFPerformancePropertyBuilder setDeviceId(String deviceId);

    /**
     * 设置设备型号
     *
     * @param deviceId
     * @return
     */
    ITMFPerformancePropertyBuilder setModel(String model);

    /**
     * 设置上报域名
     *
     * @param keyHost
     * @return
     */
    ITMFPerformancePropertyBuilder setKeyHost(String keyHost);

    /**
     * 行为域名
     * @param keyAthenaHost
     * @return
     */
    ITMFPerformancePropertyBuilder setKeyAthenaHost(String keyAthenaHost);

    /**
     * 性能分析模块的构造方法
     *
     * @return
     */
    ITMFPerformanceProperty build();
}
