package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;

public final class OperatorTakeLastTimed<T> implements Observable.Operator<T, T> {
    /* access modifiers changed from: private */
    public final long ageMillis;
    /* access modifiers changed from: private */
    public final int count;
    /* access modifiers changed from: private */
    public final Scheduler scheduler;

    public OperatorTakeLastTimed(long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.ageMillis = timeUnit.toMillis(j);
        this.scheduler = scheduler2;
        this.count = -1;
    }

    public OperatorTakeLastTimed(int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        if (i >= 0) {
            this.ageMillis = timeUnit.toMillis(j);
            this.scheduler = scheduler2;
            this.count = i;
            return;
        }
        throw new IndexOutOfBoundsException("count could not be negative");
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final ArrayDeque arrayDeque = new ArrayDeque();
        final ArrayDeque arrayDeque2 = new ArrayDeque();
        final NotificationLite instance = NotificationLite.instance();
        final TakeLastQueueProducer takeLastQueueProducer = new TakeLastQueueProducer(instance, arrayDeque, subscriber);
        subscriber.setProducer(takeLastQueueProducer);
        final Subscriber<? super T> subscriber2 = subscriber;
        return new Subscriber<T>(subscriber) {
            /* access modifiers changed from: protected */
            public void runEvictionPolicy(long j) {
                while (OperatorTakeLastTimed.this.count >= 0 && arrayDeque.size() > OperatorTakeLastTimed.this.count) {
                    arrayDeque2.pollFirst();
                    arrayDeque.pollFirst();
                }
                while (!arrayDeque.isEmpty() && ((Long) arrayDeque2.peekFirst()).longValue() < j - OperatorTakeLastTimed.this.ageMillis) {
                    arrayDeque2.pollFirst();
                    arrayDeque.pollFirst();
                }
            }

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T t) {
                long now = OperatorTakeLastTimed.this.scheduler.now();
                arrayDeque2.add(Long.valueOf(now));
                arrayDeque.add(instance.next(t));
                runEvictionPolicy(now);
            }

            public void onError(Throwable th) {
                arrayDeque2.clear();
                arrayDeque.clear();
                subscriber2.onError(th);
            }

            public void onCompleted() {
                runEvictionPolicy(OperatorTakeLastTimed.this.scheduler.now());
                arrayDeque2.clear();
                arrayDeque.offer(instance.completed());
                takeLastQueueProducer.startEmitting();
            }
        };
    }
}
