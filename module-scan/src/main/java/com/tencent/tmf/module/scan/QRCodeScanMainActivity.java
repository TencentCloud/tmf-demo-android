package com.tencent.tmf.module.scan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.common.gen.ModuleScanConst;
import com.tencent.tmf.common.log.L;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.portal.annotations.Parameter;
import com.tencent.tmf.portal.annotations.Return;
import com.tencent.tmf.scan.api.QrCodeActivity;
import com.tencent.tmf.scan.api.ScanResult;

import java.util.ArrayList;
import java.util.List;

//import com.tencent.tmf.demo.webview.jsapi.ScanQRCode;
//import com.tencent.tmf.demo.x5.BrowserActivity;

@Destination(
        url = "portal://com.tencent.tmf.module.sacn/qrcode-activity",
        launcher = Launcher.ACTIVITY,
        description = "扫码页面",
        parameters = {
                @Parameter(name = "getResult", optional = true, type = Boolean.class, description = "获取扫码结果"),
        },
        returns = {
                @Return(name = "scanResult", optional = true, type = String.class, description = "扫码结果"),
        }
)
public class QRCodeScanMainActivity extends TopBarActivity implements View.OnClickListener {

    private static final String TAG = "QRCodeScanMainActivity";
    private static final int REQUEST_CODE_START_CAMERA = 103;

    private TextView mKeywordView;
    private EditText mKeywordInput;
    private SwitchCompat mAutoFlashSwitch;
    private View mAddButton;
    private View mStartButton;

    private ArrayList<String> mKeywords = new ArrayList<>();
    private boolean getResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopBar.setTitle(getResources().getString(R.string.module_scan_0));

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                getResult = bundle.getBoolean(ModuleScanConst.P_GET_RESULT, false);
            }
        }

        mStartButton = findViewById(R.id.start_camera);
        mAutoFlashSwitch = findViewById(R.id.auto_flash_switch);
        mKeywordInput = findViewById(R.id.edit_keyword);
        mAddButton = findViewById(R.id.add_keyword);
        mKeywordView = findViewById(R.id.list_keyword);

        mStartButton.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_qrcode_scan_main, null, false);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.start_camera) {
            final boolean autoFlash = mAutoFlashSwitch.isChecked();
            Intent intent = QrCodeActivity.from(this)
                    .enableAutoFlash(autoFlash)
                    .setFilters(mKeywords)
                    .getIntent();
            startActivityForResult(intent, REQUEST_CODE_START_CAMERA);
        } else if (id == R.id.add_keyword) {
            final String text = mKeywordInput.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                mKeywords.add(text);
                StringBuilder builder = new StringBuilder(mKeywordView.getText());
                builder.append("\n").append(text);
                mKeywordView.setText(builder.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_START_CAMERA && resultCode == RESULT_OK && data != null) {
            List<ScanResult> list = data.getParcelableArrayListExtra(QrCodeActivity.KEY_RESULT);
            L.d(list.toString());

            StringBuilder value = new StringBuilder();
            for (ScanResult result : list) {
                value.append(result.data).append("\n");
            }
            if (TextUtils.isEmpty(value.toString())) {
                return;
            }

            if (getResult) {
                //扫码后返回扫码结果
                Intent intent = new Intent();
                intent.putExtra(ModuleScanConst.R_SCAN_RESULT, value.toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                return;
            }

            //扫码后直接打开网页或显示内容
            if (value.toString().startsWith("http://") || value.toString().startsWith("https://")) {
                Portal.from(QRCodeScanMainActivity.this)
                        .url(ModuleH5containerConst.U_BROWSER_ACTIVITY)
                        .param(ModuleH5containerConst.P_URL, value.toString())
                        .launch();
                finish();
            } else {
                //dialog显示内容
                showMessageDialog(value.toString());
            }
        }
    }

    QMUIDialog msgDialog;

    private void showMessageDialog(String str) {

        if (msgDialog == null || !msgDialog.isShowing()) {

            msgDialog = new QMUIDialog.MessageDialogBuilder(this)
                    .setMessage(str)
                    .addAction(getResources().getString(R.string.module_scan_1), new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .create(R.style.QMUI_Dialog);
            msgDialog.show();
        }
    }
}
