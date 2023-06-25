package com.tencent.tmf.common;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.demo.qmui.UIContextHolder;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.toolkit.TmfKit;

public class CommonApp {

    private static CommonApp sInstance;
    private Application application;
    private Context context;

    public static CommonApp get() {
        if (sInstance == null) {
            sInstance = new CommonApp();
        }
        return sInstance;
    }

    public Application getApplication() {
        return application;
    }


    public void onCreate(Application application, Context context) {
        Log.d(Constant.TAG, "CommonApp.onCreate=" + Utils.getCurrentProcessName(context));
        this.application = application;
        this.context = context;

        initQMUI();

        initPortal();

        TmfKit.init(application);
    }

    public void onAttachedBaseContext(Context context) {
        Log.d(Constant.TAG, "CommonApp.onAttachedBaseContext");
        ContextHolder.sContext = context;
        UIContextHolder.sContext = context;
    }

    /**
     * 初始化Demo组件框架
     */
    private void initPortal() {
        // 设置可输出log
        Portal.setDebuggable(true);

        Bundle param = new Bundle();
        param.putString("message", "This is a message from: CommonApp");
        Portal.init(application, new String[]{
                "module-portal-dynamic"
        }, param);
        Portal.attachModule("module-base");
        Portal.attachModule("module-main");
        Portal.attachModule("module-conch");
        Portal.attachModule("module-hotpatch");
        Portal.attachModule("module-offline");
        Portal.attachModule("module-hybrid");
        Portal.attachModule("module-push");
        Portal.attachModule("module-qmui");
        Portal.attachModule("module-upgrade");
    }

    /**
     * 初始化UI组件(QMUI)
     */
    private void initQMUI() {
        // UI组件
        QMUISwipeBackActivityManager.init(application);
    }
}
