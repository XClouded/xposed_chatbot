package com.taobao.rxm.produce;

import android.text.TextUtils;
import com.taobao.rxm.common.Constant;
import com.taobao.rxm.common.Releasable;
import com.taobao.rxm.consume.ChainConsumer;
import com.taobao.rxm.consume.ConsumeType;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.consume.DelegateConsumerPool;
import com.taobao.rxm.request.RequestContext;
import com.taobao.rxm.schedule.ScheduleResultWrapper;
import com.taobao.rxm.schedule.ScheduledAction;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.tcommon.log.FLog;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ChainProducer<OUT, NEXT_OUT extends Releasable, CONTEXT extends RequestContext> implements Producer<OUT, CONTEXT>, ChainConsumer<OUT, NEXT_OUT, CONTEXT> {
    private Scheduler mConsumeScheduler;
    private final ConsumeType mConsumeType;
    private Type[] mGenericTypes;
    private final String mName;
    private Producer<NEXT_OUT, CONTEXT> mNextProducer;
    private Scheduler mProduceScheduler;
    private final int mProduceType;

    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<OUT, CONTEXT> consumer) {
        return false;
    }

    public abstract DelegateConsumerPool<OUT, NEXT_OUT, CONTEXT> getDelegateConsumerPool();

    /* access modifiers changed from: protected */
    public abstract void scheduleConductingResult(Scheduler scheduler, Consumer<OUT, CONTEXT> consumer, ScheduleResultWrapper<NEXT_OUT> scheduleResultWrapper, boolean z);

    public ChainProducer(String str, int i, int i2) {
        this.mName = getDefaultName(str);
        this.mProduceType = i;
        this.mConsumeType = new ConsumeType(i2);
    }

    private String getDefaultName(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String name = getClass().getName();
        int lastIndexOf = name.lastIndexOf(46);
        return lastIndexOf != -1 ? name.substring(lastIndexOf + 1) : name;
    }

    private boolean isChainEndNode() {
        return this.mProduceType == 2;
    }

    public boolean maySkipResultConsume() {
        return !isChainEndNode() && !getConsumeType().activeOn(1);
    }

    private void onProduceStart(Consumer<OUT, CONTEXT> consumer, boolean z, boolean z2) {
        ProducerListener producerListener = ((RequestContext) consumer.getContext()).getProducerListener();
        if (producerListener != null) {
            producerListener.onEnterIn((RequestContext) consumer.getContext(), getClass(), z, z2);
        }
    }

    private void onProduceFinish(Consumer<OUT, CONTEXT> consumer, boolean z, boolean z2, boolean z3) {
        ProducerListener producerListener = ((RequestContext) consumer.getContext()).getProducerListener();
        if (producerListener != null) {
            producerListener.onExitOut((RequestContext) consumer.getContext(), getClass(), z, z2, z3);
        }
    }

    /* access modifiers changed from: protected */
    public void onConductStart(Consumer<OUT, CONTEXT> consumer) {
        onProduceStart(consumer, false, false);
    }

    /* access modifiers changed from: protected */
    public void onConductFinish(Consumer<OUT, CONTEXT> consumer, boolean z) {
        onProduceFinish(consumer, false, z, false);
    }

    /* access modifiers changed from: protected */
    public void onConsumeStart(Consumer<OUT, CONTEXT> consumer, boolean z) {
        onProduceStart(consumer, true, z);
    }

    /* access modifiers changed from: protected */
    public void onConsumeFinish(Consumer<OUT, CONTEXT> consumer, boolean z, boolean z2) {
        onProduceFinish(consumer, true, z, z2);
    }

    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<OUT, CONTEXT> consumer, ScheduledAction scheduledAction) {
        return conductResult(consumer);
    }

    public int getProduceType() {
        return this.mProduceType;
    }

    public ConsumeType getConsumeType() {
        return this.mConsumeType;
    }

    public ChainProducer<OUT, NEXT_OUT, CONTEXT> produceOn(Scheduler scheduler) {
        this.mProduceScheduler = scheduler;
        return this;
    }

    public ChainProducer<OUT, NEXT_OUT, CONTEXT> consumeOn(Scheduler scheduler) {
        this.mConsumeScheduler = scheduler;
        return this;
    }

    public Scheduler getProduceScheduler() {
        return this.mProduceScheduler;
    }

    public Scheduler getConsumeScheduler() {
        return this.mConsumeScheduler;
    }

    private boolean ensureGenericTypes() {
        if (this.mGenericTypes == null) {
            try {
                this.mGenericTypes = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
            } catch (Exception e) {
                FLog.e(Constant.RX_LOG, "chain producer get generic types error=%s", e);
                return false;
            }
        }
        return true;
    }

    public Type getOutType() {
        if (!ensureGenericTypes()) {
            return null;
        }
        return this.mGenericTypes[0];
    }

    public Type getNextOutType() {
        if (!ensureGenericTypes()) {
            return null;
        }
        if (this.mGenericTypes[1] == RequestContext.class) {
            return this.mGenericTypes[0];
        }
        return this.mGenericTypes[1];
    }

    public Producer<NEXT_OUT, CONTEXT> getNextProducer() {
        return this.mNextProducer;
    }

    public <NN_OUT extends Releasable> ChainProducer setNextProducer(ChainProducer<NEXT_OUT, NN_OUT, CONTEXT> chainProducer) {
        Preconditions.checkNotNull(chainProducer);
        this.mNextProducer = chainProducer;
        return chainProducer;
    }

    public String getName() {
        return this.mName;
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [NEXT_OUT, OUT] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scheduleNewResult(com.taobao.rxm.consume.Consumer<OUT, CONTEXT> r3, boolean r4, NEXT_OUT r5, boolean r6) {
        /*
            r2 = this;
            com.taobao.rxm.schedule.ScheduleResultWrapper r0 = new com.taobao.rxm.schedule.ScheduleResultWrapper
            r1 = 1
            r0.<init>(r1, r4)
            r0.newResult = r5
            com.taobao.rxm.schedule.Scheduler r4 = r2.mConsumeScheduler
            r2.scheduleConductingResult(r4, r3, r0, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.produce.ChainProducer.scheduleNewResult(com.taobao.rxm.consume.Consumer, boolean, com.taobao.rxm.common.Releasable, boolean):void");
    }

    public void scheduleNewResult(Consumer<OUT, CONTEXT> consumer, boolean z, NEXT_OUT next_out) {
        scheduleNewResult(consumer, z, next_out, true);
    }

    public void scheduleProgressUpdate(Consumer<OUT, CONTEXT> consumer, float f) {
        ScheduleResultWrapper scheduleResultWrapper = new ScheduleResultWrapper(4, false);
        scheduleResultWrapper.progress = f;
        scheduleConductingResult(this.mConsumeScheduler, consumer, scheduleResultWrapper);
    }

    public void scheduleCancellation(Consumer<OUT, CONTEXT> consumer) {
        scheduleConductingResult(this.mConsumeScheduler, consumer, new ScheduleResultWrapper(8, true));
    }

    public void scheduleFailure(Consumer<OUT, CONTEXT> consumer, Throwable th) {
        ScheduleResultWrapper scheduleResultWrapper = new ScheduleResultWrapper(16, true);
        scheduleResultWrapper.throwable = th;
        scheduleConductingResult(this.mConsumeScheduler, consumer, scheduleResultWrapper);
    }

    /* access modifiers changed from: protected */
    public void scheduleConductingResult(Scheduler scheduler, Consumer<OUT, CONTEXT> consumer, ScheduleResultWrapper<NEXT_OUT> scheduleResultWrapper) {
        scheduleConductingResult(scheduler, consumer, scheduleResultWrapper, true);
    }
}
