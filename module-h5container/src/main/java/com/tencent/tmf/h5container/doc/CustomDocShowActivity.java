package com.tencent.tmf.h5container.doc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.h5container.R;
import com.tencent.tmf.x5docpreview.api.FileReaderView;
import com.tencent.tmf.x5docpreview.api.IFileLoadingListener;
import com.tencent.tmf.x5docpreview.api.PreviewDocImpl;
import com.tencent.tmf.x5docpreview.impl.PreviewConfig;


public class CustomDocShowActivity extends TopBarActivity implements IFileLoadingListener {

    private static final String TAG = "Docpre";
    FileReaderView mFileReadView;
    LoadingProgress mProgress;
    RelativeLayout mLoadingPanel;
    ImageView mFileTypeIcon;
    TextView mFileName;
    TextView mFileSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getResources().getString(R.string.custom_doc_activity_title);
        mTopBar.setTitle(title);
        initView();
        Intent intent = getIntent();
        String filePath = intent.getStringExtra(PreviewConfig.PARAM_FILE_PATH);
        String fileName = intent.getStringExtra(PreviewConfig.PARAM_FILE_NAME);
        Log.i(TAG, "fileName : " + fileName);
        if (TextUtils.isEmpty(filePath)) {
            PreviewDocImpl.setProgressListener(this);
            mLoadingPanel.setVisibility(View.VISIBLE);
            mFileName.setText(fileName);
        } else {
            showFile(filePath);
        }
    }

    private void initView() {
        mFileReadView = findViewById(R.id.file_read_view);
        mLoadingPanel = findViewById(R.id.loading_panel);
        mProgress = findViewById(R.id.doc_loading_progress);
        mFileTypeIcon = findViewById(R.id.file_type_icon);
        mFileName = findViewById(R.id.file_name);
        mFileSize = findViewById(R.id.file_size);
        Log.i(TAG, "hashcode = " + mProgress.hashCode());
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_doc_show, null);
    }

    private void showFile(String filePath) {
        if (mFileReadView != null) {
            mFileReadView.show(filePath);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFileReadView != null) {
            mFileReadView.stop();
        }
        PreviewDocImpl.cancelLoading();
    }

    @Override
    public void loadingFail(String url) {
        String text = getResources().getString(R.string.custom_doc_activity_load_doc_failed);
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadingProgress(float progress) {
        Log.i(TAG, "loadingProgress progress =  " + progress);
        mProgress.setProgress((int) (progress * 100));
    }

    @Override
    public void loadingFinish(String url, String path) {
        Log.i(TAG, "loadingFinish hashcode = " + hashCode());
        mLoadingPanel.setVisibility(View.GONE);
        showFile(path);
    }
}
