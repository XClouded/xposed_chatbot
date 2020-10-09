package com.alimama.unionwl.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import java.io.InputStream;

public class BitmapUtils {
    public static Bitmap resizeImage(Bitmap bitmap, int i) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = ((float) i) / ((float) width);
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    private static Bitmap createScaleBitmap(Bitmap bitmap, int i, int i2) {
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i2, false);
        if (bitmap != createScaledBitmap) {
            bitmap.recycle();
        }
        return createScaledBitmap;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int i, int i2, int i3) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = calculateInSampleSize(options, i2, i3);
        options.inJustDecodeBounds = false;
        return createScaleBitmap(BitmapFactory.decodeResource(resources, i, options), i2, i3);
    }

    public static Bitmap decodeSampledBitmapFromSteam(InputStream inputStream, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, (Rect) null, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return createScaleBitmap(BitmapFactory.decodeStream(inputStream, (Rect) null, options), i, i2);
    }

    public static Bitmap decodeSampledBitmapFromFd(String str, int i, int i2) {
        return decodeSampledBitmapFromFd(str, i, i2, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap decodeSampledBitmapFromFd(String str, int i, int i2, Bitmap.Config config) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = config;
        return createScaleBitmap(BitmapFactory.decodeFile(str, options), i, i2);
    }

    public static Bitmap decodeSampledBitmap(byte[] bArr, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
    }

    public static int calculateInSampleSize(byte[] bArr, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        return calculateInSampleSize(options, i, i2);
    }

    public static int calculateInSampleSize(String str, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        return calculateInSampleSize(options, i, i2);
    }

    public static Bitmap convertViewToBitmap(View view) {
        Canvas canvas;
        Bitmap bitmap;
        if (view == null) {
            return null;
        }
        if (view.getMeasuredWidth() <= 0) {
            view.measure(-2, -2);
            bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        } else {
            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap increaseBitmap(Bitmap bitmap, int i, int i2, int i3) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        RectF rectF = new RectF(0.0f, 0.0f, (float) i, (float) i2);
        Paint paint = new Paint();
        paint.setColor(i3);
        canvas.drawRect(rectF, paint);
        canvas.drawBitmap(bitmap, (float) ((i - width) / 2), (float) ((i2 - height) / 2), (Paint) null);
        canvas.save(31);
        canvas.restore();
        return createBitmap;
    }
}
