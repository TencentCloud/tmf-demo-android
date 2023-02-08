package com.tencent.tmf.common.config;

import android.content.Context;
import android.util.Log;
import java.io.File;

public class AppDataUtil {

    private static final String TAG = "TMFConfigActivity";

    /**
     * 使用系统命令清除APP所有数据
     *
     * @param context
     */
    public static void clearAppData(Context context) {
        try {
            Process p = Runtime.getRuntime().exec("pm clear " + context.getPackageName());
            int status = p.waitFor();
            Log.i(TAG, "[tmf_debug]pm clear, status: " + status);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "[tmf_debug]pm clear, e: ", e);
        }
    }

    /**
     * 清除APP指定数据
     */
    public static void clearData(Context context) {
        final String path = context.getFilesDir().getParent();

        //清空配置文件目录shared_prefs；
        File fileXml = new File(path + "/shared_prefs");
        if (fileXml.exists()) {
            File[] files = fileXml.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }

        //清空缓存目录；
        File fileCache = context.getCacheDir();
        if (fileCache.exists()) {
            File[] files = fileCache.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }

        //清空file目录；
        File fileFile = new File(path + "/files");
        ;
        if (fileFile.exists()) {
            File[] files = fileFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }

        //清空数据库目录；
        File fileDb = new File(path + "/databases");
        if (fileDb.exists()) {
            File[] files = fileDb.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public static void killMyself() {
        //这里可以重启你的应用程序，我的app中有service，所以我只要杀死进程就自动重启了。
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
