package com.ali.user.mobile.utils;

import android.content.pm.PackageInfo;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MD5Util {
    public static String getMD5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            instance.update(str.getBytes(Charset.forName("UTF-8")));
            return getHashString(instance);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getHashString(MessageDigest messageDigest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest.digest()) {
            sb.append(Integer.toHexString((b >> 4) & 15));
            sb.append(Integer.toHexString(b & 15));
        }
        return sb.toString();
    }

    public static String getApkPublicKeyDigest(String str) {
        try {
            PackageInfo packageInfo = DataProviderFactory.getApplicationContext().getPackageManager().getPackageInfo(str, 64);
            MessageDigest instance = MessageDigest.getInstance("SHA1");
            instance.update(((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getPublicKey().toString().getBytes());
            return getHashString(instance.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getHashString(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(Integer.toHexString((b >> 4) & 15));
            sb.append(Integer.toHexString(b & 15));
        }
        return sb.toString();
    }
}
