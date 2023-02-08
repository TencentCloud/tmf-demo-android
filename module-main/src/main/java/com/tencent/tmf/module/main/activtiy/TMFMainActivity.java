package com.tencent.tmf.module.main.activtiy;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.portal.ActivityResultCallback;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.config.TMFConfigListActivity;
import com.tencent.tmf.common.gen.ModuleCustomMainConst;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.common.gen.ModuleMainConst;
import com.tencent.tmf.common.gen.ModulePocConst;
import com.tencent.tmf.common.gen.ModuleScanConst;
import com.tencent.tmf.common.gen.ModuleTestConst;
import com.tencent.tmf.common.service.IModuleCustomService;
import com.tencent.tmf.common.service.IModulePocService;
import com.tencent.tmf.common.service.IModuleTestService;
import com.tencent.tmf.common.setting.SettingActivity;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.common.utils.AppUtil;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.common.utils.Utils;
import com.tencent.tmf.demo.main.R;
import com.tencent.tmf.push.api.PushCenter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.functions.Action1;

@Destination(
        url = "portal://com.tencent.tmf.module.main/tmf-main-activity",
        launcher = Launcher.ACTIVITY,
        description = "TMF测试首页"
)
public class TMFMainActivity extends TopBarActivity implements View.OnClickListener {

    private static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            "android.permission.READ_MEDIA_AUDIO", Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};
    private static final int REQUEST_PERMISSION = 404;
    private QMUIRoundButton mScan;
    private QMUIRoundButton mOpen;
    private QMUIRoundButton mAppInfo;
    private QMUIRoundButton mMore;
    private QMUIListPopup mListPopup;
    private AutoCompleteTextView mAutotextview;
    private TextView mOutputText;
    private List<String> mSearchData = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    public static boolean isHttpUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return Patterns.WEB_URL.matcher(url).matches() || URLUtil.isValidUrl(url);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        PushCenter.initFirstActivity(this);
        checkPermission();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> noPermissionList = new ArrayList<>();
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    noPermissionList.add(permission);
                }
            }
            if (noPermissionList.size() > 0) {
                String[] p = new String[noPermissionList.size()];
                noPermissionList.toArray(p);
                requestPermissions(p, REQUEST_PERMISSION);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_scanmain, null);
    }
