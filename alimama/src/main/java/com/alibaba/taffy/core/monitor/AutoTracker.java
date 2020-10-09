package com.alibaba.taffy.core.monitor;

import android.os.SystemClock;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class AutoTracker {
    private final AtomicInteger count;
    private final HashMap<String, TrackItem> data = new HashMap<>();
    private final TrackerListener listener;
    private final ReentrantLock lock = new ReentrantLock();
    private final int maxPointCount;
    private final String name;

    public interface TrackerListener {
        void onTrackEnd(String str, Map<String, TrackItem> map);
    }

    public static class TrackItem {
        public long end = -1;
        public long start = -1;

        public long getCost() {
            return this.end - this.start;
        }
    }

    public AutoTracker(String str, int i, TrackerListener trackerListener) {
        this.name = str;
        if (i > 0) {
            this.maxPointCount = i;
            this.count = new AtomicInteger(i);
            this.listener = trackerListener;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void reset() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            this.count.set(this.maxPointCount);
            this.data.clear();
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean isDone() {
        return this.count.get() <= 0;
    }

    public void begin(String str) {
        if (this.count.get() > 0) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                TrackItem trackItem = new TrackItem();
                trackItem.start = SystemClock.elapsedRealtime();
                TrackItem put = this.data.put(str, trackItem);
                if (!(put == null || put.end == -1)) {
                    this.count.incrementAndGet();
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public void end(String str) {
        if (this.count.get() > 0) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                TrackItem trackItem = this.data.get(str);
                if (trackItem != null) {
                    if (trackItem.end == -1) {
                        this.count.decrementAndGet();
                    }
                    trackItem.end = SystemClock.elapsedRealtime();
                    if (((long) this.count.get()) == 0) {
                        this.listener.onTrackEnd(this.name, this.data);
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
