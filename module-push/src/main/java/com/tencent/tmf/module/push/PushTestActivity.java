package com.tencent.tmf.module.push;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.mmkv.MMKV;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.profile.api.ISetTagCallback;
import com.tencent.tmf.profile.api.ProfileManager;
import com.tencent.tmf.profile.api.TagErrorCode;


@Destination(
        url = "portal://com.tencent.tmf.module.push/push-test-activity",
        launcher = Launcher.ACTIVITY,
        description = "条件推送演示"
)
public class PushTestActivity extends TopBarActivity {

    private EditText mEtUserId;
    private QMUIRoundButton mBtnSetUserId;
    private QMUIRoundButton mBtnRemoveUserId;

    private QMUIRoundButton mBtnSetTagId;
    private QMUIRoundButton mBtnRemoveTagId;
    private EditText mTagKey;
    private EditText mTagValue;
    private EditText mCountryValue;
    private EditText mProvinceValue;
    private EditText mCityValue;

    private int mErrCode = 0;

    private MMKV mmkv;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    showSuccessDialog(getStringById(R.string.module_push_3));
                    break;
                case 2:
                    showFailDialog(getStringById(R.string.module_push_4) + mErrCode);
                    break;
                case 3:
                    showSuccessDialog(getStringById(R.string.module_push_5));
                    break;
                case 4:
                    showFailDialog(getStringById(R.string.module_push_6) + mErrCode);
                    break;
                case 5:
                    showSuccessDialog(getStringById(R.string.module_push_7));
                    break;
                case 6:
                    showSuccessDialog(getStringById(R.string.module_push_8));
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_push_test, null);
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopBar.setTitle(getStringById(R.string.module_push_9));

        mEtUserId = findViewById(R.id.et_user_id);

        MMKV.initialize(this);//可在App启动时初始化MMKV组件
        mmkv = MMKV.mmkvWithID("simple_mmkv_demo");//当前业务kv实例，实例ID:simple_mmkv_demo

        String userId = mmkv.getString("send_user_id", "");

        if (userId != null && !userId.isEmpty()) {
            mEtUserId.setText(userId);
        }

        mBtnSetUserId = findViewById(R.id.btn_set_user_id);
        mBtnSetUserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = mEtUserId.getText().toString();
                if (userId != null && !
                        userId.isEmpty()) {
                    ProfileManager.setUserId(userId);
                    mHandler.sendEmptyMessage(5);

                    mmkv.putString("send_user_id", userId);
                }
            }
        });

        mBtnRemoveUserId = findViewById(R.id.btn_remove_user_id);
        mBtnRemoveUserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileManager.setUserId("");
                mHandler.sendEmptyMessage(6);
            }
        });

        mTagKey = findViewById(R.id.et_tag_key);
        mTagValue = findViewById(R.id.et_tag_value);

        mBtnSetTagId = findViewById(R.id.btn_set_tag);
        mBtnSetTagId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileManager.setTagWithCallback(mTagKey.getText().toString(), mTagValue.getText().toString(),
                        new ISetTagCallback() {
                            @Override
                            public void onResult(int i) {
                                if (i == TagErrorCode.ERR_NONE) {
                                    mHandler.sendEmptyMessage(1);
                                } else {
                                    mErrCode = i;
                                    mHandler.sendEmptyMessage(2);
                                }
                            }
                        });
            }
        });

        mBtnRemoveTagId = findViewById(R.id.btn_remove_tag);
        mBtnRemoveTagId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileManager.setTagWithCallback(mTagKey.getText().toString(), "", new ISetTagCallback() {
                    @Override
                    public void onResult(int i) {
                        if (i == TagErrorCode.ERR_NONE) {
                            mHandler.sendEmptyMessage(3);
                        } else {
                            mErrCode = i;
                            mHandler.sendEmptyMessage(4);
                        }
                    }
                });
            }
        });

        findViewById(R.id.btn_google_fcm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PushTestActivity.this);
                ToastUtil.showToast(getStringById(R.string.module_push_10)+"(" + status + "): " + (status == ConnectionResult.SUCCESS));
            }
        });

        mCountryValue = findViewById(R.id.et_district_country);
        mProvinceValue = findViewById(R.id.et_district_province);
        mCityValue = findViewById(R.id.et_district_city);
        //reportLocationToProfile country=中国,province=北京市,city=北京市
        findViewById(R.id.btn_report_district).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String county = mCountryValue.getText().toString().trim();
                String province = mProvinceValue.getText().toString().trim();
                String city = mCityValue.getText().toString().trim();
                if (TextUtils.isEmpty(county) || TextUtils.isEmpty(province) || TextUtils.isEmpty(city)) {
                    ToastUtil.showToast(getStringById(R.string.module_push_11));
                    return;
                }
                ProfileManager.setLocation(county, province, city);
                Log.e(Constant.TAG,
                        "reportLocationToProfile country=" + county + ",province=" + province + ",city=" + city);
            }
        });
    }

    private void showSuccessDialog(String str) {
        if (isFinishing()) {
            return;
        }
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(str)
                .create();
        tipDialog.show();

        mTagKey.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);
    }

    private void showFailDialog(String str) {
        if (isFinishing()) {
            return;
        }

        QMUITipDialog tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(str)
                .create();
        tipDialog.show();
        mTagKey.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);
    }
}
