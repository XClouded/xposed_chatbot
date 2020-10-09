package com.vivo.push;

import java.util.List;

/* compiled from: LocalAliasTagsManager */
final class l implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ LocalAliasTagsManager b;

    l(LocalAliasTagsManager localAliasTagsManager, List list) {
        this.b = localAliasTagsManager;
        this.a = list;
    }

    public final void run() {
        if (this.a != null && this.a.size() > 0) {
            this.b.mSubscribeAppAliasManager.delAliasSuccess((String) this.a.get(0));
        }
    }
}
