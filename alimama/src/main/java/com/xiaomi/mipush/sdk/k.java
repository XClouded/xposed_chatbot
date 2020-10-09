package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;

final class k implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ f f68a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f69a;

    k(String str, Context context, f fVar) {
        this.f69a = str;
        this.a = context;
        this.f68a = fVar;
    }

    public void run() {
        if (!TextUtils.isEmpty(this.f69a)) {
            String str = "";
            String[] split = this.f69a.split(Constants.WAVE_SEPARATOR);
            int length = split.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String str2 = split[i];
                if (!TextUtils.isEmpty(str2) && str2.startsWith("token:")) {
                    str = str2.substring(str2.indexOf(":") + 1);
                    break;
                }
                i++;
            }
            if (!TextUtils.isEmpty(str)) {
                b.a("ASSEMBLE_PUSH : receive correct token");
                j.d(this.a, this.f68a, str);
                j.a(this.a);
                return;
            }
            b.a("ASSEMBLE_PUSH : receive incorrect token");
        }
    }
}
