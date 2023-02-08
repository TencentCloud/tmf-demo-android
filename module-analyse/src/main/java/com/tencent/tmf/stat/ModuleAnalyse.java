package com.tencent.tmf.stat;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleQapmConst;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.stat.api.AnalyseConfig;
import com.tencent.tmf.stat.api.TMFStatService;
import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-analyse")
public class ModuleAnalyse implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleAnalyse attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            initAnalyse(context);
            Log.d(Constant.TAG, "ModuleAnalyse attach mainProcess");
        }
    }

    private void initAnalyse(Context context) {
        AnalyseConfig configuration = new AnalyseConfig.Builder("aurora").build();
        TMFStatService.startWithConfiguration((Application) context, configuration);

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
