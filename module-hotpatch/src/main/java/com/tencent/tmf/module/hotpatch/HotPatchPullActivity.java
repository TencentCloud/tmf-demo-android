package com.tencent.tmf.module.hotpatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.hotpatchcore.api.HotPatchManager;

/**
 * HotPatchPullActivity
 * <p>
 * Created by lonrinelin on 2019/4/24.
 * Describe:
 */
@SuppressWarnings("checkstyle:EmptyLineSeparator")
public class HotPatchPullActivity extends TopBarActivity {

    private static final String TAG = "HotPatchPullActivity";

    @SuppressWarnings("checkstyle:MultipleVariableDeclarations")
    private EditText mBaseIDEditText, mVersionEditText, mUrlEditText, mMD5EditText, mSizeEditText;
    @SuppressWarnings("checkstyle:MultipleVariableDeclarations")
    private QMUIRoundButton mDownloadUpgradeBtn, mDownloadBtn, mUpgradeBtn, mDeleteBtn, mCleanPatchesBtn;

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_hotpatch_pull, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(this.getResources().getString(R.string.hotpatch_top_title));
        mBaseIDEditText = (EditText) findViewById(R.id.patch_baseid);
        mBaseIDEditText.setText(BuildConfig.TINKER_ID);
        mVersionEditText = (EditText) findViewById(R.id.patch_version);
        mUrlEditText = (EditText) findViewById(R.id.patch_url);
        mMD5EditText = (EditText) findViewById(R.id.patch_md5);
        mSizeEditText = (EditText) findViewById(R.id.patch_size);
        mDownloadUpgradeBtn = (QMUIRoundButton) findViewById(R.id.patch_download_upgrade);
        mDownloadBtn = (QMUIRoundButton) findViewById(R.id.patch_download);
        mUpgradeBtn = (QMUIRoundButton) findViewById(R.id.patch_upgrade);
        mDeleteBtn = (QMUIRoundButton) findViewById(R.id.patch_delete);
        mCleanPatchesBtn = (QMUIRoundButton) findViewById(R.id.patch_clean_patches);
        mDownloadUpgradeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TinkerLog.i(TAG, "download and patch");
                downloadPatch(true);
            }
        });
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TinkerLog.i(TAG, "download ");
                downloadPatch(false);
            }
        });
        mUpgradeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TinkerLog.i(TAG, "merge patch");
                // TEST_lonrinelin
                if (HotPatchManager.USE_LOCAL_TEST_PATCH) {
                    HotPatchManager.getInstance().startUpgradeLocalPatch();
                    return;
                }
                try {
                    String baseID = mBaseIDEditText.getText().toString();
                    int version = Integer.parseInt(mVersionEditText.getText().toString());
                    HotPatchManager.getInstance().startUpgradePatch(baseID, version);
                } catch (Exception e) {
                    String text = getResources().getString(R.string.hotpatch_pull_activity_toast);
                    ToastUtil.showToast(text);
                }
            }
        });
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TinkerLog.i(TAG, "delete download patch package");
                try {
                    String baseID = mBaseIDEditText.getText().toString();
                    int version = Integer.parseInt(mVersionEditText.getText().toString());
                    HotPatchManager.getInstance().startDeleteDownloadedPatch(baseID, version);
                } catch (Exception e) {
                    String text = getResources().getString(R.string.hotpatch_pull_activity_toast);

                    ToastUtil.showToast(text);
                }

            }
        });
        mCleanPatchesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TinkerLog.i(TAG, "clear patch");
                HotPatchManager.getInstance().startCleanPatches();
            }
        });
    }

    private void downloadPatch(boolean toUpgrade) {
        try {
            String baseID = mBaseIDEditText.getText().toString();
            int version = Integer.parseInt(mVersionEditText.getText().toString());
            String url = mUrlEditText.getText().toString();
            String md5 = mMD5EditText.getText().toString();
            int size = Integer.parseInt(mSizeEditText.getText().toString());

//            String baseID = "1.0.0.8248100";
//            int version = 2;
//            String url = "http://193.112.50.196:18080/files/WiFiManager_Tinker_Patch_1_1.0.0.8248102_20190516014550
//            .jar";
//            String MD5 = "2aebeba48558ef1137a0cfda5737fa47";
//            int size = 5114;

            HotPatchManager.getInstance().startDownloadPatch(baseID, version, url, md5, size, toUpgrade);
        } catch (Exception e) {
            String text = getResources().getString(R.string.hotpatch_pull_activity_toast);
            ToastUtil.showToast(text);
        }
    }

}
