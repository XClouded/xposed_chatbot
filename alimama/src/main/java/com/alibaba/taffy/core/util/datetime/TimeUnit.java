package com.alibaba.taffy.core.util.datetime;

import java.util.TimeZone;

public enum TimeUnit {
    SECOND {
        public long next(long j, int i) {
            return j + (((long) i) * TimeUnit.C0);
        }

        public long toMillis() {
            return TimeUnit.C0;
        }

        public long toMillis(int i) {
            return ((long) i) * TimeUnit.C0;
        }

        public int distance(long j, long j2) {
            return (int) ((j2 - j) / TimeUnit.C0);
        }

        public long floor(long j, long j2) {
            return (((j - j2) / TimeUnit.C0) * TimeUnit.C0) + j2;
        }
    },
    MINUTE {
        public long next(long j, int i) {
            return j + (((long) i) * 60000);
        }

        public long toMillis() {
            return 60000;
        }

        public long toMillis(int i) {
            return ((long) i) * 60000;
        }

        public int distance(long j, long j2) {
            return (int) ((j2 - j) / 60000);
        }

        public long floor(long j, long j2) {
            return (((j - j2) / 60000) * 60000) + j2;
        }
    },
    HOUR {
        public long next(long j, int i) {
            return j + (((long) i) * TimeUnit.C2);
        }

        public long toMillis() {
            return TimeUnit.C2;
        }

        public long toMillis(int i) {
            return ((long) i) * TimeUnit.C2;
        }

        public int distance(long j, long j2) {
            return (int) ((j2 - j) / TimeUnit.C2);
        }

        public long floor(long j, long j2) {
            return (((j - j2) / TimeUnit.C2) * TimeUnit.C2) + j2;
        }
    },
    DAY {
        public long next(long j, int i) {
            return j + (((long) i) * 86400000);
        }

        public long toMillis() {
            return 86400000;
        }

        public long toMillis(int i) {
            return ((long) i) * 86400000;
        }

        public int distance(long j, long j2) {
            return (int) ((j2 - j) / 86400000);
        }

        public long floor(long j, long j2) {
            long access$100 = j2 - TimeUnit.RAW_OFFSET;
            return (((j - access$100) / 86400000) * 86400000) + access$100;
        }
    },
    MONTH {
        public long next(long j, int i) {
            return j + (((long) i) * 2592000000L);
        }

        public long toMillis() {
            return 2592000000L;
        }

        public long toMillis(int i) {
            return ((long) i) * 2592000000L;
        }

        public int distance(long j, long j2) {
            return (int) ((j2 - j) / 2592000000L);
        }

        public long floor(long j, long j2) {
            return (((j - j2) / 2592000000L) * 2592000000L) + j2;
        }
    },
    YEAR {
        public long next(long j, int i) {
            return j + (((long) i) * TimeUnit.C5);
        }

        public long toMillis() {
            return TimeUnit.C5;
        }

        public long toMillis(int i) {
            return ((long) i) * TimeUnit.C5;
        }

        public int distance(long j, long j2) {
            return (int) ((j2 - j) / TimeUnit.C5);
        }

        public long floor(long j, long j2) {
            return (((j - j2) / TimeUnit.C5) * TimeUnit.C5) + j2;
        }
    };
    
    private static final long C0 = 1000;
    private static final long C1 = 60000;
    private static final long C2 = 3600000;
    private static final long C3 = 86400000;
    private static final long C4 = 2592000000L;
    private static final long C5 = 946080000000L;
    /* access modifiers changed from: private */
    public static final long RAW_OFFSET = 0;

    static {
        RAW_OFFSET = (long) TimeZone.getDefault().getRawOffset();
    }

    public long toMillis() {
        throw new AbstractMethodError();
    }

    public long toMillis(int i) {
        throw new AbstractMethodError();
    }

    public long next(long j, int i) {
        throw new AbstractMethodError();
    }

    public int distance(long j, long j2) {
        throw new AbstractMethodError();
    }

    public long floor(long j, long j2) {
        throw new AbstractMethodError();
    }
}
