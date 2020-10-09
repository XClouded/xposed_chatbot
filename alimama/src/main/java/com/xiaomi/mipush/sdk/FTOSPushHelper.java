package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.huawei.hms.support.api.push.PushReceiver;
import com.xiaomi.channel.commonutils.logger.b;
import java.util.Map;

public class FTOSPushHelper {
    private static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    private static volatile boolean f15a = false;

    private static void a(Context context) {
        AbstractPushManager a2 = g.a(context).a(f.ASSEMBLE_PUSH_FTOS);
        if (a2 != null) {
            b.a("ASSEMBLE_PUSH :  register fun touch os when network change!");
            a2.register();
        }
    }

    public static void doInNetworkChange(Context context) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (!getNeedRegister()) {
            return;
        }
        if (a <= 0 || a + 300000 <= elapsedRealtime) {
            a = elapsedRealtime;
            a(context);
        }
    }

    public static boolean getNeedRegister() {
        return f15a;
    }

    public static boolean hasNetwork(Context context) {
        return j.a(context);
    }

    public static void notifyFTOSNotificationClicked(Context context, Map<String, String> map) {
        PushMessageReceiver a2;
        if (map != null && map.containsKey(PushReceiver.BOUND_KEY.pushMsgKey)) {
            String str = map.get(PushReceiver.BOUND_KEY.pushMsgKey);
            if (!TextUtils.isEmpty(str) && (a2 = j.a(context)) != null) {
                MiPushMessage a3 = j.a(str);
                if (!a3.getExtra().containsKey("notify_effect")) {
                    a2.onNotificationMessageClicked(context, a3);
                }
            }
        }
    }

    public static void setNeedRegister(boolean z) {
        f15a = z;
    }

    public static void uploadToken(Context context, String str) {
        j.a(context, f.ASSEMBLE_PUSH_FTOS, str);
    }
}
