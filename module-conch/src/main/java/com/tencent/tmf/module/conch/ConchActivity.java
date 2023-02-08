package com.tencent.tmf.module.conch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.gen.ModulePocConst;
import com.tencent.tmf.conch.api.ConchService;
import com.tencent.tmf.conch.api.IConchService;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;

import java.util.List;

import Protocol.MConch.EConchExecutePhase;
import Protocol.MConch.EResult;
import Protocol.MConch.NewCommonConchArgs;

@Destination(
        url = "portal://com.tencent.tmf.module.conch/conch-activity",
        launcher = Launcher.ACTIVITY,
        description = "云指令测试页面"
)
public class ConchActivity extends TopBarActivity implements View.OnClickListener {

    private static final String TAG = ConchActivity.class.getSimpleName();
    private EditText mEditText;
    private Button mButton;
    private Button mConchList;
    private TextView mResult;
    private TextView mRegConchList;

    private IConchService conchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEditText = findViewById(R.id.et_conch_id);
        mButton = findViewById(R.id.btn_pull_conch);
        mConchList = findViewById(R.id.btn_conch_list);
        mResult = findViewById(R.id.tv_conch_log);
        mRegConchList = findViewById(R.id.tv_conch_registered);

        mButton.setOnClickListener(this);
        mConchList.setOnClickListener(this);
        String title = getResources().getString(R.string.conch_activity_conch_title);
        mTopBar.setTitle(title);

        //先行初始化云指令,初始化云指令要先初始化Shark，更详细请参照Shark组件的初始化步骤

        conchService = ConchService.getInstance(this);
//        conchService.registerConchPush(6238, mConchListener); // 6238：消息推送云指令
        conchService.registerConchPush(6396, mConchListener);

        List<Integer> registeredCmds = conchService.registeredCmdIds();
        String registedConch = getResources().getString(R.string.conch_activity_conch_registed);
        StringBuilder sb = new StringBuilder(registedConch);
        for (int i = 0; i < registeredCmds.size(); ++i) {
            if ((i + 1) % 6 == 0) {
                sb.append(registeredCmds.get(i)).append("\n");
            } else {
                sb.append(registeredCmds.get(i)).append(", ");
            }
        }
        mRegConchList.setText(sb);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_conch, null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_pull_conch) {
            if (conchService != null) {
                clearMsg();
                int conchId = -1;
                try {
                    conchId = Integer.valueOf(mEditText.getText().toString());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if (conchId == -1) {
                    String text = getResources().getString(R.string.conch_activity_conch_input_correct);
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                conchService.registerConchPush(conchId, mConchListener);
                conchService.pullConch(conchId);
            } else {
                String text = getResources().getString(R.string.conch_activity_conch_input_not_found);

                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }
        } else {
            Portal.from(this).url("portal://com.tencent.tmf.module.poc/conch-list-activity").launch();
        }
    }

    public IConchService.IConchPushListener mConchListener = new IConchService.IConchPushListener() {
        @Override
        public void onRecvPush(IConchService.ConchPushInfo conchPushInfo) {
            Log.i(TAG, "onRecvPush, conchPushInfo: " + conchPushInfo);
            if (conchPushInfo == null) {
                return;
            }

            try {
                final StringBuilder builder = new StringBuilder();
                String text = getResources().getString(R.string.conch_activity_conch_input_result);

                builder.append(text);
                builder.append("TaskId:" + conchPushInfo.mTaskId).append("\n");
                builder.append("TaskSeqno:" + conchPushInfo.mTaskSeqno).append("\n");
                builder.append("CmdId:" + conchPushInfo.mConch.cmdId).append("\n");

                NewCommonConchArgs args = ConchService.getJceStruct(conchPushInfo, new NewCommonConchArgs(), false);
                builder.append("Params:");
                for (String arg : args.newParam) {
                    builder.append("\narg:" + arg);
                }
                Log.i(TAG, "[conch_debug]onRecvPush, " + builder.toString());
                mResult.post(new Runnable() {
                    @Override
                    public void run() {
                        println(builder.toString());
                    }
                });

                conchService
                        .reportConchResult(conchPushInfo.mTaskId, conchPushInfo.mTaskSeqno, conchPushInfo.mConch.cmdId,
                                conchPushInfo.mConch.conchSeqno, EConchExecutePhase.ECEP_Phase_Execute,
                                EResult.ER_Success, conchPushInfo.mTraceId);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };


    private StringBuilder sb = new StringBuilder();


    private synchronized void println(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb.append("\n").append(msg);
                mResult.setText(sb.toString());
            }
        });
    }

    private void clearMsg() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb.setLength(0);
                mResult.setText(sb.toString());
            }
        });
    }
}
