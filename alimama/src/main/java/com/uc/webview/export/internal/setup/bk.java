package com.uc.webview.export.internal.setup;

import com.uc.webview.export.cyclone.UCLoader;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.bf;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class bk implements Callable<Object> {
    final /* synthetic */ bf.a a;

    bk(bf.a aVar) {
        this.a = aVar;
    }

    public final Object call() throws Exception {
        this.a.e.mSdkShellClassLoader = new UCLoader((String) this.a.e.sdkShellModule.first, (String) this.a.e.sdkShellModule.second, this.a.e.soDirPath, bf.class.getClassLoader());
        return Integer.valueOf(ae.e.c);
    }
}
