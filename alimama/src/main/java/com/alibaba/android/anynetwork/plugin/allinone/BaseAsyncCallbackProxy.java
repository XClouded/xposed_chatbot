package com.alibaba.android.anynetwork.plugin.allinone;

import com.alibaba.android.anynetwork.core.IANAsyncCallback;

public class BaseAsyncCallbackProxy {
    protected IANAsyncCallback mANCallback;
    protected IAllInOneConverter mConverter;

    public BaseAsyncCallbackProxy(IANAsyncCallback iANAsyncCallback, IAllInOneConverter iAllInOneConverter) {
        this.mANCallback = iANAsyncCallback;
        this.mConverter = iAllInOneConverter;
    }
}
