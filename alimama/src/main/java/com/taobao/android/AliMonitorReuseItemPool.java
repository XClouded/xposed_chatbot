package com.taobao.android;

import com.taobao.android.AliMonitorReusable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class AliMonitorReuseItemPool<T extends AliMonitorReusable> {
    private static long poolSize;
    private static AtomicLong totalInvokeTimes = new AtomicLong(0);
    private static AtomicLong totalPollSuccessTimes = new AtomicLong(0);
    private final int MAX_ITEM_COUNT = 20;
    private Set<Integer> hashCodes = new HashSet();
    private AtomicLong invokeTimes = new AtomicLong(0);
    private ConcurrentLinkedQueue<T> items = new ConcurrentLinkedQueue<>();
    private Integer objectSize = null;
    private AtomicLong pollSuccessTimes = new AtomicLong(0);

    public static void reset(long j) {
        poolSize = j;
        totalInvokeTimes = new AtomicLong(0);
    }

    public T poll() {
        totalInvokeTimes.getAndIncrement();
        this.invokeTimes.getAndIncrement();
        T t = (AliMonitorReusable) this.items.poll();
        if (t != null) {
            this.hashCodes.remove(Integer.valueOf(System.identityHashCode(t)));
            this.pollSuccessTimes.getAndIncrement();
            totalPollSuccessTimes.getAndIncrement();
        }
        return t;
    }

    public void offer(T t) {
        t.clean();
        if (this.items.size() < 20) {
            synchronized (this.hashCodes) {
                int identityHashCode = System.identityHashCode(t);
                if (!this.hashCodes.contains(Integer.valueOf(identityHashCode))) {
                    this.hashCodes.add(Integer.valueOf(identityHashCode));
                    this.items.offer(t);
                }
            }
        }
    }
}
