package com.alibaba.ut.abtest.internal.util.hash;

import com.alibaba.ut.abtest.internal.util.PreconditionUtils;
import kotlin.UByte;

public final class Ints {
    public static int fromBytes(byte b, byte b2, byte b3, byte b4) {
        return (b << 24) | ((b2 & UByte.MAX_VALUE) << 16) | ((b3 & UByte.MAX_VALUE) << 8) | (b4 & UByte.MAX_VALUE);
    }

    private Ints() {
    }

    public static int min(int... iArr) {
        PreconditionUtils.checkArgument(iArr.length > 0);
        int i = iArr[0];
        for (int i2 = 1; i2 < iArr.length; i2++) {
            if (iArr[i2] < i) {
                i = iArr[i2];
            }
        }
        return i;
    }
}
