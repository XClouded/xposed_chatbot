package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

public final class OperatorElementAt<T> implements Observable.Operator<T, T> {
    /* access modifiers changed from: private */
    public final T defaultValue;
    /* access modifiers changed from: private */
    public final boolean hasDefault;
    /* access modifiers changed from: private */
    public final int index;

    public OperatorElementAt(int i) {
        this(i, (Object) null, false);
    }

    public OperatorElementAt(int i, T t) {
        this(i, t, true);
    }

    private OperatorElementAt(int i, T t, boolean z) {
        if (i >= 0) {
            this.index = i;
            this.defaultValue = t;
            this.hasDefault = z;
            return;
        }
        throw new IndexOutOfBoundsException(i + " is out of bounds");
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        AnonymousClass1 r0 = new Subscriber<T>() {
            private int currentIndex = 0;

            public void onNext(T t) {
                int i = this.currentIndex;
                this.currentIndex = i + 1;
                if (i == OperatorElementAt.this.index) {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                    unsubscribe();
                }
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onCompleted() {
                if (this.currentIndex > OperatorElementAt.this.index) {
                    return;
                }
                if (OperatorElementAt.this.hasDefault) {
                    subscriber.onNext(OperatorElementAt.this.defaultValue);
                    subscriber.onCompleted();
                    return;
                }
                Subscriber subscriber = subscriber;
                subscriber.onError(new IndexOutOfBoundsException(OperatorElementAt.this.index + " is out of bounds"));
            }

            public void setProducer(Producer producer) {
                subscriber.setProducer(new InnerProducer(producer));
            }
        };
        subscriber.add(r0);
        return r0;
    }

    static class InnerProducer extends AtomicBoolean implements Producer {
        private static final long serialVersionUID = 1;
        final Producer actual;

        public InnerProducer(Producer producer) {
            this.actual = producer;
        }

        public void request(long j) {
            if (j < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            } else if (j > 0 && compareAndSet(false, true)) {
                this.actual.request(Long.MAX_VALUE);
            }
        }
    }
}
