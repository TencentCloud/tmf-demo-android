package com.tencent.tmf.module.scan;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.common.Constant;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-scan")
public class ModuleScan implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleScan attach");

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
