package com.taobao.monitor.impl.trace;

import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import com.ali.ha.fulltrace.dump.DumpManager;
import com.ali.ha.fulltrace.event.ReceiverLowMemoryEvent;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.logger.DataLoggerUtils;
import com.taobao.monitor.impl.trace.AbsDispatcher;

public class ApplicationLowMemoryDispatcher extends AbsDispatcher<LowMemoryListener> implements ComponentCallbacks {
    private static final String TAG = "ApplicationLowMemory";

    public interface LowMemoryListener {
        void onLowMemory();
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public ApplicationLowMemoryDispatcher() {
        Global.instance().context().registerComponentCallbacks(this);
    }

    public void dispatchOnLowMemory() {
        foreach(new AbsDispatcher.ListenerCaller<LowMemoryListener>() {
            public void callListener(LowMemoryListener lowMemoryListener) {
                lowMemoryListener.onLowMemory();
            }
        });
    }

    public void onLowMemory() {
        DataLoggerUtils.log(TAG, "onLowMemory");
        dispatchOnLowMemory();
        ReceiverLowMemoryEvent receiverLowMemoryEvent = new ReceiverLowMemoryEvent();
        receiverLowMemoryEvent.level = 1.0f;
        DumpManager.getInstance().append(receiverLowMemoryEvent);
    }
}
