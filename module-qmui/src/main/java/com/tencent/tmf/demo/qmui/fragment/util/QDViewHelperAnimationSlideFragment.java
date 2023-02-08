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
import android.view.animation.Animation;
import android.widget.TextView;
import com.qmuiteam.qmui.util.QMUIDirection;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;


/**
 * {@link QMUIViewHelper#slideIn(View, int, Animation.AnimationListener, boolean, QMUIDirection)} 与
 * {@link QMUIViewHelper#slideOut(View, int, Animation.AnimationListener, boolean, QMUIDirection)} 的使用示例。
 * Created by Kayo on 2017/2/7.
 */

public class QDViewHelperAnimationSlideFragment extends BaseFragment {

    QMUITopBar mTopBar;
    QMUIRoundButton mActionButton;
    TextView mPopupView;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_viewhelper_animation_show_and_hide, null);
        mTopBar = root.findViewById(R.id.topbar);
        mActionButton = root.findViewById(R.id.actiontBtn);
        mPopupView = root.findViewById(R.id.popup);

        initTopBar();
        initContent();

        return root;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(QDDataManager.getInstance().getName(this.getClass()));
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    private void initContent() {
        mActionButton.setText(getStringById(R.string.qmui_item_51));
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupView.getVisibility() == View.GONE) {
                    mActionButton.setText(getStringById(R.string.qmui_item_52));
                    mPopupView.setText(getStringById(R.string.qmui_item_55));
                    QMUIViewHelper.slideIn(mPopupView, 500, null, true, QMUIDirection.TOP_TO_BOTTOM);
                } else {
                    mActionButton.setText(getStringById(R.string.qmui_item_51));
                    mPopupView.setText(getStringById(R.string.qmui_item_56));
                    QMUIViewHelper.slideOut(mPopupView, 500, null, true, QMUIDirection.BOTTOM_TO_TOP);

                }
            }
        });
    }
}
