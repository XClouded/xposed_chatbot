package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;

public final class OperatorMap<T, R> implements Observable.Operator<R, T> {
    /* access modifiers changed from: private */
    public final Func1<? super T, ? extends R> transformer;

    public OperatorMap(Func1<? super T, ? extends R> func1) {
        this.transformer = func1;
    }

    public Subscriber<? super T> call(final Subscriber<? super R> subscriber) {
        return new Subscriber<T>(subscriber) {
            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onNext(T t) {
                try {
                    subscriber.onNext(OperatorMap.this.transformer.call(t));
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    onError(OnErrorThrowable.addValueAsLastCause(th, t));
                }
            }
        };
    }
}
