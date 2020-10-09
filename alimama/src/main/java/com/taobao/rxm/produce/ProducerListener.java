package com.taobao.rxm.produce;

import com.taobao.rxm.request.RequestContext;

public interface ProducerListener<T extends RequestContext> {
    void onEnterIn(T t, Class cls, boolean z, boolean z2);

    void onExitOut(T t, Class cls, boolean z, boolean z2, boolean z3);
}
