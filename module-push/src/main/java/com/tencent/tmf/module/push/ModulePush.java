package com.tencent.tmf.module.push;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleConchConst;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.profile.api.ISetTagCallback;
import com.tencent.tmf.profile.api.ITagErrorListener;
import com.tencent.tmf.profile.api.ProfileManager;
import com.tencent.tmf.push.api.ProtocolType;
import com.tencent.tmf.push.api.PushCenter;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-push")
public class ModulePush implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModulePush attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            initPush(context);
            Log.d(Constant.TAG, "ModulePush attach mainProcess");
        }
    }

    private void initPush(Context context) {
        // 获取网关
//        IShark shark = SharkService.getSharkWithInit();
        // 获取application
//        Application application = CommonApp.get().getApplication();
        //初始化推送
        initPushAndProfile();
        // 初始化应用升级云指令弹窗
        PushDialogManager.getInstance().init(context);
    }


    /**
     * 初始化push，设置profile
     */
    private void initPushAndProfile() {
        /*
         * 协议版本说明：
         * 1. PROTOCOL_TYPE_SHARK，直接通过移动网关实现拉取与推送
         * 2. PROTOCOL_TYPE_CONCH，依赖数据同步组件实现拉取与推送
         * 协议选择：
         * 1. 如果对接的是共有云版服务，需要选择PROTOCOL_TYPE_SHARK
         * 2. 如果对接的是私有化服务，选择PROTOCOL_TYPE_CONCH即可。如果选择PROTOCOL_TYPE_SHARK，
         * 需要同管理员确认服务版本是否支持新协议
         */
        int protocalType = ConfigSp.getInstance().isNewProtocal() ? ProtocolType.PROTOCOL_TYPE_SHARK
                : ProtocolType.PROTOCOL_TYPE_CONCH;
        // 将原init接口和setTMFPushServiceImpl合并为新init接口，原接口设为duplicate
        // 初始化接口增加参数，传入TMFPushRcvService实现类
        PushCenter.init(ConfigSp.getInstance().userFcm(), new PushRcvService(), protocalType);//初始化，包括注册厂商sdk和自建通道
//        PushCenter.init(application, shark, new PushRcvService());//初始化，包括注册厂商sdk和自建通道
//        PushCenter.init(application, shark);//初始化，包括注册厂商sdk和自建通道
//        PushCenter.setTMFPushServiceImpl(PushRcvService.class);//接收通知栏消息
        PushCenter.setOpenUrlImpl(new OpenUrlForPushImpl());//可选：传入打开url的intent，不传则打开默认浏览器
//        PushCenter.setCustomNotification(new CustomNotificationForPushImpl());//可选:非厂商push时通知栏自定义样式的实现
        PushCenter.setAllowResolveInnerError(true);//设置允许调起页面处理push注册错误，默认为false

        //由于push组件内部依赖profile组件，因此profile组件的初始化需提前到push组件init之前
//        ProfileManager.init(application,shark);
        //设置userId的接口示例，用于按用户标识做消息推送，实际使用场景为获取到userId后进行上报
//        ProfileManager.setUserId("custom_userid_2");
        // userId解绑示例
//        ProfileManager.setUserId("");
        // 自定义标签设置示例
        ProfileManager.setTagErrorListener(new ITagErrorListener() { //全局自定义标签错误监听器
            @Override
            public void onError(List<TagResult> list) {
                if (list != null && list.size() > 0) {
                    for (TagResult tagResult : list) {
                        Log.i("TMF_Profile",
                                "tagKey=" + tagResult.mTagKey + ",tagValue=" + tagResult.mTagValue + ",errorCode="
                                        + tagResult.mErrorCode);
                    }
                }
            }
        });
        ProfileManager.setTag("tag1", "tag1_value1");
        // 自定义标签删除示例，value为空即删除
//        ProfileManager.setTag("tag1","");

        // 自定义标签快速上报及回调接口示例，建议仅使用本接口上报Tag，不使用原setTag接口
        long currentTime = System.currentTimeMillis();
        ProfileManager.setTagWithCallback("tag2", "tag2_value1", new ISetTagCallback() {
            @Override
            public void onResult(int resultCode) {
                long usedTime = System.currentTimeMillis() - currentTime;
                Log.i("TMF_Profile", "resultCode = " + resultCode + ",used time = " + usedTime);
            }
        });
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
        depends.add(ModuleConchConst.MODULE_ID);
        return depends;
    }
}
