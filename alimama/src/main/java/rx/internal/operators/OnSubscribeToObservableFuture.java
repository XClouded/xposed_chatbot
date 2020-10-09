package rx.internal.operators;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public final class OnSubscribeToObservableFuture {
    private OnSubscribeToObservableFuture() {
        throw new IllegalStateException("No instances!");
    }

    static class ToObservableFuture<T> implements Observable.OnSubscribe<T> {
        /* access modifiers changed from: private */
        public final Future<? extends T> that;
        private final long time;
        private final TimeUnit unit;

        public ToObservableFuture(Future<? extends T> future) {
            this.that = future;
            this.time = 0;
            this.unit = null;
        }

        public ToObservableFuture(Future<? extends T> future, long j, TimeUnit timeUnit) {
            this.that = future;
            this.time = j;
            this.unit = timeUnit;
        }

        public void call(Subscriber<? super T> subscriber) {
            subscriber.add(Subscriptions.create(new Action0() {
                public void call() {
                    ToObservableFuture.this.that.cancel(true);
                }
            }));
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(this.unit == null ? this.that.get() : this.that.get(this.time, this.unit));
                    subscriber.onCompleted();
                }
            } catch (Throwable th) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(th);
                }
            }
        }
    }

    public static <T> Observable.OnSubscribe<T> toObservableFuture(Future<? extends T> future) {
        return new ToObservableFuture(future);
    }

    public static <T> Observable.OnSubscribe<T> toObservableFuture(Future<? extends T> future, long j, TimeUnit timeUnit) {
        return new ToObservableFuture(future, j, timeUnit);
    }
}
