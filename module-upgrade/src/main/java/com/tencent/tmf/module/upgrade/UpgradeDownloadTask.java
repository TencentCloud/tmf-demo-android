package com.tencent.tmf.module.upgrade;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpgradeDownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_CANCELED = 2;

    private volatile boolean cancelFlag = false;

    private UpgradeDownloadListener mDownloadListener;
    private File mApkFile;

    private Context mContext;

    public UpgradeDownloadTask(Context context) {
        mContext = context;
    }

    public void setDownloadListener(UpgradeDownloadListener listener) {
        mDownloadListener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                //更新包存储路径
                mApkFile = new File(mContext.getExternalFilesDir("Download"), "TMF_Demo_Upgrade.apk");

                if (mApkFile.exists()) {
                    mApkFile.delete();
                }

                if (!mApkFile.exists()) {
                    mApkFile.createNewFile();
                } else {
                    //安装包已存在时，可对已有的包进行校验，校验通过则无需下载
                }

                // 下载文件
                HttpURLConnection conn = (HttpURLConnection) new URL(strings[0]).openConnection();
                conn.connect();
                int length = conn.getContentLength();
                if (length <= 0) {
                    return TYPE_FAILED;
                }
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(mApkFile);

                try {
                    int count = 0;
                    byte[] buffer = new byte[2048];
                    while (true) {
                        if (cancelFlag) {
                            return TYPE_CANCELED;
                        }

                        int numread = is.read(buffer);
                        count += numread;
                        // 计算进度条的当前位置，进度更新回调
                        publishProgress((int) (((float) count / length) * 100));

                        // 下载完成
                        if (numread < 0) {
                            publishProgress(100);

                            return TYPE_SUCCESS;
                        }
                        fos.write(buffer, 0, numread);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    fos.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_SUCCESS:
                mDownloadListener.onDownloadSucceed(mApkFile);
                break;

            case TYPE_FAILED:
                mDownloadListener.onDownloadField();
                break;

            case TYPE_CANCELED:

                mDownloadListener.onCanceled();
                break;
            default:
                break;
        }
    }

    public boolean isCancelFlag() {
        return cancelFlag;
    }

    private long lastUpdateProgressTime = 0;
    @Override
    protected void onProgressUpdate(Integer... values) {
        if (!cancelFlag) {
            if (System.currentTimeMillis() - lastUpdateProgressTime > 500) {
                lastUpdateProgressTime = System.currentTimeMillis();
                mDownloadListener.onProgressChanged(values[0]);
            }
        }

    }

    public void cancelDownload() {
        cancelFlag = true;
    }
}
