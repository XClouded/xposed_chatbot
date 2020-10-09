package com.alibaba.ut.abtest.internal.util.hash;

import kotlin.UByte;

public final class UnsignedBytes {
    private static final int UNSIGNED_MASK = 255;

    public static int toInt(byte b) {
        return b & UByte.MAX_VALUE;
    }

    private UnsignedBytes() {
    }
}
