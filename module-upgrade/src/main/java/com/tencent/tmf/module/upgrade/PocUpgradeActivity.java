package com.tencent.tmf.module.upgrade;

import static com.tencent.tmf.upgrade.api.IUpgradeChecker.HAS_NEW_VERSION;
import static com.tencent.tmf.upgrade.api.IUpgradeChecker.NETWORK_ERROR;
import static com.tencent.tmf.upgrade.api.IUpgradeChecker.NO_NEW_VERSION;
import static com.tencent.tmf.upgrade.api.IUpgradeChecker.RESOURCE_LIMIT;
import static com.tencent.tmf.upgrade.api.UpgradeInfo.UPGRADE_TYPE_FORCED;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.upgrade.api.IAutoCheckCallback;
import com.tencent.tmf.upgrade.api.IUpgradeChecker;
import com.tencent.tmf.upgrade.api.ProtocolType;
import com.tencent.tmf.upgrade.api.UpgradeInfo;
import com.tencent.tmf.upgrade.api.UpgradeService;

import java.io.File;

@Destination(
        url = "portal://com.tencent.tmf.module.upgrade/upgrade-activity",
        launcher = Launcher.ACTIVITY,
        description = "Shark调试用"
)
public class PocUpgradeActivity extends TopBarActivity implements View.OnClickListener, IAutoCheckCallback {

    private static final String TAG = "TMF_Upgrade";

    private QMUITipDialog mTipsDialog;
    private QMUIDialog mUpgradeDialog;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private String mUpgradeApkUrl;
    private String mUpgradeTitle;
    private String mUpgradeMsg;
    private boolean mForceUpgradeFlag;

    private static final String NOTIFICATION_UPGRADE = "notification_upgrade";
    private static final String NOTIFICATION_APK_URL = "notification_apk_url";

    private static final int TYPE_DIALOG = 1;
    private static final int TYPE_NOTIFICATION = 2;

    private static final int MSG_UPGRADE_PROGRESS = 0x01;
    private static final int MSG_SHOW_PROGRESS = 0x02;
    private static final int MSG_INSTALL_APK = 0x03;
    private static final int MSG_MANUAL_CHECK_TIME_OUT = 0x04;
    private static final int MSG_SHOW_UPGRADE_DIALOG = 0x05;
    private static final int MSG_SHOW_UPGRADE_NOTIFICATION = 0x06;
    private static final int MSG_SHOW_TOAST = 0x07;
    private static final int MSG_DOWNLOAD_FAILED = 0x08;
    private static final int MSG_UPGRADE_CANCELED = 0x09;
    private Handler mUpgradeHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPGRADE_PROGRESS:
                    if (mUpgradeDownloadTask.isCancelFlag()) {
                        mUpgradeHandler.removeMessages(MSG_UPGRADE_PROGRESS);
                        return;
                    }
                    // 刷新进度条
                    if (msg.arg2 == TYPE_DIALOG) {
                        showOrUpdateDownloadProgressDialog(msg.arg1);
                    } else if (msg.arg2 == TYPE_NOTIFICATION) {
                        mNotificationBuilder.setProgress(100, msg.arg1, false);
                        mNotificationBuilder.setContentText(getStringById(R.string.module_upgrade_5) + msg.arg1 + "%");
                        mNotificationManager.notify(111, mNotificationBuilder.build());
                    }
                    break;
                case MSG_SHOW_PROGRESS:
                    // 展示下载进度条
                    showOrUpdateDownloadProgressDialog(0);

