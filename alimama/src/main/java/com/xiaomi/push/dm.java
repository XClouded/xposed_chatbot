package com.xiaomi.push;

import android.content.Context;
import android.content.IntentFilter;
import android.taobao.windvane.config.WVConfigManager;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.mpcd.receivers.BroadcastActionsReceiver;

public class dm {
    private static IntentFilter a() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme(WVConfigManager.CONFIGNAME_PACKAGE);
        return intentFilter;
    }

    /* renamed from: a  reason: collision with other method in class */
    private static dr m177a() {
        return new dn();
    }

    public static void a(Context context) {
        ds.a(context).a();
        try {
            context.registerReceiver(new BroadcastActionsReceiver(a()), a());
        } catch (Throwable th) {
            b.a(th);
        }
    }
}
