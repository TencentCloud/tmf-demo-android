package com.tencent.tmf.module.gm;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.common.Constant;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-gm")
public class ModuleGM implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleGM attach");
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
