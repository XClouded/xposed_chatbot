package rx.internal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import rx.Subscription;
import rx.exceptions.Exceptions;

public final class SubscriptionRandomList<T extends Subscription> implements Subscription {
    private Set<T> subscriptions;
    private boolean unsubscribed = false;

    public synchronized boolean isUnsubscribed() {
        return this.unsubscribed;
    }

    public void add(T t) {
        synchronized (this) {
            if (!this.unsubscribed) {
                if (this.subscriptions == null) {
                    this.subscriptions = new HashSet(4);
                }
                this.subscriptions.add(t);
                t = null;
            }
        }
        if (t != null) {
            t.unsubscribe();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
        r2.unsubscribe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0011, code lost:
        if (r0 == false) goto L_?;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(rx.Subscription r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.unsubscribed     // Catch:{ all -> 0x0019 }
            if (r0 != 0) goto L_0x0017
            java.util.Set<T> r0 = r1.subscriptions     // Catch:{ all -> 0x0019 }
            if (r0 != 0) goto L_0x000a
            goto L_0x0017
        L_0x000a:
            java.util.Set<T> r0 = r1.subscriptions     // Catch:{ all -> 0x0019 }
            boolean r0 = r0.remove(r2)     // Catch:{ all -> 0x0019 }
            monitor-exit(r1)     // Catch:{ all -> 0x0019 }
            if (r0 == 0) goto L_0x0016
            r2.unsubscribe()
        L_0x0016:
            return
        L_0x0017:
            monitor-exit(r1)     // Catch:{ all -> 0x0019 }
            return
        L_0x0019:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0019 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.SubscriptionRandomList.remove(rx.Subscription):void");
    }

    public void clear() {
        synchronized (this) {
            if (!this.unsubscribed) {
                if (this.subscriptions != null) {
                    Set<T> set = this.subscriptions;
                    this.subscriptions = null;
                    unsubscribeFromAll(set);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
        if (r2 >= r1) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0018, code lost:
        r5.call(r0[r2]);
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
        r1 = r0.length;
        r2 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void forEach(rx.functions.Action1<T> r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.unsubscribed     // Catch:{ all -> 0x0023 }
            if (r0 != 0) goto L_0x0021
            java.util.Set<T> r0 = r4.subscriptions     // Catch:{ all -> 0x0023 }
            if (r0 != 0) goto L_0x000a
            goto L_0x0021
        L_0x000a:
            java.util.Set<T> r0 = r4.subscriptions     // Catch:{ all -> 0x0023 }
            r1 = 0
            java.lang.Object[] r0 = r0.toArray(r1)     // Catch:{ all -> 0x0023 }
            rx.Subscription[] r0 = (rx.Subscription[]) r0     // Catch:{ all -> 0x0023 }
            monitor-exit(r4)     // Catch:{ all -> 0x0023 }
            int r1 = r0.length
            r2 = 0
        L_0x0016:
            if (r2 >= r1) goto L_0x0020
            r3 = r0[r2]
            r5.call(r3)
            int r2 = r2 + 1
            goto L_0x0016
        L_0x0020:
            return
        L_0x0021:
            monitor-exit(r4)     // Catch:{ all -> 0x0023 }
            return
        L_0x0023:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0023 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.SubscriptionRandomList.forEach(rx.functions.Action1):void");
    }

    public void unsubscribe() {
        synchronized (this) {
            if (!this.unsubscribed) {
                this.unsubscribed = true;
                Set<T> set = this.subscriptions;
                this.subscriptions = null;
                unsubscribeFromAll(set);
            }
        }
    }

    private static <T extends Subscription> void unsubscribeFromAll(Collection<T> collection) {
        if (collection != null) {
            ArrayList arrayList = null;
            for (T unsubscribe : collection) {
                try {
                    unsubscribe.unsubscribe();
                } catch (Throwable th) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(th);
                }
            }
            Exceptions.throwIfAny(arrayList);
        }
    }
}
