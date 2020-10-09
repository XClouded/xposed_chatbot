package com.alibaba.taffy.core.util.lang;

import com.alibaba.taffy.core.StandardCharsets;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class StringUtil {
    private static final String DEFAULT_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    public static final String EMPTY = "";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        /* access modifiers changed from: protected */
        public DateFormat initialValue() {
            return new SimpleDateFormat(StringUtil.DEFAULT_FORMATTER);
        }
    };

    public static String defaultIfNull(String str, String str2) {
        return str == null ? str2 : str;
    }

    public static String toCharset(String str, String str2, String str3) {
        if (str == null) {
            return null;
        }
        try {
            return new String(str.getBytes(str2), str3);
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    public static String trimToNull(String str) {
        String trim = trim(str);
        if (isEmpty(trim)) {
            return null;
        }
        return trim;
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static <T extends CharSequence> T defaultIfEmpty(T t, T t2) {
        return isEmpty(t) ? t2 : t;
    }

    public static String toString(Object obj, String str) {
        return obj == null ? str : toString(obj);
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Date) {
            return threadLocal.get().format(obj);
        }
        if (obj instanceof Throwable) {
            return getErrorMessage((Throwable) obj);
        }
        return obj.toString();
    }

    public static String getErrorMessage(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    public static String format(Number number, String str) {
        return format(number, str, (RoundingMode) null);
    }

    public static String format(Number number, String str, RoundingMode roundingMode) {
        if (number == null || isBlank(str)) {
            return "";
        }
        try {
            DecimalFormat decimalFormat = new DecimalFormat(str);
            if (roundingMode != null) {
                decimalFormat.setRoundingMode(roundingMode);
            }
            return decimalFormat.format(number);
        } catch (Throwable unused) {
            return "";
        }
    }

    public static boolean isBlank(CharSequence charSequence) {
        int length;
        if (charSequence == null || (length = charSequence.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence charSequence) {
        return !isBlank(charSequence);
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return !isEmpty(charSequence);
    }

    public static byte[] getBytes(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        return str.getBytes(charset);
    }

    public static byte[] getBytesUtf8(String str) {
        return getBytes(str, StandardCharsets.UTF_8);
    }

    public static byte[] getBytesIso8859_1(String str) {
        return getBytes(str, StandardCharsets.ISO_8859_1);
    }

    public static byte[] getBytesUsAscii(String str) {
        return getBytes(str, StandardCharsets.US_ASCII);
    }

    public static byte[] getBytesUtf16(String str) {
        return getBytes(str, StandardCharsets.UTF_16);
    }

    public static byte[] getBytesUtf16Be(String str) {
        return getBytes(str, StandardCharsets.UTF_16BE);
    }

    public static byte[] getBytesUtf16Le(String str) {
        return getBytes(str, StandardCharsets.UTF_16LE);
    }

    private static IllegalStateException newIllegalStateException(String str, UnsupportedEncodingException unsupportedEncodingException) {
        return new IllegalStateException(str + ": " + unsupportedEncodingException);
    }

    public static String newString(byte[] bArr, Charset charset) {
        if (bArr == null) {
            return null;
        }
        return new String(bArr, charset);
    }

    public static String newString(byte[] bArr, String str) {
        if (bArr == null) {
            return null;
        }
        try {
            return new String(bArr, str);
        } catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(str, e);
        }
    }

    public static String newStringIso8859_1(byte[] bArr) {
        return new String(bArr, StandardCharsets.ISO_8859_1);
    }

    public static String newStringUsAscii(byte[] bArr) {
        return new String(bArr, StandardCharsets.US_ASCII);
    }

    public static String newStringUtf16(byte[] bArr) {
        return new String(bArr, StandardCharsets.UTF_16);
    }

    public static String newStringUtf16Be(byte[] bArr) {
        return new String(bArr, StandardCharsets.UTF_16BE);
    }

    public static String newStringUtf16Le(byte[] bArr) {
        return new String(bArr, StandardCharsets.UTF_16LE);
    }

    public static String newStringUtf8(byte[] bArr) {
        return newString(bArr, StandardCharsets.UTF_8);
    }

    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
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

    public static <T> String join(char c, T... tArr) {
        return join((Object[]) tArr, c);
    }

    public static <T> String join(String str, T... tArr) {
        return join((Object[]) tArr, str);
    }

    public static <T> String join(Collection<T> collection, char c) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), c);
    }

    public static <T> String join(Collection<T> collection, String str) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), str);
    }

    public static <T> String join(Iterable<T> iterable, char c) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), c);
    }

    public static <T> String join(Iterable<T> iterable, String str) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), str);
    }

    public static <T> String join(Iterator<T> it, char c) {
        if (it == null) {
            return null;
        }
        if (!it.hasNext()) {
            return "";
        }
        T next = it.next();
        if (!it.hasNext()) {
            return toString(next);
        }
        StringBuilder sb = new StringBuilder(256);
        if (next != null) {
            sb.append(next);
        }
        while (it.hasNext()) {
            sb.append(c);
            T next2 = it.next();
            if (next2 != null) {
                sb.append(next2);
            }
        }
        return sb.toString();
    }

    public static <T> String join(Iterator<T> it, String str) {
        if (it == null) {
            return null;
        }
        if (!it.hasNext()) {
            return "";
        }
        T next = it.next();
        if (!it.hasNext()) {
            return toString(next);
        }
        StringBuilder sb = new StringBuilder(256);
        if (next != null) {
            sb.append(next);
        }
        while (it.hasNext()) {
            if (str != null) {
                sb.append(str);
            }
            T next2 = it.next();
            if (next2 != null) {
                sb.append(next2);
            }
        }
        return sb.toString();
    }

    public static String join(long[] jArr, char c) {
        if (jArr == null) {
            return null;
        }
        return join(jArr, c, 0, jArr.length);
    }

    public static String join(int[] iArr, char c) {
        if (iArr == null) {
            return null;
        }
        return join(iArr, c, 0, iArr.length);
    }

    public static String join(short[] sArr, char c) {
        if (sArr == null) {
            return null;
        }
        return join(sArr, c, 0, sArr.length);
    }

    public static String join(byte[] bArr, char c) {
        if (bArr == null) {
            return null;
        }
        return join(bArr, c, 0, bArr.length);
    }

    public static String join(char[] cArr, char c) {
        if (cArr == null) {
            return null;
        }
        return join(cArr, c, 0, cArr.length);
    }

    public static String join(float[] fArr, char c) {
        if (fArr == null) {
            return null;
        }
        return join(fArr, c, 0, fArr.length);
    }

    public static String join(double[] dArr, char c) {
        if (dArr == null) {
            return null;
        }
        return join(dArr, c, 0, dArr.length);
    }

    public static String join(Object[] objArr, char c) {
        if (objArr == null) {
            return null;
        }
        return join(objArr, c, 0, objArr.length);
    }

    public static String join(Object[] objArr, String str) {
        if (objArr == null) {
            return null;
        }
        return join(objArr, str, 0, objArr.length);
    }

    public static String join(Object[] objArr, String str, int i, int i2) {
        if (objArr == null) {
            return null;
        }
        if (str == null) {
            str = "";
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(str);
            }
            if (objArr[i4] != null) {
                sb.append(objArr[i4]);
            }
        }
        return sb.toString();
    }

    public static String join(Object[] objArr, char c, int i, int i2) {
        if (objArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(c);
            }
            sb.append(objArr[i4]);
        }
        return sb.toString();
    }

    public static String join(long[] jArr, char c, int i, int i2) {
        if (jArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(c);
            }
            sb.append(jArr[i4]);
        }
        return sb.toString();
    }

    public static String join(int[] iArr, char c, int i, int i2) {
        if (iArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(c);
            }
            sb.append(iArr[i4]);
        }
        return sb.toString();
    }

    public static String join(byte[] bArr, char c, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(c);
            }
            sb.append(bArr[i4]);
        }
        return sb.toString();
    }

    public static String join(short[] sArr, char c, int i, int i2) {
        if (sArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(c);
            }
            sb.append(sArr[i4]);
        }
        return sb.toString();
    }

    public static String join(char[] cArr, char c, int i, int i2) {
        if (cArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(c);
            }
            sb.append(cArr[i4]);
        }
        return sb.toString();
    }

    public static String join(double[] dArr, char c, int i, int i2) {
        if (dArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(c);
            }
            sb.append(dArr[i4]);
        }
        return sb.toString();
    }

    public static String join(float[] fArr, char c, int i, int i2) {
        if (fArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(i3 * 16);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                sb.append(c);
            }
            sb.append(fArr[i4]);
        }
        return sb.toString();
    }

    public static String[] split(String str) {
        return split(str, (String) null, -1);
    }

    public static String[] split(String str, char c) {
        return splitWorker(str, c, false);
    }

    public static String[] split(String str, String str2) {
        return splitWorker(str, str2, -1, false);
    }

    public static String[] split(String str, String str2, int i) {
        return splitWorker(str, str2, i, false);
    }

    private static String[] splitWorker(String str, char c, boolean z) {
        boolean z2;
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return new String[0];
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        boolean z3 = false;
        int i2 = 0;
        loop0:
        while (true) {
            z2 = false;
            while (i < length) {
                if (str.charAt(i) == c) {
                    if (z3 || z) {
                        arrayList.add(str.substring(i2, i));
                        z3 = false;
                        z2 = true;
                    }
                    i2 = i + 1;
                    i = i2;
                } else {
                    i++;
                    z3 = true;
                }
            }
            break loop0;
        }
        if (z3 || (z && z2)) {
            arrayList.add(str.substring(i2, i));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private static String[] splitWorker(String str, String str2, int i, boolean z) {
        boolean z2;
        boolean z3;
        int i2;
        int i3;
        boolean z4;
        boolean z5;
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return new String[0];
        }
        ArrayList arrayList = new ArrayList();
        if (str2 == null) {
            int i4 = 0;
            boolean z6 = false;
            int i5 = 1;
            int i6 = 0;
            loop0:
            while (true) {
                z2 = false;
                while (i4 < length) {
                    if (Character.isWhitespace(str.charAt(i4))) {
                        if (z6 || z) {
                            int i7 = i5 + 1;
                            if (i5 == i) {
                                i4 = length;
                                z5 = false;
                            } else {
                                z5 = true;
                            }
                            arrayList.add(str.substring(i6, i4));
                            z2 = z5;
                            i5 = i7;
                            z6 = false;
                        }
                        i6 = i4 + 1;
                        i4 = i6;
                    } else {
                        i4++;
                        z6 = true;
                    }
                }
                break loop0;
            }
            i2 = i6;
            z3 = z6;
            i3 = i4;
        } else if (str2.length() == 1) {
            char charAt = str2.charAt(0);
            i3 = 0;
            i2 = 0;
            z3 = false;
            z2 = false;
            int i8 = 1;
            while (i3 < length) {
                if (str.charAt(i3) == charAt) {
                    if (z3 || z) {
                        int i9 = i8 + 1;
                        if (i8 == i) {
                            i3 = length;
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        arrayList.add(str.substring(i2, i3));
                        i8 = i9;
                        z3 = false;
                    }
                    i2 = i3 + 1;
                    i3 = i2;
                } else {
                    i3++;
                    z3 = true;
                    z2 = false;
                }
            }
        } else {
            int i10 = 0;
            i2 = 0;
            z3 = false;
            z2 = false;
            int i11 = 1;
            while (i3 < length) {
                if (str2.indexOf(str.charAt(i3)) >= 0) {
                    if (z3 || z) {
                        int i12 = i11 + 1;
                        if (i11 == i) {
                            i3 = length;
                            z4 = false;
                        } else {
                            z4 = true;
                        }
                        arrayList.add(str.substring(i2, i3));
                        i11 = i12;
                        z3 = false;
                    }
                    i2 = i3 + 1;
                    i10 = i2;
                } else {
                    i10 = i3 + 1;
                    z3 = true;
                    z2 = false;
                }
            }
        }
        if (z3 || (z && z2)) {
            arrayList.add(str.substring(i2, i3));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String stripStart(String str, String str2) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        int i = 0;
        if (str2 == null) {
            while (i != length && Character.isWhitespace(str.charAt(i))) {
                i++;
            }
        } else if (str2.isEmpty()) {
            return str;
        } else {
            while (i != length && str2.indexOf(str.charAt(i)) != -1) {
                i++;
            }
        }
        return str.substring(i);
    }

    public static String stripEnd(String str, String str2) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        if (str2 == null) {
            while (length != 0 && Character.isWhitespace(str.charAt(length - 1))) {
                length--;
            }
        } else if (str2.isEmpty()) {
            return str;
        } else {
            while (length != 0 && str2.indexOf(str.charAt(length - 1)) != -1) {
                length--;
            }
        }
        return str.substring(0, length);
    }

    public static String strip(String str) {
        return strip(str, (String) null);
    }

    public static String strip(String str, String str2) {
        if (isEmpty(str)) {
            return str;
        }
        return stripEnd(stripStart(str, str2), str2);
    }

    public static String[] stripAll(String... strArr) {
        return stripAll(strArr, (String) null);
    }

    public static String[] stripAll(String[] strArr, String str) {
        int length;
        if (strArr == null || (length = strArr.length) == 0) {
            return strArr;
        }
        String[] strArr2 = new String[length];
        for (int i = 0; i < length; i++) {
            strArr2[i] = strip(strArr[i], str);
        }
        return strArr2;
    }

    public static boolean equals(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null || charSequence.length() != charSequence2.length()) {
            return false;
        }
        return CharSequenceUtil.regionMatches(charSequence, 0, charSequence2, 0, charSequence.length(), false);
    }

    public static boolean equalsIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null || charSequence.length() != charSequence2.length()) {
            return false;
        }
        return CharSequenceUtil.regionMatches(charSequence, 0, charSequence2, 0, charSequence.length(), true);
    }
}
