package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;

public final class OperatorThrottleFirst<T> implements Observable.Operator<T, T> {
    /* access modifiers changed from: private */
    public final Scheduler scheduler;
    /* access modifiers changed from: private */
    public final long timeInMilliseconds;

    public OperatorThrottleFirst(long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.timeInMilliseconds = timeUnit.toMillis(j);
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            private long lastOnNext = 0;

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T t) {
                long now = OperatorThrottleFirst.this.scheduler.now();
                if (this.lastOnNext == 0 || now - this.lastOnNext >= OperatorThrottleFirst.this.timeInMilliseconds) {
                    this.lastOnNext = now;
                    subscriber.onNext(t);
                }
            }

            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }
        };
    }
}
