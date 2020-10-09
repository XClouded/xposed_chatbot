package com.uc.webview.export.utility;

import java.util.Formatter;
import java.util.Locale;

/* compiled from: U4Source */
final class a extends ThreadLocal<Formatter> {
    StringBuilder a = new StringBuilder();

    a() {
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public synchronized Formatter initialValue() {
        return new Formatter(this.a, Locale.getDefault());
    }

    public final /* synthetic */ Object get() {
        Formatter formatter = (Formatter) super.get();
        this.a.setLength(0);
        return formatter;
    }
}
