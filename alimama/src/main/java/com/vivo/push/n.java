package com.vivo.push;

import java.util.List;

/* compiled from: LocalAliasTagsManager */
final class n implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ LocalAliasTagsManager b;

    n(LocalAliasTagsManager localAliasTagsManager, List list) {
        this.b = localAliasTagsManager;
        this.a = list;
    }

    public final void run() {
        if (this.a != null && this.a.size() > 0) {
            this.b.mSubscribeAppAliasManager.setAliasSuccess((String) this.a.get(0));
        }
    }
}
