package com.tencent.tmf.module.portal;

import android.os.Bundle;

import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.PortalMethod;
import com.tencent.tmf.portal.annotations.Destination;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Xiaomao Yi on 2022/5/30.
 */
@Destination(url = PortalConst.TIME_METHOD,
        launcher = Launcher.METHOD,
        description = "Portal演示")
public class TimeMethod implements PortalMethod {

    @Override
    public Bundle invoke(Bundle bundle) {
        SimpleDateFormat format = new SimpleDateFormat(bundle.containsKey("format") ? bundle.getString("format") :
                "EEE, MMM d, yyyy hh:mm:ss a z", Locale.getDefault());
        Bundle data = new Bundle();
        data.putString("result", format.format(new Date()));
        return data;
    }
}
