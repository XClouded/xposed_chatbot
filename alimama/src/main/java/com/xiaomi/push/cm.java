package com.xiaomi.push;

import android.util.Base64;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;
import org.apache.http.NameValuePair;

class cm {
    public static String a(String str) {
        if (str == null) {
            return "";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            instance.update(a(str));
            return String.format("%1$032X", new Object[]{new BigInteger(1, instance.digest())});
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String a(List<NameValuePair> list, String str) {
        Collections.sort(list, new cn());
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (NameValuePair next : list) {
            if (!z) {
                sb.append("&");
            }
            sb.append(next.getName());
            sb.append(SymbolExpUtil.SYMBOL_EQUAL);
            sb.append(next.getValue());
            z = false;
        }
        sb.append("&");
        sb.append(str);
        return a(new String(Base64.encode(a(sb.toString()), 2)));
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m150a(String str) {
    }

    /* renamed from: a  reason: collision with other method in class */
    private static byte[] m151a(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str.getBytes();
        }
    }
}
