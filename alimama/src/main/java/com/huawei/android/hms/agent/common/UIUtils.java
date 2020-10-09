package com.huawei.android.hms.agent.common;

import android.app.Activity;

public final class UIUtils {
    public static boolean isActivityFullscreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags & 1024) == 1024;
    }
}
