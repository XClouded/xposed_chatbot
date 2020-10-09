package com.taobao.phenix.compat.effects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.RSRuntimeException;
import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import com.taobao.pexode.PexodeOptions;
import com.taobao.phenix.bitmap.BitmapProcessor;
import com.taobao.phenix.compat.effects.internal.NdkCore;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.tcommon.log.FLog;
import java.lang.reflect.Array;

public class BlurBitmapProcessor implements BitmapProcessor {
    private static int DEFAULT_DOWN_SAMPLING = 1;
    private static int RS_MAX_RADIUS = 25;
    private final Context mContext;
    private final int mRadius;
    private final int mSampling;

    public BlurBitmapProcessor(Context context) {
        this(context, RS_MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public BlurBitmapProcessor(Context context, int i) {
        this(context, i, DEFAULT_DOWN_SAMPLING);
    }

    public BlurBitmapProcessor(Context context, int i, int i2) {
        boolean z = false;
        Preconditions.checkArgument(i > 0, "blur radius must be greater than zero");
        Preconditions.checkArgument(i2 > 0 ? true : z, "blur sampling must be greater than zero");
        this.mContext = context;
        this.mRadius = i;
        this.mSampling = i2;
    }

    public String getId() {
        return "R" + this.mRadius + "$S" + this.mSampling;
    }

    public Bitmap process(@NonNull String str, @NonNull BitmapProcessor.BitmapSupplier bitmapSupplier, @NonNull Bitmap bitmap) {
        Bitmap bitmap2 = bitmapSupplier.get(bitmap.getWidth() / this.mSampling, bitmap.getHeight() / this.mSampling, bitmap.getConfig() != null ? bitmap.getConfig() : PexodeOptions.CONFIG);
        Canvas canvas = new Canvas(bitmap2);
        canvas.scale(1.0f / ((float) this.mSampling), 1.0f / ((float) this.mSampling));
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        Bitmap blurBitmap = NdkCore.blurBitmap(bitmap2, this.mRadius);
        if (blurBitmap == null && Build.VERSION.SDK_INT >= 18 && this.mRadius <= RS_MAX_RADIUS) {
            try {
                blurBitmap = blurWithRenderScript(this.mContext, bitmap2, this.mRadius);
            } catch (RSRuntimeException e) {
                FLog.i("Effects4Phenix", "render script blur error=%s", e);
            }
        }
        return blurBitmap == null ? blurWithJava(bitmap2, this.mRadius, true) : blurBitmap;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: android.renderscript.Allocation} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: android.renderscript.Allocation} */
    /* JADX WARNING: type inference failed for: r1v0, types: [android.renderscript.Allocation] */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0067  */
    @android.annotation.TargetApi(18)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap blurWithRenderScript(android.content.Context r4, android.graphics.Bitmap r5, int r6) throws android.renderscript.RSRuntimeException {
        /*
            r0 = 0
            android.renderscript.RenderScript r4 = android.renderscript.RenderScript.create(r4)     // Catch:{ all -> 0x0052 }
            android.renderscript.RenderScript$RSMessageHandler r1 = new android.renderscript.RenderScript$RSMessageHandler     // Catch:{ all -> 0x004f }
            r1.<init>()     // Catch:{ all -> 0x004f }
            r4.setMessageHandler(r1)     // Catch:{ all -> 0x004f }
            android.renderscript.Allocation$MipmapControl r1 = android.renderscript.Allocation.MipmapControl.MIPMAP_NONE     // Catch:{ all -> 0x004f }
            r2 = 1
            android.renderscript.Allocation r1 = android.renderscript.Allocation.createFromBitmap(r4, r5, r1, r2)     // Catch:{ all -> 0x004f }
            android.renderscript.Type r2 = r1.getType()     // Catch:{ all -> 0x004c }
            android.renderscript.Allocation r2 = android.renderscript.Allocation.createTyped(r4, r2)     // Catch:{ all -> 0x004c }
            android.renderscript.Element r3 = android.renderscript.Element.U8_4(r4)     // Catch:{ all -> 0x0048 }
            android.renderscript.ScriptIntrinsicBlur r3 = android.renderscript.ScriptIntrinsicBlur.create(r4, r3)     // Catch:{ all -> 0x0048 }
            r3.setInput(r1)     // Catch:{ all -> 0x0046 }
            float r6 = (float) r6     // Catch:{ all -> 0x0046 }
            r3.setRadius(r6)     // Catch:{ all -> 0x0046 }
            r3.forEach(r2)     // Catch:{ all -> 0x0046 }
            r2.copyTo(r5)     // Catch:{ all -> 0x0046 }
            if (r4 == 0) goto L_0x0036
            r4.destroy()
        L_0x0036:
            if (r1 == 0) goto L_0x003b
            r1.destroy()
        L_0x003b:
            if (r2 == 0) goto L_0x0040
            r2.destroy()
        L_0x0040:
            if (r3 == 0) goto L_0x0045
            r3.destroy()
        L_0x0045:
            return r5
        L_0x0046:
            r5 = move-exception
            goto L_0x004a
        L_0x0048:
            r5 = move-exception
            r3 = r0
        L_0x004a:
            r0 = r2
            goto L_0x0056
        L_0x004c:
            r5 = move-exception
            r3 = r0
            goto L_0x0056
        L_0x004f:
            r5 = move-exception
            r1 = r0
            goto L_0x0055
        L_0x0052:
            r5 = move-exception
            r4 = r0
            r1 = r4
        L_0x0055:
            r3 = r1
        L_0x0056:
            if (r4 == 0) goto L_0x005b
            r4.destroy()
        L_0x005b:
            if (r1 == 0) goto L_0x0060
            r1.destroy()
        L_0x0060:
            if (r0 == 0) goto L_0x0065
            r0.destroy()
        L_0x0065:
            if (r3 == 0) goto L_0x006a
            r3.destroy()
        L_0x006a:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.compat.effects.BlurBitmapProcessor.blurWithRenderScript(android.content.Context, android.graphics.Bitmap, int):android.graphics.Bitmap");
    }

    public static Bitmap blurWithJava(Bitmap bitmap, int i, boolean z) {
        Bitmap bitmap2;
        int[] iArr;
        int i2 = i;
        if (z) {
            bitmap2 = bitmap;
        } else {
            bitmap2 = bitmap.copy(bitmap.getConfig(), true);
        }
        if (i2 < 1) {
            return null;
        }
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int i3 = width * height;
        int[] iArr2 = new int[i3];
        bitmap2.getPixels(iArr2, 0, width, 0, 0, width, height);
        int i4 = width - 1;
        int i5 = height - 1;
        int i6 = i2 + i2 + 1;
        int[] iArr3 = new int[i3];
        int[] iArr4 = new int[i3];
        int[] iArr5 = new int[i3];
        int[] iArr6 = new int[Math.max(width, height)];
        int i7 = (i6 + 1) >> 1;
        int i8 = i7 * i7;
        int i9 = i8 * 256;
        int[] iArr7 = new int[i9];
        for (int i10 = 0; i10 < i9; i10++) {
            iArr7[i10] = i10 / i8;
        }
        int[][] iArr8 = (int[][]) Array.newInstance(int.class, new int[]{i6, 3});
        int i11 = i2 + 1;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        while (i12 < height) {
            Bitmap bitmap3 = bitmap2;
            int i15 = -i2;
            int i16 = 0;
            int i17 = 0;
            int i18 = 0;
            int i19 = 0;
            int i20 = 0;
            int i21 = 0;
            int i22 = 0;
            int i23 = 0;
            int i24 = 0;
            while (i15 <= i2) {
                int i25 = i5;
                int i26 = height;
                int i27 = iArr2[i13 + Math.min(i4, Math.max(i15, 0))];
                int[] iArr9 = iArr8[i15 + i2];
                iArr9[0] = (i27 & 16711680) >> 16;
                iArr9[1] = (i27 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr9[2] = i27 & 255;
                int abs = i11 - Math.abs(i15);
                i16 += iArr9[0] * abs;
                i17 += iArr9[1] * abs;
                i18 += iArr9[2] * abs;
                if (i15 > 0) {
                    i19 += iArr9[0];
                    i20 += iArr9[1];
                    i21 += iArr9[2];
                } else {
                    i22 += iArr9[0];
                    i23 += iArr9[1];
                    i24 += iArr9[2];
                }
                i15++;
                height = i26;
                i5 = i25;
            }
            int i28 = i5;
            int i29 = height;
            int i30 = i2;
            int i31 = 0;
            while (i31 < width) {
                iArr3[i13] = iArr7[i16];
                iArr4[i13] = iArr7[i17];
                iArr5[i13] = iArr7[i18];
                int i32 = i16 - i22;
                int i33 = i17 - i23;
                int i34 = i18 - i24;
                int[] iArr10 = iArr8[((i30 - i2) + i6) % i6];
                int i35 = i22 - iArr10[0];
                int i36 = i23 - iArr10[1];
                int i37 = i24 - iArr10[2];
                if (i12 == 0) {
                    iArr = iArr7;
                    iArr6[i31] = Math.min(i31 + i2 + 1, i4);
                } else {
                    iArr = iArr7;
                }
                int i38 = iArr2[i14 + iArr6[i31]];
                iArr10[0] = (i38 & 16711680) >> 16;
                iArr10[1] = (i38 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr10[2] = i38 & 255;
                int i39 = i19 + iArr10[0];
                int i40 = i20 + iArr10[1];
                int i41 = i21 + iArr10[2];
                i16 = i32 + i39;
                i17 = i33 + i40;
                i18 = i34 + i41;
                i30 = (i30 + 1) % i6;
                int[] iArr11 = iArr8[i30 % i6];
                i22 = i35 + iArr11[0];
                i23 = i36 + iArr11[1];
                i24 = i37 + iArr11[2];
                i19 = i39 - iArr11[0];
                i20 = i40 - iArr11[1];
                i21 = i41 - iArr11[2];
                i13++;
                i31++;
                iArr7 = iArr;
            }
            int[] iArr12 = iArr7;
            i14 += width;
            i12++;
            bitmap2 = bitmap3;
            height = i29;
            i5 = i28;
        }
        Bitmap bitmap4 = bitmap2;
        int i42 = i5;
        int i43 = height;
        int[] iArr13 = iArr7;
        int i44 = 0;
        while (i44 < width) {
            int i45 = -i2;
            int i46 = i45 * width;
            int i47 = 0;
            int i48 = 0;
            int i49 = 0;
            int i50 = 0;
            int i51 = 0;
            int i52 = 0;
            int i53 = 0;
            int i54 = 0;
            int i55 = 0;
            while (i45 <= i2) {
                int[] iArr14 = iArr6;
                int max = Math.max(0, i46) + i44;
                int[] iArr15 = iArr8[i45 + i2];
                iArr15[0] = iArr3[max];
                iArr15[1] = iArr4[max];
                iArr15[2] = iArr5[max];
                int abs2 = i11 - Math.abs(i45);
                i47 += iArr3[max] * abs2;
                i48 += iArr4[max] * abs2;
                i49 += iArr5[max] * abs2;
                if (i45 > 0) {
                    i50 += iArr15[0];
                    i51 += iArr15[1];
                    i52 += iArr15[2];
                } else {
                    i53 += iArr15[0];
                    i54 += iArr15[1];
                    i55 += iArr15[2];
                }
                int i56 = i42;
                if (i45 < i56) {
                    i46 += width;
                }
                i45++;
                i42 = i56;
                iArr6 = iArr14;
            }
            int[] iArr16 = iArr6;
            int i57 = i42;
            int i58 = i44;
            int i59 = i51;
            int i60 = i52;
            int i61 = 0;
            int i62 = i2;
            int i63 = i50;
            int i64 = i49;
            int i65 = i48;
            int i66 = i47;
            int i67 = i43;
            while (i61 < i67) {
                iArr2[i58] = (iArr2[i58] & -16777216) | (iArr13[i66] << 16) | (iArr13[i65] << 8) | iArr13[i64];
                int i68 = i66 - i53;
                int i69 = i65 - i54;
                int i70 = i64 - i55;
                int[] iArr17 = iArr8[((i62 - i2) + i6) % i6];
                int i71 = i53 - iArr17[0];
                int i72 = i54 - iArr17[1];
                int i73 = i55 - iArr17[2];
                if (i44 == 0) {
                    iArr16[i61] = Math.min(i61 + i11, i57) * width;
                }
                int i74 = iArr16[i61] + i44;
                iArr17[0] = iArr3[i74];
                iArr17[1] = iArr4[i74];
                iArr17[2] = iArr5[i74];
                int i75 = i63 + iArr17[0];
                int i76 = i59 + iArr17[1];
                int i77 = i60 + iArr17[2];
                i66 = i68 + i75;
                i65 = i69 + i76;
                i64 = i70 + i77;
                i62 = (i62 + 1) % i6;
                int[] iArr18 = iArr8[i62];
                i53 = i71 + iArr18[0];
                i54 = i72 + iArr18[1];
                i55 = i73 + iArr18[2];
                i63 = i75 - iArr18[0];
                i59 = i76 - iArr18[1];
                i60 = i77 - iArr18[2];
                i58 += width;
                i61++;
                i2 = i;
            }
            i44++;
            i42 = i57;
            i43 = i67;
            iArr6 = iArr16;
            i2 = i;
        }
        bitmap4.setPixels(iArr2, 0, width, 0, 0, width, i43);
        return bitmap4;
    }
}