private String getStringById(int id){
        return getResources().getString(id);
}

    private void initView() {
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        mTopBar.removeAllLeftViews();

        mScan = findViewById(R.id.scanQrcode);
        mOpen = findViewById(R.id.btnOpen);
        mMore = findViewById(R.id.btnMore);
        mAppInfo = findViewById(R.id.btn_app_info);
        mOutputText = findViewById(R.id.text_output);
        mScan.setOnClickListener(this);
        mOpen.setOnClickListener(this);
        mMore.setOnClickListener(this);
        mAppInfo.setOnClickListener(this);
        mAppInfo.setOnClickListener(this);

        final Button menuButton = mTopBar.addRightTextButton(getStringById(R.string.tmf_main_more), 13479);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initListPopupIfNeed();

                mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
                mListPopup.show(menuButton);
            }
        });

        mSearchData = ConfigSp.getInstance().getMainInput();
        mAutotextview = findViewById(R.id.autotextview);
        mAdapter = new ArrayAdapter<>(getApplicationContext(), com.tencent.tmf.common.R.layout.item_auto_complete,
                mSearchData);
        mAutotextview.setAdapter(mAdapter);

        mOutputText.setText(getStringById(R.string.tmf_main_pack_time)+ BuildConfig.CREATE_TIME);
    }

    private void initListPopupIfNeed() {
        if (mListPopup == null) {
            //POC入口暂时隐藏
             IModulePocService iModulePocService = Portal.getService(IModulePocService.class);
//            IModulePocService iModulePocService = null;
            IModuleCustomService iModuleCustomService = Portal.getService(IModuleCustomService.class);
            String[] listItems = new String[]{
                    getStringById(R.string.tmf_main_dynamic_conf),
                    getStringById(R.string.module_main_6),
                    getStringById(R.string.module_main_7),
                    getStringById(R.string.module_main_8),
            };
            List<String> data = new ArrayList<>();
            Collections.addAll(data, listItems);
//            if (iModulePocService != null) {
//                data.add(iModulePocService.getTitle());
//            }
            if (iModuleCustomService != null) {
                data.add(iModuleCustomService.getTitle());
            }
            IModuleTestService iModuleTestService = Portal.getService(IModuleTestService.class);
            if (iModuleTestService != null) {
                data.add(iModuleTestService.getTitle());
            }
            ArrayAdapter adapter = new ArrayAdapter<>(this, com.tencent.tmf.common.R.layout.simple_list_item, data);
            mListPopup = new QMUIListPopup(this, QMUIPopup.DIRECTION_NONE, adapter);
            mListPopup.create(QMUIDisplayHelper.dp2px(this, 130), QMUIDisplayHelper.dp2px(this, 250),
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    startActivity(new Intent(TMFMainActivity.this, TMFConfigListActivity.class));
                                    break;
                                case 1:
                                    if (iModulePocService != null) {
                                        Portal.from(TMFMainActivity.this)
                                                .url(ModulePocConst.U_T_M_F_POC_MAIN_ACTIVITY)
                                                .launch();
                                    } else if (iModuleCustomService != null) {
                                        Portal.from(TMFMainActivity.this)
                                                .url(ModuleCustomMainConst.U_CUSTOM_LIST_ACTIVITY)
                                                .launch();
                                    } else if (iModuleTestService != null) {
                                        Portal.from(TMFMainActivity.this)
                                                .url(ModuleTestConst.U_TEST_LIST_ACTIVITY)
                                                .launch();
                                    }
                                    break;
                                case 2:
                                    if (iModuleCustomService != null) {
                                        Portal.from(TMFMainActivity.this)
                                                .url(ModuleCustomMainConst.U_CUSTOM_LIST_ACTIVITY)
                                                .launch();
                                    } else if (iModuleTestService != null) {
                                        Portal.from(TMFMainActivity.this)
                                                .url(ModuleTestConst.U_TEST_LIST_ACTIVITY)
                                                .launch();
                                    }
                                    break;
                                case 3:
                                    Portal.from(TMFMainActivity.this)
                                            .url(ModuleTestConst.U_TEST_LIST_ACTIVITY)
                                            .launch();
                                    break;
                                default:
                                    Log.e("TAG","item click default");
                                    break;
                            }
                            mListPopup.dismiss();
                        }
                    });
            mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnMore) {
            Portal.from(this)
                    .url(ModuleMainConst.U_COMPONENT_LIST_ACTIVITY)
                    .launch();
        } else if (id == R.id.btnOpen) {
            String url = mAutotextview.getText().toString();
            if (url != null && url.trim().equalsIgnoreCase("setting")) {
                autoTextview(url);
                startActivity(new Intent(this, SettingActivity.class));
                return;
            }
            if (TextUtils.isEmpty(url) || !isHttpUrl(url)) {
                ToastUtil.showToast(getStringById(R.string.tmf_main_enter_correct_url));
                return;
            }
            url = url.trim();
            autoTextview(url);
            ConfigSp.getInstance().putMainInput(mSearchData);
            if (!url.startsWith("http") && !url.startsWith("https")) {
                url = "http://" + url;
            }
            Portal.from(this)
                    .url(ModuleH5containerConst.U_BROWSER_ACTIVITY)
                    .param(ModuleH5containerConst.P_URL, url)
                    .launch();
        } else if (id == R.id.scanQrcode) {
            Portal.from(this)
                    .url(ModuleScanConst.U_Q_R_CODE_SCAN_MAIN_ACTIVITY)
                    .startActivityWithCallback(new ActivityResultCallback() {
                        @Override
                        public void onActivityResult(int resultCode, Intent data) {
                            switch (resultCode) {
                                case Activity.RESULT_OK: {
                                    if (data != null) {
                                        String result = data.getStringExtra(ModuleScanConst.R_SCAN_RESULT);
                                        mAutotextview.setText(result);
                                    }
                                    break;
                                }
                                case Activity.RESULT_CANCELED: {
                                    break;
                                }
                                default:
                                    break;
                            }
                        }
                    })
                    .param(ModuleScanConst.P_GET_RESULT, true)
                    .launch();
        } else if (id == R.id.btn_app_info) {
            appinfo();
        }
    }

    private void autoTextview(String url) {
        boolean isFind = false;
        for (String text : mSearchData) {
            if (url.trim().equalsIgnoreCase(text)) {
                isFind = true;
            }
        }
        if (!isFind) {
            mAdapter.add(url);
            mSearchData.add(url);
            ConfigSp.getInstance().putMainInput(mSearchData);
        }
    }

    private void appinfo() {
        StringBuilder builder = new StringBuilder();

        String guid = SharkService.getSharkWithInit().getGuid();
        String versionName = AppUtil.getVersionName(this);
        int versionCode = AppUtil.getVersionCode(this);//lib下的BuildConfig不再生成VERSION_CODE和VERSION_NAME
        int buildNo = BuildConfig.BUILD_NO;

        builder.append(getStringById(R.string.tmf_main_pack_time)).append(BuildConfig.CREATE_TIME).append("\n");
        builder.append("guid: ").append(guid).append("\n");
        builder.append("versionName: ").append(versionName).append(".").append(buildNo).append("\n");
        builder.append("versionCode: ").append(versionCode).append("\n");
        builder.append("buildNo: ").append(buildNo).append("\n");
        builder.append("gitRev: ").append(BuildConfig.GIT_REV);

        String appInfo = builder.toString();
        mOutputText.setText(appInfo);

        ClipData myClip = ClipData.newPlainText("guid", guid);
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), getStringById(R.string.tmf_main_guid_copy_tips), Toast.LENGTH_SHORT).show();
    }

}
