package com.tencent.tmf.h5container.doc;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.tmf.x5docpreview.api.FileReaderView;

public class MyFileReaderView extends FileReaderView {

    public MyFileReaderView(Context context) {
        super(context);
    }

    public MyFileReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
        super.onCallBackAction(integer, o, o1);
        if (TbsReaderView.ReaderCallback.NOTIFY_CANDISPLAY == integer) {
            Log.e("SEED", "success open 111");
        }
    }

    public MyFileReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
