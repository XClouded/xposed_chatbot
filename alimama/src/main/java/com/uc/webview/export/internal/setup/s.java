package com.uc.webview.export.internal.setup;

import java.util.HashMap;

/* compiled from: U4Source */
final class s extends HashMap<String, String> {
    final /* synthetic */ boolean a;
    final /* synthetic */ o b;

    s(o oVar, boolean z) {
        this.b = oVar;
        this.a = z;
        put("vmsize_saving_enable", this.a ? "true" : "false");
    }
}
