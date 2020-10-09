package com.huawei.hianalytics.f.g;

import android.text.TextUtils;
import com.coloros.mcssdk.c.a;
import com.huawei.hianalytics.util.c;
import java.security.SecureRandom;
import java.util.Locale;

public abstract class e {
    private static byte a(char c) {
        return (byte) a.f.indexOf(c);
    }

    public static String a() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        return a(bArr);
    }

    public static String a(byte[] bArr) {
        return c.a(bArr);
    }

    public static byte[] a(String str) {
        if (TextUtils.isEmpty(str)) {
            return new byte[0];
        }
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        int length = upperCase.length() / 2;
        char[] charArray = upperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (a(charArray[i2 + 1]) | (a(charArray[i2]) << 4));
        }
        return bArr;
    }

    public static String b() {
        byte[] bArr = new byte[128];
        new SecureRandom().nextBytes(bArr);
        return a(bArr);
    }

    public static String c() {
        return "a8cb572c8030b2df5c2b622608bea02b0c3e5d4dff3f72c9e3204049a45c0760cd3604af8d57f0e0c693cc";
    }

    public static String d() {
        return "49cb4254efce57d5861aedca86e5baf1205b09cd7f742b38065559f0f70676754915acca5ad6eeaa0d68dfd5143d0a50faedb6cda3b13852705c881ba5b587ecbbb4467cbed08b6754a3f424d90c66fd3b82d48bd5c132b88ff36da668f5adc286ec8317166c70110203010001";
    }
}
