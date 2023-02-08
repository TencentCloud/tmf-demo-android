package com.tencent.tmf.module.qapm.performance;


import com.tencent.tmf.base.api.config.TMFQAPMDefaultConfig;

/**
 * Author: jinfazhang
 * Date: 2019/4/16 20:54
 */
public interface PerformanceConstants {
    String REPORT_URL = TMFQAPMDefaultConfig.getUrl();
    String APP_KEY = TMFQAPMDefaultConfig.getAppKey();
    String USER_ID = "TMF-test3"; // 用户ID，配到白名单就可以每次都命中采样
    String APP_VERSION = "1.0.0"; // APP的版本名，这里hardcode的版本名只是用于演示，需要填入实际的版本名
    String GUID = "03035119022815300300429461345540"; // 这里hardcode的guid只是用于演示，需要填入实际的guid
    String SCENE_LISTVIEW_DROP_FRAME = "listview_drop_frame"; // 监控listview掉帧率的场景名
}
