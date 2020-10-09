package com.taobao.android.ultron.datamodel.cache.db;

import kotlin.UByte;

public class Utils {
    private static final long INITIALCRC = -1;
    private static final long POLY64REV = -7661587058870466123L;
    private static long[] sCrcTable = new long[256];

    static {
        for (int i = 0; i < 256; i++) {
            long j = (long) i;
            for (int i2 = 0; i2 < 8; i2++) {
                j = (j >> 1) ^ ((((int) j) & 1) != 0 ? POLY64REV : 0);
            }
            sCrcTable[i] = j;
        }
    }

    public static void assertTrue(boolean z) {
        if (!z) {
            throw new AssertionError();
        }
    }

    public static long crc64Long(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return crc64Long(getBytes(str));
    }

    public static long crc64Long(byte[] bArr) {
        long j = -1;
        for (byte b : bArr) {
            j = (j >> 8) ^ sCrcTable[(((int) j) ^ b) & UByte.MAX_VALUE];
        }
        return j;
    }

    public static byte[] getBytes(String str) {
        byte[] bArr = new byte[(str.length() * 2)];
        int i = 0;
        for (char c : str.toCharArray()) {
            int i2 = i + 1;
            bArr[i] = (byte) (c & 255);
            i = i2 + 1;
            bArr[i2] = (byte) (c >> 8);
        }
        return bArr;
    }
}
