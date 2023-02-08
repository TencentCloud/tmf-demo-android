package com.tencent.tmf.module.portal.interceptors;

import com.tencent.tmf.portal.Interceptor;
import com.tencent.tmf.portal.Request;

/**
 * Created by Xiaomao Yi on 2022/5/30.
 */
public class MethodInterceptor implements Interceptor {

    @Override
    public void intercept(Chain chain) {
        Request request = Request.create(chain.request()).param("format", "yyyy-MM-dd HH:mm:ss").build();
        request.setDestination(chain.request().destination());
        chain.proceed(request);
    }
}
