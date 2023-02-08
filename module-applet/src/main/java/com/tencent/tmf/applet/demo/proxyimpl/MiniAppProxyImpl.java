package com.tencent.tmf.applet.demo.proxyimpl;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.tencent.tmfmini.sdk.annotation.ProxyService;
import com.tencent.tmfmini.sdk.launcher.core.IMiniAppContext;
import com.tencent.tmfmini.sdk.launcher.core.proxy.AsyncResult;
import com.tencent.tmfmini.sdk.launcher.core.proxy.BaseMiniAppProxyImpl;
import com.tencent.tmfmini.sdk.launcher.core.proxy.MiniAppProxy;
import com.tencent.tmfmini.sdk.launcher.ui.MoreItem;
import com.tencent.tmfmini.sdk.launcher.ui.MoreItemList;
import com.tencent.tmfmini.sdk.launcher.ui.OnMoreItemSelectedListener;
import com.tencent.tmf.applet.demo.R;
import com.tencent.tmf.applet.demo.utils.UniversalDrawable;
import com.tencent.tmf.base.api.TMFBase;
import com.tencent.tmf.mini.api.bean.MiniAuthInfo;
import com.tencent.tmf.mini.api.callback.IAuthView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@ProxyService(proxy = MiniAppProxy.class)
public class MiniAppProxyImpl extends BaseMiniAppProxyImpl {

    private static final String TAG = "MiniAppProxyImpl";

    /**
     * 自定义授权弹窗view
     * @param context
     * @param authInfo
     * @param authView
     * @return true:自定义授权view;false:使用内置
     */
    @Override
    public boolean authView(Context context, MiniAuthInfo authInfo, IAuthView authView) {
        boolean isCustom = false;
        if(isCustom) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.mini_auth_view, null);
            //必须设置
            inflate.findViewById(R.id.mini_auth_btn_refuse).setOnClickListener(authInfo.refuseListener);
            //必须设置
            inflate.findViewById(R.id.mini_auth_btn_grant).setOnClickListener(authInfo.grantListener);

