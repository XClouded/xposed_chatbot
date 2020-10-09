package com.xiaomi.push;

import android.content.Context;
import com.coloros.mcssdk.c.a;
import kotlin.UByte;

public class af {
    static final char[] a = a.f.toCharArray();

    public static String a(byte[] bArr, int i, int i2) {
        StringBuilder sb = new StringBuilder(i2 * 2);
        for (int i3 = 0; i3 < i2; i3++) {
            byte b = bArr[i + i3] & UByte.MAX_VALUE;
            sb.append(a[b >> 4]);
            sb.append(a[b & 15]);
        }
        return sb.toString();
    }

    public static boolean a(Context context) {
        return ae.f110a;
    }
}
