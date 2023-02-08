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

package com.tencent.tmf.demo.qmui.fragment.components;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;

/**
 * QDSpanTouchFixTextViewFragment
 * @author cginechen
 * @date 2017-05-05
 */
public class QDSpanTouchFixTextViewFragment extends BaseFragment {

    QMUITopBar mTopBar;
    TextView mSystemTv1;
    TextView mSystemTv2;
    QMUISpanTouchFixTextView mSpanTouchFixTextView1;
    QMUISpanTouchFixTextView mSpanTouchFixTextView2;

    private int highlightTextNormalColor;
    private int highlightTextPressedColor;
    private int highlightBgNormalColor;
    private int highlightBgPressedColor;

    private LinearLayout mClickArea1;
    private LinearLayout mClickArea2;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.touch_fix_tv_1 || i == R.id.touch_fix_tv_2) {
                Toast.makeText(getContext(), "onClickTv", Toast.LENGTH_SHORT).show();

            } else if (i == R.id.click_area_1 || i == R.id.click_area_2) {
                Toast.makeText(getContext(), "onClickArea", Toast.LENGTH_SHORT).show();

            }
        }
    };
    private View.OnClickListener onClickListenerArea = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "onClickArea", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected View onCreateView() {
        highlightTextNormalColor = ContextCompat.getColor(getContext(), R.color.app_color_blue_2);
        highlightTextPressedColor = ContextCompat.getColor(getContext(), R.color.app_color_blue_2_pressed);
        highlightBgNormalColor = QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_8);
        highlightBgPressedColor = QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_6);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_touch_span_fix_layout, null);
        mTopBar = view.findViewById(R.id.topbar);
        mSystemTv1 = view.findViewById(R.id.sysytem_tv_1);
        mSystemTv2 = view.findViewById(R.id.sysytem_tv_2);
        mSpanTouchFixTextView1 = view.findViewById(R.id.touch_fix_tv_1);
        mSpanTouchFixTextView2 = view.findViewById(R.id.touch_fix_tv_2);
        mClickArea1 = view.findViewById(R.id.click_area_1);
        mClickArea2 = view.findViewById(R.id.click_area_2);
        mSpanTouchFixTextView1.setOnClickListener(mOnClickListener);
        mSpanTouchFixTextView2.setOnClickListener(mOnClickListener);
        mClickArea1.setOnClickListener(mOnClickListener);
        mClickArea2.setOnClickListener(mOnClickListener);

        initTopBar();

        // 场景一
        mSystemTv1.setMovementMethod(LinkMovementMethod.getInstance());
        mSystemTv1.setText(generateSp(getResources().getString(R.string.system_behavior_1)));

        mSpanTouchFixTextView1.setMovementMethodDefault();
        mSpanTouchFixTextView1.setText(generateSp(getResources().getString(R.string.span_touch_fix_1)));

        // 场景二
        mSystemTv2.setMovementMethod(LinkMovementMethod.getInstance());
        mSystemTv2.setText(generateSp(getResources().getString(R.string.system_behavior_2)));

        mSpanTouchFixTextView2.setMovementMethodDefault();
        mSpanTouchFixTextView2.setNeedForceEventToParent(true);
        mSpanTouchFixTextView2.setText(generateSp(getResources().getString(R.string.span_touch_fix_2)));

        return view;
    }

    private SpannableString generateSp(String text) {
        String highlight1 = "@qmui";
        String highlight2 = "#qmui#";
        SpannableString sp = new SpannableString(text);
        int start = 0;
        int end;
        int index;
        while ((index = text.indexOf(highlight1, start)) > -1) {
            end = index + highlight1.length();
            sp.setSpan(new QMUITouchableSpan(highlightTextNormalColor, highlightTextPressedColor,
                    highlightBgNormalColor, highlightBgPressedColor) {
                @Override
                public void onSpanClick(View widget) {
                    Toast.makeText(getContext(), "click @qmui", Toast.LENGTH_SHORT).show();
                }
            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            start = end;
        }

        start = 0;
        while ((index = text.indexOf(highlight2, start)) > -1) {
            end = index + highlight2.length();
            sp.setSpan(new QMUITouchableSpan(highlightTextNormalColor, highlightTextPressedColor,
                    highlightBgNormalColor, highlightBgPressedColor) {
                @Override
                public void onSpanClick(View widget) {
                    Toast.makeText(getContext(), "click #qmui#", Toast.LENGTH_SHORT).show();
                }
            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            start = end;
        }
        return sp;
    }

    private void initTopBar() {
        mTopBar.setTitle(QDDataManager.getInstance().getDescription(this.getClass()).getName());
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }
}
