package rx.internal.operators;

import java.util.Arrays;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.plugins.RxJavaPlugins;

public final class OperatorOnErrorReturn<T> implements Observable.Operator<T, T> {
    final Func1<Throwable, ? extends T> resultFunction;

    public OperatorOnErrorReturn(Func1<Throwable, ? extends T> func1) {
        this.resultFunction = func1;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        AnonymousClass1 r0 = new Subscriber<T>() {
            private boolean done = false;

            public void onNext(T t) {
                if (!this.done) {
                    subscriber.onNext(t);
                }
            }

            public void onError(Throwable th) {
                if (this.done) {
                    Exceptions.throwIfFatal(th);
                    return;
                }
                this.done = true;
                try {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                    unsubscribe();
                    subscriber.onNext(OperatorOnErrorReturn.this.resultFunction.call(th));
                    subscriber.onCompleted();
                } catch (Throwable th2) {
                    subscriber.onError(new CompositeException(Arrays.asList(new Throwable[]{th, th2})));
                }
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    subscriber.onCompleted();
                }
            }

            public void setProducer(final Producer producer) {
                subscriber.setProducer(new Producer() {
                    public void request(long j) {
                        producer.request(j);
                    }
                });
            }
        };
        subscriber.add(r0);
        return r0;
    }
}