                    break;
                case MSG_INSTALL_APK:
                    // 安装 APK 文件
                    InstallUtil.installApp(PocUpgradeActivity.this, (File) msg.obj);
                    UpgradeService.getUpgradePhaseMonitor().onInstallClick(true);//上报开始安装
                    UpgradeService.getUpgradePhaseMonitor().onStartInstall();//上报开始安装
                    break;
                case MSG_MANUAL_CHECK_TIME_OUT:
                    // 检查超时
                    mTipsDialog.dismiss();
                    Toast.makeText(PocUpgradeActivity.this,
                            getResources().getString(R.string.check_upgrade_network_error), Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SHOW_UPGRADE_DIALOG:
                    if (mUpgradeHandler.hasMessages(MSG_MANUAL_CHECK_TIME_OUT)) {
                        mUpgradeHandler.removeMessages(MSG_MANUAL_CHECK_TIME_OUT);
                    }
                    mTipsDialog.dismiss();
                    showUpgradeDialog();
                    UpgradeService.getUpgradePhaseMonitor().onUpgradeShow();//上报展示更新
                    break;
                case MSG_SHOW_UPGRADE_NOTIFICATION:
                    if (mUpgradeHandler.hasMessages(MSG_MANUAL_CHECK_TIME_OUT)) {
                        mUpgradeHandler.removeMessages(MSG_MANUAL_CHECK_TIME_OUT);
                    }
                    showUpgradeNotification();
                    UpgradeService.getUpgradePhaseMonitor().onUpgradeShow();//上报展示更新
                    break;
                case MSG_SHOW_TOAST:
                    if (mUpgradeHandler.hasMessages(MSG_MANUAL_CHECK_TIME_OUT)) {
                        mUpgradeHandler.removeMessages(MSG_MANUAL_CHECK_TIME_OUT);
                    }
                    mTipsDialog.dismiss();
                    showMsgDialog(msg.obj.toString());
                    break;
                case MSG_DOWNLOAD_FAILED:
                    if (mUpgradeHandler.hasMessages(MSG_MANUAL_CHECK_TIME_OUT)) {
                        mUpgradeHandler.removeMessages(MSG_MANUAL_CHECK_TIME_OUT);
                    }
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    showMsgDialog(msg.obj.toString());
                    break;
                case MSG_UPGRADE_CANCELED:
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private TextView mAppVersion;
    private UpgradeDownloadTask mUpgradeDownloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getStringById(R.string.module_upgrade_6));
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS_STORAGE, 0);
            }
        }

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

        //升级服务初始化
        UpgradeService.init(this, this, protocalType);

        mAppVersion = findViewById(R.id.tv_app_version);
        mAppVersion.setText(getStringById(R.string.module_upgrade_7) + getVersonName(this) + "." + com.tencent.tmf.common.BuildConfig.BUILD_NO);
        findViewById(R.id.manual_upgrade_check).setOnClickListener(this);
        findViewById(R.id.auto_upgrade_check).setOnClickListener(this);
        //升级loading view
        mTipsDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getResources().getString(R.string.check_upgrade_ing))
                .create();

        //升级通知栏相关
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationManager.createNotificationChannel(
                    new NotificationChannel("custom_nt_channel", "channel_name", NotificationManager.IMPORTANCE_HIGH));
            mNotificationBuilder = new NotificationCompat.Builder(this, "custom_nt_channel");
        } else {
            mNotificationBuilder = new NotificationCompat.Builder(this);
        }
        mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        //从通知栏进入
        if (getIntent().getBooleanExtra(NOTIFICATION_UPGRADE, false)) {
            UpgradeService.getUpgradePhaseMonitor().onUpgradeClick(true);//上报更新通知栏点击
            startDownloadApk(getIntent().getStringExtra(NOTIFICATION_APK_URL), TYPE_NOTIFICATION);
        }

        UpgradeService.autoUpgradeCheck();
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_upgrade_poc, null);
    }

    private void showUpgradeNotification() {
        Intent intent = new Intent(this, PocUpgradeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(NOTIFICATION_UPGRADE, true);
        intent.putExtra(NOTIFICATION_APK_URL, mUpgradeApkUrl);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder.setContentTitle(mUpgradeTitle)
                .setContentText(mUpgradeMsg)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        mNotificationManager.notify(111, mNotificationBuilder.build());
    }

    private void showMsgDialog(String msg) {
        if (isFinishing()) {
            return;
        }

        new QMUIDialog.MessageDialogBuilder(this)
                .setMessage(msg)
                .addAction(0, getStringById(R.string.module_upgrade_8), QMUIDialogAction.ACTION_PROP_NEUTRAL, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog qmuiDialog, int i) {
                        qmuiDialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create()
                .show();
    }


    ProgressDialog progressDialog;

    private void showOrUpdateDownloadProgressDialog(int progress) {
        if (isFinishing()) {
            return;
        }

        if (progressDialog == null || !progressDialog.isShowing()) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getStringById(R.string.module_upgrade_9) + progress + "%...)");
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getStringById(R.string.module_upgrade_10), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mUpgradeDownloadTask != null) {
                        mUpgradeDownloadTask.cancelDownload();
//                        progressDialog.dismiss();

                    }
                }
            });
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {

            progressDialog.setMessage(getStringById(R.string.module_upgrade_9) + progress + "%...)");
        }

        if (progress == 100) {
            progressDialog.dismiss();
        }
    }

    private void showUpgradeDialog() {
        if (isFinishing()) {
            return;
        }

        QMUIDialog.MessageDialogBuilder dialogBuilder = new QMUIDialog.MessageDialogBuilder(this)
                .setTitle(mUpgradeTitle)
                .setMessage(mUpgradeMsg)
                .addAction(0, getStringById(R.string.module_upgrade_11), QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(PocUpgradeActivity.this,
                                getResources().getString(R.string.upgrade_apk_downloading), Toast.LENGTH_SHORT).show();
                        UpgradeService.getUpgradePhaseMonitor().onUpgradeClick(true);//上报更新弹框点击
                        mUpgradeHandler.obtainMessage(MSG_SHOW_PROGRESS).sendToTarget();
                        startDownloadApk(mUpgradeApkUrl, TYPE_DIALOG);
                        dialog.dismiss();
                    }
                });
        if (!mForceUpgradeFlag) { //非强制升级
            dialogBuilder.addAction(getStringById(R.string.module_upgrade_10), new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    dialog.dismiss();
                    UpgradeService.getUpgradePhaseMonitor().onUpgradeClick(false);//上报点击更新弹框取消
                }
            });
        } else {
            dialogBuilder.setCancelable(false).setCanceledOnTouchOutside(false);
        }
        mUpgradeDialog = dialogBuilder.create();
        mUpgradeDialog.show();
    }

    private void startDownloadApk(String apkUrl, final int type) {

        mUpgradeDownloadTask = new UpgradeDownloadTask(this);
        mUpgradeDownloadTask.setDownloadListener(new UpgradeDownloadListener() {
            @Override
            public void onProgressChanged(int progress) {
                Message msg = new Message();
                msg.what = MSG_UPGRADE_PROGRESS;
                msg.arg1 = progress;
                msg.arg2 = type;
                mUpgradeHandler.sendMessage(msg);
            }

            @Override
            public void onDownloadSucceed(File file) {
                Message msg = new Message();
                msg.what = MSG_INSTALL_APK;
                msg.obj = file;
                mUpgradeHandler.sendMessage(msg);
                UpgradeService.getUpgradePhaseMonitor().onDownloadResult(true);//上报下载成功
            }

            @Override
            public void onDownloadField() {
                Message msg = new Message();
                msg.what = MSG_DOWNLOAD_FAILED;
                msg.obj = getStringById(R.string.module_upgrade_12);
                mUpgradeHandler.sendMessage(msg);
                UpgradeService.getUpgradePhaseMonitor().onDownloadResult(false);//上报下载成功
            }

            @Override
            public void onCanceled() {

                Message msg = new Message();
                msg.what = MSG_UPGRADE_CANCELED;
                mUpgradeHandler.sendMessage(msg);
            }
        });
        mUpgradeDownloadTask.execute(apkUrl);
        UpgradeService.getUpgradePhaseMonitor().onStartDownload();//上报开始下载
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.manual_upgrade_check) {
            mTipsDialog.show();
            UpgradeService.manualCheckUpgrade(new IUpgradeChecker.UpgradeCheckCallback() {
                @Override
                public void onResult(int resultCode, UpgradeInfo upgradeInfo) {
                    Message message = new Message();
                    if (resultCode == HAS_NEW_VERSION && null != upgradeInfo) {
                        mUpgradeApkUrl = upgradeInfo.getApkUrl();
                        mUpgradeTitle =
                                TextUtils.isEmpty(upgradeInfo.getTitle()) ? getStringById(R.string.module_upgrade_13) : upgradeInfo.getTitle();
                        mUpgradeMsg = upgradeInfo.toString();
                        mForceUpgradeFlag = upgradeInfo.getType() == UPGRADE_TYPE_FORCED;
                        message.what = MSG_SHOW_UPGRADE_DIALOG;
                        Log.i(TAG, upgradeInfo.toString());
                    } else if (resultCode == NO_NEW_VERSION) {
                        message.what = MSG_SHOW_TOAST;
                        message.obj = getResources().getString(R.string.check_upgrade_latest_version_now);
                    } else if (resultCode == NETWORK_ERROR) {
                        message.what = MSG_SHOW_TOAST;
                        message.obj = getResources().getString(R.string.check_upgrade_network_error);
                    } else if (resultCode == RESOURCE_LIMIT) {
                        message.what = MSG_SHOW_TOAST;
                        message.obj = getResources().getString(R.string.check_upgrade_res_limit);
                    }
                    mUpgradeHandler.sendMessage(message);
                }
            });
            mUpgradeHandler.sendEmptyMessageDelayed(MSG_MANUAL_CHECK_TIME_OUT, 10000);
        } else if (v.getId() == R.id.auto_upgrade_check) {
            //注册升级检查服务，添加自动检查监听器
            UpgradeService.autoUpgradeCheck();
//            Toast.makeText(this, "开始自动监听升级单", Toast.LENGTH_SHORT).show();
        }
    }

    //以下为拦截弹框返回示例
    private boolean isForceUpgradeDialogShowing() {
        return null != mUpgradeDialog && mUpgradeDialog.isShowing() && mForceUpgradeFlag;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doOnBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void doOnBackPressed() {
        if (isForceUpgradeDialogShowing()) {
            return;
        }
        super.doOnBackPressed();
    }

    public static String getVersonName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }

    @Override
    public void onLimit(UpgradeInfo upgradeInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PocUpgradeActivity.this, getStringById(R.string.module_upgrade_18), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpgrade(UpgradeInfo upgradeInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PocUpgradeActivity.this, getStringById(R.string.module_upgrade_15), Toast.LENGTH_SHORT).show();
                if (null != upgradeInfo) {
                    mUpgradeApkUrl = upgradeInfo.getApkUrl();
                    mUpgradeTitle = upgradeInfo.getTitle();
                    mUpgradeMsg = upgradeInfo.toString();
                    mForceUpgradeFlag = upgradeInfo.getType() == UPGRADE_TYPE_FORCED;
                    mUpgradeHandler.obtainMessage(MSG_SHOW_UPGRADE_DIALOG).sendToTarget();
//                            mUpgradeHandler.obtainMessage(MSG_SHOW_UPGRADE_NOTIFICATION).sendToTarget();
                    Log.i(TAG, upgradeInfo.toString());
                }
            }
        });
    }
}
