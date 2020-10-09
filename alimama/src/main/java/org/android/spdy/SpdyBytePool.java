package org.android.spdy;

import anet.channel.entity.EventType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class SpdyBytePool {
    private static final int POOL_SIZE = 16;
    private static volatile SpdyBytePool gInstance = null;
    private static Object lock = new Object();
    private ArrayList<Deque<SpdyByteArray>> bucks;

    private SpdyBytePool() {
        this.bucks = null;
        this.bucks = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            this.bucks.add((Object) null);
        }
    }

    public static SpdyBytePool getInstance() {
        if (gInstance == null) {
            synchronized (lock) {
                if (gInstance == null) {
                    gInstance = new SpdyBytePool();
                }
            }
        }
        return gInstance;
    }

    /* access modifiers changed from: package-private */
    public SpdyByteArray getSpdyByteArray(int i) {
        int i2 = (i + EventType.ALL) & -4096;
        int i3 = i2 > 0 ? i2 >> 12 : 0;
        SpdyByteArray spdyByteArray = null;
        if (i3 < 16) {
            synchronized (lock) {
                Deque deque = this.bucks.get(i3);
                if (deque != null && deque.size() > 0) {
                    spdyByteArray = (SpdyByteArray) deque.pop();
                }
            }
        }
        return spdyByteArray == null ? new SpdyByteArray(i2) : spdyByteArray;
    }

    /* access modifiers changed from: package-private */
    public void recycle(SpdyByteArray spdyByteArray) {
        int i = spdyByteArray.length > 0 ? spdyByteArray.length >> 12 : 0;
        if (i < 16) {
            synchronized (lock) {
                Deque deque = this.bucks.get(i);
                if (deque == null) {
                    deque = new ArrayDeque(16);
                    this.bucks.set(i, deque);
                }
                deque.push(spdyByteArray);
            }
        }
    }
}
