package com.tencent.tmf.module.icdp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.ToastUtil;

public class ParamsEnvActivity extends TopBarActivity {

    /**
     * TODO
     * http://console.tmf.ip:8086   对应 http://193.112.44.156:18080
     * http://console.tmf.com:30001 对应 http://152.136.43.195:30028
     * http://console.tmf.dri:30001 对应 http://123.207.174.82:30001  http://123.207.174.82:30028 ??
     */

    public static final String POCICDPSERVER = "http://tmf.qq.com:30028";//  "http://123.207.174.82:30001" ?

    public static final String TESTICDPSERVER = "http://193.112.44.156:18080";

    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        String text = getResources().getString(R.string.icdp_params_env_activity_title);
        mTopBar.setTitle(text);
        initView();
    }

    EditText etEnverSver;
    EditText etUid;
    Spinner spinner;

    private void initView() {

        View itemParamsEnvServer = findViewById(R.id.item_params_env_server);
        String text = getResources().getString(R.string.icdp_params_env_server_add);

        setItemTitle(itemParamsEnvServer, text);
        etEnverSver = setItemValue(itemParamsEnvServer, Config.getInstance(this).getServerHost());

        View itemParamsUid = findViewById(R.id.item_params_env_uid);
        String text1 = getResources().getString(R.string.icdp_params_env_activity_title);

        setItemTitle(itemParamsUid, text1);
        etUid = setItemValue(itemParamsUid, Config.getInstance(this).getUid());

        spinner = findViewById(R.id.item_spinner);
        String text2 = getResources().getString(R.string.icdp_params_env_server_poc);
        String text3 = getResources().getString(R.string.icdp_params_env_server_test);
        String text4 = getResources().getString(R.string.icdp_params_env_server_custom);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.demo_spinner_item,
            new String[]{text2,text3,text4}
        );
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Config.getInstance(activity).setEnv("poc");
                    Config.getInstance(activity).setServerHost(POCICDPSERVER);
                    enableEditServer(false);
                    read();
                } else if (position == 1) {
                    Config.getInstance(activity).setEnv("test");
                    Config.getInstance(activity).setServerHost(TESTICDPSERVER);
                    enableEditServer(false);
                    read();
                } else if (position == 2) {
                    Config.getInstance(activity).setEnv("else");
                    Config.getInstance(activity).setServerHost(Config.getInstance(activity).getCustomServerHost());
                    enableEditServer(true);
                    read();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        read();

        String env = Config.getInstance(activity).getEnv();
        if (env.equals("poc")) {
            spinner.setSelection(0);
        } else if (env.equals("test")) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
        }
        enableEditServer(false);
        findViewById(R.id.save_params).setOnClickListener(v -> {

            Config.getInstance(activity).setServerHost(etEnverSver.getText().toString().trim());
            if (Config.getInstance(activity).getEnv().equals("else")) {
                Config.getInstance(activity).setCustomServerHost(etEnverSver.getText().toString().trim());
            }
            Config.getInstance(activity).setUid(etUid.getText().toString().trim());
            String text5 = getResources().getString(R.string.icdp_params_env_restart_to_take_effect);
            ToastUtil.showToast(text5);
        });


    }

    private void read() {
        etEnverSver.setText(Config.getInstance(activity).getServerHost());
    }

    private void enableEditServer(boolean enable) {
        etEnverSver.setEnabled(enable);
    }

    void setItemTitle(View v, String text) {
        TextView itemTitle = v.findViewById(R.id.item_title);
        if (itemTitle != null) {
            itemTitle.setText(text);
        }
    }

    private EditText setItemValue(View v, String text) {
        EditText itemEdit = v.findViewById(R.id.item_edit);
        itemEdit.setText(text);
        return itemEdit;
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.demo_activity_params_env, null);
    }
}
