package com.tencent.tmf.module.shark.activity;

//import Protocol.MTMFShark.SashimiHeader;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.gen.ModuleSharkConst;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.common.utils.JsonUtil;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.module.shark.R;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.shark.api.ESharkCode;
import com.tencent.tmf.shark.api.ISendHttpEntityCallback;
import com.tencent.tmf.shark.api.IShark;
import com.tencent.tmf.shark.api.SharkCommonConst;
import com.tencent.tmf.shark.api.SharkExtra;
import com.tencent.tmf.shark.api.SharkHttpEntity;
import com.tencent.tmf.shark.api.SharkRetCode;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import Protocol.MShark.SashimiHeader;

@Destination(
        url = "portal://com.tencent.tmf.module.shark/shark-poc-activity",
        launcher = Launcher.ACTIVITY,
        description = "网关测试"
)
public class SharkPocActivity extends TopBarActivity {

    private static String TAG = "SharkPocActivity";
    private static final int SHARK_CONFIG_VIEW_ID = 2333;
    private Spinner mCmdIdSpinner;
    private QMUIRoundButton mSendOnceBtn;
    private QMUIRoundButton mSendMoreBtn;
    private TextView mLogTv;
    private StringBuilder sb = new StringBuilder();
    private AutoCompleteTextView mApiName;
    private EditText mData;
    private EditText mHeaders;
    private EditText mCookies;
    private EditText mQueries;
    private EditText mPathParams;
    private EditText mTimeout;
    private PocCmdBean mCurPocCmdBean;
    private List<String> mSearchData = new ArrayList<>();
    private ArrayAdapter<String> mSearchAdapter = null;

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_shark_4_poc, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
        mTopBar.setTitle(getStringById(R.string.module_shark_4));
        mTopBar.addRightTextButton(getStringById(R.string.module_shark_10), SHARK_CONFIG_VIEW_ID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == SHARK_CONFIG_VIEW_ID) {
                    Portal.from(SharkPocActivity.this)
                            .url(ModuleSharkConst.U_SHARK_ACTIVITY)
                            .launch();
                }
            }
        });
        mCmdIdSpinner = findViewById(R.id.spinner_cmd_id);
        mCmdIdSpinner.setDropDownVerticalOffset(Utils.dp2px(this, 40));

        mApiName = findViewById(R.id.et_apiname);
        mData = findViewById(R.id.et_data);
        mHeaders = findViewById(R.id.et_headers);
        mCookies = findViewById(R.id.et_cookies);
        mQueries = findViewById(R.id.et_queries);
        mPathParams = findViewById(R.id.et_pathParams);
        mTimeout = findViewById(R.id.et_timeout);
        mLogTv = findViewById(R.id.tv_shark_log);

        mSendOnceBtn = findViewById(R.id.btn_send_once);
        mSendMoreBtn = findViewById(R.id.btn_send_more);

        mSearchData = ConfigSp.getInstance().getSharkApiName();
        mSearchAdapter = new ArrayAdapter<>(this, com.tencent.tmf.common.R.layout.item_auto_complete, mSearchData);
        mApiName.setAdapter(mSearchAdapter);
    }

    private void initData() {
        ArrayList<PocCmdBean> pocCmdBeans = PocCmdBean.initPocCmdBeans();

        mCurPocCmdBean = pocCmdBeans.get(0);
        mData.setText(mCurPocCmdBean.getData());
        mHeaders.setText(JsonUtil.map2json(mCurPocCmdBean.getHeaders()).toString());
        mCookies.setText(JsonUtil.map2json(mCurPocCmdBean.getCookies()).toString());
        mQueries.setText(JsonUtil.map2json(mCurPocCmdBean.getQueries()).toString());
        mPathParams.setText(JsonUtil.map2json(mCurPocCmdBean.getPathParams()).toString());

        CmdAdapter cmdAdapter = new CmdAdapter(this, pocCmdBeans);

        mCmdIdSpinner.setAdapter(cmdAdapter);
        mCmdIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                mCurPocCmdBean = pocCmdBeans.get(pos);
                mApiName.setText(mCurPocCmdBean.apiName);
                mData.setText(mCurPocCmdBean.getData());
                mHeaders.setText(JsonUtil.map2json(mCurPocCmdBean.getHeaders()).toString());
                mCookies.setText(JsonUtil.map2json(mCurPocCmdBean.getCookies()).toString());
                mQueries.setText(JsonUtil.map2json(mCurPocCmdBean.getQueries()).toString());
                mPathParams.setText(JsonUtil.map2json(mCurPocCmdBean.getPathParams()).toString());
                mLogTv.setText("");

                if ("RATELIMIT".equals(mCurPocCmdBean.apiName)) {
                    mSendMoreBtn.setVisibility(View.VISIBLE);
                } else {
                    mSendMoreBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSendOnceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMsg();
                getInputAndSendShark();
            }
        });

        mSendMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMsg();
                getInputAndSendSharkMoreTimes();
            }
        });
    }


    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private void getInputAndSendShark() {
        String apiName = mApiName.getText().toString();
        if (TextUtils.isEmpty(apiName) || apiName.trim().length() == 0) {
            ToastUtil.showToast(getStringById(R.string.module_shark_13));
            return;
        }
        try {
            String dataText = mData.getText().toString();
            String headers = mHeaders.getText().toString();
            String cookies = mCookies.getText().toString();
            String queries = mQueries.getText().toString();
            String pathParams = mPathParams.getText().toString();
            long timeout = Long.parseLong(mTimeout.getText().toString());
            SharkHttpEntity req = new SharkHttpEntity();
            req.data = dataText.getBytes("utf-8");
            req.params = new SashimiHeader();// 请求参数：header、cookie、query、apiName
            req.params.apiName = apiName;
            if (!TextUtils.isEmpty(headers)) {
                req.params.header = JsonUtil.json2Map(new JSONObject(headers));
            }
            if (!TextUtils.isEmpty(cookies)) {
                req.params.cookies = JsonUtil.json2Map(new JSONObject(cookies));
            }
            if (!TextUtils.isEmpty(queries)) {
                req.params.query = JsonUtil.json2Map(new JSONObject(queries));
            }
            if (!TextUtils.isEmpty(pathParams)) {
                req.params.pathParam = JsonUtil.json2Map(new JSONObject(pathParams));
            }
            if (apiName.equalsIgnoreCase("ROUTEWeight")) { //路由权重
                route1.set(0);
                route2.set(0);
                routeSum.set(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            sendByShark4RouteWeight(req);
                        }
                    }
                }).start();
            } else {
                sendByShark(req, dataText, true, timeout);
            }
        } catch (Exception e) {
            e.printStackTrace();
            println("getInputAndSendShark, exception: " + e);
        }
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private void getInputAndSendSharkMoreTimes() {
        String apiName = mApiName.getText().toString();
        if (TextUtils.isEmpty(apiName) || apiName.trim().length() == 0) {
            ToastUtil.showToast(getStringById(R.string.module_shark_13));
            return;
        }
        try {
            String dataText = mData.getText().toString();
            String headers = mHeaders.getText().toString();
            String cookies = mCookies.getText().toString();
            String queries = mQueries.getText().toString();
            String pathParams = mPathParams.getText().toString();
            long timeout = Long.parseLong(mTimeout.getText().toString());
            SharkHttpEntity req = new SharkHttpEntity();
            req.data = dataText.getBytes("utf-8");
            req.params = new SashimiHeader();// 请求参数：header、cookie、query、apiName
            req.params.apiName = apiName;
            if (!TextUtils.isEmpty(headers)) {
                req.params.header = JsonUtil.json2Map(new JSONObject(headers));
            }
            if (!TextUtils.isEmpty(cookies)) {
                req.params.cookies = JsonUtil.json2Map(new JSONObject(cookies));
            }
            if (!TextUtils.isEmpty(queries)) {
                req.params.query = JsonUtil.json2Map(new JSONObject(queries));
            }
            if (!TextUtils.isEmpty(pathParams)) {
                req.params.pathParam = JsonUtil.json2Map(new JSONObject(pathParams));
            }

            if (apiName.equalsIgnoreCase("RATELIMIT")) { //限流
                rateLimit1.set(0);
                rateLimit2.set(0);
                rateLimitSum.set(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < 10; j++) {
                            for (int i = 0; i < 20; i++) {
                                sendByShark4RateLimit(req);
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            } else if (apiName.equalsIgnoreCase("ROUTEWeight")) { //路由权重
                route1.set(0);
                route2.set(0);
                routeSum.set(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            sendByShark4RouteWeight(req);
                        }
                    }
                }).start();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            sendByShark(req, dataText, true, timeout);
                        }

                    }
                }).start();
            }

        } catch (Exception e) {
            println("getInputAndSendShark, exception: " + e);
        }
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    private void sendByShark(SharkHttpEntity req, String dataText, boolean outputLog, long timeout) {
        IShark shark = SharkService.getSharkWithInit();

        String apiName = req.params.apiName;
        boolean isFind = false;
        for (String text : mSearchData) {
            if (apiName.trim().equalsIgnoreCase(text)) {
                isFind = true;
            }
        }
        if (!isFind) {
            mSearchAdapter.add(apiName);
            mSearchData.add(apiName);
            ConfigSp.getInstance().putSharkApiName(mSearchData);
        }

        if (outputLog) {
            println(getStringById(R.string.module_shark_14));
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
                            println(getStringById(R.string.module_shark_15));
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
                            println(getStringById(R.string.module_shark_16) + e.getMessage());
                        }
                    }
                } else {
                    if (outputLog) {
                        println(getStringById(R.string.module_shark_17) + seqNo + " cmdId: "
                                + cmdId + " retCode: " + retCode.errorCode + " errDomain: "
                                + retCode.errorDomain + " resp: " + resp);
                    }
                }
            }
        }, timeout > 0 ? timeout : 100000); // 超时时间为100秒
    }


    private AtomicInteger rateLimit1 = new AtomicInteger(0);
    private AtomicInteger rateLimit2 = new AtomicInteger(0);
    private AtomicInteger rateLimitSum = new AtomicInteger(0);


    private void sendByShark4RateLimit(SharkHttpEntity req) {
        Log.e(TAG, "sendByShark4RateLimit");
        IShark shark = SharkService.getSharkWithInit();
        shark.sendHttpEntity(req, SharkCommonConst.DEFAULT, new ISendHttpEntityCallback() {
            @Override
            public void onFinish(int seqNo, int cmdId, SharkRetCode retCode, SharkHttpEntity resp,
                    SharkExtra sharkExtra) {
                if (retCode.isAccessLayerOk() // 接入层没有错误
                        && retCode.errorCode == ESharkCode.ERR_NONE // 业务层没有错误
                        && resp != null) { // 成功
                    try {
                        println(getStringById(R.string.module_shark_18) + resp.httpCode);
                    } catch (Exception e) {
                        e.printStackTrace();

                        println(getStringById(R.string.module_shark_16) + e.getMessage());
                    }

                    rateLimit1.getAndIncrement();
                    rateLimitSum.getAndIncrement();
                    if (rateLimitSum.get() == 200) {
                        println(getStringById(R.string.module_shark_19) + rateLimit1.get() + getStringById(R.string.module_shark_20) + rateLimit2.get() + getStringById(R.string.module_shark_21));
                    }
                } else {
                    println(getStringById(R.string.module_shark_22) + retCode);
                    Log.e(TAG, getStringById(R.string.module_shark_22) + retCode);
                    rateLimit2.getAndIncrement();
                    rateLimitSum.getAndIncrement();
                    if (rateLimitSum.get() == 200) {
                        println(getStringById(R.string.module_shark_19) + rateLimit1.get() + getStringById(R.string.module_shark_20) + rateLimit2.get() + getStringById(R.string.module_shark_21));
                    }
                }
            }
        }, 3000); // 超时时间为3秒
    }


    private AtomicInteger route1 = new AtomicInteger(0);
    private AtomicInteger route2 = new AtomicInteger(0);
    private AtomicInteger routeSum = new AtomicInteger(0);

    private void sendByShark4RouteWeight(SharkHttpEntity req) {
        IShark shark = SharkService.getSharkWithInit();
        shark.sendHttpEntity(req, SharkCommonConst.DEFAULT, new ISendHttpEntityCallback() {
            @Override
            public void onFinish(int seqNo, int cmdId, SharkRetCode retCode, SharkHttpEntity resp,
                    SharkExtra sharkExtra) {
                if (retCode.isAccessLayerOk() // 接入层没有错误
                        && retCode.errorCode == ESharkCode.ERR_NONE // 业务层没有错误
                        && resp != null) { // 成功
                    try {

                        String result = resp.data != null ? new String(resp.data, "utf-8") : "null";
                        Log.e(TAG,"result:" + result);
                        if (result.contains("apiName=echo2")) {
                            println("ROUTEWeight2"+getStringById(R.string.module_shark_23)+"，httpCode: " + resp.httpCode);
                            route2.getAndIncrement();
                        } else {
                            println("ROUTEWeight1"+getStringById(R.string.module_shark_23)+"，httpCode: " + resp.httpCode);
                            route1.getAndIncrement();
                        }

                        routeSum.getAndIncrement();

                        if (routeSum.get() == 20) {
                            println(getStringById(R.string.module_shark_24) + route1.get() + getStringById(R.string.module_shark_26) + route2.get());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        println(getStringById(R.string.module_shark_25) + e.getMessage());

                    }
                } else {
                    println(getStringById(R.string.module_shark_17) + seqNo + " cmdId: " + cmdId
                            + " retCode: " + retCode.errorCode + " errDomain: "
                            + retCode.errorDomain + " resp: " + resp);
                }
            }
        }, 3000); // 超时时间为3秒
    }


    private synchronized void println(final String msg) {
        Log.e(TAG, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb.append("\n").append(msg);
                mLogTv.setText(sb.toString());
            }
        });
    }

    private void clearMsg() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb.setLength(0);
                mLogTv.setText(sb.toString());
            }
        });
    }
}
