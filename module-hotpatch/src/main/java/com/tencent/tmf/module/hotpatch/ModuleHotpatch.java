package com.tencent.tmf.module.hotpatch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.demo.hotpatch.HotPatchTestActivity;
import com.tencent.tmf.hotpatchcore.HotPatch;
import com.tencent.tmf.hotpatchcore.Protocol.ProtocolType;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.portal.annotations.SingleModule;

import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-hotpatch")
public class ModuleHotpatch implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleHotpatch attach currentProcess: " + Utils.getCurrentProcessName(context));
        if (Utils.isMainProcess(context)) {
            initHotPatch(context);
            Log.d(Constant.TAG, "ModuleHotpatch attach mainProcess");
        }
    }

    /**
     * 初始化热修复
     *
     * @param context
     */
    private void initHotPatch(Context context) {
//        int protocolType = ConfigSp.getInstance().isNewProtocal() ? ProtocolType.PROTOCAL_TYPE_NEW
//                : ProtocolType.PROTOCAL_TYPE_OLD;
//        if (protocolType == ProtocolType.PROTOCAL_TYPE_NEW) {
//            //新协议初始化
//            HotPatchHolder.sHotPatch = HotPatch
//                    .with(context)
//                    .setProtocolType(ProtocolType.PROTOCAL_TYPE_NEW) // 默认旧协议
//                    .build();
//            IExternalController controllers = HotPatchHolder.sHotPatch.getExternalController();
//            controllers.setHotpatchNetworkController(HotPatchSharkController.get());
//            controllers.setReportController(HotPatchReportController.get());
//
//        } else if (protocolType == ProtocolType.PROTOCAL_TYPE_OLD) {
//            //旧协议初始化
//            HotPatchHolder.sHotPatch = HotPatch
//                    .with(context)
//                    .setProtocolType(ProtocolType.PROTOCAL_TYPE_OLD) // 默认旧协议
//                    .setShark(SharkService.getSharkWithInit()) // 旧协议必须设置网关
//                    .build();
//        }
//        //初始化热修复
//        HotPatchHolder.sHotPatch.init();

        /*
         * 协议版本说明：
         * 1. PROTOCOL_TYPE_SHARK，直接通过移动网关实现拉取与推送
         * 2. PROTOCOL_TYPE_CONCH，依赖数据同步组件实现拉取与推送
         * 协议选择：
         * 1. 如果对接的是共有云版服务，需要选择PROTOCOL_TYPE_SHARK
         * 2. 如果对接的是私有化服务，选择PROTOCOL_TYPE_CONCH即可。如果选择PROTOCOL_TYPE_SHARK，
         * 需要同管理员确认服务版本是否支持新协议
         */
        int protocolType = ConfigSp.getInstance().isNewProtocal() ? ProtocolType.PROTOCOL_TYPE_SHARK
                : ProtocolType.PROTOCOL_TYPE_CONCH;
        HotPatch.init(context, protocolType);
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
        depends.add(ModuleBaseConst.MODULE_ID);
        return depends;
    }
}
