package com.tencent.tmf.applet.demo.activity;

import android.Manifest.permission;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder;
import com.tencent.tmf.mini.api.TmfMiniSDK;
import com.tencent.tmf.mini.api.bean.MiniCode;
import com.tencent.tmf.mini.api.callback.MiniCallback;
import com.tencent.tmf.applet.demo.R;
import com.tencent.tmf.applet.demo.sp.impl.CommonSp;
import com.tencent.tmf.applet.demo.utils.DialogUtils;
import com.tencent.tmf.applet.demo.utils.SoftKeyboardUtil;
import com.tencent.tmf.common.gen.ModuleAppletConst;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * https://blog.csdn.net/weixin_45882303/article/details/121155068
 * https://blog.csdn.net/q714093365/article/details/124379047
 * https://blog.csdn.net/qq_29364417/article/details/109379915
 */
@Destination(
        url = ModuleAppletConst.U_LOGIN_ACTIVITY,
        launcher = Launcher.ACTIVITY,
        description = "小程序登录页面"
)
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private static final String[] PERMISSIONS = new String[]{permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSION = 404;
    private TextView mConfigServerText;
    private LinearLayout mOperateLayout;
    private AutoCompleteTextView mOperateUserNameEdit;
    private EditText mOperatePasswordEdit;
    private LinearLayout mOpenLayout;
    private AutoCompleteTextView mOpenUserNameEdit;
    private EditText mOpenPasswordEdit;
    private Button mLoginBtn;
    private TextView mOtherLoginText;
    private TextView mAccountInfo;
    private TextView mChangeAccountText;
    private List<String> mUserNameData;
    private ArrayAdapter<String> mOperateUserNameAdapter;
    private ArrayAdapter<String> mOpenUserNameAdapter;
    private ImageView mOperatePasswordEye;
    private ImageView mOpenPasswordEye;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));//设置状态栏颜色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗

        setContentView(R.layout.applet_layout_login);
        checkPermission();
        initView();
    }

    private boolean checkPermission() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
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

    private void initView() {
        mConfigServerText = (TextView) findViewById(R.id.config_server_text);
        mOperateLayout = (LinearLayout) findViewById(R.id.operate_layout);
        mOperateUserNameEdit = findViewById(R.id.operate_user_name_edit);
        mOperatePasswordEdit = findViewById(R.id.operate_password_edit);
        mOpenLayout = (LinearLayout) findViewById(R.id.open_layout);
        mOpenUserNameEdit = findViewById(R.id.open_user_name_edit);
        mOpenPasswordEdit = findViewById(R.id.open_password_edit);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mOtherLoginText = (TextView) findViewById(R.id.other_login_text);
        mAccountInfo = (TextView) findViewById(R.id.account_info);
        mChangeAccountText = (TextView) findViewById(R.id.change_account_text);
        mOperatePasswordEye = findViewById(R.id.operate_password_eye);
        mOpenPasswordEye = findViewById(R.id.open_password_eye);

        mConfigServerText.setOnClickListener(this::onClick);
        mOtherLoginText.setOnClickListener(this::onClick);
        mLoginBtn.setOnClickListener(this::onClick);
        mChangeAccountText.setOnClickListener(this::onClick);
        mOperatePasswordEye.setOnClickListener(this::onClick);
        mOpenPasswordEye.setOnClickListener(this::onClick);
        findViewById(R.id.skip_text).setOnClickListener(this);

        mUserNameData = CommonSp.getInstance().getUsers();
        mOperateUserNameAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.applet_item_auto_complete, mUserNameData);
        mOperateUserNameEdit.setAdapter(mOperateUserNameAdapter);
        mOperateUserNameEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mOperateUserNameAdapter.getItem(position);
                String pwd = CommonSp.getInstance().getPwd(name);
                mOperatePasswordEdit.setText(pwd);
            }
        });

        mOpenUserNameAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.applet_item_auto_complete, mUserNameData);
        mOpenUserNameEdit.setAdapter(mOpenUserNameAdapter);
        mOpenUserNameEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mOpenUserNameAdapter.getItem(position);
                String pwd = CommonSp.getInstance().getPwd(name);
                mOpenPasswordEdit.setText(pwd);
            }
        });

        JSONObject lastUser = CommonSp.getInstance().getLastUser();
        if (lastUser != null) {
            String name = lastUser.optString("name");
            String pwd = lastUser.optString("pwd");
            mOperateUserNameEdit.setText(name);
            mOpenUserNameEdit.setText(name);
            mOperatePasswordEdit.setText(pwd);
            mOpenPasswordEdit.setText(pwd);
        }
        mOperateUserNameEdit.requestFocus();
        SoftKeyboardUtil.showInputMethod(LoginActivity.this, mOperateUserNameEdit);
    }

    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.config_server_text) {
            startActivity(new Intent(LoginActivity.this, ServerConfigListActivity.class));
        } else if (viewId == R.id.skip_text) {
            String title = getString(R.string.applet_login_act_skip_login);
            String titleTips = getString(R.string.applet_login_act_skip_login_tips);
            String psBtn = getString(R.string.applet_login_act_skip_login_confirm);
            String negBtn = getString(R.string.applet_login_act_skip_login_cancel);
            DialogUtils.showDialog(this, title, titleTips,
                    psBtn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommonSp.getInstance().putSkipLogin(true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                            dialog.dismiss();
                        }
                    }, negBtn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else if (viewId == R.id.login_btn) {
            boolean isOpenLogin = mOpenLayout.getVisibility() == View.VISIBLE;
            String userName = "", password = "";
            if (isOpenLogin) {
                userName = mOpenUserNameEdit.getText().toString();
                password = mOpenPasswordEdit.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, R.string.applet_login_act_toast_input_open, Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                userName = mOperateUserNameEdit.getText().toString();
                password = mOperatePasswordEdit.getText().toString();
                if (TextUtils.isEmpty(mOperateUserNameEdit.getText().toString()) ||
                        TextUtils.isEmpty(mOperatePasswordEdit.getText().toString())) {
                    Toast.makeText(this, R.string.applet_login_act_skip_input_yy, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            final QMUITipDialog dialog = new Builder(this)
                    .setIconType(Builder.ICON_TYPE_LOADING)
                    .setTipWord(getString(R.string.applet_login_act_logining))
                    .create();
            dialog.show();

            final String finalName = userName;
            final String finalPwd = password;
            TmfMiniSDK.login(userName, password, isOpenLogin, new MiniCallback<Void>() {
                @Override
                public void value(int code, String msg, Void data) {
                    dialog.dismiss();
                    if (code == MiniCode.CODE_OK) {
                        String tips = getString(R.string.applet_login_act_tips);
                        String tipsD = getString(R.string.applet_login_act_tips_detail);
                        String remind = getString(R.string.applet_login_remind_known);
                        DialogUtils.showDialog(LoginActivity.this, tips, tipsD, remind,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mOpenUserNameAdapter.add(finalName);
                                        mOperateUserNameAdapter.add(finalName);
                                        CommonSp.getInstance().putUser(finalName, finalPwd);

                                        TmfMiniSDK.setUserId(finalName);
                                        CommonSp.getInstance().putUserName(finalName);
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        LoginActivity.this.finish();
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.applet_login_remind_login_failed, msg), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        } else if (viewId == R.id.other_login_text) {
            if (mOpenPasswordEdit.getVisibility() == View.VISIBLE) {
                mOpenPasswordEdit.setVisibility(View.GONE);
                mOtherLoginText.setText(R.string.applet_login_platform);
                mOpenUserNameEdit.setHint(R.string.applet_login_platform_id);
            } else {
                mOpenPasswordEdit.setVisibility(View.VISIBLE);
                mOtherLoginText.setText(R.string.applet_login_app_account);
                mOpenUserNameEdit.setHint(R.string.applet_login_open_name_account);
            }
        } else if (viewId == R.id.change_account_text) {
            if (mOperateLayout.getVisibility() == View.VISIBLE) {
                mOperateLayout.setVisibility(View.GONE);
                mOpenLayout.setVisibility(View.VISIBLE);
//                mOtherLoginText.setVisibility(View.VISIBLE);
                mAccountInfo.setText(R.string.applet_login_current_company);
                mChangeAccountText.setText(R.string.applet_login_switch_yy);
            } else {
                mOperateLayout.setVisibility(View.VISIBLE);
                mOpenLayout.setVisibility(View.GONE);
//                mOtherLoginText.setVisibility(View.GONE);
                mAccountInfo.setText(R.string.applet_login_current_yy);
                mChangeAccountText.setText(R.string.applet_login_current_company);
            }
        } else if (viewId == R.id.operate_password_eye) {
            if (mOperatePasswordEdit.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                mOperatePasswordEye.setBackgroundResource(R.mipmap.applet_eye_visiable);
                mOperatePasswordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                mOperatePasswordEye.setBackgroundResource(R.mipmap.applet_eye_invisible);
                mOperatePasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        } else if (viewId == R.id.open_password_eye) {
            if (mOpenPasswordEdit.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                mOpenPasswordEye.setBackgroundResource(R.mipmap.applet_eye_visiable);
                mOpenPasswordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                mOpenPasswordEye.setBackgroundResource(R.mipmap.applet_eye_invisible);
                mOpenPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
    }
}















