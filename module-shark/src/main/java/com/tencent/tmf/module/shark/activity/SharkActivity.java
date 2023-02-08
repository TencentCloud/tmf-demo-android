package com.tencent.tmf.module.shark.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.config.AppDataUtil;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.common.storage.sp.SharkSp;
import com.tencent.tmf.module.shark.R;
import com.tencent.tmf.shark.api.IShark;
import com.tencent.tmf.shark.api.SharkConfig;

/**
 * Shark调试用
 */
@SuppressWarnings("checkstyle:EmptyLineSeparator")
@Destination(
        url = "portal://com.tencent.tmf.module.shark/shark-activity",
        launcher = Launcher.ACTIVITY,
        description = "Shark调试用"
)
public class SharkActivity extends TopBarActivity {

    private static String TAG = "SharkActivity";

    private Context appContext;
    private QMUICommonListItemView cbTcpConnect;//shark初始化时，是否允许tcp长连接
    private ViewGroup channelLayout;
    @SuppressWarnings("checkstyle:MultipleVariableDeclarations")
    private QMUICommonListItemView cbHttpChannel, cbTcpChannel, cbIpPassThrough;
    @SuppressWarnings("checkstyle:MultipleVariableDeclarations")
    private EditText etProductId, etCustomId, etTCPHost, etTcpPort, etHttpURL, etPublicKey, etPublicKeyId;
    @SuppressWarnings("checkstyle:MultipleVariableDeclarations")
    private QMUIRoundButton btnInit, btnRestart, btnCleanMsg;
    private TextView tvMsg;
    private String sharkPkg;
    private SharkConfig sharkConfig;
    //private SharkEventReceiver sharkEventReceiver;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getStringById(R.string.module_shark_4));
        appContext = getApplicationContext();
//sharkPkg = SharkService.getSharkPkg(appContext);
//sharkConfig = new EditableSharkConfig();

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        cbTcpConnect = findViewById(R.id.checkBox_tcp_connect);
        channelLayout = findViewById(R.id.layout_double_channel);
        cbHttpChannel = findViewById(R.id.checkBox_http);
        cbTcpChannel = findViewById(R.id.checkBox_tcp);
        cbIpPassThrough = findViewById(R.id.checkBox_ip_pass_through);

        etProductId = (EditText) findViewById(R.id.et_productId);
        etCustomId = (EditText) findViewById(R.id.et_customId);
        etPublicKey = (EditText) findViewById(R.id.et_publickey);
        etPublicKeyId = (EditText) findViewById(R.id.et_publickey_id);
        etTCPHost = (EditText) findViewById(R.id.et_tcphost);
        etTcpPort = (EditText) findViewById(R.id.et_tcpport);
        etHttpURL = (EditText) findViewById(R.id.et_httpurl);

        btnInit = findViewById(R.id.btn_init);
        btnRestart = findViewById(R.id.btn_restart);
        btnCleanMsg = findViewById(R.id.btn_cleanmsg);
        tvMsg = (TextView) findViewById(R.id.tv_msg);

//SharkConfigInfo configInfo = SharkService.getSharkConfigInfo();
//etProductId.setText("" + configInfo.productId);
//etCustomId.setText(configInfo.customId);
//etPublicKey.setText(configInfo.pubKey);
//etPublicKeyId.setText(configInfo.keyId);
//etTCPHost.setText(configInfo.tcpHost);
//etTcpPort.setText("" + configInfo.tcpPort);
//etHttpURL.setText(configInfo.httpUrl);

        cbTcpConnect.setText(getStringById(R.string.module_shark_5));
        boolean sharkTcpConnect = SharkSp.getInstance().getSharkTcpConnect();
        cbTcpConnect.getSwitch().setChecked(sharkTcpConnect);
        if (!sharkTcpConnect) {
            channelLayout.setVisibility(View.GONE);
        }
        cbTcpConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IShark shark = SharkService.getSharkWithInit();
                if (shark == null) {
                    showToast(getStringById(R.string.module_shark_6));
                    return;
                }

                cbTcpConnect.getSwitch().toggle();
                boolean isChecked = cbTcpConnect.getSwitch().isChecked();
                SharkSp.getInstance().putSharkTcpConnect(isChecked);
                if (isChecked) {
                    channelLayout.setVisibility(View.VISIBLE);
                } else {
                    channelLayout.setVisibility(View.GONE);
                }

