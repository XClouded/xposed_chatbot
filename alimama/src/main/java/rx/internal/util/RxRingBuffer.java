package rx.internal.util;

import java.io.PrintStream;
import java.util.Queue;
import rx.Observer;
import rx.Subscription;
import rx.exceptions.MissingBackpressureException;
import rx.internal.operators.NotificationLite;
import rx.internal.util.unsafe.SpmcArrayQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;

public class RxRingBuffer implements Subscription {
    public static final int SIZE = _size;
    private static ObjectPool<Queue<Object>> SPMC_POOL = new ObjectPool<Queue<Object>>() {
        /* access modifiers changed from: protected */
        public SpmcArrayQueue<Object> createObject() {
            return new SpmcArrayQueue<>(RxRingBuffer.SIZE);
        }
    };
    private static ObjectPool<Queue<Object>> SPSC_POOL = new ObjectPool<Queue<Object>>() {
        /* access modifiers changed from: protected */
        public SpscArrayQueue<Object> createObject() {
            return new SpscArrayQueue<>(RxRingBuffer.SIZE);
        }
    };
    static int _size;
    private static final NotificationLite<Object> on = NotificationLite.instance();
    private final ObjectPool<Queue<Object>> pool;
    private Queue<Object> queue;
    private final int size;
    public volatile Object terminalState;

    public static RxRingBuffer getSpscInstance() {
        if (UnsafeAccess.isUnsafeAvailable()) {
            return new RxRingBuffer(SPSC_POOL, SIZE);
        }
        return new RxRingBuffer();
    }

    public static RxRingBuffer getSpmcInstance() {
        if (UnsafeAccess.isUnsafeAvailable()) {
            return new RxRingBuffer(SPMC_POOL, SIZE);
        }
        return new RxRingBuffer();
    }

    static {
        _size = 128;
        if (PlatformDependent.isAndroid()) {
            _size = 16;
        }
        String property = System.getProperty("rx.ring-buffer.size");
        if (property != null) {
            try {
                _size = Integer.parseInt(property);
            } catch (Exception e) {
                PrintStream printStream = System.err;
                printStream.println("Failed to set 'rx.buffer.size' with value " + property + " => " + e.getMessage());
            }
        }
    }

    private RxRingBuffer(Queue<Object> queue2, int i) {
        this.queue = queue2;
        this.pool = null;
        this.size = i;
    }

    private RxRingBuffer(ObjectPool<Queue<Object>> objectPool, int i) {
        this.pool = objectPool;
        this.queue = objectPool.borrowObject();
        this.size = i;
    }

    public synchronized void release() {
        Queue<Object> queue2 = this.queue;
        ObjectPool<Queue<Object>> objectPool = this.pool;
        if (!(objectPool == null || queue2 == null)) {
            queue2.clear();
            this.queue = null;
            objectPool.returnObject(queue2);
        }
    }

    public void unsubscribe() {
        release();
    }

    RxRingBuffer() {
        this((Queue<Object>) new SynchronizedQueue(SIZE), SIZE);
    }

    public void onNext(Object obj) throws MissingBackpressureException {
        boolean z;
        boolean z2;
        synchronized (this) {
            Queue<Object> queue2 = this.queue;
            z = true;
            if (queue2 != null) {
                z2 = !queue2.offer(on.next(obj));
                z = false;
            } else {
                z2 = false;
            }
        }
        if (z) {
            throw new IllegalStateException("This instance has been unsubscribed and the queue is no longer usable.");
        } else if (z2) {
            throw new MissingBackpressureException();
        }
    }

    public void onCompleted() {
        if (this.terminalState == null) {
            this.terminalState = on.completed();
        }
    }

    public void onError(Throwable th) {
        if (this.terminalState == null) {
            this.terminalState = on.error(th);
        }
    }

    public int available() {
        return this.size - count();
    }

    public int capacity() {
        return this.size;
    }

    public int count() {
        Queue<Object> queue2 = this.queue;
        if (queue2 == null) {
            return 0;
        }
        return queue2.size();
    }

    public boolean isEmpty() {
        Queue<Object> queue2 = this.queue;
        if (queue2 == null) {
            return true;
        }
        return queue2.isEmpty();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object poll() {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.Queue<java.lang.Object> r0 = r4.queue     // Catch:{ all -> 0x001d }
            r1 = 0
            if (r0 != 0) goto L_0x0008
            monitor-exit(r4)     // Catch:{ all -> 0x001d }
            return r1
        L_0x0008:
            java.lang.Object r2 = r0.poll()     // Catch:{ all -> 0x001d }
            java.lang.Object r3 = r4.terminalState     // Catch:{ all -> 0x001d }
            if (r2 != 0) goto L_0x001b
            if (r3 == 0) goto L_0x001b
            java.lang.Object r0 = r0.peek()     // Catch:{ all -> 0x001d }
            if (r0 != 0) goto L_0x001b
            r4.terminalState = r1     // Catch:{ all -> 0x001d }
            r2 = r3
        L_0x001b:
            monitor-exit(r4)     // Catch:{ all -> 0x001d }
            return r2
        L_0x001d:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x001d }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.RxRingBuffer.poll():java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001a, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object peek() {
        /*
            r3 = this;
            monitor-enter(r3)
            java.util.Queue<java.lang.Object> r0 = r3.queue     // Catch:{ all -> 0x001b }
            if (r0 != 0) goto L_0x0008
            r0 = 0
            monitor-exit(r3)     // Catch:{ all -> 0x001b }
            return r0
        L_0x0008:
            java.lang.Object r1 = r0.peek()     // Catch:{ all -> 0x001b }
            java.lang.Object r2 = r3.terminalState     // Catch:{ all -> 0x001b }
            if (r1 != 0) goto L_0x0019
            if (r2 == 0) goto L_0x0019
            java.lang.Object r0 = r0.peek()     // Catch:{ all -> 0x001b }
            if (r0 != 0) goto L_0x0019
            r1 = r2
        L_0x0019:
            monitor-exit(r3)     // Catch:{ all -> 0x001b }
            return r1
        L_0x001b:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x001b }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.RxRingBuffer.peek():java.lang.Object");
    }

    public boolean isCompleted(Object obj) {
        return on.isCompleted(obj);
    }

    public boolean isError(Object obj) {
        return on.isError(obj);
    }

    public Object getValue(Object obj) {
        return on.getValue(obj);
    }

    public boolean accept(Object obj, Observer observer) {
        return on.accept(observer, obj);
    }

    public Throwable asError(Object obj) {
        return on.getError(obj);
    }

    public boolean isUnsubscribed() {
        return this.queue == null;
    }
}
