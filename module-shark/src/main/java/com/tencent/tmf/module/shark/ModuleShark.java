package com.tencent.tmf.module.shark;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.shark.api.Shark;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-shark")
public class ModuleShark implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleShark attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            //网关初始化
//            Shark.setAppContext(context);
            Log.d(Constant.TAG, "ModuleShark attach mainProcess");
        }
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
        return null;
    }

}
