package rx.internal.operators;

import java.util.ArrayDeque;
import rx.Observable;
import rx.Subscriber;

public final class OperatorTakeLast<T> implements Observable.Operator<T, T> {
    /* access modifiers changed from: private */
    public final int count;

    public OperatorTakeLast(int i) {
        if (i >= 0) {
            this.count = i;
            return;
        }
        throw new IndexOutOfBoundsException("count cannot be negative");
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final ArrayDeque arrayDeque = new ArrayDeque();
        final NotificationLite instance = NotificationLite.instance();
        final TakeLastQueueProducer takeLastQueueProducer = new TakeLastQueueProducer(instance, arrayDeque, subscriber);
        subscriber.setProducer(takeLastQueueProducer);
        final Subscriber<? super T> subscriber2 = subscriber;
        return new Subscriber<T>(subscriber) {
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onCompleted() {
                arrayDeque.offer(instance.completed());
                takeLastQueueProducer.startEmitting();
            }

            public void onError(Throwable th) {
                arrayDeque.clear();
                subscriber2.onError(th);
            }

            public void onNext(T t) {
                if (OperatorTakeLast.this.count != 0) {
                    if (arrayDeque.size() == OperatorTakeLast.this.count) {
                        arrayDeque.removeFirst();
                    }
                    arrayDeque.offerLast(instance.next(t));
                }
            }
        };
    }
}
