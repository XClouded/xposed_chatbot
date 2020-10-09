package com.taobao.android.launcher;

import android.util.Log;

public class ExecutorDecorator<T> implements IExecutable<T> {
    private IExecutable[] mExecutors;

    public ExecutorDecorator(IExecutable... iExecutableArr) {
        this.mExecutors = iExecutableArr;
    }

    public boolean execute(T t) {
        if (this.mExecutors == null || this.mExecutors.length == 0) {
            Log.e(Launcher.TAG, "mExecutors in Decorator can not be null");
            return false;
        } else if (this.mExecutors.length == 1) {
            return this.mExecutors[0].execute(t);
        } else {
            for (IExecutable execute : this.mExecutors) {
                if (execute.execute(t)) {
                    return true;
                }
            }
            return false;
        }
    }
}
