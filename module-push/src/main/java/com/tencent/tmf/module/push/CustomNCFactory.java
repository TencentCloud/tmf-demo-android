package com.tencent.tmf.module.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.ContextHolder;
import com.tencent.tmf.push.api.ICustomNCReporter;
import com.tencent.tmf.push.api.PushCenter;
import com.tencent.tmf.push.api.TMFPushMessage;

/**
 * push通知栏实现类
 * Created by winnieyzhou on 2019/6/19.
 */
public class CustomNCFactory {

    private static final String channelId = "custom_push_channel";
    private static final String channelName = "custom_push";
    private NotificationManager mNotificationManager;
    private static int mIncrementId;

    private CustomNCFactory() {
        createNotification(ContextHolder.sContext);
    }

    private static class Holder {

        private static CustomNCFactory INSTANCE = new CustomNCFactory();
    }

    public static CustomNCFactory getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * showPushNotification
     * @param context
     * @param tmfPushMessage
     */
    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    public void showPushNotification(Context context, TMFPushMessage tmfPushMessage) {
        if (null == tmfPushMessage) {
            if (BuildConfig.LOG_DEBUG) {
                Log.e("TMF_PUSH", "ShowPushNotification with tmfPushMessage null! Please check the data.");
            }
            return;
        }
        openNotificationChannel(context);

        int notifyId = (int) (System.currentTimeMillis() + mIncrementId++);//notify_id取值为时间+自增Id
        if (BuildConfig.LOG_DEBUG) {
            Log.i("TMF_PUSH", "notify_id=" + notifyId + "，mIncrementId=" + mIncrementId);
        }
        Intent intentClick = new Intent(context, CustomNCBroadcastReceiver.class);
        intentClick.setAction(CustomNCBroadcastReceiver.NOTIFY_CLICK_ACTION);
        intentClick.putExtra(CustomNCBroadcastReceiver.NOTIFY_ID, notifyId);
        intentClick.putExtra(CustomNCBroadcastReceiver.NOTIFY_MESSAGE, tmfPushMessage);
        PendingIntent pendingIntentClick = PendingIntent
                .getBroadcast(context, notifyId, intentClick, PendingIntent.FLAG_ONE_SHOT);

        Intent intentCancel = new Intent(context, CustomNCBroadcastReceiver.class);
        intentCancel.setAction(CustomNCBroadcastReceiver.NOTIFY_CLEAR_ACTION);
        intentCancel.putExtra(CustomNCBroadcastReceiver.NOTIFY_ID, notifyId);
        intentCancel.putExtra(CustomNCBroadcastReceiver.NOTIFY_MESSAGE, tmfPushMessage);
        PendingIntent pendingIntentCancel = PendingIntent
                .getBroadcast(context, notifyId, intentCancel, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(context, channelId);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context);
        }
        notificationBuilder.setContentTitle(tmfPushMessage.getTitle())
                .setContentText(tmfPushMessage.getContent())
                .setAutoCancel(true)
                .setContentIntent(pendingIntentClick)
                .setDeleteIntent(pendingIntentCancel)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setSmallIcon(
                        context.getResources().getIdentifier("icon_tabbar_util", "mipmap", context.getPackageName()))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        context.getResources().getIdentifier("mipush_logo", "mipmap", context.getPackageName())));

        mNotificationManager.notify(notifyId, notificationBuilder.build());
        //上报通知栏事件-展示
        PushCenter.getCustomNCReporter().reportNCEvent(tmfPushMessage, ICustomNCReporter.NC_EVENT_SHOW);
        if (BuildConfig.LOG_DEBUG) {
            Log.d("TMF_PUSH", "custom notification show");
        }
    }

    private void createNotification(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(
                    new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    private void openNotificationChannel(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(channelId);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
