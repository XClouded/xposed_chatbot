package com.taobao.phenix.bitmap;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.common.AshmemBitmapFactory;
import com.taobao.phenix.bitmap.BitmapProcessor;
import com.taobao.phenix.intf.Phenix;

public class BitmapSupplier implements BitmapProcessor.BitmapSupplier {
    private static final BitmapSupplier sBitmapSupplier = new BitmapSupplier();

    public static BitmapSupplier getInstance() {
        return sBitmapSupplier;
    }

    @NonNull
    public Bitmap get(int i, int i2, Bitmap.Config config) {
        Bitmap bitmap;
        if (Pexode.isAshmemSupported()) {
            bitmap = AshmemBitmapFactory.instance().newBitmapWithPin(i, i2, config);
        } else {
            BitmapPool build = Phenix.instance().bitmapPoolBuilder().build();
            bitmap = build != null ? build.getFromPool(i, i2, config) : null;
        }
        return bitmap == null ? Bitmap.createBitmap(i, i2, config) : bitmap;
    }
}
