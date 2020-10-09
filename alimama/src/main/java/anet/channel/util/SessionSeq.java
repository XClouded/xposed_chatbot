package anet.channel.util;

import android.text.TextUtils;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionSeq {
    private static AtomicInteger mIndex = new AtomicInteger();

    public static String createSequenceNo(String str) {
        if (mIndex.get() == Integer.MAX_VALUE) {
            mIndex.set(0);
        }
        if (!TextUtils.isEmpty(str)) {
            return StringUtils.concatString(str, ".AWCN", String.valueOf(mIndex.incrementAndGet()));
        }
        return StringUtils.concatString("AWCN", String.valueOf(mIndex.incrementAndGet()));
    }
}
