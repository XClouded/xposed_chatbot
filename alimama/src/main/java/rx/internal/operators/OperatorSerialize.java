package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSerialize<T> implements Observable.Operator<T, T> {

    private static final class Holder {
        static final OperatorSerialize<Object> INSTANCE = new OperatorSerialize<>();

        private Holder() {
        }
    }

    public static <T> OperatorSerialize<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorSerialize() {
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new SerializedSubscriber(new Subscriber<T>(subscriber) {
            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onNext(T t) {
                subscriber.onNext(t);
            }
        });
    }
}
