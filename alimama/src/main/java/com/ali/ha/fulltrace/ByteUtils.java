package com.ali.ha.fulltrace;

public class ByteUtils {
    public static final byte BYTES_COUNT_BYTE = 1;
    public static final byte BYTES_COUNT_DOUBLE = 8;
    public static final byte BYTES_COUNT_FLOAT = 4;
    public static final byte BYTES_COUNT_INT = 4;
    public static final byte BYTES_COUNT_LONG = 8;
    public static final byte BYTES_COUNT_SHORT = 2;

    public static byte[] floatToBytes(float f) {
        long floatToRawIntBits = (long) Float.floatToRawIntBits(f);
        return new byte[]{(byte) ((int) (floatToRawIntBits & 255)), (byte) ((int) ((floatToRawIntBits >> 8) & 255)), (byte) ((int) ((floatToRawIntBits >> 16) & 255)), (byte) ((int) ((floatToRawIntBits >> 24) & 255))};
    }

    public static byte[] doubleToBytes(double d) {
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        return new byte[]{(byte) ((int) (doubleToRawLongBits & 255)), (byte) ((int) ((doubleToRawLongBits >> 8) & 255)), (byte) ((int) ((doubleToRawLongBits >> 16) & 255)), (byte) ((int) ((doubleToRawLongBits >> 24) & 255)), (byte) ((int) ((doubleToRawLongBits >> 32) & 255)), (byte) ((int) ((doubleToRawLongBits >> 40) & 255)), (byte) ((int) ((doubleToRawLongBits >> 48) & 255)), (byte) ((int) ((doubleToRawLongBits >> 56) & 255))};
    }

    public static byte[] long2Bytes(long j) {
        return new byte[]{(byte) ((int) j), (byte) ((int) (j >> 8)), (byte) ((int) (j >> 16)), (byte) ((int) (j >> 24)), (byte) ((int) (j >> 32)), (byte) ((int) (j >> 40)), (byte) ((int) (j >> 48)), (byte) ((int) (j >> 56))};
    }

    public static byte[] int2Bytes(int i) {
        return new byte[]{(byte) i, (byte) (i >> 8), (byte) (i >> 16), (byte) (i >> 24)};
    }

    public static byte[] short2Bytes(short s) {
        return new byte[]{(byte) s, (byte) (s >> 8)};
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

    public static int fill(byte[] bArr, byte[] bArr2, int i) {
        System.arraycopy(bArr2, 0, bArr, i, bArr2.length);
        return bArr2.length;
    }
}
