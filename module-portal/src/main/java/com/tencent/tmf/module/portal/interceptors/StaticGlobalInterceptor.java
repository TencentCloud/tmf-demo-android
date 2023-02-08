package com.tencent.tmf.module.portal.interceptors;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;

import com.tencent.tmf.module.portal.R;
import com.tencent.tmf.portal.Interceptor;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.GlobalInterceptor;

/**
 * Created by Xiaomao Yi on 2022/5/30.
 */
//@GlobalInterceptor(name = "static")
public class StaticGlobalInterceptor implements Interceptor {

    @Override
    public void intercept(Interceptor.Chain chain) {
        new Handler(Looper.getMainLooper()).post(() -> new AlertDialog.Builder(chain.request().context())
                .setTitle(chain.request().context().getString(R.string.module_portal_55))
                .setMessage(chain.request().context().getString(R.string.module_portal_56))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Portal.unregisterGlobalInterceptor("static");
                    chain.proceed(chain.request());
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    chain.proceed(chain.request());
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }
}
