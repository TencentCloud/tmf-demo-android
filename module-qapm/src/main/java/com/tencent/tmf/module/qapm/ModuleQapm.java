package com.tencent.tmf.module.qapm;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.qapmsdk.QAPM;
import com.tencent.qapmsdk.athena.BreadCrumbConfig;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleIcdpConst;
import com.tencent.tmf.common.gen.ModuleQapmConst;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.common.utils.PrivacyFitUtil;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.module.qapm.performance.Config;
import com.tencent.tmf.module.qapm.performance.PerformanceConstants;
import com.tencent.tmf.module.qapm.performance.api.TMFPerformanceConstants;
import com.tencent.tmf.module.qapm.performance.api.TMFPerformanceService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SingleModule(name = "module-qapm")
public class ModuleQapm implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleQapm attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            initPerformanceService(context);
            Log.d(Constant.TAG, "ModuleQapm attach mainProcess");
        }
    }

    /**
     * 初始化性能监控(QAPM)
     */
    private void initPerformanceService(Context context) {
//        String TMF_QAPM_KEY         = "33e15431-1024";
//        String TMF_QAPM_ATHENA_URL = "https://athena.qq.com/";
//        String TMF_QAPM_REPORT_URL = "https://ten.sngapm.qq.com";
//        String TMF_QAPM_CONFIG_URL = "https://ten.sngapm.qq.com";
//        String TMF_QAPM_USER_ID = "11223344";
        // 性能分析
        //AppKey和KeyHost要设置可用的，否则导致QAPM监控不开起
        TMFPerformanceService.init(context, TMFPerformanceService.createPerformancePropertyBuilder()
//                .setAppKey("33e15431-1024")
                .setModel(getPhoneModel())
                .setAppKey(Config.getInstance(context).getQapmKey())
                .setAppVersion(PerformanceConstants.APP_VERSION)
                .setSymbolId(UUID.randomUUID().toString())
                .setUserId(Config.getInstance(context).getUid())
                .setDeviceId(PerformanceConstants.GUID)
//                .setKeyHost("https://ten.sngapm.qq.com")
                .setKeyHost(Config.getInstance(context).geKeyHost())
                .setKeyAthenaHost(Config.getInstance(context).getkeyAthenaHost())
                .build());
        TMFPerformanceService.setProperty(TMFPerformanceConstants.PROPERTY_KEY_LOG_LEVEL, BuildConfig.DEBUG
                ? TMFPerformanceConstants.LEVEL_DEBUG : TMFPerformanceConstants.LEVEL_INFO);
        TMFPerformanceService.beginScene(TMFPerformanceConstants.SCENE_ALL, BuildConfig.DEBUG
                ? TMFPerformanceConstants.MODE_ALL : TMFPerformanceConstants.MODE_STABLE);
    }

    @Override
    public void detach() {
    }

    @Override
    public List<Class<?>> supportedServices() {
        return null;
    }

    @Override
    public ServiceFactory serviceFactory() {
        return null;
    }

    @Override
    public List<String> dependsOn() {
        List<String> dependsOn = new ArrayList<>();
        dependsOn.add(ModuleBaseConst.MODULE_ID);
        return dependsOn;
    }

    /**
     * 隐私合规改造
     *
     * @return phoneModel
     */
    private String getPhoneModel() {
        return Config.IS_FOR_PRIVACY_CHECK ? "" : PrivacyFitUtil.MODEL;
    }
}
