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

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;

public class QDFloatLayoutFragment extends BaseFragment {

    private static final String TAG = "QDFloatLayoutFragment";

    QMUITopBarLayout mTopBar;
    QMUIFloatLayout mFloatLayout;

    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_floatlayout, null);
        mTopBar = root.findViewById(R.id.topbar);
        mFloatLayout = root.findViewById(R.id.qmuidemo_floatlayout);

        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        initTopBar();
        for (int i = 0; i < 8; i++) {
            addItemToFloatLayout(mFloatLayout);
        }
        mFloatLayout.setOnLineCountChangeListener(new QMUIFloatLayout.OnLineCountChangeListener() {
            @Override
            public void onChange(int oldLineCount, int newLineCount) {
                Log.i(TAG, "oldLineCount = " + oldLineCount + " ;newLineCount = " + newLineCount);
            }
        });
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

        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheet();
                    }
                });

    }

    private void addItemToFloatLayout(QMUIFloatLayout floatLayout) {
        int currentChildCount = floatLayout.getChildCount();

        TextView textView = new TextView(getActivity());
        int textViewPadding = QMUIDisplayHelper.dp2px(getContext(), 4);
        textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_white));
        textView.setText(String.valueOf(currentChildCount));
        textView.setBackgroundResource(
                currentChildCount % 2 == 0 ? R.color.app_color_theme_3 : R.color.app_color_theme_6);

        int textViewSize = QMUIDisplayHelper.dpToPx(60);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(textViewSize, textViewSize);
        floatLayout.addView(textView, lp);
    }

    private void removeItemFromFloatLayout(QMUIFloatLayout floatLayout) {
        if (floatLayout.getChildCount() == 0) {
            return;
        }
        floatLayout.removeView(floatLayout.getChildAt(floatLayout.getChildCount() - 1));
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    private void showBottomSheet() {
        new QMUIBottomSheet.BottomListSheetBuilder(getContext())
                .addItem(getStringById(R.string.qmui_item_0))
                .addItem(getStringById(R.string.qmui_item_1))
                .addItem(getStringById(R.string.qmui_item_2))
                .addItem(getStringById(R.string.qmui_item_3))
                .addItem(getStringById(R.string.qmui_item_4))
                .addItem(getStringById(R.string.qmui_item_5))
                .addItem(getStringById(R.string.qmui_item_6))
                .addItem(getStringById(R.string.qmui_item_7))
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        switch (position) {
                            case 0:
                                addItemToFloatLayout(mFloatLayout);
                                break;
                            case 1:
                                removeItemFromFloatLayout(mFloatLayout);
                                break;
                            case 2:
                                mFloatLayout.setGravity(Gravity.LEFT);
                                break;
                            case 3:
                                mFloatLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                                break;
                            case 4:
                                mFloatLayout.setGravity(Gravity.RIGHT);
                                break;
                            case 5:
                                mFloatLayout.setMaxLines(1);
                                break;
                            case 6:
                                mFloatLayout.setMaxNumber(4);
                                break;
                            case 7:
                                mFloatLayout.setMaxLines(Integer.MAX_VALUE);
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

}
