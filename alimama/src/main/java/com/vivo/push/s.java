package com.vivo.push;

/* compiled from: PushClientManager */
final class s implements IPushActionListener {
    final /* synthetic */ p a;

    s(p pVar) {
        this.a = pVar;
    }

    public final void onStateChanged(int i) {
        if (i == 0) {
            String unused = this.a.l = "";
            this.a.k.a("APP_TOKEN", "");
            this.a.t();
            this.a.k.c("APP_TAGS");
            return;
        }
        String unused2 = this.a.l = null;
        this.a.k.c("APP_TOKEN");
    }
}
