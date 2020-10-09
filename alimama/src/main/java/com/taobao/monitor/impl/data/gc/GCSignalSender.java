package com.taobao.monitor.impl.data.gc;

import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.logger.DataLoggerUtils;
import com.taobao.monitor.impl.trace.ApplicationGCDispatcher;
import com.taobao.monitor.impl.trace.IDispatcher;

class GCSignalSender {
    /* access modifiers changed from: private */
    public static InnerRunnable gcTask = new InnerRunnable();

    GCSignalSender() {
    }

    static void sendGCSignal() {
        Global.instance().handler().post(gcTask);
    }

    private static class InnerRunnable implements Runnable {
        private InnerRunnable() {
        }

        public void run() {
            Global.instance().handler().removeCallbacks(GCSignalSender.gcTask);
            IDispatcher dispatcher = APMContext.getDispatcher(APMContext.APPLICATION_GC_DISPATCHER);
            if (dispatcher instanceof ApplicationGCDispatcher) {
                ((ApplicationGCDispatcher) dispatcher).dispatchGC();
            }
            DataLoggerUtils.log("gc", new Object[0]);
        }
    }
}
