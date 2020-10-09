package com.taobao.xcode.szxing.qrcode.encoder;

final class MaskUtil {
    private static final int N1 = 3;
    private static final int N2 = 3;
    private static final int N3 = 40;
    private static final int N4 = 10;

    private MaskUtil() {
    }

    static int applyMaskPenaltyRule1(ByteMatrix byteMatrix) {
        return applyMaskPenaltyRule1Internal(byteMatrix, true) + applyMaskPenaltyRule1Internal(byteMatrix, false);
    }

    static int applyMaskPenaltyRule2(ByteMatrix byteMatrix) {
        byte[][] array = byteMatrix.getArray();
        int width = byteMatrix.getWidth();
        int height = byteMatrix.getHeight();
        int i = 0;
        int i2 = 0;
        while (i < height - 1) {
            int i3 = i2;
            int i4 = 0;
            while (i4 < width - 1) {
                byte b = array[i][i4];
                int i5 = i4 + 1;
                if (b == array[i][i5]) {
                    int i6 = i + 1;
                    if (b == array[i6][i4] && b == array[i6][i5]) {
                        i3++;
                    }
                }
                i4 = i5;
            }
            i++;
            i2 = i3;
        }
        return i2 * 3;
    }

    static int applyMaskPenaltyRule3(ByteMatrix byteMatrix) {
        int i;
        int i2;
        int i3;
        int i4;
        byte[][] array = byteMatrix.getArray();
        int width = byteMatrix.getWidth();
        int height = byteMatrix.getHeight();
        int i5 = 0;
        int i6 = 0;
        while (i5 < height) {
            int i7 = i6;
            for (int i8 = 0; i8 < width; i8++) {
                int i9 = i8 + 6;
                if (i9 < width && array[i5][i8] == 1 && array[i5][i8 + 1] == 0 && array[i5][i8 + 2] == 1 && array[i5][i8 + 3] == 1 && array[i5][i8 + 4] == 1 && array[i5][i8 + 5] == 0 && array[i5][i9] == 1 && (((i3 = i8 + 10) < width && array[i5][i8 + 7] == 0 && array[i5][i8 + 8] == 0 && array[i5][i8 + 9] == 0 && array[i5][i3] == 0) || (i8 - 4 >= 0 && array[i5][i8 - 1] == 0 && array[i5][i8 - 2] == 0 && array[i5][i8 - 3] == 0 && array[i5][i4] == 0))) {
                    i7 += 40;
                }
                int i10 = i5 + 6;
                if (i10 < height && array[i5][i8] == 1 && array[i5 + 1][i8] == 0 && array[i5 + 2][i8] == 1 && array[i5 + 3][i8] == 1 && array[i5 + 4][i8] == 1 && array[i5 + 5][i8] == 0 && array[i10][i8] == 1 && (((i = i5 + 10) < height && array[i5 + 7][i8] == 0 && array[i5 + 8][i8] == 0 && array[i5 + 9][i8] == 0 && array[i][i8] == 0) || (i5 - 4 >= 0 && array[i5 - 1][i8] == 0 && array[i5 - 2][i8] == 0 && array[i5 - 3][i8] == 0 && array[i2][i8] == 0))) {
                    i7 += 40;
                }
            }
            i5++;
            i6 = i7;
        }
        return i6;
    }

    static int applyMaskPenaltyRule4(ByteMatrix byteMatrix) {
        byte[][] array = byteMatrix.getArray();
        int width = byteMatrix.getWidth();
        int height = byteMatrix.getHeight();
        int i = 0;
        int i2 = 0;
        while (i < height) {
            byte[] bArr = array[i];
            int i3 = i2;
            for (int i4 = 0; i4 < width; i4++) {
                if (bArr[i4] == 1) {
                    i3++;
                }
            }
            i++;
            i2 = i3;
        }
        double d = (double) i2;
        double height2 = (double) (byteMatrix.getHeight() * byteMatrix.getWidth());
        Double.isNaN(d);
        Double.isNaN(height2);
        return ((int) (Math.abs((d / height2) - 0.5d) * 20.0d)) * 10;
    }

    static boolean getDataMaskBit(int i, int i2, int i3) {
        int i4;
        switch (i) {
            case 0:
                i4 = (i3 + i2) & 1;
                break;
            case 1:
                i4 = i3 & 1;
                break;
            case 2:
                i4 = i2 % 3;
                break;
            case 3:
                i4 = (i3 + i2) % 3;
                break;
            case 4:
                i4 = ((i3 >>> 1) + (i2 / 3)) & 1;
                break;
            case 5:
                int i5 = i3 * i2;
                i4 = (i5 & 1) + (i5 % 3);
                break;
            case 6:
                int i6 = i3 * i2;
                i4 = ((i6 & 1) + (i6 % 3)) & 1;
                break;
            case 7:
                i4 = (((i3 * i2) % 3) + ((i3 + i2) & 1)) & 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid mask pattern: " + i);
        }
        if (i4 == 0) {
            return true;
        }
        return false;
    }

    private static int applyMaskPenaltyRule1Internal(ByteMatrix byteMatrix, boolean z) {
        int height = z ? byteMatrix.getHeight() : byteMatrix.getWidth();
        int width = z ? byteMatrix.getWidth() : byteMatrix.getHeight();
        byte[][] array = byteMatrix.getArray();
        int i = 0;
        for (int i2 = 0; i2 < height; i2++) {
            int i3 = i;
            int i4 = 0;
            byte b = -1;
            for (int i5 = 0; i5 < width; i5++) {
                byte b2 = z ? array[i2][i5] : array[i5][i2];
                if (b2 == b) {
                    i4++;
                } else {
                    if (i4 >= 5) {
                        i3 += (i4 - 5) + 3;
                    }
                    i4 = 1;
                    b = b2;
                }
            }
            if (i4 >= 5) {
                i3 += (i4 - 5) + 3;
            }
            i = i3;
        }
        return i;
    }
}
