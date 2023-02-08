package com.tencent.tmf.module.colorlog;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.colorlog.api.ColorLogger;
import com.tencent.tmf.colorlog.api.IColorLogUploadCallback;
import com.tencent.tmf.common.activity.TopBarActivity;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Destination(
        url = "portal://com.tencent.tmf.module.colorlog/color-log-activity",
        launcher = Launcher.ACTIVITY,
        description = "日志"
)
public class ColorLogActivity extends TopBarActivity implements View.OnClickListener {

    private static final String TAG = "ColorLog";
    private QMUIRoundButton mUploadBtn;
    private QMUIRoundButton mUploadTodayBtn;
    private QMUIRoundButton mUploadTestBtn;
    private EditText mInputEdt;
    private TextView mResultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mTopBar.setTitle(getResources().getString(R.string.color_log));
        mUploadBtn = findViewById(R.id.btn_upload_log);
        mUploadTodayBtn = findViewById(R.id.btn_upload_log_today);
        mUploadTestBtn = findViewById(R.id.btn_upload_log_test);
        mInputEdt = findViewById(R.id.edt_input);
        mResultTv = findViewById(R.id.tv_result);
        mUploadBtn.setOnClickListener(this);
        mUploadTodayBtn.setOnClickListener(this);
        mUploadTestBtn.setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_color_log, null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_upload_log) {
            String uploadDate = mInputEdt.getText().toString();
            if (!TextUtils.isEmpty(uploadDate)) {
                String preText = getResources().getString(R.string.color_log_info_pre);
                String midText = getResources().getString(R.string.color_log_info_mid);
                ColorLogger.upload(uploadDate, new IColorLogUploadCallback() {
                    @Override
                    public void onUploadResult(int errorCode) {
                        Log.i("ColorLog", "manual upload color log result = " + errorCode);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResultTv.setText(preText + uploadDate + midText + errorCode);
                            }
                        });
                    }
                });
            }
        } else if (id == R.id.btn_upload_log_today) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
            String uploadDate = dateFormat.format(System.currentTimeMillis());
            if (!TextUtils.isEmpty(uploadDate)) {
                String preText = getResources().getString(R.string.color_log_info_pre);
                String midText = getResources().getString(R.string.color_log_info_mid);
                ColorLogger.upload(uploadDate, new IColorLogUploadCallback() {
                    @Override
                    public void onUploadResult(int errorCode) {
                        Log.i("ColorLog", "manual upload color log result = " + errorCode);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResultTv.setText(preText + uploadDate + midText + errorCode);
                            }
                        });
                    }
                });
            }
        } else if (id == R.id.btn_upload_log_test) {
            ColorLogger.d(TAG, "ColorLogger LEVEL_DEBUG Demo");
            ColorLogger.i(TAG, "ColorLogger LEVEL_INFO Demo");
            ColorLogger.w(TAG, "ColorLogger LEVEL_WARNING Demo");
            ColorLogger.e(TAG, "ColorLogger LEVEL_ERROR Demo");
            ColorLogger.flush(true);
        }
    }
}
