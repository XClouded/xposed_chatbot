package rx.internal.operators;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

class OperatorTimeoutBase<T> implements Observable.Operator<T, T> {
    private final FirstTimeoutStub<T> firstTimeoutStub;
    private final Observable<? extends T> other;
    private final Scheduler scheduler;
    private final TimeoutStub<T> timeoutStub;

    interface FirstTimeoutStub<T> extends Func3<TimeoutSubscriber<T>, Long, Scheduler.Worker, Subscription> {
    }

    interface TimeoutStub<T> extends Func4<TimeoutSubscriber<T>, Long, T, Scheduler.Worker, Subscription> {
    }

    OperatorTimeoutBase(FirstTimeoutStub<T> firstTimeoutStub2, TimeoutStub<T> timeoutStub2, Observable<? extends T> observable, Scheduler scheduler2) {
        this.firstTimeoutStub = firstTimeoutStub2;
        this.timeoutStub = timeoutStub2;
        this.other = observable;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Scheduler.Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        SerialSubscription serialSubscription = new SerialSubscription();
        subscriber.add(serialSubscription);
        TimeoutSubscriber timeoutSubscriber = new TimeoutSubscriber(new SerializedSubscriber(subscriber), this.timeoutStub, serialSubscription, this.other, createWorker);
        serialSubscription.set((Subscription) this.firstTimeoutStub.call(timeoutSubscriber, 0L, createWorker));
        return timeoutSubscriber;
    }

    static final class TimeoutSubscriber<T> extends Subscriber<T> {
        static final AtomicLongFieldUpdater<TimeoutSubscriber> ACTUAL_UPDATER = AtomicLongFieldUpdater.newUpdater(TimeoutSubscriber.class, "actual");
        static final AtomicIntegerFieldUpdater<TimeoutSubscriber> TERMINATED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(TimeoutSubscriber.class, "terminated");
        volatile long actual;
        private final Object gate;
        private final Scheduler.Worker inner;
        private final Observable<? extends T> other;
        private final SerialSubscription serial;
        private final SerializedSubscriber<T> serializedSubscriber;
        volatile int terminated;
        private final TimeoutStub<T> timeoutStub;

        private TimeoutSubscriber(SerializedSubscriber<T> serializedSubscriber2, TimeoutStub<T> timeoutStub2, SerialSubscription serialSubscription, Observable<? extends T> observable, Scheduler.Worker worker) {
            super(serializedSubscriber2);
            this.gate = new Object();
            this.serializedSubscriber = serializedSubscriber2;
            this.timeoutStub = timeoutStub2;
            this.serial = serialSubscription;
            this.other = observable;
            this.inner = worker;
        }

        public void onNext(T t) {
            boolean z;
            synchronized (this.gate) {
                if (this.terminated == 0) {
                    ACTUAL_UPDATER.incrementAndGet(this);
                    z = true;
                } else {
                    z = false;
                }
            }
            if (z) {
                this.serializedSubscriber.onNext(t);
                this.serial.set((Subscription) this.timeoutStub.call(this, Long.valueOf(this.actual), t, this.inner));
            }
        }

        public void onError(Throwable th) {
            boolean z;
            synchronized (this.gate) {
                z = true;
                if (TERMINATED_UPDATER.getAndSet(this, 1) != 0) {
                    z = false;
                }
            }
            if (z) {
                this.serial.unsubscribe();
                this.serializedSubscriber.onError(th);
            }
        }

        public void onCompleted() {
            boolean z;
            synchronized (this.gate) {
                z = true;
                if (TERMINATED_UPDATER.getAndSet(this, 1) != 0) {
                    z = false;
                }
            }
            if (z) {
                this.serial.unsubscribe();
                this.serializedSubscriber.onCompleted();
            }
        }

        public void onTimeout(long j) {
            boolean z;
            synchronized (this.gate) {
                z = true;
                if (j != this.actual || TERMINATED_UPDATER.getAndSet(this, 1) != 0) {
                    z = false;
                }
            }
            if (!z) {
                return;
            }
            if (this.other == null) {
                this.serializedSubscriber.onError(new TimeoutException());
                return;
            }
            this.other.unsafeSubscribe(this.serializedSubscriber);
            this.serial.set(this.serializedSubscriber);
        }
    }
}
