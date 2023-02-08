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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;

/**
 * {@link QMUIColorHelper} 的使用示例。
 * Created by Kayo on 2016/12/1.
 */

public class QDColorHelperFragment extends BaseFragment {

    QMUITopBar mTopBar;
    View mAlphaView;
    TextView mAlphaTextView;
    SeekBar mRatioSeekBar;
    TextView mTransformTextView;
    LinearLayout mRatioSeekBarWrap;

    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_colorhelper, null);
        mTopBar = root.findViewById(R.id.topbar);
        mAlphaView = root.findViewById(R.id.square_alpha);
        mAlphaTextView = root.findViewById(R.id.square_desc_alpha);
        mRatioSeekBar = root.findViewById(R.id.ratioSeekBar);
        mTransformTextView = root.findViewById(R.id.transformTextView);
        mRatioSeekBarWrap = root.findViewById(R.id.ratioSeekBarWrap);

        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
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

        mTopBar.setTitle(mQDItemDescription.getName());
    }

    private void initContent() {
        // 设置颜色的 alpha 值
        float alpha = 0.5f;
        int alphaColor = QMUIColorHelper
                .setColorAlpha(ContextCompat.getColor(getContext(), R.color.colorHelper_square_alpha_background),
                        alpha);
        mAlphaView.setBackgroundColor(alphaColor);
        mAlphaTextView.setText(String.format(getResources().getString(R.string.colorHelper_squqre_alpha), alpha));

        // 根据比例，在两个 color 值之间计算出一个 color 值
        final int fromColor = ContextCompat.getColor(getContext(), R.color.colorHelper_square_from_ratio_background);
        final int toColor = ContextCompat.getColor(getContext(), R.color.colorHelper_square_to_ratio_background);

        mRatioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int ratioColor = QMUIColorHelper.computeColor(fromColor, toColor, (float) progress / 100);
                mRatioSeekBarWrap.setBackgroundColor(ratioColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mRatioSeekBar.setProgress(50);

        // 将 color 颜色值转换为字符串
        String transformColor = QMUIColorHelper.colorToString(mTransformTextView.getCurrentTextColor());
        mTransformTextView.setText(String.format(getResources().getString(R.string.qmui_8), transformColor));
    }
}
