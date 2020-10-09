package rx.internal.producers;

import java.util.List;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;

public final class ProducerObserverArbiter<T> implements Producer, Observer<T> {
    static final Producer NULL_PRODUCER = new Producer() {
        public void request(long j) {
        }
    };
    final Subscriber<? super T> child;
    Producer currentProducer;
    boolean emitting;
    volatile boolean hasError;
    Producer missedProducer;
    long missedRequested;
    Object missedTerminal;
    List<T> queue;
    long requested;

    public ProducerObserverArbiter(Subscriber<? super T> subscriber) {
        this.child = subscriber;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r4.child.onNext(r5);
        r0 = r4.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
        if (r0 == Long.MAX_VALUE) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0027, code lost:
        r4.requested = r0 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002c, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0030, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0031, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r4.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0036, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNext(T r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.emitting     // Catch:{ all -> 0x003a }
            if (r0 == 0) goto L_0x0016
            java.util.List<T> r0 = r4.queue     // Catch:{ all -> 0x003a }
            if (r0 != 0) goto L_0x0011
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x003a }
            r1 = 4
            r0.<init>(r1)     // Catch:{ all -> 0x003a }
            r4.queue = r0     // Catch:{ all -> 0x003a }
        L_0x0011:
            r0.add(r5)     // Catch:{ all -> 0x003a }
            monitor-exit(r4)     // Catch:{ all -> 0x003a }
            return
        L_0x0016:
            monitor-exit(r4)     // Catch:{ all -> 0x003a }
            rx.Subscriber<? super T> r0 = r4.child     // Catch:{ all -> 0x0030 }
            r0.onNext(r5)     // Catch:{ all -> 0x0030 }
            long r0 = r4.requested     // Catch:{ all -> 0x0030 }
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r5 == 0) goto L_0x002c
            r2 = 1
            long r0 = r0 - r2
            r4.requested = r0     // Catch:{ all -> 0x0030 }
        L_0x002c:
            r4.emitLoop()     // Catch:{ all -> 0x0030 }
            return
        L_0x0030:
            r5 = move-exception
            monitor-enter(r4)
            r0 = 0
            r4.emitting = r0     // Catch:{ all -> 0x0037 }
            monitor-exit(r4)     // Catch:{ all -> 0x0037 }
            throw r5
        L_0x0037:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0037 }
            throw r5
        L_0x003a:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003a }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.onNext(java.lang.Object):void");
    }

    public void onError(Throwable th) {
        boolean z;
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = th;
                z = false;
            } else {
                this.emitting = true;
                z = true;
            }
        }
        if (z) {
            this.child.onError(th);
        } else {
            this.hasError = true;
        }
    }

    public void onCompleted() {
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = true;
                return;
            }
            this.emitting = true;
            this.child.onCompleted();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r2 = r5.requested + r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0022, code lost:
        if (r2 >= 0) goto L_0x0029;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0024, code lost:
        r2 = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0029, code lost:
        r5.requested = r2;
        r0 = r5.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002d, code lost:
        if (r0 == null) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002f, code lost:
        r0.request(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0032, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0035, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0036, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0037, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r5.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x003c, code lost:
        throw r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void request(long r6) {
        /*
            r5 = this;
            r0 = 0
            int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r2 < 0) goto L_0x0043
            int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r2 != 0) goto L_0x000b
            return
        L_0x000b:
            monitor-enter(r5)
            boolean r2 = r5.emitting     // Catch:{ all -> 0x0040 }
            if (r2 == 0) goto L_0x0018
            long r0 = r5.missedRequested     // Catch:{ all -> 0x0040 }
            r2 = 0
            long r0 = r0 + r6
            r5.missedRequested = r0     // Catch:{ all -> 0x0040 }
            monitor-exit(r5)     // Catch:{ all -> 0x0040 }
            return
        L_0x0018:
            r2 = 1
            r5.emitting = r2     // Catch:{ all -> 0x0040 }
            monitor-exit(r5)     // Catch:{ all -> 0x0040 }
            long r2 = r5.requested     // Catch:{ all -> 0x0036 }
            r4 = 0
            long r2 = r2 + r6
            int r4 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r4 >= 0) goto L_0x0029
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
        L_0x0029:
            r5.requested = r2     // Catch:{ all -> 0x0036 }
            rx.Producer r0 = r5.currentProducer     // Catch:{ all -> 0x0036 }
            if (r0 == 0) goto L_0x0032
            r0.request(r6)     // Catch:{ all -> 0x0036 }
        L_0x0032:
            r5.emitLoop()     // Catch:{ all -> 0x0036 }
            return
        L_0x0036:
            r6 = move-exception
            monitor-enter(r5)
            r7 = 0
            r5.emitting = r7     // Catch:{ all -> 0x003d }
            monitor-exit(r5)     // Catch:{ all -> 0x003d }
            throw r6
        L_0x003d:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003d }
            throw r6
        L_0x0040:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0040 }
            throw r6
        L_0x0043:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "n >= 0 required"
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.request(long):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r5.currentProducer = r6;
        r0 = r5.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0016, code lost:
        if (r6 == null) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001c, code lost:
        if (r0 == 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001e, code lost:
        r6.request(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0021, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0024, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0025, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0026, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r5.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x002b, code lost:
        throw r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProducer(rx.Producer r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.emitting     // Catch:{ all -> 0x002f }
            if (r0 == 0) goto L_0x000e
            if (r6 == 0) goto L_0x0008
            goto L_0x000a
        L_0x0008:
            rx.Producer r6 = NULL_PRODUCER     // Catch:{ all -> 0x002f }
        L_0x000a:
            r5.missedProducer = r6     // Catch:{ all -> 0x002f }
            monitor-exit(r5)     // Catch:{ all -> 0x002f }
            return
        L_0x000e:
            r0 = 1
            r5.emitting = r0     // Catch:{ all -> 0x002f }
            monitor-exit(r5)     // Catch:{ all -> 0x002f }
            r5.currentProducer = r6     // Catch:{ all -> 0x0025 }
            long r0 = r5.requested     // Catch:{ all -> 0x0025 }
            if (r6 == 0) goto L_0x0021
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 == 0) goto L_0x0021
            r6.request(r0)     // Catch:{ all -> 0x0025 }
        L_0x0021:
            r5.emitLoop()     // Catch:{ all -> 0x0025 }
            return
        L_0x0025:
            r6 = move-exception
            monitor-enter(r5)
            r0 = 0
            r5.emitting = r0     // Catch:{ all -> 0x002c }
            monitor-exit(r5)     // Catch:{ all -> 0x002c }
            throw r6
        L_0x002c:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x002c }
            throw r6
        L_0x002f:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x002f }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.setProducer(rx.Producer):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0026, code lost:
        if (r5 == null) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002c, code lost:
        if (r5.isEmpty() == false) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002e, code lost:
        r6 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        if (r4 == null) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0033, code lost:
        if (r4 == java.lang.Boolean.TRUE) goto L_0x003b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0035, code lost:
        r0.onError((java.lang.Throwable) r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003b, code lost:
        if (r6 == false) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
        r0.onCompleted();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0040, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0041, code lost:
        if (r5 == null) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0043, code lost:
        r4 = r5.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004b, code lost:
        if (r4.hasNext() == false) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004d, code lost:
        r6 = r4.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0055, code lost:
        if (r0.isUnsubscribed() == false) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0057, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005a, code lost:
        if (r14.hasError == false) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r0.onNext(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0061, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0062, code lost:
        rx.exceptions.Exceptions.throwIfFatal(r1);
        r0.onError(rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r6));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x006c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x006d, code lost:
        r4 = ((long) r5.size()) + 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0074, code lost:
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0075, code lost:
        r10 = r14.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007e, code lost:
        if (r10 == Long.MAX_VALUE) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0082, code lost:
        if (r1 == 0) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0084, code lost:
        r10 = r10 + r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0087, code lost:
        if (r10 >= 0) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0089, code lost:
        r10 = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x008c, code lost:
        if (r4 == 0) goto L_0x00a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0090, code lost:
        if (r10 == Long.MAX_VALUE) goto L_0x00a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0092, code lost:
        r10 = r10 - r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0095, code lost:
        if (r10 < 0) goto L_0x0098;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x009f, code lost:
        throw new java.lang.IllegalStateException("More produced than requested");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a0, code lost:
        r14.requested = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00a2, code lost:
        if (r3 == null) goto L_0x00b7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00a6, code lost:
        if (r3 != NULL_PRODUCER) goto L_0x00ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00a8, code lost:
        r14.currentProducer = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00ac, code lost:
        r14.currentProducer = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00b0, code lost:
        if (r10 == 0) goto L_0x0002;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00b2, code lost:
        r3.request(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00b7, code lost:
        r3 = r14.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00b9, code lost:
        if (r3 == null) goto L_0x0002;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00bd, code lost:
        if (r1 == 0) goto L_0x0002;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00bf, code lost:
        r3.request(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void emitLoop() {
        /*
            r14 = this;
            rx.Subscriber<? super T> r0 = r14.child
        L_0x0002:
            monitor-enter(r14)
            long r1 = r14.missedRequested     // Catch:{ all -> 0x00c4 }
            rx.Producer r3 = r14.missedProducer     // Catch:{ all -> 0x00c4 }
            java.lang.Object r4 = r14.missedTerminal     // Catch:{ all -> 0x00c4 }
            java.util.List<T> r5 = r14.queue     // Catch:{ all -> 0x00c4 }
            r6 = 0
            r7 = 0
            int r9 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r9 != 0) goto L_0x001c
            if (r3 != 0) goto L_0x001c
            if (r5 != 0) goto L_0x001c
            if (r4 != 0) goto L_0x001c
            r14.emitting = r6     // Catch:{ all -> 0x00c4 }
            monitor-exit(r14)     // Catch:{ all -> 0x00c4 }
            return
        L_0x001c:
            r14.missedRequested = r7     // Catch:{ all -> 0x00c4 }
            r9 = 0
            r14.missedProducer = r9     // Catch:{ all -> 0x00c4 }
            r14.queue = r9     // Catch:{ all -> 0x00c4 }
            r14.missedTerminal = r9     // Catch:{ all -> 0x00c4 }
            monitor-exit(r14)     // Catch:{ all -> 0x00c4 }
            if (r5 == 0) goto L_0x002e
            boolean r10 = r5.isEmpty()
            if (r10 == 0) goto L_0x002f
        L_0x002e:
            r6 = 1
        L_0x002f:
            if (r4 == 0) goto L_0x0041
            java.lang.Boolean r10 = java.lang.Boolean.TRUE
            if (r4 == r10) goto L_0x003b
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            r0.onError(r4)
            return
        L_0x003b:
            if (r6 == 0) goto L_0x0041
            r0.onCompleted()
            return
        L_0x0041:
            if (r5 == 0) goto L_0x0074
            java.util.Iterator r4 = r5.iterator()
        L_0x0047:
            boolean r6 = r4.hasNext()
            if (r6 == 0) goto L_0x006d
            java.lang.Object r6 = r4.next()
            boolean r10 = r0.isUnsubscribed()
            if (r10 == 0) goto L_0x0058
            return
        L_0x0058:
            boolean r10 = r14.hasError
            if (r10 == 0) goto L_0x005d
            goto L_0x0002
        L_0x005d:
            r0.onNext(r6)     // Catch:{ Throwable -> 0x0061 }
            goto L_0x0047
        L_0x0061:
            r1 = move-exception
            rx.exceptions.Exceptions.throwIfFatal(r1)
            java.lang.Throwable r1 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r6)
            r0.onError(r1)
            return
        L_0x006d:
            int r4 = r5.size()
            long r4 = (long) r4
            long r4 = r4 + r7
            goto L_0x0075
        L_0x0074:
            r4 = r7
        L_0x0075:
            long r10 = r14.requested
            r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r6 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r6 == 0) goto L_0x00a2
            int r6 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r6 == 0) goto L_0x008a
            long r10 = r10 + r1
            int r6 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
            if (r6 >= 0) goto L_0x008a
            r10 = r12
        L_0x008a:
            int r6 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r6 == 0) goto L_0x00a0
            int r6 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r6 == 0) goto L_0x00a0
            long r10 = r10 - r4
            int r4 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
            if (r4 < 0) goto L_0x0098
            goto L_0x00a0
        L_0x0098:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "More produced than requested"
            r0.<init>(r1)
            throw r0
        L_0x00a0:
            r14.requested = r10
        L_0x00a2:
            if (r3 == 0) goto L_0x00b7
            rx.Producer r1 = NULL_PRODUCER
            if (r3 != r1) goto L_0x00ac
            r14.currentProducer = r9
            goto L_0x0002
        L_0x00ac:
            r14.currentProducer = r3
            int r1 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
            if (r1 == 0) goto L_0x0002
            r3.request(r10)
            goto L_0x0002
        L_0x00b7:
            rx.Producer r3 = r14.currentProducer
            if (r3 == 0) goto L_0x0002
            int r4 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r4 == 0) goto L_0x0002
            r3.request(r1)
            goto L_0x0002
        L_0x00c4:
            r0 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x00c4 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.emitLoop():void");
    }
}
