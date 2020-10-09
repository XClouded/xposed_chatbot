package com.huawei.hms.support.api.push.b.b;

import com.huawei.hms.support.api.push.b.a.a.a;
import com.huawei.hms.support.api.push.b.a.a.b;

/* compiled from: SecretUtil */
public abstract class e {
    private static String b() {
        return "2A57086C86EF54970C1E6EB37BFC72B1";
    }

    private static byte[] a(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null || bArr.length == 0 || bArr2.length == 0) {
            return new byte[0];
        }
        int length = bArr.length;
        if (length != bArr2.length) {
            return new byte[0];
        }
        byte[] bArr3 = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr3[i] = (byte) (bArr[i] ^ bArr2[i]);
        }
        return bArr3;
    }

    private static byte[] a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return new byte[0];
        }
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] >> 2);
        }
        return bArr;
    }

    public static byte[] a() {
        byte[] b = a.b(b.a());
        byte[] b2 = a.b(b.a());
        return a(a(a(b, b2), a.b(b())));
    }
}
