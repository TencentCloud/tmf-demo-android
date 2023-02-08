package com.tencent.tmf.module.qapm.performance.api;

import com.tencent.qapmsdk.QAPM;

/**
 * 性能分析模块常量定义
 * Author: jinfazhang
 * Date: 2019/4/16 20:54
 */
public interface TMFPerformanceConstants {

    int PROPERTY_KEY_APP_ID = QAPM.PropertyKeyAppId;
    int PROPERTY_KEY_USER_ID = QAPM.PropertyKeyUserId;
    int PROPERTY_KEY_APP_VERSION = QAPM.PropertyKeyAppVersion;
    int PROPERTY_KEY_SYMBOL_ID = QAPM.PropertyKeySymbolId;
    int PROPERTY_KEY_LOG_LEVEL = QAPM.PropertyKeyLogLevel;
    int PROPERTY_KEY_HOST = QAPM.PropertyKeyHost;
    //  int PropertyKeyConfig = QAPM.PropertyKeyConfig;
    //  int PropertyKeyEventCon = QAPM.PropertyKeyEventCon;
    int PROPERTY_INSPECTOR_LISTENER = QAPM.PropertyInspectorListener;
    int PROPERTY_MEMORY_CELLING_LISTENER = QAPM.PropertyMemoryCellingListener;
    int PROPERTY_KEY_DEVICE_ID = QAPM.PropertyKeyDeviceId;
    int PROPERTY_KEY_MODEL = QAPM.PropertyKeyModel;
    int PROPERTY_KEY_APP_INSTANCE = QAPM.PropertyKeyAppInstance;
    int PROPERTY_KEY_ATHENAHOST_INSTANCE = QAPM.PropertyKeyAthenaHost;

    /**
     * 所有场景
     */
    String SCENE_ALL = QAPM.SCENE_ALL;
    /**
     * 内存泄漏监控
     */
    int MODE_LEAK_INSPECTOR = QAPM.ModeLeakInspector;
    /**
     * 文件IO监控
     */
    int MODE_FILE_IO = QAPM.ModeFileIO;
    /**
     * 数据库监控
     */
    int MODE_DBIO = QAPM.ModeDBIO;
    int MODE_LOOPER = QAPM.ModeLooper;
    int MODE_CEILING = QAPM.ModeCeiling;
//    /**
//     * 电量监控
//     */
//    int ModeBattery = QAPM.ModeBattery;

    /**
     * 区间性能监控
     */
    int MODE_RESOURCE = QAPM.ModeResource;
    /**
     * 掉帧监控
     */
    int MODE_DROP_FRAME = QAPM.ModeDropFrame;
    /**
     * ANR监控
     */
    int MODE_ANR = QAPM.ModeANR;
    /**
     * Crash监控
     */
    int MODE_CRASH = QAPM.ModeCrash;
    /**
     * 开启全部监控，包括内存泄漏、文件IO、数据库IO、卡顿、触顶、电量、区间性能、掉帧率
     */
    int MODE_ALL = QAPM.ModeAll;
    /**
     * 开启适合外网使用的监控，包括卡顿、区间性能、掉帧率
     */
    int MODE_STABLE = QAPM.ModeStable;

    int LEVEL_OFF = QAPM.LevelOff;
    int LEVEL_ERROR = QAPM.LevelError;
    int LEVEL_WARN = QAPM.LevelWarn;
    int LEVEL_INFO = QAPM.LevelInfo;
    int LEVEL_DEBUG = QAPM.LevelDebug;
    int LEVEL_VERBOS = QAPM.LevelVerbos;

}
