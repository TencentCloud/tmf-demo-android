package com.tencent.tmf.module.offline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.gen.ModuleOfflineConst;
import com.tencent.tmf.weboffline.api.OfflineManager;
import com.tencent.tmf.weboffline.api.SimpleCallback;
import com.tencent.tmf.weboffline.api.callback.IOfflineUpdateCallback;
import com.tencent.tmf.weboffline.api.entitiy.DownloadInfo;
import com.tencent.tmf.weboffline.api.entitiy.ProgressEntity;
import com.tencent.tmf.weboffline.api.entitiy.ProtocolType;
import com.tencent.tmf.weboffline.api.entitiy.UpdateEntity;
import com.tencent.tmf.weboffline.api.entitiy.UpdateSetting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

@Destination(
        url = "portal://com.tencent.tmf.module.offline/offline-activity",
        launcher = Launcher.ACTIVITY,
        description = "离线包测试"
)
public class OfflineActivity extends TopBarActivity {

    public static final String TAG = OfflineActivity.class.getSimpleName();

    private static final String BID = "WebOffline_Demo_1";
    private static final String BIDS = "WebOffline_Demo_1+Common_1";

    private static final String URL_HOST = "https://3gimg.qq.com/";
    private static final String MAIN_PATH = "webapp_scan/TMF/TMF_intro/index.html";

    AlertDialog alertDialog = null;
    private OfflineManager mOfflineManager;
    private TextView mOutputText;
    private EditText mOneOfflinePkgEdit;
    private EditText mMultiOfflinePkgEdit;
    private QMUIListPopup mListPopup;
    private IOfflineUpdateCallback offlineUpdateCallback = new IOfflineUpdateCallback() {
        @Override
        public void update(int code, List<UpdateEntity> updateInfos) {
            Log.d(TAG, "update, code=" + code);
            if (code == OfflineManager.CHECK_CODE_SUCC_NO_UPDATE) {
                mOutputText.append("\n");
                mOutputText.append(getStringById(R.string.offline_activity_check_update_tip_0));
            } else if (code == OfflineManager.CHECK_CODE_IGNORE_HAD_UPDATE_IN_A_SHORT_TIME) {
                mOutputText.append("\n");
                mOutputText.append(getStringById(R.string.offline_activity_check_update_tip_1));
            } else if (code == OfflineManager.CHECK_CODE_RES_LIMIT){
                mOutputText.append("\n");
                mOutputText.append(getStringById(R.string.offline_activity_check_update_tip_18));
            } else {
                mOutputText.append("\n");
                mOutputText
                        .append("update, code=" + code + " " + (updateInfos == null ? null : updateInfos.toString()));
            }
        }

        @Override
        public void downloadProgress(ProgressEntity progressInfo) {
            Log.d(TAG, "downloadProgress, " + progressInfo.toString());
            mOutputText.append("\n");
            mOutputText.append("downloadProgress, " + progressInfo.toString());
        }

        @Override
        public void downloadFinish(DownloadInfo downloadInfo) {
            if (downloadInfo.code == OfflineManager.DOWNLOAD_CODE_ERROR_S_FAIL) {
                alert(getStringById(R.string.offline_activity_check_update_tip_2));
            }
            Log.d(TAG, "downloadFinish, " + downloadInfo.toString());
            mOutputText.append("\n");
            mOutputText.append("downloadFinish, " + downloadInfo.toString());
        }
    };
    private String getStringById(int id){
        return getResources().getString(id);
    }

    private void alert(String msg) {
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(getStringById(R.string.offline_activity_check_update_tip_3))//标题
                .setMessage(msg)//内容
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getStringById(R.string.offline_activity_check_update_tip_4));

        Context context = getApplicationContext();
        mOfflineManager = new OfflineManager(context);

        mOutputText = findViewById(R.id.text_output);
        mOneOfflinePkgEdit = findViewById(R.id.edit_one);
        mMultiOfflinePkgEdit = findViewById(R.id.edit_multi);

