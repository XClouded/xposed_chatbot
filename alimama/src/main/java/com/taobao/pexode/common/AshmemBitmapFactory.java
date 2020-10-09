package com.taobao.pexode.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.taobao.pexode.DecodeHelper;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.decoder.SystemDecoder;
import com.taobao.tcommon.log.FLog;

public class AshmemBitmapFactory implements NewBitmapFactory {

    private static class Singleton {
        /* access modifiers changed from: private */
        public static final AshmemBitmapFactory INSTANCE = new AshmemBitmapFactory();

        private Singleton() {
        }
    }

    public static AshmemBitmapFactory instance() {
        return Singleton.INSTANCE;
    }

    public Bitmap newBitmap(int i, int i2, Bitmap.Config config) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;
        SystemDecoder.setupAshmemOptions(options, true);
        byte[] generateBytes = EmptyJpegGenerator.generateBytes(i, i2, DecodeHelper.instance().offerBytes(EmptyJpegGenerator.FIXED_JPG_LENGTH));
        if (generateBytes != null) {
            bitmap = BitmapFactory.decodeByteArray(generateBytes, 0, EmptyJpegGenerator.FIXED_JPG_LENGTH, options);
            DecodeHelper.instance().releaseBytes(generateBytes);
        } else {
            bitmap = null;
        }
        if (bitmap != null) {
            bitmap.setHasAlpha(true);
        }
        return bitmap;
    }

    public Bitmap newBitmapWithPin(int i, int i2, Bitmap.Config config) {
        Bitmap newBitmap = newBitmap(i, i2, config);
        if (newBitmap == null) {
            return newBitmap;
        }
        try {
            NdkCore.nativePinBitmap(newBitmap);
            newBitmap.eraseColor(0);
            return newBitmap;
        } catch (Throwable th) {
            FLog.e(Pexode.TAG, "AshmemBitmapFactory native pin bitmap error=%s", th);
            return null;
        }
    }
}
