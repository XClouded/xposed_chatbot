package com.taobao.monitor.impl.data.gc;

import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.data.IDataExecutor;
import com.taobao.monitor.impl.trace.DispatcherManager;

public class GCCollector implements IDataExecutor {
    public void execute() {
        GCSwitcher gCSwitcher = new GCSwitcher();
        DispatcherManager.getDispatcher(APMContext.APPLICATION_GC_DISPATCHER).addListener(gCSwitcher);
        gCSwitcher.open();
    }
}
