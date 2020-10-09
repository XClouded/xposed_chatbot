package com.taobao.fresco.disk.cache;

public interface CacheKey {
    boolean equals(Object obj);

    int hashCode();

    String toString();
}
