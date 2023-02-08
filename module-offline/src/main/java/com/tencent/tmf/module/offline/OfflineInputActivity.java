package com.tencent.tmf.module.offline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.config.AppDataUtil;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.common.utils.Utils;
import java.util.ArrayList;
import java.util.List;

@Destination(
        url = "portal://com.tencent.tmf.module.offline/offline-input-activity",
        launcher = Launcher.ACTIVITY,
        description = "设置公钥"
)
public class OfflineInputActivity extends TopBarActivity {

    private EditText mEdit;
    private AlertDialog alertDialog = null;
    private Spinner mSpinner;
    private boolean isFirst = true;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEdit = findViewById(R.id.edit);

        initSpinner();
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.layout_key_input, null);
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    public void onSave(View view) {
        String publichKey = mEdit.getText().toString();
        if (TextUtils.isEmpty(publichKey)) {
            ToastUtil.showToast(getStringById(R.string.offline_activity_check_update_tip_13));
            return;
        }
        OfflineSp.getInstance().putOfflinePublickKey(publichKey);

        alertDialog = new AlertDialog.Builder(this)
                .setTitle(getStringById(R.string.offline_activity_check_update_tip_14))//标题
                .setMessage(getStringById(R.string.offline_activity_check_update_tip_15))//内容
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDataUtil.killMyself();
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    private void initSpinner() {
        mSpinner = findViewById(R.id.spinner);
        mSpinner.setDropDownVerticalOffset(Utils.dp2px(this, 40));

        List<String> mSearchData = new ArrayList<>();
        mSearchData.add(getStringById(R.string.offline_activity_check_update_tip_16));
        mSearchData.add(ModuleOffline.sInstance.CUSTOM_PUB_KEY);
        mSearchData.add(ModuleOffline.sInstance.CUSTOM_PUB_KEY1);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.item_offline_auto_complete, mSearchData);
        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != 0) {
                    mEdit.setText(mSearchData.get(pos));
                } else {
                    mEdit.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
