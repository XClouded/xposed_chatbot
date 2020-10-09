package com.taobao.xcode.szxing.qrcode;

import com.taobao.xcode.szxing.EncodeHintType;
import com.taobao.xcode.szxing.WriterException;
import com.taobao.xcode.szxing.common.BitMatrix;
import com.taobao.xcode.szxing.qrcode.decoder.ErrorCorrectionLevel;
import com.taobao.xcode.szxing.qrcode.encoder.ByteMatrix;
import com.taobao.xcode.szxing.qrcode.encoder.Encoder;
import com.taobao.xcode.szxing.qrcode.encoder.QRCode;
import java.util.HashMap;

public class Writer {
    private static final int QUIET_ZONE_SIZE = 1;

    public static BitMatrix encode(String str, ErrorCorrectionLevel errorCorrectionLevel, String str2) throws WriterException {
        if (str.length() != 0) {
            HashMap hashMap = new HashMap();
            hashMap.put(EncodeHintType.CHARACTER_SET, str2);
            ByteMatrix matrix = Encoder.encode(str, errorCorrectionLevel, hashMap).getMatrix();
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            BitMatrix bitMatrix = new BitMatrix(width, height);
            for (int i = 0; i < width; i++) {
                for (int i2 = 0; i2 < height; i2++) {
                    if (matrix.get(i, i2) == 1) {
                        bitMatrix.setRegion(i, i2, 1, 1);
                    }
                }
            }
            return bitMatrix;
        }
        throw new WriterException("Found empty contents");
    }

    public static BitMatrix encode(String str, int i, int i2) throws WriterException {
        return encode(str, i, i2, (Option) null);
    }

    public static BitMatrix encode(String str, int i, int i2, Option option) throws WriterException {
        String str2;
        int i3;
        checkParameters(str, i, i2);
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        HashMap hashMap = new HashMap();
        if (option != null) {
            errorCorrectionLevel = option.getEcLevel();
            i3 = option.getMargin();
            str2 = option.getCharacterSet();
        } else {
            str2 = "UTF-8";
            i3 = 1;
        }
        hashMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        hashMap.put(EncodeHintType.MARGIN, errorCorrectionLevel);
        hashMap.put(EncodeHintType.CHARACTER_SET, str2);
        return renderResult(Encoder.encode(str, errorCorrectionLevel, hashMap), i, i2, i3);
    }

    protected static BitMatrix renderResult(QRCode qRCode, int i, int i2, int i3) {
        ByteMatrix matrix = qRCode.getMatrix();
        if (matrix != null) {
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int i4 = i3 << 1;
            int i5 = width + i4;
            int i6 = i4 + height;
            int max = Math.max(i, i5);
            int max2 = Math.max(i2, i6);
            int min = Math.min(max / i5, max2 / i6);
            int i7 = (max - (width * min)) / 2;
            int i8 = (max2 - (height * min)) / 2;
            BitMatrix bitMatrix = new BitMatrix(max, max2);
            int i9 = 0;
            while (i9 < height) {
                int i10 = i7;
                int i11 = 0;
                while (i11 < width) {
                    if (matrix.get(i11, i9) == 1) {
                        bitMatrix.setRegion(i10, i8, min, min);
                    }
                    i11++;
                    i10 += min;
                }
                i9++;
                i8 += min;
            }
            return bitMatrix;
        }
        throw new IllegalStateException();
    }

    protected static void checkParameters(String str, int i, int i2) throws WriterException {
        if (str.length() == 0) {
            throw new WriterException("Found empty contents");
        } else if (i < 0 || i2 < 0) {
            throw new WriterException("Requested dimensions are too small: " + i + 'x' + i2);
        }
    }
}
