package com.tencent.tmf.module.portal;

import android.os.Bundle;

import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.PortalMethod;
import com.tencent.tmf.portal.annotations.Destination;

/**
 * Created by Xiaomao Yi on 2022/5/30.
 */
@Destination(url = PortalConst.ASYNC_METHOD,
        launcher = Launcher.METHOD,
        description = "Portal异步演示")
public class AsyncMethod implements PortalMethod {

    @Override
    public Bundle invoke(Bundle bundle) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bundle data = new Bundle();
        data.putString("result", "Async task complete");
        return data;
    }
}
