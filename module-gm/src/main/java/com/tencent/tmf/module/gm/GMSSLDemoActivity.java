package com.tencent.tmf.module.gm;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.tencent.tmf.base.api.shark.TMFGMSSLConfig;
import com.tencent.tmf.base.api.shark.TMFKONASMSSLContext;
import com.tencent.tmf.base.api.shark.TMFLazySocketFactory;
import com.tencent.tmf.common.activity.TopBarActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class GMSSLDemoActivity extends TopBarActivity implements View.OnClickListener {

    public static final String TEST_HOST_URL = "https://49.233.103.172:8081";
    //        public static final String TEST_HOST_URL = "https://140.143.135.173:8082";
//            String TEST_HOST_URL = "https://demo.gmssl.cn/";
//            String TEST_HOST_URL = "https://www.babassl.cn";
//        String TEST_HOST_URL = "https://sm2test.ovssl.cn/";
//        String defaultTEST_HOST_URLUrl = "https://sm2test.ovssl.cn/";
    private static final String TAG = "GMSSLDemoActivity";

    private TextView tvMsg;

    EditText mUrlEdit;

    Spinner mSpinner;
    Spinner mSpinnerCipher;
    Switch protocalSwith;
    Switch suitesSwitch;

    Handler mHandler = new Handler();


    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_gmssl, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String ti = getResources().getString(R.string.gm_demo_title);
        mTopBar.setTitle(ti);

        mSpinner = findViewById(R.id.sp_protocols);
        mSpinnerCipher = findViewById(R.id.sp_ciphers);
        protocalSwith = findViewById(R.id.protocolSwitch);
        protocalSwith.setChecked(false);
        protocalSwith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                protocalSwith.setChecked(!protocalSwith.isChecked());
                mSpinner.setEnabled(protocalSwith.isEnabled());
            }
        });
        mSpinner.setEnabled(false);
        suitesSwitch = findViewById(R.id.suitesSwitch);
        suitesSwitch.setChecked(false);
        suitesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                suitesSwitch.setChecked(!suitesSwitch.isChecked());
                mSpinnerCipher.setEnabled(suitesSwitch.isEnabled());
            }
        });
        mSpinnerCipher.setEnabled(false);

        findViewById(R.id.btn_oneway).setOnClickListener(this);
        findViewById(R.id.btn_direction).setOnClickListener(this);
        findViewById(R.id.btn_cleanmsg).setOnClickListener(this);
        mUrlEdit = findViewById(R.id.edit_url);
        tvMsg = findViewById(R.id.tv_msg);

        mUrlEdit.setText(TEST_HOST_URL);
    }

    @Override
    public void onClick(View v) {
        String reqText = getResources().getString(R.string.gm_ssl_demo_requesting);
        println(reqText);
        int id = v.getId();
        String url = mUrlEdit.getText().toString();
        if (id == R.id.btn_oneway) {
            onOnewayBtnClick(url);
        } else if (id == R.id.btn_direction) {
            onDirectionalBtnClick(url);
        } else if (id == R.id.btn_cleanmsg) {
            clearMsg();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(null);
    }

    private void onOnewayBtnClick(String url) {
        showMyDialog("onOnewayBtnClick...");
        new Thread() {
            @Override
            public void run() {
                httpRequestWith(url, createFactoryOneWay());
                closeMyDialog();
            }
        }.start();
    }

    private SSLSocketFactory createFactoryOneWay() {
        TMFGMSSLConfig config = new TMFGMSSLConfig.Builder("TLCPv1.1")
//                .setCAAssetFile("certs/CA.crt")
//                .setCSCert("certs/CS.crt", "certs/CS.Key", "1234")
//                .setCECert("certs/CE.crt", "certs/CE.Key", "1234")
                .build();
        return new TMFLazySocketFactory(new TLCPContext(this, config));
    }

    private SSLSocketFactory createFactoryDirectional() {
        TMFGMSSLConfig config = new TMFGMSSLConfig.Builder("TLCPv1.1")
                .setCAAssetFile("sm2certs/CA.crt")
                .setCSCert("sm2certs/CS.crt", "sm2certs/CS.key")
                .setCECert("sm2certs/CE.crt", "sm2certs/CE.key")
                .build();
        return new TMFLazySocketFactory(new TLCPContext(this, config));
    }

    private void httpRequestWith(String url, SSLSocketFactory sslSocketFactory) {
        String tag = getResources().getString(R.string.gm_ssl_demo_ssl);

        try {
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) new URL(url).openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setSSLSocketFactory(sslSocketFactory);
            httpURLConnection.setHostnameVerifier((s, sslSession) -> {

                println(tag+"verify server url = " + s);
                return true;
            });

            int statusCode = httpURLConnection.getResponseCode();
            println(tag+"statusCode: " + statusCode);
            InputStream is = httpURLConnection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int len;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) > 0) {
                byteArrayOutputStream.write(buf, 0, len);
            }
            println(tag+"[suite:]" + httpURLConnection.getCipherSuite());
            println(tag+"[success:]" + byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            println(tag+"[failed:]" + e);
        }
    }

    private void onDirectionalBtnClick(String url) {
        try {
            showMyDialog("onDirectionalBtnClick...");
            new Thread() {
                @Override
                public void run() {
                    httpRequestWith(url, createFactoryDirectional());
                    closeMyDialog();
                }
            }.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final StringBuilder sb = new StringBuilder();

    // ------------------------ dialog ----------------------------------
    private void println(final String msg) {
        Log.i(TAG, msg);
        runOnUiThread(() -> {
            sb.append("\n").append(msg);
            tvMsg.setText(sb.toString());
        });
    }

    private void clearMsg() {
        runOnUiThread(() -> {
            sb.setLength(0);
            tvMsg.setText(sb.toString());
        });
    }

    private AlertDialog mAlertDialog = null;

    private void showMyDialog(String msg) {
        mHandler.post(() -> {
            synchronized (GMSSLDemoActivity.this) {
                if (mAlertDialog != null) {
                    return;
                }
                mAlertDialog = new AlertDialog.Builder(GMSSLDemoActivity.this)
                        .setMessage(msg)
                        .setCancelable(false)
                        .create();
                mAlertDialog.show();
            }
        });
    }

    private void closeMyDialog() {
        mHandler.post(() -> {
            synchronized (GMSSLDemoActivity.this) {
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();
                    mAlertDialog = null;
                }
            }
        });
    }

    private class TLCPContext extends TMFKONASMSSLContext {

        public TLCPContext(Context context, TMFGMSSLConfig config) {
            super(context, config);
        }

        @Override
        public Socket configSocket(Socket s) {
            if (s instanceof SSLSocket) {
                if (protocalSwith.isChecked()) {
                    ((SSLSocket) s).setEnabledProtocols(new String[]{getSelectedProtocol(), "TLSv1.2"});
                }
                if (suitesSwitch.isChecked()) {
                    ((SSLSocket) s).setEnabledCipherSuites(new String[]{getSelectedCipherSuite()});
                }
            }
            return super.configSocket(s);
        }
    }

    String getSelectedProtocol() {
        String[] protocols = getResources().getStringArray(R.array.protocols);
        return protocols[mSpinner.getSelectedItemPosition()];
    }

    String getSelectedCipherSuite() {
        String[] suites = getResources().getStringArray(R.array.ciphers_array);
        return suites[mSpinnerCipher.getSelectedItemPosition()];
    }
}