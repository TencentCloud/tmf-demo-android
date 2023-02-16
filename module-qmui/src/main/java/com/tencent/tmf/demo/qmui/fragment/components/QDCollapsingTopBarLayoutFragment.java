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

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.adaptor.QDRecyclerViewAdapter;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.fragment.components.viewpager.QDLazyTestObserver;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * QDCollapsingTopBarLayoutFragment
 * @author cginechen
 * @date 2017-09-02
 */

public class QDCollapsingTopBarLayoutFragment extends BaseFragment {
    private static final String TAG = "CollapsingTopBarLayout";

    QDRecyclerViewAdapter mRecyclerViewAdapter;
    LinearLayoutManager mPagerLayoutManager;


    RecyclerView mRecyclerView;
    QMUICollapsingTopBarLayout mCollapsingTopBarLayout;
    QMUITopBar mTopBar;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_collapsing_topbar_layout, null);
        mTopBar = rootView.findViewById(R.id.topbar);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mCollapsingTopBarLayout = rootView.findViewById(R.id.collapsing_topbar_layout);
        initTopBar();
        mPagerLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mPagerLayoutManager);
        mRecyclerViewAdapter = new QDRecyclerViewAdapter();
        mRecyclerViewAdapter.setItemCount(10);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mCollapsingTopBarLayout.setScrimUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "scrim: " + animation.getAnimatedValue());
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLazyViewLifecycleOwner().getLifecycle().addObserver(
                new QDLazyTestObserver("QDCollapsingTopBar"));
    }

    @Override
    protected boolean translucentFull() {
        return true;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mCollapsingTopBarLayout.setTitle(QDDataManager.getInstance().getDescription(this.getClass()).getName());
    }
}
