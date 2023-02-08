package com.tencent.tmf.common.utils;

import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class FileUtil {

    /**
     * 写文件
     *
     * @param file
     * @param data
     * @param append 是否为追加方式，如果false则每次替换原来的内容
     */
    public static boolean write2File(File file, byte[] data, boolean append) {
        if (data == null) {
            return false;
        }

        FileOutputStream os = null;
        try {
            if (!file.exists()) {
                file.getAbsoluteFile().getParentFile().mkdirs();
                file.createNewFile();
            }
            os = new FileOutputStream(file, append);
            os.write(data);
            return true;
        } catch (Throwable e) {
            Log.w("FileUtil", "write2File(): " + e, e);
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 读取文件内容
     */
    public static byte[] readFile(File file) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String getFileMD5(File file) {
        byte[] fileBytes = readFile(file);
        if (fileBytes == null || fileBytes.length <= 0) {
            return "";
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 从文件下载URL得到下载文件名称
     *
     * @param url
     * @param defaultName
     * @return
     */
    public static final String guessFileName(String url, String defaultName) {
        String fileName = null;

        // If all the other http-related approaches failed, use the plain uri
        if (fileName == null) {
            String decodedUrl = Uri.decode(url);
            if (decodedUrl != null) {
                int queryIndex = decodedUrl.indexOf('?');
                // If there is a query string strip it, same as desktop browsers
                if (queryIndex > 0) {
                    decodedUrl = decodedUrl.substring(0, queryIndex);
                }
                if (!decodedUrl.endsWith("/")) {
                    int index = decodedUrl.lastIndexOf('/') + 1;
                    if (index > 0) {
                        fileName = decodedUrl.substring(index);
                    }
                }
            }
        }

        if (fileName == null) {
            fileName = defaultName;
        }
        // Finally, if couldn't get filename from URI, get a generic filename
        if (fileName == null) {
            fileName = "downloadfile" + url.hashCode();
        }

        return fileName;
    }

    /**
     * 是否插有存储卡
     *
     * @return 是否插有存储卡
     */
    public static boolean hasStorageCard() {
        try {
            String state = Environment.getExternalStorageState();
            return state.equals(Environment.MEDIA_MOUNTED);
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 存储卡挂载成功环境下检查存储卡是否可写
     */
    public static boolean hasStorageCardReadWritePermission() {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
        String directoryName = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        File f = new File(directoryName, ".probe");
        try {
            // Remove stale file if any
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * 获取Sdcard的状态] 0:已挂载可读写；1：未挂载；2已挂载不可读或写；3磁盘是否满
     *
     * @return
     */
    public static int getSdcardStatus(long size) {
        if (!hasStorageCard()) {
            return 1;
        } else {
            if (!hasStorageCardReadWritePermission()) {
                return 2;
            } else {
                SizeInfo si = new SizeInfo();
                getSizeInfo(Environment.getExternalStorageDirectory(), si);
                // 磁盘不足
                if (si.availdSize < size) {
                    return 3;
                }
                return 0;
            }
        }
    }

    public static class SizeInfo {

        /**
         * 可用大小
         */
        public long availdSize;

        /**
         * 总共大小
         */
        public long totalSize;
    }

    /**
     * 查询指定目录空间信息
     *
     * @param path
     * @param info
     */
    public static void getSizeInfo(File path, SizeInfo info) {
        /*
         * crash:64261643 加保护
         * by maxxx
         */
        try {
            StatFs statfs = new StatFs(path.getPath());

            long blockSize = statfs.getBlockSize();// 获取block的SIZE
            info.availdSize = statfs.getAvailableBlocks() * blockSize;
            info.totalSize = statfs.getBlockCount() * blockSize;
        } catch (Exception e) {
            Log.e("", "getSizeInfo: ", e);
        }
    }

}
