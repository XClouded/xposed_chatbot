package rx.internal.operators;

import com.taobao.weex.el.parse.Operators;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.MissingBackpressureException;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;

public final class OperatorMapNotification<T, R> implements Observable.Operator<R, T> {
    /* access modifiers changed from: private */
    public final Func0<? extends R> onCompleted;
    /* access modifiers changed from: private */
    public final Func1<? super Throwable, ? extends R> onError;
    /* access modifiers changed from: private */
    public final Func1<? super T, ? extends R> onNext;

    public OperatorMapNotification(Func1<? super T, ? extends R> func1, Func1<? super Throwable, ? extends R> func12, Func0<? extends R> func0) {
        this.onNext = func1;
        this.onError = func12;
        this.onCompleted = func0;
    }

    public Subscriber<? super T> call(final Subscriber<? super R> subscriber) {
        AnonymousClass1 r0 = new Subscriber<T>() {
            SingleEmitter<R> emitter;

            public void setProducer(Producer producer) {
                this.emitter = new SingleEmitter<>(subscriber, producer, this);
                subscriber.setProducer(this.emitter);
            }

            public void onCompleted() {
                try {
                    this.emitter.offerAndComplete(OperatorMapNotification.this.onCompleted.call());
                } catch (Throwable th) {
                    subscriber.onError(th);
                }
            }

            public void onError(Throwable th) {
                try {
                    this.emitter.offerAndComplete(OperatorMapNotification.this.onError.call(th));
                } catch (Throwable unused) {
                    subscriber.onError(th);
                }
            }

            public void onNext(T t) {
                try {
                    this.emitter.offer(OperatorMapNotification.this.onNext.call(t));
                } catch (Throwable th) {
                    subscriber.onError(OnErrorThrowable.addValueAsLastCause(th, t));
                }
            }
        };
        subscriber.add(r0);
        return r0;
    }

    static final class SingleEmitter<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -249869671366010660L;
        final Subscription cancel;
        final Subscriber<? super T> child;
        volatile boolean complete;
        boolean emitting;
        boolean missed;
        final NotificationLite<T> nl;
        final Producer producer;
        final Queue<Object> queue;

        public SingleEmitter(Subscriber<? super T> subscriber, Producer producer2, Subscription subscription) {
            this.child = subscriber;
            this.producer = producer2;
            this.cancel = subscription;
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscArrayQueue<>(2) : new ConcurrentLinkedQueue<>();
            this.nl = NotificationLite.instance();
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
            this.producer.request(j);
            drain();
        }

