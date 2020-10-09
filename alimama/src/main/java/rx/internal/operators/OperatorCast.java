package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;

public class OperatorCast<T, R> implements Observable.Operator<R, T> {
    /* access modifiers changed from: private */
    public final Class<R> castClass;

    public OperatorCast(Class<R> cls) {
        this.castClass = cls;
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
                    subscriber.onNext(OperatorCast.this.castClass.cast(t));
                } catch (Throwable th) {
                    onError(OnErrorThrowable.addValueAsLastCause(th, t));
                }
            }
        };
    }
}
