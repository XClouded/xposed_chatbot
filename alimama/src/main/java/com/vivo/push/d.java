package com.vivo.push;

/* compiled from: LocalAliasTagsManager */
final class d implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ LocalAliasTagsManager b;

    d(LocalAliasTagsManager localAliasTagsManager, String str) {
        this.b = localAliasTagsManager;
        this.a = str;
    }

    public final void run() {
        if (this.b.mSubscribeAppAliasManager.setAlias(this.a)) {
            p.a().a(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, this.a);
        }
    }
}
