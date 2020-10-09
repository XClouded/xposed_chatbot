package com.huawei.hms.c;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.huawei.hms.support.log.a;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* compiled from: SHA256 */
public abstract class i {
    public static byte[] a(byte[] bArr) {
        try {
            return MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256).digest(bArr);
        } catch (NoSuchAlgorithmException e) {
            a.d("SHA256", "NoSuchAlgorithmException" + e.getMessage());
            return new byte[0];
        }
    }

    public static byte[] a(File file) {
        BufferedInputStream bufferedInputStream = null;
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(file));
            try {
                byte[] bArr = new byte[4096];
                int i = 0;
                while (true) {
                    int read = bufferedInputStream2.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    i += read;
                    instance.update(bArr, 0, read);
                }
                if (i > 0) {
                    byte[] digest = instance.digest();
                    e.a((InputStream) bufferedInputStream2);
                    return digest;
                }
                e.a((InputStream) bufferedInputStream2);
                return new byte[0];
            } catch (IOException | NoSuchAlgorithmException unused) {
                bufferedInputStream = bufferedInputStream2;
                try {
                    a.d("SHA256", "An exception occurred while computing file 'SHA-256'.");
                    e.a((InputStream) bufferedInputStream);
                    return new byte[0];
                } catch (Throwable th) {
                    th = th;
                    e.a((InputStream) bufferedInputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedInputStream = bufferedInputStream2;
                e.a((InputStream) bufferedInputStream);
                throw th;
            }
        } catch (IOException | NoSuchAlgorithmException unused2) {
            a.d("SHA256", "An exception occurred while computing file 'SHA-256'.");
            e.a((InputStream) bufferedInputStream);
            return new byte[0];
        }
    }
}
