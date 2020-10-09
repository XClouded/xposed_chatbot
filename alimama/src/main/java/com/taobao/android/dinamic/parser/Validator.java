package com.taobao.android.dinamic.parser;

import androidx.annotation.NonNull;
import com.taobao.android.dinamic.model.DinamicInfo;
import com.taobao.android.dinamic.model.DinamicTailer;
import com.taobao.android.dinamic.view.ViewResult;
import java.util.Arrays;
import kotlin.UByte;

public class Validator {
    public static final int CEHCKSUMLEN = 16;
    public static final String MAIGIC = "444E4D58";
    public static final int MINLEN = 56;
    public static final int TAILERLEN = 40;

    public static boolean isLitter() {
        return false;
    }

    public static byte[] validate(byte[] bArr, ViewResult viewResult) {
        DinamicInfo info;
        byte[] option1;
        DinamicTailer tailer = getTailer(bArr);
        if (!(tailer == null || (info = getInfo(bArr, tailer.getOffset())) == null || (option1 = info.getOption1()) == null || option1.length <= 0)) {
            setMask(bArr);
            byte[] checkSum = ValidateAlgorithm.getCheckSum(bArr, option1[option1.length - 1] & 3);
            if (checkSum != null) {
                if (Arrays.equals(checkSum, tailer.getChecksum())) {
                    return copyContent(bArr, tailer.getOffset());
                }
                return null;
            }
        }
        return bArr;
    }

    private static byte[] copyContent(byte[] bArr, long j) {
        if (j >= 2147483647L) {
            return bArr;
        }
        int i = (int) j;
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, i);
        return bArr2;
    }

    public static DinamicInfo getInfo(byte[] bArr, long j) {
        int length = bArr.length;
        DinamicInfo dinamicInfo = new DinamicInfo();
        if (length <= 56 || j <= 0) {
            return null;
        }
        byte[] bArr2 = new byte[4];
        byte[] bArr3 = new byte[8];
        if (j < 2147483647L) {
            int i = (int) j;
            System.arraycopy(bArr, i, bArr2, 0, bArr2.length);
            System.arraycopy(bArr, i + 8, bArr3, 0, bArr3.length);
        }
        dinamicInfo.setInterpreterVersion(byteArrayToStr(bArr2));
        dinamicInfo.setOption1(bArr3);
        return dinamicInfo;
    }

    public static DinamicTailer getTailer(@NonNull byte[] bArr) {
        long j;
        int length = bArr.length;
        DinamicTailer dinamicTailer = new DinamicTailer();
        if (length <= 56) {
            return null;
        }
        int i = length - 40;
        byte[] bArr2 = new byte[4];
        byte[] bArr3 = new byte[4];
        byte[] bArr4 = new byte[8];
        byte[] bArr5 = new byte[16];
        System.arraycopy(bArr, i, bArr2, 0, bArr2.length);
        String byteArrayToHexString = byteArrayToHexString(bArr2);
        if (!MAIGIC.equals(byteArrayToHexString)) {
            return null;
        }
        System.arraycopy(bArr, i + 4, bArr3, 0, bArr3.length);
        System.arraycopy(bArr, i + 8, bArr4, 0, bArr4.length);
        System.arraycopy(bArr, i + 24, bArr5, 0, bArr5.length);
        dinamicTailer.setMagicWord(byteArrayToHexString);
        dinamicTailer.setComplierVersion(byteArrayToStr(bArr3));
        if (isLitter()) {
            j = byteArrayToLongLE(bArr4);
        } else {
            j = byteArrayToLongBE(bArr4);
        }
        dinamicTailer.setOffset(j);
        dinamicTailer.setChecksum(bArr5);
        return dinamicTailer;
    }

    public static String byteArrayToHexString(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            str = str + hexString.toUpperCase();
        }
        return str;
    }

    public static String byteArrayToStr(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return new String(bArr);
    }

    public static long byteArrayToLongLE(byte[] bArr) {
        if (bArr.length < 8) {
            return -1;
        }
        return ((long) ((bArr[1] & UByte.MAX_VALUE) << 8)) | ((long) (bArr[0] & UByte.MAX_VALUE)) | ((long) ((bArr[2] & UByte.MAX_VALUE) << 16)) | ((long) ((bArr[3] & UByte.MAX_VALUE) << 24)) | ((long) ((bArr[4] & UByte.MAX_VALUE) << 32)) | ((long) ((bArr[5] & UByte.MAX_VALUE) << 40)) | ((long) ((bArr[6] & UByte.MAX_VALUE) << 48)) | ((long) ((bArr[7] & UByte.MAX_VALUE) << 56));
    }

    public static long byteArrayToLongBE(byte[] bArr) {
        if (bArr.length < 8) {
            return -1;
        }
        return ((long) ((bArr[6] << 8) & 65535)) | ((long) ((bArr[0] << 56) & 65535)) | ((long) ((bArr[1] << 48) & 65535)) | ((long) ((bArr[2] << 40) & 65535)) | ((long) ((bArr[3] << 32) & 65535)) | ((long) ((bArr[4] << 24) & 65535)) | ((long) ((bArr[5] << 16) & 65535)) | ((long) (bArr[7] & UByte.MAX_VALUE));
    }

    public static void setMask(byte[] bArr) {
        int length = bArr.length;
        if (16 < length) {
            for (int i = length - 16; i < length; i++) {
                bArr[i] = -1;
            }
        }
    }
}
