package rx.subjects;

import java.util.ArrayList;
import rx.Observable;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.subjects.SubjectSubscriptionManager;

public final class PublishSubject<T> extends Subject<T, T> {
    private final NotificationLite<T> nl = NotificationLite.instance();
    final SubjectSubscriptionManager<T> state;

    @Experimental
    public T getValue() {
        return null;
    }

    @Experimental
    public boolean hasValue() {
        return false;
    }

    public static <T> PublishSubject<T> create() {
        final SubjectSubscriptionManager subjectSubscriptionManager = new SubjectSubscriptionManager();
        subjectSubscriptionManager.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
                subjectObserver.emitFirst(subjectSubscriptionManager.get(), subjectSubscriptionManager.nl);
            }
        };
        return new PublishSubject<>(subjectSubscriptionManager, subjectSubscriptionManager);
    }

    protected PublishSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager) {
        super(onSubscribe);
        this.state = subjectSubscriptionManager;
    }

    public void onCompleted() {
        if (this.state.active) {
            Object completed = this.nl.completed();
            for (SubjectSubscriptionManager.SubjectObserver emitNext : this.state.terminate(completed)) {
                emitNext.emitNext(completed, this.state.nl);
            }
        }
    }

    public void onError(Throwable th) {
        if (this.state.active) {
            Object error = this.nl.error(th);
            ArrayList arrayList = null;
            for (SubjectSubscriptionManager.SubjectObserver emitNext : this.state.terminate(error)) {
                try {
                    emitNext.emitNext(error, this.state.nl);
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
        for (SubjectSubscriptionManager.SubjectObserver onNext : this.state.observers()) {
            onNext.onNext(t);
        }
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
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
    public Throwable getThrowable() {
        Object obj = this.state.get();
        if (this.nl.isError(obj)) {
            return this.nl.getError(obj);
        }
        return null;
    }

    @Experimental
    public Object[] getValues() {
        return new Object[0];
    }

    @Experimental
    public T[] getValues(T[] tArr) {
        if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
    }
}
