package com.tencent.tmf.applet.demo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.tmf.applet.demo.R;
import com.tencent.tmf.applet.demo.ui.adapter.DebugAdapter.DebugHolder;
import com.tencent.tmf.applet.demo.ui.entity.DebugEntity;

import xiao.framework.adapter.XGCRecyclerViewAdapter;
import xiao.framework.adapter.viewholder.XGCRecyclerViewHolder;

public class DebugAdapter extends XGCRecyclerViewAdapter<DebugEntity, DebugHolder> {

    public DebugAdapter(Context context) {
        super(context);
    }

    @Override
    protected DebugHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.applet_debug_item, parent, false);
        return new DebugHolder(context, this, parent, itemView, viewType);
    }

    @Override
    protected void setItemData(int position, DebugHolder holder, DebugEntity model, int viewType) {
        holder.setData(model);
    }


    public static class DebugHolder extends XGCRecyclerViewHolder<DebugEntity, DebugAdapter> {

        private TextView mTitleText;
        private TextView mContentText;


        /**
         * 子类必须要实现
         *
         * @param context
         * @param adapter
         * @param parent
         * @param itemView
         * @param viewType
         */
        public DebugHolder(Context context, DebugAdapter adapter, ViewGroup parent, View itemView,
                int viewType) {
            super(context, adapter, parent, itemView, viewType);
        }

        @Override
        protected void initWidgets() {
            mTitleText = (TextView) findViewById(R.id.title_text);
            mContentText = (TextView) findViewById(R.id.content_text);
        }

        @Override
        public void setData(DebugEntity data) {
            if (data.type == DebugEntity.TYPE_1) {
                mTitleText.setVisibility(View.VISIBLE);
                mTitleText.setText(data.title);
                mContentText.setText(data.content);
                mContentText.setTextIsSelectable(true);
            } else if (data.type == DebugEntity.TYPE_2) {
                mTitleText.setVisibility(View.GONE);
                mContentText.setText(data.title);
                mContentText.setTextIsSelectable(false);
            }
        }
    }
}
