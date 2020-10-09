package com.alimama.union.app.rxnetwork;

import androidx.annotation.Nullable;

public class RxMtopResponse<T> {
    public boolean isReqSuccess;
    @Nullable
    public T result;
    public String retCode;
    public String retMsg;
}
