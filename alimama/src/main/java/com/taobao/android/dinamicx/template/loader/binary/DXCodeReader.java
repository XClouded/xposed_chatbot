package com.taobao.android.dinamicx.template.loader.binary;

import android.util.Log;
import kotlin.UByte;

public class DXCodeReader {
    private static final String TAG = "CodeReader_TMTEST";
    private byte[] mCode;
    private int mCount;
    private int mCurIndex;
    private int version;

    public void setCode(byte[] bArr) {
        this.mCode = bArr;
        if (this.mCode != null) {
            this.mCount = this.mCode.length;
        } else {
            this.mCount = 0;
        }
        this.mCurIndex = 0;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i) {
        this.version = i;
    }

    public boolean seekBy(int i) {
        return seek(this.mCurIndex + i);
    }

    public boolean seek(int i) {
        if (i > this.mCount) {
            this.mCurIndex = this.mCount;
            return false;
        } else if (i < 0) {
            this.mCurIndex = 0;
            return false;
        } else {
            this.mCurIndex = i;
            return true;
        }
    }

    public void release() {
        if (this.mCode != null) {
            this.mCode = null;
        }
    }

    public byte[] getCode() {
        return this.mCode;
    }

    public int getPos() {
        return this.mCurIndex;
    }

    public int getMaxSize() {
        return this.mCount;
    }

    public boolean isEndOfCode() {
        return this.mCurIndex == this.mCount;
    }

    public byte readByte() {
        if (this.mCode == null || this.mCurIndex >= this.mCount) {
            Log.e(TAG, "readByte error mCode:" + this.mCode + "  mCurIndex:" + this.mCurIndex + "  mCount:" + this.mCount);
            return -1;
        }
        byte[] bArr = this.mCode;
        int i = this.mCurIndex;
        this.mCurIndex = i + 1;
        return bArr[i];
    }

    public short readShort() {
        if (this.mCode == null || this.mCurIndex >= this.mCount - 1) {
            Log.e(TAG, "readShort error mCode:" + this.mCode + "  mCurIndex:" + this.mCurIndex + "  mCount:" + this.mCount);
            return -1;
        }
        byte[] bArr = this.mCode;
        int i = this.mCurIndex;
        this.mCurIndex = i + 1;
        byte[] bArr2 = this.mCode;
        int i2 = this.mCurIndex;
        this.mCurIndex = i2 + 1;
        return (short) (((bArr[i] & UByte.MAX_VALUE) << 8) | (bArr2[i2] & UByte.MAX_VALUE));
    }

    public int readInt() {
        if (this.mCode == null || this.mCurIndex >= this.mCount - 3) {
            Log.e(TAG, "readInt error mCode:" + this.mCode + "  mCurIndex:" + this.mCurIndex + "  mCount:" + this.mCount);
            return -1;
        }
        byte[] bArr = this.mCode;
        int i = this.mCurIndex;
        this.mCurIndex = i + 1;
        byte[] bArr2 = this.mCode;
        int i2 = this.mCurIndex;
        this.mCurIndex = i2 + 1;
        byte b = ((bArr[i] & UByte.MAX_VALUE) << 24) | ((bArr2[i2] & UByte.MAX_VALUE) << 16);
        byte[] bArr3 = this.mCode;
        int i3 = this.mCurIndex;
        this.mCurIndex = i3 + 1;
        byte b2 = b | ((bArr3[i3] & UByte.MAX_VALUE) << 8);
        byte[] bArr4 = this.mCode;
        int i4 = this.mCurIndex;
        this.mCurIndex = i4 + 1;
        return b2 | (bArr4[i4] & UByte.MAX_VALUE);
    }

    public long readLong() {
        if (this.mCode == null || this.mCurIndex >= this.mCount - 7) {
            Log.e(TAG, "readLong error mCode:" + this.mCode + "  mCurIndex:" + this.mCurIndex + "  mCount:" + this.mCount);
            return -1;
        }
        byte[] bArr = this.mCode;
        int i = this.mCurIndex;
        this.mCurIndex = i + 1;
        byte[] bArr2 = this.mCode;
        int i2 = this.mCurIndex;
        this.mCurIndex = i2 + 1;
        long j = ((((long) bArr[i]) & 255) << 56) | ((((long) bArr2[i2]) & 255) << 48);
        byte[] bArr3 = this.mCode;
        int i3 = this.mCurIndex;
        this.mCurIndex = i3 + 1;
        long j2 = j | ((((long) bArr3[i3]) & 255) << 40);
        byte[] bArr4 = this.mCode;
        int i4 = this.mCurIndex;
        this.mCurIndex = i4 + 1;
        long j3 = j2 | ((((long) bArr4[i4]) & 255) << 32);
        byte[] bArr5 = this.mCode;
        int i5 = this.mCurIndex;
        this.mCurIndex = i5 + 1;
        long j4 = j3 | ((((long) bArr5[i5]) & 255) << 24);
        byte[] bArr6 = this.mCode;
        int i6 = this.mCurIndex;
        this.mCurIndex = i6 + 1;
        long j5 = j4 | ((((long) bArr6[i6]) & 255) << 16);
        byte[] bArr7 = this.mCode;
        int i7 = this.mCurIndex;
        this.mCurIndex = i7 + 1;
        long j6 = j5 | ((((long) bArr7[i7]) & 255) << 8);
        byte[] bArr8 = this.mCode;
        int i8 = this.mCurIndex;
        this.mCurIndex = i8 + 1;
        return j6 | (255 & ((long) bArr8[i8]));
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }
}
