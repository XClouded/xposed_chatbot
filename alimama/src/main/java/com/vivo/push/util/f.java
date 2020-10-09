package com.vivo.push.util;

import java.util.concurrent.ThreadFactory;

/* compiled from: ConcurrentUtils */
final class f implements ThreadFactory {
    final /* synthetic */ String a;

    f(String str) {
        this.a = str;
    }

    public final Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(this.a);
        thread.setDaemon(true);
        return thread;
    }
}
