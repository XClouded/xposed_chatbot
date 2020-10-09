package rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

public class OperatorTakeLastOne<T> implements Observable.Operator<T, T> {

    private static class Holder {
        static final OperatorTakeLastOne<Object> INSTANCE = new OperatorTakeLastOne<>();

        private Holder() {
        }
    }

    public static <T> OperatorTakeLastOne<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorTakeLastOne() {
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final ParentSubscriber parentSubscriber = new ParentSubscriber(subscriber);
        subscriber.setProducer(new Producer() {
            public void request(long j) {
                parentSubscriber.requestMore(j);
            }
        });
        subscriber.add(parentSubscriber);
        return parentSubscriber;
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private static final Object ABSENT = new Object();
        private static final int NOT_REQUESTED_COMPLETED = 1;
        private static final int NOT_REQUESTED_NOT_COMPLETED = 0;
        private static final int REQUESTED_COMPLETED = 3;
        private static final int REQUESTED_NOT_COMPLETED = 2;
        private final Subscriber<? super T> child;
        private T last = ABSENT;
        private final AtomicInteger state = new AtomicInteger(0);

        ParentSubscriber(Subscriber<? super T> subscriber) {
            this.child = subscriber;
        }

        /* access modifiers changed from: package-private */
        public void requestMore(long j) {
            if (j > 0) {
                while (true) {
                    int i = this.state.get();
                    if (i == 0) {
                        if (this.state.compareAndSet(0, 2)) {
                            return;
                        }
                    } else if (i != 1) {
                        return;
                    } else {
                        if (this.state.compareAndSet(1, 3)) {
                            emit();
                            return;
                        }
                    }
                }
            }
        }

        public void onCompleted() {
            if (this.last == ABSENT) {
                this.child.onCompleted();
                return;
            }
            while (true) {
                int i = this.state.get();
                if (i == 0) {
                    if (this.state.compareAndSet(0, 1)) {
                        return;
                    }
                } else if (i != 2) {
                    return;
                } else {
                    if (this.state.compareAndSet(2, 3)) {
                        emit();
                        return;
                    }
                }
            }
        }

        private void emit() {
            if (isUnsubscribed()) {
                this.last = null;
                return;
            }
            T t = this.last;
            this.last = null;
            if (t != ABSENT) {
                try {
                    this.child.onNext(t);
                } catch (Throwable th) {
                    this.child.onError(th);
                    return;
                }
            }
            if (!isUnsubscribed()) {
                this.child.onCompleted();
            }
        }

        public void onError(Throwable th) {
            this.child.onError(th);
        }

        public void onNext(T t) {
            this.last = t;
        }
    }
}
