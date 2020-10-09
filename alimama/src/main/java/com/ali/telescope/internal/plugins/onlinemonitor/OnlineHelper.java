package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.util.ByteUtils;

public class OnlineHelper {
    public static byte[] getDimension(String str) {
        if (str == null) {
            str = "";
        }
        return ByteUtils.merge(ByteUtils.int2Bytes(str.getBytes().length), str.getBytes());
    }

    public static byte[] getMeasure(Double d) {
        if (d == null) {
            d = Double.valueOf(0.0d);
        }
        return ByteUtils.doubleToBytes(d.doubleValue());
    }
}
