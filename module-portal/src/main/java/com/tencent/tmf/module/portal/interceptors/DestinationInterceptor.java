package com.tencent.tmf.module.portal.interceptors;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.widget.EditText;

import com.tencent.tmf.module.portal.R;
import com.tencent.tmf.portal.Interceptor;
import com.tencent.tmf.portal.Request;

/**
 * Created by Xiaomao Yi on 2022/5/30.
 */
public class DestinationInterceptor implements Interceptor {

    @Override
    public void intercept(Chain chain) {
        final Context context = chain.request().context();
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.module_portal_3));
        builder.setView(input);
        builder.setPositiveButton(context.getResources().getString(R.string.module_portal_4), (dialog, which) -> {
            Request request = Request.create(chain.request()).param("param", input.getText().toString()).build();
            request.setDestination(chain.request().destination());
            chain.proceed(request);
        });
        builder.setNegativeButton(context.getResources().getString(R.string.module_portal_5), (dialog, which) -> {
            chain.proceed(chain.request());
        });
        new Handler(Looper.getMainLooper()).post(builder::show);
    }
}