            //返回自定义View
            authView.getView(inflate);
        }

        return isCustom;
    }

    /**
     * 获取scope.userInfo授权用户信息
     *
     * @param appId
     * @param result
     */
    @Override
    public void getUserInfo(String appId, AsyncResult result) {
        JSONObject jsonObject = new JSONObject();
        try {
            //返回昵称
            jsonObject.put("nickName", "userInfo测试");
            //返回头像url
            jsonObject.put("avatarUrl", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.daimg.com%2Fuploads%2Fallimg%2F210114%2F1-210114151951.jpg&refer=http%3A%2F%2Fimg.daimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1673852149&t=e2a830d9fabd7e0818059d92c3883017");
            result.onReceiveResult(true, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接入方APP的版本信息
     */
    @Override
    public String getAppVersion() {
        return "8.2.0";
    }

    /**
     * 接入方APP的名称
     */
    @Override
    public String getAppName() {
        return "miniApp";
    }

    /**
     * 接入方APP是否是debug版本
     */
    @Override
    public boolean isDebugVersion() {
        return true;
    }

    /**
     * 获取Drawable对象
     *
     * @param context         上下文
     * @param source          来源，可以是本地或者网络
     * @param width           图片宽度
     * @param hight           图片高度
     * @param defaultDrawable 默认图片,用于加载中和加载失败
     */
    @Override
    public Drawable getDrawable(Context context, String source, int width, int hight, Drawable defaultDrawable) {
        //接入方接入自己的ImageLoader
        //demo里使用开源的universalimageloader
        UniversalDrawable drawable = new UniversalDrawable();
        if (TextUtils.isEmpty(source)) {
            return drawable;
        }
        drawable.loadImage(context, source);
        return drawable;
    }

    @Override
    public Drawable getDrawable(Context context, String source, int width, int hight, Drawable defaultDrawable,
                                boolean useApng) {
        return getDrawable(context, source, width, hight, defaultDrawable);
    }

    /**
     * 打开选图界面
     *
     * @param context        当前Activity
     * @param maxSelectedNum 允许选择的最大数量
     * @param listner        回调接口
     * @return 不支持该接口，请返回false
     */
    @Override
    public boolean openChoosePhotoActivity(Context context, int maxSelectedNum, IChoosePhotoListner listner) {
        return false;
    }

    /**
     * 打开图片预览界面
     *
     * @param context       当前Activity
     * @param selectedIndex 当前选择的图片索引
     * @param pathList      图片路径列表
     * @return 不支持该接口，请返回false
     */
    @Override
    public boolean openImagePreview(Context context, int selectedIndex, List<String> pathList) {
        return false;
    }


    /**
     * 点击胶囊按钮的更多选项
     *
     * @param miniAppContext 小程序运行环境
     * @return 不支持该接口，请返回false
     */
    @Override
    public boolean onCapsuleButtonMoreClick(IMiniAppContext miniAppContext) {
        return false;
    }

    /**
     * 点击胶囊按钮的关闭选项
     *
     * @param miniAppContext         小程序运行环境(小程序进程，非主进程)
     * @param onCloseClickedListener 点击小程序关闭时回调
     * @return 不支持该接口，请返回false
     */
    @Override
    public boolean onCapsuleButtonCloseClick(IMiniAppContext miniAppContext,
                                             DialogInterface.OnClickListener onCloseClickedListener) {
        return false;
    }

    /**
     * 返回胶囊更多面板的按钮，扩展按钮的ID需要设置为[100, 200]这个区间中的值，否则，添加无效
     */
    @Override
    public ArrayList<MoreItem> getMoreItems(MoreItemList.Builder builder) {
        MoreItem item1 = new MoreItem();
        item1.id = ShareProxyImpl.OTHER_MORE_ITEM_1;
//        item1.text = "其他1";
        item1.text = getString(R.string.applet_mini_proxy_impl_other1);
        item1.shareInMiniProcess = true;
        item1.drawable = R.mipmap.mini_demo_about;

        MoreItem item2 = new MoreItem();
        item2.id = ShareProxyImpl.OTHER_MORE_ITEM_2;
        item2.text = getString(R.string.applet_mini_proxy_impl_other2);
        item2.drawable = R.mipmap.mini_demo_about;

        MoreItem item3 = new MoreItem();
        item3.id = DemoMoreItemSelectedListener.CLOSE_MINI_APP;
        item3.text = getString(R.string.applet_mini_proxy_impl_float_app);
        item3.drawable = R.mipmap.mini_demo_about;

        MoreItem item4 = new MoreItem();
        item4.id = ShareProxyImpl.OTHER_MORE_ITEM_INVALID;
        item4.text = getString(R.string.applet_mini_proxy_impl_out_of_effect);
        item4.drawable = R.mipmap.mini_demo_about;

        // 自行调整顺序。
        builder.addMoreItem(item1)
                .addMoreItem(item2)
                .addShareQQ("QQ", R.mipmap.mini_demo_channel_qq)
                .addMoreItem(item3)
                .addShareQzone(getString(R.string.applet_mini_proxy_impl_Qzone),
                        R.mipmap.mini_demo_channel_qzone)
                .addShareWxFriends(getString(R.string.applet_mini_proxy_impl_wechat_friend),
                        R.mipmap.mini_demo_channel_wx_friend)
                .addShareWxMoments(getString(R.string.applet_mini_proxy_impl_wechat_group),
                        R.mipmap.mini_demo_channel_wx_moment)
                .addRestart(getString(R.string.applet_mini_proxy_impl_restart),
                        R.mipmap.mini_demo_restart_miniapp)
                .addAbout(getString(R.string.applet_mini_proxy_impl_about),
                        R.mipmap.mini_demo_about)
                .addDebug(getString(R.string.mini_sdk_more_item_debug),
                        R.mipmap.mini_demo_about)
                .addMonitor(getString(R.string.applet_mini_proxy_impl_performance),
                        R.mipmap.mini_demo_about)
                .addComplaint(getString(R.string.applet_mini_proxy_impl_complain_and_report),
                        R.mipmap.mini_demo_browser_report);

        return builder.build();
    }

    private String getString(int id) {
        return TMFBase.getContext().getString(id);
    }

    /**
     * 返回胶囊更多面板按钮点击监听器
     *
     * @return
     */
    @Override
    public OnMoreItemSelectedListener getMoreItemSelectedListener() {
        return new DemoMoreItemSelectedListener();
    }

    @Override
    public boolean uploadUserLog(String appId, String logPath) {
        Log.d("TMF_MINI", "uploadUserLog " + appId + " logPath " + logPath);
        return false;
    }
}


