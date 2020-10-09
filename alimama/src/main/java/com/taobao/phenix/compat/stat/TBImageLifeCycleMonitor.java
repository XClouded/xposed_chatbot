package com.taobao.phenix.compat.stat;

import com.taobao.phenix.lifecycle.IPhenixLifeCycle;
import com.taobao.phenix.lifecycle.PhenixLifeCycleManager;
import java.util.Map;

public class TBImageLifeCycleMonitor implements IPhenixLifeCycle {

    private static class Holder {
        /* access modifiers changed from: private */
        public static final TBImageLifeCycleMonitor INSTANCE = new TBImageLifeCycleMonitor();

        private Holder() {
        }
    }

    public static TBImageLifeCycleMonitor instance() {
        return Holder.INSTANCE;
    }

    public void onRequest(String str, String str2, Map<String, Object> map) {
        PhenixLifeCycleManager.instance().onRequest(str, str2, map);
    }

    public void onError(String str, String str2, Map<String, Object> map) {
        PhenixLifeCycleManager.instance().onError(str, str2, map);
    }

    public void onCancel(String str, String str2, Map<String, Object> map) {
        PhenixLifeCycleManager.instance().onCancel(str, str2, map);
    }

    public void onFinished(String str, String str2, Map<String, Object> map) {
        PhenixLifeCycleManager.instance().onFinished(str, str2, map);
    }

    public void onEvent(String str, String str2, Map<String, Object> map) {
        PhenixLifeCycleManager.instance().onEvent(str, str2, map);
    }
}
