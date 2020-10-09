package com.ali.user.mobile.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.LruCache;

@SuppressLint({"NewApi"})
public class ImageUtil {
    private static LruCache<String, Bitmap> mMemoryCache = new LruCache<>(5);

    public static void addBitmapToMemoryCache(String str, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(str) == null) {
            mMemoryCache.put(str, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemoryCache(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<>(5);
        }
        return mMemoryCache.get(str);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int i) {
        int i2 = options.outWidth;
        if (i2 > i) {
            return Math.round(((float) i2) / ((float) i));
        }
        return 1;
    }

    public static Bitmap decodeSampledBitmapFromResource(String str, int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = calculateInSampleSize(options, i);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }
}
