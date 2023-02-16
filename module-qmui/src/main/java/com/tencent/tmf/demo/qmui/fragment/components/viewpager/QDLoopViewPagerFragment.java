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

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIPagerAdapter;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUIViewPager;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import java.util.ArrayList;
import java.util.List;

/**
 * QDLoopViewPagerFragment
 * @author cginechen
 * @date 2017-09-13
 */

public class QDLoopViewPagerFragment extends BaseFragment {

    QMUITopBar mTopBar;
    QMUIViewPager mViewPager;

    private List<String> mItems = new ArrayList<>();


    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_loop_viewpager, null);
        mTopBar = layout.findViewById(R.id.topbar);
        mViewPager = layout.findViewById(R.id.pager);
        initData(5);
        initTopBar();
        initPagers();
        return layout;
    }

    private void initData(int count) {
        for (int i = 0; i < count; i++) {
            mItems.add(String.valueOf(i));
        }
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


    private void initPagers() {
        QMUIPagerAdapter pagerAdapter = new QMUIPagerAdapter() {

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mItems.get(position);
            }

            @Override
            @NonNull
            protected Object hydrate(@NonNull ViewGroup container, int position) {
                return new ItemView(getContext());
            }

            @Override
            protected void populate(@NonNull ViewGroup container, @NonNull Object item, int position) {
                ItemView itemView = (ItemView) item;
                itemView.setText(mItems.get(position));
                container.addView(itemView);
            }

            @Override
            protected void destroy(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        };
        //setPageTransformer默认采用ViewCompat.LAYER_TYPE_HARDWARE， 但它在某些4.x的国产机下会crash
        boolean canUseHardware = Build.VERSION.SDK_INT >= 21;
        mViewPager.setPageTransformer(false, new CardTransformer(),
                canUseHardware ? ViewCompat.LAYER_TYPE_HARDWARE : ViewCompat.LAYER_TYPE_SOFTWARE);
        mViewPager.setInfiniteRatio(500);
        mViewPager.setEnableLoop(true);
        mViewPager.setAdapter(pagerAdapter);
    }

    static class ItemView extends FrameLayout {

        private TextView mTextView;

        public ItemView(Context context) {
            super(context);
            mTextView = new TextView(context);
            mTextView.setTextSize(20);
            mTextView.setTextColor(ContextCompat.getColor(context, R.color.app_color_theme_5));
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.qmui_config_color_white));
            int size = QMUIDisplayHelper.dp2px(context, 300);
            LayoutParams lp = new LayoutParams(size, size);
            lp.gravity = Gravity.CENTER;
            addView(mTextView, lp);
        }

        public void setText(CharSequence text) {
            mTextView.setText(text);
        }
    }
}
