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

package com.tencent.tmf.demo.qmui.fragment.components.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentPagerAdapter;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDCollapsingTopBarLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTabSegmentScrollableModeFragment;

/**
 * QDFitSystemWindowViewPagerFragment
 * @author cginechen
 * @date 2017-09-13
 */

public class QDFitSystemWindowViewPagerFragment extends BaseFragment {

    QMUIViewPager mViewPager;
    QMUITabSegment mTabSegment;

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_fsw_viewpager, null);
        mTabSegment = layout.findViewById(R.id.tabs);
        mViewPager = layout.findViewById(R.id.pager);
        initPagers();
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLazyViewLifecycleOwner().getLifecycle().addObserver(new QDLazyTestObserver("QDfSWViewPager"));
    }

    private void initPagers() {
        QMUIFragmentPagerAdapter pagerAdapter = new QMUIFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public QMUIFragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new QDTabSegmentScrollableModeFragment();
                    case 1:
                        return new QDCollapsingTopBarLayoutFragment();
                    case 2:
                        return new QDFitSystemWindowViewPagerFragment();
                    case 3:
                    default:
                        return new QDViewPagerFragment();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "TabSegment";
                    case 1:
                        return "CTopBar";
                    case 2:
                        return "IViewPager";
                    case 3:
                    default:
                        return "ViewPager";
                }
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager);
    }

    @Override
    protected boolean canDragBack() {
        return mViewPager.getCurrentItem() == 0;
    }
}
