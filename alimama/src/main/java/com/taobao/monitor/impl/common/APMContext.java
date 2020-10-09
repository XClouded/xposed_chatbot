package com.taobao.monitor.impl.common;

import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.IDispatcher;

public class APMContext {
    public static final String ACTIVITY_EVENT_DISPATCHER = "ACTIVITY_EVENT_DISPATCHER";
    public static final String ACTIVITY_FPS_DISPATCHER = "ACTIVITY_FPS_DISPATCHER";
    public static final String ACTIVITY_IMAGE_DISPATCHER = "ACTIVITY_IMAGE_DISPATCHER";
    public static final String ACTIVITY_LIFECYCLE_DISPATCHER = "ACTIVITY_LIFECYCLE_DISPATCHER";
    public static final String ACTIVITY_NETWORK_DISPATCHER = "ACTIVITY_NETWORK_DISPATCHER";
    public static final String ACTIVITY_USABLE_VISIBLE_DISPATCHER = "ACTIVITY_USABLE_VISIBLE_DISPATCHER";
    public static final String APPLICATION_BACKGROUND_CHANGED_DISPATCHER = "APPLICATION_BACKGROUND_CHANGED_DISPATCHER";
    public static final String APPLICATION_GC_DISPATCHER = "APPLICATION_GC_DISPATCHER";
    public static final String APPLICATION_LOW_MEMORY_DISPATCHER = "APPLICATION_LOW_MEMORY_DISPATCHER";
    public static final String FRAGMENT_LIFECYCLE_DISPATCHER = "FRAGMENT_LIFECYCLE_DISPATCHER";
    public static final String FRAGMENT_USABLE_VISIBLE_DISPATCHER = "FRAGMENT_USABLE_VISIBLE_DISPATCHER";
    public static final String IMAGE_STAGE_DISPATCHER = "IMAGE_STAGE_DISPATCHER";
    public static final String NETWORK_STAGE_DISPATCHER = "NETWORK_STAGE_DISPATCHER";

    private APMContext() {
    }

    public static APMContext instance() {
        return Holder.INSTANCE;
    }

    public static IDispatcher getDispatcher(String str) {
        return DispatcherManager.getDispatcher(str);
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static final APMContext INSTANCE = new APMContext();

        private Holder() {
        }
    }
}
