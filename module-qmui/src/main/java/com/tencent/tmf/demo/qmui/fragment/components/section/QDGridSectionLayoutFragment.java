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

package com.tencent.tmf.demo.qmui.fragment.components.section;

import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.section.QMUIStickySectionAdapter;
import com.tencent.tmf.demo.qmui.model.SectionHeader;
import com.tencent.tmf.demo.qmui.model.SectionItem;

public class QDGridSectionLayoutFragment extends QDBaseSectionLayoutFragment {

    @Override
    protected QMUIStickySectionAdapter<SectionHeader, SectionItem,
            QMUIStickySectionAdapter.ViewHolder> createAdapter() {
        return new QDGridSectionAdapter();
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mAdapter.getItemIndex(i) < 0 ? layoutManager.getSpanCount() : 1;
            }
        });
        return layoutManager;
    }

    @Override
    protected void initStickyLayout() {
        super.initStickyLayout();
        mSectionLayout.getRecyclerView().addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                    @NonNull RecyclerView.State state) {
                if (view instanceof TextView) {
                    int margin = QMUIDisplayHelper.dp2px(getContext(), 10);
                    outRect.set(margin, margin, margin, margin);
                } else {
                    outRect.set(0, 0, 0, 0);
                }
            }
        });
    }
}
