package com.taobao.rxm.produce;

import com.taobao.rxm.common.Constant;
import com.taobao.rxm.common.Releasable;
import com.taobao.rxm.consume.ChainDelegateConsumer;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.consume.DelegateConsumerPool;
import com.taobao.rxm.request.RequestContext;
import com.taobao.rxm.schedule.ScheduleResultWrapper;
import com.taobao.rxm.schedule.ScheduledAction;
import com.taobao.rxm.schedule.ScheduledActionPool;
import com.taobao.tcommon.log.FLog;

public abstract class BaseChainProducer<OUT, NEXT_OUT extends Releasable, CONTEXT extends RequestContext> extends ChainProducer<OUT, NEXT_OUT, CONTEXT> {
    private ScheduledActionPool mActionPool;
    private DelegateConsumerPool<OUT, NEXT_OUT, CONTEXT> mDelegateConsumerPool;

    public void consumeCancellation(Consumer<OUT, CONTEXT> consumer) {
    }

    public void consumeFailure(Consumer<OUT, CONTEXT> consumer, Throwable th) {
    }

    public void consumeNewResult(Consumer<OUT, CONTEXT> consumer, boolean z, NEXT_OUT next_out) {
    }

    public void consumeProgressUpdate(Consumer<OUT, CONTEXT> consumer, float f) {
    }

    public BaseChainProducer(int i, int i2) {
        this((String) null, i, i2);
    }

    public BaseChainProducer(String str, int i, int i2) {
        super(str, i, i2);
        this.mActionPool = new ScheduledActionPool();
        this.mDelegateConsumerPool = new DelegateConsumerPool<>();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: com.taobao.rxm.schedule.ScheduledAction} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: com.taobao.rxm.produce.BaseChainProducer$1} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: com.taobao.rxm.produce.BaseChainProducer$1} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: com.taobao.rxm.produce.BaseChainProducer$1} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scheduleConductingResult(com.taobao.rxm.schedule.Scheduler r8, com.taobao.rxm.consume.Consumer<OUT, CONTEXT> r9, com.taobao.rxm.schedule.ScheduleResultWrapper<NEXT_OUT> r10, boolean r11) {
        /*
            r7 = this;
            if (r8 == 0) goto L_0x0043
            if (r11 == 0) goto L_0x0010
            boolean r0 = r8.isScheduleMainThread()
            if (r0 == 0) goto L_0x0010
            boolean r0 = com.taobao.tcommon.core.RuntimeUtil.isMainThread()
            if (r0 != 0) goto L_0x0043
        L_0x0010:
            com.taobao.rxm.schedule.ScheduledActionPool r0 = r7.mActionPool
            com.taobao.rxm.schedule.ScheduledAction r0 = r0.offer()
            if (r0 != 0) goto L_0x0032
            com.taobao.rxm.produce.BaseChainProducer$1 r0 = new com.taobao.rxm.produce.BaseChainProducer$1
            java.lang.Object r1 = r9.getContext()
            com.taobao.rxm.request.RequestContext r1 = (com.taobao.rxm.request.RequestContext) r1
            int r3 = r1.getSchedulePriority()
            r1 = r0
            r2 = r7
            r4 = r9
            r5 = r10
            r6 = r11
            r1.<init>(r3, r4, r5, r6)
            com.taobao.rxm.schedule.ScheduledActionPool r9 = r7.mActionPool
            r0.setScheduledActionPool(r9)
            goto L_0x003f
        L_0x0032:
            java.lang.Object r1 = r9.getContext()
            com.taobao.rxm.request.RequestContext r1 = (com.taobao.rxm.request.RequestContext) r1
            int r1 = r1.getSchedulePriority()
            r0.reset(r1, r9, r10, r11)
        L_0x003f:
            r8.schedule(r0)
            goto L_0x0047
        L_0x0043:
            r8 = 0
            r7.dispatchResultByType(r9, r10, r8)
        L_0x0047:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.produce.BaseChainProducer.scheduleConductingResult(com.taobao.rxm.schedule.Scheduler, com.taobao.rxm.consume.Consumer, com.taobao.rxm.schedule.ScheduleResultWrapper, boolean):void");
    }

    /* access modifiers changed from: private */
    public void dispatchResultByType(Consumer<OUT, CONTEXT> consumer, ScheduleResultWrapper<NEXT_OUT> scheduleResultWrapper, ScheduledAction scheduledAction) {
        if (scheduleResultWrapper != null) {
            int i = scheduleResultWrapper.consumeType;
            if (i == 1) {
                consumeNewResult(consumer, scheduleResultWrapper.isLast, (Releasable) scheduleResultWrapper.newResult);
            } else if (i == 4) {
                consumeProgressUpdate(consumer, scheduleResultWrapper.progress);
            } else if (i == 8) {
                consumeCancellation(consumer);
            } else if (i == 16) {
                consumeFailure(consumer, scheduleResultWrapper.throwable);
            }
        } else if (((RequestContext) consumer.getContext()).isCancelled()) {
            FLog.i(Constant.RX_LOG, "[ChainProducer] ID=%d cancelled before conducting result, producer=%s type=%s", Integer.valueOf(((RequestContext) consumer.getContext()).getId()), getName(), ProduceType.toString(getProduceType()));
            consumer.onCancellation();
        } else if (!conductResult(consumer, scheduledAction) && getProduceType() == 1) {
            leadToNextProducer(consumer);
        }
    }

    public void produceResults(Consumer<OUT, CONTEXT> consumer) {
        if (((RequestContext) consumer.getContext()).isCancelled()) {
            FLog.i(Constant.RX_LOG, "[ChainProducer] ID=%d cancelled before leading to produce result, producer=%s type=%s", Integer.valueOf(((RequestContext) consumer.getContext()).getId()), getName(), ProduceType.toString(getProduceType()));
            consumer.onCancellation();
        } else if (getProduceType() != 0) {
            scheduleConductingResult(getProduceScheduler(), consumer, (ScheduleResultWrapper) null);
        } else {
            leadToNextProducer(consumer);
        }
    }

    private void leadToNextProducer(Consumer<OUT, CONTEXT> consumer) {
        if (getNextProducer() != null) {
            getNextProducer().produceResults(getDelegatingConsumer(consumer).consumeOn(getConsumeScheduler()));
            return;
        }
        throw new RuntimeException(getName() + " can't conduct result while no next producer");
    }

    public DelegateConsumerPool<OUT, NEXT_OUT, CONTEXT> getDelegateConsumerPool() {
        return this.mDelegateConsumerPool;
    }

    private ChainDelegateConsumer<OUT, NEXT_OUT, CONTEXT> getDelegatingConsumer(Consumer<OUT, CONTEXT> consumer) {
        ChainDelegateConsumer offer = getDelegateConsumerPool().offer();
        return offer != null ? offer.reset(consumer, this) : new ChainDelegateConsumer<>(consumer, this);
    }
}
