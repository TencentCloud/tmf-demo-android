package com.tencent.tmf.module.icdp

import android.app.Application
import android.content.Context
import android.util.Log
import com.tencent.tmf.icdp.HotSplashManager
import com.tencent.tmf.icdp.ICDPOnAdClickListener
import java.lang.ref.WeakReference

fun init(context:Context){
    HotSplashManager.newBuilder()
            .bootId(Config.getInstance(context).hotSplash)
            .observe(context as Application)
            .displayPolicy(HotSplashManager.HotSplashDisplayPolicy.ONLY_SPECIFIED)// 只在指定的activity显示
            .include("com.tencent.tmf.module.icdp.IcdpActivity")
            .include("com.tencent.tmf.module.icdp.BoothActivity")
            .build()
    var listener: WeakReference<ICDPOnAdClickListener> =
            WeakReference(ICDPOnAdClickListener { boothIdentityId, url, icdpInstance, ext ->
                Log.e("HotSplash", "ICDPOnAdClickListener onclick boothIdentityId:"  + boothIdentityId);
            })
    HotSplashManager.registerOnAdClickListener(listener)
}
