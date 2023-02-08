package com.tencent.tmf.demo.hotpatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.common.utils.AppUtil;
import com.tencent.tmf.common.utils.LibraryUtil;
import com.tencent.tmf.hotpatch.api.ValueCallback;
import com.tencent.tmf.hotpatch.api.controller.HotPatchSharkController;
import com.tencent.tmf.hotpatch.impl.utils.ObjectUtils;
import com.tencent.tmf.hotpatch.protocolnew.MHotRepair.AndroidHotRepairInfo;
import com.tencent.tmf.hotpatch.protocolnew.MHotRepair.SCAndroidHotRepair;
import com.tencent.tmf.hotpatchcore.HotPatch;
import com.tencent.tmf.hotpatchcore.Protocol.ProtocolType;
import com.tencent.tmf.module.hotpatch.HotPatchPullActivity;
import com.tencent.tmf.module.hotpatch.R;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;

import java.io.IOException;
import java.util.ArrayList;


@Destination(
        url = "portal://com.tencent.tmf.demo.hotpatch/hotpatch-test-activity",
        launcher = Launcher.ACTIVITY,
        description = "热修复"
)
public class HotPatchTestActivity extends TopBarActivity implements ValueCallback<SCAndroidHotRepair> {

    private static final String TAG = "HotPatchTestActivity";

    static {
        LibraryUtil.loadLibrary("hello");
    }

    public static native String sayHello();

    private ImageView mTestResImg;
    private TextView mTestJavaText;
    private TextView mTestSoText;
    private TextView mTestVersionText;
    private QMUIRoundButton mPullHotfixBtn; // 拉取
    private QMUIRoundButton mResetBtn; //重启

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_hotpatch_test, null);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getResources().getString(R.string.hotpatch_test_activity_title);
        mTopBar.setTitle(title);

        mTestJavaText = findViewById(R.id.tv_test_java);
        // mTestJavaText.setText("修复状态：未修复");
//        mTestJavaText.setText("修复状态：已修复");

        mTestResImg = findViewById(R.id.iv_test_res);
        mTestResImg.setImageResource(R.mipmap.ic_hotpatch_test_img);

        mTestSoText = findViewById(R.id.tv_test_so);
        mTestSoText.setText(sayHello());

        String versionName = AppUtil.getVersionName(this);
        int buildNo = com.tencent.tmf.common.BuildConfig.BUILD_NO;
        mTestVersionText = findViewById(R.id.tv_test_version);
        mTestVersionText.setText("version: " + versionName + "." + buildNo);

        //重启
        mResetBtn = findViewById(R.id.btn_reset_app);
        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Process p = execRuntimeProcess("pm clear com.tencent.tmf.demo");
//                    if (p == null) {
//                        Toast.makeText(HotPatchTestActivity.this, "重置失败", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(HotPatchTestActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
//
//                    }
                killMyself();
            }
        });

        // 拉取
        mPullHotfixBtn = findViewById(R.id.btn_pull_hotfix);
        mPullHotfixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotPatch.pullPatch();
                TinkerLog.i(TAG, "【pull patch】 start request backend...");
            }
        });

        HotPatchSharkController.get().addCallbacks(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HotPatchSharkController.get().removeCallbacks(this);
    }

    public static Process execRuntimeProcess(String commond) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(commond);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static void killMyself() {
        //这里可以重启你的应用程序，我的app中有service，所以我只要杀死进程就自动重启了。
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onResult(SCAndroidHotRepair scAndroidHotfix) {
        TinkerLog.i(TAG, "【pull patch】backend response...");
        if (ObjectUtils.isEmpty(scAndroidHotfix)) {
            TinkerLog.i(TAG, "【pull patch】backend response-> no data");
            return;
        }
        ArrayList<AndroidHotRepairInfo> infoList = scAndroidHotfix.infoList;
        TinkerLog.i(TAG, "【pull patch】 hotpatch exec list--------------------------------");
        if (ObjectUtils.isNotEmpty(infoList)) {
            for (AndroidHotRepairInfo hotfixInfo : infoList) {
                TinkerLog.i(TAG, hotfixInfo.toString());
            }
        } else {
            TinkerLog.e(TAG, "【pull patch】hotpatch exec list is empty!!!");
        }

        TinkerLog.i(TAG, "【pull patch】cancel hotpath list--------------------------------");
        ArrayList<AndroidHotRepairInfo> undoList = scAndroidHotfix.undoList;
        if (ObjectUtils.isNotEmpty(undoList)) {
            for (AndroidHotRepairInfo hotfixInfo : undoList) {
                TinkerLog.i(TAG, hotfixInfo.toString());
            }
        } else {
            TinkerLog.e(TAG, "【pull patch】cancel hotpatch list is empty!!!");
        }
    }
}
