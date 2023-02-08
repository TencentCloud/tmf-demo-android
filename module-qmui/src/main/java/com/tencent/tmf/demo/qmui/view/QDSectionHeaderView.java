/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.tencent.tmf.demo.qmui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.model.SectionHeader;

public class QDSectionHeaderView extends LinearLayout {

    private TextView mTitleTv;
    private ImageView mArrowView;

    private int headerHeight = QMUIDisplayHelper.dp2px(getContext(), 56);

    public QDSectionHeaderView(Context context) {
        this(context, null);
    }

    public QDSectionHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(Color.WHITE);

        mTitleTv = new TextView(getContext());
        mTitleTv.setTextSize(20);
        mTitleTv.setTextColor(Color.BLACK);

        int paddingHor = QMUIDisplayHelper.dp2px(context, 24);
        mTitleTv.setPadding(paddingHor, 0, paddingHor, 0);
        addView(mTitleTv, new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        mArrowView = new AppCompatImageView(context);
        mArrowView.setImageDrawable(QMUIResHelper.getAttrDrawable(getContext(),
                R.attr.qmui_common_list_item_chevron));
        mArrowView.setScaleType(ImageView.ScaleType.CENTER);
        addView(mArrowView, new LayoutParams(headerHeight, headerHeight));
    }

    public ImageView getArrowView() {
        return mArrowView;
    }

    public void render(SectionHeader header, boolean isFold) {
        mTitleTv.setText(header.getText());
        mArrowView.setRotation(isFold ? 0f : 90f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(headerHeight, MeasureSpec.EXACTLY));
    }
}
