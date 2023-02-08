package com.tencent.tmf.module.hybrid;

import android.content.Context;
import com.tencent.tmf.hybrid.ui.CustomViewProvider;
import com.tencent.tmf.hybrid.ui.IH5ContentViewProvider;
import com.tencent.tmf.hybrid.ui.IH5TitleViewProvider;


public class TestCustomViewProvider implements CustomViewProvider {

    @Override
    public IH5TitleViewProvider createTitleView(Context context) {
        return new TestCustomTitleView(context);
    }


    @Override
    public IH5ContentViewProvider createH5ContentView(Context context) {
        return new TestCustomContentView(context);
    }
}
