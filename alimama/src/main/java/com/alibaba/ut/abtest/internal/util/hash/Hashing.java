package com.alibaba.ut.abtest.internal.util.hash;

public final class Hashing {
    static final int GOOD_FAST_HASH_SEED = ((int) System.currentTimeMillis());
    static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(1526958062);

    private Hashing() {
    }

    public static HashFunction getMurmur3_32() {
        return MURMUR3_32;
    }

    public static HashFunction getGoodMurmur3_32() {
        return Murmur3_32HashFunction.GOOD_FAST_HASH_32;
    }
}
