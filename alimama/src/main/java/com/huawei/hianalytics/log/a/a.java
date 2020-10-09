package com.huawei.hianalytics.log.a;

import android.text.TextUtils;
import android.util.Base64;
import com.coloros.mcssdk.mode.Message;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.util.c;
import java.nio.charset.Charset;
import java.text.MessageFormat;

public final class a {
    private static String a(String str) {
        if (!str.startsWith("https") && !str.startsWith("http")) {
            return str;
        }
        String substring = str.substring(str.indexOf("//") + "//".length());
        return substring.substring(substring.indexOf("/"));
    }

    private static String a(String str, String str2) {
        byte[] encode = Base64.encode(c.a(str, str2), 2);
        if (encode == null) {
            return null;
        }
        return new String(encode, Charset.forName("UTF-8"));
    }

    public static String a(String str, String str2, String str3) {
        b bVar;
        String str4;
        StringBuffer stringBuffer = new StringBuffer(512);
        String a = a(str);
        if (a.contains("?")) {
            str4 = a.substring(0, a.indexOf(63));
            bVar = a.substring(a.indexOf("?")).length() > 1 ? new b(a.substring(a.indexOf("?") + 1)) : new b((String) null);
        } else {
            bVar = new b((String) null);
            str4 = a;
        }
        stringBuffer.append("POST");
        stringBuffer.append("&");
        String a2 = bVar.a(Message.APP_ID);
        if (TextUtils.isEmpty(a2)) {
            b.d("AuthoHeadUtil", "appid is empty！");
            return null;
        }
        stringBuffer.append(str4.substring(str4.indexOf("/")));
        stringBuffer.append("&");
        stringBuffer.append(bVar.a());
        stringBuffer.append("&");
        stringBuffer.append(str2);
        stringBuffer.append("&appID=");
        stringBuffer.append(a2);
        return MessageFormat.format("HMAC-SHA256 appID={0}, signature=\"{1}\"", new Object[]{a2, a(stringBuffer.toString(), str3)});
    }
}
