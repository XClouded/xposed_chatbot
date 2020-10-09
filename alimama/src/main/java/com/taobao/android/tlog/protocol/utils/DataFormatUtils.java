package com.taobao.android.tlog.protocol.utils;

public class DataFormatUtils {
    public static final byte BYTES_COUNT_INT = 4;

    public static byte[] int2Bytes(int i) {
        return new byte[]{(byte) i, (byte) (i >> 8), (byte) (i >> 16), (byte) (i >> 24)};
    }

    public static byte[] merge(byte[]... bArr) {
        int i = 0;
        for (byte[] length : bArr) {
            i += length.length;
        }
        byte[] bArr2 = new byte[i];
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            System.arraycopy(bArr[i3], 0, bArr2, i2, bArr[i3].length);
            i2 += bArr[i3].length;
        }
        return bArr2;
    }
}
