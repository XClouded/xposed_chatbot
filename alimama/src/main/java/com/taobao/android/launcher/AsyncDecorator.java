package com.taobao.android.launcher;

import com.taobao.android.task.Coordinator;

public abstract class AsyncDecorator<T> extends ExecutorDecorator<T> {
    /* access modifiers changed from: protected */
    public abstract String getName(T t);

    public AsyncDecorator(IExecutable... iExecutableArr) {
        super(iExecutableArr);
    }

    public boolean execute(final T t) {
        Coordinator.postTask(new Coordinator.TaggedRunnable(getName(t)) {
            public void run() {
                boolean unused = AsyncDecorator.super.execute(t);
            }
        });
        return false;
    }
}
