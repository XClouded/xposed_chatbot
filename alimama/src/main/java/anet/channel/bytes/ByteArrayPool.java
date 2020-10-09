package anet.channel.bytes;

import java.util.Random;
import java.util.TreeSet;

public class ByteArrayPool {
    public static final int MAX_POOL_SIZE = 524288;
    public static final String TAG = "awcn.ByteArrayPool";
    private final TreeSet<ByteArray> byteArrayPool = new TreeSet<>();
    private final Random random = new Random();
    private final ByteArray std = ByteArray.create(0);
    private long total = 0;

    public static ByteArrayPool getInstance() {
        return SingleInstance.instance;
    }

    static class SingleInstance {
        static ByteArrayPool instance = new ByteArrayPool();

        SingleInstance() {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0048, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void refund(anet.channel.bytes.ByteArray r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            if (r5 == 0) goto L_0x0047
            int r0 = r5.bufferLength     // Catch:{ all -> 0x0044 }
            r1 = 524288(0x80000, float:7.34684E-40)
            if (r0 < r1) goto L_0x000a
            goto L_0x0047
        L_0x000a:
            long r0 = r4.total     // Catch:{ all -> 0x0044 }
            int r2 = r5.bufferLength     // Catch:{ all -> 0x0044 }
            long r2 = (long) r2     // Catch:{ all -> 0x0044 }
            long r0 = r0 + r2
            r4.total = r0     // Catch:{ all -> 0x0044 }
            java.util.TreeSet<anet.channel.bytes.ByteArray> r0 = r4.byteArrayPool     // Catch:{ all -> 0x0044 }
            r0.add(r5)     // Catch:{ all -> 0x0044 }
        L_0x0017:
            long r0 = r4.total     // Catch:{ all -> 0x0044 }
            r2 = 524288(0x80000, double:2.590327E-318)
            int r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r5 <= 0) goto L_0x0042
            java.util.Random r5 = r4.random     // Catch:{ all -> 0x0044 }
            boolean r5 = r5.nextBoolean()     // Catch:{ all -> 0x0044 }
            if (r5 == 0) goto L_0x0031
            java.util.TreeSet<anet.channel.bytes.ByteArray> r5 = r4.byteArrayPool     // Catch:{ all -> 0x0044 }
            java.lang.Object r5 = r5.pollFirst()     // Catch:{ all -> 0x0044 }
            anet.channel.bytes.ByteArray r5 = (anet.channel.bytes.ByteArray) r5     // Catch:{ all -> 0x0044 }
            goto L_0x0039
        L_0x0031:
            java.util.TreeSet<anet.channel.bytes.ByteArray> r5 = r4.byteArrayPool     // Catch:{ all -> 0x0044 }
            java.lang.Object r5 = r5.pollLast()     // Catch:{ all -> 0x0044 }
            anet.channel.bytes.ByteArray r5 = (anet.channel.bytes.ByteArray) r5     // Catch:{ all -> 0x0044 }
        L_0x0039:
            long r0 = r4.total     // Catch:{ all -> 0x0044 }
            int r5 = r5.bufferLength     // Catch:{ all -> 0x0044 }
            long r2 = (long) r5     // Catch:{ all -> 0x0044 }
            long r0 = r0 - r2
            r4.total = r0     // Catch:{ all -> 0x0044 }
            goto L_0x0017
        L_0x0042:
            monitor-exit(r4)
            return
        L_0x0044:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        L_0x0047:
            monitor-exit(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.bytes.ByteArrayPool.refund(anet.channel.bytes.ByteArray):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0038, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized anet.channel.bytes.ByteArray retrieve(int r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            r0 = 524288(0x80000, float:7.34684E-40)
            if (r6 < r0) goto L_0x000d
            anet.channel.bytes.ByteArray r6 = anet.channel.bytes.ByteArray.create(r6)     // Catch:{ all -> 0x000b }
            monitor-exit(r5)
            return r6
        L_0x000b:
            r6 = move-exception
            goto L_0x0039
        L_0x000d:
            anet.channel.bytes.ByteArray r0 = r5.std     // Catch:{ all -> 0x000b }
            r0.bufferLength = r6     // Catch:{ all -> 0x000b }
            java.util.TreeSet<anet.channel.bytes.ByteArray> r0 = r5.byteArrayPool     // Catch:{ all -> 0x000b }
            anet.channel.bytes.ByteArray r1 = r5.std     // Catch:{ all -> 0x000b }
            java.lang.Object r0 = r0.ceiling(r1)     // Catch:{ all -> 0x000b }
            anet.channel.bytes.ByteArray r0 = (anet.channel.bytes.ByteArray) r0     // Catch:{ all -> 0x000b }
            if (r0 != 0) goto L_0x0022
            anet.channel.bytes.ByteArray r0 = anet.channel.bytes.ByteArray.create(r6)     // Catch:{ all -> 0x000b }
            goto L_0x0037
        L_0x0022:
            byte[] r6 = r0.buffer     // Catch:{ all -> 0x000b }
            r1 = 0
            java.util.Arrays.fill(r6, r1)     // Catch:{ all -> 0x000b }
            r0.dataLength = r1     // Catch:{ all -> 0x000b }
            java.util.TreeSet<anet.channel.bytes.ByteArray> r6 = r5.byteArrayPool     // Catch:{ all -> 0x000b }
            r6.remove(r0)     // Catch:{ all -> 0x000b }
            long r1 = r5.total     // Catch:{ all -> 0x000b }
            int r6 = r0.bufferLength     // Catch:{ all -> 0x000b }
            long r3 = (long) r6     // Catch:{ all -> 0x000b }
            long r1 = r1 - r3
            r5.total = r1     // Catch:{ all -> 0x000b }
        L_0x0037:
            monitor-exit(r5)
            return r0
        L_0x0039:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.bytes.ByteArrayPool.retrieve(int):anet.channel.bytes.ByteArray");
    }

    public ByteArray retrieveAndCopy(byte[] bArr, int i) {
        ByteArray retrieve = retrieve(i);
        System.arraycopy(bArr, 0, retrieve.buffer, 0, i);
        retrieve.dataLength = i;
        return retrieve;
    }
}
