package com.huawei.hianalytics.f.d;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.huawei.hianalytics.g.b;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public abstract class a {

    /* renamed from: com.huawei.hianalytics.f.d.a$a  reason: collision with other inner class name */
    public interface C0005a {
        void a(long j, byte[] bArr);
    }

    public static void a(String str, String str2, C0005a aVar) {
        MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
        long currentTimeMillis = System.currentTimeMillis();
        try {
            byte[] digest = instance.digest((str + str2 + currentTimeMillis).getBytes("UTF-8"));
            if (aVar != null) {
                aVar.a(currentTimeMillis, digest);
            }
        } catch (UnsupportedEncodingException unused) {
            b.c("Generator", "getHmac(): UnsupportedEncodingException: Exception when writing the log file.");
        }
    }
}
