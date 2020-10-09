package com.alipay.sdk.app;

import android.text.TextUtils;
import com.alipay.sdk.data.a;
import java.util.Collections;
import java.util.List;

public class i {
    public static List<a.C0001a> a = Collections.singletonList(c);
    private static String b = "";
    private static final a.C0001a c = new a.C0001a("com.eg.android.AlipayGphone", 73, com.alipay.sdk.cons.a.h);
    private static final a.C0001a d = new a.C0001a(PayResultActivity.c, 40, "e6b1bdcb890370f2f2419fe06d0fdf7628ad0083d52da1ecfe991164711bbf9297e75353de96f1740695d07610567b1240549af9cbd87d06919ac31c859ad37ab6907c311b4756e1e208775989a4f691bff4bbbc58174d2a96b1d0d970a05114d7ee57dfc33b1bafaf6e0d820e838427018b6435f903df04ba7fd34d73f843df9434b164e0220baabb10c8978c3f4c6b7da79d8220a968356d15090dea07df9606f665cbec14d218dd3d691cce2866a58840971b6a57b76af88b1a65fdffd2c080281a6ab20be5879e0330eb7ff70871ce684e7174ada5dc3159c461375a0796b17ce7beca83cf34f65976d237aee993db48d34a4e344f4d8b7e99119168bdd7");

    public static void a(String str) {
        b = str;
        if (((str.hashCode() == 3331 && str.equals("hk")) ? (char) 0 : 65535) != 0) {
            a = Collections.singletonList(c);
        } else {
            a = Collections.singletonList(d);
        }
    }

    public static String a() {
        return b;
    }

    public static boolean b() {
        return TextUtils.isEmpty(b) || TextUtils.equals("cn", b);
    }
}
