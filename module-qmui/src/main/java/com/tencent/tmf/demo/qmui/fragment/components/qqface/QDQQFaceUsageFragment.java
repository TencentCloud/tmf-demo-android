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

package com.tencent.tmf.demo.qmui.fragment.components.qqface;

import android.view.LayoutInflater;
import android.view.View;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;


/**
 * QDQQFaceUsageFragment
 * @author cginechen
 * @date 2016-12-24
 */

public class QDQQFaceUsageFragment extends BaseFragment {

    QMUITopBar mTopBar;
    QDQQFaceView mQQFace1;
    QDQQFaceView mQQFace2;
    QDQQFaceView mQQFace3;
    QDQQFaceView mQQFace4;
    QDQQFaceView mQQFace5;
    QDQQFaceView mQQFace6;
    QDQQFaceView mQQFace7;
    QDQQFaceView mQQFace8;
    QDQQFaceView mQQFace9;
    QDQQFaceView mQQFace10;
    QDQQFaceView mQQFace11;
    QDQQFaceView mQQFace12;
    QDQQFaceView mQQFace13;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_qqface_layout, null);
        mTopBar = view.findViewById(R.id.topbar);
        mQQFace1 = view.findViewById(R.id.qqface1);
        mQQFace2 = view.findViewById(R.id.qqface2);
        mQQFace3 = view.findViewById(R.id.qqface3);
        mQQFace4 = view.findViewById(R.id.qqface4);
        mQQFace5 = view.findViewById(R.id.qqface5);
        mQQFace6 = view.findViewById(R.id.qqface6);
        mQQFace7 = view.findViewById(R.id.qqface7);
        mQQFace8 = view.findViewById(R.id.qqface8);
        mQQFace9 = view.findViewById(R.id.qqface9);
        mQQFace10 = view.findViewById(R.id.qqface10);
        mQQFace11 = view.findViewById(R.id.qqface11);
        mQQFace12 = view.findViewById(R.id.qqface12);
        mQQFace13 = view.findViewById(R.id.qqface13);
        initTopBar();
        initData();
        return view;
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

    private void initData() {
        mQQFace1.setText("这是一行很长很长[微笑][微笑][微笑][微笑]的文本，但是[微笑][微笑][微笑][微笑]只能单行显示");
        mQQFace2.setText("这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行；"
                + "这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行；"
                + "这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行。");
        mQQFace3.setText("这是一行很长很长[微笑][微笑][微笑][微笑]的文本，但是[微笑][微笑][微笑][微笑]只能单行显示");
        mQQFace4.setText("这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行；"
                + "这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行；"
                + "这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行。");
        mQQFace5.setText("这是一行很长很长[微笑][微笑][微笑][微笑]的文本，但是[微笑][微笑][微笑][微笑]只能单行显示");
        mQQFace6.setText("这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行；"
                + "这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行；"
                + "这是一段很长很长[微笑][微笑][微笑][微笑]的文本，但是最多只能显示三行。");

        mQQFace7.setText("[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]");
        mQQFace8.setText("[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]");
        mQQFace9.setText("[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]");
        mQQFace10.setText("[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑]");
        mQQFace11.setText("[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑]");
        mQQFace12.setText("[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑]");
        mQQFace13.setText("表情可以和字体一起变大[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑][微笑]"
                + "[微笑][微笑][微笑][微笑][微笑]");
    }
}
