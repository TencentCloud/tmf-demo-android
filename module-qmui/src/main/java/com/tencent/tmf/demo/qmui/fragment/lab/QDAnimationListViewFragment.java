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

package com.tencent.tmf.demo.qmui.fragment.lab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.adaptor.QDSimpleAdapter;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.manager.QDDataManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cginechen
 * @date 2017-03-30
 */

public class QDAnimationListViewFragment extends BaseFragment {

    QMUITopBar mTopBar;
    QMUIAnimationListView mListView;

    private List<String> mData = new ArrayList<>();

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_animation_listview, null);
        mTopBar = root.findViewById(R.id.topbar);
        mListView = root.findViewById(R.id.listview);
        initTopBar();
        initListView();
        return root;
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(QDDataManager.getInstance().getName(this.getClass()));
        mTopBar.addRightTextButton(getStringById(R.string.qmui_0), QMUIViewHelper.generateViewId())
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListView.manipulate(new QMUIAnimationListView.Manipulator<MyAdapter>() {
                            @Override
                            public void manipulate(MyAdapter adapter) {
                                int position = mListView.getFirstVisiblePosition();
                                long current = System.currentTimeMillis();
                                mData.add(position + 1, "item add" + (current + 1));
                                mData.add(position + 2, "item add" + (current + 2));
                                mData.add(position + 3, "item add" + (current + 3));
                            }
                        });

                    }
                });
        mTopBar.addRightTextButton(getStringById(R.string.qmui_1), QMUIViewHelper.generateViewId())
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListView.manipulate(new QMUIAnimationListView.Manipulator<MyAdapter>() {
                            @Override
                            public void manipulate(MyAdapter adapter) {
                                int position = mListView.getFirstVisiblePosition();
                                if (mData.size() > position + 4) {
                                    mData.remove(position + 1);
                                    mData.remove(position + 3);
                                } else {
                                    Toast.makeText(getContext(), getStringById(R.string.qmui_2), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
    }

    private void initListView() {
        for (int i = 0; i < 20; i++) {
            mData.add("item " + (i + 1));
        }
        MyAdapter adapter = new MyAdapter(getContext(), mData);
        mListView.setAdapter(adapter);
    }


    private static class MyAdapter extends QDSimpleAdapter {

        public MyAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
