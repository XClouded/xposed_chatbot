package rx.observables;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.exceptions.CompositeException;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;

@Experimental
public abstract class AbstractOnSubscribe<T, S> implements Observable.OnSubscribe<T> {
    private static final Func1<Object, Object> NULL_FUNC1 = new Func1<Object, Object>() {
        public Object call(Object obj) {
            return null;
        }
    };

    /* access modifiers changed from: protected */
    public abstract void next(SubscriptionState<T, S> subscriptionState);

    /* access modifiers changed from: protected */
    public S onSubscribe(Subscriber<? super T> subscriber) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onTerminated(S s) {
    }

    public final void call(Subscriber<? super T> subscriber) {
        SubscriptionState subscriptionState = new SubscriptionState(subscriber, onSubscribe(subscriber));
        subscriber.add(new SubscriptionCompleter(subscriptionState));
        subscriber.setProducer(new SubscriptionProducer(subscriptionState));
    }

    public final Observable<T> toObservable() {
        return Observable.create(this);
    }

    public static <T, S> AbstractOnSubscribe<T, S> create(Action1<SubscriptionState<T, S>> action1) {
        return create(action1, NULL_FUNC1, Actions.empty());
    }

    public static <T, S> AbstractOnSubscribe<T, S> create(Action1<SubscriptionState<T, S>> action1, Func1<? super Subscriber<? super T>, ? extends S> func1) {
        return create(action1, func1, Actions.empty());
    }

    public static <T, S> AbstractOnSubscribe<T, S> create(Action1<SubscriptionState<T, S>> action1, Func1<? super Subscriber<? super T>, ? extends S> func1, Action1<? super S> action12) {
        return new LambdaOnSubscribe(action1, func1, action12);
    }

    private static final class LambdaOnSubscribe<T, S> extends AbstractOnSubscribe<T, S> {
        final Action1<SubscriptionState<T, S>> next;
        final Func1<? super Subscriber<? super T>, ? extends S> onSubscribe;
        final Action1<? super S> onTerminated;

        public /* bridge */ /* synthetic */ void call(Object obj) {
            AbstractOnSubscribe.super.call((Subscriber) obj);
        }

        private LambdaOnSubscribe(Action1<SubscriptionState<T, S>> action1, Func1<? super Subscriber<? super T>, ? extends S> func1, Action1<? super S> action12) {
            this.next = action1;
            this.onSubscribe = func1;
            this.onTerminated = action12;
        }

        /* access modifiers changed from: protected */
        public S onSubscribe(Subscriber<? super T> subscriber) {
            return this.onSubscribe.call(subscriber);
        }

        /* access modifiers changed from: protected */
        public void onTerminated(S s) {
            this.onTerminated.call(s);
        }

        /* access modifiers changed from: protected */
        public void next(SubscriptionState<T, S> subscriptionState) {
            this.next.call(subscriptionState);
        }
    }

    private static final class SubscriptionCompleter<T, S> extends AtomicBoolean implements Subscription {
        private static final long serialVersionUID = 7993888274897325004L;
        private final SubscriptionState<T, S> state;

        private SubscriptionCompleter(SubscriptionState<T, S> subscriptionState) {
            this.state = subscriptionState;
        }

        public boolean isUnsubscribed() {
            return get();
        }

        public void unsubscribe() {
            if (compareAndSet(false, true)) {
                this.state.free();
            }
        }
    }

    private static final class SubscriptionProducer<T, S> implements Producer {
        final SubscriptionState<T, S> state;

        private SubscriptionProducer(SubscriptionState<T, S> subscriptionState) {
            this.state = subscriptionState;
        }

        /* JADX WARNING: Removed duplicated region for block: B:12:0x003c A[LOOP:1: B:12:0x003c->B:17:0x005b, LOOP_START] */
        /* JADX WARNING: Removed duplicated region for block: B:6:0x001d A[LOOP:0: B:6:0x001d->B:9:0x002d, LOOP_START] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void request(long r6) {
            /*
                r5 = this;
                r0 = 0
                int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                if (r2 <= 0) goto L_0x005d
                rx.observables.AbstractOnSubscribe$SubscriptionState<T, S> r2 = r5.state
                java.util.concurrent.atomic.AtomicLong r2 = r2.requestCount
                long r2 = rx.internal.operators.BackpressureUtils.getAndAddRequest(r2, r6)
                int r4 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
                if (r4 != 0) goto L_0x005d
                r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r4 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
                if (r4 != 0) goto L_0x0030
            L_0x001d:
                rx.observables.AbstractOnSubscribe$SubscriptionState<T, S> r6 = r5.state
                rx.Subscriber r6 = r6.subscriber
                boolean r6 = r6.isUnsubscribed()
                if (r6 != 0) goto L_0x005d
                boolean r6 = r5.doNext()
                if (r6 != 0) goto L_0x001d
                goto L_0x005d
            L_0x0030:
                rx.observables.AbstractOnSubscribe$SubscriptionState<T, S> r6 = r5.state
                rx.Subscriber r6 = r6.subscriber
                boolean r6 = r6.isUnsubscribed()
                if (r6 != 0) goto L_0x005d
            L_0x003c:
                boolean r6 = r5.doNext()
                if (r6 != 0) goto L_0x0043
                goto L_0x005d
            L_0x0043:
                rx.observables.AbstractOnSubscribe$SubscriptionState<T, S> r6 = r5.state
                java.util.concurrent.atomic.AtomicLong r6 = r6.requestCount
                long r6 = r6.decrementAndGet()
                int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                if (r2 <= 0) goto L_0x005d
                rx.observables.AbstractOnSubscribe$SubscriptionState<T, S> r6 = r5.state
                rx.Subscriber r6 = r6.subscriber
                boolean r6 = r6.isUnsubscribed()
                if (r6 == 0) goto L_0x003c
            L_0x005d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.observables.AbstractOnSubscribe.SubscriptionProducer.request(long):void");
        }

        /* access modifiers changed from: protected */
        public boolean doNext() {
            if (!this.state.use()) {
                return false;
            }
            try {
                int phase = this.state.phase();
                this.state.parent.next(this.state);
                if (this.state.verify()) {
                    if (!this.state.accept()) {
                        if (!this.state.stopRequested()) {
                            SubscriptionState.access$708(this.state);
                            this.state.free();
                            return true;
                        }
                    }
                    this.state.terminate();
                    this.state.free();
                    return false;
                }
                throw new IllegalStateException("No event produced or stop called @ Phase: " + phase + " -> " + this.state.phase() + ", Calls: " + this.state.calls());
            } catch (Throwable th) {
                this.state.free();
                throw th;
            }
        }
    }

