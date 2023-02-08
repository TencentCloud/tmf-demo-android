package com.tencent.tmf.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import androidx.multidex.MultiDex;
import com.tencent.tmf.common.CommonApp;
import com.tencent.tmf.hotpatch.api.application.HotpatchApplication;

public class TmfDelegaleApplication extends HotpatchApplication {

    private Application application;
    private Context context;

    public TmfDelegaleApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
            long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime,
                tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = getApplication();
        context = application.getApplicationContext();

        CommonApp.get().onCreate(application, context);
    }

    @Override
    public void onAttachedBaseContext(Context context) {
        MultiDex.install(context);
        CommonApp.get().onAttachedBaseContext(context);
    }

//    /**
//     * 初始化启动来源标识
//     */
//    private void initStartTrace() {
//        // 配置当前当前引用的scheme、host、path等信息，具体用法参照AndroidManifest TMFMainActivity的配置
//        StartUpTraceConfig startUpTraceConfig = StartUpTraceConfig.builder()
//                .setReporter(new Reporter())
//                .build();
//        // 初始化来源监听 (必须)
//        StartUpTrace.init(startUpTraceConfig);
//        // 注册Activity声明周期监听（必须） 如有自定义的生命周期回调，请集成自TmfActivityLifecycleCallbacks
//        application.registerActivityLifecycleCallbacks(new TmfActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityStarted(Activity activity) {
//                super.onActivityStarted(activity);
//
//                try {
//                    SharkService.getSharkWithInit().startTcpChannel();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

}
