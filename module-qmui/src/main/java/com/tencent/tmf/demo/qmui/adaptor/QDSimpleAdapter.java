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

package com.tencent.tmf.demo.qmui.adaptor;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.tencent.tmf.demo.qmui.R;
import java.util.List;

/**
 * QDSimpleAdapter
 * @author cginechen
 * @date 2017-03-30
 */

public class QDSimpleAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mData;

    public QDSimpleAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
    }

    public void setData(List<String> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView itemView;
        if (convertView == null) {
            itemView = new ItemView(mContext);
            itemView.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        } else {
            itemView = (ItemView) convertView;
        }
        itemView.setText(getItem(position));
        return itemView;
    }

    public void remove(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    static class ItemView extends FrameLayout {

        private TextView textView;

        public ItemView(Context context) {
            super(context);
            textView = new TextView(context);
            int paddingHor = QMUIDisplayHelper.dp2px(context, 12);
            int paddingVer = QMUIDisplayHelper.dp2px(context, 6);
            setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
            addView(textView, new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dp2px(context, 64)));
            int paddingTvHor = QMUIDisplayHelper.dp2px(context, 16);
            QMUIViewHelper.setBackgroundKeepingPadding(textView,
                    QMUIResHelper.getAttrDrawable(context, R.attr.qmui_s_list_item_bg_with_border_bottom));
            textView.setPadding(paddingTvHor, 0, paddingTvHor, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
        }

        public void setText(String text) {
            textView.setText(text);
        }
    }
}
