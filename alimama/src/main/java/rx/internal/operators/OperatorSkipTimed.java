package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorSkipTimed<T> implements Observable.Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public OperatorSkipTimed(long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        Scheduler.Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        createWorker.schedule(new Action0() {
            public void call() {
                atomicBoolean.set(true);
            }
        }, this.time, this.unit);
        return new Subscriber<T>(subscriber) {
            public void onNext(T t) {
                if (atomicBoolean.get()) {
                    subscriber.onNext(t);
                }
            }

            public void onError(Throwable th) {
                try {
                    subscriber.onError(th);
                } finally {
                    unsubscribe();
                }
            }

            public void onCompleted() {
                try {
                    subscriber.onCompleted();
                } finally {
                    unsubscribe();
                }
            }
        };
    }
}
