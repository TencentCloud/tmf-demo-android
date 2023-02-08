package com.tencent.tmf.applet.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.tmf.applet.demo.R;

public abstract class BaseBottomDialog extends Dialog {

    public BaseBottomDialog(Context context) {
        super(context, R.style.TmfBottomDialog);

        View contentView = LayoutInflater.from(context).inflate(getLayout(), null);
        setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);

        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        getWindow().setWindowAnimations(R.style.TmfBottomDialog_Animation);
    }

    public abstract int getLayout();
}
