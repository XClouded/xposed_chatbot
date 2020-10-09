package rx.subscriptions;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Subscription;

public final class SerialSubscription implements Subscription {
    static final AtomicReferenceFieldUpdater<SerialSubscription, State> STATE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SerialSubscription.class, State.class, "state");
    volatile State state = new State(false, Subscriptions.empty());

    private static final class State {
        final boolean isUnsubscribed;
        final Subscription subscription;

        State(boolean z, Subscription subscription2) {
            this.isUnsubscribed = z;
            this.subscription = subscription2;
        }

        /* access modifiers changed from: package-private */
        public State unsubscribe() {
            return new State(true, this.subscription);
        }

        /* access modifiers changed from: package-private */
        public State set(Subscription subscription2) {
            return new State(this.isUnsubscribed, subscription2);
        }
    }

    public boolean isUnsubscribed() {
        return this.state.isUnsubscribed;
    }

    public void unsubscribe() {
        State state2;
        do {
            state2 = this.state;
            if (!state2.isUnsubscribed) {
            } else {
                return;
            }
        } while (!STATE_UPDATER.compareAndSet(this, state2, state2.unsubscribe()));
        state2.subscription.unsubscribe();
    }

    public void set(Subscription subscription) {
        State state2;
        if (subscription != null) {
            do {
                state2 = this.state;
                if (state2.isUnsubscribed) {
                    subscription.unsubscribe();
                    return;
                }
            } while (!STATE_UPDATER.compareAndSet(this, state2, state2.set(subscription)));
            state2.subscription.unsubscribe();
            return;
        }
        throw new IllegalArgumentException("Subscription can not be null");
    }

    public Subscription get() {
        return this.state.subscription;
    }
}
