package com.taobao.monitor.performance;

public interface IApmAdapterFactory {
    IWXApmAdapter createApmAdapter();

    IWXApmAdapter createApmAdapterByType(String str);
}
