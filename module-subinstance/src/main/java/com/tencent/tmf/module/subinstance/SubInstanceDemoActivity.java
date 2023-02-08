package com.tencent.tmf.module.subinstance;

import Protocol.MShark.SashimiHeader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.shark.api.ESharkCode;
import com.tencent.tmf.shark.api.IGuidCallback;
import com.tencent.tmf.shark.api.ISendHttpEntityCallback;
import com.tencent.tmf.shark.api.IShark;
import com.tencent.tmf.shark.api.SharkCommonConst;
import com.tencent.tmf.shark.api.SharkExtra;
import com.tencent.tmf.shark.api.SharkHttpEntity;
import com.tencent.tmf.shark.api.SharkRetCode;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 国密算法加解密示例
 */
@Destination(
        url = "portal://com.tencent.tmf.module.subinstance/subinstance-activity",
        launcher = Launcher.ACTIVITY,
        description = "多实例"
)
public class SubInstanceDemoActivity extends TopBarActivity {

    private static final String TAG = "SubInstanceDemoActivity";
    private TextView tvMsg;

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_instances, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getStringById(R.string.module_subinstance_0));
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        QMUIRoundButton btnRun = (QMUIRoundButton) findViewById(R.id.btn_shark);
        btnRun.setOnClickListener(v -> {
            SharkHttpEntity sharkHttpEntity = new SharkHttpEntity();
            sharkHttpEntity.params = new SashimiHeader();
            sharkHttpEntity.params.apiName = "TMFEcho";
            sharkHttpEntity.params.header = new HashMap<>();
            sharkHttpEntity.params.header.put("header_1", "h1");
            sharkHttpEntity.params.cookies = new HashMap<>();
            sharkHttpEntity.params.cookies.put("cookie_2", "value_2");
            sharkHttpEntity.params.cookies.put("cookie_1", "c1");
            sharkHttpEntity.params.query = new HashMap<>();
            sharkHttpEntity.params.query.put("isWap", "1");
            sharkHttpEntity.params.query.put("processCode", "AM0013");
            sharkHttpEntity.params.query.put("stewardNo", "9808858");
            sharkHttpEntity.data = "this is data".getBytes(StandardCharsets.UTF_8);
            sendByShark(sharkHttpEntity, "this is data", true);
        });

        QMUIRoundButton btnProfile = (QMUIRoundButton) findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(v -> {
            SubServices.getSubProfile().setUserId("subInstance");
            println("setUserId: subInstance \n");
        });

        QMUIRoundButton btnGuid = (QMUIRoundButton) findViewById(R.id.btn_guid);
        btnGuid.setOnClickListener(v -> {
            SubServices.getSubShark().getGuidAsyn(new IGuidCallback() {
                @Override
                public void onCallback(int errCode, String guid) {
                    if (errCode == 0) {
                        println("GUID:" + guid + "\n");
                    } else {
                        println("GUID error:" + errCode + "\n");
                    }
                }
            });
        });
    }

    private final StringBuilder sb = new StringBuilder();

    private void clearMsg() {
        runOnUiThread(() -> {
            sb.setLength(0);
            tvMsg.setText(sb.toString());
        });
    }

    private void println(final String msg) {
        Log.i(TAG, msg);
        runOnUiThread(() -> {
            sb.append("\n").append(msg);
            tvMsg.setText(sb.toString());
        });
    }

    private void sendByShark(SharkHttpEntity req, String dataText, boolean outputLog) {
        IShark shark = SubServices.getSubShark();

        if (outputLog) {
            println(getStringById(R.string.module_subinstance_1));
            println("apiName: " + req.params.apiName);
            println("----data(in text)-----");
            println("" + dataText);
            println("----data(in utf-8)----");
            println("" + (req.data != null ? Arrays.toString(req.data) : "null"));
            println("--------params--------");
            println("header: " + req.params.header);
            println("cookies: " + req.params.cookies);
            println("query: " + req.params.query);
            println("----------------------");
        }

        shark.sendHttpEntity(req, SharkCommonConst.DEFAULT, new ISendHttpEntityCallback() {
            @Override
            public void onFinish(int seqNo, int cmdId, SharkRetCode retCode, SharkHttpEntity resp,
                    SharkExtra sharkExtra) {
                if (retCode.isAccessLayerOk() // 接入层没有错误
                        && retCode.errorCode == ESharkCode.ERR_NONE // 业务层没有错误
                        && resp != null) { // 成功
                    try {
                        if (outputLog) {
                            println(getStringById(R.string.module_subinstance_2));
                            println("cmdId: " + cmdId);
                            println("httpCode: " + resp.httpCode);
                            println("----data(in text)-----");
                            println(resp.data != null ? new String(resp.data, "utf-8") : "null");
                            println("----------------------");
                            if (resp.params != null) {
                                println("--------params--------");
                                println("header: " + resp.params.header);
                                println("----------------------");
                            } else {
                                println("resp.params == null");
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        if (outputLog) {
                            println(getStringById(R.string.module_subinstance_3) + e.getMessage());
                        }
                    }
                } else {
                    if (outputLog) {
                        println(getStringById(R.string.module_subinstance_4) + seqNo + " cmdId: "
                                + cmdId + " retCode: " + retCode.errorCode + " errDomain: "
                                + retCode.errorDomain + " resp: " + resp);
                    }
                }
            }
        }, 100000); // 超时时间为100秒
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }

}
