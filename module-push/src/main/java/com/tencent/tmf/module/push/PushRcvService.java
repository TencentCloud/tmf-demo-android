package com.tencent.tmf.module.push;

import android.util.Log;
import com.tencent.tmf.push.api.TMFPushRcvService;

/**
 * 接收push信息
 */
public class PushRcvService extends TMFPushRcvService {

    public static final String TAG = "TMF_PUSH_DEMO";

    @Override
    public void onRegisterResult(long errorCode, String errorReason) {
        //根据返回的错误码到对应厂商平台查阅错误码对应问题
        Log.i(TAG, "onRegisterResult errorCode:" + errorCode + ",errorReason:" + errorReason);
    }

    @Override
    public void onNotificationMsgClicked(String jsonExtra) { //由原onExtra接口更名
        //通知栏点击时附加参数回调，返回控制台填写的附加参数键值对（json格式）
        Log.i(TAG, "onNotificationMsgClicked jsonExtra:" + jsonExtra);
    }

    @Override
    public void onNotificationMsgArrived(String jsonExtra) {
        //通知栏到达时附加参数回调，返回控制台填写的附加参数键值对（json格式）
        Log.i(TAG, "onNotificationMsgArrived jsonExtra:" + jsonExtra);
    }

    @Override
    public void onReceivePushMsg(String pushMsg) {
        //返回透传消息数据
        Log.i(TAG, "onReceivePushMsg:" + pushMsg);
    }
}
