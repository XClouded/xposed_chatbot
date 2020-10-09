package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorSkipWhile<T> implements Observable.Operator<T, T> {
    /* access modifiers changed from: private */
    public final Func2<? super T, Integer, Boolean> predicate;

    public OperatorSkipWhile(Func2<? super T, Integer, Boolean> func2) {
        this.predicate = func2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            int index;
            boolean skipping = true;

            public void onNext(T t) {
                if (!this.skipping) {
                    subscriber.onNext(t);
                    return;
                }
                Func2 access$000 = OperatorSkipWhile.this.predicate;
                int i = this.index;
                this.index = i + 1;
                if (!((Boolean) access$000.call(t, Integer.valueOf(i))).booleanValue()) {
                    this.skipping = false;
                    subscriber.onNext(t);
                    return;
                }
                request(1);
            }

            public void onError(Throwable th) {
                subscriber.onError(th);
            }

            public void onCompleted() {
                subscriber.onCompleted();
            }
        };
    }

    public static <T> Func2<T, Integer, Boolean> toPredicate2(final Func1<? super T, Boolean> func1) {
        return new Func2<T, Integer, Boolean>() {
            public Boolean call(T t, Integer num) {
                return (Boolean) func1.call(t);
            }
        };
    }
}
