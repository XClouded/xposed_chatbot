package rx.internal.util;

import rx.Subscription;

public class SynchronizedSubscription implements Subscription {
    private final Subscription s;

    public SynchronizedSubscription(Subscription subscription) {
        this.s = subscription;
    }

    public synchronized void unsubscribe() {
        this.s.unsubscribe();
    }

    public synchronized boolean isUnsubscribed() {
        return this.s.isUnsubscribed();
    }
}
