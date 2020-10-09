package rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSampleWithObservable<T, U> implements Observable.Operator<T, T> {
    static final Object EMPTY_TOKEN = new Object();
    final Observable<U> sampler;

    public OperatorSampleWithObservable(Observable<U> observable) {
        this.sampler = observable;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        final AtomicReference atomicReference = new AtomicReference(EMPTY_TOKEN);
        AnonymousClass1 r2 = new Subscriber<U>(subscriber) {
            public void onNext(U u) {
                Object andSet = atomicReference.getAndSet(OperatorSampleWithObservable.EMPTY_TOKEN);
                if (andSet != OperatorSampleWithObservable.EMPTY_TOKEN) {
                    serializedSubscriber.onNext(andSet);
                }
            }

            public void onError(Throwable th) {
                serializedSubscriber.onError(th);
                unsubscribe();
            }

            public void onCompleted() {
                serializedSubscriber.onCompleted();
                unsubscribe();
            }
        };
        AnonymousClass2 r3 = new Subscriber<T>(subscriber) {
            public void onNext(T t) {
                atomicReference.set(t);
            }

            public void onError(Throwable th) {
                serializedSubscriber.onError(th);
                unsubscribe();
            }

            public void onCompleted() {
                serializedSubscriber.onCompleted();
                unsubscribe();
            }
        };
        this.sampler.unsafeSubscribe(r2);
        return r3;
    }
}
