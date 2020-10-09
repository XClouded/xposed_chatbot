package com.xiaomi.push;

import kotlin.UByte;

public class ac {
    public static int a(byte[] bArr) {
        if (bArr.length == 4) {
            return (bArr[3] & UByte.MAX_VALUE) | 0 | ((bArr[0] & UByte.MAX_VALUE) << 24) | ((bArr[1] & UByte.MAX_VALUE) << 16) | ((bArr[2] & UByte.MAX_VALUE) << 8);
        }
        throw new IllegalArgumentException("the length of bytes must be 4");
    }

    public static byte[] a(int i) {
        return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }
}
