package rx.subscriptions;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Subscription;

public final class RefCountSubscription implements Subscription {
    static final State EMPTY_STATE = new State(false, 0);
    static final AtomicReferenceFieldUpdater<RefCountSubscription, State> STATE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(RefCountSubscription.class, State.class, "state");
    private final Subscription actual;
    volatile State state = EMPTY_STATE;

    private static final class State {
        final int children;
        final boolean isUnsubscribed;

        State(boolean z, int i) {
            this.isUnsubscribed = z;
            this.children = i;
        }

        /* access modifiers changed from: package-private */
        public State addChild() {
            return new State(this.isUnsubscribed, this.children + 1);
        }

        /* access modifiers changed from: package-private */
        public State removeChild() {
            return new State(this.isUnsubscribed, this.children - 1);
        }

        /* access modifiers changed from: package-private */
        public State unsubscribe() {
            return new State(true, this.children);
        }
    }

    public RefCountSubscription(Subscription subscription) {
        if (subscription != null) {
            this.actual = subscription;
            return;
        }
        throw new IllegalArgumentException("s");
    }

    public Subscription get() {
        State state2;
        do {
            state2 = this.state;
            if (state2.isUnsubscribed) {
                return Subscriptions.unsubscribed();
            }
        } while (!STATE_UPDATER.compareAndSet(this, state2, state2.addChild()));
        return new InnerSubscription(this);
    }

    public boolean isUnsubscribed() {
        return this.state.isUnsubscribed;
    }

    public void unsubscribe() {
        State state2;
        State unsubscribe;
        do {
            state2 = this.state;
            if (!state2.isUnsubscribed) {
                unsubscribe = state2.unsubscribe();
            } else {
                return;
            }
        } while (!STATE_UPDATER.compareAndSet(this, state2, unsubscribe));
        unsubscribeActualIfApplicable(unsubscribe);
    }

    private void unsubscribeActualIfApplicable(State state2) {
        if (state2.isUnsubscribed && state2.children == 0) {
            this.actual.unsubscribe();
        }
    }

    /* access modifiers changed from: package-private */
    public void unsubscribeAChild() {
        State state2;
        State removeChild;
        do {
            state2 = this.state;
            removeChild = state2.removeChild();
        } while (!STATE_UPDATER.compareAndSet(this, state2, removeChild));
        unsubscribeActualIfApplicable(removeChild);
    }

    private static final class InnerSubscription implements Subscription {
        static final AtomicIntegerFieldUpdater<InnerSubscription> INNER_DONE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(InnerSubscription.class, "innerDone");
        volatile int innerDone;
        final RefCountSubscription parent;

        public InnerSubscription(RefCountSubscription refCountSubscription) {
            this.parent = refCountSubscription;
        }

        public void unsubscribe() {
            if (INNER_DONE_UPDATER.compareAndSet(this, 0, 1)) {
                this.parent.unsubscribeAChild();
            }
        }

        public boolean isUnsubscribed() {
            return this.innerDone != 0;
        }
    }
}
