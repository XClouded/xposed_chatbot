package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

public final class OnSubscribeRange implements Observable.OnSubscribe<Integer> {
    private final int end;
    private final int start;

    public OnSubscribeRange(int i, int i2) {
        this.start = i;
        this.end = i2;
    }

    public void call(Subscriber<? super Integer> subscriber) {
        subscriber.setProducer(new RangeProducer(subscriber, this.start, this.end));
    }

    private static final class RangeProducer extends AtomicLong implements Producer {
        private static final long serialVersionUID = 4114392207069098388L;
        private final int end;
        private long index;
        private final Subscriber<? super Integer> o;

        private RangeProducer(Subscriber<? super Integer> subscriber, int i, int i2) {
            this.o = subscriber;
            this.index = (long) i;
            this.end = i2;
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
            long j2 = this.index;
            while (true) {
                long j3 = (((long) this.end) - j2) + 1;
                long min = Math.min(j3, j);
                boolean z = j3 <= j;
                long j4 = min + j2;
                Subscriber<? super Integer> subscriber = this.o;
                while (j2 != j4) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(Integer.valueOf((int) j2));
                        j2++;
                    } else {
                        return;
                    }
                }
                if (!z) {
                    this.index = j4;
                    j = addAndGet(-min);
                    if (j != 0) {
                        j2 = j4;
                    } else {
                        return;
                    }
                } else if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                    return;
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void fastpath() {
            long j = ((long) this.end) + 1;
            Subscriber<? super Integer> subscriber = this.o;
            long j2 = this.index;
            while (j2 != j) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf((int) j2));
                    j2++;
                } else {
                    return;
                }
            }
            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }
    }
}
