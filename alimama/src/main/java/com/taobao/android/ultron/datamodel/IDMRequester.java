package com.taobao.android.ultron.datamodel;

public interface IDMRequester {
    boolean execute(AbsRequestCallback absRequestCallback);

    @Deprecated
    boolean execute(IRequestCallback iRequestCallback);

    boolean execute(Object obj, AbsRequestCallback absRequestCallback);
}
