package anet.channel.strategy.utils;

import android.text.TextUtils;
import anet.channel.util.ALog;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class Utils {
    public static String stringNull2Empty(String str) {
        return str == null ? "" : str;
    }

    public static boolean isIPV4Address(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char[] charArray = str.toCharArray();
        if (charArray.length < 7 || charArray.length > 15) {
            return false;
        }
        int i = 0;
        int i2 = 0;
        for (char c : charArray) {
            if (c >= '0' && c <= '9') {
                i = ((i * 10) + c) - 48;
                if (i > 255) {
                    return false;
                }
            } else if (c != '.' || (i2 = i2 + 1) > 3) {
                return false;
            } else {
                i = 0;
            }
        }
        return true;
    }

    public static boolean isIPV6Address(String str) {
        int i;
        int i2;
        boolean z;
        int i3;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            return false;
        }
        if (charArray[0] != ':') {
            i3 = 0;
            z = false;
            i2 = 0;
            i = 0;
        } else if (charArray[1] != ':') {
            return false;
        } else {
            i3 = 1;
            z = false;
            i2 = 0;
            i = 1;
        }
        boolean z2 = true;
        while (i3 < charArray.length) {
            char c = charArray[i3];
            int digit = Character.digit(c, 16);
            if (digit != -1) {
                i2 = (i2 << 4) + digit;
                if (i2 > 65535) {
                    return false;
                }
                z2 = false;
            } else if (c != ':' || (i = i + 1) > 7) {
                return false;
            } else {
                if (!z2) {
                    i2 = 0;
                    z2 = true;
                } else if (z) {
                    return false;
                } else {
                    z = true;
                }
            }
            i3++;
        }
        if (z || i >= 7) {
            return true;
        }
        return false;
    }

    public static boolean checkHostValidAndNotIp(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char[] charArray = str.toCharArray();
        if (charArray.length <= 0 || charArray.length > 255) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 'A' && charArray[i] <= 'Z') || ((charArray[i] >= 'a' && charArray[i] <= 'z') || charArray[i] == '*')) {
                z = true;
            } else if (!((charArray[i] >= '0' && charArray[i] <= '9') || charArray[i] == '.' || charArray[i] == '-')) {
                return false;
            }
        }
        return z;
    }

    public static String longToIP(long j) {
        StringBuilder sb = new StringBuilder(16);
        long j2 = 1000000000;
        do {
            sb.append(j / j2);
            sb.append('.');
            j %= j2;
            j2 /= 1000;
        } while (j2 > 0);
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String encodeQueryParams(Map<String, String> map, String str) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(64);
        try {
            for (Map.Entry next : map.entrySet()) {
                if (next.getKey() != null) {
                    sb.append(URLEncoder.encode((String) next.getKey(), str));
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append(URLEncoder.encode(stringNull2Empty((String) next.getValue()), str).replace("+", "%20"));
                    sb.append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        } catch (UnsupportedEncodingException e) {
            ALog.e("Request", "format params failed", (String) null, e, new Object[0]);
        }
        return sb.toString();
    }
}
