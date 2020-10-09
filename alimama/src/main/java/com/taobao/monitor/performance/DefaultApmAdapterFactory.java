package com.taobao.monitor.performance;

public class DefaultApmAdapterFactory implements IApmAdapterFactory {
    public IWXApmAdapter createApmAdapter() {
        return new DefaultWXApmAdapter();
    }

    public IWXApmAdapter createApmAdapterByType(String str) {
        return createApmAdapter();
    }
}
