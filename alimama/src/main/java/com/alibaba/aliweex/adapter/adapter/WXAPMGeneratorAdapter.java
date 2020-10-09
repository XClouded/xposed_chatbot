package com.alibaba.aliweex.adapter.adapter;

import com.taobao.weex.performance.IApmGenerator;
import com.taobao.weex.performance.IWXApmMonitorAdapter;

public class WXAPMGeneratorAdapter implements IApmGenerator {
    public IWXApmMonitorAdapter generateApmInstance(String str) {
        return new WXAPMAdapter();
    }
}
