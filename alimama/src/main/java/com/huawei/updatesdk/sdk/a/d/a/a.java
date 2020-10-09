package com.huawei.updatesdk.sdk.a.d.a;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.huawei.updatesdk.sdk.a.d.e;
import java.security.MessageDigest;
import java.util.Locale;

public abstract class a {
    private static String a = "AESUtil";

    public static String a(String str) {
        if (e.a(str)) {
            return null;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
            instance.update(str.getBytes("UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            byte[] digest = instance.digest();
            int length = digest.length;
            for (int i = 0; i < length; i++) {
                stringBuffer.append(String.format("%02X", new Object[]{Byte.valueOf(digest[i])}));
            }
            return stringBuffer.toString();
        } catch (Exception unused) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(a, "sha256EncryptStr error:Exception");
            return null;
        }
    }

    public static String b(String str) {
        String a2 = a(str);
        if (a2 == null) {
            return null;
        }
        return a2.toLowerCase(Locale.getDefault());
    }
}
