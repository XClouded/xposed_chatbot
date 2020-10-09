package com.taobao.monitor.impl.trace;

import java.util.HashMap;
import java.util.Map;

public class DispatcherManager {
    private static Map<String, IDispatcher> nameServers = new HashMap();

    private DispatcherManager() {
    }

    public static void addDispatcher(String str, IDispatcher iDispatcher) {
        nameServers.put(str, iDispatcher);
    }

    public static IDispatcher getDispatcher(String str) {
        IDispatcher iDispatcher = nameServers.get(str);
        return iDispatcher == null ? EmptyDispatcher.NULL : iDispatcher;
    }

    public static boolean isEmpty(IDispatcher iDispatcher) {
        return iDispatcher == null || iDispatcher == EmptyDispatcher.NULL;
    }
}
