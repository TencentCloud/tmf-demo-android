package com.tencent.tmf.module.colorlog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.colorlog.api.ColorLogger;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleUploadConst;
import com.tencent.tmf.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-colorlog")
public class ModuleColorLog implements Module {
    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleColorLog attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            initColorLog(context);
            Log.d(Constant.TAG, "ModuleColorLog attach mainProcess");
        }
    }

    /**
     * 初始化染色日志
     */
    private void initColorLog(Context context) {
        //构造染色日志配置
//        ColorLogConfig config = ColorLogConfig.builder(context)
//                .setColorLogEnabled(true) //打开染色日志开关
//                .setLogDir(Constants.LOG_DIR_BASE + File.separator + "color_log") //设置日志文件存储路径
//                .setLogNamePrefix("ColorLog") //设置日志文件名的前缀
//                .setLogMaxSize(20 << 20) //设置日志文件大小的最大值，单位byte
//                .setLogMaxKeepDays(10) //设置文件最长保存时间，单位天
//                .setColorLogKey("") //日志加密用的public key，参数设置为空字符即不加密
//                .build();
//        ColorLogger.init(config, SharkService.getSharkWithInit()); //初始化染色日志
//        ColorLogger.initWithDefaultConfig();
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
        depends.add(ModuleUploadConst.MODULE_ID);
        return depends;
    }
}
