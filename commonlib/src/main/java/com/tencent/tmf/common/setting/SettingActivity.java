package com.tencent.tmf.common.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.tencent.tmf.common.R;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import com.tencent.tmf.common.utils.ToastUtil;

public class SettingActivity extends TopBarActivity implements View.OnClickListener {

    private QMUICommonListItemView mProtocalSwitch;
    private QMUICommonListItemView mUseFcm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopBar.setTitle(R.string.setting_activity_title);
        mProtocalSwitch = findViewById(R.id.cb_protocal);
        mProtocalSwitch.setText(getResources().getString(R.string.setting_activity_start_new_protocal));
        mProtocalSwitch.setOnClickListener(this);

        mProtocalSwitch.getSwitch().setChecked(ConfigSp.getInstance().isNewProtocal());

        mUseFcm = findViewById(R.id.use_fcm);
        mUseFcm.setText("使用FCM");
        mUseFcm.setOnClickListener(this);
        mUseFcm.getSwitch().setChecked(ConfigSp.getInstance().userFcm());
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_setting, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cb_protocal) {
            mProtocalSwitch.getSwitch().toggle();
            boolean isChecked = mProtocalSwitch.getSwitch().isChecked();
            ConfigSp.getInstance().putNewProtocal(isChecked);
            String toast = getResources().getString(R.string.setting_activity_restart_to_take_effect);
            ToastUtil.showToast(toast);
        }else if (id == R.id.use_fcm){
            mUseFcm.getSwitch().toggle();
            boolean isChecked = mUseFcm.getSwitch().isChecked();
            ConfigSp.getInstance().putUseFcm(isChecked);
            String toast = getResources().getString(R.string.setting_activity_restart_to_take_effect);
            ToastUtil.showToast(toast);
        }
    }
}
