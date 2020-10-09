package com.alibaba.android.common;

import android.content.Context;

public interface ServiceProxy {
    public static final String COMMON_SERVICE_LOGGER = "common_logger";
    public static final String COMMON_SERVICE_THREAD_POOL = "common_thread_pool";

    Context getApplicationContext();

    ServiceProxy getParent();

    Object getService(String str);

    void setApplicationContext(Context context);
}
