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

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;

/**
 * {@link QMUIDrawableHelper} 的使用示例。
 * Created by Kayo on 2016/12/5.
 */

public class QDDrawableHelperFragment extends BaseFragment {

    QMUITopBar mTopBar;
    QMUIRoundButton mCreateFromViewButton;
    ImageView mSolidImageView;
    ImageView mCircleGradientView;
    ImageView mTintColorImageView;
    ImageView mTintColorOriginImageView;
    View mSeparatorView;

    private View mRootView;

    private QDItemDescription mQDItemDescription;

    @Override
    protected View onCreateView() {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_drawablehelper, null);
        mTopBar = mRootView.findViewById(R.id.topbar);
        mCreateFromViewButton = mRootView.findViewById(R.id.createFromView);
        mSolidImageView = mRootView.findViewById(R.id.solidImage);
        mCircleGradientView = mRootView.findViewById(R.id.circleGradient);
        mTintColorImageView = mRootView.findViewById(R.id.tintColor);
        mTintColorOriginImageView = mRootView.findViewById(R.id.tintColorOrigin);
        mSeparatorView = mRootView.findViewById(R.id.separator);

        mQDItemDescription = QDDataManager.getInstance().getDescription(this.getClass());
        initTopBar();

        initContent();

        return mRootView;
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
        int commonShapeSize = getResources().getDimensionPixelSize(R.dimen.drawableHelper_common_shape_size);
        int commonShapeRadius = QMUIDisplayHelper.dp2px(getContext(), 10);

        // 创建一张指定大小的纯色图片，支持圆角
        BitmapDrawable solidImageBitmapDrawable = QMUIDrawableHelper
                .createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius,
                        ContextCompat.getColor(getContext(), R.color.app_color_theme_3));
        mSolidImageView.setImageDrawable(solidImageBitmapDrawable);

        // 创建一张圆形渐变图片，支持圆角
        GradientDrawable gradientCircleGradientDrawable = QMUIDrawableHelper
                .createCircleGradientDrawable(ContextCompat.getColor(getContext(), R.color.app_color_theme_4),
                        ContextCompat.getColor(getContext(), R.color.qmui_config_color_transparent), commonShapeRadius,
                        0.5f, 0.5f);
        mCircleGradientView.setImageDrawable(gradientCircleGradientDrawable);

        // 设置 Drawable 的颜色
        // 创建两张表现相同的图片
        BitmapDrawable tintColorBitmapDrawble = QMUIDrawableHelper
                .createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius,
                        ContextCompat.getColor(getContext(), R.color.app_color_theme_1));
        BitmapDrawable tintColorOriginBitmapDrawble = QMUIDrawableHelper
                .createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius,
                        ContextCompat.getColor(getContext(), R.color.app_color_theme_1));
        // 其中一张重新设置颜色
        QMUIDrawableHelper.setDrawableTintColor(tintColorBitmapDrawble,
                ContextCompat.getColor(getContext(), R.color.app_color_theme_7));
        mTintColorImageView.setImageDrawable(tintColorBitmapDrawble);
        mTintColorOriginImageView.setImageDrawable(tintColorOriginBitmapDrawble);

        // 创建带上分隔线或下分隔线的 Drawable
        LayerDrawable separatorLayerDrawable = QMUIDrawableHelper
                .createItemSeparatorBg(ContextCompat.getColor(getContext(), R.color.app_color_theme_7),
                        ContextCompat.getColor(getContext(), R.color.app_color_theme_6),
                        QMUIDisplayHelper.dp2px(getContext(), 2), true);
        QMUIViewHelper.setBackgroundKeepingPadding(mSeparatorView, separatorLayerDrawable);

        // 从一个 View 创建 Bitmap
        mCreateFromViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIDialog.CustomDialogBuilder dialogBuilder = new QMUIDialog.CustomDialogBuilder(getContext());
                dialogBuilder.setLayout(R.layout.drawablehelper_createfromview);
                final QMUIDialog dialog = dialogBuilder.setTitle(getStringById(R.string.qmui_51)).create();
                ImageView displayImageView = (ImageView) dialog.findViewById(R.id.createFromViewDisplay);
                if (displayImageView == null) {
                    return;
                }
                Bitmap createFromViewBitmap = QMUIDrawableHelper.createBitmapFromView(mRootView);
                displayImageView.setImageBitmap(createFromViewBitmap);

                displayImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }
}
