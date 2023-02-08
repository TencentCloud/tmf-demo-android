package com.tencent.tmf.module.location;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.tmf.module.location.ui.AppContext;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleConchConst;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-location")
public class ModuleLocation implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleLocation attach");
        //腾讯定位隐私条款
        TencentLocationManager.setUserAgreePrivacy(true);
        AppContext.sContext = context;
    }

    @Override
    public void detach() {

    }

    @Override
    public List<Class<?>> supportedServices() {
        return new ArrayList<>();
    }

    @Override
    public ServiceFactory serviceFactory() {
        return null;
    }

    @Override
    public List<String> dependsOn() {
        List<String> depends = new ArrayList<>();
        depends.add(ModuleConchConst.MODULE_ID);
        return depends;
    }

}
