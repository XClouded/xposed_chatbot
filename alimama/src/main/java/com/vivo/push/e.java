package com.vivo.push;

import java.util.HashSet;
import java.util.List;

/* compiled from: LocalAliasTagsManager */
final class e implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ LocalAliasTagsManager b;

    e(LocalAliasTagsManager localAliasTagsManager, List list) {
        this.b = localAliasTagsManager;
        this.a = list;
    }

    public final void run() {
        if (this.a != null && this.a.size() > 0) {
            HashSet hashSet = new HashSet();
            for (String add : this.a) {
                hashSet.add(add);
            }
            this.b.mSubscribeAppTagManager.setTagsSuccess(hashSet);
        }
    }
}
