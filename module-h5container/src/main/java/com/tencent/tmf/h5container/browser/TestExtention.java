package com.tencent.tmf.h5container.browser;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewClientExtension;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewCallbackClient;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class TestExtention extends ProxyWebViewClientExtension {

    private WebView mWebview;
    private final boolean LOG_VERBOSE = true;
    private final String LOGTAG = "SEED";

    /**
     * 循环向上转型, 获     * @param object : 子类对象
     *
     * @param methodName : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @return 父类中的方法对象
     */
    private Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;

        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                //这里甚么都不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会进入
            }
        }

        if (method == null) {
        }

        return null;
    }

    private void invokeMethod(Object object, String name, Class<?>[] parameterTypes, Object... args) {
        if (object == null) {

            return;
        }

        try {
            Method method = getDeclaredMethod(object, name, parameterTypes);
            method.setAccessible(true);
            method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {

            e.printStackTrace();
        }
    }

    private WebViewCallbackClient mCallbackClient;

    public TestExtention(WebView webView, WebViewCallbackClient callbackClient) {
        this.mWebview = webView;
        this.mCallbackClient = callbackClient;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, View view) {
        if (LOG_VERBOSE) {
            Log.e(LOGTAG, "ProxyWebViewClientExtension - onTouchEvent");
        }
        return mCallbackClient.onTouchEvent(event, view);
    }

    // 1
    public boolean onInterceptTouchEvent(MotionEvent ev, View view) {
        return mCallbackClient.onInterceptTouchEvent(ev, view);
    }

    // 3
    public boolean dispatchTouchEvent(MotionEvent ev, View view) {
        return mCallbackClient.dispatchTouchEvent(ev, view);
    }

    // 4
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY,
            int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent, View view) {
        return mCallbackClient.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent, view);
    }

    // 5
    public void onScrollChanged(int l, int t, int oldl, int oldt, View view) {
        mCallbackClient.onScrollChanged(l, t, oldl, oldt, view);
    }

    // 6
    public void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
            boolean clampedY, View view) {
        mCallbackClient.onOverScrolled(scrollX, scrollY, clampedX, clampedY, view);
    }

    // 7
    public void computeScroll(View view) {
        mCallbackClient.computeScroll(view);
    }

}
