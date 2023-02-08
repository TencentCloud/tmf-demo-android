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

package com.tencent.tmf.demo.qmui.manager;


import com.tencent.tmf.demo.qmui.base.BaseFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDBottomSheetFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDButtonFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDCollapsingTopBarLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDDialogFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDEmptyViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDFloatLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDGroupListViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDLinkTextViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDPopupFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDPriorityLinearLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDProgressBarFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDPullRefreshFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDRadiusImageViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDSpanTouchFixTextViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTabSegmentFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTipDialogFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDVerticalTextViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.qqface.QDQQFaceFragment;
import com.tencent.tmf.demo.qmui.fragment.components.section.QDSectionLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.viewpager.QDViewPagerFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDAnimationListViewFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDArchTestFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScrollFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDSnapHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDWebViewFixFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDColorHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDDeviceHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDDrawableHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDNotchHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDSpanFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDStatusBarHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDViewHelperFragment;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * QDDataManager
 * @author cginechen
 * @date 2016-10-21
 */

public class QDDataManager {
    private static QDDataManager _sInstance;
    private QDWidgetContainer mWidgetContainer;

    private List<Class<? extends BaseFragment>> mComponentsNames;
    private List<Class<? extends BaseFragment>> mUtilNames;
    private List<Class<? extends BaseFragment>> mLabNames;

    public QDDataManager() {
        mWidgetContainer = QDWidgetContainer.getInstance();
        initComponentsDesc();
        initUtilDesc();
        initLabDesc();
    }

    public static QDDataManager getInstance() {
        if (_sInstance == null) {
            _sInstance = new QDDataManager();
        }
        return _sInstance;
    }


    /**
     * Components
     */
    private void initComponentsDesc() {
        mComponentsNames = new ArrayList<>();
        mComponentsNames.add(QDButtonFragment.class);
        mComponentsNames.add(QDDialogFragment.class);
        mComponentsNames.add(QDFloatLayoutFragment.class);
        mComponentsNames.add(QDEmptyViewFragment.class);
        mComponentsNames.add(QDTabSegmentFragment.class);
        mComponentsNames.add(QDProgressBarFragment.class);
        mComponentsNames.add(QDBottomSheetFragment.class);
        mComponentsNames.add(QDGroupListViewFragment.class);
        mComponentsNames.add(QDTipDialogFragment.class);
        mComponentsNames.add(QDRadiusImageViewFragment.class);
        mComponentsNames.add(QDVerticalTextViewFragment.class);
        mComponentsNames.add(QDPullRefreshFragment.class);
        mComponentsNames.add(QDPopupFragment.class);
        mComponentsNames.add(QDSpanTouchFixTextViewFragment.class);
        mComponentsNames.add(QDLinkTextViewFragment.class);
        mComponentsNames.add(QDQQFaceFragment.class);
        mComponentsNames.add(QDSpanFragment.class);
        mComponentsNames.add(QDCollapsingTopBarLayoutFragment.class);
        mComponentsNames.add(QDViewPagerFragment.class);
        mComponentsNames.add(QDLayoutFragment.class);
        mComponentsNames.add(QDPriorityLinearLayoutFragment.class);
        mComponentsNames.add(QDSectionLayoutFragment.class);
        mComponentsNames.add(QDContinuousNestedScrollFragment.class);
    }

    /**
     * Helper
     */
    private void initUtilDesc() {
        mUtilNames = new ArrayList<>();
        mUtilNames.add(QDColorHelperFragment.class);
        mUtilNames.add(QDDeviceHelperFragment.class);
        mUtilNames.add(QDDrawableHelperFragment.class);
        mUtilNames.add(QDStatusBarHelperFragment.class);
        mUtilNames.add(QDViewHelperFragment.class);
        mUtilNames.add(QDNotchHelperFragment.class);
    }

    /**
     * Lab
     */
    private void initLabDesc() {
        mLabNames = new ArrayList<>();
        mLabNames.add(QDAnimationListViewFragment.class);
        mLabNames.add(QDSnapHelperFragment.class);
        mLabNames.add(QDArchTestFragment.class);
        mLabNames.add(QDWebViewFixFragment.class);
    }

    public QDItemDescription getDescription(Class<? extends BaseFragment> cls) {
        return mWidgetContainer.get(cls);
    }

    public String getName(Class<? extends BaseFragment> cls) {
        QDItemDescription itemDescription = getDescription(cls);
        if (itemDescription == null) {
            return null;
        }
        return itemDescription.getName();
    }

    public String getDocUrl(Class<? extends BaseFragment> cls) {
        QDItemDescription itemDescription = getDescription(cls);
        if (itemDescription == null) {
            return null;
        }
        return itemDescription.getDocUrl();
    }

    public List<QDItemDescription> getComponentsDescriptions() {
        List<QDItemDescription> list = new ArrayList<>();
        for (int i = 0; i < mComponentsNames.size(); i++) {
            list.add(mWidgetContainer.get(mComponentsNames.get(i)));
        }
        return list;
    }

    public List<QDItemDescription> getUtilDescriptions() {
        List<QDItemDescription> list = new ArrayList<>();
        for (int i = 0; i < mUtilNames.size(); i++) {
            list.add(mWidgetContainer.get(mUtilNames.get(i)));
        }
        return list;
    }

    public List<QDItemDescription> getLabDescriptions() {
        List<QDItemDescription> list = new ArrayList<>();
        for (int i = 0; i < mLabNames.size(); i++) {
            list.add(mWidgetContainer.get(mLabNames.get(i)));
        }
        return list;
    }
}
