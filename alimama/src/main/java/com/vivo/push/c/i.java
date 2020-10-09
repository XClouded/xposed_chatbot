package com.vivo.push.c;

import java.util.List;

/* compiled from: OnDelTagsReceiveTask */
final class i implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ List b;
    final /* synthetic */ List c;
    final /* synthetic */ String d;
    final /* synthetic */ h e;

    i(h hVar, int i, List list, List list2, String str) {
        this.e = hVar;
        this.a = i;
        this.b = list;
        this.c = list2;
        this.d = str;
    }

    public final void run() {
        this.e.b.onDelTags(this.e.a, this.a, this.b, this.c, this.d);
    }
}
