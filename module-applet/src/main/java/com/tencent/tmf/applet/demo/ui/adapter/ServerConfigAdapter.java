package com.tencent.tmf.applet.demo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.tmf.applet.demo.R;
import com.tencent.tmf.applet.demo.ui.entity.ServerConfigEntity;
import com.tencent.tmf.applet.demo.ui.holder.ServerConfigHolder;

import xiao.framework.adapter.XGCRecyclerViewAdapter;

public class ServerConfigAdapter extends XGCRecyclerViewAdapter<ServerConfigEntity, ServerConfigHolder> {

    public ServerConfigAdapter(Context context) {
        super(context);
    }

    @Override
    protected ServerConfigHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.applet_server_info_item, parent, false);
        return new ServerConfigHolder(context, this, parent, itemView, viewType);
    }

    @Override
    protected void setItemData(int position, ServerConfigHolder holder, ServerConfigEntity model, int viewType) {
        holder.setData(model);
    }
}
