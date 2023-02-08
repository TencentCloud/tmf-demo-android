package com.tencent.tmf.module.portalDynamic;

import com.tencent.tmf.portal.ServiceFactory;

/**
 * Created by Xiaomao Yi on 2022/5/31.
 */
public class PortalServiceFactory implements ServiceFactory {

    @Override
    public <T> T create(Class<T> aClass) {
        if (aClass.isAssignableFrom(PortalDynamicServiceImpl.class)) {
            return (T) new PortalDynamicServiceImpl();
        }
        return null;
    }
}
