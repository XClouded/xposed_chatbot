package com.taobao.rxm.consume;

import com.taobao.rxm.common.Constant;
import com.taobao.rxm.common.Releasable;
import com.taobao.rxm.produce.ChainProducer;
import com.taobao.rxm.request.RequestContext;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.tcommon.core.RuntimeUtil;
import com.taobao.tcommon.log.FLog;
import com.taobao.weex.el.parse.Operators;

public class ChainDelegateConsumer<OUT, NEXT_OUT extends Releasable, CONTEXT extends RequestContext> implements Consumer<NEXT_OUT, CONTEXT> {
    private ChainProducer<OUT, NEXT_OUT, CONTEXT> mChainProducer;
    private Consumer<OUT, CONTEXT> mConsumer;
    private Scheduler mScheduler;

    public ChainDelegateConsumer(Consumer<OUT, CONTEXT> consumer, ChainProducer<OUT, NEXT_OUT, CONTEXT> chainProducer) {
        this.mConsumer = consumer;
        this.mChainProducer = chainProducer;
    }

    public ChainDelegateConsumer<OUT, NEXT_OUT, CONTEXT> reset() {
        reset((Consumer) null, (ChainProducer) null);
        return this;
    }

    public ChainDelegateConsumer<OUT, NEXT_OUT, CONTEXT> reset(Consumer<OUT, CONTEXT> consumer, ChainProducer<OUT, NEXT_OUT, CONTEXT> chainProducer) {
        this.mConsumer = consumer;
        this.mChainProducer = chainProducer;
        this.mScheduler = null;
        return this;
    }

    public Consumer<OUT, CONTEXT> getConsumer() {
        return this.mConsumer;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        try {
            DelegateConsumerPool<OUT, NEXT_OUT, CONTEXT> delegateConsumerPool = this.mChainProducer.getDelegateConsumerPool();
            if (delegateConsumerPool != null && !delegateConsumerPool.recycle((ChainDelegateConsumer<OUT, NEXT_OUT, CONTEXT>) this)) {
                super.finalize();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void onNewResult(NEXT_OUT next_out, boolean z) {
        String name = this.mChainProducer.getName();
        boolean z2 = false;
        if (getContext().isCancelled()) {
            FLog.i(Constant.RX_LOG, "[DelegateConsumer] ID=%d cancelled before receiving new result, producer=%s isLast=%b", Integer.valueOf(getContext().getId()), name, Boolean.valueOf(z));
            if (next_out != null) {
                next_out.release();
            }
            this.mConsumer.onCancellation();
            return;
        }
        if (this.mChainProducer.getConsumeType().activeOn(1) || (z && this.mChainProducer.getConsumeType().activeOn(2))) {
            z2 = true;
        }
        if (z2) {
            this.mChainProducer.scheduleNewResult(this.mConsumer, z, next_out);
        } else {
            this.mConsumer.onNewResult(next_out, z);
        }
    }

    public void onFailure(Throwable th) {
        if (this.mChainProducer.getConsumeType().activeOn(16)) {
            this.mChainProducer.scheduleFailure(this.mConsumer, th);
        } else {
            this.mConsumer.onFailure(th);
        }
    }

    public void onProgressUpdate(float f) {
        if (this.mChainProducer.getConsumeType().activeOn(4)) {
            this.mChainProducer.scheduleProgressUpdate(this.mConsumer, f);
        } else {
            this.mConsumer.onProgressUpdate(f);
        }
    }

    public void onCancellation() {
        if (this.mChainProducer.getConsumeType().activeOn(8)) {
            this.mChainProducer.scheduleCancellation(this.mConsumer);
        } else {
            this.mConsumer.onCancellation();
        }
    }

    public Consumer<NEXT_OUT, CONTEXT> consumeOn(Scheduler scheduler) {
        this.mScheduler = scheduler;
        return this;
    }

    public CONTEXT getContext() {
        if (this.mConsumer == null) {
            return null;
        }
        return (RequestContext) this.mConsumer.getContext();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RuntimeUtil.getClassShortName(getClass()));
        sb.append("[cxt-id:");
        sb.append(getContext() != null ? getContext().getId() : 0);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }
}
