package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorFinally<T> implements Observable.Operator<T, T> {
    final Action0 action;

    public OperatorFinally(Action0 action0) {
        this.action = action0;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            public void onNext(T t) {
                subscriber.onNext(t);
            }

            public void onError(Throwable th) {
                try {
                    subscriber.onError(th);
                } finally {
                    OperatorFinally.this.action.call();
                }
            }

            public void onCompleted() {
                try {
                    subscriber.onCompleted();
                } finally {
                    OperatorFinally.this.action.call();
                }
            }
        };
    }
}
