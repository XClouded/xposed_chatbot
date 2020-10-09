package com.uc.webview.export.business.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.BaseSetupTask;
import java.util.HashMap;

/* compiled from: U4Source */
final class l extends HashMap<String, Pair<ValueCallback<BaseSetupTask>, ValueCallback<BaseSetupTask>>> {
    final /* synthetic */ a a;

    l(a aVar) {
        this.a = aVar;
        put(UCCore.EVENT_EXCEPTION, new Pair((Object) null, this.a.i));
        put("die_delegate", new Pair((Object) null, this.a.j));
        put(UCCore.EVENT_INIT_CORE_SUCCESS, new Pair((Object) null, this.a.h));
        put(UCCore.LEGACY_EVENT_SETUP, new Pair((Object) null, this.a.k));
    }
}
