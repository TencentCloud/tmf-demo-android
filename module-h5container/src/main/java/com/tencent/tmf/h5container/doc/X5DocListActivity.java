package com.tencent.tmf.h5container.doc;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.h5container.R;
import com.tencent.tmf.x5docpreview.api.PreviewDocImpl;
import java.io.File;

@Destination(
        url = "portal://com.tencent.tmf.module.h5/x5-doc-list-activity",
        launcher = Launcher.ACTIVITY,
        description = "doc"
)
public class X5DocListActivity extends TopBarActivity implements View.OnClickListener {

    private static final String DEFAULT_DOC_PREVIEW_URL = "http://grs.syau.edu.cn/JiaoXue/WenJianXZ/76B0FD246429ED9B"
            + ".shtml";
    private QMUIRoundButton mOnlineBtn;
    private QMUIRoundButton mOnlineMultiBtn;
    private QMUIRoundButton mLocalBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        PreviewDocImpl previewDoc = new PreviewDocImpl.Builder()
                .with(this) //必须
                .setContentActivity(CustomDocShowActivity.class)
                .create();
    }


    private void initView() {
        String title = getResources().getString(R.string.x5_doc_list_activity_title);
        mTopBar.setTitle(title);
        mOnlineBtn = findViewById(R.id.open_online_file);
        mOnlineMultiBtn = findViewById(R.id.open_online_file_multi);
        mLocalBtn = findViewById(R.id.open_local_file);
        mOnlineBtn.setOnClickListener(this);
        mOnlineMultiBtn.setOnClickListener(this);
        mLocalBtn.setOnClickListener(this);
    }


    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_x5_doc_list, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.open_online_file) {
            new FileReaderDialog(X5DocListActivity.this).showFileList();
        } else if (id == R.id.open_online_file_multi) {
            String title = getResources().getString(R.string.x5_doc_list_activity_title_2);

            Portal.from(this)
                    .url(ModuleH5containerConst.U_J_SAPI_ACTIVITY)
                    .param(ModuleH5containerConst.P_URL, "file:///android_asset/x5_doc_preview_demo.html")
                    .param(ModuleH5containerConst.P_TITLE, title)
                    .launch();
        } else if (id == R.id.open_local_file) {
            showLocalExample();
        }
    }

    private void showLocalExample() {
        String filePath = Environment.getExternalStorageDirectory() + "/X5core.pdf";
        if (!new File(filePath).exists()) {
            String title = getResources().getString(R.string.x5_doc_list_activity_file_not_exists);

            ToastUtil.showToast(title + filePath);
        } else {
            openFileReader(X5DocListActivity.this, filePath);
        }
    }

    public void openFileReader(Context context, String pathName) {
        Intent intent = new Intent(context, ShowContentActivity.class);
        intent.putExtra("file_path", pathName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
