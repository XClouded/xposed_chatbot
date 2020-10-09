package rx.internal.operators;

import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Timestamped;
import rx.subscriptions.Subscriptions;

public final class OperatorReplay<T> extends ConnectableObservable<T> {
    static final Func0 DEFAULT_UNBOUNDED_FACTORY = new Func0() {
        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    };
    final Func0<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Observable<? extends T> source;

    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerProducer<T> innerProducer);
    }

    public static <T, U, R> Observable<R> multicastSelector(final Func0<? extends ConnectableObservable<U>> func0, final Func1<? super Observable<U>, ? extends Observable<R>> func1) {
        return Observable.create(new Observable.OnSubscribe<R>() {
            public void call(final Subscriber<? super R> subscriber) {
                try {
                    ConnectableObservable connectableObservable = (ConnectableObservable) func0.call();
                    ((Observable) func1.call(connectableObservable)).subscribe(subscriber);
                    connectableObservable.connect(new Action1<Subscription>() {
                        public void call(Subscription subscription) {
                            subscriber.add(subscription);
                        }
                    });
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    subscriber.onError(th);
                }
            }
        });
    }

    public static <T> ConnectableObservable<T> observeOn(final ConnectableObservable<T> connectableObservable, Scheduler scheduler) {
        final Observable<T> observeOn = connectableObservable.observeOn(scheduler);
        return new ConnectableObservable<T>(new Observable.OnSubscribe<T>() {
            public void call(final Subscriber<? super T> subscriber) {
                observeOn.unsafeSubscribe(new Subscriber<T>(subscriber) {
                    public void onNext(T t) {
                        subscriber.onNext(t);
                    }

                    public void onError(Throwable th) {
                        subscriber.onError(th);
                    }

                    public void onCompleted() {
                        subscriber.onCompleted();
                    }
                });
            }
        }) {
            public void connect(Action1<? super Subscription> action1) {
                connectableObservable.connect(action1);
            }
        };
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable) {
        return create(observable, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable, final int i) {
        if (i == Integer.MAX_VALUE) {
            return create(observable);
        }
        return create(observable, new Func0<ReplayBuffer<T>>() {
            public ReplayBuffer<T> call() {
                return new SizeBoundReplayBuffer(i);
            }
        });
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable, long j, TimeUnit timeUnit, Scheduler scheduler) {
        return create(observable, j, timeUnit, scheduler, Integer.MAX_VALUE);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable, long j, TimeUnit timeUnit, final Scheduler scheduler, final int i) {
        final long millis = timeUnit.toMillis(j);
        return create(observable, new Func0<ReplayBuffer<T>>() {
            public ReplayBuffer<T> call() {
                return new SizeAndTimeBoundReplayBuffer(i, millis, scheduler);
            }
        });
    }

    static <T> ConnectableObservable<T> create(Observable<? extends T> observable, final Func0<? extends ReplayBuffer<T>> func0) {
        final AtomicReference atomicReference = new AtomicReference();
        return new OperatorReplay(new Observable.OnSubscribe<T>() {
            /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void call(rx.Subscriber<? super T> r5) {
                /*
                    r4 = this;
                L_0x0000:
                    java.util.concurrent.atomic.AtomicReference r0 = r0
                    java.lang.Object r0 = r0.get()
                    rx.internal.operators.OperatorReplay$ReplaySubscriber r0 = (rx.internal.operators.OperatorReplay.ReplaySubscriber) r0
                    if (r0 != 0) goto L_0x0026
                    rx.internal.operators.OperatorReplay$ReplaySubscriber r1 = new rx.internal.operators.OperatorReplay$ReplaySubscriber
                    java.util.concurrent.atomic.AtomicReference r2 = r0
                    rx.functions.Func0 r3 = r4
                    java.lang.Object r3 = r3.call()
                    rx.internal.operators.OperatorReplay$ReplayBuffer r3 = (rx.internal.operators.OperatorReplay.ReplayBuffer) r3
                    r1.<init>(r2, r3)
                    r1.init()
                    java.util.concurrent.atomic.AtomicReference r2 = r0
                    boolean r0 = r2.compareAndSet(r0, r1)
                    if (r0 != 0) goto L_0x0025
                    goto L_0x0000
                L_0x0025:
                    r0 = r1
                L_0x0026:
                    rx.internal.operators.OperatorReplay$InnerProducer r1 = new rx.internal.operators.OperatorReplay$InnerProducer
                    r1.<init>(r0, r5)
                    r0.add(r1)
                    r5.add(r1)
                    r5.setProducer(r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.AnonymousClass7.call(rx.Subscriber):void");
            }
        }, observable, atomicReference, func0);
    }

    private OperatorReplay(Observable.OnSubscribe<T> onSubscribe, Observable<? extends T> observable, AtomicReference<ReplaySubscriber<T>> atomicReference, Func0<? extends ReplayBuffer<T>> func0) {
        super(onSubscribe);
        this.source = observable;
        this.current = atomicReference;
        this.bufferFactory = func0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(rx.functions.Action1<? super rx.Subscription> r5) {
        /*
            r4 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorReplay$ReplaySubscriber<T>> r0 = r4.current
            java.lang.Object r0 = r0.get()
            rx.internal.operators.OperatorReplay$ReplaySubscriber r0 = (rx.internal.operators.OperatorReplay.ReplaySubscriber) r0
            if (r0 == 0) goto L_0x0010
            boolean r1 = r0.isUnsubscribed()
            if (r1 == 0) goto L_0x002c
        L_0x0010:
            rx.internal.operators.OperatorReplay$ReplaySubscriber r1 = new rx.internal.operators.OperatorReplay$ReplaySubscriber
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorReplay$ReplaySubscriber<T>> r2 = r4.current
            rx.functions.Func0<? extends rx.internal.operators.OperatorReplay$ReplayBuffer<T>> r3 = r4.bufferFactory
            java.lang.Object r3 = r3.call()
            rx.internal.operators.OperatorReplay$ReplayBuffer r3 = (rx.internal.operators.OperatorReplay.ReplayBuffer) r3
            r1.<init>(r2, r3)
            r1.init()
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorReplay$ReplaySubscriber<T>> r2 = r4.current
            boolean r0 = r2.compareAndSet(r0, r1)
            if (r0 != 0) goto L_0x002b
            goto L_0x0000
        L_0x002b:
            r0 = r1
        L_0x002c:
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.get()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x003f
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.compareAndSet(r3, r2)
            if (r1 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r2 = 0
        L_0x0040:
            r5.call(r0)
            if (r2 == 0) goto L_0x004a
            rx.Observable<? extends T> r5 = r4.source
            r5.unsafeSubscribe(r0)
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.connect(rx.functions.Action1):void");
    }

    static final class ReplaySubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY = new InnerProducer[0];
        static final InnerProducer[] TERMINATED = new InnerProducer[0];
        final ReplayBuffer<T> buffer;
        boolean done;
        boolean emitting;
        long maxChildRequested;
        long maxUpstreamRequested;
        boolean missed;
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile Producer producer;
        final AtomicReference<InnerProducer[]> producers = new AtomicReference<>(EMPTY);
        final AtomicBoolean shouldConnect = new AtomicBoolean();

        public ReplaySubscriber(AtomicReference<ReplaySubscriber<T>> atomicReference, ReplayBuffer<T> replayBuffer) {
            this.buffer = replayBuffer;
            request(0);
        }

        /* access modifiers changed from: package-private */
        public void init() {
            add(Subscriptions.create(new Action0() {
                public void call() {
                    ReplaySubscriber.this.producers.getAndSet(ReplaySubscriber.TERMINATED);
                }
            }));
        }

        /* access modifiers changed from: package-private */
        public boolean add(InnerProducer<T> innerProducer) {
            InnerProducer[] innerProducerArr;
            InnerProducer[] innerProducerArr2;
            if (innerProducer != null) {
                do {
                    innerProducerArr = this.producers.get();
                    if (innerProducerArr == TERMINATED) {
                        return false;
                    }
                    int length = innerProducerArr.length;
                    innerProducerArr2 = new InnerProducer[(length + 1)];
                    System.arraycopy(innerProducerArr, 0, innerProducerArr2, 0, length);
                    innerProducerArr2[length] = innerProducer;
                } while (!this.producers.compareAndSet(innerProducerArr, innerProducerArr2));
                return true;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: package-private */
        public void remove(InnerProducer<T> innerProducer) {
            InnerProducer[] innerProducerArr;
            InnerProducer[] innerProducerArr2;
            do {
                innerProducerArr = this.producers.get();
                if (innerProducerArr != EMPTY && innerProducerArr != TERMINATED) {
                    int i = -1;
                    int length = innerProducerArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerProducerArr[i2].equals(innerProducer)) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            innerProducerArr2 = EMPTY;
                        } else {
                            InnerProducer[] innerProducerArr3 = new InnerProducer[(length - 1)];
                            System.arraycopy(innerProducerArr, 0, innerProducerArr3, 0, i);
                            System.arraycopy(innerProducerArr, i + 1, innerProducerArr3, i, (length - i) - 1);
                            innerProducerArr2 = innerProducerArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.producers.compareAndSet(innerProducerArr, innerProducerArr2));
        }

        public void setProducer(Producer producer2) {
            if (this.producer == null) {
                this.producer = producer2;
                manageRequests();
                replay();
                return;
            }
            throw new IllegalStateException("Only a single producer can be set on a Subscriber.");
        }

        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                replay();
            }
        }

        public void onError(Throwable th) {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.error(th);
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.complete();
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0018, code lost:
            if (isUnsubscribed() == false) goto L_0x001b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001a, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x001b, code lost:
            r0 = r12.producers.get();
            r1 = r12.maxChildRequested;
            r3 = r0.length;
            r8 = 0;
            r7 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002b, code lost:
            if (r7 >= r3) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
            r8 = java.lang.Math.max(r8, r0[r7].totalRequested.get());
            r7 = r7 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003c, code lost:
            r10 = r12.maxUpstreamRequested;
            r0 = r12.producer;
            r1 = r8 - r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0044, code lost:
            if (r1 == 0) goto L_0x0067;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0046, code lost:
            r12.maxChildRequested = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0048, code lost:
            if (r0 == null) goto L_0x0059;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004c, code lost:
            if (r10 == 0) goto L_0x0055;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x004e, code lost:
            r12.maxUpstreamRequested = 0;
            r0.request(r10 + r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0055, code lost:
            r0.request(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0059, code lost:
            r10 = r10 + r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x005d, code lost:
            if (r10 >= 0) goto L_0x0064;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x005f, code lost:
            r10 = Long.MAX_VALUE;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0064, code lost:
            r12.maxUpstreamRequested = r10;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0069, code lost:
            if (r10 == 0) goto L_0x0072;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x006b, code lost:
            if (r0 == null) goto L_0x0072;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x006d, code lost:
            r12.maxUpstreamRequested = 0;
            r0.request(r10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0072, code lost:
            monitor-enter(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0075, code lost:
            if (r12.missed != false) goto L_0x007b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0077, code lost:
            r12.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0079, code lost:
            monitor-exit(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x007a, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x007b, code lost:
            r12.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x007d, code lost:
            monitor-exit(r12);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void manageRequests() {
            /*
                r12 = this;
                boolean r0 = r12.isUnsubscribed()
                if (r0 == 0) goto L_0x0007
                return
            L_0x0007:
                monitor-enter(r12)
                boolean r0 = r12.emitting     // Catch:{ all -> 0x0082 }
                r1 = 1
                if (r0 == 0) goto L_0x0011
                r12.missed = r1     // Catch:{ all -> 0x0082 }
                monitor-exit(r12)     // Catch:{ all -> 0x0082 }
                return
            L_0x0011:
                r12.emitting = r1     // Catch:{ all -> 0x0082 }
                monitor-exit(r12)     // Catch:{ all -> 0x0082 }
            L_0x0014:
                boolean r0 = r12.isUnsubscribed()
                if (r0 == 0) goto L_0x001b
                return
            L_0x001b:
                java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorReplay$InnerProducer[]> r0 = r12.producers
                java.lang.Object r0 = r0.get()
                rx.internal.operators.OperatorReplay$InnerProducer[] r0 = (rx.internal.operators.OperatorReplay.InnerProducer[]) r0
                long r1 = r12.maxChildRequested
                int r3 = r0.length
                r4 = 0
                r5 = 0
                r8 = r5
                r7 = 0
            L_0x002b:
                if (r7 >= r3) goto L_0x003c
                r10 = r0[r7]
                java.util.concurrent.atomic.AtomicLong r10 = r10.totalRequested
                long r10 = r10.get()
                long r8 = java.lang.Math.max(r8, r10)
                int r7 = r7 + 1
                goto L_0x002b
            L_0x003c:
                long r10 = r12.maxUpstreamRequested
                rx.Producer r0 = r12.producer
                long r1 = r8 - r1
                int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
                if (r3 == 0) goto L_0x0067
                r12.maxChildRequested = r8
                if (r0 == 0) goto L_0x0059
                int r3 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
                if (r3 == 0) goto L_0x0055
                r12.maxUpstreamRequested = r5
                long r10 = r10 + r1
                r0.request(r10)
                goto L_0x0072
            L_0x0055:
                r0.request(r1)
                goto L_0x0072
            L_0x0059:
                r0 = 0
                long r10 = r10 + r1
                int r0 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
                if (r0 >= 0) goto L_0x0064
                r10 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            L_0x0064:
                r12.maxUpstreamRequested = r10
                goto L_0x0072
            L_0x0067:
                int r1 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
                if (r1 == 0) goto L_0x0072
                if (r0 == 0) goto L_0x0072
                r12.maxUpstreamRequested = r5
                r0.request(r10)
            L_0x0072:
                monitor-enter(r12)
                boolean r0 = r12.missed     // Catch:{ all -> 0x007f }
                if (r0 != 0) goto L_0x007b
                r12.emitting = r4     // Catch:{ all -> 0x007f }
                monitor-exit(r12)     // Catch:{ all -> 0x007f }
                return
            L_0x007b:
                r12.missed = r4     // Catch:{ all -> 0x007f }
                monitor-exit(r12)     // Catch:{ all -> 0x007f }
                goto L_0x0014
            L_0x007f:
                r0 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x007f }
                throw r0
            L_0x0082:
                r0 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x0082 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.ReplaySubscriber.manageRequests():void");
        }

        /* access modifiers changed from: package-private */
        public void replay() {
            for (InnerProducer replay : this.producers.get()) {
                this.buffer.replay(replay);
            }
        }
    }

    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested = new AtomicLong();

        public InnerProducer(ReplaySubscriber<T> replaySubscriber, Subscriber<? super T> subscriber) {
            this.parent = replaySubscriber;
            this.child = subscriber;
        }

        public void request(long j) {
            long j2;
            long j3;
            if (j >= 0) {
                do {
                    j2 = get();
                    if (j2 != Long.MIN_VALUE) {
                        if (j2 < 0 || j != 0) {
                            j3 = j2 + j;
                            if (j3 < 0) {
                                j3 = Long.MAX_VALUE;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(j2, j3));
                addTotalRequested(j);
                this.parent.manageRequests();
                this.parent.buffer.replay(this);
            }
        }

        /* access modifiers changed from: package-private */
        public void addTotalRequested(long j) {
            long j2;
            long j3;
            do {
                j2 = this.totalRequested.get();
                j3 = j2 + j;
                if (j3 < 0) {
                    j3 = Long.MAX_VALUE;
                }
            } while (!this.totalRequested.compareAndSet(j2, j3));
        }

        public long produced(long j) {
            long j2;
            long j3;
            if (j > 0) {
                do {
                    j2 = get();
                    if (j2 == Long.MIN_VALUE) {
                        return Long.MIN_VALUE;
                    }
                    j3 = j2 - j;
                    if (j3 < 0) {
                        throw new IllegalStateException("More produced (" + j + ") than requested (" + j2 + Operators.BRACKET_END_STR);
                    }
                } while (!compareAndSet(j2, j3));
                return j3;
            }
            throw new IllegalArgumentException("Cant produce zero or less");
        }

        public boolean isUnsubscribed() {
            return get() == Long.MIN_VALUE;
        }

        public void unsubscribe() {
            if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.manageRequests();
            }
        }

        /* access modifiers changed from: package-private */
        public <U> U index() {
            return this.index;
        }
    }

    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile int size;

        public UnboundedReplayBuffer(int i) {
            super(i);
        }

        public void next(T t) {
            add(this.nl.next(t));
            this.size++;
        }

        public void error(Throwable th) {
            add(this.nl.error(th));
            this.size++;
        }

        public void complete() {
            add(this.nl.completed());
            this.size++;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
            if (r15.isUnsubscribed() == false) goto L_0x0014;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0013, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0014, code lost:
            r0 = r14.size;
            r1 = (java.lang.Integer) r15.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001d, code lost:
            if (r1 == null) goto L_0x0024;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001f, code lost:
            r1 = r1.intValue();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0024, code lost:
            r1 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0025, code lost:
            r3 = r15.get();
            r7 = r3;
            r9 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002f, code lost:
            if (r7 == 0) goto L_0x0077;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0031, code lost:
            if (r1 >= r0) goto L_0x0077;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0033, code lost:
            r11 = get(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003f, code lost:
            if (r14.nl.accept(r15.child, r11) == false) goto L_0x0042;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0041, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0046, code lost:
            if (r15.isUnsubscribed() == false) goto L_0x0049;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0048, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0049, code lost:
            r1 = r1 + 1;
            r7 = r7 - 1;
            r9 = r9 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0050, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0051, code lost:
            rx.exceptions.Exceptions.throwIfFatal(r0);
            r15.unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005d, code lost:
            if (r14.nl.isError(r11) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0067, code lost:
            r15.child.onError(rx.exceptions.OnErrorThrowable.addValueAsLastCause(r0, r14.nl.getValue(r11)));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0079, code lost:
            if (r9 == 0) goto L_0x008d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x007b, code lost:
            r15.index = java.lang.Integer.valueOf(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0088, code lost:
            if (r3 == Long.MAX_VALUE) goto L_0x008d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x008a, code lost:
            r15.produced(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x008d, code lost:
            monitor-enter(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0090, code lost:
            if (r15.missed != false) goto L_0x0096;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0092, code lost:
            r15.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0094, code lost:
            monitor-exit(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0095, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0096, code lost:
            r15.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0098, code lost:
            monitor-exit(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay(rx.internal.operators.OperatorReplay.InnerProducer<T> r15) {
            /*
                r14 = this;
                monitor-enter(r15)
                boolean r0 = r15.emitting     // Catch:{ all -> 0x009e }
                r1 = 1
                if (r0 == 0) goto L_0x000a
                r15.missed = r1     // Catch:{ all -> 0x009e }
                monitor-exit(r15)     // Catch:{ all -> 0x009e }
                return
            L_0x000a:
                r15.emitting = r1     // Catch:{ all -> 0x009e }
                monitor-exit(r15)     // Catch:{ all -> 0x009e }
            L_0x000d:
                boolean r0 = r15.isUnsubscribed()
                if (r0 == 0) goto L_0x0014
                return
            L_0x0014:
                int r0 = r14.size
                java.lang.Object r1 = r15.index()
                java.lang.Integer r1 = (java.lang.Integer) r1
                r2 = 0
                if (r1 == 0) goto L_0x0024
                int r1 = r1.intValue()
                goto L_0x0025
            L_0x0024:
                r1 = 0
            L_0x0025:
                long r3 = r15.get()
                r5 = 0
                r7 = r3
                r9 = r5
            L_0x002d:
                int r11 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
                if (r11 == 0) goto L_0x0077
                if (r1 >= r0) goto L_0x0077
                java.lang.Object r11 = r14.get(r1)
                rx.internal.operators.NotificationLite<T> r12 = r14.nl     // Catch:{ Throwable -> 0x0050 }
                rx.Subscriber<? super T> r13 = r15.child     // Catch:{ Throwable -> 0x0050 }
                boolean r12 = r12.accept(r13, r11)     // Catch:{ Throwable -> 0x0050 }
                if (r12 == 0) goto L_0x0042
                return
            L_0x0042:
                boolean r11 = r15.isUnsubscribed()
                if (r11 == 0) goto L_0x0049
                return
            L_0x0049:
                int r1 = r1 + 1
                r11 = 1
                long r7 = r7 - r11
                long r9 = r9 + r11
                goto L_0x002d
            L_0x0050:
                r0 = move-exception
                rx.exceptions.Exceptions.throwIfFatal(r0)
                r15.unsubscribe()
                rx.internal.operators.NotificationLite<T> r1 = r14.nl
                boolean r1 = r1.isError(r11)
                if (r1 != 0) goto L_0x0076
                rx.internal.operators.NotificationLite<T> r1 = r14.nl
                boolean r1 = r1.isCompleted(r11)
                if (r1 != 0) goto L_0x0076
                rx.Subscriber<? super T> r15 = r15.child
                rx.internal.operators.NotificationLite<T> r1 = r14.nl
                java.lang.Object r1 = r1.getValue(r11)
                java.lang.Throwable r0 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r0, r1)
                r15.onError(r0)
            L_0x0076:
                return
            L_0x0077:
                int r0 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
                if (r0 == 0) goto L_0x008d
                java.lang.Integer r0 = java.lang.Integer.valueOf(r1)
                r15.index = r0
                r0 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
                if (r5 == 0) goto L_0x008d
                r15.produced(r9)
            L_0x008d:
                monitor-enter(r15)
                boolean r0 = r15.missed     // Catch:{ all -> 0x009b }
                if (r0 != 0) goto L_0x0096
                r15.emitting = r2     // Catch:{ all -> 0x009b }
                monitor-exit(r15)     // Catch:{ all -> 0x009b }
                return
            L_0x0096:
                r15.missed = r2     // Catch:{ all -> 0x009b }
                monitor-exit(r15)     // Catch:{ all -> 0x009b }
                goto L_0x000d
            L_0x009b:
                r0 = move-exception
                monitor-exit(r15)     // Catch:{ all -> 0x009b }
                throw r0
            L_0x009e:
                r0 = move-exception
                monitor-exit(r15)     // Catch:{ all -> 0x009e }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.UnboundedReplayBuffer.replay(rx.internal.operators.OperatorReplay$InnerProducer):void");
        }
    }

    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final Object value;

        public Node(Object obj) {
            this.value = obj;
        }
    }

    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        final NotificationLite<T> nl = NotificationLite.instance();
        int size;
        Node tail;

        /* access modifiers changed from: package-private */
        public Object enterTransform(Object obj) {
            return obj;
        }

        /* access modifiers changed from: package-private */
        public Object leaveTransform(Object obj) {
            return obj;
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
        }

        /* access modifiers changed from: package-private */
        public void truncateFinal() {
        }

        public BoundedReplayBuffer() {
            Node node = new Node((Object) null);
            this.tail = node;
            set(node);
        }

        /* access modifiers changed from: package-private */
        public final void addLast(Node node) {
            this.tail.set(node);
            this.tail = node;
            this.size++;
        }

        /* access modifiers changed from: package-private */
        public final void removeFirst() {
            Node node = (Node) ((Node) get()).get();
            if (node != null) {
                this.size--;
                setFirst(node);
                return;
            }
            throw new IllegalStateException("Empty list!");
        }

        /* access modifiers changed from: package-private */
        public final void removeSome(int i) {
            Node node = (Node) get();
            while (i > 0) {
                node = (Node) node.get();
                i--;
                this.size--;
            }
            setFirst(node);
        }

        /* access modifiers changed from: package-private */
        public final void setFirst(Node node) {
            set(node);
        }

        public final void next(T t) {
            addLast(new Node(enterTransform(this.nl.next(t))));
            truncate();
        }

        public final void error(Throwable th) {
            addLast(new Node(enterTransform(this.nl.error(th))));
            truncateFinal();
        }

        public final void complete() {
            addLast(new Node(enterTransform(this.nl.completed())));
            truncateFinal();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
            if (r12.isUnsubscribed() == false) goto L_0x0014;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0013, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0014, code lost:
            r0 = r12.get();
            r2 = (rx.internal.operators.OperatorReplay.Node) r12.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
            if (r2 != null) goto L_0x002a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0022, code lost:
            r2 = (rx.internal.operators.OperatorReplay.Node) get();
            r12.index = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x002a, code lost:
            r5 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
            if (r0 == 0) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002f, code lost:
            r7 = (rx.internal.operators.OperatorReplay.Node) r2.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0035, code lost:
            if (r7 == null) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0037, code lost:
            r2 = leaveTransform(r7.value);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0046, code lost:
            if (r11.nl.accept(r12.child, r2) == false) goto L_0x004b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0048, code lost:
            r12.index = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x004a, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x004b, code lost:
            r5 = r5 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0052, code lost:
            if (r12.isUnsubscribed() == false) goto L_0x0055;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0054, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0055, code lost:
            r2 = r7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0057, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0058, code lost:
            r12.index = null;
            rx.exceptions.Exceptions.throwIfFatal(r0);
            r12.unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0066, code lost:
            if (r11.nl.isError(r2) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0070, code lost:
            r12.child.onError(rx.exceptions.OnErrorThrowable.addValueAsLastCause(r0, r11.nl.getValue(r2)));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0082, code lost:
            if (r5 == 0) goto L_0x0092;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0084, code lost:
            r12.index = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x008d, code lost:
            if (r0 == Long.MAX_VALUE) goto L_0x0092;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x008f, code lost:
            r12.produced(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0092, code lost:
            monitor-enter(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0096, code lost:
            if (r12.missed != false) goto L_0x009c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0098, code lost:
            r12.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x009a, code lost:
            monitor-exit(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x009b, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x009c, code lost:
            r12.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x009e, code lost:
            monitor-exit(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void replay(rx.internal.operators.OperatorReplay.InnerProducer<T> r12) {
            /*
                r11 = this;
                monitor-enter(r12)
                boolean r0 = r12.emitting     // Catch:{ all -> 0x00a4 }
                r1 = 1
                if (r0 == 0) goto L_0x000a
                r12.missed = r1     // Catch:{ all -> 0x00a4 }
                monitor-exit(r12)     // Catch:{ all -> 0x00a4 }
                return
            L_0x000a:
                r12.emitting = r1     // Catch:{ all -> 0x00a4 }
                monitor-exit(r12)     // Catch:{ all -> 0x00a4 }
            L_0x000d:
                boolean r0 = r12.isUnsubscribed()
                if (r0 == 0) goto L_0x0014
                return
            L_0x0014:
                long r0 = r12.get()
                java.lang.Object r2 = r12.index()
                rx.internal.operators.OperatorReplay$Node r2 = (rx.internal.operators.OperatorReplay.Node) r2
                r3 = 0
                if (r2 != 0) goto L_0x002a
                java.lang.Object r2 = r11.get()
                rx.internal.operators.OperatorReplay$Node r2 = (rx.internal.operators.OperatorReplay.Node) r2
                r12.index = r2
            L_0x002a:
                r5 = r3
            L_0x002b:
                int r7 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
                if (r7 == 0) goto L_0x0080
                java.lang.Object r7 = r2.get()
                rx.internal.operators.OperatorReplay$Node r7 = (rx.internal.operators.OperatorReplay.Node) r7
                if (r7 == 0) goto L_0x0080
                java.lang.Object r2 = r7.value
                java.lang.Object r2 = r11.leaveTransform(r2)
                r8 = 0
                rx.internal.operators.NotificationLite<T> r9 = r11.nl     // Catch:{ Throwable -> 0x0057 }
                rx.Subscriber<? super T> r10 = r12.child     // Catch:{ Throwable -> 0x0057 }
                boolean r9 = r9.accept(r10, r2)     // Catch:{ Throwable -> 0x0057 }
                if (r9 == 0) goto L_0x004b
                r12.index = r8     // Catch:{ Throwable -> 0x0057 }
                return
            L_0x004b:
                r8 = 1
                long r5 = r5 + r8
                boolean r2 = r12.isUnsubscribed()
                if (r2 == 0) goto L_0x0055
                return
            L_0x0055:
                r2 = r7
                goto L_0x002b
            L_0x0057:
                r0 = move-exception
                r12.index = r8
                rx.exceptions.Exceptions.throwIfFatal(r0)
                r12.unsubscribe()
                rx.internal.operators.NotificationLite<T> r1 = r11.nl
                boolean r1 = r1.isError(r2)
                if (r1 != 0) goto L_0x007f
                rx.internal.operators.NotificationLite<T> r1 = r11.nl
                boolean r1 = r1.isCompleted(r2)
                if (r1 != 0) goto L_0x007f
                rx.Subscriber<? super T> r12 = r12.child
                rx.internal.operators.NotificationLite<T> r1 = r11.nl
                java.lang.Object r1 = r1.getValue(r2)
                java.lang.Throwable r0 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r0, r1)
                r12.onError(r0)
            L_0x007f:
                return
            L_0x0080:
                int r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
                if (r7 == 0) goto L_0x0092
                r12.index = r2
                r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r4 == 0) goto L_0x0092
                r12.produced(r5)
            L_0x0092:
                monitor-enter(r12)
                boolean r0 = r12.missed     // Catch:{ all -> 0x00a1 }
                r1 = 0
                if (r0 != 0) goto L_0x009c
                r12.emitting = r1     // Catch:{ all -> 0x00a1 }
                monitor-exit(r12)     // Catch:{ all -> 0x00a1 }
                return
            L_0x009c:
                r12.missed = r1     // Catch:{ all -> 0x00a1 }
                monitor-exit(r12)     // Catch:{ all -> 0x00a1 }
                goto L_0x000d
            L_0x00a1:
                r0 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x00a1 }
                throw r0
            L_0x00a4:
                r0 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x00a4 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.BoundedReplayBuffer.replay(rx.internal.operators.OperatorReplay$InnerProducer):void");
        }

        /* access modifiers changed from: package-private */
        public final void collect(Collection<? super T> collection) {
            Node node = (Node) get();
            while (true) {
                node = (Node) node.get();
                if (node != null) {
                    Object leaveTransform = leaveTransform(node.value);
                    if (!this.nl.isCompleted(leaveTransform) && !this.nl.isError(leaveTransform)) {
                        collection.add(this.nl.getValue(leaveTransform));
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean hasError() {
            return this.tail.value != null && this.nl.isError(leaveTransform(this.tail.value));
        }

        /* access modifiers changed from: package-private */
        public boolean hasCompleted() {
            return this.tail.value != null && this.nl.isCompleted(leaveTransform(this.tail.value));
        }
    }

    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        public SizeBoundReplayBuffer(int i) {
            this.limit = i;
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAgeInMillis;
        final Scheduler scheduler;

        public SizeAndTimeBoundReplayBuffer(int i, long j, Scheduler scheduler2) {
            this.scheduler = scheduler2;
            this.limit = i;
            this.maxAgeInMillis = j;
        }

        /* access modifiers changed from: package-private */
        public Object enterTransform(Object obj) {
            return new Timestamped(this.scheduler.now(), obj);
        }

        /* access modifiers changed from: package-private */
        public Object leaveTransform(Object obj) {
            return ((Timestamped) obj).getValue();
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
            Node node;
            long now = this.scheduler.now() - this.maxAgeInMillis;
            Node node2 = (Node) get();
            Node node3 = (Node) node2.get();
            int i = 0;
            while (true) {
                Node node4 = node3;
                node = node2;
                node2 = node4;
                if (node2 != null) {
                    if (this.size <= this.limit) {
                        if (((Timestamped) node2.value).getTimestampMillis() > now) {
                            break;
                        }
                        i++;
                        this.size--;
                        node3 = (Node) node2.get();
                    } else {
                        i++;
                        this.size--;
                        node3 = (Node) node2.get();
                    }
                } else {
                    break;
                }
            }
            if (i != 0) {
                setFirst(node);
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x003c  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void truncateFinal() {
            /*
                r10 = this;
                rx.Scheduler r0 = r10.scheduler
                long r0 = r0.now()
                long r2 = r10.maxAgeInMillis
                long r0 = r0 - r2
                java.lang.Object r2 = r10.get()
                rx.internal.operators.OperatorReplay$Node r2 = (rx.internal.operators.OperatorReplay.Node) r2
                java.lang.Object r3 = r2.get()
                rx.internal.operators.OperatorReplay$Node r3 = (rx.internal.operators.OperatorReplay.Node) r3
                r4 = 0
            L_0x0016:
                r9 = r3
                r3 = r2
                r2 = r9
                if (r2 == 0) goto L_0x003a
                int r5 = r10.size
                r6 = 1
                if (r5 <= r6) goto L_0x003a
                java.lang.Object r5 = r2.value
                rx.schedulers.Timestamped r5 = (rx.schedulers.Timestamped) r5
                long r7 = r5.getTimestampMillis()
                int r5 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
                if (r5 > 0) goto L_0x003a
                int r4 = r4 + 1
                int r3 = r10.size
                int r3 = r3 - r6
                r10.size = r3
                java.lang.Object r3 = r2.get()
                rx.internal.operators.OperatorReplay$Node r3 = (rx.internal.operators.OperatorReplay.Node) r3
                goto L_0x0016
            L_0x003a:
                if (r4 == 0) goto L_0x003f
                r10.setFirst(r3)
            L_0x003f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.SizeAndTimeBoundReplayBuffer.truncateFinal():void");
        }
    }
}
