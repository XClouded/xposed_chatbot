package mtopsdk.security.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha1Utils {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String HMAC_SHA1 = "HmacSha1";

    private static char[] encodeHex(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[(length << 1)];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr[i] = DIGITS[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr[i3] = DIGITS[bArr[i2] & 15];
        }
        return cArr;
    }

    public static String hmacSha1Hex(String str, String str2) {
        try {
            Mac instance = Mac.getInstance(HMAC_SHA1);
            instance.init(new SecretKeySpec(str2.getBytes("utf-8"), HMAC_SHA1));
            return new String(encodeHex(instance.doFinal(str.getBytes("utf-8"))));
        } catch (Exception unused) {
            return null;
        }
    }
}
