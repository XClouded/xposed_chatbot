package com.alibaba.ut.abtest.internal.util;

import android.text.TextUtils;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

public final class StringUtils {
    public static final String EMPTY = "";
    public static final String WHITE_SPACES = " \r\n\t　   ";
    private static char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private StringUtils() {
    }

    public static String emptyToNull(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return str;
    }

    public static String deleteWhitespace(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int length = str.length();
        char[] cArr = new char[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (!Character.isWhitespace(str.charAt(i2))) {
                cArr[i] = str.charAt(i2);
                i++;
            }
        }
        if (i == length) {
            return str;
        }
        return new String(cArr, 0, i);
    }

    public static String bytesToHexString(byte[] bArr) {
        return bytesToHexString(bArr, (Character) null);
    }

    public static String bytesToHexString(byte[] bArr, Character ch) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * (ch == null ? 2 : 3));
        for (int i = 0; i < bArr.length; i++) {
            int i2 = (bArr[i] >>> 4) & 15;
            byte b = bArr[i] & 15;
            if (i > 0 && ch != null) {
                stringBuffer.append(ch.charValue());
            }
            stringBuffer.append(hexChars[i2]);
            stringBuffer.append(hexChars[b]);
        }
        return stringBuffer.toString();
    }

    private static byte[] getBytes(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        return str.getBytes(charset);
    }

    public static byte[] getBytesUtf8(String str) {
        return getBytes(str, Charset.forName("UTF-8"));
    }

    public static String megastrip(String str, boolean z, boolean z2, String str2) {
        if (str == null) {
            return null;
        }
        int i = 0;
        int length = str.length() - 1;
        while (z && i <= length && str2.indexOf(str.charAt(i)) >= 0) {
            i++;
        }
        while (z2 && length >= i && str2.indexOf(str.charAt(length)) >= 0) {
            length--;
        }
        return str.substring(i, length + 1);
    }

    public static String lstrip(String str) {
        return megastrip(str, true, false, WHITE_SPACES);
    }

    public static String rstrip(String str) {
        return megastrip(str, false, true, WHITE_SPACES);
    }

    public static String strip(String str) {
        return megastrip(str, true, true, WHITE_SPACES);
    }

    public static String joinInts(int[] iArr, String str) {
        if (iArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iArr.length; i++) {
            if (i > 0 && str != null) {
                sb.append(str);
            }
            sb.append(String.valueOf(iArr[i]));
        }
        return sb.toString();
    }

    public static int[] splitInts(String str, String str2) throws IllegalArgumentException {
        StringTokenizer stringTokenizer = new StringTokenizer(str, str2);
        int countTokens = stringTokenizer.countTokens();
        int[] iArr = new int[countTokens];
        for (int i = 0; i < countTokens; i++) {
            iArr[i] = Integer.parseInt(stringTokenizer.nextToken());
        }
        return iArr;
    }
}
