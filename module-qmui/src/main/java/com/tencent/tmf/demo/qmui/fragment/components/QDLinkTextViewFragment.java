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
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.textview.QMUILinkTextView;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;


/**
 * QDLinkTextViewFragment
 * @author cginechen
 * @date 2017-05-05
 */

public class QDLinkTextViewFragment extends BaseFragment {

    QMUITopBar mTopBar;
    QMUILinkTextView mLinkTextView;


    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_link_texview_layout, null);
        mTopBar = view.findViewById(R.id.topbar);
        mLinkTextView = view.findViewById(R.id.link_text_view);
        initTopBar();
        mLinkTextView.setOnLinkClickListener(mOnLinkClickListener);
        mLinkTextView.setOnLinkLongClickListener(new QMUILinkTextView.OnLinkLongClickListener() {
            @Override
            public void onLongClick(String text) {
                Toast.makeText(getContext(), "long click: " + text, Toast.LENGTH_SHORT).show();
            }
        });
        mLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click TextView", Toast.LENGTH_SHORT).show();
            }
        });
        // if parent click event should be triggered when TextView area is clicked
//        mLinkTextView.setNeedForceEventToParent(true);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "forceEventToParent", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
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
    private String getStringById(int id){
        return getResources().getString(id);
    }

    private QMUILinkTextView.OnLinkClickListener mOnLinkClickListener = new QMUILinkTextView.OnLinkClickListener() {
        @Override
        public void onTelLinkClick(String phoneNumber) {
            Toast.makeText(getContext(), getStringById(R.string.qmui_item_13) + phoneNumber, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMailLinkClick(String mailAddress) {
            Toast.makeText(getContext(), getStringById(R.string.qmui_item_14) + mailAddress, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWebUrlLinkClick(String url) {
            Toast.makeText(getContext(), getStringById(R.string.qmui_item_15) + url, Toast.LENGTH_SHORT).show();
        }
    };
}
