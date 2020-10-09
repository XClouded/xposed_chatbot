package com.ut.mini.internal;

import com.alibaba.analytics.utils.StringUtils;
import com.ut.mini.UTHitBuilders;
import java.util.Map;

public class UTOriginalCustomHitBuilder extends UTHitBuilders.UTHitBuilder {
    public UTOriginalCustomHitBuilder(String str, int i, String str2, String str3, String str4, Map<String, String> map) {
        if (!StringUtils.isEmpty(str)) {
            super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_PAGE, str);
        }
        super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_EVENT_ID, "" + i);
        if (!StringUtils.isEmpty(str2)) {
            super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG1, str2);
        }
        if (!StringUtils.isEmpty(str3)) {
            super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, str3);
        }
        if (!StringUtils.isEmpty(str4)) {
            super.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG3, str4);
        }
        super.setProperties(map);
    }
}
