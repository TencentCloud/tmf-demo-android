package com.tencent.tmf.module.subinstance;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.tencent.tmf.base.api.TMFBase;
import com.tencent.tmf.base.api.config.ITMFConfigManager;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.core.api.TMFConfigManagerFactory;
import com.tencent.tmf.core.api.TMFServiceManager;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.profile.api.IProfile;
import com.tencent.tmf.profile.api.ProfileManager;
import com.tencent.tmf.shark.api.IShark;
import com.tencent.tmf.shark.api.SharkCommonConst;
import com.tencent.tmf.sharkservice.api.SharkService;
import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-subinstance")
public class ModuleSubInstance implements Module {

    @Override
    public void attach(Context context, Bundle extras) {
        Log.d(Constant.TAG, "ModuleSubInstance attach");
        initSubInstance(context);
    }


    @Override
    public void detach() {

    }

    @Override
    public List<Class<?>> supportedServices() {
        List<Class<?>> services = new ArrayList<>();
        return services;
    }

    @Override
    public ServiceFactory serviceFactory() {
        return null;
    }

    @Override
    public List<String> dependsOn() {
        List<String> depends = new ArrayList<>();
        depends.add(ModuleBaseConst.MODULE_ID);
        return depends;
    }

    public static String PKG_SUFFIX = "_TMF";
    public static final int SERVER_TYPE = SharkCommonConst.SERVER_TYPE_RELEASE;

    public static String getSharkPkg(Context context) {
        return context.getPackageName() + PKG_SUFFIX;
    }

    private boolean initSubInstance(Context application) {
        ITMFConfigManager configManager = TMFConfigManagerFactory.createConfigManager(
                application.getApplicationContext(),
                "tmf-configurations.json");

        SubServices.setConfigManager(configManager);

        TMFServiceManager.getServiceManager(configManager.getEnvId())
                .registerService(Application.class, TMFBase.getApplication());

        if (!configManager.isValid()) {
            throw new IllegalArgumentException("Invalid config, please check your config file in assets");
        }

        try {
            String sharkPackage = getSharkPkg(application) + "_SUB";
            IShark subShark = SharkService.startSharkWith(application,
                    configManager,
                    sharkPackage,
                    sharkConfigInfo -> {
                        sharkConfigInfo.channel = "sub";
                        sharkConfigInfo.buildNo = 1111;
                        sharkConfigInfo.enableTcp = false;
                    },
                    null);

            TMFServiceManager.getServiceManager(configManager.getEnvId()).registerService(IShark.class, subShark);

            IProfile profile = ProfileManager.initWith(TMFBase.getApplication(), subShark);
            TMFServiceManager.getServiceManager(configManager.getEnvId()).registerService(IProfile.class, profile);
        } catch (Exception e) {
            throw new IllegalStateException("TMFBase init error.", e);
        }
        return true;
    }

}
