package com.huawei.updatesdk.sdk.a.a.a;

import java.util.ArrayDeque;
import java.util.Queue;

public final class a {
    private static final a b = new a();
    private final Queue<byte[]> a = new ArrayDeque(0);

    private a() {
    }

    public static a a() {
        return b;
    }

    public void a(byte[] bArr) {
        if (bArr.length == 65536) {
            synchronized (this.a) {
                if (this.a.size() < 32 && !this.a.offer(bArr)) {
                    com.huawei.updatesdk.sdk.a.c.a.a.a.a("ByteArrayPool", "releaseBytes false");
                }
            }
        }
    }

    public byte[] b() {
        byte[] poll;
        synchronized (this.a) {
            poll = this.a.poll();
        }
        return poll == null ? new byte[65536] : poll;
    }
}
