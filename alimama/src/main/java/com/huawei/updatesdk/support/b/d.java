package com.huawei.updatesdk.support.b;

import android.content.Context;
import androidx.core.view.InputDeviceCompat;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class d {
    public static String a(int i) {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(0);
        return percentInstance.format((double) (((float) i) / 100.0f));
    }

    public static String a(Context context, long j) {
        if (j == 0) {
            return context.getString(com.huawei.updatesdk.support.e.d.b(context, "upsdk_storage_utils"), new Object[]{"0"});
        }
        DecimalFormat decimalFormat = null;
        if (j > 104857) {
            decimalFormat = new DecimalFormat("###.#");
        } else if (j > 10485) {
            decimalFormat = new DecimalFormat("###.##");
        }
        if (decimalFormat != null) {
            double d = (double) j;
            Double.isNaN(d);
            return context.getString(com.huawei.updatesdk.support.e.d.b(context, "upsdk_storage_utils"), new Object[]{decimalFormat.format((d / 1024.0d) / 1024.0d)});
        }
        return context.getString(com.huawei.updatesdk.support.e.d.b(context, "upsdk_storage_utils"), new Object[]{"0.01"});
    }

    public static byte[] a(String str) {
        char[] charArray = str.toCharArray();
        int length = charArray.length / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            int digit = Character.digit(charArray[i2 + 1], 16) | (Character.digit(charArray[i2], 16) << 4);
            if (digit > 127) {
                digit += InputDeviceCompat.SOURCE_ANY;
            }
            bArr[i] = (byte) digit;
        }
        return bArr;
    }
}
