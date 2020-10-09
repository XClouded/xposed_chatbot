package com.taobao.pexode.decoder;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.PexodeOptions;
import com.taobao.pexode.common.AshmemBitmapFactory;
import com.taobao.pexode.common.DegradeEventListener;
import com.taobao.pexode.common.NdkCore;
import com.taobao.pexode.entity.RewindableStream;
import com.taobao.pexode.exception.PexodeException;
import com.taobao.tcommon.log.FLog;
import java.io.IOException;
import java.lang.reflect.Field;

public abstract class FilledBitmapDecoder implements Decoder {
    protected static Field sBitmapBufferField;

    /* access modifiers changed from: protected */
    public abstract Bitmap decodeAshmem(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws PexodeException, IOException;

    /* access modifiers changed from: protected */
    public abstract Bitmap decodeInBitmap(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws PexodeException, IOException;

    /* access modifiers changed from: protected */
    public abstract Bitmap decodeNormal(RewindableStream rewindableStream, PexodeOptions pexodeOptions) throws PexodeException;

    /* access modifiers changed from: protected */
    public synchronized boolean ensureBitmapBufferField() {
        if (sBitmapBufferField == null) {
            try {
                sBitmapBufferField = Bitmap.class.getDeclaredField("mBuffer");
                sBitmapBufferField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                FLog.e(Pexode.TAG, "ensure Bitmap buffer field error=%s", e);
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public byte[] getPixelBufferFromBitmap(Bitmap bitmap) {
        try {
            if (ensureBitmapBufferField()) {
                return (byte[]) sBitmapBufferField.get(bitmap);
            }
            return null;
        } catch (Exception e) {
            FLog.e(Pexode.TAG, "get Bitmap buffer field error=%s", e);
            return null;
        }
    }

    protected static long getPixelAddressFromBitmap(@NonNull Bitmap bitmap) {
        long[] jArr = {0};
        try {
            NdkCore.nativePinBitmapWithAddr(bitmap, jArr);
        } catch (Throwable th) {
            FLog.e(Pexode.TAG, "get Bitmap pixels address error=%s", th);
        }
        return jArr[0];
    }

    protected static Bitmap newBitmap(PexodeOptions pexodeOptions, boolean z) {
        if (z) {
            return AshmemBitmapFactory.instance().newBitmap(pexodeOptions.outWidth, pexodeOptions.outHeight, PexodeOptions.CONFIG);
        }
        return Bitmap.createBitmap(pexodeOptions.outWidth, pexodeOptions.outHeight, PexodeOptions.CONFIG);
    }

    protected static boolean invalidBitmap(Bitmap bitmap, PexodeOptions pexodeOptions, String str) {
        if (bitmap == null) {
            FLog.e(Pexode.TAG, "%s bitmap is null", str);
            return true;
        } else if (bitmap.getWidth() * bitmap.getHeight() >= pexodeOptions.outWidth * pexodeOptions.outHeight) {
            return false;
        } else {
            FLog.e(Pexode.TAG, "%s bitmap space not large enough", str);
            return true;
        }
    }
}
