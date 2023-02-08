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

package com.tencent.tmf.demo.qmui.fragment;

import static com.tencent.tmf.demo.qmui.fragment.QDWebExplorerFragment.EXTRA_TITLE;
import static com.tencent.tmf.demo.qmui.fragment.QDWebExplorerFragment.EXTRA_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIPackageHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.tencent.tmf.demo.qmui.R;
import com.tencent.tmf.demo.qmui.base.BaseFragment;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * 关于界面
 * <p>
 * Created by Kayo on 2016/11/18.
 */
public class QDAboutFragment extends BaseFragment {

    QMUITopBarLayout mTopBar;
    TextView mVersionTextView;
    QMUIGroupListView mAboutGroupListView;
    TextView mCopyrightTextView;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_about, null);
        mTopBar = root.findViewById(R.id.topbar);
        mVersionTextView = root.findViewById(R.id.version);
        mAboutGroupListView = root.findViewById(R.id.about_list);
        mCopyrightTextView = root.findViewById(R.id.copyright);

        initTopBar();

        mVersionTextView.setText(QMUIPackageHelper.getAppVersion(getContext()));

        QMUIGroupListView.newSection(getContext())
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_homepage)),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://qmuiteam.com/android";
                                Bundle bundle = new Bundle();
                                bundle.putString(EXTRA_URL, url);
                                bundle.putString(EXTRA_TITLE, getResources().getString(R.string.about_item_homepage));
                                QMUIFragment fragment = new QDWebExplorerFragment();
                                fragment.setArguments(bundle);
                                startFragment(fragment);
                            }
                        })
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_github)),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://github.com/Tencent/QMUI_Android";
                                Bundle bundle = new Bundle();
                                bundle.putString(EXTRA_URL, url);
                                bundle.putString(EXTRA_TITLE, getResources().getString(R.string.about_item_github));
                                QMUIFragment fragment = new QDWebExplorerFragment();
                                fragment.setArguments(bundle);
                                startFragment(fragment);
                            }
                        })
                .addTo(mAboutGroupListView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String currentYear = dateFormat.format(new java.util.Date());
        mCopyrightTextView.setText(String.format(getResources().getString(R.string.about_copyright), currentYear));

        return root;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(getResources().getString(R.string.about_title));
    }

    @Override
    public QMUIFragment.TransitionConfig onFetchTransitionConfig() {
        return SCALE_TRANSITION_CONFIG;
    }
}
