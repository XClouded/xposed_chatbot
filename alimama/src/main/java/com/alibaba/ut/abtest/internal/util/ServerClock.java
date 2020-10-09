package com.alibaba.ut.abtest.internal.util;

import com.alibaba.analytics.core.logbuilder.TimeStampAdjustMgr;

public final class ServerClock {
    private ServerClock() {
    }

    public static long now() {
        return TimeStampAdjustMgr.getInstance().getCurrentMils();
    }
}
