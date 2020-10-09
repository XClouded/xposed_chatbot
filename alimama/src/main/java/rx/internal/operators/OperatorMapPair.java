package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorMapPair<T, U, R> implements Observable.Operator<Observable<? extends R>, T> {
    final Func1<? super T, ? extends Observable<? extends U>> collectionSelector;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    public static <T, U> Func1<T, Observable<U>> convertSelector(final Func1<? super T, ? extends Iterable<? extends U>> func1) {
        return new Func1<T, Observable<U>>() {
            public Observable<U> call(T t) {
                return Observable.from((Iterable) func1.call(t));
            }
        };
    }

    public OperatorMapPair(Func1<? super T, ? extends Observable<? extends U>> func1, Func2<? super T, ? super U, ? extends R> func2) {
        this.collectionSelector = func1;
        this.resultSelector = func2;
    }

    public Subscriber<? super T> call(final Subscriber<? super Observable<? extends R>> subscriber) {
        return new Subscriber<T>(subscriber) {
            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onNext(final T t) {
                try {
                    subscriber.onNext(((Observable) OperatorMapPair.this.collectionSelector.call(t)).map(new Func1<U, R>() {
                        public R call(U u) {
                            return OperatorMapPair.this.resultSelector.call(t, u);
                        }
                    }));
                } catch (Throwable th) {
                    subscriber.onError(OnErrorThrowable.addValueAsLastCause(th, t));
                }
            }
        };
    }
}
