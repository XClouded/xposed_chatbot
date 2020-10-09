package com.alibaba.ut.abtest.internal.util.hash;

import java.io.Serializable;

public interface Funnel<T> extends Serializable {
    void funnel(T t, PrimitiveSink primitiveSink);
}
