package com.taobao.monitor.impl.util;

import com.taobao.monitor.impl.common.Global;

public class TopicUtils {
    public static String getFullTopic(String str) {
        return Global.instance().getNamespace() + str;
    }
}
