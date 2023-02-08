package com.tencent.tmf.module.shark.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.shark.R;
import com.tencent.tmf.shark.api.IGuidCallback;
import com.tencent.tmf.shark.api.IVidCallback;

@Destination(
        url = "portal://com.tencent.tmf.module.shark/device-id-activity",
        launcher = Launcher.ACTIVITY,
        description = "Shark调试用"
)
public class DeviceIdActivity extends TopBarActivity {

    private ClipboardManager mClipboardManager;
    private StringBuilder sb = new StringBuilder();
    private QMUIRoundButton mBtnGetGuid;
    private QMUIRoundButton mBtnGetVid;
    private TextView mDeviceIdTv;


    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_device_id, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopBar.setTitle(getStringById(R.string.module_shark_0));
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        mDeviceIdTv = findViewById(R.id.tv_device_id);
        mBtnGetGuid = findViewById(R.id.btn_get_guid);
        mBtnGetGuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharkService.getSharkWithInit().getGuidAsyn(new IGuidCallback() {
                    @Override
                    public void onCallback(int retCode, String guid) {
                        if (!TextUtils.isEmpty(guid)) {
                            println("guid: " + guid + getStringById(R.string.module_shark_1));
                            ClipData myClip = ClipData.newPlainText("guid", guid);
                            mClipboardManager.setPrimaryClip(myClip);
                            mBtnGetGuid.setClickable(false);
                            mBtnGetGuid.setAlpha(0.5f);
                        } else {
                            println(getStringById(R.string.module_shark_2) + guid + " retCode: " + retCode + "\n\n");
                        }
                    }
                });
            }
        });
        mBtnGetVid = findViewById(R.id.btn_get_vid);
        mBtnGetVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharkService.getSharkWithInit().getVidAsyn(new IVidCallback() {
                    @Override
                    public void onCallback(int retCode, String vid) {
                        if (!TextUtils.isEmpty(vid)) {
                            println("vid: " + vid + getStringById(R.string.module_shark_1));
                            ClipData myClip = ClipData.newPlainText("vid", vid);
                            mClipboardManager.setPrimaryClip(myClip);
                            mBtnGetVid.setClickable(false);
                            mBtnGetVid.setAlpha(0.5f);
                        } else {
                            println(getStringById(R.string.module_shark_3) + vid + " retCode: " + retCode + "\n\n");
                        }


                    }
                });
            }
        });
    }

    private synchronized void println(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb.append("\n").append(msg);
                mDeviceIdTv.setText(sb.toString());
            }
        });
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
}
