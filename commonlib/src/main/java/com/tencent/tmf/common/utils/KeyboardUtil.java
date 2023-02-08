package com.tencent.tmf.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.tencent.smtt.sdk.WebView;
import com.tencent.tmf.webview.api.webview.ITMFWebView;

public class KeyboardUtil {

    /**
     * 显示系统键盘
     *
     * @param activity
     * @param itmfWebView
     */
    public static void showSysKeyboard(Activity activity, ITMFWebView itmfWebView) {
        if (itmfWebView == null) {
            return;
        }

        Object object = itmfWebView.getWebView();
        View focusView = null;
        if (object instanceof WebView) {
            //X5内核的webview
            focusView = ((WebView) object).findFocus();
        } else if (object instanceof android.webkit.WebView) {
            //系统的webview
            focusView = ((android.webkit.WebView) object).findFocus();
        }

        final InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            if (focusView != null) {
                inputManager.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
            } else {
                //如果系统输入法是关闭状态，那么就调起输入法
                if (inputManager.isActive() && !KeyboardUtil.isSoftShowing(activity)) {
                    inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 关闭系统键盘
     *
     * @param activity
     * @param itmfWebView
     */
    public static void closeSysKeyboard(Activity activity, ITMFWebView itmfWebView) {
        Object object = itmfWebView.getWebView();
        View focusView = null;
        if (object instanceof WebView) {
            //X5内核的webview
            focusView = ((WebView) object).findFocus();
        } else if (object instanceof android.webkit.WebView) {
            //系统的webview
            focusView = ((android.webkit.WebView) object).findFocus();
        }

        if (focusView == null) {
            focusView = activity.getWindow().peekDecorView();
        }

        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            if (focusView != null) {
                inputManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            } else {
                //如果系统输入法是显示状态，那么就关闭输入法
                if (inputManager.isActive() && KeyboardUtil.isSoftShowing(activity)) {
                    inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 判断软键盘是否显示方法
     *
     * @param activity
     * @return
     */

    public static boolean isSoftShowing(Activity activity) {
        //获取当屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom + getSoftButtonsBarHeight(activity);
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
}