        /* access modifiers changed from: package-private */
        public void produced(long j) {
            long j2;
            long j3;
            do {
                j2 = get();
                if (j2 >= 0) {
                    j3 = j2 - j;
                    if (j3 < 0) {
                        throw new IllegalStateException("More produced (" + j + ") than requested (" + j2 + Operators.BRACKET_END_STR);
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(j2, j3));
        }

        public void offer(T t) {
            if (!this.queue.offer(t)) {
                this.child.onError(new MissingBackpressureException());
                unsubscribe();
                return;
            }
            drain();
        }

        public void offerAndComplete(T t) {
            if (!this.queue.offer(t)) {
                this.child.onError(new MissingBackpressureException());
                unsubscribe();
                return;
            }
            this.complete = true;
            drain();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
            r2 = get();
            r4 = r9.complete;
            r5 = r9.queue.isEmpty();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
            if (r4 == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
            if (r5 == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
            r9.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002a, code lost:
            if (r2 <= 0) goto L_0x004d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002c, code lost:
            r2 = r9.queue.poll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0032, code lost:
            if (r2 == null) goto L_0x0045;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0034, code lost:
            r9.child.onNext(r9.nl.getValue(r2));
            produced(1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0045, code lost:
            if (r4 == false) goto L_0x004d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0047, code lost:
            r9.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x004c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004d, code lost:
            monitor-enter(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0050, code lost:
            if (r9.missed != false) goto L_0x0056;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            r9.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0054, code lost:
            monitor-exit(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0055, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            r9.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0058, code lost:
            monitor-exit(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x005a, code lost:
            r2 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x005b, code lost:
            r1 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            monitor-exit(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
            throw r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x005e, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x005f, code lost:
            r8 = r2;
            r2 = r1;
            r1 = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0063, code lost:
            r2 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0065, code lost:
            r1 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0066, code lost:
            r2 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0067, code lost:
            if (r2 == false) goto L_0x0069;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0069, code lost:
            monitor-enter(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
            r9.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0071, code lost:
            throw r1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r9 = this;
                monitor-enter(r9)
                boolean r0 = r9.emitting     // Catch:{ all -> 0x0072 }
                r1 = 1
                if (r0 == 0) goto L_0x000a
                r9.missed = r1     // Catch:{ all -> 0x0072 }
                monitor-exit(r9)     // Catch:{ all -> 0x0072 }
                return
            L_0x000a:
                r9.emitting = r1     // Catch:{ all -> 0x0072 }
                r0 = 0
                r9.missed = r0     // Catch:{ all -> 0x0072 }
                monitor-exit(r9)     // Catch:{ all -> 0x0072 }
            L_0x0010:
                long r2 = r9.get()     // Catch:{ all -> 0x0065 }
                boolean r4 = r9.complete     // Catch:{ all -> 0x0065 }
                java.util.Queue<java.lang.Object> r5 = r9.queue     // Catch:{ all -> 0x0065 }
                boolean r5 = r5.isEmpty()     // Catch:{ all -> 0x0065 }
                if (r4 == 0) goto L_0x0026
                if (r5 == 0) goto L_0x0026
                rx.Subscriber<? super T> r1 = r9.child     // Catch:{ all -> 0x0065 }
                r1.onCompleted()     // Catch:{ all -> 0x0065 }
                return
            L_0x0026:
                r5 = 0
                int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
                if (r7 <= 0) goto L_0x004d
                java.util.Queue<java.lang.Object> r2 = r9.queue     // Catch:{ all -> 0x0065 }
                java.lang.Object r2 = r2.poll()     // Catch:{ all -> 0x0065 }
                if (r2 == 0) goto L_0x0045
                rx.Subscriber<? super T> r3 = r9.child     // Catch:{ all -> 0x0065 }
                rx.internal.operators.NotificationLite<T> r4 = r9.nl     // Catch:{ all -> 0x0065 }
                java.lang.Object r2 = r4.getValue(r2)     // Catch:{ all -> 0x0065 }
                r3.onNext(r2)     // Catch:{ all -> 0x0065 }
                r2 = 1
                r9.produced(r2)     // Catch:{ all -> 0x0065 }
                goto L_0x004d
            L_0x0045:
                if (r4 == 0) goto L_0x004d
                rx.Subscriber<? super T> r1 = r9.child     // Catch:{ all -> 0x0065 }
                r1.onCompleted()     // Catch:{ all -> 0x0065 }
                return
            L_0x004d:
                monitor-enter(r9)     // Catch:{ all -> 0x0065 }
                boolean r2 = r9.missed     // Catch:{ all -> 0x005a }
                if (r2 != 0) goto L_0x0056
                r9.emitting = r0     // Catch:{ all -> 0x0063 }
                monitor-exit(r9)     // Catch:{ all -> 0x0063 }
                return
            L_0x0056:
                r9.missed = r0     // Catch:{ all -> 0x005a }
                monitor-exit(r9)     // Catch:{ all -> 0x005a }
                goto L_0x0010
            L_0x005a:
                r2 = move-exception
                r1 = 0
            L_0x005c:
                monitor-exit(r9)     // Catch:{ all -> 0x0063 }
                throw r2     // Catch:{ all -> 0x005e }
            L_0x005e:
                r2 = move-exception
                r8 = r2
                r2 = r1
                r1 = r8
                goto L_0x0067
            L_0x0063:
                r2 = move-exception
                goto L_0x005c
            L_0x0065:
                r1 = move-exception
                r2 = 0
            L_0x0067:
                if (r2 != 0) goto L_0x0071
                monitor-enter(r9)
                r9.emitting = r0     // Catch:{ all -> 0x006e }
                monitor-exit(r9)     // Catch:{ all -> 0x006e }
                goto L_0x0071
            L_0x006e:
                r0 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x006e }
                throw r0
            L_0x0071:
                throw r1
            L_0x0072:
                r0 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x0072 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMapNotification.SingleEmitter.drain():void");
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.cancel.unsubscribe();
            }
        }
    }
}
