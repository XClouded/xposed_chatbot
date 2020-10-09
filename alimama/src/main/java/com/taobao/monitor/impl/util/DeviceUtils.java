package com.taobao.monitor.impl.util;

import com.taobao.monitor.impl.common.Global;

public class DeviceUtils {
    public static int dip2px(int i) {
        return (int) ((((float) i) * Global.instance().context().getResources().getDisplayMetrics().density) + 0.5f);
    }
}
