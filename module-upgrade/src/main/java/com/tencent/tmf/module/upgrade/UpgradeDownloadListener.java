package com.tencent.tmf.module.upgrade;

import java.io.File;

public interface UpgradeDownloadListener {

    void onProgressChanged(int progress);

    void onDownloadSucceed(File apkFile);

    void onDownloadField();

    void onCanceled();
}
