package com.tencent.tmf.demo.qmui.manager;

import com.tencent.tmf.demo.qmui.R;
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
import com.tencent.tmf.demo.qmui.fragment.components.QDRadiusImageView2ScaleTypeFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDRadiusImageView2UsageFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDRadiusImageViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDRadiusImageViewScaleTypeFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDRadiusImageViewUsageFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDSpanTouchFixTextViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTabSegmentFixModeFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTabSegmentFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTabSegmentScrollableModeFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTabSegmentSpaceWeightFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDTipDialogFragment;
import com.tencent.tmf.demo.qmui.fragment.components.QDVerticalTextViewFragment;
import com.tencent.tmf.demo.qmui.fragment.components.qqface.QDQQFaceFragment;
import com.tencent.tmf.demo.qmui.fragment.components.qqface.QDQQFacePerformanceTestFragment;
import com.tencent.tmf.demo.qmui.fragment.components.qqface.QDQQFaceUsageFragment;
import com.tencent.tmf.demo.qmui.fragment.components.section.QDGridSectionLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.section.QDListSectionLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.section.QDListWithDecorationSectionLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.section.QDSectionLayoutFragment;
import com.tencent.tmf.demo.qmui.fragment.components.viewpager.QDFitSystemWindowViewPagerFragment;
import com.tencent.tmf.demo.qmui.fragment.components.viewpager.QDLoopViewPagerFragment;
import com.tencent.tmf.demo.qmui.fragment.components.viewpager.QDViewPagerFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDAnimationListViewFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDArchTestFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScroll1Fragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScroll2Fragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScroll3Fragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScroll4Fragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScroll5Fragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScroll6Fragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScroll7Fragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScroll8Fragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDContinuousNestedScrollFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDSnapHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDSwipeDeleteListViewFragment;
import com.tencent.tmf.demo.qmui.fragment.lab.QDWebViewFixFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDColorHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDDeviceHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDDrawableHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDNotchHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDSpanFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDStatusBarHelperFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDViewHelperAnimationFadeFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDViewHelperAnimationSlideFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDViewHelperBackgroundAnimationBlinkFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDViewHelperBackgroundAnimationFullFragment;
import com.tencent.tmf.demo.qmui.fragment.util.QDViewHelperFragment;
import com.tencent.tmf.demo.qmui.model.QDItemDescription;
import java.util.HashMap;
import java.util.Map;
import com.tencent.tmf.demo.qmui.UIContextHolder;
/**
 * Created by winnieyzhou on 2019/4/2.
 */
class QDWidgetContainer {

    private static QDWidgetContainer sInstance = new QDWidgetContainer();

    private Map<Class<? extends BaseFragment>, QDItemDescription> mWidgets;

