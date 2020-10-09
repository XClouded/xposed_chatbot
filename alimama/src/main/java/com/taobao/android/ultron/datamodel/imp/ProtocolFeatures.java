package com.taobao.android.ultron.datamodel.imp;

import java.math.BigInteger;

public class ProtocolFeatures {
    public static final BigInteger FEATURE_CONTAINER_CACHE = new BigInteger("2");
    public static final BigInteger FEATURE_SIMPLE_POPUP = new BigInteger("4");
    public static final BigInteger FEATURE_TAG_ID = new BigInteger("1");

    public static BigInteger getMixFeature(BigInteger... bigIntegerArr) {
        BigInteger bigInteger = null;
        if (bigIntegerArr != null && bigIntegerArr.length > 0) {
            for (int i = 0; i < bigIntegerArr.length; i++) {
                if (i == 0) {
                    bigInteger = bigIntegerArr[i];
                } else {
                    bigInteger = bigInteger.or(bigIntegerArr[i]);
                }
            }
        }
        return bigInteger;
    }

    public static boolean hasFeature(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger == null || bigInteger2 == null) {
            return false;
        }
        return bigInteger2.equals(bigInteger.and(bigInteger2));
    }

    public static BigInteger removeFeature(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger == null || bigInteger2 == null || !hasFeature(bigInteger, bigInteger2)) {
            return bigInteger;
        }
        return bigInteger.xor(bigInteger2);
    }
}
