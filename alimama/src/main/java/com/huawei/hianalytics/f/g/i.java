package com.huawei.hianalytics.f.g;

import android.text.TextUtils;
import android.util.Pair;
import com.huawei.hianalytics.g.b;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public abstract class i {
    public static long a(String str, long j) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, Locale.getDefault());
            return simpleDateFormat.parse(simpleDateFormat.format(Long.valueOf(j))).getTime();
        } catch (ParseException unused) {
            b.c("HiAnalytics/event/stringUtil", "getMillisOfDate(): Time conversion Exception !");
            return 0;
        }
    }

    public static Pair<String, String> a(String str) {
        String str2;
        String str3;
        if ("_default_config_tag".equals(str)) {
            return new Pair<>(str, "");
        }
        String[] split = str.split("-");
        if (split.length > 2) {
            str3 = split[split.length - 1];
            str2 = str.substring(0, (str.length() - str3.length()) - 1);
        } else {
            str2 = split[0];
            str3 = split[1];
        }
        return new Pair<>(str2, str3);
    }

    public static String a() {
        return "f6040d0e807aaec325ecf44823765544e92905158169f694b282bf17388632cf95a83bae7d2d235c1f039";
    }

    public static String a(int i) {
        switch (i) {
            case 0:
                return "oper";
            case 1:
                return "maint";
            case 2:
                return "preins";
            case 3:
                return "diffprivacy";
            default:
                return "allType";
        }
    }

    public static String a(String str, int i) {
        if (TextUtils.isEmpty(str) || str.length() <= i) {
            return str;
        }
        int length = str.length();
        String str2 = str;
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = length - 1;
            str2 = str2.substring(i3) + str2.substring(0, i3);
        }
        return str2;
    }

    public static String a(String str, String str2, String str3) {
        if ("_default_config_tag".equals(str)) {
            return "_default_config_tag#" + str3;
        }
        return str + "-" + str2 + "#" + str3;
    }

    public static String a(String str, String str2, String str3, String str4, String str5) {
        byte[] a = e.a(str);
        byte[] a2 = e.a(str2);
        byte[] a3 = e.a(str3);
        byte[] a4 = e.a(str4);
        int length = a.length;
        if (length > a2.length) {
            length = a2.length;
        }
        if (length > a3.length) {
            length = a3.length;
        }
        if (length > a4.length) {
            length = a4.length;
        }
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            cArr[i] = (char) (((a[i] ^ a2[i]) ^ a3[i]) ^ a4[i]);
        }
        return a(cArr, e.a(str5));
    }

    private static String a(char[] cArr, byte[] bArr) {
        String str;
        String str2;
        try {
            return e.a(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(cArr, bArr, 10000, 128)).getEncoded());
        } catch (InvalidKeySpecException unused) {
            str2 = "HiAnalytics/event/stringUtil";
            str = "getAuthToken() encryptPBKDF2 Invalid key specification !";
            b.d(str2, str);
            return null;
        } catch (NoSuchAlgorithmException unused2) {
            str2 = "HiAnalytics/event/stringUtil";
            str = "getAuthToken() encryptPBKDF2 No such algorithm!";
            b.d(str2, str);
            return null;
        }
    }

    public static Set<String> a(Set<String> set) {
        String str;
        if (set == null || set.size() == 0) {
            return new HashSet();
        }
        HashSet hashSet = new HashSet(set.size());
        for (String next : set) {
            if (!"_default_config_tag".equals(next)) {
                String str2 = next + "-" + "oper";
                String str3 = next + "-" + "maint";
                str = next + "-" + "diffprivacy";
                hashSet.add(str2);
                hashSet.add(str3);
            } else {
                str = "_default_config_tag";
            }
            hashSet.add(str);
        }
        return hashSet;
    }
}
