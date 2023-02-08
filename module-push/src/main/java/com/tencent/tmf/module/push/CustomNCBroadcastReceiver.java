package com.tencent.tmf.module.push;

import static com.tencent.tmf.push.api.TMFPushMessage.OPEN_ACTIVITY;
import static com.tencent.tmf.push.api.TMFPushMessage.OPEN_URL;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.tmf.push.api.ICustomNCReporter;
import com.tencent.tmf.push.api.PushCenter;
import com.tencent.tmf.push.api.TMFPushMessage;

import java.net.URISyntaxException;

/**
 * 接收通知栏事件
 * Created by winnieyzhou on 2019/6/20.
 */
public class CustomNCBroadcastReceiver extends BroadcastReceiver {

    public static final String NOTIFY_ID = "notify_id";
    public static final String NOTIFY_MESSAGE = "notify_message";
    public static final String NOTIFY_CLICK_ACTION = "custom_push_notification_clicked";
    public static final String NOTIFY_CLEAR_ACTION = "custom_push_notification_cancelled";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        if (!NOTIFY_CLICK_ACTION.equals(action) && !NOTIFY_CLEAR_ACTION.equals(action)) {
            return;
        }
        int notifyId = intent.getIntExtra(NOTIFY_ID, -1);
        if (notifyId != -1) {
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notifyId);
        }

        TMFPushMessage tmfPushMessage = (TMFPushMessage) intent.getSerializableExtra(NOTIFY_MESSAGE);
        if (null == tmfPushMessage) {
            return;
        }

        if (NOTIFY_CLICK_ACTION.equals(action)) {
            Log.e("TMF_PUSH", "custom notification click");
            //处理点击事件
            Intent clickIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            int type = tmfPushMessage.getJumpType();
            String jumpParam = tmfPushMessage.getJumpParam();
            String jsonExtra = tmfPushMessage.getJsonExtra();
            if (!TextUtils.isEmpty(jumpParam)) {
                if (type == OPEN_URL) {
                    clickIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jumpParam));
                } else if (type == OPEN_ACTIVITY) {
                    try {
                        clickIntent = Intent.parseUri(jumpParam, Intent.URI_INTENT_SCHEME);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (clickIntent != null) {
                clickIntent.putExtra("jsonExtra", jsonExtra);
                if (!(context instanceof Activity)) {
                    clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                try {
                    context.startActivity(clickIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("TMF_PUSH", "custom push jump ActivityNotFoundException:" + e.getMessage());
                }
            }
            //上报通知栏事件-点击
            PushCenter.getCustomNCReporter().reportNCEvent(tmfPushMessage, ICustomNCReporter.NC_EVENT_CLICK);
        }

        if (NOTIFY_CLEAR_ACTION.equals(action)) {
            Log.e("TMF_PUSH", "custom notificaition clear");
            //上报通知栏事件-清除
            PushCenter.getCustomNCReporter().reportNCEvent(tmfPushMessage, ICustomNCReporter.NC_EVENT_CLEAR);
        }
    }
}
