package com.taobao.pexode.mimetype;

import com.taobao.pexode.Pexode;
import com.taobao.tcommon.log.FLog;
import java.io.ByteArrayInputStream;
import kotlin.UByte;

public class MimeTypeCheckUtil {
    public static final byte[] APNG_ACTL_BYTES = {97, 99, 84, 76};
    private static final String APNG_ACTL_CHUNK = "acTL";
    public static final int APNG_ACTL_OFFSET = 37;
    private static final String APNG_FCTL_CHUNK = "fcTL";
    private static final String APNG_FDAT_CHUNK = "fdAT";
    private static final byte[] BMP_HEADER = asciiBytes("BM");
    public static final int BMP_HEADER_LENGTH = 2;
    private static final int EXIF_MAGIC_NUMBER = 65496;
    private static final byte[] GIF_HEADER_87A = asciiBytes("GIF87a");
    private static final byte[] GIF_HEADER_89A = asciiBytes("GIF89a");
    public static final int GIF_HEADER_LENGTH = 6;
    private static final byte[] HEIF_HEADER = asciiBytes("heic");
    public static final int HEIF_HEADER_LENGTH = 4;
    public static final int JPEG_HEADER_LENGTH = 2;
    private static final int PNG_CHUNK_LENGTH = 4;
    public static final byte[] PNG_HEADER = {-119, 80, 78, 71, 13, 10, 26, 10};
    public static final int PNG_HEADER_LENGTH = 41;
    private static final String PNG_IDAT_CHUNK = "IDAT";
    public static final int WEBP_A_HEADER_LENGTH = 21;
    public static final int WEBP_HEADER_LENGTH = 21;
    private static final byte[] WEBP_NAME_BYTES = asciiBytes("WEBP");
    private static final byte[] WEBP_RIFF_BYTES = asciiBytes("RIFF");
    private static final byte[] WEBP_VP8X_BYTES = asciiBytes("VP8X");

    public static boolean isJpegHeader(byte[] bArr) {
        if (bArr != null && bArr.length >= 2) {
            if (((bArr[1] & UByte.MAX_VALUE) | ((bArr[0] << 8) & 65280)) == EXIF_MAGIC_NUMBER) {
                return true;
            }
        }
        return false;
    }

    private static boolean isWebPSimpleHeader(byte[] bArr) {
        return bArr.length >= 21 && matchBytePattern(bArr, 0, WEBP_RIFF_BYTES) && matchBytePattern(bArr, 8, WEBP_NAME_BYTES);
    }

    private static boolean isWebPAlphaHeader(byte[] bArr) {
        return bArr.length >= 21 && matchBytePattern(bArr, 12, WEBP_VP8X_BYTES) && (bArr[20] & 16) == 16;
    }

    public static boolean isWebPHeader(byte[] bArr) {
        return bArr != null && isWebPSimpleHeader(bArr) && !isWebPAlphaHeader(bArr);
    }

    public static boolean isWebPAHeader(byte[] bArr) {
        return bArr != null && isWebPSimpleHeader(bArr) && isWebPAlphaHeader(bArr);
    }

    public static boolean isGifHeader(byte[] bArr) {
        if (bArr == null || bArr.length < 6) {
            return false;
        }
        if (matchBytePattern(bArr, 0, GIF_HEADER_87A) || matchBytePattern(bArr, 0, GIF_HEADER_89A)) {
            return true;
        }
        return false;
    }

    public static boolean isPngHeader(byte[] bArr) {
        if (bArr == null || bArr.length < 41 || !matchBytePattern(bArr, 0, PNG_HEADER) || containActlChunk(bArr) || bArr[25] >= 3) {
            return false;
        }
        return true;
    }

    public static boolean isPngAHeader(byte[] bArr) {
        if (bArr == null || bArr.length < 41 || !matchBytePattern(bArr, 0, PNG_HEADER) || containActlChunk(bArr) || bArr[25] < 3) {
            return false;
        }
        return true;
    }

    public static boolean isBmpHeader(byte[] bArr) {
        if (bArr == null || bArr.length < 2 || !matchBytePattern(bArr, 0, BMP_HEADER)) {
            return false;
        }
        return true;
    }

    public static boolean isHeifHeader(byte[] bArr) {
        return bArr != null && bArr.length >= 4 && matchBytePattern(bArr, 20, HEIF_HEADER);
    }

    public static boolean matchBytePattern(byte[] bArr, int i, byte[] bArr2) {
        if (bArr == null || bArr2 == null || i < 0 || bArr2.length + i > bArr.length) {
            return false;
        }
        for (int i2 = 0; i2 < bArr2.length; i2++) {
            if (bArr[i2 + i] != bArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] asciiBytes(String str) {
        try {
            return str.getBytes("ASCII");
        } catch (Exception e) {
            FLog.e(Pexode.TAG, "check image format asciiBytes error=%s", e);
            return null;
        }
    }

    public static boolean containActlChunk(byte[] bArr) {
        long j;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        if (byteArrayInputStream.skip(33) != 33) {
            return false;
        }
        do {
            try {
                byte[] readApngChunk = readApngChunk(byteArrayInputStream);
                if (readApngChunk != null) {
                    StringBuilder sb = new StringBuilder();
                    int length = readApngChunk.length;
                    for (int i = 0; i < length; i++) {
                        sb.append(String.format("%02X", new Object[]{Byte.valueOf(readApngChunk[i])}));
                    }
                    int parseInt = Integer.parseInt(sb.toString(), 16);
                    byte[] readApngChunk2 = readApngChunk(byteArrayInputStream);
                    if (readApngChunk2 != null) {
                        String str = new String(readApngChunk2);
                        if (APNG_ACTL_CHUNK.equals(str)) {
                            return true;
                        }
                        if (!APNG_FCTL_CHUNK.equals(str) && !PNG_IDAT_CHUNK.equals(str)) {
                            if (!APNG_FDAT_CHUNK.equals(str)) {
                                j = (long) (parseInt + 4);
                            }
                        }
                        return false;
                    }
                }
            } catch (Throwable unused) {
            }
            return false;
        } while (byteArrayInputStream.skip(j) == j);
        return false;
    }

    private static byte[] readApngChunk(ByteArrayInputStream byteArrayInputStream) {
        byte[] bArr = new byte[4];
        if (byteArrayInputStream.read(bArr, 0, 4) != 4) {
            return null;
        }
        return bArr;
    }
}
