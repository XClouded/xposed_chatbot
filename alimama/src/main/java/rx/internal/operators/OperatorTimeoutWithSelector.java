package rx.internal.operators;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.internal.operators.OperatorTimeoutBase;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class OperatorTimeoutWithSelector<T, U, V> extends OperatorTimeoutBase<T> {
    public /* bridge */ /* synthetic */ Subscriber call(Subscriber subscriber) {
        return super.call(subscriber);
    }

    public OperatorTimeoutWithSelector(final Func0<? extends Observable<U>> func0, final Func1<? super T, ? extends Observable<V>> func1, Observable<? extends T> observable) {
        super(new OperatorTimeoutBase.FirstTimeoutStub<T>() {
            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long l, Scheduler.Worker worker) {
                if (Func0.this == null) {
                    return Subscriptions.unsubscribed();
                }
                try {
                    return ((Observable) Func0.this.call()).unsafeSubscribe(new Subscriber<U>() {
                        public void onCompleted() {
                            timeoutSubscriber.onTimeout(l.longValue());
                        }

                        public void onError(Throwable th) {
                            timeoutSubscriber.onError(th);
                        }

                        public void onNext(U u) {
                            timeoutSubscriber.onTimeout(l.longValue());
                        }
                    });
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    timeoutSubscriber.onError(th);
                    return Subscriptions.unsubscribed();
                }
            }
        }, new OperatorTimeoutBase.TimeoutStub<T>() {
            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long l, T t, Scheduler.Worker worker) {
                try {
                    return ((Observable) Func1.this.call(t)).unsafeSubscribe(new Subscriber<V>() {
                        public void onCompleted() {
                            timeoutSubscriber.onTimeout(l.longValue());
                        }

                        public void onError(Throwable th) {
                            timeoutSubscriber.onError(th);
                        }

                        public void onNext(V v) {
                            timeoutSubscriber.onTimeout(l.longValue());
                        }
                    });
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    timeoutSubscriber.onError(th);
                    return Subscriptions.unsubscribed();
                }
            }
        }, observable, Schedulers.immediate());
    }
}
