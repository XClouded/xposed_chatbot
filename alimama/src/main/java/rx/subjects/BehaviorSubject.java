package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import rx.Observable;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.subjects.SubjectSubscriptionManager;

public final class BehaviorSubject<T> extends Subject<T, T> {
    private final NotificationLite<T> nl = NotificationLite.instance();
    private final SubjectSubscriptionManager<T> state;

    public static <T> BehaviorSubject<T> create() {
        return create((Object) null, false);
    }

    public static <T> BehaviorSubject<T> create(T t) {
        return create(t, true);
    }

    private static <T> BehaviorSubject<T> create(T t, boolean z) {
        final SubjectSubscriptionManager subjectSubscriptionManager = new SubjectSubscriptionManager();
        if (z) {
            subjectSubscriptionManager.set(NotificationLite.instance().next(t));
        }
        subjectSubscriptionManager.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
                subjectObserver.emitFirst(subjectSubscriptionManager.get(), subjectSubscriptionManager.nl);
            }
        };
        subjectSubscriptionManager.onTerminated = subjectSubscriptionManager.onAdded;
        return new BehaviorSubject<>(subjectSubscriptionManager, subjectSubscriptionManager);
    }

    protected BehaviorSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager) {
        super(onSubscribe);
        this.state = subjectSubscriptionManager;
    }

    public void onCompleted() {
        if (this.state.get() == null || this.state.active) {
            Object completed = this.nl.completed();
            for (SubjectSubscriptionManager.SubjectObserver emitNext : this.state.terminate(completed)) {
                emitNext.emitNext(completed, this.state.nl);
            }
        }
    }

    public void onError(Throwable th) {
        if (this.state.get() == null || this.state.active) {
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
        if (this.state.get() == null || this.state.active) {
            Object next = this.nl.next(t);
            for (SubjectSubscriptionManager.SubjectObserver emitNext : this.state.next(next)) {
                emitNext.emitNext(next, this.state.nl);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int subscriberCount() {
        return this.state.observers().length;
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Experimental
    public boolean hasValue() {
        return this.nl.isNext(this.state.get());
    }

    @Experimental
    public boolean hasThrowable() {
        return this.nl.isError(this.state.get());
    }

    @Experimental
    public boolean hasCompleted() {
        return this.nl.isCompleted(this.state.get());
    }

    @Experimental
    public T getValue() {
        Object obj = this.state.get();
        if (this.nl.isNext(obj)) {
            return this.nl.getValue(obj);
        }
        return null;
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
        Object obj = this.state.get();
        if (this.nl.isNext(obj)) {
            if (tArr.length == 0) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), 1);
            }
            tArr[0] = this.nl.getValue(obj);
            if (tArr.length > 1) {
                tArr[1] = null;
            }
        } else if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
    }
}
