package com.taobao.monitor.impl.processor;

import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.processor.IProcessor;
import com.taobao.monitor.impl.trace.IDispatcher;

public abstract class AbsProcessor implements IProcessor {
    private APMContext apmContext;
    private volatile boolean isStopped;
    private IProcessor.IProcessorLifeCycle lifeCycle;

    protected AbsProcessor() {
        this(true);
    }

    protected AbsProcessor(boolean z) {
        this.apmContext = APMContext.instance();
        this.isStopped = false;
    }

    /* access modifiers changed from: protected */
    public IDispatcher getDispatcher(String str) {
        APMContext aPMContext = this.apmContext;
        return APMContext.getDispatcher(str);
    }

    public void setLifeCycle(IProcessor.IProcessorLifeCycle iProcessorLifeCycle) {
        this.lifeCycle = iProcessorLifeCycle;
    }

    /* access modifiers changed from: protected */
    public void startProcessor() {
        if (this.lifeCycle != null) {
            this.lifeCycle.processorOnStart(this);
        }
    }

    /* access modifiers changed from: protected */
    public void stopProcessor() {
        if (!this.isStopped) {
            this.isStopped = true;
            if (this.lifeCycle != null) {
                this.lifeCycle.processorOnEnd(this);
            }
        }
    }
}
