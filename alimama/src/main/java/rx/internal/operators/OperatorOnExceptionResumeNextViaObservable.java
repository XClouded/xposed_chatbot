package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.plugins.RxJavaPlugins;

public final class OperatorOnExceptionResumeNextViaObservable<T> implements Observable.Operator<T, T> {
    final Observable<? extends T> resumeSequence;

    public OperatorOnExceptionResumeNextViaObservable(Observable<? extends T> observable) {
        this.resumeSequence = observable;
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
                if (th instanceof Exception) {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                    unsubscribe();
                    OperatorOnExceptionResumeNextViaObservable.this.resumeSequence.unsafeSubscribe(subscriber);
                    return;
                }
                subscriber.onError(th);
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
