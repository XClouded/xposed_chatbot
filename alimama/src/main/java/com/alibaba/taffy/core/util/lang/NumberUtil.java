package com.alibaba.taffy.core.util.lang;

import java.util.Date;
import kotlin.UByte;

public class NumberUtil {
    private static final int BITS = 8;

    public static byte[] toBytes(int i) {
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < 4; i2++) {
            bArr[i2] = (byte) (i >> ((3 - i2) * 8));
        }
        return bArr;
    }

    public static byte[] toBytes(long j) {
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((int) (j >> ((7 - i) * 8)));
        }
        return bArr;
    }

    public static byte toByte(Object obj) {
        return toByte(obj, (byte) 0);
    }

    public static byte toByte(Object obj, byte b) {
        if (obj == null) {
            return b;
        }
        if (obj instanceof Character) {
            return Integer.valueOf(((Character) obj).charValue()).byteValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        if (obj instanceof Date) {
            return Long.valueOf(((Date) obj).getTime()).byteValue();
        }
        String obj2 = obj.toString();
        if (isNatureDigit(obj2)) {
            return Byte.parseByte(obj2);
        }
        return isNumber(obj2) ? Double.valueOf(toDouble(obj, (double) b)).byteValue() : b;
    }

    public static short toShort(Object obj) {
        return toShort(obj, 0);
    }

    public static short toShort(Object obj, short s) {
        if (obj == null) {
            return s;
        }
        if (obj instanceof Character) {
            return Integer.valueOf(((Character) obj).charValue()).shortValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        if (obj instanceof Date) {
            return Long.valueOf(((Date) obj).getTime()).shortValue();
        }
        String obj2 = obj.toString();
        if (isNatureDigit(obj2)) {
            return Short.parseShort(obj2);
        }
        return isNumber(obj2) ? Double.valueOf(toDouble(obj, (double) s)).shortValue() : s;
    }

    public static int toInt(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            i += (bArr[i2] & UByte.MAX_VALUE) << ((3 - i2) * 8);
        }
        return i;
    }

    public static int toInt(Object obj) {
        return toInt(obj, 0);
    }

    public static int toInt(Object obj, int i) {
        if (obj == null) {
            return i;
        }
        if (obj instanceof Character) {
            return ((Character) obj).charValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        if (obj instanceof Date) {
            return Long.valueOf(((Date) obj).getTime()).intValue();
        }
        String obj2 = obj.toString();
        if (isNatureDigit(obj2)) {
            return Integer.parseInt(obj2);
        }
        return isNumber(obj2) ? Double.valueOf(toDouble(obj, (double) i)).intValue() : i;
    }

    public static long toLong(byte[] bArr) {
        long j = 0;
        for (int i = 0; i < bArr.length; i++) {
            j += (long) ((bArr[i] & UByte.MAX_VALUE) << ((7 - i) * 8));
        }
        return j;
    }

    public static long toLong(Object obj) {
        return toLong(obj, 0);
    }

    public static long toLong(Object obj, long j) {
        if (obj == null) {
            return j;
        }
        if (obj instanceof Character) {
            return (long) ((Character) obj).charValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        if (obj instanceof Date) {
            return ((Date) obj).getTime();
        }
        String obj2 = obj.toString();
        if (isNatureDigit(obj2)) {
            return Long.parseLong(obj2);
        }
        return isNumber(obj2) ? Double.valueOf(toDouble(obj, (double) j)).longValue() : j;
    }

    public static float toFloat(Object obj) {
        return toFloat(obj, 0.0f);
    }

    public static float toFloat(Object obj, float f) {
        if (obj == null) {
            return f;
        }
        if (obj instanceof Character) {
            return (float) ((Character) obj).charValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        if (obj instanceof Date) {
            return Long.valueOf(((Date) obj).getTime()).floatValue();
        }
        String obj2 = obj.toString();
        return isNumber(obj2) ? Float.parseFloat(obj2) : f;
    }

    public static double toDouble(Object obj) {
        return toDouble(obj, 0.0d);
    }

    public static double toDouble(Object obj, double d) {
        if (obj == null) {
            return d;
        }
        if (obj instanceof Character) {
            return (double) ((Character) obj).charValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        if (obj instanceof Date) {
            return Long.valueOf(((Date) obj).getTime()).doubleValue();
        }
        String obj2 = obj.toString();
        return isNumber(obj2) ? Double.parseDouble(obj2) : d;
    }

    public static String toHexString(int i, int i2) {
        if (i2 == 10) {
            return String.valueOf(i2);
        }
        StringBuilder sb = new StringBuilder();
        if (i == 0) {
            return "0";
        }
        while (i > 0) {
            sb.insert(0, toHexCode(i % i2));
            i /= i2;
        }
        return sb.toString();
    }

    static String toHexCode(int i) {
        if (i < 10) {
            return String.valueOf(i);
        }
        return String.valueOf((char) (i + 55));
    }

    public static boolean isNatureDigit(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        for (int i = str.charAt(0) == '-' ? 1 : 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:147:?, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:?, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:?, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:?, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:?, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0093, code lost:
        if (r3 >= r0.length) goto L_0x00e2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0097, code lost:
        if (r0[r3] < '0') goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x009b, code lost:
        if (r0[r3] > '9') goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x009d, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00a0, code lost:
        if (r0[r3] == 'e') goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00a4, code lost:
        if (r0[r3] != 'E') goto L_0x00a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00a9, code lost:
        if (r0[r3] != '.') goto L_0x00b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00ab, code lost:
        if (r12 != false) goto L_0x00b1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00ad, code lost:
        if (r13 == false) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00b0, code lost:
        return r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00b1, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00b2, code lost:
        if (r6 != false) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00b8, code lost:
        if (r0[r3] == 'd') goto L_0x00ca;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00be, code lost:
        if (r0[r3] == 'D') goto L_0x00ca;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00c2, code lost:
        if (r0[r3] == 'f') goto L_0x00ca;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00c8, code lost:
        if (r0[r3] != 'F') goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00ca, code lost:
        return r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x00cf, code lost:
        if (r0[r3] == 'l') goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x00d5, code lost:
        if (r0[r3] != 'L') goto L_0x00d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x00d8, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x00d9, code lost:
        if (r11 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x00db, code lost:
        if (r13 != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x00dd, code lost:
        if (r12 != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x00df, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x00e1, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x00e2, code lost:
        if (r6 != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x00e4, code lost:
        if (r11 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x00e6, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isNumber(java.lang.String r16) {
        /*
            boolean r0 = com.alibaba.taffy.core.util.lang.StringUtil.isEmpty(r16)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            char[] r0 = r16.toCharArray()
            int r2 = r0.length
            char r3 = r0[r1]
            r4 = 45
            r5 = 1
            if (r3 != r4) goto L_0x0016
            r3 = 1
            goto L_0x0017
        L_0x0016:
            r3 = 0
        L_0x0017:
            int r6 = r3 + 1
            r7 = 70
            r8 = 102(0x66, float:1.43E-43)
            r9 = 57
            r10 = 48
            if (r2 <= r6) goto L_0x0079
            char r11 = r0[r3]
            if (r11 != r10) goto L_0x0079
            char r11 = r0[r6]
            r12 = 120(0x78, float:1.68E-43)
            if (r11 == r12) goto L_0x004f
            char r11 = r0[r6]
            r12 = 88
            if (r11 != r12) goto L_0x0034
            goto L_0x004f
        L_0x0034:
            char r11 = r0[r6]
            boolean r11 = java.lang.Character.isDigit(r11)
            if (r11 == 0) goto L_0x0079
        L_0x003c:
            int r2 = r0.length
            if (r6 >= r2) goto L_0x004e
            char r2 = r0[r6]
            if (r2 < r10) goto L_0x004d
            char r2 = r0[r6]
            r3 = 55
            if (r2 <= r3) goto L_0x004a
            goto L_0x004d
        L_0x004a:
            int r6 = r6 + 1
            goto L_0x003c
        L_0x004d:
            return r1
        L_0x004e:
            return r5
        L_0x004f:
            int r3 = r3 + 2
            if (r3 != r2) goto L_0x0054
            return r1
        L_0x0054:
            int r2 = r0.length
            if (r3 >= r2) goto L_0x0078
            char r2 = r0[r3]
            if (r2 < r10) goto L_0x005f
            char r2 = r0[r3]
            if (r2 <= r9) goto L_0x0074
        L_0x005f:
            char r2 = r0[r3]
            r4 = 97
            if (r2 < r4) goto L_0x0069
            char r2 = r0[r3]
            if (r2 <= r8) goto L_0x0074
        L_0x0069:
            char r2 = r0[r3]
            r4 = 65
            if (r2 < r4) goto L_0x0077
            char r2 = r0[r3]
            if (r2 <= r7) goto L_0x0074
            goto L_0x0077
        L_0x0074:
            int r3 = r3 + 1
            goto L_0x0054
        L_0x0077:
            return r1
        L_0x0078:
            return r5
        L_0x0079:
            int r2 = r2 + -1
            r6 = 0
            r11 = 0
            r12 = 0
            r13 = 0
        L_0x007f:
            r14 = 69
            r15 = 101(0x65, float:1.42E-43)
            r4 = 46
            if (r3 < r2) goto L_0x00e8
            int r7 = r2 + 1
            if (r3 >= r7) goto L_0x0092
            if (r6 == 0) goto L_0x0092
            if (r11 != 0) goto L_0x0092
            r7 = 70
            goto L_0x00e8
        L_0x0092:
            int r2 = r0.length
            if (r3 >= r2) goto L_0x00e2
            char r2 = r0[r3]
            if (r2 < r10) goto L_0x009e
            char r2 = r0[r3]
            if (r2 > r9) goto L_0x009e
            return r5
        L_0x009e:
            char r2 = r0[r3]
            if (r2 == r15) goto L_0x00e1
            char r2 = r0[r3]
            if (r2 != r14) goto L_0x00a7
            goto L_0x00e1
        L_0x00a7:
            char r2 = r0[r3]
            if (r2 != r4) goto L_0x00b2
            if (r12 != 0) goto L_0x00b1
            if (r13 == 0) goto L_0x00b0
            goto L_0x00b1
        L_0x00b0:
            return r11
        L_0x00b1:
            return r1
        L_0x00b2:
            if (r6 != 0) goto L_0x00cb
            char r2 = r0[r3]
            r4 = 100
            if (r2 == r4) goto L_0x00ca
            char r2 = r0[r3]
            r4 = 68
            if (r2 == r4) goto L_0x00ca
            char r2 = r0[r3]
            if (r2 == r8) goto L_0x00ca
            char r2 = r0[r3]
            r7 = 70
            if (r2 != r7) goto L_0x00cb
        L_0x00ca:
            return r11
        L_0x00cb:
            char r2 = r0[r3]
            r4 = 108(0x6c, float:1.51E-43)
            if (r2 == r4) goto L_0x00d9
            char r0 = r0[r3]
            r2 = 76
            if (r0 != r2) goto L_0x00d8
            goto L_0x00d9
        L_0x00d8:
            return r1
        L_0x00d9:
            if (r11 == 0) goto L_0x00e0
            if (r13 != 0) goto L_0x00e0
            if (r12 != 0) goto L_0x00e0
            r1 = 1
        L_0x00e0:
            return r1
        L_0x00e1:
            return r1
        L_0x00e2:
            if (r6 != 0) goto L_0x00e7
            if (r11 == 0) goto L_0x00e7
            r1 = 1
        L_0x00e7:
            return r1
        L_0x00e8:
            char r5 = r0[r3]
            if (r5 < r10) goto L_0x00f5
            char r5 = r0[r3]
            if (r5 > r9) goto L_0x00f5
            r5 = 45
            r6 = 0
            r11 = 1
            goto L_0x012c
        L_0x00f5:
            char r5 = r0[r3]
            if (r5 != r4) goto L_0x0103
            if (r12 != 0) goto L_0x0102
            if (r13 == 0) goto L_0x00fe
            goto L_0x0102
        L_0x00fe:
            r5 = 45
            r12 = 1
            goto L_0x012c
        L_0x0102:
            return r1
        L_0x0103:
            char r4 = r0[r3]
            if (r4 == r15) goto L_0x0122
            char r4 = r0[r3]
            if (r4 != r14) goto L_0x010c
            goto L_0x0122
        L_0x010c:
            char r4 = r0[r3]
            r5 = 43
            if (r4 == r5) goto L_0x011a
            char r4 = r0[r3]
            r5 = 45
            if (r4 != r5) goto L_0x0119
            goto L_0x011c
        L_0x0119:
            return r1
        L_0x011a:
            r5 = 45
        L_0x011c:
            if (r6 != 0) goto L_0x011f
            return r1
        L_0x011f:
            r6 = 0
            r11 = 0
            goto L_0x012c
        L_0x0122:
            r5 = 45
            if (r13 == 0) goto L_0x0127
            return r1
        L_0x0127:
            if (r11 != 0) goto L_0x012a
            return r1
        L_0x012a:
            r6 = 1
            r13 = 1
        L_0x012c:
            int r3 = r3 + 1
            r4 = 45
            r5 = 1
            goto L_0x007f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.lang.NumberUtil.isNumber(java.lang.String):boolean");
    }
}
