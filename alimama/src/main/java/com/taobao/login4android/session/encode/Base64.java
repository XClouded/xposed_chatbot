package com.taobao.login4android.session.encode;

import com.alibaba.wireless.security.SecExceptionCode;
import com.facebook.imageutils.JfifUtil;
import java.nio.charset.Charset;
import kotlin.jvm.internal.ByteCompanionObject;

public class Base64 {
    static final int BASELENGTH = 255;
    static final byte[] CHUNK_SEPARATOR = "\r\n".getBytes(Charset.forName("UTF-8"));
    static final int CHUNK_SIZE = 76;
    static final int EIGHTBIT = 8;
    static final int FOURBYTE = 4;
    static final int LOOKUPLENGTH = 64;
    static final byte PAD = 61;
    static final int SIGN = -128;
    static final int SIXTEENBIT = 16;
    static final int TWENTYFOURBITGROUP = 24;
    private static byte[] base64Alphabet = new byte[255];
    private static byte[] lookUpBase64Alphabet = new byte[64];

    static {
        int i;
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < 255; i4++) {
            base64Alphabet[i4] = -1;
        }
        for (int i5 = 90; i5 >= 65; i5--) {
            base64Alphabet[i5] = (byte) (i5 - 65);
        }
        int i6 = SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE;
        while (true) {
            i = 26;
            if (i6 < 97) {
                break;
            }
            base64Alphabet[i6] = (byte) ((i6 - 97) + 26);
            i6--;
        }
        int i7 = 57;
        while (true) {
            i2 = 52;
            if (i7 < 48) {
                break;
            }
            base64Alphabet[i7] = (byte) ((i7 - 48) + 52);
            i7--;
        }
        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;
        for (int i8 = 0; i8 <= 25; i8++) {
            lookUpBase64Alphabet[i8] = (byte) (i8 + 65);
        }
        int i9 = 0;
        while (i <= 51) {
            lookUpBase64Alphabet[i] = (byte) (i9 + 97);
            i++;
            i9++;
        }
        while (i2 <= 61) {
            lookUpBase64Alphabet[i2] = (byte) (i3 + 48);
            i2++;
            i3++;
        }
        lookUpBase64Alphabet[62] = 43;
        lookUpBase64Alphabet[63] = 47;
    }

    private static boolean isBase64(byte b) {
        return b == 61 || base64Alphabet[b] != -1;
    }

    public static boolean isArrayByteBase64(byte[] bArr) {
        if (r0 == 0) {
            return true;
        }
        for (byte isBase64 : discardWhitespace(bArr)) {
            if (!isBase64(isBase64)) {
                return false;
            }
        }
        return true;
    }

    public static byte[] encodeBase64(byte[] bArr) {
        return encodeBase64(bArr, false);
    }

    public static byte[] encodeBase64Chunked(byte[] bArr) {
        return encodeBase64(bArr, true);
    }

    public byte[] decode(byte[] bArr) {
        return decodeBase64(bArr);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z) {
        int i;
        int i2;
        int i3;
        byte[] bArr2 = bArr;
        int length = bArr2.length * 8;
        int i4 = length % 24;
        int i5 = length / 24;
        int i6 = i4 != 0 ? (i5 + 1) * 4 : i5 * 4;
        if (z) {
            if (CHUNK_SEPARATOR.length == 0) {
                i = 0;
            } else {
                i = (int) Math.ceil((double) (((float) i6) / 76.0f));
            }
            i6 += CHUNK_SEPARATOR.length * i;
        } else {
            i = 0;
        }
        byte[] bArr3 = new byte[i6];
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 76;
        while (i7 < i5) {
            int i11 = i7 * 3;
            byte b = bArr2[i11];
            byte b2 = bArr2[i11 + 1];
            byte b3 = bArr2[i11 + 2];
            byte b4 = (byte) (b2 & 15);
            byte b5 = (byte) (b & 3);
            byte b6 = (byte) ((b & ByteCompanionObject.MIN_VALUE) == 0 ? b >> 2 : (b >> 2) ^ JfifUtil.MARKER_SOFn);
            byte b7 = (byte) ((b2 & ByteCompanionObject.MIN_VALUE) == 0 ? b2 >> 4 : (b2 >> 4) ^ 240);
            if ((b3 & ByteCompanionObject.MIN_VALUE) == 0) {
                i2 = i5;
                i3 = b3 >> 6;
            } else {
                i2 = i5;
                i3 = (b3 >> 6) ^ 252;
            }
            byte b8 = (byte) i3;
            bArr3[i8] = lookUpBase64Alphabet[b6];
            bArr3[i8 + 1] = lookUpBase64Alphabet[b7 | (b5 << 4)];
            bArr3[i8 + 2] = lookUpBase64Alphabet[b8 | (b4 << 2)];
            bArr3[i8 + 3] = lookUpBase64Alphabet[b3 & 63];
            i8 += 4;
            if (z && i8 == i10) {
                System.arraycopy(CHUNK_SEPARATOR, 0, bArr3, i8, CHUNK_SEPARATOR.length);
                i9++;
                int length2 = ((i9 + 1) * 76) + (CHUNK_SEPARATOR.length * i9);
                i8 += CHUNK_SEPARATOR.length;
                i10 = length2;
            }
            i7++;
            i5 = i2;
        }
        int i12 = i7 * 3;
        if (i4 == 8) {
            byte b9 = bArr2[i12];
            byte b10 = (byte) (b9 & 3);
            bArr3[i8] = lookUpBase64Alphabet[(byte) ((b9 & ByteCompanionObject.MIN_VALUE) == 0 ? b9 >> 2 : (b9 >> 2) ^ JfifUtil.MARKER_SOFn)];
            bArr3[i8 + 1] = lookUpBase64Alphabet[b10 << 4];
            bArr3[i8 + 2] = PAD;
            bArr3[i8 + 3] = PAD;
        } else if (i4 == 16) {
            byte b11 = bArr2[i12];
            byte b12 = bArr2[i12 + 1];
            byte b13 = (byte) (b12 & 15);
            byte b14 = (byte) (b11 & 3);
            byte b15 = (byte) ((b11 & ByteCompanionObject.MIN_VALUE) == 0 ? b11 >> 2 : (b11 >> 2) ^ JfifUtil.MARKER_SOFn);
            byte b16 = (byte) ((b12 & ByteCompanionObject.MIN_VALUE) == 0 ? b12 >> 4 : (b12 >> 4) ^ 240);
            bArr3[i8] = lookUpBase64Alphabet[b15];
            bArr3[i8 + 1] = lookUpBase64Alphabet[b16 | (b14 << 4)];
            bArr3[i8 + 2] = lookUpBase64Alphabet[b13 << 2];
            bArr3[i8 + 3] = PAD;
        }
        if (z && i9 < i) {
            System.arraycopy(CHUNK_SEPARATOR, 0, bArr3, i6 - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);
        }
        return bArr3;
    }

    public static byte[] decodeBase64(byte[] bArr) {
        byte[] discardNonBase64 = discardNonBase64(bArr);
        if (discardNonBase64.length == 0) {
            return new byte[0];
        }
        int length = discardNonBase64.length / 4;
        int length2 = discardNonBase64.length;
        while (discardNonBase64[length2 - 1] == 61) {
            length2--;
            if (length2 == 0) {
                return new byte[0];
            }
        }
        byte[] bArr2 = new byte[(length2 - length)];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2 * 4;
            byte b = discardNonBase64[i3 + 2];
            byte b2 = discardNonBase64[i3 + 3];
            byte b3 = base64Alphabet[discardNonBase64[i3]];
            byte b4 = base64Alphabet[discardNonBase64[i3 + 1]];
            if (b != 61 && b2 != 61) {
                byte b5 = base64Alphabet[b];
                byte b6 = base64Alphabet[b2];
                bArr2[i] = (byte) ((b3 << 2) | (b4 >> 4));
                bArr2[i + 1] = (byte) (((b4 & 15) << 4) | ((b5 >> 2) & 15));
                bArr2[i + 2] = (byte) ((b5 << 6) | b6);
            } else if (b == 61) {
                bArr2[i] = (byte) ((b4 >> 4) | (b3 << 2));
            } else if (b2 == 61) {
                byte b7 = base64Alphabet[b];
                bArr2[i] = (byte) ((b3 << 2) | (b4 >> 4));
                bArr2[i + 1] = (byte) (((b4 & 15) << 4) | ((b7 >> 2) & 15));
            }
            i += 3;
        }
        return bArr2;
    }

    static byte[] discardWhitespace(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            byte b = bArr[i2];
            if (!(b == 13 || b == 32)) {
                switch (b) {
                    case 9:
                    case 10:
                        break;
                    default:
                        bArr2[i] = bArr[i2];
                        i++;
                        break;
                }
            }
        }
        byte[] bArr3 = new byte[i];
        System.arraycopy(bArr2, 0, bArr3, 0, i);
        return bArr3;
    }

    static byte[] discardNonBase64(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (isBase64(bArr[i2])) {
                bArr2[i] = bArr[i2];
                i++;
            }
        }
        byte[] bArr3 = new byte[i];
        System.arraycopy(bArr2, 0, bArr3, 0, i);
        return bArr3;
    }

    public byte[] encode(byte[] bArr) {
        return encodeBase64(bArr, false);
    }
}
