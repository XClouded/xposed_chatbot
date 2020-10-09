package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.internal.producers.ProducerArbiter;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

public final class OperatorSwitch<T> implements Observable.Operator<T, Observable<? extends T>> {

    private static final class Holder {
        static final OperatorSwitch<Object> INSTANCE = new OperatorSwitch<>();

        private Holder() {
        }
    }

    public static <T> OperatorSwitch<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorSwitch() {
    }

    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> subscriber) {
        SwitchSubscriber switchSubscriber = new SwitchSubscriber(subscriber);
        subscriber.add(switchSubscriber);
        return switchSubscriber;
    }

    private static final class SwitchSubscriber<T> extends Subscriber<Observable<? extends T>> {
        boolean active;
        final ProducerArbiter arbiter;
        InnerSubscriber<T> currentSubscriber;
        boolean emitting;
        final Object guard = new Object();
        int index;
        boolean mainDone;
        final NotificationLite<?> nl = NotificationLite.instance();
        List<Object> queue;
        final SerializedSubscriber<T> serializedChild;
        final SerialSubscription ssub;

        SwitchSubscriber(Subscriber<? super T> subscriber) {
            this.serializedChild = new SerializedSubscriber<>(subscriber);
            this.arbiter = new ProducerArbiter();
            this.ssub = new SerialSubscription();
            subscriber.add(this.ssub);
            subscriber.setProducer(new Producer() {
                public void request(long j) {
                    if (j > 0) {
                        SwitchSubscriber.this.arbiter.request(j);
                    }
                }
            });
        }

        public void onNext(Observable<? extends T> observable) {
            synchronized (this.guard) {
                int i = this.index + 1;
                this.index = i;
                this.active = true;
                this.currentSubscriber = new InnerSubscriber<>(i, this.arbiter, this);
            }
            this.ssub.set(this.currentSubscriber);
            observable.unsafeSubscribe(this.currentSubscriber);
        }

        public void onError(Throwable th) {
            this.serializedChild.onError(th);
            unsubscribe();
        }

        public void onCompleted() {
            synchronized (this.guard) {
                this.mainDone = true;
                if (!this.active) {
                    if (this.emitting) {
                        if (this.queue == null) {
                            this.queue = new ArrayList();
                        }
                        this.queue.add(this.nl.completed());
                        return;
                    }
                    List<Object> list = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    drain(list);
                    this.serializedChild.onCompleted();
                    unsubscribe();
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0028, code lost:
            r8 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            drain(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x002d, code lost:
            if (r8 == false) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x002f, code lost:
            r5.serializedChild.onNext(r6);
            r5.arbiter.produced(1);
            r8 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003c, code lost:
            r7 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
            monitor-enter(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
            r3 = r5.queue;
            r5.queue = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0043, code lost:
            if (r3 != null) goto L_0x0049;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0045, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            monitor-exit(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
            monitor-exit(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0050, code lost:
            if (r5.serializedChild.isUnsubscribed() == false) goto L_0x0060;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0052, code lost:
            r1 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0053, code lost:
            if (r1 != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0055, code lost:
            r6 = r5.guard;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0057, code lost:
            monitor-enter(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x005a, code lost:
            monitor-exit(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0060, code lost:
            r7 = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0062, code lost:
            r6 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0063, code lost:
            r1 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
            monitor-exit(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
            throw r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0066, code lost:
            r6 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0068, code lost:
            r6 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x006a, code lost:
            r6 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x006b, code lost:
            r1 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x006c, code lost:
            if (r1 == false) goto L_0x006e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x0070, code lost:
            monitor-enter(r5.guard);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x0078, code lost:
            throw r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit(T r6, int r7, rx.internal.operators.OperatorSwitch.InnerSubscriber<T> r8) {
            /*
                r5 = this;
                java.lang.Object r8 = r5.guard
                monitor-enter(r8)
                int r0 = r5.index     // Catch:{ all -> 0x0079 }
                if (r7 == r0) goto L_0x0009
                monitor-exit(r8)     // Catch:{ all -> 0x0079 }
                return
            L_0x0009:
                boolean r7 = r5.emitting     // Catch:{ all -> 0x0079 }
                if (r7 == 0) goto L_0x001f
                java.util.List<java.lang.Object> r7 = r5.queue     // Catch:{ all -> 0x0079 }
                if (r7 != 0) goto L_0x0018
                java.util.ArrayList r7 = new java.util.ArrayList     // Catch:{ all -> 0x0079 }
                r7.<init>()     // Catch:{ all -> 0x0079 }
                r5.queue = r7     // Catch:{ all -> 0x0079 }
            L_0x0018:
                java.util.List<java.lang.Object> r7 = r5.queue     // Catch:{ all -> 0x0079 }
                r7.add(r6)     // Catch:{ all -> 0x0079 }
                monitor-exit(r8)     // Catch:{ all -> 0x0079 }
                return
            L_0x001f:
                java.util.List<java.lang.Object> r7 = r5.queue     // Catch:{ all -> 0x0079 }
                r0 = 0
                r5.queue = r0     // Catch:{ all -> 0x0079 }
                r1 = 1
                r5.emitting = r1     // Catch:{ all -> 0x0079 }
                monitor-exit(r8)     // Catch:{ all -> 0x0079 }
                r8 = 1
            L_0x0029:
                r2 = 0
                r5.drain(r7)     // Catch:{ all -> 0x006a }
                if (r8 == 0) goto L_0x003c
                rx.observers.SerializedSubscriber<T> r7 = r5.serializedChild     // Catch:{ all -> 0x006a }
                r7.onNext(r6)     // Catch:{ all -> 0x006a }
                rx.internal.producers.ProducerArbiter r7 = r5.arbiter     // Catch:{ all -> 0x006a }
                r3 = 1
                r7.produced(r3)     // Catch:{ all -> 0x006a }
                r8 = 0
            L_0x003c:
                java.lang.Object r7 = r5.guard     // Catch:{ all -> 0x006a }
                monitor-enter(r7)     // Catch:{ all -> 0x006a }
                java.util.List<java.lang.Object> r3 = r5.queue     // Catch:{ all -> 0x0062 }
                r5.queue = r0     // Catch:{ all -> 0x0062 }
                if (r3 != 0) goto L_0x0049
                r5.emitting = r2     // Catch:{ all -> 0x0062 }
                monitor-exit(r7)     // Catch:{ all -> 0x0068 }
                goto L_0x0053
            L_0x0049:
                monitor-exit(r7)     // Catch:{ all -> 0x0062 }
                rx.observers.SerializedSubscriber<T> r7 = r5.serializedChild     // Catch:{ all -> 0x006a }
                boolean r7 = r7.isUnsubscribed()     // Catch:{ all -> 0x006a }
                if (r7 == 0) goto L_0x0060
                r1 = 0
            L_0x0053:
                if (r1 != 0) goto L_0x005f
                java.lang.Object r6 = r5.guard
                monitor-enter(r6)
                r5.emitting = r2     // Catch:{ all -> 0x005c }
                monitor-exit(r6)     // Catch:{ all -> 0x005c }
                goto L_0x005f
            L_0x005c:
                r7 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x005c }
                throw r7
            L_0x005f:
                return
            L_0x0060:
                r7 = r3
                goto L_0x0029
            L_0x0062:
                r6 = move-exception
                r1 = 0
            L_0x0064:
                monitor-exit(r7)     // Catch:{ all -> 0x0068 }
                throw r6     // Catch:{ all -> 0x0066 }
            L_0x0066:
                r6 = move-exception
                goto L_0x006c
            L_0x0068:
                r6 = move-exception
                goto L_0x0064
            L_0x006a:
                r6 = move-exception
                r1 = 0
            L_0x006c:
                if (r1 != 0) goto L_0x0078
                java.lang.Object r7 = r5.guard
                monitor-enter(r7)
                r5.emitting = r2     // Catch:{ all -> 0x0075 }
                monitor-exit(r7)     // Catch:{ all -> 0x0075 }
                goto L_0x0078
            L_0x0075:
                r6 = move-exception
                monitor-exit(r7)     // Catch:{ all -> 0x0075 }
                throw r6
            L_0x0078:
                throw r6
            L_0x0079:
                r6 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0079 }
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorSwitch.SwitchSubscriber.emit(java.lang.Object, int, rx.internal.operators.OperatorSwitch$InnerSubscriber):void");
        }

        /* access modifiers changed from: package-private */
        public void drain(List<Object> list) {
            if (list != null) {
                for (Object next : list) {
                    if (this.nl.isCompleted(next)) {
                        this.serializedChild.onCompleted();
                        return;
                    } else if (this.nl.isError(next)) {
                        this.serializedChild.onError(this.nl.getError(next));
                        return;
                    } else {
                        this.serializedChild.onNext(next);
                        this.arbiter.produced(1);
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void error(Throwable th, int i) {
            synchronized (this.guard) {
                if (i == this.index) {
                    if (this.emitting) {
                        if (this.queue == null) {
                            this.queue = new ArrayList();
                        }
                        this.queue.add(this.nl.error(th));
                        return;
                    }
                    List<Object> list = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    drain(list);
                    this.serializedChild.onError(th);
                    unsubscribe();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void complete(int i) {
            synchronized (this.guard) {
                if (i == this.index) {
                    this.active = false;
                    if (this.mainDone) {
                        if (this.emitting) {
                            if (this.queue == null) {
                                this.queue = new ArrayList();
                            }
                            this.queue.add(this.nl.completed());
                            return;
                        }
                        List<Object> list = this.queue;
                        this.queue = null;
                        this.emitting = true;
                        drain(list);
                        this.serializedChild.onCompleted();
                        unsubscribe();
                    }
                }
            }
        }
    }

    private static final class InnerSubscriber<T> extends Subscriber<T> {
        private final ProducerArbiter arbiter;
        private final int id;
        private final SwitchSubscriber<T> parent;

        InnerSubscriber(int i, ProducerArbiter producerArbiter, SwitchSubscriber<T> switchSubscriber) {
            this.id = i;
            this.arbiter = producerArbiter;
            this.parent = switchSubscriber;
        }

        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }

        public void onNext(T t) {
            this.parent.emit(t, this.id, this);
        }

        public void onError(Throwable th) {
            this.parent.error(th, this.id);
        }

        public void onCompleted() {
            this.parent.complete(this.id);
        }
    }
}