                restart();
            }
        });

        cbHttpChannel.setText(getStringById(R.string.module_shark_7));
        cbHttpChannel.getSwitch().setChecked(SharkSp.getInstance().getSharkHttp());
        cbHttpChannel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IShark shark = SharkService.getSharkWithInit();
                if (shark == null) {
                    showToast(getStringById(R.string.module_shark_6));
                    return;
                }

                cbHttpChannel.getSwitch().toggle();
                boolean isChecked = cbHttpChannel.getSwitch().isChecked();
                shark.setHttpEnable(isChecked);
                SharkSp.getInstance().putSharkHttp(isChecked);
            }
        });

        cbTcpChannel.setText(getStringById(R.string.module_shark_8));
        cbTcpChannel.getSwitch().setChecked(SharkSp.getInstance().getSharkTcp());
        IShark shark = SharkService.getSharkWithInit();
        shark.setTcpEnable(SharkSp.getInstance().getSharkTcp());
        cbTcpChannel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IShark shark = SharkService.getSharkWithInit();
                if (shark == null) {
                    showToast(getStringById(R.string.module_shark_6));
                    return;
                }

                cbTcpChannel.getSwitch().toggle();
                boolean isChecked = cbTcpChannel.getSwitch().isChecked();
                shark.setTcpEnable(isChecked);
                SharkSp.getInstance().putSharkTcp(isChecked);
            }
        });

        cbIpPassThrough.setText(getStringById(R.string.module_shark_9));
        cbIpPassThrough.getSwitch().setChecked(SharkSp.getInstance().getIpPathThrough());
        cbIpPassThrough.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IShark shark = SharkService.getSharkWithInit();
                if (shark == null) {
                    showToast(getStringById(R.string.module_shark_6));
                    return;
                }

                cbIpPassThrough.getSwitch().toggle();
                boolean isChecked = cbIpPassThrough.getSwitch().isChecked();
                shark.setIpPassThroughEnable(isChecked);
                SharkSp.getInstance().putIpPassThrough(isChecked);
            }
        });

//btnInit.setOnClickListener(new OnClickListener() {
//
//@Override
//public void onClick(View v) {
//if (!cbHttpChannel.getSwitch().isChecked() && !cbTcpChannel.getSwitch().isChecked()) {
//showToast("至少选择一个通道！");
//return;
//}
//
//IShark shark = SharkService.getSharkWithInit();
//if (shark == null) {
//SharkService.init(sharkConfig);
//shark = SharkService.getSharkWithInit();
//} else {
//showToast("之前已经初始化过了，忽略本次！");
//}
//
//shark.setHttpEnable(cbHttpChannel.getSwitch().isChecked());
//shark.setTcpEnable(cbTcpChannel.getSwitch().isChecked());
//}
//});

//btnRestart.setOnClickListener(new OnClickListener() {
//
//@Override
//public void onClick(View v) {
//try {
//Process p = Runtime.getRuntime().exec("pm clear " + getPackageName());
//int status = p.waitFor();
//Log.i(TAG, "[tmf_debug]pm clear, status: " + status);
//} catch (Exception e) {
//e.printStackTrace();
//Log.w(TAG, "[tmf_debug]pm clear, e: ", e);
//}
//}
//
//});
//btnCleanMsg.setOnClickListener(new OnClickListener() {
//@Override
//public void onClick(View v) {
//clearMsg();
//}
//});
//tvMsg.setOnLongClickListener(new View.OnLongClickListener() {
//@Override
//public boolean onLongClick(View v) {
//ClipData myClip = ClipData.newPlainText("text", tvMsg.getText());
//clipboardManager.setPrimaryClip(myClip);
//showToast("内容复制成功");
//return false;
//}
//});
//
//
//sharkEventReceiver = new SharkEventReceiver();
//sharkEventReceiver.register(appContext);
    }

    private void restart() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle(getStringById(R.string.module_shark_10))
                .setMessage(getStringById(R.string.module_shark_11))
                .setCancelable(false)
//.addAction("取消", new QMUIDialogAction.ActionListener() {
//@Override
//public void onClick(QMUIDialog dialog, int index) {
//dialog.dismiss();
//}
//})
                .addAction(getStringById(R.string.module_shark_12), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        AppDataUtil.killMyself();
                    }
                })
                .create().show();
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_shark_4_tmf, null);
    }

    public void onDestroy() {
        //sharkEventReceiver.unregister(appContext);
        super.onDestroy();
    }

