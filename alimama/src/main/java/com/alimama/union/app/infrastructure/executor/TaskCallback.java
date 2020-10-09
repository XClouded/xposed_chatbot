package com.alimama.union.app.infrastructure.executor;

public interface TaskCallback<T> {
    void onFailure(Exception exc);

    void onSuccess(T t);
}
