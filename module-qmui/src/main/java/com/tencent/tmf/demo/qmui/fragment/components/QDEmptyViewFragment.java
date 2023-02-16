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

import android.view.LayoutInflater;
import android.view.View;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;


/**
 * {@link QMUIEmptyView} 的使用示例。
 * Created by cgspine on 15/9/14.
 */

public class QDEmptyViewFragment extends BaseFragment {

    QMUITopBarLayout mTopBar;
    QMUIEmptyView mEmptyView;

    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_emptyview, null);
        mTopBar = root.findViewById(R.id.topbar);
        mEmptyView = root.findViewById(R.id.emptyView);

        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        initTopBar();

        return root;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        // 切换其他情况的按钮
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheetList();
                    }
                });

        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem(getResources().getString(R.string.emptyView_mode_title_double_text))
                .addItem(getResources().getString(R.string.emptyView_mode_title_single_text))
                .addItem(getResources().getString(R.string.emptyView_mode_title_loading))
                .addItem(getResources().getString(R.string.emptyView_mode_title_single_text_and_button))
                .addItem(getResources().getString(R.string.emptyView_mode_title_double_text_and_button))
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                mEmptyView.show(getResources().getString(R.string.emptyView_mode_desc_double),
                                        getResources().getString(R.string.emptyView_mode_desc_detail_double));
                                break;
                            case 1:
                                mEmptyView.show(getResources().getString(R.string.emptyView_mode_desc_single), null);
                                break;
                            case 2:
                                mEmptyView.show(true);
                                break;
                            case 3:
                                mEmptyView
                                        .show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title),
                                                null, getResources().getString(R.string.emptyView_mode_desc_retry),
                                                null);
                                break;
                            case 4:
                                mEmptyView
                                        .show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title),
                                                getResources().getString(R.string.emptyView_mode_desc_fail_desc),
                                                getResources().getString(R.string.emptyView_mode_desc_retry), null);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .build()
                .show();
    }
}
