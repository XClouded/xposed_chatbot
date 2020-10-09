package rx.subjects;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.operators.NotificationLite;
import rx.subscriptions.Subscriptions;

final class SubjectSubscriptionManager<T> implements Observable.OnSubscribe<T> {
    static final AtomicReferenceFieldUpdater<SubjectSubscriptionManager, Object> LATEST_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SubjectSubscriptionManager.class, Object.class, "latest");
    static final AtomicReferenceFieldUpdater<SubjectSubscriptionManager, State> STATE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SubjectSubscriptionManager.class, State.class, "state");
    boolean active = true;
    volatile Object latest;
    public final NotificationLite<T> nl = NotificationLite.instance();
    Action1<SubjectObserver<T>> onAdded = Actions.empty();
    Action1<SubjectObserver<T>> onStart = Actions.empty();
    Action1<SubjectObserver<T>> onTerminated = Actions.empty();
    volatile State<T> state = State.EMPTY;

    SubjectSubscriptionManager() {
    }

    public void call(Subscriber<? super T> subscriber) {
        SubjectObserver subjectObserver = new SubjectObserver(subscriber);
        addUnsubscriber(subscriber, subjectObserver);
        this.onStart.call(subjectObserver);
        if (!subscriber.isUnsubscribed() && add(subjectObserver) && subscriber.isUnsubscribed()) {
            remove(subjectObserver);
        }
    }

    /* access modifiers changed from: package-private */
    public void addUnsubscriber(Subscriber<? super T> subscriber, final SubjectObserver<T> subjectObserver) {
        subscriber.add(Subscriptions.create(new Action0() {
            public void call() {
                SubjectSubscriptionManager.this.remove(subjectObserver);
            }
        }));
    }

    /* access modifiers changed from: package-private */
    public void set(Object obj) {
        this.latest = obj;
    }

    /* access modifiers changed from: package-private */
    public Object get() {
        return this.latest;
    }

    /* access modifiers changed from: package-private */
    public SubjectObserver<T>[] observers() {
        return this.state.observers;
    }

    /* access modifiers changed from: package-private */
    public boolean add(SubjectObserver<T> subjectObserver) {
        State<T> state2;
        do {
            state2 = this.state;
            if (state2.terminated) {
                this.onTerminated.call(subjectObserver);
                return false;
            }
        } while (!STATE_UPDATER.compareAndSet(this, state2, state2.add(subjectObserver)));
        this.onAdded.call(subjectObserver);
        return true;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r4) {
        /*
            r3 = this;
        L_0x0000:
            rx.subjects.SubjectSubscriptionManager$State<T> r0 = r3.state
            boolean r1 = r0.terminated
            if (r1 == 0) goto L_0x0007
            return
        L_0x0007:
            rx.subjects.SubjectSubscriptionManager$State r1 = r0.remove(r4)
            if (r1 == r0) goto L_0x0015
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater<rx.subjects.SubjectSubscriptionManager, rx.subjects.SubjectSubscriptionManager$State> r2 = STATE_UPDATER
            boolean r0 = r2.compareAndSet(r3, r0, r1)
            if (r0 == 0) goto L_0x0000
        L_0x0015:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.subjects.SubjectSubscriptionManager.remove(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
    }

    /* access modifiers changed from: package-private */
    public SubjectObserver<T>[] next(Object obj) {
        set(obj);
        return this.state.observers;
    }

    /* access modifiers changed from: package-private */
    public SubjectObserver<T>[] terminate(Object obj) {
        set(obj);
        this.active = false;
        if (this.state.terminated) {
            return State.NO_OBSERVERS;
        }
        return STATE_UPDATER.getAndSet(this, State.TERMINATED).observers;
    }

    protected static final class State<T> {
        static final State EMPTY = new State(false, NO_OBSERVERS);
        static final SubjectObserver[] NO_OBSERVERS = new SubjectObserver[0];
        static final State TERMINATED = new State(true, NO_OBSERVERS);
        final SubjectObserver[] observers;
        final boolean terminated;

        public State(boolean z, SubjectObserver[] subjectObserverArr) {
            this.terminated = z;
            this.observers = subjectObserverArr;
        }

        public State add(SubjectObserver subjectObserver) {
            int length = this.observers.length;
            SubjectObserver[] subjectObserverArr = new SubjectObserver[(length + 1)];
            System.arraycopy(this.observers, 0, subjectObserverArr, 0, length);
            subjectObserverArr[length] = subjectObserver;
            return new State(this.terminated, subjectObserverArr);
        }

        public State remove(SubjectObserver subjectObserver) {
            SubjectObserver[] subjectObserverArr;
            SubjectObserver[] subjectObserverArr2 = this.observers;
            if (r1 == 1 && subjectObserverArr2[0] == subjectObserver) {
                return EMPTY;
            }
            if (r1 == 0) {
                return this;
            }
            int i = r1 - 1;
            SubjectObserver[] subjectObserverArr3 = new SubjectObserver[i];
            int i2 = 0;
            for (SubjectObserver subjectObserver2 : subjectObserverArr2) {
                if (subjectObserver2 != subjectObserver) {
                    if (i2 == i) {
                        return this;
                    }
                    subjectObserverArr3[i2] = subjectObserver2;
                    i2++;
                }
            }
            if (i2 == 0) {
                return EMPTY;
            }
            if (i2 < i) {
                subjectObserverArr = new SubjectObserver[i2];
                System.arraycopy(subjectObserverArr3, 0, subjectObserverArr, 0, i2);
            } else {
                subjectObserverArr = subjectObserverArr3;
            }
            return new State(this.terminated, subjectObserverArr);
        }
    }

    protected static final class SubjectObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        protected volatile boolean caughtUp;
        boolean emitting;
        boolean fastPath;
        boolean first = true;
        private volatile Object index;
        List<Object> queue;

        public SubjectObserver(Observer<? super T> observer) {
            this.actual = observer;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001f, code lost:
            r1.fastPath = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitNext(java.lang.Object r2, rx.internal.operators.NotificationLite<T> r3) {
            /*
                r1 = this;
                boolean r0 = r1.fastPath
                if (r0 != 0) goto L_0x0026
                monitor-enter(r1)
                r0 = 0
                r1.first = r0     // Catch:{ all -> 0x0023 }
                boolean r0 = r1.emitting     // Catch:{ all -> 0x0023 }
                if (r0 == 0) goto L_0x001e
                java.util.List<java.lang.Object> r3 = r1.queue     // Catch:{ all -> 0x0023 }
                if (r3 != 0) goto L_0x0017
                java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x0023 }
                r3.<init>()     // Catch:{ all -> 0x0023 }
                r1.queue = r3     // Catch:{ all -> 0x0023 }
            L_0x0017:
                java.util.List<java.lang.Object> r3 = r1.queue     // Catch:{ all -> 0x0023 }
                r3.add(r2)     // Catch:{ all -> 0x0023 }
                monitor-exit(r1)     // Catch:{ all -> 0x0023 }
                return
            L_0x001e:
                monitor-exit(r1)     // Catch:{ all -> 0x0023 }
                r0 = 1
                r1.fastPath = r0
                goto L_0x0026
            L_0x0023:
                r2 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0023 }
                throw r2
            L_0x0026:
                rx.Observer<? super T> r0 = r1.actual
                r3.accept(r0, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitNext(java.lang.Object, rx.internal.operators.NotificationLite):void");
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0013, code lost:
            if (r2 == null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0015, code lost:
            emitLoop((java.util.List<java.lang.Object>) null, r2, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitFirst(java.lang.Object r2, rx.internal.operators.NotificationLite<T> r3) {
            /*
                r1 = this;
                monitor-enter(r1)
                boolean r0 = r1.first     // Catch:{ all -> 0x001c }
                if (r0 == 0) goto L_0x001a
                boolean r0 = r1.emitting     // Catch:{ all -> 0x001c }
                if (r0 == 0) goto L_0x000a
                goto L_0x001a
            L_0x000a:
                r0 = 0
                r1.first = r0     // Catch:{ all -> 0x001c }
                if (r2 == 0) goto L_0x0010
                r0 = 1
            L_0x0010:
                r1.emitting = r0     // Catch:{ all -> 0x001c }
                monitor-exit(r1)     // Catch:{ all -> 0x001c }
                if (r2 == 0) goto L_0x0019
                r0 = 0
                r1.emitLoop(r0, r2, r3)
            L_0x0019:
                return
            L_0x001a:
                monitor-exit(r1)     // Catch:{ all -> 0x001c }
                return
            L_0x001c:
                r2 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x001c }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitFirst(java.lang.Object, rx.internal.operators.NotificationLite):void");
        }

        /* access modifiers changed from: protected */
        public void emitLoop(List<Object> list, Object obj, NotificationLite<T> notificationLite) {
            boolean z = true;
            boolean z2 = true;
            while (true) {
                if (list != null) {
                    try {
                        for (Object accept : list) {
                            accept(accept, notificationLite);
                        }
                    } catch (Throwable th) {
                        th = th;
                        z = false;
                        if (!z) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                        }
                        throw th;
                    }
                }
                if (z2) {
                    accept(obj, notificationLite);
                    z2 = false;
                }
                synchronized (this) {
                    try {
                        list = this.queue;
                        this.queue = null;
                        if (list == null) {
                            this.emitting = false;
                            try {
                                return;
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    throw th;
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                            }
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        z = false;
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void accept(Object obj, NotificationLite<T> notificationLite) {
            if (obj != null) {
                notificationLite.accept(this.actual, obj);
            }
        }

        /* access modifiers changed from: protected */
        public Observer<? super T> getActual() {
            return this.actual;
        }

        public <I> I index() {
            return this.index;
        }

        public void index(Object obj) {
            this.index = obj;
        }
    }
}
