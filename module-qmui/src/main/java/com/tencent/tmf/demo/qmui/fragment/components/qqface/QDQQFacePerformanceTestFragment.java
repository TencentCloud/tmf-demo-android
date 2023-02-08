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
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTabSegmentFixModeFragment;
import com.tencent.tmf.demo.qmui.fragment.components.qqface.pageview.QDEmojiconPagerView;
import com.tencent.tmf.demo.qmui.fragment.components.qqface.pageview.QDQQFacePagerView;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import java.util.HashMap;
import java.util.Map;

/**
 * QDQQFacePerformanceTestFragment
 * @author cginechen
 * @date 2017-06-08
 */

public class QDQQFacePerformanceTestFragment extends BaseFragment {

    QMUITopBarLayout mTopBar;
    QMUITabSegment mTabSegment;
    ViewPager mContentViewPager;

    private Map<Page, View> mPageMap = new HashMap<>();
    private Page mDestPage = Page.QMUIQQFaceView;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return QDTabSegmentFixModeFragment.ContentPage.SIZE;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            Page page = Page.getPage(position);
            View view = getPageView(page);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Page page = Page.getPage(position);
            if (page == Page.QMUIQQFaceView) {
                return getStringById(R.string.qmui_item_17);
            } else {
                return getStringById(R.string.qmui_item_18);
            }
        }
    };

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab_viewpager_layout, null);
        mTopBar = rootView.findViewById(R.id.topbar);
        mTabSegment = rootView.findViewById(R.id.tabSegment);
        mContentViewPager = rootView.findViewById(R.id.contentViewPager);

        initTopBar();
        initTabAndPager();

        return rootView;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(QDDataManager.getInstance().getDescription(this.getClass()).getName());
    }

    private void initTabAndPager() {
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        mTabSegment.setupWithViewPager(mContentViewPager, true);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }

    private View getPageView(Page page) {
        View view = mPageMap.get(page);
        if (view == null) {
            if (page == Page.QMUIQQFaceView) {
                view = new QDQQFacePagerView(getContext());
            } else if (page == Page.EmojiconTextView) {
                view = new QDEmojiconPagerView(getContext());
            } else {
                view = new QDQQFacePagerView(getContext());
            }
            mPageMap.put(page, view);
        }
        return view;
    }

    public enum Page {
        QMUIQQFaceView(0),
        EmojiconTextView(1);
        private final int position;

        Page(int pos) {
            position = pos;
        }

        public static Page getPage(int position) {
            switch (position) {
                case 0:
                    return QMUIQQFaceView;
                case 1:
                    return EmojiconTextView;
                default:
                    return QMUIQQFaceView;
            }
        }

        public int getPosition() {
            return position;
        }
    }
}
