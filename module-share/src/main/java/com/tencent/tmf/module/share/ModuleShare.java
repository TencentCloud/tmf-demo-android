package com.tencent.tmf.module.share;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.tmf.portal.annotations.SingleModule;
import com.tencent.tmf.portal.Module;
import com.tencent.tmf.portal.ServiceFactory;
import com.tencent.tmf.common.Constant;
import com.tencent.tmf.common.gen.ModuleBaseConst;
import com.tencent.tmf.share.api.ITMFShareKeys;
import com.tencent.tmf.share.api.TMFShareService;
import com.tencent.tmf.common.BuildConfig;
import java.util.ArrayList;
import java.util.List;

@SingleModule(name = "module-share")
public class ModuleShare implements Module {

    @Override
    public void attach(Context context, Bundle bundle) {
        Log.d(Constant.TAG, "ModuleShare attach");
        
        initShare();
        
    }

    private void initShare() {

        ITMFShareKeys itmfShareKeys = new ITMFShareKeys() {
            @Override
            public String getWXAppId() {
                return BuildConfig.SHARE_WX_APP_ID;
            }

            @Override
            public String getQQAppId() {
                return BuildConfig.SHARE_QQ_APP_ID;
            }

            @Override
            public String getSinaWeiboAppId() {
                return BuildConfig.SHARE_WEIBO_APP_ID;
            }

            @Override
            public String getSinaWeiboRedirectUrl() {
                return BuildConfig.SHARE_WEIBO_REDIRECT_URL;
            }

            @Override
            public String getSinaWeiboScope() {
                return BuildConfig.SHARE_WEIBO_SCOPE;
            }

            @Override
            public String getAlipayAppId() {
                return BuildConfig.SHARE_ALIPAY_APP_ID;
            }

            @Override
            public String getDingDingAppId() {
                return BuildConfig.SHARE_DINGDING_APP_ID;
            }

            @Override
            public String getCwxAppId() {
                return BuildConfig.SHARE_CWX_APP_ID;
            }

            @Override
            public String getCwxAppAgentId() {
                return BuildConfig.SHARE_CWX_APP_AGENT_ID;
            }

            @Override
            public String getCwxSchema() {
                return BuildConfig.SHARE_CWX_SCHEMA;
            }
        };
        TMFShareService.init(itmfShareKeys);
    }

    @Override
    public void detach() {

    }

    @Override
    public List<Class<?>> supportedServices() {
        return new ArrayList<>();
    }

    @Override
    public ServiceFactory serviceFactory() {
        return null;
    }

    @Override
    public List<String> dependsOn() {
        List<String> depends = new ArrayList<>();
        depends.add(ModuleBaseConst.MODULE_ID);
        return depends;
    }

}
