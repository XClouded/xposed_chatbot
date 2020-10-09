package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.xiaomi.channel.commonutils.logger.b;

public class COSPushHelper {
    private static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    private static volatile boolean f14a = false;

    public static void convertMessage(Intent intent) {
        j.a(intent);
    }

    public static void doInNetworkChange(Context context) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (!getNeedRegister()) {
            return;
        }
        if (a <= 0 || a + 300000 <= elapsedRealtime) {
            a = elapsedRealtime;
            registerCOSAssemblePush(context);
        }
    }

    public static boolean getNeedRegister() {
        return f14a;
    }

    public static boolean hasNetwork(Context context) {
        return j.a(context);
    }

    public static void onNotificationMessageCome(Context context, String str) {
    }

    public static void onPassThoughMessageCome(Context context, String str) {
    }

    public static void registerCOSAssemblePush(Context context) {
        AbstractPushManager a2 = g.a(context).a(f.ASSEMBLE_PUSH_COS);
        if (a2 != null) {
            b.a("ASSEMBLE_PUSH :  register cos when network change!");
            a2.register();
        }
    }

    public static synchronized void setNeedRegister(boolean z) {
        synchronized (COSPushHelper.class) {
            f14a = z;
        }
    }

    public static void uploadToken(Context context, String str) {
        j.a(context, f.ASSEMBLE_PUSH_COS, str);
    }
}
