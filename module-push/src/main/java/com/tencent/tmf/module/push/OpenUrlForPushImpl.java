package com.tencent.tmf.module.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.push.api.IOpenUrlForPush;

/**
 * 返回打开url的intent
 * Created by winnieyzhou on 2019/6/14.
 */
public class OpenUrlForPushImpl implements IOpenUrlForPush {

    @Override
    public Intent getIntent2OpenUrl(Context context, String url, String extra) {
        Log.i(PushRcvService.TAG, "getIntent2OpenUrl:" + url + ", extra=" + extra);
        if (!TextUtils.isEmpty(url)) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(context, "com.tencent.tmf.h5container.browser.BrowserActivity"));
            //intent.putExtra(JSapiActivity.PAGE_TYPE, JSapiActivity.WEB_PAGE);
            intent.putExtra(ModuleH5containerConst.P_URL, url);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;
        }
        return null;
    }
}
