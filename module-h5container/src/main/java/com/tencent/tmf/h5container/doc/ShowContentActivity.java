package com.tencent.tmf.h5container.doc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.tmf.h5container.R;
import java.io.File;

public class ShowContentActivity extends AppCompatActivity implements TbsReaderView.ReaderCallback {

    private static final String TAG = "ShowContentActivity";
    TbsReaderView mTbsReaderView;
    RelativeLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        initView();
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("file_path");
        showFile(filePath);
    }


    private void initView() {
        mContent = findViewById(R.id.content);
        mTbsReaderView = new TbsReaderView(this, this);
        mContent.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
    }

    private void showFile(String filePath) {
        Log.i(TAG, "showFile filePath = " + filePath);
        //加载文件
        Bundle localBundle = new Bundle();
        localBundle.putString("filePath", filePath);

        //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
        String bsReaderTemp = Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp";
        File bsReaderTempFile = new File(bsReaderTemp);
        if (!bsReaderTempFile.exists()) {
            Log.d("print", "creating /TbsReaderTemp！！");
            boolean mkdir = bsReaderTempFile.mkdir();
            if (!mkdir) {
                Log.d("print", "create /TbsReaderTemp failed！！！！！");
            }
        }
        localBundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");

        if (this.mTbsReaderView == null) {
            this.mTbsReaderView = new TbsReaderView(this, this);
        }
        boolean bool = this.mTbsReaderView.preOpen(getFileType(filePath), false);
        Log.i(TAG, "boole = " + bool);
        if (bool) {
            this.mTbsReaderView.openFile(localBundle);
        }
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.i(TAG, "paramString---->null");
            return str;
        }
        Log.i(TAG, "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.i(TAG, "i <= -1");
            return str;
        }

        str = paramString.substring(i + 1);
        Log.i(TAG, "paramString.substring(i + 1)------>" + str);
        return str;
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }
}
