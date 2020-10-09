package com.alimama.union.app.rxnetwork;

import androidx.annotation.Nullable;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class RxResponse {
    @Nullable
    byte[] data;
    boolean isReqSuccess;
    String msg;
    public SafeJSONObject obj;
    String result;
    String retCode;
}
