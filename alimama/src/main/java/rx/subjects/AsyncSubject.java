package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import rx.Observable;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.subjects.SubjectSubscriptionManager;

public final class AsyncSubject<T> extends Subject<T, T> {
    volatile Object lastValue;
    private final NotificationLite<T> nl = NotificationLite.instance();
    final SubjectSubscriptionManager<T> state;

    public static <T> AsyncSubject<T> create() {
        final SubjectSubscriptionManager subjectSubscriptionManager = new SubjectSubscriptionManager();
        subjectSubscriptionManager.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
                Object obj = subjectSubscriptionManager.get();
                NotificationLite<T> notificationLite = subjectSubscriptionManager.nl;
                subjectObserver.accept(obj, notificationLite);
                if (obj == null || (!notificationLite.isCompleted(obj) && !notificationLite.isError(obj))) {
                    subjectObserver.onCompleted();
                }
            }
        };
        return new AsyncSubject<>(subjectSubscriptionManager, subjectSubscriptionManager);
    }

    protected AsyncSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager) {
        super(onSubscribe);
        this.state = subjectSubscriptionManager;
    }

    public void onCompleted() {
        if (this.state.active) {
            Object obj = this.lastValue;
            if (obj == null) {
                obj = this.nl.completed();
            }
            for (SubjectSubscriptionManager.SubjectObserver subjectObserver : this.state.terminate(obj)) {
                if (obj == this.nl.completed()) {
                    subjectObserver.onCompleted();
                } else {
                    subjectObserver.onNext(this.nl.getValue(obj));
                    subjectObserver.onCompleted();
                }
            }
        }
    }

    public void onError(Throwable th) {
        if (this.state.active) {
            ArrayList arrayList = null;
            for (SubjectSubscriptionManager.SubjectObserver onError : this.state.terminate(this.nl.error(th))) {
                try {
                    onError.onError(th);
                } catch (Throwable th2) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(th2);
                }
            }
            Exceptions.throwIfAny(arrayList);
        }
    }

    public void onNext(T t) {
        this.lastValue = this.nl.next(t);
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Experimental
    public boolean hasValue() {
        return !this.nl.isError(this.state.get()) && this.nl.isNext(this.lastValue);
    }

    @Experimental
    public boolean hasThrowable() {
        return this.nl.isError(this.state.get());
    }

    @Experimental
    public boolean hasCompleted() {
        Object obj = this.state.get();
        return obj != null && !this.nl.isError(obj);
    }

    @Experimental
    public T getValue() {
        Object obj = this.lastValue;
        if (this.nl.isError(this.state.get()) || !this.nl.isNext(obj)) {
            return null;
        }
        return this.nl.getValue(obj);
    }

    @Experimental
    public Throwable getThrowable() {
        Object obj = this.state.get();
        if (this.nl.isError(obj)) {
            return this.nl.getError(obj);
        }
        return null;
    }

    @Experimental
    public T[] getValues(T[] tArr) {
        Object obj = this.lastValue;
        if (!this.nl.isError(this.state.get()) && this.nl.isNext(obj)) {
            T value = this.nl.getValue(obj);
            if (tArr.length == 0) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), 1);
            }
            tArr[0] = value;
            if (tArr.length > 1) {
                tArr[1] = null;
            }
        } else if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
    }
}