        mOneOfflinePkgEdit.setText(BID);
        mMultiOfflinePkgEdit.setText(BIDS);

//        final Button menuButton = mTopBar.addRightTextButton("更多", 13079);
//        menuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initListPopup();
//
//                mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
//                mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
//                mListPopup.show(menuButton);
//            }
//        });
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_weboffline_main, null);
    }

    private void initListPopup() {
        if (mListPopup == null) {
            String[] listItems = new String[]{
                    "当前公钥",
                    "设置公钥",
            };
            List<String> data = new ArrayList<>();
            Collections.addAll(data, listItems);
            ArrayAdapter adapter = new ArrayAdapter<>(this, com.tencent.tmf.common.R.layout.simple_list_item, data);
            mListPopup = new QMUIListPopup(this, QMUIPopup.DIRECTION_NONE, adapter);
            mListPopup.create(QMUIDisplayHelper.dp2px(this, 130), QMUIDisplayHelper.dp2px(this, 140),
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    new QMUIDialog.MessageDialogBuilder(OfflineActivity.this)
                                            .setTitle("公钥信息")
                                            .setMessage(ModuleOffline.sInstance.current)
                                            .addAction("确认", new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    dialog.dismiss();
                                                }
                                            }).create().show();
                                    break;
                                case 1:
                                    Portal.from(OfflineActivity.this)
                                            .url(ModuleOfflineConst.U_OFFLINE_INPUT_ACTIVITY)
                                            .launch();
                                    break;
                                default:
                                    break;
                            }
                            mListPopup.dismiss();
                        }
                    });
        }
    }

    /**
     * 单个离线包检查更新
     *
     * @param view
     */
    public void onCheckOneByBid(View view) {
        String bid = mOneOfflinePkgEdit.getText().toString();
        if (TextUtils.isEmpty(bid)) {
            showToast(getStringById(R.string.offline_activity_check_update_tip_5));
            return;
        }
        UpdateSetting updateSetting = new UpdateSetting();
        updateSetting.ignoreFreqLimit = true;
        mOfflineManager.checkLatestUpdate(bid, updateSetting, offlineUpdateCallback);
    }

    private String getUrl(String bid){
        boolean isNewProto = ConfigSp.getInstance().isNewProtocal();
        return isNewProto
                ? (URL_HOST + bid + "/" + MAIN_PATH)
                : (URL_HOST + MAIN_PATH);
    }

    /**
     * 打开单离线包
     *
     * @param view
     */
    public void onOpenOne(View view) {
        String bid = mOneOfflinePkgEdit.getText().toString();
        if (TextUtils.isEmpty(bid)) {
            showToast(getStringById(R.string.offline_activity_check_update_tip_5));
            return;
        }
        Portal.from(this)
                .url(ModuleOfflineConst.U_SHOW_OFFLINE_ACTIVITY)
                .param("url", getUrl(bid) + "?_bid=" + bid)
                .launch();
//        Intent intent = new Intent(this, ShowOfflineActivity.class);
//        intent.putExtra("url", URL + "?_bid=" + bid);
//        startActivity(intent);
    }

    /**
     * 多个离线包检查更新
     *
     * @param view
     */
    public void onCheckMultiByBid(View view) {
        String bids = mMultiOfflinePkgEdit.getText().toString();
        if (TextUtils.isEmpty(bids)) {
            showToast(getStringById(R.string.offline_activity_check_update_tip_5));
            return;
        }
        UpdateSetting updateSetting = new UpdateSetting();
        updateSetting.ignoreFreqLimit = true;
        mOfflineManager.checkUpdateByUrl(getUrl(bids) + "?_bids=" + bids.trim(), updateSetting, offlineUpdateCallback);
    }

    /**
     * 打开多离线包
     *
     * @param view
     */
    public void onOpenMulti(View view) {
        String bids = mMultiOfflinePkgEdit.getText().toString();
        if (TextUtils.isEmpty(bids)) {
            showToast(getStringById(R.string.offline_activity_check_update_tip_5));
            return;
        }

        String []bidArr = bids.split("\\+");

        Portal.from(this)
                .url(ModuleOfflineConst.U_SHOW_OFFLINE_ACTIVITY)
                .param("url", getUrl(bidArr[0]) + "?_bids=" + bids.trim())
                .launch();

//        Intent intent = new Intent(this, ShowOfflineActivity.class);
//        intent.putExtra("url", URL + "?_bids=" + bids.trim());
//        startActivity(intent);
    }

    /**
     * 检查更新所有离线包
     */
    public void checkUpdateForAllBiz(View view) {
        UpdateSetting updateSetting = new UpdateSetting();
        updateSetting.ignoreFreqLimit = true;
        mOfflineManager.checkAllUpdate(updateSetting, offlineUpdateCallback);
    }


    /**
     * 获取本地离线包的版本
     *
     * @return
     */
    public void getBizVersion(View view) {
        OfflineManager.getBizVersion(this, mOneOfflinePkgEdit.getText().toString(), new SimpleCallback<String>() {
            @Override
            public void callback(String version) {
                showToast(getStringById(R.string.offline_activity_check_update_tip_7) + version);
            }
        });
    }

    /**
     * 获取本地离线包的版本
     */
    public void getBizConfig(View view) {
        OfflineManager.getBizConfig(this, mOneOfflinePkgEdit.getText().toString(), new SimpleCallback<JSONObject>() {
            @Override
            public void callback(JSONObject config) {
                showToast(getStringById(R.string.offline_activity_check_update_tip_6) + config);
            }
        });
    }

    /**
     * 获取上次更新离线包的时间戳
     */
    public void getBizUpdateTime(View view) {
        OfflineManager.getBizUpdateTime(this, mOneOfflinePkgEdit.getText().toString(), new SimpleCallback<Long>() {
            @Override
            public void callback(Long time) {
                showToast(getStringById(R.string.offline_activity_check_update_tip_8) + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(time));
            }
        });
    }

    /**
     * 清除离线包
     */
    public void deleteBiz(View view) {
        OfflineManager.deleteBiz(this, mOneOfflinePkgEdit.getText().toString(), new SimpleCallback<Integer>() {
            @Override
            public void callback(Integer ret) {
                showToast(getStringById(R.string.offline_activity_check_update_tip_9) + (ret == 0 ? getStringById(R.string.offline_activity_check_update_tip_10) : getStringById(R.string.offline_activity_check_update_tip_11)));
            }
        });
    }

    /**
     * 获取本地离线包的版本
     */
    public void getLocalOfflineVersions(View view) {
        OfflineManager.getLocalOfflineVersions(this, new SimpleCallback<String>() {
            @Override
            public void callback(String versions) {
                showToast(getStringById(R.string.offline_activity_check_update_tip_12) + versions);
            }
        });
    }

    /**
     * 清除所有离线包
     */
    public void deleteAllOfflineData(View view) {
        OfflineManager.deleteAllOfflineData(this, new SimpleCallback<Integer>() {
            @Override
            public void callback(Integer ret) {
                showToast(getStringById(R.string.offline_activity_check_update_tip_9) + (ret == 0 ? getStringById(R.string.offline_activity_check_update_tip_10) : getStringById(R.string.offline_activity_check_update_tip_11)));
            }
        });
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(OfflineActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCleanLog(View view) {
        mOutputText.setText("");
    }


    public void onDownloadTest(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    UpdateSetting updateSetting = new UpdateSetting();
                    updateSetting.ignoreFreqLimit = true;
//                    updateSetting.isDownload = false;
                    mOfflineManager.checkLatestUpdate(mOneOfflinePkgEdit.getText().toString(), updateSetting, offlineUpdateCallback);
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }
}
