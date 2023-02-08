package com.tencent.tmf.applet.demo;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.tencent.tmf.applet.demo.service.AppletServiceImpl;
import com.tencent.tmf.applet.demo.sp.impl.CommonSp;
import com.tencent.tmf.applet.demo.utils.AppUtil;
import com.tencent.tmf.applet.demo.utils.GlideUtil;
import com.tencent.tmf.common.CommonApp;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.service.IAppletService;
import com.tencent.tmf.mini.api.TmfMiniSDK;
import com.tencent.tmf.mini.api.bean.MiniInitConfig;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-applet")
public class ModuleApplet implements Module {

    public static final String TAG = "TMF_APPLET_DEMO";
    public static final String IMEI = "test002";
    public static  String COUNTRY = "中国";
    public static  String PROVINCE = "北京市";
    public static  String CITY = "北京市";
    public static final String TMF_CONFIGURATIONS = "server/tmf-android-configurations-xiao.json";
    //feedback 测试
//    public static final String TMF_CONFIGURATIONS = "server/tmf-android-configurations-feedback.json";
    public static Application sApp;
    private IAppletService appletService;

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG,"ModuleApplet attach");
        appletService = new AppletServiceImpl();
        initApplet(CommonApp.get().getApplication());
    }

    @Override
    public void detach() {

    }

    @Override
    public List<Class<?>> supportedServices() {
        List<Class<?>> list = new ArrayList<>();
        list.add(IAppletService.class);
        return list;
    }

    @Override
    public ServiceFactory serviceFactory() {
        return new ServiceFactory() {
            @Override
            public <T> T create(Class<T> serviceClazz) {
                if (serviceClazz == IAppletService.class) {
                    return (T) appletService;
                }
                return null;
            }
        };
    }

    @Override
    public List<String> dependsOn() {
        List<String> depends = new ArrayList<>();
        depends.add("module-base");
        return depends;
    }

    private void initApplet(Application application) {
        sApp = application;
        if (AppUtil.isMainProcess(application)) {
            GlideUtil.init(application);
            QMUISwipeBackActivityManager.init(application);
        }

        MiniInitConfig.Builder builder = new MiniInitConfig.Builder();
        String configFilePath = CommonSp.getInstance().getConfigFilePath();
        if (!TextUtils.isEmpty(configFilePath)) {
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                builder.configFilePath(configFile.getAbsolutePath());
            } else {
                Log.i(TAG, "configFilePath = " + configFilePath + " not exist");
                builder.configAssetName(TMF_CONFIGURATIONS);
            }
        } else {
            builder.configAssetName(TMF_CONFIGURATIONS);
        }

        MiniInitConfig config = builder.verifyPkg(false)
                .imei(IMEI)
                .debug(true)
                .build();
        TmfMiniSDK.init(application, config);
        COUNTRY = sApp.getResources().getString(R.string.applet_mini_data_country);
        CITY = sApp.getResources().getString(R.string.applet_mini_proxy_city);
        PROVINCE = sApp.getResources().getString(R.string.applet_mini_proxy_province);
        TmfMiniSDK.setLocation(COUNTRY, PROVINCE, CITY);

        TmfMiniSDK.preloadMiniApp(application, null);
    }
}
