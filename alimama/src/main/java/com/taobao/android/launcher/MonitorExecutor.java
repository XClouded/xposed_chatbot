package com.taobao.android.launcher;

import android.util.SparseArray;

public abstract class MonitorExecutor<T> implements IExecutable<T> {
    /* access modifiers changed from: protected */
    public void onException(T t, SparseArray<Object> sparseArray, Throwable th) {
    }

    /* access modifiers changed from: protected */
    public abstract void onExecute(T t, SparseArray<Object> sparseArray) throws Exception;

    /* access modifiers changed from: protected */
    public void onFinish(T t, SparseArray<Object> sparseArray) {
    }

    /* access modifiers changed from: protected */
    public SparseArray<Object> onStart(T t) {
        return null;
    }

    public boolean execute(T t) {
        SparseArray<Object> onStart = onStart(t);
        try {
            onExecute(t, onStart);
        } catch (Throwable th) {
            onFinish(t, onStart);
            throw th;
        }
        onFinish(t, onStart);
        return false;
    }
}
