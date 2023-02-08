package com.tencent.tmf.module.push;

import com.tencent.tmf.common.ContextHolder;
import com.tencent.tmf.push.api.ICustomNotificationForPush;
import com.tencent.tmf.push.api.TMFPushMessage;

/**
 * 自定义通知栏实现接口
 * Created by winnieyzhou on 2019/6/19.
 */
public class CustomNotificationForPushImpl implements ICustomNotificationForPush {

    @Override
    public boolean showCustomNotification(TMFPushMessage tmfPushMessage) {
        //展示自定义的通知栏
        CustomNCFactory.getInstance().showPushNotification(ContextHolder.sContext,tmfPushMessage);
        return true;
    }
}
