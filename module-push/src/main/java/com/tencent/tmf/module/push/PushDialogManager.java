package com.tencent.tmf.module.push;

import Protocol.MConch.EConchExecutePhase;
import Protocol.MConch.EResult;
import Protocol.MConch.NewCommonConchArgs;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.conch.api.ConchConfig;
import com.tencent.tmf.conch.api.ConchService;
import com.tencent.tmf.conch.api.IConchService;

/**
 * 应用内弹框示例
 * Created by winnieyzhou on 2019/6/26.
 */
public class PushDialogManager {

    private static final String TAG = "PUSH_DIALOG";
    private static final int PUSH_DIALOG_CONCH_ID = 6399;//申请云指令id
    private IConchService mConchService;
    private Context mContext;
    private String mTitle;
    private String mContent;

    private static final int MSG_SHOW_DIALOG = 0x07;
    private Handler mPushDialogHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_DIALOG:
                    // 展示弹框
                    showUpgradeDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private PushDialogManager() {
    }

    private static class Holder {

        private static PushDialogManager INSTANCE = new PushDialogManager();
    }

    public static PushDialogManager getInstance() {
        return Holder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        if (mConchService == null) {
            ConchConfig conchConfig = ConchConfig.builder(mContext).setIShark(SharkService.getSharkWithInit()).build();
            mConchService = ConchService.with(conchConfig);
        }
        mConchService.registerConchPush(PUSH_DIALOG_CONCH_ID, mConchListener);//监听云指令
    }

    private IConchService.IConchPushListener mConchListener = new IConchService.IConchPushListener() {
        @Override
        public void onRecvPush(IConchService.ConchPushInfo conchPushInfo) {
            //接收云指令push回调
            if (conchPushInfo == null || conchPushInfo.mConch == null) {
                Log.i(TAG, "onRecvPush conchPushInfo == null");
                return;
            }
            try {
                NewCommonConchArgs args = ConchService.getJceStruct(conchPushInfo, new NewCommonConchArgs(), false);
                mTitle = args.newParam.get(0);
                mContent = args.newParam.get(1);
                mPushDialogHandler.sendEmptyMessage(MSG_SHOW_DIALOG);

                final StringBuilder builder = new StringBuilder();
                builder.append(mContext.getResources().getString(R.string.module_push_0));
                builder.append("TaskId:" + conchPushInfo.mTaskId).append("\n");
                builder.append("TaskSeqno:" + conchPushInfo.mTaskSeqno).append("\n");
                builder.append("CmdId:" + conchPushInfo.mConch.cmdId).append("\n");

                builder.append("Params:");
                for (String arg : args.newParam) {
                    builder.append("\narg:" + arg);
                }
                Log.i(TAG, "onRecvPush, " + builder.toString());
                mConchService
                        .reportConchResult(conchPushInfo.mTaskId, conchPushInfo.mTaskSeqno, conchPushInfo.mConch.cmdId,
                                conchPushInfo.mConch.conchSeqno, EConchExecutePhase.ECEP_Phase_Execute,
                                EResult.ER_Success);
            } catch (Throwable e) {
                Log.e(TAG, "onRecvPush Throwable:" + e.toString());
                e.printStackTrace();
            }
        }
    };

    private void showUpgradeDialog() {
        new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle(mTitle)
                .setMessage(mContent)
                .addAction(mContext.getString(R.string.module_push_1), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).addAction(mContext.getString(R.string.module_push_2), new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        }).create().show();
    }
}
