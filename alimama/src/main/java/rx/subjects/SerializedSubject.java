package rx.subjects;

import rx.Observable;
import rx.Subscriber;
import rx.annotations.Experimental;
import rx.observers.SerializedObserver;

public class SerializedSubject<T, R> extends Subject<T, R> {
    private final Subject<T, R> actual;
    private final SerializedObserver<T> observer;

    public SerializedSubject(final Subject<T, R> subject) {
        super(new Observable.OnSubscribe<R>() {
            public void call(Subscriber<? super R> subscriber) {
                Subject.this.unsafeSubscribe(subscriber);
            }
        });
        this.actual = subject;
        this.observer = new SerializedObserver<>(subject);
    }

    public void onCompleted() {
        this.observer.onCompleted();
    }

    public void onError(Throwable th) {
        this.observer.onError(th);
    }

    public void onNext(T t) {
        this.observer.onNext(t);
    }

    public boolean hasObservers() {
        return this.actual.hasObservers();
    }

    @Experimental
    public boolean hasCompleted() {
        return this.actual.hasCompleted();
    }

    @Experimental
    public boolean hasThrowable() {
        return this.actual.hasThrowable();
    }

    @Experimental
    public boolean hasValue() {
        return this.actual.hasValue();
    }

    @Experimental
    public Throwable getThrowable() {
        return this.actual.getThrowable();
    }

    @Experimental
    public T getValue() {
        return this.actual.getValue();
    }

    @Experimental
    public Object[] getValues() {
        return this.actual.getValues();
    }

    @Experimental
    public T[] getValues(T[] tArr) {
        return this.actual.getValues(tArr);
    }
}
