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

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.activity.TranslucentActivity;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;


/**
 * {@link QMUIStatusBarHelper} 的使用示例。
 * Created by Kayo on 2016/12/12.
 */

public class QDStatusBarHelperFragment extends BaseFragment {

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

        initGroupListView();

        return root;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIStatusBarHelper.setStatusBarDarkMode(getBaseFragmentActivity()); // 退出界面之前把状态栏还原为白色字体与图标
                popBackStack();
            }
        });

        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private void initGroupListView() {

        QMUIGroupListView.newSection(getContext())
                .setDescription(getStringById(R.string.qmui_item_28))
                .addItemView(mGroupListView.createItemView(getStringById(R.string.qmui_item_29)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentTranslucent = new Intent(getContext(), TranslucentActivity.class);
                        startActivity(intentTranslucent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_still);
                    }
                })
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setDescription(getStringById(R.string.qmui_item_30))
                .addItemView(mGroupListView.createItemView(getStringById(R.string.qmui_item_31)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
                    }
                })
                .addItemView(mGroupListView.createItemView(getStringById(R.string.qmui_item_32)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QMUIStatusBarHelper.setStatusBarDarkMode(getBaseFragmentActivity());
                    }
                })
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setDescription(getStringById(R.string.qmui_item_33))
                .addItemView(mGroupListView.createItemView(getStringById(R.string.qmui_item_34)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String result = String
                                .format(getResources().getString(R.string.statusBarHelper_statusBar_height_result),
                                        QMUIStatusBarHelper.getStatusbarHeight(getContext()));
                        final QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext()).setTipWord(result)
                                .create();
                        tipDialog.show();
                        mGroupListView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tipDialog.dismiss();
                            }
                        }, 1500);
                    }
                })
                .addTo(mGroupListView);

    }

    private String getStringById(int id){
        return getResources().getString(id);
    }
}
