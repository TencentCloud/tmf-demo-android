package com.tencent.tmf.module.upgrade;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by winnieyzhou on 2019/5/10.
 */
public class UpgradeDownloader {

    public interface DownLoadProgressListener {

        void onProgressChanged(int progress);

        void onDownloadFinished(File apkFile);
    }

    public static void startDownloadApk(final Context context, final String apkUrl,
            final DownLoadProgressListener progressListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                        long downloadLength = 0;
                        //更新包存储路径
                        File apkFile = new File(context.getExternalFilesDir("Download"), "TMF_Demo_Upgrade.apk");

                        if (apkFile.exists()) {
                            apkFile.delete();
                        }

                        if (!apkFile.exists()) {
                            apkFile.createNewFile();
                        } else {
                            //安装包已存在时，可对已有的包进行校验，校验通过则无需下载
                        }

                        // 下载文件
                        HttpURLConnection conn = (HttpURLConnection) new URL(apkUrl).openConnection();
                        conn.connect();
                        int length = conn.getContentLength();
                        if (length <= 0) {
                            return;
                        }
                        InputStream is = conn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(apkFile);

                        try {
                            int count = 0;
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int numread = is.read(buffer);
                                count += numread;
                                // 计算进度条的当前位置，进度更新回调
                                progressListener.onProgressChanged((int) (((float) count / length) * 100));

                                // 下载完成
                                if (numread < 0) {
                                    progressListener.onProgressChanged(100);
                                    progressListener.onDownloadFinished(apkFile);
                                    break;
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
            }
        }).start();
    }
}
