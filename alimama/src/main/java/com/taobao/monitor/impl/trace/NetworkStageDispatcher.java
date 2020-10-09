package com.taobao.monitor.impl.trace;

import com.taobao.monitor.impl.trace.AbsDispatcher;

public class NetworkStageDispatcher extends AbsDispatcher<INetworkStageListener> {

    public interface INetworkStageListener {
        void onNetworkStage(int i);
    }

    public void dispatchNetworkStage(final int i) {
        foreach(new AbsDispatcher.ListenerCaller<INetworkStageListener>() {
            public void callListener(INetworkStageListener iNetworkStageListener) {
                iNetworkStageListener.onNetworkStage(i);
            }
        });
    }
}
