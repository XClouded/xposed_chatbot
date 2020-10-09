package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.internal.util.LinkedArrayList;
import rx.subscriptions.SerialSubscription;

public final class CachedObservable<T> extends Observable<T> {
    private CacheState<T> state;

    public static <T> CachedObservable<T> from(Observable<? extends T> observable) {
        return from(observable, 16);
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> observable, int i) {
        if (i >= 1) {
            CacheState cacheState = new CacheState(observable, i);
            return new CachedObservable<>(new CachedSubscribe(cacheState), cacheState);
        }
        throw new IllegalArgumentException("capacityHint > 0 required");
    }

    private CachedObservable(Observable.OnSubscribe<T> onSubscribe, CacheState<T> cacheState) {
        super(onSubscribe);
        this.state = cacheState;
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected() {
        return this.state.isConnected;
    }

    /* access modifiers changed from: package-private */
    public boolean hasObservers() {
        return this.state.producers.length != 0;
    }

    /* access modifiers changed from: package-private */
    public int cachedEventCount() {
        return this.state.size();
    }

    static final class CacheState<T> extends LinkedArrayList implements Observer<T> {
        static final ReplayProducer<?>[] EMPTY = new ReplayProducer[0];
        final SerialSubscription connection = new SerialSubscription();
        volatile boolean isConnected;
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile ReplayProducer<?>[] producers = EMPTY;
        final Observable<? extends T> source;
        boolean sourceDone;

        public CacheState(Observable<? extends T> observable, int i) {
            super(i);
            this.source = observable;
        }

        public void addProducer(ReplayProducer<T> replayProducer) {
            synchronized (this.connection) {
                ReplayProducer<?>[] replayProducerArr = this.producers;
                int length = replayProducerArr.length;
                ReplayProducer<?>[] replayProducerArr2 = new ReplayProducer[(length + 1)];
                System.arraycopy(replayProducerArr, 0, replayProducerArr2, 0, length);
                replayProducerArr2[length] = replayProducer;
                this.producers = replayProducerArr2;
            }
        }

        public void removeProducer(ReplayProducer<T> replayProducer) {
            synchronized (this.connection) {
                ReplayProducer<?>[] replayProducerArr = this.producers;
                int length = replayProducerArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (replayProducerArr[i2].equals(replayProducer)) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        this.producers = EMPTY;
                        return;
                    }
                    ReplayProducer<?>[] replayProducerArr2 = new ReplayProducer[(length - 1)];
                    System.arraycopy(replayProducerArr, 0, replayProducerArr2, 0, i);
                    System.arraycopy(replayProducerArr, i + 1, replayProducerArr2, i, (length - i) - 1);
                    this.producers = replayProducerArr2;
                }
            }
        }

        public void connect() {
            AnonymousClass1 r0 = new Subscriber<T>() {
                public void onNext(T t) {
                    CacheState.this.onNext(t);
                }

                public void onError(Throwable th) {
                    CacheState.this.onError(th);
                }

                public void onCompleted() {
                    CacheState.this.onCompleted();
                }
            };
            this.connection.set(r0);
            this.source.unsafeSubscribe(r0);
            this.isConnected = true;
        }

        public void onNext(T t) {
            if (!this.sourceDone) {
                add(this.nl.next(t));
                dispatch();
            }
        }

