package com.taobao.monitor.impl.data.network;

import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.trace.NetworkStageDispatcher;
import com.taobao.network.lifecycle.IMtopLifecycle;
import com.taobao.network.lifecycle.INetworkLifecycle;
import java.util.Map;

public class NetworkLifecycleImpl implements INetworkLifecycle, IMtopLifecycle {
    private NetworkStageDispatcher stageDispatcher = null;

    public void onEvent(String str, String str2, Map<String, Object> map) {
    }

    public void onMtopCancel(String str, Map<String, Object> map) {
    }

    public void onMtopError(String str, Map<String, Object> map) {
    }

    public void onMtopEvent(String str, String str2, Map<String, Object> map) {
    }

    public void onMtopFinished(String str, Map<String, Object> map) {
    }

    public void onMtopRequest(String str, String str2, Map<String, Object> map) {
    }

    public void onValidRequest(String str, String str2, Map<String, Object> map) {
    }

    public NetworkLifecycleImpl() {
        init();
    }

    private void init() {
        IDispatcher dispatcher = DispatcherManager.getDispatcher(APMContext.NETWORK_STAGE_DISPATCHER);
        if (dispatcher instanceof NetworkStageDispatcher) {
            this.stageDispatcher = (NetworkStageDispatcher) dispatcher;
        }
    }

    public void onRequest(String str, String str2, Map<String, Object> map) {
        if (!DispatcherManager.isEmpty(this.stageDispatcher)) {
            this.stageDispatcher.dispatchNetworkStage(0);
        }
    }

    public void onError(String str, Map<String, Object> map) {
        if (!DispatcherManager.isEmpty(this.stageDispatcher)) {
            this.stageDispatcher.dispatchNetworkStage(2);
        }
    }

    public void onCancel(String str, Map<String, Object> map) {
        if (!DispatcherManager.isEmpty(this.stageDispatcher)) {
            this.stageDispatcher.dispatchNetworkStage(3);
        }
    }

    public void onFinished(String str, Map<String, Object> map) {
        if (!DispatcherManager.isEmpty(this.stageDispatcher)) {
            this.stageDispatcher.dispatchNetworkStage(1);
        }
    }
}
