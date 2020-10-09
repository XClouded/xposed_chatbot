package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.util.UtilityFunctions;

public final class OperatorDistinctUntilChanged<T, U> implements Observable.Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    private static class Holder {
        static final OperatorDistinctUntilChanged<?, ?> INSTANCE = new OperatorDistinctUntilChanged<>(UtilityFunctions.identity());

        private Holder() {
        }
    }

    public static <T> OperatorDistinctUntilChanged<T, T> instance() {
        return Holder.INSTANCE;
    }

    public OperatorDistinctUntilChanged(Func1<? super T, ? extends U> func1) {
        this.keySelector = func1;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            boolean hasPrevious;
            U previousKey;

            public void onNext(T t) {
                U u = this.previousKey;
                U call = OperatorDistinctUntilChanged.this.keySelector.call(t);
                this.previousKey = call;
                if (!this.hasPrevious) {
                    this.hasPrevious = true;
                    subscriber.onNext(t);
                } else if (u == call || (call != null && call.equals(u))) {
                    request(1);
                } else {
                    subscriber.onNext(t);
                }
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onCompleted() {
                subscriber.onCompleted();
            }
        };
    }
}
