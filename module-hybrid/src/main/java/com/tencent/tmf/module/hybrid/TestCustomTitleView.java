package com.tencent.tmf.module.hybrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.tencent.tmf.hybrid.ui.IH5TitleViewProvider;
import com.tencent.tmf.hybrid.ui.OptionType;
import com.tencent.tmf.hybrid.ui.ViewOnClickListener;
import org.json.JSONObject;

public class TestCustomTitleView implements IH5TitleViewProvider {

    private View mRoot;
    private Context mContext;

    public TestCustomTitleView(Context context) {
        mRoot = LayoutInflater.from(context).inflate(R.layout.layout_test_title_for_h5, null, false);
        this.mContext = context;
        mRoot.findViewById(R.id.back_home).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Log.e("SEED", "back pressed");
            }
        });

    }


    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(String titleContent) {

    }

    @Override
    public void setSubTitle(String subTitle) {

    }

    @Override
    public void setImgTitle(Bitmap imgBitmap) {

    }

    @Override
    public void setImgTitle(Bitmap var1, String var2) {

    }

    @Override
    public void showCloseButton(boolean var1) {

    }

    @Override
    public View getContentView() {
        return mRoot;
    }

    @Override
    public Drawable getContentBgView() {
        return null;
    }

    @Override
    public TextView getMainTitleView() {
        return null;
    }

    @Override
    public TextView getSubTitleView() {
        return null;
    }

    @Override
    public void showBackButton(boolean var1) {

    }

    @Override
    public void showOptionMenu(boolean var1) {

    }

    @Override
    public void setOptionType(OptionType var1) {

    }

    @Override
    public void setOptionType(OptionType var1, int var2, boolean var3) {

    }

    @Override
    public void showTitleLoading(boolean var1) {

    }

    @Override
    public void showTitleDisclaimer(boolean var1) {

    }

    @Override
    public void setBtIcon(Bitmap var1, int var2) {

    }

    @Override
    public void setBtIcon(Drawable var1, int var2) {

    }

    @Override
    public void setOptionMenu(JSONObject var1) {

    }

    @Override
    public void setOptionMenu(String var1, JSONObject var2) {

    }

    @Override
    public View getDivider() {
        return null;
    }

    @Override
    public View getHdividerInTitle() {
        return null;
    }

    @Override
    public View getPopAnchor() {
        return null;
    }

    @Override
    public void resetTitleColor(int var1) {

    }

    @Override
    public void switchToWhiteTheme() {

    }

    @Override
    public void switchToBlueTheme() {

    }

    @Override
    public void releaseViewList() {

    }

    @Override
    public void openTranslucentStatusBarSupport(int var1) {

    }

    @Override
    public void switchToTitleBar() {

    }

    @Override
    public View setTitleBarSearch(Bundle var1) {
        return null;
    }

    @Override
    public void setBackCloseBtnImage(String var1) {

    }

    @Override
    public void setTitleTxtColor(int var1) {

    }

    @Override
    public View getOptionMenuContainer() {
        return null;
    }

    @Override
    public View getOptionMenuContainer(int var1) {
        return null;
    }

    @Override
    public void setTitleView(View var1) {

    }

    @Override
    public void initTitleSegControl(JSONObject var1) {

    }

    @Override
    public void enableTitleSegControl(boolean var1) {

    }

    @Override
    public void enableBackButtonBackground(boolean var1) {

    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void setBackgroundAlphaValue(int resId) {

    }

    @Override
    public void setBackgroundColor(int color) {

    }

    @Override
    public void setTitleColorWhiteTheme() {

    }

    @Override
    public void setTitleColorBlueTheme() {

    }

    @Override
    public void setIconThemeWithColor() {

    }

    @Override
    public void showBackHome(boolean show) {

    }

    @Override
    public void addViewClickListen(ViewOnClickListener clickListener) {

    }
}