// ----------------------------------------------

    private StringBuilder sb = new StringBuilder();

    private void clearMsg() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb.setLength(0);
                tvMsg.setText(sb.toString());
            }
        });
    }

    private void println(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb.append("\n").append(msg);
                tvMsg.setText(sb.toString());
            }
        });
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }
    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    /**
//     * 接收shark的事件
//     * 此外只是为了打印日志
//     */
//private final class SharkEventReceiver extends BroadcastReceiver {
//private boolean hasRegister = false;
//
//@Override
//public void onReceive(final Context context, final Intent intent) {
//final String action = intent.getAction();
//if (action == null) {
//return;
//}
//
//String pkgName = context.getPackageName();
//String guidGotAction = String.format(SharkCommonConst.ACTION_GUID_GOT_FORMAT, pkgName);
//String skGotAction = String.format(SharkCommonConst.ACTION_SECRETKEY_GOT_FORMAT, pkgName);
//String fpEventAction = String.format(SharkCommonConst.ACTION_FP_EVENT_FORMAT, pkgName);
//
//String fromSharkPkg = intent.getStringExtra(SharkCommonConst.KEY_SHARK_PKG);
//if (fromSharkPkg == null || !fromSharkPkg.equals(sharkPkg)) {
//return;
//}
//
//if (guidGotAction.equals(action)) {
//int retCode = intent.getIntExtra(SharkCommonConst.KEY_RETCODE, -1);
//String guid = intent.getStringExtra(SharkCommonConst.KEY_GUID);
//
//println("【注册Guid结果】" + (retCode == 0 ? "成功" : "失败(" + retCode + ")"));
//println("guid: " + guid);
//} else if (skGotAction.equals(action)) {
//int retCode = intent.getIntExtra(SharkCommonConst.KEY_RETCODE, -1);
//println("【交换密钥结果】" +  (retCode == 0 ? "成功" : "失败(" + retCode + ")"));
//SecretKey secretKey = null;
//if (retCode == ESharkCode.ERR_NONE) {
//secretKey = new SecretKey();
//secretKey.symmetricAlgo = intent.getIntExtra(SharkCommonConst.KEY_SECRET_ALGO, 0);
//secretKey.randomKey = intent.getStringExtra(SharkCommonConst.KEY_SECRET_KEY);;
//secretKey.sessionId = intent.getStringExtra(SharkCommonConst.KEY_SECRET_SESSIONID);
//println("key: " + secretKey.randomKey + "\n" + "sessionId: " + secretKey.sessionId);
//}
//} else if (fpEventAction.equals(action)) {
//if (cbTcpChannel.getSwitch().isChecked()) {
//int retCode = intent.getIntExtra(SharkCommonConst.KEY_RETCODE, -1);
//String sharkIdent = intent.getStringExtra(SharkCommonConst.KEY_SHARK_IDENT);
//println("【首包997结果】" + (retCode == 0 ? "成功" : "失败(" + retCode + ")") + " shark: " + sharkIdent);
//}
//}
//}
//
//private void register(Context context) {
//if (!hasRegister) {
//try {
//String pkgName = context.getPackageName();
//IntentFilter intentFilter = new IntentFilter();
//intentFilter.addAction(String.format(SharkCommonConst.ACTION_GUID_GOT_FORMAT, pkgName));
//intentFilter.addAction(String.format(SharkCommonConst.ACTION_SECRETKEY_GOT_FORMAT, pkgName));
//intentFilter.addAction(String.format(SharkCommonConst.ACTION_FP_EVENT_FORMAT, pkgName));
//context.registerReceiver(this, intentFilter, Shark.getPermissionString(), null);
//hasRegister = true;
//} catch (Throwable e) {
//Log.e(TAG, "[shark_e][shark_guid] register: " + e, e);
//}
//}
//}
//
//private void unregister(Context context) {
//if (hasRegister) {
//try {
//context.unregisterReceiver(this);
//hasRegister = false;
//} catch (Throwable e) {
//Log.e(TAG, "[shark_e][shark_guid] unregister: " + e, e);
//}
//}
//}
//}

//    /**
//     * TMF的Shark配置
//     */
//private class EditableSharkConfig extends SharkConfig {
//
//@Override
//public int getProductId() {
//int productId = Integer.parseInt(etProductId.getText().toString());
//println("【使用Product ID】" + productId);
//return productId;
//}
//
//@Override
//public String getCustomId() {
//String customId = etCustomId.getText().toString();
//println("【使用Custom ID】" + customId);
//return customId;
//}
//
//@Override
//public String getPublicKey(int serverType) {
//String publicKey = etPublicKey.getText().toString();
//println("【使用公钥】" + publicKey);
//return publicKey;
//}
//
//@Override
//public String getPublicKeyId(int serverType) {
//String publicKeyId = etPublicKeyId.getText().toString();
//println("【使用公钥ID】" + publicKeyId);
//return publicKeyId;
//}
//
///**
// * 获取自定义的TCP通道域名和端口
// */
//@Override
//public List<String> getTcpDomainPorts(int serverType, int operator) {
//String tcpHost = etTCPHost.getText().toString();
//int tcpPort = Integer.parseInt(etTcpPort.getText().toString());
//
//List<String> custom = new ArrayList<String>();
//custom.add(tcpHost + ":" + tcpPort);
//
//println("【使用TCP HOST和端口】" + custom);
//return custom;
//}
//
///**
// * 获取自定义的HTTP URL
// */
//@Override
//public List<String> getHttpUrls(int serverType, int operator) {
//List<String> urls = new ArrayList<String>();
//urls.add(etHttpURL.getText().toString());
//
//println("【使用HTTP URL】" + urls);
//return urls;
//}
//}
}