    public static final class SubscriptionState<T, S> {
        private long calls;
        private boolean hasCompleted;
        private boolean hasOnNext;
        private final AtomicInteger inUse;
        /* access modifiers changed from: private */
        public final AbstractOnSubscribe<T, S> parent;
        private int phase;
        /* access modifiers changed from: private */
        public final AtomicLong requestCount;
        private final S state;
        private boolean stopRequested;
        /* access modifiers changed from: private */
        public final Subscriber<? super T> subscriber;
        private Throwable theException;
        private T theValue;

        static /* synthetic */ long access$708(SubscriptionState subscriptionState) {
            long j = subscriptionState.calls;
            subscriptionState.calls = 1 + j;
            return j;
        }

        private SubscriptionState(AbstractOnSubscribe<T, S> abstractOnSubscribe, Subscriber<? super T> subscriber2, S s) {
            this.parent = abstractOnSubscribe;
            this.subscriber = subscriber2;
            this.state = s;
            this.requestCount = new AtomicLong();
            this.inUse = new AtomicInteger(1);
        }

        public S state() {
            return this.state;
        }

        public int phase() {
            return this.phase;
        }

        public void phase(int i) {
            this.phase = i;
        }

        public void advancePhase() {
            advancePhaseBy(1);
        }

        public void advancePhaseBy(int i) {
            this.phase += i;
        }

        public long calls() {
            return this.calls;
        }

        public void onNext(T t) {
            if (this.hasOnNext) {
                throw new IllegalStateException("onNext not consumed yet!");
            } else if (!this.hasCompleted) {
                this.theValue = t;
                this.hasOnNext = true;
            } else {
                throw new IllegalStateException("Already terminated", this.theException);
            }
        }

        public void onError(Throwable th) {
            if (th == null) {
                throw new NullPointerException("e != null required");
            } else if (!this.hasCompleted) {
                this.theException = th;
                this.hasCompleted = true;
            } else {
                throw new IllegalStateException("Already terminated", this.theException);
            }
        }

        public void onCompleted() {
            if (!this.hasCompleted) {
                this.hasCompleted = true;
                return;
            }
            throw new IllegalStateException("Already terminated", this.theException);
        }

        public void stop() {
            this.stopRequested = true;
        }

        /* access modifiers changed from: protected */
        public boolean accept() {
            if (this.hasOnNext) {
                T t = this.theValue;
                this.theValue = null;
                this.hasOnNext = false;
                try {
                    this.subscriber.onNext(t);
                } catch (Throwable th) {
                    this.hasCompleted = true;
                    Throwable th2 = this.theException;
                    this.theException = null;
                    if (th2 == null) {
                        this.subscriber.onError(th);
                    } else {
                        this.subscriber.onError(new CompositeException(Arrays.asList(new Throwable[]{th, th2})));
                    }
                    return true;
                }
            }
            if (!this.hasCompleted) {
                return false;
            }
            Throwable th3 = this.theException;
            this.theException = null;
            if (th3 != null) {
                this.subscriber.onError(th3);
            } else {
                this.subscriber.onCompleted();
            }
            return true;
        }

        /* access modifiers changed from: protected */
        public boolean verify() {
            return this.hasOnNext || this.hasCompleted || this.stopRequested;
        }

        /* access modifiers changed from: protected */
        public boolean stopRequested() {
            return this.stopRequested;
        }

        /* access modifiers changed from: protected */
        public boolean use() {
            int i = this.inUse.get();
            if (i == 0) {
                return false;
            }
            if (i == 1 && this.inUse.compareAndSet(1, 2)) {
                return true;
            }
            throw new IllegalStateException("This is not reentrant nor threadsafe!");
        }

        /* access modifiers changed from: protected */
        public void free() {
            if (this.inUse.get() > 0 && this.inUse.decrementAndGet() == 0) {
                this.parent.onTerminated(this.state);
            }
        }

        /* access modifiers changed from: protected */
        public void terminate() {
            int i;
            do {
                i = this.inUse.get();
                if (i <= 0) {
                    return;
                }
            } while (!this.inUse.compareAndSet(i, 0));
            this.parent.onTerminated(this.state);
        }
    }
}
