package com.alibaba.ut.abtest.internal.util.hash;

public final class UnsignedInts {
    static final long INT_MASK = 4294967295L;

    public static long toLong(int i) {
        return ((long) i) & INT_MASK;
    }

    private UnsignedInts() {
    }
}
