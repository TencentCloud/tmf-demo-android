package com.tencent.tmf.h5container;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleQapmConst;
import com.tencent.tmf.common.log.L;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.webview.api.TMFWebConfig;
import com.tencent.tmf.webview.x5.TMFX5Core;
import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-h5container")
public class ModuleH5Container implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleH5Container attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            initX5(context);
            Log.d(Constant.TAG, "ModuleH5Container attach mainProcess");
        }
    }

    /**
     * 初始化X5
     */
    private void initX5(final Context context) {
        TMFWebConfig.setLocalDomian("http://www.test.local");
        // X5内核
        TMFX5Core.getInstance().onX5CoreInited(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
                if (value) {
                    L.d("X5 init success  ");
                } else {
                    L.e("X5 init failed ");
                }
            }
        });
        TMFX5Core.getInstance().init(context);
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
