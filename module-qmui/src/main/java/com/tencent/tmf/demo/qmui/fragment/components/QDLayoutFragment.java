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
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.qmuiteam.qmui.layout.QMUILayoutHelper;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;


/**
 * Created by cgspine on 2018/3/22.
 */

public class QDLayoutFragment extends BaseFragment {

    QMUITopBarLayout mTopBar;
    QMUILinearLayout mTestLayout;
    SeekBar mAlphaSeekBar;
    SeekBar mElevationSeekBar;
    TextView mAlphaTv;
    TextView mElevationTv;
    RadioGroup mHideRadiusGroup;

    private QDItemDescription mQDItemDescription;
    private float mShadowAlpha = 0.25f;
    private int mShadowElevationDp = 14;
    private int mRadius;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.shadow_color_red) {
                mTestLayout.setShadowColor(0xffff0000);
            } else if (view.getId() == R.id.shadow_color_blue) {
                mTestLayout.setShadowColor(0xff0000ff);
            }
        }
    };

    @Override
    protected View onCreateView() {
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 15);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_layout, null);
        mTopBar = view.findViewById(R.id.topbar);
        mTestLayout = view.findViewById(R.id.layout_for_test);
        mAlphaSeekBar = view.findViewById(R.id.test_seekbar_alpha);
        mElevationSeekBar = view.findViewById(R.id.test_seekbar_elevation);
        mAlphaTv = view.findViewById(R.id.alpha_tv);
        mElevationTv = view.findViewById(R.id.elevation_tv);
        mHideRadiusGroup = view.findViewById(R.id.hide_radius_group);
        view.findViewById(R.id.shadow_color_red).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.shadow_color_blue).setOnClickListener(mOnClickListener);
        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        initTopBar();
        initLayout();
        return view;
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

    private void initLayout() {
        mAlphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShadowAlpha = progress * 1f / 100;
                mAlphaTv.setText("alpha: " + mShadowAlpha);
                mTestLayout.setRadiusAndShadow(mRadius,
                        QMUIDisplayHelper.dp2px(getContext(), mShadowElevationDp),
                        mShadowAlpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mElevationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShadowElevationDp = progress;
                mElevationTv.setText("elevation: " + progress + "dp");
                mTestLayout.setRadiusAndShadow(mRadius,
                        QMUIDisplayHelper.dp2px(getActivity(), mShadowElevationDp), mShadowAlpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mAlphaSeekBar.setProgress((int) (mShadowAlpha * 100));
        mElevationSeekBar.setProgress(mShadowElevationDp);

        mHideRadiusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.hide_radius_none) {
                    mTestLayout.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_NONE);

                } else if (checkedId == R.id.hide_radius_left) {
                    mTestLayout.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_LEFT);

                } else if (checkedId == R.id.hide_radius_top) {
                    mTestLayout.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_TOP);

                } else if (checkedId == R.id.hide_radius_bottom) {
                    mTestLayout.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_BOTTOM);

                } else if (checkedId == R.id.hide_radius_right) {
                    mTestLayout.setRadius(mRadius, QMUILayoutHelper.HIDE_RADIUS_SIDE_RIGHT);

                }
            }
        });
    }
}
