package com.tencent.tmf.module.qapm.performance.impl;

import com.tencent.tmf.module.qapm.performance.api.ITMFPerformanceProperty;
import com.tencent.tmf.module.qapm.performance.api.ITMFPerformancePropertyBuilder;

/**
 * Author: jinfazhang
 * Date: 2019/4/22 10:34
 */
public class PerformanceProperty implements ITMFPerformanceProperty, ITMFPerformancePropertyBuilder {

    private String appKey;
    private String appVersion;
    private String symbolId;
    private String userId;
    private String keyHost;
    private String keyAthenaHost;
    private String deviceId;
    private String model;

    @Override
    public ITMFPerformanceProperty build() {
        return this;
    }

    @Override
    public ITMFPerformancePropertyBuilder setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    @Override
    public ITMFPerformancePropertyBuilder setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    @Override
    public ITMFPerformancePropertyBuilder setSymbolId(String symbolId) {
        this.symbolId = symbolId;
        return this;
    }

    @Override
    public ITMFPerformancePropertyBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public ITMFPerformancePropertyBuilder setKeyHost(String keyHost) {
        this.keyHost = keyHost;
        return this;
    }

    @Override
    public ITMFPerformancePropertyBuilder setKeyAthenaHost(String keyAthenaHost) {
        this.keyAthenaHost = keyAthenaHost;
        return this;
    }

    @Override
    public ITMFPerformancePropertyBuilder setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    @Override
    public ITMFPerformancePropertyBuilder setModel(String model) {
        this.model = model;
        return this;
    }

    @Override
    public String getAppKey() {
        return this.appKey;
    }

    @Override
    public String getAppVersion() {
        return this.appVersion;
    }

    @Override
    public String getSymbolId() {
        return this.symbolId;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    @Override
    public String getKeyHost() {
        return this.keyHost;
    }

    @Override
    public String getKeyAthenaHost() {
        return this.keyAthenaHost;
    }

    @Override
    public String getDeviceId() {
        return this.deviceId;
    }

    @Override
    public String getModel() {
        return this.model;
    }
}
