package com.tencent.tmf.module.subinstance;

import com.tencent.tmf.base.api.config.ITMFConfigManager;
import com.tencent.tmf.core.api.TMFServiceManager;
import com.tencent.tmf.profile.api.IProfile;
import com.tencent.tmf.shark.api.IShark;

public class SubServices {

    private static ITMFConfigManager configManager;

    public static void setConfigManager(ITMFConfigManager configManager) {
        SubServices.configManager = configManager;
    }

    public static IShark getSubShark() {
        return TMFServiceManager.getServiceManager(configManager.getEnvId()).getService(IShark.class);
    }

    public static IProfile getSubProfile() {
        return TMFServiceManager.getServiceManager(configManager.getEnvId()).getService(IProfile.class);
    }
}
