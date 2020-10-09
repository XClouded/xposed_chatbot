package com.taobao.rxm.consume;

import android.util.Log;
import com.taobao.rxm.common.Constant;
import com.taobao.rxm.request.RequestContext;
import com.taobao.rxm.schedule.ScheduleResultWrapper;
import com.taobao.rxm.schedule.ScheduledAction;
import com.taobao.rxm.schedule.ScheduledActionPool;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.tcommon.core.RuntimeUtil;
import com.taobao.tcommon.log.FLog;
import com.taobao.weex.el.parse.Operators;

public abstract class BaseConsumer<OUT, CONTEXT extends RequestContext> implements Consumer<OUT, CONTEXT> {
    private final ScheduledActionPool mActionPool = new ScheduledActionPool();
    final CONTEXT mContext;
    boolean mIsFinished;
    private Scheduler mScheduler;

    /* access modifiers changed from: protected */
    public abstract void onCancellationImpl();

    /* access modifiers changed from: protected */
    public abstract void onFailureImpl(Throwable th);

    /* access modifiers changed from: protected */
    public abstract void onNewResultImpl(OUT out, boolean z);

    /* access modifiers changed from: protected */
    public void onProgressUpdateImpl(float f) {
    }

    public BaseConsumer(CONTEXT context) {
        Preconditions.checkNotNull(context);
        this.mContext = context;
    }

    public CONTEXT getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: protected */
    public Scheduler getScheduler() {
        return this.mScheduler;
    }

    /* access modifiers changed from: protected */
    public boolean needScheduleAction() {
        return this.mScheduler != null && (!this.mScheduler.isScheduleMainThread() || !RuntimeUtil.isMainThread());
    }

    private void scheduleConsumingResult(ScheduleResultWrapper<OUT> scheduleResultWrapper) {
        if (needScheduleAction()) {
            ScheduledAction offer = this.mActionPool.offer();
            if (offer == null) {
                offer = new ScheduledAction(getContext().getSchedulePriority(), this, scheduleResultWrapper) {
                    public void run(Consumer consumer, ScheduleResultWrapper scheduleResultWrapper) {
                        BaseConsumer.this.dispatchResultByType(scheduleResultWrapper);
                    }
                };
                offer.setScheduledActionPool(this.mActionPool);
            } else {
                offer.reset(getContext().getSchedulePriority(), this, scheduleResultWrapper);
            }
            this.mScheduler.schedule(offer);
            return;
        }
        dispatchResultByType(scheduleResultWrapper);
    }

    /* access modifiers changed from: private */
    public void dispatchResultByType(ScheduleResultWrapper<OUT> scheduleResultWrapper) {
        try {
            if (8 != scheduleResultWrapper.consumeType) {
                if (!this.mContext.isCancelledInMultiplex()) {
                    int i = scheduleResultWrapper.consumeType;
                    if (i == 1) {
                        onNewResultImpl(scheduleResultWrapper.newResult, scheduleResultWrapper.isLast);
                        return;
                    } else if (i == 4) {
                        onProgressUpdateImpl(scheduleResultWrapper.progress);
                        return;
                    } else if (i == 16) {
                        onFailureImpl(scheduleResultWrapper.throwable);
                        return;
                    } else {
                        return;
                    }
                }
            }
            onCancellationImpl();
        } catch (Exception e) {
            onUnhandledException(e);
        }
    }

    public synchronized void onNewResult(OUT out, boolean z) {
        if (!this.mIsFinished) {
            if (this.mContext.isCancelledInMultiplex()) {
                onCancellation();
                return;
            }
            this.mIsFinished = z;
            ScheduleResultWrapper scheduleResultWrapper = new ScheduleResultWrapper(1, this.mIsFinished);
            scheduleResultWrapper.newResult = out;
            scheduleConsumingResult(scheduleResultWrapper);
        }
    }

    public synchronized void onFailure(Throwable th) {
        if (!this.mIsFinished) {
            if (this.mContext.isCancelledInMultiplex()) {
                onCancellation();
                return;
            }
            this.mIsFinished = true;
            ScheduleResultWrapper scheduleResultWrapper = new ScheduleResultWrapper(16, true);
            scheduleResultWrapper.throwable = th;
            scheduleConsumingResult(scheduleResultWrapper);
        }
    }

    public synchronized void onCancellation() {
        if (!this.mIsFinished) {
            this.mIsFinished = true;
            scheduleConsumingResult(new ScheduleResultWrapper(8, true));
        }
    }

    public synchronized void onProgressUpdate(float f) {
        if (!this.mIsFinished) {
            ScheduleResultWrapper scheduleResultWrapper = new ScheduleResultWrapper(4, false);
            scheduleResultWrapper.progress = f;
            scheduleConsumingResult(scheduleResultWrapper);
        }
    }

    /* access modifiers changed from: protected */
    public void onUnhandledException(Exception exc) {
        FLog.e(Constant.RX_LOG, "UnhandledException when BaseConsumer dispatch result: %s", Log.getStackTraceString(exc));
    }

    public Consumer<OUT, CONTEXT> consumeOn(Scheduler scheduler) {
        this.mScheduler = scheduler;
        return this;
    }

    public String toString() {
        return RuntimeUtil.getClassShortName(getClass()) + "[cxt-id:" + getContext().getId() + Operators.ARRAY_END_STR;
    }
}
