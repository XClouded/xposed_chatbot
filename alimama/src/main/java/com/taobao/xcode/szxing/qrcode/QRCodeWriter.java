package com.taobao.xcode.szxing.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.taobao.xcode.szxing.EncodeHintType;
import com.taobao.xcode.szxing.WriterException;
import com.taobao.xcode.szxing.common.BitMatrix;
import com.taobao.xcode.szxing.qrcode.decoder.ErrorCorrectionLevel;
import com.taobao.xcode.szxing.qrcode.encoder.ByteMatrix;
import com.taobao.xcode.szxing.qrcode.encoder.Encoder;
import com.taobao.xcode.szxing.qrcode.encoder.QRCode;
import java.util.HashMap;

public final class QRCodeWriter extends Writer {
    private static final int QUIET_ZONE_SIZE = 1;

    public static Bitmap encode2Bitmap(String str, int i) throws WriterException {
        return encode2Bitmap(str, i, i, (Option) null);
    }

    public static Bitmap encode2Bitmap(String str, int i, int i2, Option option) throws WriterException {
        int i3;
        checkParameters(str, i, i2);
        if (option == null) {
            option = new Option();
        }
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        String str2 = "UTF-8";
        HashMap hashMap = new HashMap();
        if (option != null) {
            errorCorrectionLevel = option.getEcLevel();
            int margin = option.getMargin();
            i3 = margin;
            str2 = option.getCharacterSet();
        } else {
            i3 = 1;
        }
        hashMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        hashMap.put(EncodeHintType.MARGIN, errorCorrectionLevel);
        hashMap.put(EncodeHintType.CHARACTER_SET, str2);
        QRCode encode = Encoder.encode(str, errorCorrectionLevel, hashMap);
        int calculateMutiple = calculateMutiple(encode.getMatrix(), i, i2, i3);
        Bitmap bitMap = toBitMap(renderResult(encode, i, i2, i3), calculateMutiple, i, i2, option);
        if (bitMap != null) {
            Bitmap logo = option.getLogo();
            if (logo == null) {
                return bitMap;
            }
            int sqrt = ((int) Math.sqrt((double) ((((float) encode.getVersion().getECBlocksForLevel(errorCorrectionLevel).getTotalECCodewords()) / 3.3f) * 8.0f))) * calculateMutiple;
            if (logo.getWidth() > sqrt) {
                logo = Bitmap.createScaledBitmap(logo, sqrt, sqrt, true);
            }
            return addWatermark(bitMap, logo, option.getColorDepth());
        }
        throw new WriterException("change to qrcode bitmap error]");
    }

    private static Bitmap addWatermark(Bitmap bitmap, Bitmap bitmap2, Bitmap.Config config) {
        if (bitmap == null || bitmap2 == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = bitmap2.getWidth();
        int width3 = bitmap2.getWidth();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, (float) ((width / 2) - (width2 / 2)), (float) ((height / 2) - (width3 / 2)), (Paint) null);
        canvas.save(31);
        canvas.restore();
        return createBitmap;
    }

    private static Bitmap toBitMap(BitMatrix bitMatrix, int i, int i2, int i3, Option option) {
        BitMatrix bitMatrix2 = bitMatrix;
        int i4 = i2;
        int i5 = i3;
        int[] enclosingRectangle = bitMatrix.getEnclosingRectangle();
        int intValue = option.getForegroundColor().intValue();
        int intValue2 = option.getBackgroundColor().intValue();
        Integer pdpInnerColor = option.getPdpInnerColor();
        int[] iArr = new int[(i4 * i5)];
        for (int i6 = 0; i6 < i5; i6++) {
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i;
                if (isInParten(enclosingRectangle, i, i7, i6)) {
                    iArr[(i6 * i4) + i7] = bitMatrix.get(i7, i6) ? pdpInnerColor.intValue() : intValue2;
                } else {
                    iArr[(i6 * i4) + i7] = bitMatrix.get(i7, i6) ? intValue : intValue2;
                }
            }
            int i9 = i;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i4, i5, option.getColorDepth());
        createBitmap.setPixels(iArr, 0, i2, 0, 0, i2, i3);
        return createBitmap;
    }

    private static boolean isInParten(int[] iArr, int i, int i2, int i3) {
        int i4 = iArr[0];
        int i5 = iArr[1];
        int i6 = iArr[2];
        int i7 = iArr[3];
        int i8 = i * 2;
        int i9 = i4 + i8;
        if (i9 <= i2) {
            int i10 = i * 5;
            if (i2 < i4 + i10 && i5 + i8 <= i3 && i3 < i10 + i5) {
                return true;
            }
        }
        if (i9 <= i2) {
            int i11 = i * 5;
            if (i2 < i4 + i11) {
                int i12 = i7 + i5;
                if (i3 <= i12 - i8 && i3 > i12 - i11) {
                    return true;
                }
            }
        }
        int i13 = i4 + i6;
        if (i2 <= i13 - i8) {
            int i14 = i * 5;
            return i2 > i13 - i14 && i8 + i5 <= i3 && i3 < i5 + i14;
        }
    }

    private static int calculateMutiple(ByteMatrix byteMatrix, int i, int i2, int i3) {
        int i4 = i3 << 1;
        int width = byteMatrix.getWidth() + i4;
        int height = byteMatrix.getHeight() + i4;
        return Math.min(Math.max(i, width) / width, Math.max(i2, height) / height);
    }
}
