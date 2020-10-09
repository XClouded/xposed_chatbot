package anetwork.channel.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SeqGen {
    private static final int mask = Integer.MAX_VALUE;
    private static AtomicInteger seq = new AtomicInteger(0);

    public static String createSeqNo(String str, String str2) {
        StringBuilder sb = new StringBuilder(16);
        if (str != null) {
            sb.append(str);
            sb.append('.');
        }
        if (str2 != null) {
            sb.append(str2);
            sb.append(seq.incrementAndGet() & Integer.MAX_VALUE);
        }
        return sb.toString();
    }
}
