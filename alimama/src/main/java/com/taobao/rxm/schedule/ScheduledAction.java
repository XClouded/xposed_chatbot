package com.taobao.rxm.schedule;

import android.os.Process;
import androidx.annotation.NonNull;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.rxm.common.Releasable;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.request.RequestCancelListener;
import com.taobao.rxm.request.RequestContext;
import com.taobao.tcommon.core.RuntimeUtil;
import com.taobao.weex.el.parse.Operators;

public abstract class ScheduledAction implements Runnable, Comparable<ScheduledAction> {
    private static final int MAX_REJECT_STACK_DEPTH = 10;
    private static final int STATE_READY = 1;
    private static final int STATE_RECYCLING = 3;
    private static final int STATE_RUNNING = 2;
    static ThreadLocal<ScheduledAction> sActionCallerThreadLocal = new ThreadLocal<>();
    private ScheduledActionPool mActionPool;
    private boolean mAllowedDirectRun;
    private ScheduledActionListener mBranchActionListener;
    private Consumer<?, ? extends RequestContext> mConsumer;
    private boolean mIsNotConsumeAction;
    private ScheduledActionListener mMasterActionListener;
    private int mPriority = 1;
    private Integer mRejectedStackDepth;
    private ScheduleResultWrapper mResultWrapper;
    private long mRunningThreadId;
    private int mState;
    private long mTimeStamp;

    public abstract void run(Consumer consumer, ScheduleResultWrapper scheduleResultWrapper);

    public ScheduledAction(int i, Consumer<?, ? extends RequestContext> consumer, ScheduleResultWrapper scheduleResultWrapper) {
        reset(i, consumer, scheduleResultWrapper);
    }

    public ScheduledAction(int i, Consumer<?, ? extends RequestContext> consumer, ScheduleResultWrapper scheduleResultWrapper, boolean z) {
        reset(i, consumer, scheduleResultWrapper, z);
    }

    public ScheduledAction reset() {
        reset(1, (Consumer<?, ? extends RequestContext>) null, (ScheduleResultWrapper) null);
        return this;
    }

    public ScheduledAction reset(int i, Consumer<?, ? extends RequestContext> consumer, ScheduleResultWrapper scheduleResultWrapper) {
        return reset(i, consumer, scheduleResultWrapper, true);
    }

    public synchronized ScheduledAction reset(int i, Consumer<?, ? extends RequestContext> consumer, ScheduleResultWrapper scheduleResultWrapper, boolean z) {
        this.mTimeStamp = System.nanoTime();
        this.mPriority = i;
        this.mConsumer = consumer;
        this.mResultWrapper = scheduleResultWrapper;
        this.mAllowedDirectRun = z;
        this.mRejectedStackDepth = null;
        this.mState = 1;
        this.mRunningThreadId = 0;
        this.mMasterActionListener = null;
        this.mBranchActionListener = null;
        this.mIsNotConsumeAction = false;
        return this;
    }

    public Integer getRejectedStackDepth() {
        return this.mRejectedStackDepth;
    }

    public int getState() {
        return this.mState;
    }

    public long getRunningThreadId() {
        return this.mRunningThreadId;
    }

    public boolean canRunDirectly() {
        return !RuntimeUtil.isMainThread() && !mayStackOverflowAfterRejected() && this.mAllowedDirectRun;
    }

    public boolean mayStackOverflowAfterRejected() {
        ScheduledAction scheduledAction;
        if (this.mRejectedStackDepth == null) {
            if (RuntimeUtil.isMainThread() || (scheduledAction = sActionCallerThreadLocal.get()) == null || scheduledAction.getState() != 2 || scheduledAction.getRunningThreadId() != Thread.currentThread().getId()) {
                this.mRejectedStackDepth = 0;
            } else {
                this.mRejectedStackDepth = scheduledAction.getRejectedStackDepth();
            }
        }
        if (this.mRejectedStackDepth == null || this.mRejectedStackDepth.intValue() < 10) {
            return false;
        }
        return true;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public int compareTo(@NonNull ScheduledAction scheduledAction) {
        int priority = scheduledAction.getPriority() - getPriority();
        return priority == 0 ? (int) (this.mTimeStamp - scheduledAction.getTimeStamp()) : priority;
    }

    public void setMasterActionListener(ScheduledActionListener scheduledActionListener) {
        this.mMasterActionListener = scheduledActionListener;
    }

    public void setBranchActionListener(ScheduledActionListener scheduledActionListener) {
        this.mBranchActionListener = scheduledActionListener;
    }

    public synchronized void setScheduledActionPool(ScheduledActionPool scheduledActionPool) {
        this.mActionPool = scheduledActionPool;
    }

    public void run() {
        this.mRunningThreadId = Thread.currentThread().getId();
        if (!RuntimeUtil.isMainThread()) {
            try {
                Process.setThreadPriority(10);
            } catch (Throwable unused) {
            }
            ScheduledAction scheduledAction = sActionCallerThreadLocal.get();
            int i = 0;
            if (scheduledAction != null && scheduledAction.getState() == 2 && scheduledAction.getRunningThreadId() == Thread.currentThread().getId()) {
                if (this.mRejectedStackDepth != null) {
                    i = this.mRejectedStackDepth.intValue();
                }
                this.mRejectedStackDepth = Integer.valueOf(i + 1);
            } else {
                this.mRejectedStackDepth = 0;
            }
            sActionCallerThreadLocal.set(this);
        }
        this.mState = 2;
        run(this.mConsumer, this.mResultWrapper);
        if (!RuntimeUtil.isMainThread()) {
            sActionCallerThreadLocal.set(this);
        }
        if (this.mMasterActionListener != null) {
            this.mMasterActionListener.onActionFinished(this);
        }
        if (this.mBranchActionListener != null) {
            this.mBranchActionListener.onActionFinished(this);
        }
        this.mState = 3;
        synchronized (this) {
            if (this.mActionPool != null) {
                this.mActionPool.recycle(this);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(hashCode()));
        sb.append("@(");
        sb.append(this.mConsumer == null ? "NullConsumer" : this.mConsumer);
        sb.append(")[");
        sb.append(this.mPriority);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append(this.mTimeStamp);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    public boolean isProduceAction() {
        return this.mResultWrapper == null;
    }

    public boolean isConsumeAction() {
        return !this.mIsNotConsumeAction || this.mResultWrapper != null;
    }

    public void notConsumeAction(boolean z) {
        this.mIsNotConsumeAction = z;
    }

    private synchronized RequestContext getRequest() {
        if (this.mConsumer == null || this.mConsumer.getContext() == null) {
            return null;
        }
        return (RequestContext) this.mConsumer.getContext();
    }

    public int getContextId() {
        RequestContext request = getRequest();
        if (request != null) {
            return request.getId();
        }
        return -1;
    }

    public void registerCancelListener(RequestCancelListener requestCancelListener) {
        RequestContext request = getRequest();
        if (request != null) {
            request.registerCancelListener(requestCancelListener);
        }
    }

    public synchronized void unregisterCancelListener(RequestCancelListener requestCancelListener) {
        RequestContext request = getRequest();
        if (request != null) {
            request.unregisterCancelListener(requestCancelListener);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void cancelActing() {
        if (this.mResultWrapper != null && (this.mResultWrapper.newResult instanceof Releasable)) {
            ((Releasable) this.mResultWrapper.newResult).release();
        }
        if (this.mConsumer != null) {
            this.mConsumer.onCancellation();
            if (this.mActionPool != null) {
                this.mActionPool.recycle(this);
            }
        }
    }
}
