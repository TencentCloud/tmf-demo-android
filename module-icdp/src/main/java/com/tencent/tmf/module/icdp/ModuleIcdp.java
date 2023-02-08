package com.tencent.tmf.module.icdp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.base.api.TMFBase;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleQapmConst;
import com.tencent.tmf.icdp.ICDPApplication;
import java.util.ArrayList;
import java.util.List;


@SingleModule(name = "module-icdp")
public class ModuleIcdp implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.e(Constant.TAG, "ModuleIcdp attach.." + context.getClass().getSimpleName());
        initICDP(context);
        initHotSplash(context);
    }

    private void initHotSplash(Context context) {
        CallHotSplashKt.init(context);
    }

    private void initICDP(Context context) {
        try {
            ICDPApplication.enableDebugLog(true);
            ICDPApplication
                    .init(context, Config.getInstance(context).getServerHost(),
                            TMFBase.getConfigManager().getAppKey(), "", Config.getInstance(context).getUid(), "");
        } catch (Exception e) {
            Log.e("IcdpActivity", "error " + e.getMessage());
            e.printStackTrace();
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
        depends.add(ModuleQapmConst.MODULE_ID);
        return depends;
    }
}
