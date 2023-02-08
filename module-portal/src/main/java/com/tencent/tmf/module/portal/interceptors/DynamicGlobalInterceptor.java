package com.tencent.tmf.module.portal.interceptors;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;

import com.tencent.tmf.module.portal.R;
import com.tencent.tmf.portal.Interceptor;
import com.tencent.tmf.portal.Response;

/**
 * Created by Xiaomao Yi on 2022/5/30.
 */
public class DynamicGlobalInterceptor implements Interceptor {
    @Override
    public void intercept(Chain chain) {
        new Handler(Looper.getMainLooper()).post(() -> new AlertDialog.Builder(chain.request().context())
                .setTitle(chain.request().context().getString(R.string.module_portal_6))
                .setMessage(chain.request().context().getString(R.string.module_portal_7))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> chain.proceed(chain.request()))
                .setNegativeButton(android.R.string.no,
                        (dialog, which) -> chain.terminate(Response.create(Response.STATUS_INTERCEPT).setMessage(
                                chain.request().context().getString(R.string.module_portal_8)).build()))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }
}
