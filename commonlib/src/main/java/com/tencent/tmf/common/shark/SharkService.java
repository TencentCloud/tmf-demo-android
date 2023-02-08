package com.tencent.tmf.common.shark;

import com.tencent.tmf.base.api.TMFBase;
import com.tencent.tmf.shark.api.IShark;

/**
 * Shark服务封装
 */
public class SharkService {
    /**
     * 获取内置Shark实例
     */
    public static IShark getSharkWithInit() {
        return TMFBase.getShark();
    }
}
