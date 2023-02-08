package com.tencent.tmf.module.portalDynamic;

import com.tencent.tmf.common.service.IModulePortalDynamicService;

/**
 * Created by Xiaomao Yi on 2022/5/31.
 */
public class PortalDynamicServiceImpl implements IModulePortalDynamicService {

    @Override
    public String getMessage() {
        return "Hello from dynamic module";
    }
}
