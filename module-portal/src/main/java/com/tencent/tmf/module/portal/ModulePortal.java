package com.tencent.tmf.module.portal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.common.gen.ModuleConchConst;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiaomao Yi on 2022/5/30.
 */
@SingleModule(name = "module-portal")
public class ModulePortal implements Module.LifecycleModule {

    private static volatile String sMessageFromInit = null;
    public static String getInitMessage() {
        return sMessageFromInit;
    }

    @Override
    public void attach(Context context, Bundle bundle) {
        sMessageFromInit = bundle.getString("message");

        Log.d(Constant.TAG, "ModulePortal attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            Log.d(Constant.TAG, "ModulePortal attach mainProcess");
        }
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
        depends.add(ModuleBaseConst.MODULE_ID);
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
