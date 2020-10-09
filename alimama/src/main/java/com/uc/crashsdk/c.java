package com.uc.crashsdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.Iterator;
import java.util.Map;

/* compiled from: ProGuard */
final class c implements Application.ActivityLifecycleCallbacks {
    private boolean a = false;
    private boolean b = false;

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    c() {
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        a(activity, 2);
    }

    public final void onActivityStarted(Activity activity) {
        a(activity, 1);
    }

    public final void onActivityResumed(Activity activity) {
        a(activity, 1);
    }

    public final void onActivityPaused(Activity activity) {
        a(activity, 2);
    }

    public final void onActivityStopped(Activity activity) {
        a(activity, 2);
    }

    public final void onActivityDestroyed(Activity activity) {
        if (h.I()) {
            boolean unused = b.S = true;
            synchronized (b.R) {
                b.R.remove(activity);
                a(2);
            }
        }
    }

    private void a(Activity activity, int i) {
        if (h.I()) {
            boolean unused = b.S = true;
            synchronized (b.R) {
                b.R.put(activity, Integer.valueOf(i));
                a(i);
            }
        }
    }

    private void a(int i) {
        boolean z = 1 == i;
        if (!z) {
            Iterator it = b.R.entrySet().iterator();
            while (true) {
                if (it.hasNext()) {
                    Object value = ((Map.Entry) it.next()).getValue();
                    if (value != null && ((Integer) value).intValue() == 1) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (this.a != z) {
            b.a(z);
            this.a = z;
        }
        boolean isEmpty = b.R.isEmpty();
        if (this.b != isEmpty) {
            if (isEmpty) {
                b.t();
            }
            this.b = isEmpty;
        }
    }
}
