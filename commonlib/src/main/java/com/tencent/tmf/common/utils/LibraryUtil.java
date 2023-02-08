package com.tencent.tmf.common.utils;

import com.tencent.tinker.lib.tinker.TinkerApplicationHelper;
import com.tencent.tmf.hotpatch.api.application.HotpatchApplication;

/**
 * LibraryUtil
 * <p>
 * Created by lonrinelin on 2019/5/2.
 * Describe: lib相关
 */
public class LibraryUtil {

    /**
     * 依是否有使用tinker而选择特定的加载方式
     * @param libName
     */
    public static void loadLibrary(String libName) {
        if (HotpatchApplication.getTinkerApplication() != null) {
            // load lib/armeabi library
            TinkerApplicationHelper.loadArmV7aLibrary(HotpatchApplication.getTinkerApplication(), libName);
//            // load lib/armeabi-v7a library
//            TinkerLoadLibrary.loadArmV7Library(TMFApplicationContext.sApplication, libName);
        } else {
            System.loadLibrary(libName);
        }
    }

}
