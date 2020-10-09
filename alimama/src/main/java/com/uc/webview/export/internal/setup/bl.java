package com.uc.webview.export.internal.setup;

import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.setup.bf;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.k;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
final class bl implements Callable<Object> {
    final /* synthetic */ bf.a a;

    bl(bf.a aVar) {
        this.a = aVar;
    }

    public final Object call() throws Exception {
        b.a(29);
        g.a((String) this.a.f.getOption("core_ver_excludes"), this.a.e.mSdkShellClassLoader, "com.uc.webview.browser.shell.Build", "CORE_VERSION", CDParamKeys.CD_VALUE_STRING_SPLITER);
        g.a((String) this.a.f.getOption("sdk_ver_excludes"), this.a.e.mSdkShellClassLoader, "com.uc.webview.browser.shell.Build$Version", "NAME", CDParamKeys.CD_VALUE_STRING_SPLITER);
        if (k.a((Boolean) this.a.f.getOption(UCCore.OPTION_SHARE_CORE_SETUP_TASK_FLAG))) {
            g.b(UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_UCM_VERSIONS), this.a.e.mSdkShellClassLoader, "com.uc.webview.browser.shell.Build$Version", "NAME", CDParamKeys.CD_VALUE_STRING_SPLITER);
        }
        b.a(213);
        g.a(this.a.e, af.a, this.a.e.mSdkShellClassLoader, this.a.f.mOptions);
        return Integer.valueOf(ae.e.c);
    }
}
