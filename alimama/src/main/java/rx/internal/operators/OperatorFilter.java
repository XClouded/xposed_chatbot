package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;

public final class OperatorFilter<T> implements Observable.Operator<T, T> {
    /* access modifiers changed from: private */
    public final Func1<? super T, Boolean> predicate;

    public OperatorFilter(Func1<? super T, Boolean> func1) {
        this.predicate = func1;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onNext(T t) {
                try {
                    if (((Boolean) OperatorFilter.this.predicate.call(t)).booleanValue()) {
                        subscriber.onNext(t);
                    } else {
                        request(1);
                    }
                } catch (Throwable th) {
                    subscriber.onError(OnErrorThrowable.addValueAsLastCause(th, t));
                }
            }
        };
    }
}
