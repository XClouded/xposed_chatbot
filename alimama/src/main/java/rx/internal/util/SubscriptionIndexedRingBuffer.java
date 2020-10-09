package rx.internal.util;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Subscription;
import rx.functions.Func1;

public final class SubscriptionIndexedRingBuffer<T extends Subscription> implements Subscription {
    private static final Func1<Subscription, Boolean> UNSUBSCRIBE = new Func1<Subscription, Boolean>() {
        public Boolean call(Subscription subscription) {
            subscription.unsubscribe();
            return Boolean.TRUE;
        }
    };
    private static final AtomicIntegerFieldUpdater<SubscriptionIndexedRingBuffer> UNSUBSCRIBED = AtomicIntegerFieldUpdater.newUpdater(SubscriptionIndexedRingBuffer.class, "unsubscribed");
    private volatile IndexedRingBuffer<T> subscriptions = IndexedRingBuffer.getInstance();
    private volatile int unsubscribed = 0;

    public boolean isUnsubscribed() {
        return this.unsubscribed == 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0019, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int add(T r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            int r0 = r3.unsubscribed     // Catch:{ all -> 0x0020 }
            r1 = 1
            if (r0 == r1) goto L_0x001a
            rx.internal.util.IndexedRingBuffer<T> r0 = r3.subscriptions     // Catch:{ all -> 0x0020 }
            if (r0 != 0) goto L_0x000b
            goto L_0x001a
        L_0x000b:
            rx.internal.util.IndexedRingBuffer<T> r0 = r3.subscriptions     // Catch:{ all -> 0x0020 }
            int r0 = r0.add(r4)     // Catch:{ all -> 0x0020 }
            int r2 = r3.unsubscribed     // Catch:{ all -> 0x0020 }
            if (r2 != r1) goto L_0x0018
            r4.unsubscribe()     // Catch:{ all -> 0x0020 }
        L_0x0018:
            monitor-exit(r3)
            return r0
        L_0x001a:
            r4.unsubscribe()     // Catch:{ all -> 0x0020 }
            r4 = -1
            monitor-exit(r3)
            return r4
        L_0x0020:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.SubscriptionIndexedRingBuffer.add(rx.Subscription):int");
    }

    public void remove(int i) {
        Subscription subscription;
        if (this.unsubscribed != 1 && this.subscriptions != null && i >= 0 && (subscription = (Subscription) this.subscriptions.remove(i)) != null && subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void removeSilently(int i) {
        if (this.unsubscribed != 1 && this.subscriptions != null && i >= 0) {
            this.subscriptions.remove(i);
        }
    }

    public void unsubscribe() {
        if (UNSUBSCRIBED.compareAndSet(this, 0, 1) && this.subscriptions != null) {
            unsubscribeFromAll(this.subscriptions);
            IndexedRingBuffer<T> indexedRingBuffer = this.subscriptions;
            this.subscriptions = null;
            indexedRingBuffer.unsubscribe();
        }
    }

    public int forEach(Func1<T, Boolean> func1) {
        return forEach(func1, 0);
    }

    public synchronized int forEach(Func1<T, Boolean> func1, int i) {
        if (this.unsubscribed != 1) {
            if (this.subscriptions != null) {
                return this.subscriptions.forEach(func1, i);
            }
        }
        return 0;
    }

    private static void unsubscribeFromAll(IndexedRingBuffer<? extends Subscription> indexedRingBuffer) {
        if (indexedRingBuffer != null) {
            indexedRingBuffer.forEach(UNSUBSCRIBE);
        }
    }
}
