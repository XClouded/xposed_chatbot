package com.taobao.tao.log.utils;

import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.coloros.mcssdk.c.a;
import com.taobao.android.tlog.protocol.utils.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SimpleCrypto {
    public static String encrypt(String str, String str2) {
        String substring = HashUtils.hash(str2, MessageDigestAlgorithms.MD5).substring(0, 16);
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(substring.getBytes(), "AES");
            Cipher instance = Cipher.getInstance(a.a);
            instance.init(1, secretKeySpec, new IvParameterSpec(substring.getBytes()));
            return Base64.encodeBase64String(instance.doFinal(str.toString().getBytes("UTF-8")));
        } catch (Exception unused) {
            return Base64.encodeBase64String("I'm wrong!".getBytes());
        }
    }

    public static String decrypt(String str, String str2) {
        String substring = HashUtils.hash(str2, MessageDigestAlgorithms.MD5).substring(0, 16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(substring.getBytes(), "AES");
        try {
            Cipher instance = Cipher.getInstance(a.a);
            instance.init(2, secretKeySpec, new IvParameterSpec(substring.getBytes()));
            return new String(instance.doFinal(Base64.decode(str)), "UTF-8");
        } catch (Exception unused) {
            return null;
        }
    }
}
