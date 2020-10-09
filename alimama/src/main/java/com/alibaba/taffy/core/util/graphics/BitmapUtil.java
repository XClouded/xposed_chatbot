package com.alibaba.taffy.core.util.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.core.internal.view.SupportMenu;
import com.alibaba.taffy.core.util.lang.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public class BitmapUtil {
    private static final int DEFAULT_JPEG_QUALITY = 80;
    private static final String TAG = "BitmapUtil";

    public static Bitmap fitByHeight(Bitmap bitmap, int i) {
        return fitByHeight(bitmap, i, 0.9f);
    }

    public static Bitmap fitByHeight(Bitmap bitmap, int i, float f) {
        if (bitmap == null || i <= 0) {
            return bitmap;
        }
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("bitmap width <=0 or height <=0");
        }
        if ((((float) height) * 1.0f) / ((float) width) < f) {
            bitmap = rotate(bitmap, 0, 0, width, height, 90.0f, true);
        }
        return scaleByHeigth(bitmap, i, 0.1f);
    }

    public static Bitmap fitByWidth(Bitmap bitmap, int i) {
        return fitByWidth(bitmap, i, 0.9f);
    }

    public static Bitmap fitByWidth(Bitmap bitmap, int i, float f) {
        if (bitmap == null || i <= 0) {
            return bitmap;
        }
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("bitmap width <=0 or height <=0");
        }
        if ((((float) width) * 1.0f) / ((float) height) < f) {
            bitmap = rotate(bitmap, 0, 0, width, height, 90.0f, true);
        }
        return scaleByWidth(bitmap, i, 0.1f);
    }

    public static Bitmap scaleByWidth(Bitmap bitmap, int i) {
        return scaleByWidth(bitmap, i, 0.9f);
    }

    public static Bitmap scaleByWidth(Bitmap bitmap, int i, float f) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float f2 = (((float) i) * 1.0f) / ((float) width);
        float f3 = f2 - 1.0f;
        if (f3 < f || f3 > f) {
            return scale(bitmap, 0, 0, width, height, f2, true);
        }
        return bitmap;
    }

    public static Bitmap scaleByHeigth(Bitmap bitmap, int i) {
        return scaleByHeigth(bitmap, i, 0.9f);
    }

    public static Bitmap scaleByHeigth(Bitmap bitmap, int i, float f) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float f2 = (((float) i) * 1.0f) / ((float) height);
        float f3 = f2 - 1.0f;
        if (f3 < f || f3 > f) {
            return scale(bitmap, 0, 0, width, height, f2, true);
        }
        return bitmap;
    }

    public static Bitmap scale(Bitmap bitmap, int i, int i2, int i3, int i4, float f, boolean z) {
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, i, i2, i3, i4, matrix, true);
        if (createBitmap != bitmap && z) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static Bitmap rotate(Bitmap bitmap, int i, int i2, int i3, int i4, float f, boolean z) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, i, i2, i3, i4, matrix, true);
        if (createBitmap != bitmap && z) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static Bitmap readFromFile(String str) {
        try {
            return readFromStream(new FileInputStream(str));
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static Bitmap readFromFile(String str, int i, int i2) {
        try {
            return readFromStream(new FileInputStream(str), i, i2);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static Bitmap readFromURI(String str) {
        try {
            return readFromStream((InputStream) new URL(str).getContent());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static Bitmap readFromURI(String str, int i, int i2) {
        try {
            return readFromStream((InputStream) new URL(str).getContent(), i, i2);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static Bitmap readFromStream(InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, (Rect) null, options);
        int i = options.outWidth;
        int i2 = options.outHeight;
        options.inJustDecodeBounds = false;
        if (i <= 0 || i2 <= 0) {
            return null;
        }
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(inputStream, (Rect) null, options);
    }

    public static Bitmap readFromStream(InputStream inputStream, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, (Rect) null, options);
        int i3 = options.outWidth;
        int i4 = options.outHeight;
        options.inJustDecodeBounds = false;
        if (i3 <= 0 || i4 <= 0) {
            return null;
        }
        options.inSampleSize = getSampleSize(i3, i4, i, i2);
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(inputStream, (Rect) null, options);
    }

    private static int getSampleSize(int i, int i2, int i3, int i4) {
        if (i3 <= 0) {
            i3 = i;
        }
        if (i4 <= 0) {
            i4 = i2;
        }
        int i5 = i / i3;
        int i6 = i2 / i4;
        return i5 > i6 ? i5 : i6;
    }

    public static boolean isAvailableBitmap(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    public static byte[] toBytes(Bitmap bitmap) {
        if (!isAvailableBitmap(bitmap)) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Drawable toDrawable(Resources resources, Bitmap bitmap) {
        return new BitmapDrawable(resources, bitmap);
    }

    public static Bitmap toBitmap(byte[] bArr) {
        if (bArr != null) {
            return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        }
        return null;
    }

    public static Bitmap toBitmap(String str) {
        if (!StringUtil.isNotEmpty(str)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = true;
        options.inPreferredConfig = null;
        return BitmapFactory.decodeFile(str, options);
    }

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    public static Bitmap toBitmap(Resources resources, int i) {
        return BitmapFactory.decodeResource(resources, i);
    }

    public static Bitmap fitSize(Bitmap bitmap, long j) {
        if (bitmap == null) {
            return null;
        }
        long byteCount = (long) bitmap.getByteCount();
        if (byteCount <= 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        double d = (double) (byteCount / j);
        if (Math.ceil(d) < 2.0d) {
            matrix.postScale(0.9f, 0.9f);
        } else if (Math.ceil(d) >= 4.0d) {
            matrix.postScale(0.25f, 0.25f);
        } else {
            matrix.postScale(0.5f, 0.5f);
        }
        if (byteCount <= j) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        recycleQuietly(bitmap);
        return fitSize(createBitmap, j);
    }

    public static byte[] compressToBytes(Bitmap bitmap) {
        return compressToBytes(bitmap, 80);
    }

    public static byte[] compressToBytes(Bitmap bitmap, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(65536);
        bitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void recycleQuietly(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                bitmap.recycle();
            } catch (Throwable th) {
                Log.w(TAG, "unable recycle bitmap", th);
            }
        }
    }

    public static Bitmap roundCorner(Bitmap bitmap, float f) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public static Bitmap reflection(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        int i = height / 2;
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, i, width, i, matrix, false);
        Bitmap createBitmap2 = Bitmap.createBitmap(width, i + height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        float f = (float) height;
        float f2 = (float) (height + 4);
        Canvas canvas2 = canvas;
        float f3 = f;
        float f4 = (float) width;
        canvas2.drawRect(0.0f, f3, f4, f2, new Paint());
        canvas.drawBitmap(createBitmap, 0.0f, f2, (Paint) null);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, (float) bitmap.getHeight(), 0.0f, (float) (createBitmap2.getHeight() + 4), 1895825407, 16777215, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas2.drawRect(0.0f, f3, f4, (float) (createBitmap2.getHeight() + 4), paint);
        return createBitmap2;
    }

    public static boolean isJpeg(byte[] bArr) {
        if (bArr == null || bArr.length <= 2 || bArr[0] != -1 || bArr[1] != -40) {
            return false;
        }
        return true;
    }

    public static boolean isPng(byte[] bArr) {
        if (bArr != null && bArr.length > 8 && bArr[0] == -119 && bArr[1] == 80 && bArr[2] == 78 && bArr[3] == 71 && bArr[4] == 13 && bArr[5] == 10 && bArr[6] == 26 && bArr[7] == 10) {
            return true;
        }
        return false;
    }

    public static byte[] toJpeg(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap == null) {
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] toPng(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap == null) {
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap toGrayscaleAndMark(Context context, Bitmap bitmap, String str) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float f = context.getResources().getDisplayMetrics().density;
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        paint.reset();
        paint.setFlags(1);
        Rect rect = new Rect();
        paint.setTextSize((float) ((int) (f * 14.0f)));
        paint.getTextBounds(str, 0, str.length(), rect);
        paint.setColor(-1);
        int width2 = (createBitmap.getWidth() - rect.width()) / 2;
        int height2 = (createBitmap.getHeight() - rect.height()) / 2;
        float f2 = (float) width2;
        canvas.drawRect(f2, (float) height2, (float) (width2 + rect.width() + 5), (float) (rect.height() + height2 + 5), paint);
        paint.setColor(SupportMenu.CATEGORY_MASK);
        canvas.drawText(str, f2, (float) (height2 + rect.height()), paint);
        return createBitmap;
    }
}
