package com.tencent.tmf.h5container.jsapi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.tencent.smtt.sdk.WebView;

import java.util.List;

public class SpecialHandle {

    private static final String LOGTAG = "SpecialHandle";

    public static boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            if (!TextUtils.isEmpty(url) && (url.startsWith("mailto:") || url.startsWith("mail:") || url.startsWith("tel:")
                    || url.startsWith("smsto:") || url.startsWith("sms:"))) {
//            Toast.makeText(view.getContext(), "拦截特殊intent:"+url, Toast.LENGTH_LONG).show();
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                } else if (url.startsWith("mailto:") || url.startsWith("mail:")) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                } else if (url.startsWith("smsto:") || url.startsWith("sms:")) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
                return true;
            }

            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if ("http".equals(scheme) || "https".equals(scheme)) {
                // 只放开http和https类型请求
            } else {
//                Log.e(LOGTAG, "donot support intent:"+url);
                Intent action = new Intent(Intent.ACTION_VIEW);
                action.setData(Uri.parse(url));
                view.getContext().startActivity(action);

                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
}