        public void onError(Throwable th) {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(this.nl.error(th));
                this.connection.unsubscribe();
                dispatch();
            }
        }

        public void onCompleted() {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(this.nl.completed());
                this.connection.unsubscribe();
                dispatch();
            }
        }

        /* access modifiers changed from: package-private */
        public void dispatch() {
            for (ReplayProducer<?> replay : this.producers) {
                replay.replay();
            }
        }
    }

    static final class CachedSubscribe<T> extends AtomicBoolean implements Observable.OnSubscribe<T> {
        private static final long serialVersionUID = -2817751667698696782L;
        final CacheState<T> state;

        public CachedSubscribe(CacheState<T> cacheState) {
            this.state = cacheState;
        }

        public void call(Subscriber<? super T> subscriber) {
            ReplayProducer replayProducer = new ReplayProducer(subscriber, this.state);
            this.state.addProducer(replayProducer);
            subscriber.add(replayProducer);
            subscriber.setProducer(replayProducer);
            if (!get() && compareAndSet(false, true)) {
                this.state.connect();
            }
        }
    }

    static final class ReplayProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -2557562030197141021L;
        final Subscriber<? super T> child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        boolean emitting;
        int index;
        boolean missed;
        final CacheState<T> state;

        public ReplayProducer(Subscriber<? super T> subscriber, CacheState<T> cacheState) {
            this.child = subscriber;
            this.state = cacheState;
        }

        public void request(long j) {
            long j2;
            long j3;
            do {
                j2 = get();
                if (j2 >= 0) {
                    j3 = j2 + j;
                    if (j3 < 0) {
                        j3 = Long.MAX_VALUE;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(j2, j3));
            replay();
        }

        public long produced(long j) {
            return addAndGet(-j);
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            if (get() >= 0 && getAndSet(-1) >= 0) {
                this.state.removeProducer(this);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:105:0x00e9, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:119:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:121:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
            r4 = r1.state.nl;
            r5 = r1.child;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0016, code lost:
            r6 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001e, code lost:
            if (r6 >= 0) goto L_0x0021;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0020, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0021, code lost:
            r0 = r1.state.size();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0027, code lost:
            if (r0 == 0) goto L_0x00c7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0029, code lost:
            r10 = r1.currentBuffer;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x002b, code lost:
            if (r10 != null) goto L_0x0035;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x002d, code lost:
            r10 = r1.state.head();
            r1.currentBuffer = r10;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0035, code lost:
            r11 = r10.length - 1;
            r12 = r1.index;
            r13 = r1.currentIndexInBuffer;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x003d, code lost:
            if (r6 != 0) goto L_0x005f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003f, code lost:
            r0 = r10[r13];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0045, code lost:
            if (r4.isCompleted(r0) == false) goto L_0x004e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0047, code lost:
            r5.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x004d, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0052, code lost:
            if (r4.isError(r0) == false) goto L_0x00c7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0054, code lost:
            r5.onError(r4.getError(r0));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x005e, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0061, code lost:
            if (r6 <= 0) goto L_0x00c7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0063, code lost:
            r14 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0064, code lost:
            if (r12 >= r0) goto L_0x00b6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0068, code lost:
            if (r6 <= 0) goto L_0x00b6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x006e, code lost:
            if (r5.isUnsubscribed() == false) goto L_0x0071;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0070, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0071, code lost:
            if (r13 != r11) goto L_0x007a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0073, code lost:
            r10 = (java.lang.Object[]) r10[r11];
            r13 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x007a, code lost:
            r15 = r10[r13];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0080, code lost:
            if (r4.accept(r5, r15) == false) goto L_0x0089;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0085, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0086, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x0087, code lost:
            r6 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x0089, code lost:
            r13 = r13 + 1;
            r12 = r12 + 1;
            r6 = r6 - 1;
            r14 = r14 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x0093, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x0094, code lost:
            r6 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
            rx.exceptions.Exceptions.throwIfFatal(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:?, code lost:
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x009f, code lost:
            if (r4.isError(r15) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x00a7, code lost:
            r5.onError(rx.exceptions.OnErrorThrowable.addValueAsLastCause(r0, r4.getValue(r15)));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x00b3, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x00b4, code lost:
            r2 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:0x00ba, code lost:
            if (r5.isUnsubscribed() == false) goto L_0x00bd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x00bc, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x00bd, code lost:
            r1.index = r12;
            r1.currentIndexInBuffer = r13;
            r1.currentBuffer = r10;
            produced((long) r14);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x00c7, code lost:
            monitor-enter(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x00ca, code lost:
            if (r1.missed != false) goto L_0x00d0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x00cc, code lost:
            r1.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:?, code lost:
            monitor-exit(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:82:0x00cf, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:?, code lost:
            r1.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:85:0x00d2, code lost:
            monitor-exit(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:0x00d5, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:0x00d6, code lost:
            r2 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:89:?, code lost:
            monitor-exit(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:0x00d9, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:93:0x00db, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x00dd, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:95:0x00de, code lost:
            r2 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:96:0x00df, code lost:
            if (r2 == false) goto L_0x00e1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:97:0x00e1, code lost:
            monitor-enter(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:?, code lost:
            r1.emitting = false;
         */
        /* JADX WARNING: Removed duplicated region for block: B:121:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x00a7 A[Catch:{ all -> 0x00d9 }] */
        /* JADX WARNING: Removed duplicated region for block: B:97:0x00e1  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay() {
            /*
                r17 = this;
                r1 = r17
                monitor-enter(r17)
                boolean r0 = r1.emitting     // Catch:{ all -> 0x00ea }
                r2 = 1
                if (r0 == 0) goto L_0x000c
                r1.missed = r2     // Catch:{ all -> 0x00ea }
                monitor-exit(r17)     // Catch:{ all -> 0x00ea }
                return
            L_0x000c:
                r1.emitting = r2     // Catch:{ all -> 0x00ea }
                monitor-exit(r17)     // Catch:{ all -> 0x00ea }
                r3 = 0
                rx.internal.operators.CachedObservable$CacheState<T> r0 = r1.state     // Catch:{ all -> 0x00dd }
                rx.internal.operators.NotificationLite<T> r4 = r0.nl     // Catch:{ all -> 0x00dd }
                rx.Subscriber<? super T> r5 = r1.child     // Catch:{ all -> 0x00dd }
            L_0x0016:
                long r6 = r17.get()     // Catch:{ all -> 0x00dd }
                r8 = 0
                int r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r0 >= 0) goto L_0x0021
                return
            L_0x0021:
                rx.internal.operators.CachedObservable$CacheState<T> r0 = r1.state     // Catch:{ all -> 0x00dd }
                int r0 = r0.size()     // Catch:{ all -> 0x00dd }
                if (r0 == 0) goto L_0x00c7
                java.lang.Object[] r10 = r1.currentBuffer     // Catch:{ all -> 0x00dd }
                if (r10 != 0) goto L_0x0035
                rx.internal.operators.CachedObservable$CacheState<T> r10 = r1.state     // Catch:{ all -> 0x00dd }
                java.lang.Object[] r10 = r10.head()     // Catch:{ all -> 0x00dd }
                r1.currentBuffer = r10     // Catch:{ all -> 0x00dd }
            L_0x0035:
                int r11 = r10.length     // Catch:{ all -> 0x00dd }
                int r11 = r11 - r2
                int r12 = r1.index     // Catch:{ all -> 0x00dd }
                int r13 = r1.currentIndexInBuffer     // Catch:{ all -> 0x00dd }
                int r14 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r14 != 0) goto L_0x005f
                r0 = r10[r13]     // Catch:{ all -> 0x00dd }
                boolean r6 = r4.isCompleted(r0)     // Catch:{ all -> 0x00dd }
                if (r6 == 0) goto L_0x004e
                r5.onCompleted()     // Catch:{ all -> 0x00dd }
                r17.unsubscribe()     // Catch:{ all -> 0x00d9 }
                return
            L_0x004e:
                boolean r6 = r4.isError(r0)     // Catch:{ all -> 0x00dd }
                if (r6 == 0) goto L_0x00c7
                java.lang.Throwable r0 = r4.getError(r0)     // Catch:{ all -> 0x00dd }
                r5.onError(r0)     // Catch:{ all -> 0x00dd }
                r17.unsubscribe()     // Catch:{ all -> 0x00d9 }
                return
            L_0x005f:
                int r14 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r14 <= 0) goto L_0x00c7
                r14 = 0
            L_0x0064:
                if (r12 >= r0) goto L_0x00b6
                int r15 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r15 <= 0) goto L_0x00b6
                boolean r15 = r5.isUnsubscribed()     // Catch:{ all -> 0x00dd }
                if (r15 == 0) goto L_0x0071
                return
            L_0x0071:
                if (r13 != r11) goto L_0x007a
                r10 = r10[r11]     // Catch:{ all -> 0x00dd }
                java.lang.Object[] r10 = (java.lang.Object[]) r10     // Catch:{ all -> 0x00dd }
                java.lang.Object[] r10 = (java.lang.Object[]) r10     // Catch:{ all -> 0x00dd }
                r13 = 0
            L_0x007a:
                r15 = r10[r13]     // Catch:{ all -> 0x00dd }
                boolean r16 = r4.accept(r5, r15)     // Catch:{ Throwable -> 0x0093 }
                if (r16 == 0) goto L_0x0089
                r17.unsubscribe()     // Catch:{ Throwable -> 0x0086 }
                return
            L_0x0086:
                r0 = move-exception
                r6 = 1
                goto L_0x0095
            L_0x0089:
                int r13 = r13 + 1
                int r12 = r12 + 1
                r15 = 1
                long r6 = r6 - r15
                int r14 = r14 + 1
                goto L_0x0064
            L_0x0093:
                r0 = move-exception
                r6 = 0
            L_0x0095:
                rx.exceptions.Exceptions.throwIfFatal(r0)     // Catch:{ all -> 0x00b3 }
                r17.unsubscribe()     // Catch:{ all -> 0x00d9 }
                boolean r6 = r4.isError(r15)     // Catch:{ all -> 0x00d9 }
                if (r6 != 0) goto L_0x00b2
                boolean r6 = r4.isCompleted(r15)     // Catch:{ all -> 0x00d9 }
                if (r6 != 0) goto L_0x00b2
                java.lang.Object r4 = r4.getValue(r15)     // Catch:{ all -> 0x00d9 }
                java.lang.Throwable r0 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r0, r4)     // Catch:{ all -> 0x00d9 }
                r5.onError(r0)     // Catch:{ all -> 0x00d9 }
            L_0x00b2:
                return
            L_0x00b3:
                r0 = move-exception
                r2 = r6
                goto L_0x00df
            L_0x00b6:
                boolean r0 = r5.isUnsubscribed()     // Catch:{ all -> 0x00dd }
                if (r0 == 0) goto L_0x00bd
                return
            L_0x00bd:
                r1.index = r12     // Catch:{ all -> 0x00dd }
                r1.currentIndexInBuffer = r13     // Catch:{ all -> 0x00dd }
                r1.currentBuffer = r10     // Catch:{ all -> 0x00dd }
                long r6 = (long) r14     // Catch:{ all -> 0x00dd }
                r1.produced(r6)     // Catch:{ all -> 0x00dd }
            L_0x00c7:
                monitor-enter(r17)     // Catch:{ all -> 0x00dd }
                boolean r0 = r1.missed     // Catch:{ all -> 0x00d5 }
                if (r0 != 0) goto L_0x00d0
                r1.emitting = r3     // Catch:{ all -> 0x00d5 }
                monitor-exit(r17)     // Catch:{ all -> 0x00db }
                return
            L_0x00d0:
                r1.missed = r3     // Catch:{ all -> 0x00d5 }
                monitor-exit(r17)     // Catch:{ all -> 0x00d5 }
                goto L_0x0016
            L_0x00d5:
                r0 = move-exception
                r2 = 0
            L_0x00d7:
                monitor-exit(r17)     // Catch:{ all -> 0x00db }
                throw r0     // Catch:{ all -> 0x00d9 }
            L_0x00d9:
                r0 = move-exception
                goto L_0x00df
            L_0x00db:
                r0 = move-exception
                goto L_0x00d7
            L_0x00dd:
                r0 = move-exception
                r2 = 0
            L_0x00df:
                if (r2 != 0) goto L_0x00e9
                monitor-enter(r17)
                r1.emitting = r3     // Catch:{ all -> 0x00e6 }
                monitor-exit(r17)     // Catch:{ all -> 0x00e6 }
                goto L_0x00e9
            L_0x00e6:
                r0 = move-exception
                monitor-exit(r17)     // Catch:{ all -> 0x00e6 }
                throw r0
            L_0x00e9:
                throw r0
            L_0x00ea:
                r0 = move-exception
                monitor-exit(r17)     // Catch:{ all -> 0x00ea }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.CachedObservable.ReplayProducer.replay():void");
        }
    }
}
