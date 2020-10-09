package com.xiaomi.mipush.sdk;

import com.xiaomi.push.dm;

final class af implements Runnable {
    af() {
    }

    public void run() {
        dm.a(MiPushClient.sContext);
    }
}
