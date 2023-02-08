package com.tencent.tmf.module.portalDynamic;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.common.gen.ModulePortalConst;
import com.tencent.tmf.common.service.IModulePortalDynamicService;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiaomao Yi on 2022/5/30.
 */
@SingleModule(name = "module-portal-dynamic")
public class ModulePortalDynamic implements Module.LifecycleModule {
    private Context applicationContext;

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModulePortalDynamic attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            Log.d(Constant.TAG, "ModulePortalDynamic attach mainProcess");
        }

        applicationContext = context;
        Toast.makeText(applicationContext, "ModulePortalDynamic attached", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void detach() {
        Toast.makeText(applicationContext, "ModulePortalDynamic detached", Toast.LENGTH_SHORT).show();
        applicationContext = null;
    }

    @Override
    public List<Class<?>> supportedServices() {
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(IModulePortalDynamicService.class);
        return list;
    }

    @Override
    public ServiceFactory serviceFactory() {
        return new PortalServiceFactory();
    }

    @Override
    public List<String> dependsOn() {
        List<String> depends = new ArrayList<>();
        depends.add(ModulePortalConst.MODULE_ID);
        return depends;
    }

    @Override
    public void onAttachBaseContext(Context context) {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int i) {

    }
}
