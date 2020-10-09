package com.ta.audid.store;

import android.text.TextUtils;
import com.ta.audid.utils.RC4;
import com.ta.audid.utils.UtdidLogger;
import com.ta.utdid2.android.utils.Base64;

public class UtdidContentUtil {
    public static String getDecodedContent(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            byte[] decode = Base64.decode(str.getBytes("UTF-8"), 2);
            if (decode != null) {
                return new String(RC4.rc4(decode));
            }
            return "";
        } catch (Exception e) {
            UtdidLogger.e("", e, new Object[0]);
            return "";
        }
    }

    public static String getEncodedContent(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            return new String(Base64.encode(RC4.rc4(str.getBytes()), 2), "UTF-8");
        } catch (Exception e) {
            UtdidLogger.e("", e, new Object[0]);
            return "";
        }
    }
}
