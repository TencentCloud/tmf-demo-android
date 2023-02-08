package com.tencent.tmf.module.conch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.common.gen.ModuleSharkConst;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-conch")
public class ModuleConch implements Module {
    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleConch attach");
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
}
