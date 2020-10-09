package com.vivo.push.util;

import android.content.Context;
import android.text.TextUtils;
import com.vivo.push.b.aa;
import com.vivo.push.p;
import com.vivo.push.y;
import java.util.HashMap;

/* compiled from: ClientReportUtil */
public final class d {
    public static boolean a(Context context, long j, long j2) {
        p.d("ClientReportUtil", "report message: " + j + ", reportType: " + j2);
        aa aaVar = new aa(j2);
        HashMap hashMap = new HashMap();
        hashMap.put("messageID", String.valueOf(j));
        String b = z.b(context, context.getPackageName());
        if (!TextUtils.isEmpty(b)) {
            hashMap.put("remoteAppId", b);
        }
        aaVar.a(hashMap);
        p.a().a((y) aaVar);
        return true;
    }
}
