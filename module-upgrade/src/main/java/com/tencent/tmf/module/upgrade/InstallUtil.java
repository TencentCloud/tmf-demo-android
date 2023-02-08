package com.tencent.tmf.module.upgrade;

import static android.os.Build.VERSION_CODES.N;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import java.io.File;

/**
 * apk安装工具类
 * Created by winnieyzhou on 2019/4/22.
 */
public class InstallUtil {

    /**
     * 7.0以上版本安装apk需要FileProvider获取共享文件
     *
     * @param context
     * @param file apk文件
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= N) {
            return FileProvider.getUriForFile(context, "com.tencent.tmf.demo.FileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static void installApp(Context context, File file) {
        if (file != null && context != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= N) { //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setDataAndType(getUriForFile(context, file), "application/vnd.android.package-archive");
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                //有些手机丢失了packageInstaller系统apk，会crash
                e.printStackTrace();
            }
        }
    }
}
