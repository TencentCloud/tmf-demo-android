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

package com.tencent.tmf.demo.qmui.fragment.util;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.qmuiteam.qmui.span.QMUIAlignMiddleImageSpan;
import com.qmuiteam.qmui.span.QMUIBlockSpaceSpan;
import com.qmuiteam.qmui.span.QMUICustomTypefaceSpan;
import com.qmuiteam.qmui.span.QMUIMarginImageSpan;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.UIContextHolder;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;

/**
 * QMUI 中各种 Span 的使用示例。
 * Created by Kayo on 2016/12/15.
 */

public class QDSpanFragment extends BaseFragment {

    /**
     * 特殊字体 人民币符号
     */
    public static Typeface TYPEFACE_RMB;

    static {
        try {
            Typeface tmpRmb = Typeface.createFromAsset(UIContextHolder.sContext.getAssets(),
                    "fonts/iconfont.ttf");
            TYPEFACE_RMB = Typeface.create(tmpRmb, Typeface.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    QMUITopBar mTopBar;
    TextView mAlignMiddleTextView;
    TextView mMarginImageTextView;
    TextView mBlockSpaceTextView;
    TextView mCustomTypefaceTextView;
    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_spanhelper, null);
        mTopBar = root.findViewById(R.id.topbar);
        mAlignMiddleTextView = root.findViewById(R.id.alignMiddle);
        mMarginImageTextView = root.findViewById(R.id.marginImage);
        mBlockSpaceTextView = root.findViewById(R.id.blockSpace);
        mCustomTypefaceTextView = root.findViewById(R.id.customTypeface);

        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        initTopBar();

        initContentView();

        return root;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private void initContentView() {
        // 支持垂直居中的 ImageSpan
        int alignMiddleIconLength = QMUIDisplayHelper.dp2px(getContext(), 20);
        final float spanWidthCharacterCount = 2f;
        SpannableString spannable = new SpannableString(
                "[icon]" + getStringById(R.string.qmui_item_21) + spanWidthCharacterCount + getStringById(R.string.qmui_item_22));
        Drawable iconDrawable = QMUIDrawableHelper
                .createDrawableWithSize(getResources(), alignMiddleIconLength, alignMiddleIconLength,
                        QMUIDisplayHelper.dp2px(getContext(), 4),
                        ContextCompat.getColor(getContext(), R.color.app_color_theme_3));
        if (iconDrawable != null) {
            iconDrawable.setBounds(0, 0, iconDrawable.getIntrinsicWidth(), iconDrawable.getIntrinsicHeight());
        }
        ImageSpan alignMiddleImageSpan = new QMUIAlignMiddleImageSpan(iconDrawable,
                QMUIAlignMiddleImageSpan.ALIGN_MIDDLE, spanWidthCharacterCount);
        spannable.setSpan(alignMiddleImageSpan, 0, "[icon]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mAlignMiddleTextView.setText(spannable);

        // 支持增加左右间距的 ImageSpan
        int marginImageLength = QMUIDisplayHelper.dp2px(getContext(), 20);
        Drawable marginIcon = QMUIDrawableHelper
                .createDrawableWithSize(getResources(), marginImageLength, marginImageLength,
                        QMUIDisplayHelper.dp2px(getContext(), 4),
                        ContextCompat.getColor(getContext(), R.color.app_color_theme_5));
        marginIcon.setBounds(0, 0, marginIcon.getIntrinsicWidth(), marginIcon.getIntrinsicHeight());
        CharSequence marginImageTextOne = getStringById(R.string.qmui_item_23);
        SpannableString marginImageText = new SpannableString(marginImageTextOne + getStringById(R.string.qmui_item_24));
        QMUIMarginImageSpan marginImageSpan = new QMUIMarginImageSpan(marginIcon, QMUIAlignMiddleImageSpan.ALIGN_MIDDLE,
                QMUIDisplayHelper.dp2px(getContext(), 10), QMUIDisplayHelper.dp2px(getContext(), 10));
        marginImageText.setSpan(marginImageSpan, marginImageTextOne.length(),
                marginImageTextOne.length() + "[margin]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mMarginImageTextView.setText(marginImageText);

        // 整行的空白 Span，可用来用于制作段间距
        String paragraphFirst = getStringById(R.string.qmui_item_25);
        String paragraphSecond = getStringById(R.string.qmui_item_26);
        String spaceString = "[space]";
        SpannableString paragraphText = new SpannableString(paragraphFirst + spaceString + paragraphSecond);
        QMUIBlockSpaceSpan blockSpaceSpan = new QMUIBlockSpaceSpan(getContext(), QMUIDisplayHelper.dp2px(getContext(), 6));
        paragraphText.setSpan(blockSpaceSpan, paragraphFirst.length(), paragraphFirst.length() + spaceString.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mBlockSpaceTextView.setText(paragraphText);

        // 自定义部分文字的字体
        SpannableString customTypefaceText = new SpannableString(
                getResources().getString(R.string.spanUtils_rmb) + getStringById(R.string.qmui_item_27)
                        + getResources().getString(R.string.spanUtils_rmb));
        customTypefaceText
                .setSpan(new QMUICustomTypefaceSpan("", TYPEFACE_RMB), 0, getString(R.string.spanUtils_rmb).length(),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mCustomTypefaceTextView.setText(customTypefaceText);
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
}

