package com.tencent.tmf.module.hybrid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.hybrid.TMFHybridManager;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;
import java.util.List;

@SingleModule(name = "module-hybrid")
public class ModuleHybrid implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.e(Constant.TAG, "ModuleHybrid attach.." + context.getClass().getSimpleName());
        TMFHybridManager.getInstance().init(context);

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
        return null;
    }
}
