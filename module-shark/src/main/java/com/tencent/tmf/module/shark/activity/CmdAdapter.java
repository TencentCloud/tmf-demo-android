package com.tencent.tmf.module.shark.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.tencent.tmf.module.shark.R;

import java.util.ArrayList;

public class CmdAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private ArrayList<PocCmdBean> dataList;

    public CmdAdapter(Context context, ArrayList<PocCmdBean> dataList) {
        this.ctx = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_cmd, null);
            viewHolder = new ViewHolder();
            viewHolder.apiName = convertView.findViewById(R.id.tv_api_name);
            viewHolder.apiDisc = convertView.findViewById(R.id.tv_api_desc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PocCmdBean pocCmdBean = dataList.get(position);

        viewHolder.apiName.setText(pocCmdBean.getApiName());
        viewHolder.apiDisc.setText(pocCmdBean.getApiDesc());
        return convertView;
    }

    class ViewHolder {
        TextView apiName;
        TextView apiDisc;
    }
}
