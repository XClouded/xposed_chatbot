package com.taobao.application.common;

import com.taobao.application.common.impl.ApmImpl;

public class ApmHelper {
    public static void inject() {
        ApmManager.setApmDelegate(ApmImpl.instance());
    }
}
