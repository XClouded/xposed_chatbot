package com.alibaba.android.common;

import android.text.TextUtils;
import java.util.HashMap;

public class ServiceProxyFactory {
    private static final String COMMON_BASE_PROXY = "common_base_proxy";
    private static HashMap<String, ServiceProxy> proxyMap = new HashMap<>();

    private ServiceProxyFactory() {
    }

    public static synchronized void registerProxy(ServiceProxy serviceProxy) {
        synchronized (ServiceProxyFactory.class) {
            proxyMap.put(COMMON_BASE_PROXY, serviceProxy);
        }
    }

    public static synchronized void registerProxy(String str, ServiceProxy serviceProxy) {
        synchronized (ServiceProxyFactory.class) {
            if (!TextUtils.isEmpty(str)) {
                proxyMap.put(str, serviceProxy);
            }
        }
    }

    public static ServiceProxy getProxy() {
        return proxyMap.get(COMMON_BASE_PROXY);
    }

    public static ServiceProxy getProxy(String str) {
        ServiceProxy serviceProxy = proxyMap.get(str);
        return serviceProxy != null ? serviceProxy : getProxy();
    }
}
