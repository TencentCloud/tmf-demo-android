package com.tencent.tmf.module.keyboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.common.gen.ModuleSharkConst;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.keyboard.api.KeyboardConfigApi;
import com.tencent.tmf.keyboard.common.KeyboardConfigMode;
import com.tencent.tmf.keyboard.config.DefaultKeyboardLayoutProvider;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-keyboard")
public class ModuleKeyboard implements Module {
    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleKeyboard attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            // 安全键盘初始化
            initKeyboard(context);
            Log.d(Constant.TAG, "ModuleKeyboard attach mainProcess");
        }
    }

    private void initKeyboard(Context context) {
        // 初始化安全键盘配置
        KeyboardConfigApi.init(context);
        // 设置键位显示与键值映射数据从云端拉取
        KeyboardConfigApi.getInstance().setKeyboardDataConfigMode(KeyboardConfigMode.CONFIG_MODE_ONLINE);
        // 设置键位显示与键值映射关系数据网络拉取接口的实现, 已内置默认实现
//        KeyboardConfigApi.getInstance().setFetchRemoteConfigDataNetHelper(new GetKeyPositionImpl());
        // 设置为自定义键位布局
        KeyboardConfigApi.getInstance().customKeyboardLayoutProvider(new DefaultKeyboardLayoutProvider());
        // 设置键位数据更新频率 300000 -> 5分钟  默认10分钟
        KeyboardConfigApi.getInstance().setUpdateKeyPositionFrequency(300000);
    }


    @Override
    public void detach() {

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
        depends.add(ModuleBaseConst.MODULE_ID);
        return depends;
    }
}
