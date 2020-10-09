package rx.internal.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.functions.Action0;
import rx.internal.util.unsafe.MpmcArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.schedulers.Schedulers;

public abstract class ObjectPool<T> {
    private final int maxSize;
    /* access modifiers changed from: private */
    public Queue<T> pool;
    private Scheduler.Worker schedulerWorker;

    /* access modifiers changed from: protected */
    public abstract T createObject();

    public ObjectPool() {
        this(0, 0, 67);
    }

    private ObjectPool(final int i, final int i2, long j) {
        this.maxSize = i2;
        initialize(i);
        this.schedulerWorker = Schedulers.computation().createWorker();
        this.schedulerWorker.schedulePeriodically(new Action0() {
            public void call() {
                int size = ObjectPool.this.pool.size();
                int i = 0;
                if (size < i) {
                    int i2 = i2 - size;
                    while (i < i2) {
                        ObjectPool.this.pool.add(ObjectPool.this.createObject());
                        i++;
                    }
                } else if (size > i2) {
                    int i3 = size - i2;
                    while (i < i3) {
                        ObjectPool.this.pool.poll();
                        i++;
                    }
                }
            }
        }, j, j, TimeUnit.SECONDS);
    }

    public T borrowObject() {
        T poll = this.pool.poll();
        return poll == null ? createObject() : poll;
    }

    public void returnObject(T t) {
        if (t != null) {
            this.pool.offer(t);
        }
    }

    public void shutdown() {
        this.schedulerWorker.unsubscribe();
    }

    private void initialize(int i) {
        if (UnsafeAccess.isUnsafeAvailable()) {
            this.pool = new MpmcArrayQueue(Math.max(this.maxSize, 1024));
        } else {
            this.pool = new ConcurrentLinkedQueue();
        }
        for (int i2 = 0; i2 < i; i2++) {
            this.pool.add(createObject());
        }
    }
}
