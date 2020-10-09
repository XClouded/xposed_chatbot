package com.uc.webview.export.internal.utility;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.uc.webview.export.internal.utility.i;
import java.lang.Thread;

/* compiled from: U4Source */
final class j implements Thread.UncaughtExceptionHandler {
    final /* synthetic */ String a;
    final /* synthetic */ i.b b;

    j(i.b bVar, String str) {
        this.b = bVar;
        this.a = str;
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        String c = i.a;
        Log.d(c, this.a + " uncaughtException " + thread + AVFSCacheConstants.COMMA_SEP + k.a(th));
    }
}
