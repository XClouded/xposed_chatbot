package com.alibaba.analytics.utils;

import android.content.Context;
import java.util.Random;

public class PhoneInfoUtils2 {
    public static final String getUniqueID() {
        int nextInt = new Random().nextInt();
        int nextInt2 = new Random().nextInt();
        byte[] bytes = IntUtils.getBytes((int) (System.currentTimeMillis() / 1000));
        byte[] bytes2 = IntUtils.getBytes((int) System.nanoTime());
        byte[] bytes3 = IntUtils.getBytes(nextInt);
        byte[] bytes4 = IntUtils.getBytes(nextInt2);
        byte[] bArr = new byte[16];
        System.arraycopy(bytes, 0, bArr, 0, 4);
        System.arraycopy(bytes2, 0, bArr, 4, 4);
        System.arraycopy(bytes3, 0, bArr, 8, 4);
        System.arraycopy(bytes4, 0, bArr, 12, 4);
        return Base64_2.encodeBase64String(bArr);
    }

    public static String getImei(Context context) {
        String imeiBySystem = PhoneInfoUtils.getImeiBySystem(context);
        return StringUtils.isEmpty(imeiBySystem) ? getUniqueID() : imeiBySystem;
    }

    public static String getImsi(Context context) {
        String imsiBySystem = PhoneInfoUtils.getImsiBySystem(context);
        return StringUtils.isEmpty(imsiBySystem) ? getUniqueID() : imsiBySystem;
    }
}
