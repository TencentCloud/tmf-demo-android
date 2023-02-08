package com.tencent.tmf.module.storage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mmkv.MMKV;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.cipher.api.TMFCipher;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleSharkConst;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-storage")
public class ModuleStorage implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleStorage attach");
        TMFCipher.init(context);
    }

    @Override
    public void detach() {
        MMKV.onExit();
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
        depends.add(ModuleSharkConst.MODULE_ID);
        return depends;
    }

}
