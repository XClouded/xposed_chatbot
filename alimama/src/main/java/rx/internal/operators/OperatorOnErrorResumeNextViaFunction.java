package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;

public final class OperatorOnErrorResumeNextViaFunction<T> implements Observable.Operator<T, T> {
    /* access modifiers changed from: private */
    public final Func1<Throwable, ? extends Observable<? extends T>> resumeFunction;

    public OperatorOnErrorResumeNextViaFunction(Func1<Throwable, ? extends Observable<? extends T>> func1) {
        this.resumeFunction = func1;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        final ProducerArbiter producerArbiter = new ProducerArbiter();
        final SerialSubscription serialSubscription = new SerialSubscription();
        AnonymousClass1 r2 = new Subscriber<T>() {
            private boolean done = false;

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    subscriber.onCompleted();
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
                    AnonymousClass1 r0 = new Subscriber<T>() {
                        public void onNext(T t) {
                            subscriber.onNext(t);
                        }

                        public void onError(Throwable th) {
                            subscriber.onError(th);
                        }

                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        public void setProducer(Producer producer) {
                            producerArbiter.setProducer(producer);
                        }
                    };
                    serialSubscription.set(r0);
                    ((Observable) OperatorOnErrorResumeNextViaFunction.this.resumeFunction.call(th)).unsafeSubscribe(r0);
                } catch (Throwable th2) {
                    subscriber.onError(th2);
                }
            }

            public void onNext(T t) {
                if (!this.done) {
                    subscriber.onNext(t);
                }
            }

            public void setProducer(Producer producer) {
                producerArbiter.setProducer(producer);
            }
        };
        subscriber.add(serialSubscription);
        serialSubscription.set(r2);
        subscriber.setProducer(producerArbiter);
        return r2;
    }
}