    private QDWidgetContainer() {
        mWidgets = new HashMap<>();
        mWidgets.put(QDBottomSheetFragment.class,
                new QDItemDescription(QDBottomSheetFragment.class, "QMUIBottomSheet", R.mipmap.icon_grid_botton_sheet,
                        ""));
        mWidgets.put(QDButtonFragment.class,
                new QDItemDescription(QDButtonFragment.class, "RoundButton", R.mipmap.icon_grid_button, ""));
        mWidgets.put(QDCollapsingTopBarLayoutFragment.class,
                new QDItemDescription(QDCollapsingTopBarLayoutFragment.class, "QMUICollapsingTopBarLayout",
                        R.mipmap.icon_grid_collapse_top_bar, ""));
        mWidgets.put(QDDialogFragment.class,
                new QDItemDescription(QDDialogFragment.class, "QMUIDialog", R.mipmap.icon_grid_dialog, ""));
        mWidgets.put(QDEmptyViewFragment.class,
                new QDItemDescription(QDEmptyViewFragment.class, "QMUIEmptyView", R.mipmap.icon_grid_empty_view, ""));
        mWidgets.put(QDFloatLayoutFragment.class,
                new QDItemDescription(QDFloatLayoutFragment.class, "QMUIFloatLayout", R.mipmap.icon_grid_float_layout,
                        ""));
        mWidgets.put(QDGroupListViewFragment.class,
                new QDItemDescription(QDGroupListViewFragment.class, "QMUIGroupListView",
                        R.mipmap.icon_grid_group_list_view, ""));
        mWidgets.put(QDLayoutFragment.class,
                new QDItemDescription(QDLayoutFragment.class, "QMUILayout", R.mipmap.icon_grid_layout, ""));
        mWidgets.put(QDLinkTextViewFragment.class,
                new QDItemDescription(QDLinkTextViewFragment.class, "QMUILinkTextView",
                        R.mipmap.icon_grid_link_text_view, ""));
        mWidgets.put(QDPopupFragment.class,
                new QDItemDescription(QDPopupFragment.class, "QMUIPopup", R.mipmap.icon_grid_popup, ""));
        mWidgets.put(QDPriorityLinearLayoutFragment.class,
                new QDItemDescription(QDPriorityLinearLayoutFragment.class, "QMUIPriorityLinearLayout",
                        R.mipmap.icon_grid_float_layout, ""));
        mWidgets.put(QDProgressBarFragment.class,
                new QDItemDescription(QDProgressBarFragment.class, "QMUIProgressBar", R.mipmap.icon_grid_progress_bar,
                        ""));
        mWidgets.put(QDPullRefreshFragment.class,
                new QDItemDescription(QDPullRefreshFragment.class, "QMUIPullRefreshLayout",
                        R.mipmap.icon_grid_pull_refresh_layout, ""));
        mWidgets.put(QDRadiusImageView2ScaleTypeFragment.class,
                new QDItemDescription(QDRadiusImageView2ScaleTypeFragment.class, "QMUIRadiusImageView2 ScaleType", 0,
                        ""));
        mWidgets.put(QDRadiusImageView2UsageFragment.class,
                new QDItemDescription(QDRadiusImageView2UsageFragment.class, "QMUIRadiusImageView2 usage", 0, ""));
        mWidgets.put(QDRadiusImageViewFragment.class,
                new QDItemDescription(QDRadiusImageViewFragment.class, "QMUIRadiusImageView",
                        R.mipmap.icon_grid_radius_image_view, ""));
        mWidgets.put(QDRadiusImageViewScaleTypeFragment.class,
                new QDItemDescription(QDRadiusImageViewScaleTypeFragment.class, "QMUIRadiusImageView ScaleType", 0,
                        ""));
        mWidgets.put(QDRadiusImageViewUsageFragment.class,
                new QDItemDescription(QDRadiusImageViewUsageFragment.class, "QMUIRadiusImageView usage", 0, ""));
        mWidgets.put(QDSpanTouchFixTextViewFragment.class,
                new QDItemDescription(QDSpanTouchFixTextViewFragment.class, "QMUISpanTouchFixTextView",
                        R.mipmap.icon_grid_span_touch_fix_text_view, ""));
        mWidgets.put(QDTabSegmentFixModeFragment.class,
                new QDItemDescription(QDTabSegmentFixModeFragment.class, getStringById(R.string.qmui_item_62), 0, ""));
        mWidgets.put(QDTabSegmentFragment.class,
                new QDItemDescription(QDTabSegmentFragment.class, "QMUITabSegment", R.mipmap.icon_grid_tab_segment,
                        ""));
        mWidgets.put(QDTabSegmentScrollableModeFragment.class,
                new QDItemDescription(QDTabSegmentScrollableModeFragment.class, getStringById(R.string.qmui_item_63), 0, ""));
        mWidgets.put(QDTabSegmentSpaceWeightFragment.class,
                new QDItemDescription(QDTabSegmentSpaceWeightFragment.class, getStringById(R.string.qmui_item_64), 0, ""));
        mWidgets.put(QDTipDialogFragment.class,
                new QDItemDescription(QDTipDialogFragment.class, "QMUITipDialog", R.mipmap.icon_grid_tip_dialog, ""));
        mWidgets.put(QDVerticalTextViewFragment.class,
                new QDItemDescription(QDVerticalTextViewFragment.class, "QMUIVerticalTextView",
                        R.mipmap.icon_grid_vertical_text_view, ""));
        mWidgets.put(QDQQFaceFragment.class,
                new QDItemDescription(QDQQFaceFragment.class, "QMUIQQFaceView", R.mipmap.icon_grid_qq_face_view, ""));
        mWidgets.put(QDQQFacePerformanceTestFragment.class,
                new QDItemDescription(QDQQFacePerformanceTestFragment.class, getStringById(R.string.qmui_item_65), 0, ""));
        mWidgets.put(QDQQFaceUsageFragment.class,
                new QDItemDescription(QDQQFaceUsageFragment.class, getStringById(R.string.qmui_item_66), 0, ""));
        mWidgets.put(QDGridSectionLayoutFragment.class,
                new QDItemDescription(QDGridSectionLayoutFragment.class, "Sticky Section for Grid", 0, ""));
        mWidgets.put(QDListSectionLayoutFragment.class,
                new QDItemDescription(QDListSectionLayoutFragment.class, "Sticky Section for List", 0, ""));
        mWidgets.put(QDListWithDecorationSectionLayoutFragment.class,
                new QDItemDescription(QDListWithDecorationSectionLayoutFragment.class,
                        "Sticky Section for List(With Decoration)", 0, ""));
        mWidgets.put(QDSectionLayoutFragment.class,
                new QDItemDescription(QDSectionLayoutFragment.class, "QMUIStickySectionLayout",
                        R.mipmap.icon_grid_sticky_section,
                        "https://github.com/Tencent/QMUI_Android/wiki/QMUIStickySectionLayout"));
        mWidgets.put(QDFitSystemWindowViewPagerFragment.class,
                new QDItemDescription(QDFitSystemWindowViewPagerFragment.class, "QDFitSystemWindowViewPagerFragment", 0,
                        ""));
        mWidgets.put(QDLoopViewPagerFragment.class,
                new QDItemDescription(QDLoopViewPagerFragment.class, "QDLoopViewPagerFragment", 0, ""));
        mWidgets.put(QDViewPagerFragment.class, new QDItemDescription(QDViewPagerFragment.class, "QMUIViewPager",
                R.mipmap.icon_grid_pager_layout_manager, ""));
        mWidgets.put(QDAnimationListViewFragment.class,
                new QDItemDescription(QDAnimationListViewFragment.class, "QMUIAnimationListView",
                        R.mipmap.icon_grid_anim_list_view, ""));
        mWidgets.put(QDArchTestFragment.class,
                new QDItemDescription(QDArchTestFragment.class, "QMUIFragment", R.mipmap.icon_grid_layout, ""));
        mWidgets.put(QDContinuousNestedScroll1Fragment.class,
                new QDItemDescription(QDContinuousNestedScroll1Fragment.class, "webview + recyclerview", 0, ""));
        mWidgets.put(QDContinuousNestedScroll2Fragment.class,
                new QDItemDescription(QDContinuousNestedScroll2Fragment.class,
                        "webview + part sticky header + viewpager", 0, ""));
        mWidgets.put(QDContinuousNestedScroll3Fragment.class,
                new QDItemDescription(QDContinuousNestedScroll3Fragment.class, "recyclerview + recyclerview", 0, ""));
        mWidgets.put(QDContinuousNestedScroll4Fragment.class,
                new QDItemDescription(QDContinuousNestedScroll4Fragment.class,
                        "(header + recyclerview + bottom) + recyclerview", 0, ""));
        mWidgets.put(QDContinuousNestedScroll5Fragment.class,
                new QDItemDescription(QDContinuousNestedScroll5Fragment.class,
                        "(header + webview + bottom) + recyclerview", 0, ""));
        mWidgets.put(QDContinuousNestedScroll6Fragment.class,
                new QDItemDescription(QDContinuousNestedScroll6Fragment.class, "linearLayout + recyclerview", 0, ""));
        mWidgets.put(QDContinuousNestedScroll7Fragment.class,
                new QDItemDescription(QDContinuousNestedScroll7Fragment.class,
                        "(header + webview + bottom) + (part sticky header + viewpager)", 0, ""));
        mWidgets.put(QDContinuousNestedScroll8Fragment.class,
                new QDItemDescription(QDContinuousNestedScroll8Fragment.class,
                        "(header + recyclerView + bottom) + (part sticky header + viewpager)", 0, ""));
        mWidgets.put(QDContinuousNestedScrollFragment.class,
                new QDItemDescription(QDContinuousNestedScrollFragment.class, "QMUIContinuousNestedScrollLayout",
                        R.mipmap.icon_grid_continuous_nest_scroll,
                        "https://github.com/Tencent/QMUI_Android/wiki/QMUIContinuousNestedScrollLayout"));

        mWidgets.put(QDSnapHelperFragment.class,
                new QDItemDescription(QDSnapHelperFragment.class, getStringById(R.string.qmui_item_67),
                        R.mipmap.icon_grid_pager_layout_manager, ""));
        mWidgets.put(QDSwipeDeleteListViewFragment.class,
                new QDItemDescription(QDSwipeDeleteListViewFragment.class, getStringById(R.string.qmui_item_68), 0, ""));
        mWidgets.put(QDWebViewFixFragment.class,
                new QDItemDescription(QDWebViewFixFragment.class, "QMUIWebView", R.mipmap.icon_grid_webview, ""));
        mWidgets.put(QDColorHelperFragment.class,
                new QDItemDescription(QDColorHelperFragment.class, "QMUIColorHelper", R.mipmap.icon_grid_color_helper,
                        ""));
        mWidgets.put(QDDeviceHelperFragment.class,
                new QDItemDescription(QDDeviceHelperFragment.class, "QMUIDeviceHelper",
                        R.mipmap.icon_grid_device_helper, ""));
        mWidgets.put(QDDrawableHelperFragment.class,
                new QDItemDescription(QDDrawableHelperFragment.class, "QMUIDrawableHelper",
                        R.mipmap.icon_grid_drawable_helper, ""));
        mWidgets.put(QDNotchHelperFragment.class, new QDItemDescription(QDNotchHelperFragment.class, "QMUINotchHelper",
                R.mipmap.icon_grid_status_bar_helper, ""));
        mWidgets.put(QDSpanFragment.class,
                new QDItemDescription(QDSpanFragment.class, "Span", R.mipmap.icon_grid_span, ""));
        mWidgets.put(QDStatusBarHelperFragment.class,
                new QDItemDescription(QDStatusBarHelperFragment.class, "QMUIStatusBarHelper",
                        R.mipmap.icon_grid_status_bar_helper, ""));
        mWidgets.put(QDViewHelperAnimationFadeFragment.class,
                new QDItemDescription(QDViewHelperAnimationFadeFragment.class, getStringById(R.string.qmui_item_69), 0, ""));
        mWidgets.put(QDViewHelperAnimationSlideFragment.class,
                new QDItemDescription(QDViewHelperAnimationSlideFragment.class, getStringById(R.string.qmui_item_70), 0, ""));
        mWidgets.put(QDViewHelperBackgroundAnimationBlinkFragment.class,
                new QDItemDescription(QDViewHelperBackgroundAnimationBlinkFragment.class, getStringById(R.string.qmui_item_71), 0, ""));
        mWidgets.put(QDViewHelperBackgroundAnimationFullFragment.class,
                new QDItemDescription(QDViewHelperBackgroundAnimationFullFragment.class, getStringById(R.string.qmui_item_72), 0, ""));
        mWidgets.put(QDViewHelperFragment.class,
                new QDItemDescription(QDViewHelperFragment.class, "QMUIViewHelper", R.mipmap.icon_grid_view_helper,
                        ""));
    }

    private String getStringById(int id){
        return UIContextHolder.sContext.getResources().getString(id);
    }

    public static QDWidgetContainer getInstance() {
        return sInstance;
    }

    public QDItemDescription get(Class<? extends BaseFragment> fragment) {
        return mWidgets.get(fragment);
    }
}
