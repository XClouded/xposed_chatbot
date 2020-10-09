package com.alibaba.taffy.core.collection;

public interface Transformer<I, O> {
    O transform(I i);
}
