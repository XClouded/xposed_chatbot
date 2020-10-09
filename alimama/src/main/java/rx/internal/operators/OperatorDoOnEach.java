package rx.internal.operators;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;

public class OperatorDoOnEach<T> implements Observable.Operator<T, T> {
    /* access modifiers changed from: private */
    public final Observer<? super T> doOnEachObserver;

    public OperatorDoOnEach(Observer<? super T> observer) {
        this.doOnEachObserver = observer;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            private boolean done = false;

            public void onCompleted() {
                if (!this.done) {
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onCompleted();
                        this.done = true;
                        subscriber.onCompleted();
                    } catch (Throwable th) {
                        onError(th);
                    }
                }
            }

            public void onError(Throwable th) {
                Exceptions.throwIfFatal(th);
                if (!this.done) {
                    this.done = true;
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onError(th);
                        subscriber.onError(th);
                    } catch (Throwable th2) {
                        subscriber.onError(th2);
                    }
                }
            }

            public void onNext(T t) {
                if (!this.done) {
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onNext(t);
                        subscriber.onNext(t);
                    } catch (Throwable th) {
                        onError(OnErrorThrowable.addValueAsLastCause(th, t));
                    }
                }
            }
        };
    }
}
