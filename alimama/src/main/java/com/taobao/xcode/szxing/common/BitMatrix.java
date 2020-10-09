package com.taobao.xcode.szxing.common;

public final class BitMatrix {
    private final int[] bits;
    private final int height;
    private final int rowSize;
    private final int width;

    public BitMatrix(int i) {
        this(i, i);
    }

    public BitMatrix(int i, int i2) {
        if (i < 1 || i2 < 1) {
            throw new IllegalArgumentException("Both dimensions must be greater than 0");
        }
        this.width = i;
        this.height = i2;
        this.rowSize = (i + 31) >> 5;
        this.bits = new int[(this.rowSize * i2)];
    }

    public boolean get(int i, int i2) {
        return ((this.bits[(i2 * this.rowSize) + (i >> 5)] >>> (i & 31)) & 1) != 0;
    }

    public void set(int i, int i2) {
        int i3 = (i2 * this.rowSize) + (i >> 5);
        int[] iArr = this.bits;
        iArr[i3] = (1 << (i & 31)) | iArr[i3];
    }

    public void flip(int i, int i2) {
        int i3 = (i2 * this.rowSize) + (i >> 5);
        int[] iArr = this.bits;
        iArr[i3] = (1 << (i & 31)) ^ iArr[i3];
    }

    public void clear() {
        int length = this.bits.length;
        for (int i = 0; i < length; i++) {
            this.bits[i] = 0;
        }
    }

    public void setRegion(int i, int i2, int i3, int i4) {
        if (i2 < 0 || i < 0) {
            throw new IllegalArgumentException("Left and top must be nonnegative");
        } else if (i4 < 1 || i3 < 1) {
            throw new IllegalArgumentException("Height and width must be at least 1");
        } else {
            int i5 = i3 + i;
            int i6 = i4 + i2;
            if (i6 > this.height || i5 > this.width) {
                throw new IllegalArgumentException("The region must fit inside the matrix");
            }
            while (i2 < i6) {
                int i7 = this.rowSize * i2;
                for (int i8 = i; i8 < i5; i8++) {
                    int[] iArr = this.bits;
                    int i9 = (i8 >> 5) + i7;
                    iArr[i9] = iArr[i9] | (1 << (i8 & 31));
                }
                i2++;
            }
        }
    }

    public BitArray getRow(int i, BitArray bitArray) {
        if (bitArray == null || bitArray.getSize() < this.width) {
            bitArray = new BitArray(this.width);
        }
        int i2 = i * this.rowSize;
        for (int i3 = 0; i3 < this.rowSize; i3++) {
            bitArray.setBulk(i3 << 5, this.bits[i2 + i3]);
        }
        return bitArray;
    }

    public void setRow(int i, BitArray bitArray) {
        System.arraycopy(bitArray.getBitArray(), 0, this.bits, i * this.rowSize, this.rowSize);
    }

    public int[] getEnclosingRectangle() {
        int i = this.width;
        int i2 = -1;
        int i3 = this.height;
        int i4 = -1;
        int i5 = i;
        int i6 = 0;
        while (i6 < this.height) {
            int i7 = i4;
            int i8 = i2;
            int i9 = i5;
            for (int i10 = 0; i10 < this.rowSize; i10++) {
                int i11 = this.bits[(this.rowSize * i6) + i10];
                if (i11 != 0) {
                    if (i6 < i3) {
                        i3 = i6;
                    }
                    if (i6 > i7) {
                        i7 = i6;
                    }
                    int i12 = i10 * 32;
                    int i13 = 31;
                    if (i12 < i9) {
                        int i14 = 0;
                        while ((i11 << (31 - i14)) == 0) {
                            i14++;
                        }
                        int i15 = i14 + i12;
                        if (i15 < i9) {
                            i9 = i15;
                        }
                    }
                    if (i12 + 31 > i8) {
                        while ((i11 >>> i13) == 0) {
                            i13--;
                        }
                        int i16 = i12 + i13;
                        if (i16 > i8) {
                            i8 = i16;
                        }
                    }
                }
            }
            i6++;
            i5 = i9;
            i2 = i8;
            i4 = i7;
        }
        int i17 = i2 - i5;
        int i18 = i4 - i3;
        if (i17 < 0 || i18 < 0) {
            return null;
        }
        return new int[]{i5, i3, i17, i18};
    }

    public int[] getTopLeftOnBit() {
        int i = 0;
        while (i < this.bits.length && this.bits[i] == 0) {
            i++;
        }
        if (i == this.bits.length) {
            return null;
        }
        int i2 = i / this.rowSize;
        int i3 = (i % this.rowSize) << 5;
        int i4 = 0;
        while ((this.bits[i] << (31 - i4)) == 0) {
            i4++;
        }
        return new int[]{i3 + i4, i2};
    }

    public int[] getBottomRightOnBit() {
        int length = this.bits.length - 1;
        while (length >= 0 && this.bits[length] == 0) {
            length--;
        }
        if (length < 0) {
            return null;
        }
        int i = length / this.rowSize;
        int i2 = (length % this.rowSize) << 5;
        int i3 = 31;
        while ((this.bits[length] >>> i3) == 0) {
            i3--;
        }
        return new int[]{i2 + i3, i};
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BitMatrix)) {
            return false;
        }
        BitMatrix bitMatrix = (BitMatrix) obj;
        if (this.width != bitMatrix.width || this.height != bitMatrix.height || this.rowSize != bitMatrix.rowSize || this.bits.length != bitMatrix.bits.length) {
            return false;
        }
        for (int i = 0; i < this.bits.length; i++) {
            if (this.bits[i] != bitMatrix.bits[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = (((((this.width * 31) + this.width) * 31) + this.height) * 31) + this.rowSize;
        for (int i2 : this.bits) {
            i = (i * 31) + i2;
        }
        return i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.height * (this.width + 1));
        String[] strArr = {"码", "上", "淘"};
        for (int i = 0; i < this.height; i++) {
            for (int i2 = 0; i2 < this.width; i2++) {
                sb.append(get(i2, i) ? strArr[(int) (Math.random() * 3.0d)] : "  ");
            }
            sb.append(10);
        }
        return sb.toString();
    }
}
