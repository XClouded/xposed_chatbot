package com.huawei.hianalytics.util;

import android.text.TextUtils;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.huawei.hianalytics.g.b;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class c {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String a(String str) {
        String str2;
        String str3;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
            instance.update(str.getBytes("UTF-8"));
            return a(instance.digest());
        } catch (NoSuchAlgorithmException unused) {
            str2 = "HianalyticsSDK";
            str3 = "getSHA256StrJava, No Such Algorithm!";
            b.d(str2, str3);
            return "";
        } catch (UnsupportedEncodingException unused2) {
            str2 = "HianalyticsSDK";
            str3 = "getSHA256StrJava, Unsupported Encoding: UTF-8 !";
            b.d(str2, str3);
            return "";
        }
    }

    public static String a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            sb.append(a[(b & 240) >> 4]);
            sb.append(a[b & 15]);
        }
        return sb.toString();
    }

    public static byte[] a(String str, String str2) {
        String str3;
        String str4;
        byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(Charset.forName("UTF-8")), "HmacSHA256");
        try {
            Mac instance = Mac.getInstance(secretKeySpec.getAlgorithm());
            instance.init(secretKeySpec);
            return instance.doFinal(bytes);
        } catch (NoSuchAlgorithmException unused) {
            str4 = "HiAnalyticsHexUtil";
            str3 = "When digest2byte executed Exception has happened!From Algorithm error !";
            b.c(str4, str3);
            return new byte[0];
        } catch (InvalidKeyException unused2) {
            str4 = "HiAnalyticsHexUtil";
            str3 = "Exception has happened when digest2byte,From Invalid key!";
            b.c(str4, str3);
            return new byte[0];
        }
    }
}
