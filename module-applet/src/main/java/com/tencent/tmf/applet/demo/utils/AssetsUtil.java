package com.tencent.tmf.applet.demo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.tencent.tmfmini.sdk.launcher.log.QMLog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.List;

public class AssetsUtil {

    public static final String TAG = "AssetsUtil";

    public static boolean copyFileOrDir(Context context, String assetsPath, String destPath, List<String> targetFiles) {
        if (TextUtils.isEmpty(assetsPath) || TextUtils.isEmpty(destPath) || targetFiles == null
                || targetFiles.size() <= 0) {
            return false;
        }

        try {
            AssetManager assetManager = context.getAssets();
            File dir = new File(destPath);
            if (!dir.exists()) {
                dir.mkdir();
            }

            if (copyAssertToFile(assetsPath, destPath, targetFiles, assetManager)) {
                return false;
            }
            return true;
        } catch (Throwable e) {
            QMLog.e(TAG, String.format("copyFileOrDir assetsPath=%s, destPath=%s, exception", assetsPath, destPath), e);
            try {
                //拷贝失败时把目标目录删除
                File destFile = new File(destPath);
                if (destFile.exists()) {
                    delete(destPath, false);
                }
            } catch (Throwable e1) {
            }
            return false;
        }
    }

    public static boolean copyAssertToFile(String assetsPath, String destPath, List<String> targetFiles,
            AssetManager assetManager) {
        for (String file : targetFiles) {
            if (!TextUtils.isEmpty(file)) {
                File destFile = new File(destPath, file);
                if (!destFile.exists()) {
                    destFile.getParentFile().mkdirs();
                }

                String srcFilePath = assetsPath + File.separator + file;
                String destFilePath = destFile.getAbsolutePath();

                if (!doCopyAssetToFile(assetManager, srcFilePath, destFilePath)) {
                    QMLog.e(TAG, String.format("copyAssetToFile from=%s, to=%s fail", srcFilePath, destFilePath));
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean doCopyAssetToFile(AssetManager am, String assetFileName, String destFileName) {
        BufferedInputStream originFis = null;
        FileOutputStream out = null;
        FileLock outLock = null;
        try {
            out = new FileOutputStream(destFileName);
            outLock = out.getChannel().lock();
            originFis = new BufferedInputStream(am.open(assetFileName));
            byte[] originBuffer = new byte[8192];
            int originLength = 0;
            while ((originLength = originFis.read(originBuffer)) != -1) {
                out.write(originBuffer, 0, originLength);
            }
            return true;
        } catch (IOException e) {
            QMLog.e(TAG, "copyAssetToFile error! assetFileName:" + assetFileName + ", destFileName:" + destFileName, e);
        } finally {
            if (originFis != null) {
                try {
                    originFis.close();
                } catch (IOException e) {
                    QMLog.e(TAG,
                            "copyAssetToFile error! assetFileName:" + assetFileName + ", destFileName:" + destFileName,
                            e);
                }
            }
            if (out != null) {
                try {
                    out.getFD().sync();
                    if (outLock != null) {
                        outLock.release();
                    }
                } catch (IOException e) {
                    QMLog.e(TAG,
                            "copyAssetToFile error! assetFileName:" + assetFileName + ", destFileName:" + destFileName,
                            e);
                } finally {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        QMLog.e(TAG, "copyAssetToFile error! assetFileName:" + assetFileName + ", destFileName:"
                                + destFileName, ex);
                    }
                }
            }
        }
        return false;
    }

    public static long delete(String path, boolean ignoreDir) {
        if (path == null) {
            return 0;
        }
        File file = new File(path);
        if (file == null || !file.exists()) {
            return 0;
        }
        if (file.isFile()) {
            long size = file.length();
            file.delete();
            return size;
        }

        File[] fileList = file.listFiles();
        if (fileList == null) {
            return 0;
        }

        long size = 0;
        for (File f : fileList) {
            size += delete(f.getAbsolutePath(), ignoreDir);
        }
        // delete the folder if need.
        if (!ignoreDir) {
            file.delete();
        }
        return size;
    }

}

