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

package com.tencent.tmf.demo.qmui.fragment.lab;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomAreaBehavior;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomRecyclerView;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedTopAreaBehavior;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedTopDelegateLayout;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedTopRecyclerView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.tencent.tmf.demo.qmui.base.BaseRecyclerAdapter;
import com.tencent.tmf.demo.qmui.base.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QDContinuousNestedScroll4Fragment extends QDContinuousNestedScrollBaseFragment {

    private QMUIContinuousNestedTopDelegateLayout mTopDelegateLayout;
    private QMUIContinuousNestedTopRecyclerView mTopRecyclerView;
    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected void initCoordinatorLayout() {
        mTopDelegateLayout = new QMUIContinuousNestedTopDelegateLayout(getContext());
        mTopDelegateLayout.setBackgroundColor(Color.LTGRAY);
        mTopRecyclerView = new QMUIContinuousNestedTopRecyclerView(getContext());

        AppCompatTextView headerView = new AppCompatTextView(getContext()) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                        QMUIDisplayHelper.dp2px(getContext(), 100), MeasureSpec.EXACTLY
                ));
            }
        };
        headerView.setTextSize(17);
        headerView.setBackgroundColor(Color.GRAY);
        headerView.setTextColor(Color.WHITE);
        headerView.setText("This is Top Header");
        headerView.setGravity(Gravity.CENTER);
        mTopDelegateLayout.setHeaderView(headerView);

        AppCompatTextView footerView = new AppCompatTextView(getContext()) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                        QMUIDisplayHelper.dp2px(getContext(), 100), MeasureSpec.EXACTLY
                ));
            }
        };
        footerView.setTextSize(17);
        footerView.setBackgroundColor(Color.GRAY);
        footerView.setTextColor(Color.WHITE);
        footerView.setGravity(Gravity.CENTER);
        footerView.setText("This is Top Footer");
        mTopDelegateLayout.setFooterView(footerView);

        mTopDelegateLayout.setDelegateView(mTopRecyclerView);


        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        CoordinatorLayout.LayoutParams topLp = new CoordinatorLayout.LayoutParams(
                matchParent, matchParent);
        topLp.setBehavior(new QMUIContinuousNestedTopAreaBehavior(getContext()));
        mCoordinatorLayout.setTopAreaView(mTopDelegateLayout, topLp);

        mRecyclerView = new QMUIContinuousNestedBottomRecyclerView(getContext());
        CoordinatorLayout.LayoutParams recyclerViewLp = new CoordinatorLayout.LayoutParams(
                matchParent, matchParent);
        recyclerViewLp.setBehavior(new QMUIContinuousNestedBottomAreaBehavior());
        mCoordinatorLayout.setBottomAreaView(mRecyclerView, recyclerViewLp);

        mTopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mAdapter = new BaseRecyclerAdapter<String>(getContext(), null) {
            @Override
            public int getItemLayoutId(int viewType) {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, String item) {
                holder.setText(android.R.id.text1, item);
            }
        };
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Toast.makeText(getContext(), "click position=" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        mTopRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
        onDataLoaded();
    }

    private void onDataLoaded() {
        List<String> data = new ArrayList<>(Arrays.asList("Helps", "Maintain", "Liver",
                "Health", "Function", "Supports", "Healthy", "Fat", "Metabolism", "Nuturally",
                "Bracket", "Refrigerator", "Bathtub", "Wardrobe", "Comb", "Apron", "Carpet",
                "Bolster", "Pillow", "Cushion"));
        Collections.shuffle(data);
        mAdapter.setData(data);
    }
}
