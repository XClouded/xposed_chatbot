package com.xiaomi.mipush.sdk;

import com.xiaomi.mipush.sdk.MiPushClient;

final class ae implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;

    ae(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public void run() {
        MiPushClient.initialize(MiPushClient.sContext, this.a, this.b, (MiPushClient.MiPushClientCallback) null);
    }
}
