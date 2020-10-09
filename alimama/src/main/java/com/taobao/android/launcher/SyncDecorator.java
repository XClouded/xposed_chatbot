package com.taobao.android.launcher;

import com.taobao.android.task.Coordinator;

public abstract class SyncDecorator<T> extends ExecutorDecorator<T> {
    /* access modifiers changed from: protected */
    public abstract String getName(T t);

    public SyncDecorator(IExecutable... iExecutableArr) {
        super(iExecutableArr);
    }

    public boolean execute(final T t) {
        Coordinator.runTask(new Coordinator.TaggedRunnable(getName(t)) {
            public void run() {
                boolean unused = SyncDecorator.super.execute(t);
            }
        });
        return false;
    }
}
