package com.tencent.tmf.module.qapm.performance.api;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.tmf.portal.Portal;
import com.tencent.qapmsdk.QAPM;
import com.tencent.qapmsdk.base.config.ApmCertConfig;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.module.qapm.performance.impl.PerformanceProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 性能分析对外接口类
 * 注意：只监控与上报本进程（即初始化它的那个进程）的信息，如果有多个进程需要监控，得各初始化一次
 * Author: jinfazhang
 * Date: 2019/4/16 20:54
 */
public class TMFPerformanceService {

    private static final String TAG = "TMFPerformanceService";

    private static TMFPerformanceService sMonitor = new TMFPerformanceService();
    private static AtomicBoolean sHasInited = new AtomicBoolean(false);

    /**
     * 初始化监控模块
     * 注意：使用监控其他方法时，必须先调用此方法
     *
     * @param context context上下文
     * @param performanceProperty 监控所必需的参数
     */
    public static void init(Context context, ITMFPerformanceProperty performanceProperty) {
        if (context == null || performanceProperty == null) {
            throw new NullPointerException("context or performanceProperty is null");
        }
        String appKeyId = performanceProperty.getAppKey();
        if (TextUtils.isEmpty(appKeyId)) {
            throw new NullPointerException("appId is null");
        }
        /*
         * 看到以下日志即可
         *
         * // 过滤“QAPM_Magnifier”tag
         * 04-02 16:53:44.461 10805-10867/com.tencent.tmf.demo.qapm I/QAPM_Magnifier: QAPM SDK start success! PID:
         * 1024, APM_VERSION: 2.5.5.3-SNAPSHOT, SWITCH: 200, STARTED: 200
         * 04-02 16:53:44.461 10805-10867/com.tencent.tmf.demo.qapm I/QAPM_Magnifier: LEAKINSPECTOR : false, IO :
         * false, DB : false, LOOPER : true, CEILING : false, BATTERY : false, SAMPLE : true, ModeDropFrame:
         * true，ModeANR: false
         *
         * // 过滤“deviceid”msg
         * 04-02 16:53:35.361 10805-10867/com.tencent.tmf.demo.qapm I/QAPM_Authorization: os=4.4
         * .4&device=YQ601&uin=11223344&app_key=33e15431&p_id=1024&sdk_ver=2.5.5.3-SNAPSHOT&version=2
         * .1&deviceid=03035119022815300300429461345540
         * 04-02 16:53:35.391 10805-10867/com.tencent.tmf.demo.qapm I/QAPM_Config: {"device":"YQ601","os":"4.4.4",
         * "uin":"11223344","pid":"1024","sdk_ver":"2.5.5.3-SNAPSHOT","deviceid":"03035119022815300300429461345540",
         * "version":"2.1"}
         * 04-02 16:53:35.461 10805-10867/com.tencent.tmf.demo.qapm I/QAPM_Config: {"device":"YQ601","os":"4.4.4",
         * "uin":"11223344","pid":"1024","sdk_ver":"2.5.5.3-SNAPSHOT","deviceid":"03035119022815300300429461345540",
         * "version":"2.1"}
         */
        if (sHasInited.get()) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "do not init twice");
            }
            return;
        }
        sHasInited.set(true);

        // 添加一组证书
        ApmCertConfig.addAllCert(Arrays.asList("cert/sngapm.cer", "cert/qapm.cer", "cert/athena.cer"));

        setProperty(TMFPerformanceConstants.PROPERTY_KEY_DEVICE_ID, performanceProperty.getDeviceId())
                .setProperty(TMFPerformanceConstants.PROPERTY_KEY_MODEL, performanceProperty.getModel())
                .setProperty(TMFPerformanceConstants.PROPERTY_KEY_APP_INSTANCE, context.getApplicationContext())
                .setProperty(TMFPerformanceConstants.PROPERTY_KEY_HOST, performanceProperty.getKeyHost())
                .setProperty(TMFPerformanceConstants.PROPERTY_KEY_APP_ID, performanceProperty.getAppKey())
                .setProperty(TMFPerformanceConstants.PROPERTY_KEY_APP_VERSION, performanceProperty.getAppVersion())
                .setProperty(TMFPerformanceConstants.PROPERTY_KEY_SYMBOL_ID, performanceProperty.getSymbolId())
                .setProperty(TMFPerformanceConstants.PROPERTY_KEY_USER_ID, performanceProperty.getUserId())
                .setProperty(TMFPerformanceConstants.PROPERTY_KEY_ATHENAHOST_INSTANCE,
                        performanceProperty.getKeyAthenaHost());
    }

    /**
     * 创建性能分析模块的构建方法
     *
     * @return
     */
    public static ITMFPerformancePropertyBuilder createPerformancePropertyBuilder() {
        return new PerformanceProperty();
    }

    /**
     * 设置性能监控相关参数
     *
     * @param key key可选以下项 PropertyKeyAppId —— 产品ID，为“产品密钥-产品id”模式。
     *         PropertyKeyUserId —— 用户账号（比如QQ号、微信号等）
     *         PropertyKeyAppVersion —— 产品版本（以类似“7.3.0.141.r123456”格式填写， 后台可以解析出大版本号和revision）
     *         PropertyKeySymbolId —— UUID，用于拉取被混淆堆栈的mapping。
     *         PropertyKeyLogLevel —— 开启日志等级（建议debug版本开启QAPM.LevelDebug， release版本开启QAPM.LevelInfo）
     * @param value
     * @return TMFPerformanceMonitor对象
     */
    public static TMFPerformanceService setProperty(int key, String value) {
        checkHasInited();
        QAPM.setProperty(key, value);
        return sMonitor;
    }

    /**
     * 设置性能监控相关参数
     *
     * @param key 取值如下：PropertyKeyLogLevel
     *         PropertyKeyEventCon
     *         PropertyInspectorListener
     *         PropertyMemoryCellingListener
     *         PropertyKeyAppInstance
     * @param value
     * @return
     */
    public static TMFPerformanceService setProperty(int key, Object value) {
        checkHasInited();
        QAPM.setProperty(key, value);
        return sMonitor;
    }

    /**
     * 启动监控
     *
     * @param sceneName 被监控的场景名
     * @param mode 监控类型，可选以下项 ModeAll —— 开启全部监控，包括内存泄漏、文件IO、数据库IO、卡顿、触顶、电量、 区间性能、掉帧率、Crash监控
     *         ModeStable —— 开启适合外网使用的监控，包括卡顿、区间性能、掉帧率
     *         ModeResource —— 区间性能监控
     *         ModeDropFrame —— 掉帧率采集
     * @return <p>布尔值，表示监控是否开启成功
     *         注意：
     *         1. 正式版建议开启QAPM.ModeStable，研发流程内版本建议开启QAPM.ModeAll。
     *         2. 确实需要定制开启个别功能时，可使用ModeLeakInspector、ModeFileIO、ModeDBIO、ModeLooper、ModeCeiling、ModeBattery，modeCrash中
     *         一个到多
     *         个，多个使用时采用按为或方式即可，如ModeLeakInspector | ModeFileIO |
     *         ModeDBIO
     *         3. 上述定制功能开启后，不能通过endScene关闭。
     */
    public static boolean beginScene(String sceneName, int mode) {
        checkHasInited();
        return beginScene(sceneName, "", mode);
    }

    /**
     * 启动监控
     *
     * @param sceneName 被监控的场景名,见上述beginScene方法
     * @param extraInfo 可选以下项 用户定制 —— 若存在未调用endScene即再调用beginScene的场景，需要填extraInfo以区分
     * @param mode 监控类型，见上述beginScene方法
     * @return
     */
    public static boolean beginScene(String sceneName, String extraInfo, int mode) {
        checkHasInited();
        return QAPM.beginScene(sceneName, extraInfo, mode);
    }

    /**
     * 结束监控
     *
     * @param sceneName
     * @param mode 可选以下项 ModeResource —— 区间性能监控
     *         ModeDropFrame —— 掉帧率采集
     * @return 布尔值，表示监控是否终止成功
     */
    public static boolean endScene(String sceneName, int mode) {
        checkHasInited();
        return endScene(sceneName, "", mode);
    }

    public static boolean endScene(String sceneName, String extraInfo, int mode) {
        checkHasInited();
        return QAPM.endScene(sceneName, extraInfo, mode);
    }

    private static void checkHasInited() {
        if (!sHasInited.get()) {
            throw new NotInitException("性能监控模块还未初始化，请先调用init方法!!!");
        }
    }

    /**
     * 未初始化异常
     */
    private static class NotInitException extends RuntimeException {

        public NotInitException(String message) {
            super(message);
        }
    }

}
