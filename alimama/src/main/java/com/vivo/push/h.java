package com.vivo.push;

/* compiled from: LocalAliasTagsManager */
final class h implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ LocalAliasTagsManager b;

    h(LocalAliasTagsManager localAliasTagsManager, String str) {
        this.b = localAliasTagsManager;
        this.a = str;
    }

    public final void run() {
        if (this.b.mSubscribeAppAliasManager.delAlias(this.a)) {
            p.a().b(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, this.a);
        }
    }
}
