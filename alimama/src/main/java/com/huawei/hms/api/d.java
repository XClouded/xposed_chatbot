package com.huawei.hms.api;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

/* compiled from: BindingFailedResolveMgr */
class d {
    static final d a = new d();
    List<Activity> b = new ArrayList(1);

    d() {
    }

    /* access modifiers changed from: package-private */
    public void a(Activity activity) {
        for (Activity next : this.b) {
            if (!(next == null || next == activity || next.isFinishing())) {
                next.finish();
            }
        }
        this.b.add(activity);
    }

    /* access modifiers changed from: package-private */
    public void b(Activity activity) {
        this.b.remove(activity);
    }
}
