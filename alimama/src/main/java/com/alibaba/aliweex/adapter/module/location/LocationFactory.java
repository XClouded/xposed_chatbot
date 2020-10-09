package com.alibaba.aliweex.adapter.module.location;

import com.taobao.weex.WXSDKInstance;

public class LocationFactory {
    public static ILocatable getLocationProvider(WXSDKInstance wXSDKInstance) {
        return new DefaultLocation(wXSDKInstance);
    }
}
