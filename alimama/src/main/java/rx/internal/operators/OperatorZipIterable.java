package rx.internal.operators;

import java.util.Iterator;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;
import rx.observers.Subscribers;

public final class OperatorZipIterable<T1, T2, R> implements Observable.Operator<R, T1> {
    final Iterable<? extends T2> iterable;
    final Func2<? super T1, ? super T2, ? extends R> zipFunction;

    public OperatorZipIterable(Iterable<? extends T2> iterable2, Func2<? super T1, ? super T2, ? extends R> func2) {
        this.iterable = iterable2;
        this.zipFunction = func2;
    }

    public Subscriber<? super T1> call(final Subscriber<? super R> subscriber) {
        final Iterator<? extends T2> it = this.iterable.iterator();
        try {
            if (!it.hasNext()) {
                subscriber.onCompleted();
                return Subscribers.empty();
            }
        } catch (Throwable th) {
            subscriber.onError(th);
        }
        return new Subscriber<T1>(subscriber) {
            boolean once;

            public void onCompleted() {
                if (!this.once) {
                    this.once = true;
                    subscriber.onCompleted();
                }
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onNext(T1 t1) {
                try {
                    subscriber.onNext(OperatorZipIterable.this.zipFunction.call(t1, it.next()));
                    if (!it.hasNext()) {
                        onCompleted();
                    }
                } catch (Throwable th) {
                    onError(th);
                }
            }
        };
    }
}
