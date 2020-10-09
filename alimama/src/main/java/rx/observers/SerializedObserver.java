package rx.observers;

import rx.Observer;
import rx.exceptions.Exceptions;
import rx.internal.operators.NotificationLite;

public class SerializedObserver<T> implements Observer<T> {
    private static final int MAX_DRAIN_ITERATION = 1024;
    private final Observer<? super T> actual;
    private boolean emitting;
    private final NotificationLite<T> nl = NotificationLite.instance();
    private FastList queue;
    private volatile boolean terminated;

    static final class FastList {
        Object[] array;
        int size;

        FastList() {
        }

        public void add(Object obj) {
            int i = this.size;
            Object[] objArr = this.array;
            if (objArr == null) {
                objArr = new Object[16];
                this.array = objArr;
            } else if (i == objArr.length) {
                Object[] objArr2 = new Object[((i >> 2) + i)];
                System.arraycopy(objArr, 0, objArr2, 0, i);
                this.array = objArr2;
                objArr = objArr2;
            }
            objArr[i] = obj;
            this.size = i + 1;
        }
    }

    public SerializedObserver(Observer<? super T> observer) {
        this.actual = observer;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r9.actual.onNext(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002f, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0033, code lost:
        if (r2 >= 1024) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0035, code lost:
        monitor-enter(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r3 = r9.queue;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0038, code lost:
        if (r3 != null) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x003a, code lost:
        r9.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x003c, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x003d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x003e, code lost:
        r9.queue = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0041, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0042, code lost:
        r3 = r3.array;
        r4 = r3.length;
        r5 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0046, code lost:
        if (r5 >= r4) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0048, code lost:
        r6 = r3[r5];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x004a, code lost:
        if (r6 != null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0055, code lost:
        if (r9.nl.accept(r9.actual, r6) == false) goto L_0x005a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0057, code lost:
        r9.terminated = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0059, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x005a, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x005d, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x005e, code lost:
        r9.terminated = true;
        rx.exceptions.Exceptions.throwIfFatal(r1);
        r9.actual.onError(rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r10));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x006c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x006d, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0073, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0074, code lost:
        r9.terminated = true;
        rx.exceptions.Exceptions.throwIfFatal(r1);
        r9.actual.onError(rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r10));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0082, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNext(T r10) {
        /*
            r9 = this;
            boolean r0 = r9.terminated
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            monitor-enter(r9)
            boolean r0 = r9.terminated     // Catch:{ all -> 0x0083 }
            if (r0 == 0) goto L_0x000c
            monitor-exit(r9)     // Catch:{ all -> 0x0083 }
            return
        L_0x000c:
            boolean r0 = r9.emitting     // Catch:{ all -> 0x0083 }
            if (r0 == 0) goto L_0x0026
            rx.observers.SerializedObserver$FastList r0 = r9.queue     // Catch:{ all -> 0x0083 }
            if (r0 != 0) goto L_0x001b
            rx.observers.SerializedObserver$FastList r0 = new rx.observers.SerializedObserver$FastList     // Catch:{ all -> 0x0083 }
            r0.<init>()     // Catch:{ all -> 0x0083 }
            r9.queue = r0     // Catch:{ all -> 0x0083 }
        L_0x001b:
            rx.internal.operators.NotificationLite<T> r1 = r9.nl     // Catch:{ all -> 0x0083 }
            java.lang.Object r10 = r1.next(r10)     // Catch:{ all -> 0x0083 }
            r0.add(r10)     // Catch:{ all -> 0x0083 }
            monitor-exit(r9)     // Catch:{ all -> 0x0083 }
            return
        L_0x0026:
            r0 = 1
            r9.emitting = r0     // Catch:{ all -> 0x0083 }
            monitor-exit(r9)     // Catch:{ all -> 0x0083 }
            rx.Observer<? super T> r1 = r9.actual     // Catch:{ Throwable -> 0x0073 }
            r1.onNext(r10)     // Catch:{ Throwable -> 0x0073 }
        L_0x002f:
            r1 = 0
            r2 = 0
        L_0x0031:
            r3 = 1024(0x400, float:1.435E-42)
            if (r2 >= r3) goto L_0x002f
            monitor-enter(r9)
            rx.observers.SerializedObserver$FastList r3 = r9.queue     // Catch:{ all -> 0x0070 }
            if (r3 != 0) goto L_0x003e
            r9.emitting = r1     // Catch:{ all -> 0x0070 }
            monitor-exit(r9)     // Catch:{ all -> 0x0070 }
            return
        L_0x003e:
            r4 = 0
            r9.queue = r4     // Catch:{ all -> 0x0070 }
            monitor-exit(r9)     // Catch:{ all -> 0x0070 }
            java.lang.Object[] r3 = r3.array
            int r4 = r3.length
            r5 = 0
        L_0x0046:
            if (r5 >= r4) goto L_0x006d
            r6 = r3[r5]
            if (r6 != 0) goto L_0x004d
            goto L_0x006d
        L_0x004d:
            rx.internal.operators.NotificationLite<T> r7 = r9.nl     // Catch:{ Throwable -> 0x005d }
            rx.Observer<? super T> r8 = r9.actual     // Catch:{ Throwable -> 0x005d }
            boolean r6 = r7.accept(r8, r6)     // Catch:{ Throwable -> 0x005d }
            if (r6 == 0) goto L_0x005a
            r9.terminated = r0     // Catch:{ Throwable -> 0x005d }
            return
        L_0x005a:
            int r5 = r5 + 1
            goto L_0x0046
        L_0x005d:
            r1 = move-exception
            r9.terminated = r0
            rx.exceptions.Exceptions.throwIfFatal(r1)
            rx.Observer<? super T> r0 = r9.actual
            java.lang.Throwable r10 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r10)
            r0.onError(r10)
            return
        L_0x006d:
            int r2 = r2 + 1
            goto L_0x0031
        L_0x0070:
            r10 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0070 }
            throw r10
        L_0x0073:
            r1 = move-exception
            r9.terminated = r0
            rx.exceptions.Exceptions.throwIfFatal(r1)
            rx.Observer<? super T> r0 = r9.actual
            java.lang.Throwable r10 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r10)
            r0.onError(r10)
            return
        L_0x0083:
            r10 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0083 }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.observers.SerializedObserver.onNext(java.lang.Object):void");
    }

    public void onError(Throwable th) {
        Exceptions.throwIfFatal(th);
        if (!this.terminated) {
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    if (this.emitting) {
                        FastList fastList = this.queue;
                        if (fastList == null) {
                            fastList = new FastList();
                            this.queue = fastList;
                        }
                        fastList.add(this.nl.error(th));
                        return;
                    }
                    this.emitting = true;
                    this.actual.onError(th);
                }
            }
        }
    }

    public void onCompleted() {
        if (!this.terminated) {
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    if (this.emitting) {
                        FastList fastList = this.queue;
                        if (fastList == null) {
                            fastList = new FastList();
                            this.queue = fastList;
                        }
                        fastList.add(this.nl.completed());
                        return;
                    }
                    this.emitting = true;
                    this.actual.onCompleted();
                }
            }
        }
    }
}
