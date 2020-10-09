package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.NotificationLite;
import rx.internal.util.UtilityFunctions;
import rx.schedulers.Timestamped;
import rx.subjects.SubjectSubscriptionManager;

public final class ReplaySubject<T> extends Subject<T, T> {
    final SubjectSubscriptionManager<T> ssm;
    final ReplayState<T, ?> state;

    interface EvictionPolicy {
        void evict(NodeList<Object> nodeList);

        void evictFinal(NodeList<Object> nodeList);

        boolean test(Object obj, long j);
    }

    interface ReplayState<T, I> {
        void complete();

        void error(Throwable th);

        boolean isEmpty();

        T latest();

        void next(T t);

        boolean replayObserver(SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver);

        I replayObserverFromIndex(I i, SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver);

        I replayObserverFromIndexTest(I i, SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver, long j);

        int size();

        boolean terminated();

        T[] toArray(T[] tArr);
    }

    public static <T> ReplaySubject<T> create() {
        return create(16);
    }

    public static <T> ReplaySubject<T> create(int i) {
        final UnboundedReplayState unboundedReplayState = new UnboundedReplayState(i);
        SubjectSubscriptionManager subjectSubscriptionManager = new SubjectSubscriptionManager();
        subjectSubscriptionManager.onStart = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
                subjectObserver.index(Integer.valueOf(unboundedReplayState.replayObserverFromIndex((Integer) 0, subjectObserver).intValue()));
            }
        };
        subjectSubscriptionManager.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
                r2 = ((java.lang.Integer) r7.index()).intValue();
                r3 = r0.index;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:11:0x001f, code lost:
                if (r2 == r3) goto L_0x002e;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
                r7.index(r0.replayObserverFromIndex(java.lang.Integer.valueOf(r2), r7));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:13:0x002e, code lost:
                monitor-enter(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:16:0x0033, code lost:
                if (r3 != r0.index) goto L_0x0039;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:17:0x0035, code lost:
                r7.emitting = false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
                monitor-exit(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:20:0x0038, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
                monitor-exit(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:0x003b, code lost:
                r2 = th;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:24:0x003c, code lost:
                r1 = false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
                monitor-exit(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
                throw r2;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:29:0x003f, code lost:
                r2 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:30:0x0040, code lost:
                r5 = r2;
                r2 = r1;
                r1 = r5;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:31:0x0044, code lost:
                r2 = th;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:32:0x0046, code lost:
                r1 = th;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:33:0x0047, code lost:
                r2 = false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:34:0x0048, code lost:
                if (r2 == false) goto L_0x004a;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:0x004a, code lost:
                monitor-enter(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
                r7.emitting = false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:43:0x0052, code lost:
                throw r1;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void call(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r7) {
                /*
                    r6 = this;
                    monitor-enter(r7)
                    boolean r0 = r7.first     // Catch:{ all -> 0x0055 }
                    if (r0 == 0) goto L_0x0053
                    boolean r0 = r7.emitting     // Catch:{ all -> 0x0055 }
                    if (r0 == 0) goto L_0x000a
                    goto L_0x0053
                L_0x000a:
                    r0 = 0
                    r7.first = r0     // Catch:{ all -> 0x0055 }
                    r1 = 1
                    r7.emitting = r1     // Catch:{ all -> 0x0055 }
                    monitor-exit(r7)     // Catch:{ all -> 0x0055 }
                L_0x0011:
                    java.lang.Object r2 = r7.index()     // Catch:{ all -> 0x0046 }
                    java.lang.Integer r2 = (java.lang.Integer) r2     // Catch:{ all -> 0x0046 }
                    int r2 = r2.intValue()     // Catch:{ all -> 0x0046 }
                    rx.subjects.ReplaySubject$UnboundedReplayState r3 = r0     // Catch:{ all -> 0x0046 }
                    int r3 = r3.index     // Catch:{ all -> 0x0046 }
                    if (r2 == r3) goto L_0x002e
                    rx.subjects.ReplaySubject$UnboundedReplayState r4 = r0     // Catch:{ all -> 0x0046 }
                    java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0046 }
                    java.lang.Integer r2 = r4.replayObserverFromIndex((java.lang.Integer) r2, r7)     // Catch:{ all -> 0x0046 }
                    r7.index(r2)     // Catch:{ all -> 0x0046 }
                L_0x002e:
                    monitor-enter(r7)     // Catch:{ all -> 0x0046 }
                    rx.subjects.ReplaySubject$UnboundedReplayState r2 = r0     // Catch:{ all -> 0x003b }
                    int r2 = r2.index     // Catch:{ all -> 0x003b }
                    if (r3 != r2) goto L_0x0039
                    r7.emitting = r0     // Catch:{ all -> 0x003b }
                    monitor-exit(r7)     // Catch:{ all -> 0x0044 }
                    return
                L_0x0039:
                    monitor-exit(r7)     // Catch:{ all -> 0x003b }
                    goto L_0x0011
                L_0x003b:
                    r2 = move-exception
                    r1 = 0
                L_0x003d:
                    monitor-exit(r7)     // Catch:{ all -> 0x0044 }
                    throw r2     // Catch:{ all -> 0x003f }
                L_0x003f:
                    r2 = move-exception
                    r5 = r2
                    r2 = r1
                    r1 = r5
                    goto L_0x0048
                L_0x0044:
                    r2 = move-exception
                    goto L_0x003d
                L_0x0046:
                    r1 = move-exception
                    r2 = 0
                L_0x0048:
                    if (r2 != 0) goto L_0x0052
                    monitor-enter(r7)
                    r7.emitting = r0     // Catch:{ all -> 0x004f }
                    monitor-exit(r7)     // Catch:{ all -> 0x004f }
                    goto L_0x0052
                L_0x004f:
                    r0 = move-exception
                    monitor-exit(r7)     // Catch:{ all -> 0x004f }
                    throw r0
                L_0x0052:
                    throw r1
                L_0x0053:
                    monitor-exit(r7)     // Catch:{ all -> 0x0055 }
                    return
                L_0x0055:
                    r0 = move-exception
                    monitor-exit(r7)     // Catch:{ all -> 0x0055 }
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.AnonymousClass2.call(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
            }
        };
        subjectSubscriptionManager.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
                int i = (Integer) subjectObserver.index();
                if (i == null) {
                    i = 0;
                }
                unboundedReplayState.replayObserverFromIndex(i, subjectObserver);
            }
        };
        return new ReplaySubject<>(subjectSubscriptionManager, subjectSubscriptionManager, unboundedReplayState);
    }

    static <T> ReplaySubject<T> createUnbounded() {
        BoundedState boundedState = new BoundedState(new EmptyEvictionPolicy(), UtilityFunctions.identity(), UtilityFunctions.identity());
        return createWithState(boundedState, new DefaultOnAdd(boundedState));
    }

    public static <T> ReplaySubject<T> createWithSize(int i) {
        BoundedState boundedState = new BoundedState(new SizeEvictionPolicy(i), UtilityFunctions.identity(), UtilityFunctions.identity());
        return createWithState(boundedState, new DefaultOnAdd(boundedState));
    }

    public static <T> ReplaySubject<T> createWithTime(long j, TimeUnit timeUnit, Scheduler scheduler) {
        BoundedState boundedState = new BoundedState(new TimeEvictionPolicy(timeUnit.toMillis(j), scheduler), new AddTimestamped(scheduler), new RemoveTimestamped());
        return createWithState(boundedState, new TimedOnAdd(boundedState, scheduler));
    }

    public static <T> ReplaySubject<T> createWithTimeAndSize(long j, TimeUnit timeUnit, int i, Scheduler scheduler) {
        BoundedState boundedState = new BoundedState(new PairEvictionPolicy(new SizeEvictionPolicy(i), new TimeEvictionPolicy(timeUnit.toMillis(j), scheduler)), new AddTimestamped(scheduler), new RemoveTimestamped());
        return createWithState(boundedState, new TimedOnAdd(boundedState, scheduler));
    }

    static final <T> ReplaySubject<T> createWithState(final BoundedState<T> boundedState, Action1<SubjectSubscriptionManager.SubjectObserver<T>> action1) {
        SubjectSubscriptionManager subjectSubscriptionManager = new SubjectSubscriptionManager();
        subjectSubscriptionManager.onStart = action1;
        subjectSubscriptionManager.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
                r2 = (rx.subjects.ReplaySubject.NodeList.Node) r7.index();
                r3 = r1.tail();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
                if (r2 == r3) goto L_0x0028;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
                r7.index(r1.replayObserverFromIndex(r2, r7));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:13:0x0028, code lost:
                monitor-enter(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
                if (r3 != r1.tail()) goto L_0x0035;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:17:0x0031, code lost:
                r7.emitting = false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
                monitor-exit(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:20:0x0034, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
                monitor-exit(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:0x0037, code lost:
                r2 = th;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:24:0x0038, code lost:
                r1 = false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
                monitor-exit(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
                throw r2;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:29:0x003b, code lost:
                r2 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:30:0x003c, code lost:
                r5 = r2;
                r2 = r1;
                r1 = r5;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:31:0x0040, code lost:
                r2 = th;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:32:0x0042, code lost:
                r1 = th;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:33:0x0043, code lost:
                r2 = false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:34:0x0044, code lost:
                if (r2 == false) goto L_0x0046;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:35:0x0046, code lost:
                monitor-enter(r7);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
                r7.emitting = false;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:43:0x004e, code lost:
                throw r1;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void call(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r7) {
                /*
                    r6 = this;
                    monitor-enter(r7)
                    boolean r0 = r7.first     // Catch:{ all -> 0x0051 }
                    if (r0 == 0) goto L_0x004f
                    boolean r0 = r7.emitting     // Catch:{ all -> 0x0051 }
                    if (r0 == 0) goto L_0x000a
                    goto L_0x004f
                L_0x000a:
                    r0 = 0
                    r7.first = r0     // Catch:{ all -> 0x0051 }
                    r1 = 1
                    r7.emitting = r1     // Catch:{ all -> 0x0051 }
                    monitor-exit(r7)     // Catch:{ all -> 0x0051 }
                L_0x0011:
                    java.lang.Object r2 = r7.index()     // Catch:{ all -> 0x0042 }
                    rx.subjects.ReplaySubject$NodeList$Node r2 = (rx.subjects.ReplaySubject.NodeList.Node) r2     // Catch:{ all -> 0x0042 }
                    rx.subjects.ReplaySubject$BoundedState r3 = r1     // Catch:{ all -> 0x0042 }
                    rx.subjects.ReplaySubject$NodeList$Node r3 = r3.tail()     // Catch:{ all -> 0x0042 }
                    if (r2 == r3) goto L_0x0028
                    rx.subjects.ReplaySubject$BoundedState r4 = r1     // Catch:{ all -> 0x0042 }
                    rx.subjects.ReplaySubject$NodeList$Node r2 = r4.replayObserverFromIndex((rx.subjects.ReplaySubject.NodeList.Node<java.lang.Object>) r2, r7)     // Catch:{ all -> 0x0042 }
                    r7.index(r2)     // Catch:{ all -> 0x0042 }
                L_0x0028:
                    monitor-enter(r7)     // Catch:{ all -> 0x0042 }
                    rx.subjects.ReplaySubject$BoundedState r2 = r1     // Catch:{ all -> 0x0037 }
                    rx.subjects.ReplaySubject$NodeList$Node r2 = r2.tail()     // Catch:{ all -> 0x0037 }
                    if (r3 != r2) goto L_0x0035
                    r7.emitting = r0     // Catch:{ all -> 0x0037 }
                    monitor-exit(r7)     // Catch:{ all -> 0x0040 }
                    return
                L_0x0035:
                    monitor-exit(r7)     // Catch:{ all -> 0x0037 }
                    goto L_0x0011
                L_0x0037:
                    r2 = move-exception
                    r1 = 0
                L_0x0039:
                    monitor-exit(r7)     // Catch:{ all -> 0x0040 }
                    throw r2     // Catch:{ all -> 0x003b }
                L_0x003b:
                    r2 = move-exception
                    r5 = r2
                    r2 = r1
                    r1 = r5
                    goto L_0x0044
                L_0x0040:
                    r2 = move-exception
                    goto L_0x0039
                L_0x0042:
                    r1 = move-exception
                    r2 = 0
                L_0x0044:
                    if (r2 != 0) goto L_0x004e
                    monitor-enter(r7)
                    r7.emitting = r0     // Catch:{ all -> 0x004b }
                    monitor-exit(r7)     // Catch:{ all -> 0x004b }
                    goto L_0x004e
                L_0x004b:
                    r0 = move-exception
                    monitor-exit(r7)     // Catch:{ all -> 0x004b }
                    throw r0
                L_0x004e:
                    throw r1
                L_0x004f:
                    monitor-exit(r7)     // Catch:{ all -> 0x0051 }
                    return
                L_0x0051:
                    r0 = move-exception
                    monitor-exit(r7)     // Catch:{ all -> 0x0051 }
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.AnonymousClass4.call(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
            }
        };
        subjectSubscriptionManager.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() {
            public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
                NodeList.Node<Object> node = (NodeList.Node) subjectObserver.index();
                if (node == null) {
                    node = boundedState.head();
                }
                boundedState.replayObserverFromIndex(node, subjectObserver);
            }
        };
        return new ReplaySubject<>(subjectSubscriptionManager, subjectSubscriptionManager, boundedState);
    }

    ReplaySubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager, ReplayState<T, ?> replayState) {
        super(onSubscribe);
        this.ssm = subjectSubscriptionManager;
        this.state = replayState;
    }

    public void onNext(T t) {
        if (this.ssm.active) {
            this.state.next(t);
            for (SubjectSubscriptionManager.SubjectObserver subjectObserver : this.ssm.observers()) {
                if (caughtUp(subjectObserver)) {
                    subjectObserver.onNext(t);
                }
            }
        }
    }

    public void onError(Throwable th) {
        if (this.ssm.active) {
            this.state.error(th);
            ArrayList arrayList = null;
            for (SubjectSubscriptionManager.SubjectObserver subjectObserver : this.ssm.terminate(NotificationLite.instance().error(th))) {
                try {
                    if (caughtUp(subjectObserver)) {
                        subjectObserver.onError(th);
                    }
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

    public void onCompleted() {
        if (this.ssm.active) {
            this.state.complete();
            for (SubjectSubscriptionManager.SubjectObserver subjectObserver : this.ssm.terminate(NotificationLite.instance().completed())) {
                if (caughtUp(subjectObserver)) {
                    subjectObserver.onCompleted();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int subscriberCount() {
        return this.ssm.state.observers.length;
    }

    public boolean hasObservers() {
        return this.ssm.observers().length > 0;
    }

    private boolean caughtUp(SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver) {
        if (subjectObserver.caughtUp) {
            return true;
        }
        if (!this.state.replayObserver(subjectObserver)) {
            return false;
        }
        subjectObserver.caughtUp = true;
        subjectObserver.index((Object) null);
        return false;
    }

    static final class UnboundedReplayState<T> implements ReplayState<T, Integer> {
        static final AtomicIntegerFieldUpdater<UnboundedReplayState> INDEX_UPDATER = AtomicIntegerFieldUpdater.newUpdater(UnboundedReplayState.class, "index");
        volatile int index;
        private final ArrayList<Object> list;
        private final NotificationLite<T> nl = NotificationLite.instance();
        private volatile boolean terminated;

        public UnboundedReplayState(int i) {
            this.list = new ArrayList<>(i);
        }

        public void next(T t) {
            if (!this.terminated) {
                this.list.add(this.nl.next(t));
                INDEX_UPDATER.getAndIncrement(this);
            }
        }

        public void accept(Observer<? super T> observer, int i) {
            this.nl.accept(observer, this.list.get(i));
        }

        public void complete() {
            if (!this.terminated) {
                this.terminated = true;
                this.list.add(this.nl.completed());
                INDEX_UPDATER.getAndIncrement(this);
            }
        }

        public void error(Throwable th) {
            if (!this.terminated) {
                this.terminated = true;
                this.list.add(this.nl.error(th));
                INDEX_UPDATER.getAndIncrement(this);
            }
        }

        public boolean terminated() {
            return this.terminated;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
            r4.index(java.lang.Integer.valueOf(replayObserverFromIndex(r0, r4).intValue()));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0023, code lost:
            return true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x003a, code lost:
            throw new java.lang.IllegalStateException("failed to find lastEmittedLink for: " + r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000b, code lost:
            r0 = (java.lang.Integer) r4.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0011, code lost:
            if (r0 == null) goto L_0x0024;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean replayObserver(rx.subjects.SubjectSubscriptionManager.SubjectObserver<? super T> r4) {
            /*
                r3 = this;
                monitor-enter(r4)
                r0 = 0
                r4.first = r0     // Catch:{ all -> 0x003b }
                boolean r1 = r4.emitting     // Catch:{ all -> 0x003b }
                if (r1 == 0) goto L_0x000a
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                return r0
            L_0x000a:
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                java.lang.Object r0 = r4.index()
                java.lang.Integer r0 = (java.lang.Integer) r0
                if (r0 == 0) goto L_0x0024
                java.lang.Integer r0 = r3.replayObserverFromIndex((java.lang.Integer) r0, r4)
                int r0 = r0.intValue()
                java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
                r4.index(r0)
                r4 = 1
                return r4
            L_0x0024:
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "failed to find lastEmittedLink for: "
                r1.append(r2)
                r1.append(r4)
                java.lang.String r4 = r1.toString()
                r0.<init>(r4)
                throw r0
            L_0x003b:
                r0 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x003b }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.UnboundedReplayState.replayObserver(rx.subjects.SubjectSubscriptionManager$SubjectObserver):boolean");
        }

        public Integer replayObserverFromIndex(Integer num, SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver) {
            int intValue = num.intValue();
            while (intValue < this.index) {
                accept(subjectObserver, intValue);
                intValue++;
            }
            return Integer.valueOf(intValue);
        }

        public Integer replayObserverFromIndexTest(Integer num, SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver, long j) {
            return replayObserverFromIndex(num, subjectObserver);
        }

        public int size() {
            int i = this.index;
            if (i > 0) {
                int i2 = i - 1;
                Object obj = this.list.get(i2);
                if (this.nl.isCompleted(obj) || this.nl.isError(obj)) {
                    return i2;
                }
            }
            return i;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public T[] toArray(T[] tArr) {
            int size = size();
            if (size > 0) {
                if (size > tArr.length) {
                    tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
                }
                for (int i = 0; i < size; i++) {
                    tArr[i] = this.list.get(i);
                }
                if (tArr.length > size) {
                    tArr[size] = null;
                }
            } else if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }

        public T latest() {
            int i = this.index;
            if (i <= 0) {
                return null;
            }
            Object obj = this.list.get(i - 1);
            if (!this.nl.isCompleted(obj) && !this.nl.isError(obj)) {
                return this.nl.getValue(obj);
            }
            if (i > 1) {
                return this.nl.getValue(this.list.get(i - 2));
            }
            return null;
        }
    }

    static final class BoundedState<T> implements ReplayState<T, NodeList.Node<Object>> {
        final Func1<Object, Object> enterTransform;
        final EvictionPolicy evictionPolicy;
        final Func1<Object, Object> leaveTransform;
        final NodeList<Object> list = new NodeList<>();
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile NodeList.Node<Object> tail = this.list.tail;
        volatile boolean terminated;

        public BoundedState(EvictionPolicy evictionPolicy2, Func1<Object, Object> func1, Func1<Object, Object> func12) {
            this.evictionPolicy = evictionPolicy2;
            this.enterTransform = func1;
            this.leaveTransform = func12;
        }

        public void next(T t) {
            if (!this.terminated) {
                this.list.addLast(this.enterTransform.call(this.nl.next(t)));
                this.evictionPolicy.evict(this.list);
                this.tail = this.list.tail;
            }
        }

        public void complete() {
            if (!this.terminated) {
                this.terminated = true;
                this.list.addLast(this.enterTransform.call(this.nl.completed()));
                this.evictionPolicy.evictFinal(this.list);
                this.tail = this.list.tail;
            }
        }

        public void error(Throwable th) {
            if (!this.terminated) {
                this.terminated = true;
                this.list.addLast(this.enterTransform.call(this.nl.error(th)));
                this.evictionPolicy.evictFinal(this.list);
                this.tail = this.list.tail;
            }
        }

        public void accept(Observer<? super T> observer, NodeList.Node<Object> node) {
            this.nl.accept(observer, this.leaveTransform.call(node.value));
        }

        public void acceptTest(Observer<? super T> observer, NodeList.Node<Object> node, long j) {
            T t = node.value;
            if (!this.evictionPolicy.test(t, j)) {
                this.nl.accept(observer, this.leaveTransform.call(t));
            }
        }

        public NodeList.Node<Object> head() {
            return this.list.head;
        }

        public NodeList.Node<Object> tail() {
            return this.tail;
        }

        public boolean replayObserver(SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver) {
            synchronized (subjectObserver) {
                subjectObserver.first = false;
                if (subjectObserver.emitting) {
                    return false;
                }
                subjectObserver.index(replayObserverFromIndex((NodeList.Node<Object>) (NodeList.Node) subjectObserver.index(), subjectObserver));
                return true;
            }
        }

        public NodeList.Node<Object> replayObserverFromIndex(NodeList.Node<Object> node, SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver) {
            while (true) {
                NodeList.Node<T> node2 = node;
                if (node2 == tail()) {
                    return node2;
                }
                accept(subjectObserver, node2.next);
                node2 = node2.next;
            }
        }

        public NodeList.Node<Object> replayObserverFromIndexTest(NodeList.Node<Object> node, SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver, long j) {
            while (true) {
                NodeList.Node<T> node2 = node;
                if (node2 == tail()) {
                    return node2;
                }
                acceptTest(subjectObserver, node2.next, j);
                node2 = node2.next;
            }
        }

        public boolean terminated() {
            return this.terminated;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0015, code lost:
            r0 = r4.leaveTransform.call(r1.value);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int size() {
            /*
                r4 = this;
                rx.subjects.ReplaySubject$NodeList$Node r0 = r4.head()
                rx.subjects.ReplaySubject$NodeList$Node<T> r1 = r0.next
                r2 = 0
            L_0x0007:
                r3 = r1
                r1 = r0
                r0 = r3
                if (r0 == 0) goto L_0x0011
                int r2 = r2 + 1
                rx.subjects.ReplaySubject$NodeList$Node<T> r1 = r0.next
                goto L_0x0007
            L_0x0011:
                T r0 = r1.value
                if (r0 == 0) goto L_0x0032
                rx.functions.Func1<java.lang.Object, java.lang.Object> r0 = r4.leaveTransform
                T r1 = r1.value
                java.lang.Object r0 = r0.call(r1)
                if (r0 == 0) goto L_0x0032
                rx.internal.operators.NotificationLite<T> r1 = r4.nl
                boolean r1 = r1.isError(r0)
                if (r1 != 0) goto L_0x002f
                rx.internal.operators.NotificationLite<T> r1 = r4.nl
                boolean r0 = r1.isCompleted(r0)
                if (r0 == 0) goto L_0x0032
            L_0x002f:
                int r2 = r2 + -1
                return r2
            L_0x0032:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.BoundedState.size():int");
        }

        public boolean isEmpty() {
            NodeList.Node<T> node = head().next;
            if (node == null) {
                return true;
            }
            Object call = this.leaveTransform.call(node.value);
            if (this.nl.isError(call) || this.nl.isCompleted(call)) {
                return true;
            }
            return false;
        }

        public T[] toArray(T[] tArr) {
            ArrayList arrayList = new ArrayList();
            for (NodeList.Node<T> node = head().next; node != null; node = node.next) {
                Object call = this.leaveTransform.call(node.value);
                if (node.next == null && (this.nl.isError(call) || this.nl.isCompleted(call))) {
                    break;
                }
                arrayList.add(call);
            }
            return arrayList.toArray(tArr);
        }

        public T latest() {
            NodeList.Node<T> node = head().next;
            if (node == null) {
                return null;
            }
            NodeList.Node<T> node2 = null;
            while (node != tail()) {
                node2 = node;
                node = node.next;
            }
            Object call = this.leaveTransform.call(node.value);
            if (!this.nl.isError(call) && !this.nl.isCompleted(call)) {
                return this.nl.getValue(call);
            }
            if (node2 == null) {
                return null;
            }
            return this.nl.getValue(this.leaveTransform.call(node2.value));
        }
    }

    static final class SizeEvictionPolicy implements EvictionPolicy {
        final int maxSize;

        public boolean test(Object obj, long j) {
            return false;
        }

        public SizeEvictionPolicy(int i) {
            this.maxSize = i;
        }

        public void evict(NodeList<Object> nodeList) {
            while (nodeList.size() > this.maxSize) {
                nodeList.removeFirst();
            }
        }

        public void evictFinal(NodeList<Object> nodeList) {
            while (nodeList.size() > this.maxSize + 1) {
                nodeList.removeFirst();
            }
        }
    }

    static final class TimeEvictionPolicy implements EvictionPolicy {
        final long maxAgeMillis;
        final Scheduler scheduler;

        public TimeEvictionPolicy(long j, Scheduler scheduler2) {
            this.maxAgeMillis = j;
            this.scheduler = scheduler2;
        }

        public void evict(NodeList<Object> nodeList) {
            long now = this.scheduler.now();
            while (!nodeList.isEmpty() && test(nodeList.head.next.value, now)) {
                nodeList.removeFirst();
            }
        }

        public void evictFinal(NodeList<Object> nodeList) {
            long now = this.scheduler.now();
            while (nodeList.size > 1 && test(nodeList.head.next.value, now)) {
                nodeList.removeFirst();
            }
        }

        public boolean test(Object obj, long j) {
            return ((Timestamped) obj).getTimestampMillis() <= j - this.maxAgeMillis;
        }
    }

    static final class PairEvictionPolicy implements EvictionPolicy {
        final EvictionPolicy first;
        final EvictionPolicy second;

        public PairEvictionPolicy(EvictionPolicy evictionPolicy, EvictionPolicy evictionPolicy2) {
            this.first = evictionPolicy;
            this.second = evictionPolicy2;
        }

        public void evict(NodeList<Object> nodeList) {
            this.first.evict(nodeList);
            this.second.evict(nodeList);
        }

        public void evictFinal(NodeList<Object> nodeList) {
            this.first.evictFinal(nodeList);
            this.second.evictFinal(nodeList);
        }

        public boolean test(Object obj, long j) {
            return this.first.test(obj, j) || this.second.test(obj, j);
        }
    }

    static final class AddTimestamped implements Func1<Object, Object> {
        final Scheduler scheduler;

        public AddTimestamped(Scheduler scheduler2) {
            this.scheduler = scheduler2;
        }

        public Object call(Object obj) {
            return new Timestamped(this.scheduler.now(), obj);
        }
    }

    static final class RemoveTimestamped implements Func1<Object, Object> {
        RemoveTimestamped() {
        }

        public Object call(Object obj) {
            return ((Timestamped) obj).getValue();
        }
    }

    static final class DefaultOnAdd<T> implements Action1<SubjectSubscriptionManager.SubjectObserver<T>> {
        final BoundedState<T> state;

        public DefaultOnAdd(BoundedState<T> boundedState) {
            this.state = boundedState;
        }

        public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
            subjectObserver.index(this.state.replayObserverFromIndex(this.state.head(), subjectObserver));
        }
    }

    static final class TimedOnAdd<T> implements Action1<SubjectSubscriptionManager.SubjectObserver<T>> {
        final Scheduler scheduler;
        final BoundedState<T> state;

        public TimedOnAdd(BoundedState<T> boundedState, Scheduler scheduler2) {
            this.state = boundedState;
            this.scheduler = scheduler2;
        }

        public void call(SubjectSubscriptionManager.SubjectObserver<T> subjectObserver) {
            NodeList.Node<Object> node;
            if (!this.state.terminated) {
                node = this.state.replayObserverFromIndexTest(this.state.head(), subjectObserver, this.scheduler.now());
            } else {
                node = this.state.replayObserverFromIndex(this.state.head(), subjectObserver);
            }
            subjectObserver.index(node);
        }
    }

    static final class NodeList<T> {
        final Node<T> head = new Node<>(null);
        int size;
        Node<T> tail = this.head;

        NodeList() {
        }

        static final class Node<T> {
            volatile Node<T> next;
            final T value;

            Node(T t) {
                this.value = t;
            }
        }

        public void addLast(T t) {
            Node<T> node = this.tail;
            Node<T> node2 = new Node<>(t);
            node.next = node2;
            this.tail = node2;
            this.size++;
        }

        public T removeFirst() {
            if (this.head.next != null) {
                Node<T> node = this.head.next;
                this.head.next = node.next;
                if (this.head.next == null) {
                    this.tail = this.head;
                }
                this.size--;
                return node.value;
            }
            throw new IllegalStateException("Empty!");
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public int size() {
            return this.size;
        }

        public void clear() {
            this.tail = this.head;
            this.size = 0;
        }
    }

    static final class EmptyEvictionPolicy implements EvictionPolicy {
        public void evict(NodeList<Object> nodeList) {
        }

        public void evictFinal(NodeList<Object> nodeList) {
        }

        public boolean test(Object obj, long j) {
            return true;
        }

        EmptyEvictionPolicy() {
        }
    }

    @Experimental
    public boolean hasThrowable() {
        return this.ssm.nl.isError(this.ssm.get());
    }

    @Experimental
    public boolean hasCompleted() {
        NotificationLite<T> notificationLite = this.ssm.nl;
        Object obj = this.ssm.get();
        return obj != null && !notificationLite.isError(obj);
    }

    @Experimental
    public Throwable getThrowable() {
        NotificationLite<T> notificationLite = this.ssm.nl;
        Object obj = this.ssm.get();
        if (notificationLite.isError(obj)) {
            return notificationLite.getError(obj);
        }
        return null;
    }

    @Experimental
    public int size() {
        return this.state.size();
    }

    @Experimental
    public boolean hasAnyValue() {
        return !this.state.isEmpty();
    }

    @Experimental
    public boolean hasValue() {
        return hasAnyValue();
    }

    @Experimental
    public T[] getValues(T[] tArr) {
        return this.state.toArray(tArr);
    }

    public T getValue() {
        return this.state.latest();
    }
}
