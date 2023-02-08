package com.tencent.tmf.module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.tencent.tmf.base.api.TMFBase;
import com.tencent.tmf.base.api.TMFBaseConfig;
import com.tencent.tmf.base.api.shark.TMFSharkConfigInfo;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.CommonApp;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.common.storage.sp.SharkSp;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.sharkservice.api.ISharkConfigAdjustCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-base")
public class ModuleBase implements Module {
    public static final String TMF_CONFIGURATIONS = "tmf-android-configurations.json";

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleBase attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            initBase(context);
            Log.d(Constant.TAG, "ModuleBase attach mainProcess");
        }
    }

    private void initBase(Context context) {

        String configPath = ConfigSp.getInstance().getTMFConfigPATH();
        File configFile = new File(configPath);

        TMFBaseConfig.Builder builder = new TMFBaseConfig.Builder()
                .buildNo(BuildConfig.BUILD_NO) // 必须，shark, 热更等模块需要使用
                .channel(BuildConfig.CHANNEL) // 非必须
                .debug(true)
                //.migrateType(MigrateType.MigrateAll) // 非必须，新版覆盖旧版时数据迁移类型，默认MigrateAll
                .sharkConfigAdjustCallback(new ISharkConfigAdjustCallback() {
                    @Override
                    public void onAdjust(TMFSharkConfigInfo sharkConfigInfo) {
                        // 是否开启IP透传，非必须，默认关闭
                        sharkConfigInfo.ipPassThrough = SharkSp.getInstance().getIpPathThrough();
                        // 是否开启TCP通道，非必须，默认开启，推荐开启
                        sharkConfigInfo.enableTcp = SharkSp.getInstance().getSharkTcp();
                    }
                });

        if (configFile.exists()) {
            builder.configFile(configFile); // demo用来实现动态加载配置功能, 开发者无需关系
        } else {
            builder.configAssetName(TMF_CONFIGURATIONS); // 非必须，默认tmf-android-configurations.json
        }

        TMFBaseConfig config = builder.build();
        //step2 初始化基础库
        TMFBase.init(CommonApp.get().getApplication(), config);

        sharkOptions();
    }

    /**
     * 个性化设置，根据自身情况酌情调用
     */
    private void sharkOptions() {
        //是否开启http
        TMFBase.getShark().setHttpEnable(SharkSp.getInstance().getSharkHttp());
        //是否开启ip透传
        TMFBase.getShark().setIpPassThroughEnable(SharkSp.getInstance().getIpPathThrough());
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
        List<String> depends = new ArrayList<>();
//        depends.add(ModuleSharkConst.MODULE_ID);
        return depends;
    }
}
