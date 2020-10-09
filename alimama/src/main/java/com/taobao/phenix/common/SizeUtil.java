package com.taobao.phenix.common;

import android.graphics.Bitmap;
import android.os.Build;
import androidx.core.internal.view.SupportMenu;

public class SizeUtil {
    public static int getSplitHeight(int i) {
        return i & 65535;
    }

    public static int getSplitWidth(int i) {
        return (i & SupportMenu.CATEGORY_MASK) >> 16;
    }

    public static int mergeWH(int i, int i2) {
        return (i << 16) | (i2 & 65535);
    }

    public static int findBestSampleSize(int i, int i2, int i3, int i4) {
        double d = (double) i;
        double d2 = (double) i3;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = (double) i2;
        double d4 = (double) i4;
        Double.isNaN(d3);
        Double.isNaN(d4);
        double min = Math.min(d / d2, d3 / d4);
        float f = 1.0f;
        while (true) {
            float f2 = 2.0f * f;
            if (((double) f2) > min) {
                return (int) f;
            }
            f = f2;
        }
    }

    public static int getResizedDimension(int i, int i2, int i3, int i4) {
        if (i == 0 && i2 == 0) {
            return i3;
        }
        if (i == 0) {
            double d = (double) i2;
            double d2 = (double) i4;
            Double.isNaN(d);
            Double.isNaN(d2);
            double d3 = (double) i3;
            Double.isNaN(d3);
            return (int) (d3 * (d / d2));
        } else if (i2 == 0) {
            return i;
        } else {
            double d4 = (double) i4;
            double d5 = (double) i3;
            Double.isNaN(d4);
            Double.isNaN(d5);
            double d6 = d4 / d5;
            double d7 = (double) i;
            Double.isNaN(d7);
            double d8 = (double) i2;
            if (d7 * d6 <= d8) {
                return i;
            }
            Double.isNaN(d8);
            return (int) (d8 / d6);
        }
    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                return bitmap.getAllocationByteCount();
            } catch (NullPointerException unused) {
            }
        }
        return bitmap.getHeight() * bitmap.getRowBytes();
    }
}
