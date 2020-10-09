package rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

public final class OnSubscribeFromIterable<T> implements Observable.OnSubscribe<T> {
    final Iterable<? extends T> is;

    public OnSubscribeFromIterable(Iterable<? extends T> iterable) {
        if (iterable != null) {
            this.is = iterable;
            return;
        }
        throw new NullPointerException("iterable must not be null");
    }

    public void call(Subscriber<? super T> subscriber) {
        Iterator<? extends T> it = this.is.iterator();
        if (it.hasNext() || subscriber.isUnsubscribed()) {
            subscriber.setProducer(new IterableProducer(subscriber, it));
        } else {
            subscriber.onCompleted();
        }
    }

    private static final class IterableProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -8730475647105475802L;
        private final Iterator<? extends T> it;
        private final Subscriber<? super T> o;

        private IterableProducer(Subscriber<? super T> subscriber, Iterator<? extends T> it2) {
            this.o = subscriber;
            this.it = it2;
        }

        public void request(long j) {
            if (get() != Long.MAX_VALUE) {
                if (j == Long.MAX_VALUE && compareAndSet(0, Long.MAX_VALUE)) {
                    fastpath();
                } else if (j > 0 && BackpressureUtils.getAndAddRequest(this, j) == 0) {
                    slowpath(j);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void slowpath(long j) {
            Subscriber<? super T> subscriber = this.o;
            Iterator<? extends T> it2 = this.it;
            do {
                long j2 = j;
                while (!subscriber.isUnsubscribed()) {
                    if (it2.hasNext()) {
                        j2--;
                        if (j2 >= 0) {
                            subscriber.onNext(it2.next());
                        } else {
                            j = addAndGet(-j);
                        }
                    } else if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                        return;
                    } else {
                        return;
                    }
                }
                return;
            } while (j != 0);
        }

        /* access modifiers changed from: package-private */
        public void fastpath() {
            Subscriber<? super T> subscriber = this.o;
            Iterator<? extends T> it2 = this.it;
            while (!subscriber.isUnsubscribed()) {
                if (it2.hasNext()) {
                    subscriber.onNext(it2.next());
                } else if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                    return;
                } else {
                    return;
                }
            }
        }
    }
}
