package com.tencent.tmf.module.offline;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModulePushConst;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.weboffline.api.OfflineManager;
import com.tencent.tmf.weboffline.api.entitiy.ProtocolType;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-offline")
public class ModuleOffline implements Module {

    public static ModuleOffline sInstance;
    public static final String CUSTOM_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrREOFRN9uYS869mOeLmZXFw3d"
            + "jnofd7wbf3ru6zmRB7P6gTpmnvJNnclCcEC7TOmDImvVl+gVPXQ0AmWAI4q042rA"
            + "LV5NPCJiOpIzSgJH2l0F/ZVbj69QztBiKmSHVHqQ8yemqtFljNEJbE9HL3RXE/uw"
            + "GmHViFl4fGg9am5w7QIDAQAB";

    public static final String CUSTOM_PUB_KEY1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDoVcGwzsKCbRVkxJqBb7mgkI0F"
            + "ezgSnvWe8hEBQ/u7MwSbM9DQOvGX6LURf5lcSx5ObIGrLrLwSbQhnPqLiHftYmlW"
            + "HtHZkWIUWd/jftpVt/CFyVhfX5lE5SOWnrTozGKIh5FAQpYz/EC2fmDr76N+N/Lr"
            + "38KnXnYG+faTuDlHRwIDAQAB";

    public static String current;

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleOffline attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            sInstance = this;
            initWebOffline(context);
            Log.d(Constant.TAG, "ModuleOffline attach mainProcess");
        }
    }

    /**
     * 离线包初始化
     */
    private void initWebOffline(Context context) {
        //自定义公钥
//        String CUSTOM_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDoVcGwzsKCbRVkxJqBb7mgkI0F" +
//                "ezgSnvWe8hEBQ/u7MwSbM9DQOvGX6LURf5lcSx5ObIGrLrLwSbQhnPqLiHftYmlW" +
//                "HtHZkWIUWd/jftpVt/CFyVhfX5lE5SOWnrTozGKIh5FAQpYz/EC2fmDr76N+N/Lr" +
//                "38KnXnYG+faTuDlHRwIDAQAB";

//        current = CUSTOM_PUB_KEY;
//        String saveKey = OfflineSp.getInstance().getOfflinePublickKey();
//        if (!TextUtils.isEmpty(saveKey)) {
//            current = saveKey;
//        }
//
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
//
        OfflineManager.init(context, protocalType);
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
        depends.add(ModulePushConst.MODULE_ID);
        return depends;
    }

}
