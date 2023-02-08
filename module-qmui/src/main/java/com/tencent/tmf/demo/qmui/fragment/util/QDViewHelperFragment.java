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

import android.view.LayoutInflater;
import android.view.View;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;


/**
 * {@link QMUIViewHelper} 内各种方法的使用示例。
 * Created by Kayo on 2017/02/04.
 */

public class QDViewHelperFragment extends BaseFragment {

    QMUITopBarLayout mTopBar;
    QMUIGroupListView mGroupListView;
    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_grouplistview, null);
        mTopBar = root.findViewById(R.id.topbar);
        mGroupListView = root.findViewById(R.id.groupListView);

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

    private String getStringById(int id){
        return getResources().getString(id);
    }
    private void initContentView() {
        QMUIGroupListView.newSection(getContext())
                .setTitle(getStringById(R.string.qmui_item_59))
                .addItemView(mGroupListView.createItemView(
                        QDDataManager.getInstance().getName(QDViewHelperBackgroundAnimationBlinkFragment.class)),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QDViewHelperBackgroundAnimationBlinkFragment fragment =
                                        new QDViewHelperBackgroundAnimationBlinkFragment();
                                startFragment(fragment);
                            }
                        })
                .addItemView(mGroupListView.createItemView(
                        QDDataManager.getInstance().getName(QDViewHelperBackgroundAnimationFullFragment.class)),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QDViewHelperBackgroundAnimationFullFragment fragment =
                                        new QDViewHelperBackgroundAnimationFullFragment();
                                startFragment(fragment);
                            }
                        })
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle(getStringById(R.string.qmui_item_60))
                .addItemView(mGroupListView
                                .createItemView(
                                        QDDataManager.getInstance().getName(QDViewHelperAnimationFadeFragment.class)),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QDViewHelperAnimationFadeFragment fragment = new QDViewHelperAnimationFadeFragment();
                                startFragment(fragment);
                            }
                        })
                .addItemView(mGroupListView
                                .createItemView(
                                        QDDataManager.getInstance().getName(QDViewHelperAnimationSlideFragment.class)),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QDViewHelperAnimationSlideFragment fragment = new QDViewHelperAnimationSlideFragment();
                                startFragment(fragment);
                            }
                        })
                .addTo(mGroupListView);
    }

}
