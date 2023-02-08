package com.tencent.tmf.applet.demo.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.tmf.mini.api.TmfMiniSDK;
import com.tencent.tmf.mini.api.bean.MiniApp;
import com.tencent.tmf.mini.api.bean.MiniScene;
import com.tencent.tmf.mini.api.bean.MiniStartOptions;
import com.tencent.tmf.applet.demo.R;
import com.tencent.tmf.applet.demo.utils.SoftKeyboardUtil;

public class AppidDialog extends BaseBottomDialog implements OnClickListener {

    private Activity activity;
    private RelativeLayout mContainer;
    private EditText mAppidEdit;
    private Button mOkBtn;
    private Button mCencelBtn;

    public AppidDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        initView();
    }

    private void initView() {
        mContainer = (RelativeLayout) findViewById(R.id.container);
        mAppidEdit = (EditText) findViewById(R.id.appid_edit);
        mOkBtn = (Button) findViewById(R.id.ok_btn);
        mCencelBtn = (Button) findViewById(R.id.cencel_btn);

        mOkBtn.setOnClickListener(this);
        mCencelBtn.setOnClickListener(this);

        mAppidEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAppidEdit.requestFocus();
                SoftKeyboardUtil.showInputMethod(activity, mAppidEdit);
            }
        }, 150);
    }


    @Override
    public int getLayout() {
        return R.layout.applet_dialog_appid;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ok_btn) {
            String s = mAppidEdit.getText().toString();
            if (!TextUtils.isEmpty(s)) {
                MiniStartOptions miniStartOptions = new MiniStartOptions();
                TmfMiniSDK.startMiniApp(activity, s, MiniScene.LAUNCH_SCENE_SEARCH, MiniApp.TYPE_ONLINE, miniStartOptions);
                dismiss();
            } else {
                Toast.makeText(getContext(), "APPID Can't be null", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.cencel_btn) {
            dismiss();
        }
    }
}
