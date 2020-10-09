package com.alibaba.android.enhance.gpuimage.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Utils {
    private static final int DEFAULT_BACKGROUND_COLOR = -1;
    private static final float DEFAULT_SCALE_FACTOR = 2.0f;
    private static final int ROUNDING_VALUE = 4;
    private static float roundingHeightScaleFactor = 1.0f;
    private static float roundingWidthScaleFactor = 1.0f;

    private Utils() {
    }

    @Nullable
    public static Bitmap allocateBitmapFromView(@Nullable View view) {
        if (view == null) {
            return null;
        }
        try {
            return prepareSnapshot(view);
        } catch (Throwable th) {
            Log.e("image-filter", "allocated bitmap failed", th);
            return null;
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setEffectParameter(@androidx.annotation.NonNull android.media.effect.Effect r4, @androidx.annotation.NonNull java.lang.String r5, @androidx.annotation.NonNull java.lang.String r6, @androidx.annotation.NonNull java.lang.Object r7) {
        /*
            int r0 = r5.hashCode()
            r1 = 0
            switch(r0) {
                case -2083565529: goto L_0x0132;
                case -1987764372: goto L_0x0127;
                case -1970052319: goto L_0x011c;
                case -1840184026: goto L_0x0111;
                case -1827950437: goto L_0x0107;
                case -1754807094: goto L_0x00fd;
                case -1609692616: goto L_0x00f2;
                case -1497565694: goto L_0x00e7;
                case -1160544162: goto L_0x00dc;
                case -1024700271: goto L_0x00d0;
                case -924844493: goto L_0x00c5;
                case -674528384: goto L_0x00ba;
                case -593975676: goto L_0x00ae;
                case -588536232: goto L_0x00a3;
                case -485871724: goto L_0x0097;
                case -318471136: goto L_0x008b;
                case -260034836: goto L_0x007f;
                case -180248046: goto L_0x0073;
                case 143172044: goto L_0x0067;
                case 147674647: goto L_0x005c;
                case 231150570: goto L_0x0050;
                case 256864094: goto L_0x0044;
                case 458539841: goto L_0x0039;
                case 593158210: goto L_0x002e;
                case 701057643: goto L_0x0022;
                case 796341700: goto L_0x0016;
                case 1042901592: goto L_0x000a;
                default: goto L_0x0008;
            }
        L_0x0008:
            goto L_0x013d
        L_0x000a:
            java.lang.String r0 = "android.media.effect.effects.PosterizeEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 17
            goto L_0x013e
        L_0x0016:
            java.lang.String r0 = "android.media.effect.effects.FillLightEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 11
            goto L_0x013e
        L_0x0022:
            java.lang.String r0 = "android.media.effect.effects.VignetteEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 26
            goto L_0x013e
        L_0x002e:
            java.lang.String r0 = "android.media.effect.effects.BrightnessEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 0
            goto L_0x013e
        L_0x0039:
            java.lang.String r0 = "android.media.effect.effects.CropEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 6
            goto L_0x013e
        L_0x0044:
            java.lang.String r0 = "android.media.effect.effects.FlipEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 12
            goto L_0x013e
        L_0x0050:
            java.lang.String r0 = "android.media.effect.effects.LomoishEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 15
            goto L_0x013e
        L_0x005c:
            java.lang.String r0 = "android.media.effect.effects.AutoFixEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 4
            goto L_0x013e
        L_0x0067:
            java.lang.String r0 = "android.media.effect.effects.RotateEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 19
            goto L_0x013e
        L_0x0073:
            java.lang.String r0 = "android.media.effect.effects.BitmapOverlayEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 9
            goto L_0x013e
        L_0x007f:
            java.lang.String r0 = "android.media.effect.effects.GrainEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 13
            goto L_0x013e
        L_0x008b:
            java.lang.String r0 = "android.media.effect.effects.SaturateEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 20
            goto L_0x013e
        L_0x0097:
            java.lang.String r0 = "android.media.effect.effects.StraightenEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 23
            goto L_0x013e
        L_0x00a3:
            java.lang.String r0 = "android.media.effect.effects.BackDropperEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 3
            goto L_0x013e
        L_0x00ae:
            java.lang.String r0 = "android.media.effect.effects.SharpenEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 22
            goto L_0x013e
        L_0x00ba:
            java.lang.String r0 = "android.media.effect.effects.CrossProcessEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 7
            goto L_0x013e
        L_0x00c5:
            java.lang.String r0 = "android.media.effect.effects.ContrastEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 1
            goto L_0x013e
        L_0x00d0:
            java.lang.String r0 = "android.media.effect.effects.RedEyeEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 18
            goto L_0x013e
        L_0x00dc:
            java.lang.String r0 = "android.media.effect.effects.DocumentaryEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 8
            goto L_0x013e
        L_0x00e7:
            java.lang.String r0 = "android.media.effect.effects.ColorTemperatureEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 24
            goto L_0x013e
        L_0x00f2:
            java.lang.String r0 = "android.media.effect.effects.GrayscaleEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 14
            goto L_0x013e
        L_0x00fd:
            java.lang.String r0 = "android.media.effect.effects.FisheyeEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 2
            goto L_0x013e
        L_0x0107:
            java.lang.String r0 = "android.media.effect.effects.BlackWhiteEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 5
            goto L_0x013e
        L_0x0111:
            java.lang.String r0 = "android.media.effect.effects.NegativeEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 16
            goto L_0x013e
        L_0x011c:
            java.lang.String r0 = "android.media.effect.effects.DuotoneEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 10
            goto L_0x013e
        L_0x0127:
            java.lang.String r0 = "android.media.effect.effects.TintEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 25
            goto L_0x013e
        L_0x0132:
            java.lang.String r0 = "android.media.effect.effects.SepiaEffect"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x013d
            r5 = 21
            goto L_0x013e
        L_0x013d:
            r5 = -1
        L_0x013e:
            r0 = 0
            r2 = 1065353216(0x3f800000, float:1.0)
            r3 = 0
            switch(r5) {
                case 0: goto L_0x03b1;
                case 1: goto L_0x039d;
                case 2: goto L_0x037d;
                case 3: goto L_0x03c4;
                case 4: goto L_0x035d;
                case 5: goto L_0x031d;
                case 6: goto L_0x02c1;
                case 7: goto L_0x03c4;
                case 8: goto L_0x03c4;
                case 9: goto L_0x02a2;
                case 10: goto L_0x0281;
                case 11: goto L_0x0260;
                case 12: goto L_0x0243;
                case 13: goto L_0x0222;
                case 14: goto L_0x03c4;
                case 15: goto L_0x03c4;
                case 16: goto L_0x03c4;
                case 17: goto L_0x03c4;
                case 18: goto L_0x03c4;
                case 19: goto L_0x020d;
                case 20: goto L_0x01ea;
                case 21: goto L_0x03c4;
                case 22: goto L_0x01c9;
                case 23: goto L_0x01a4;
                case 24: goto L_0x0181;
                case 25: goto L_0x0168;
                case 26: goto L_0x0147;
                default: goto L_0x0145;
            }
        L_0x0145:
            goto L_0x03c4
        L_0x0147:
            java.lang.String r5 = "scale"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x0168:
            java.lang.String r5 = "tint"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r7, r0)
            int r5 = com.taobao.weex.utils.WXResourceUtils.getColor(r5, r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x0181:
            java.lang.String r5 = "scale"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            r5 = 1056964608(0x3f000000, float:0.5)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x01a4:
            java.lang.String r5 = "angle"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            r7 = -1036779520(0xffffffffc2340000, float:-45.0)
            r0 = 1110704128(0x42340000, float:45.0)
            float r5 = clamp(r5, r7, r0)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x01c9:
            java.lang.String r5 = "scale"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x01ea:
            java.lang.String r5 = "scale"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            r7 = -1082130432(0xffffffffbf800000, float:-1.0)
            float r5 = clamp(r5, r7, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x020d:
            java.lang.String r5 = "angle"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Integer r5 = java.lang.Integer.valueOf(r1)
            java.lang.Integer r5 = com.taobao.weex.utils.WXUtils.getInteger(r7, r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x0222:
            java.lang.String r5 = "strength"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x0243:
            java.lang.String r5 = "vertical"
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x0253
            java.lang.String r5 = "horizontal"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
        L_0x0253:
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x0260:
            java.lang.String r5 = "strength"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x0281:
            java.lang.String r5 = "first_color"
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x0291
            java.lang.String r5 = "second_color"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
        L_0x0291:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r7, r0)
            int r5 = com.taobao.weex.utils.WXResourceUtils.getColor(r5, r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x02a2:
            java.lang.String r5 = "bitmap"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            com.alibaba.android.WeexEnhance$ImageLoadAdapter r5 = com.alibaba.android.enhance.gpuimage.ImageFilterPlugin.getImageAdapter()
            if (r5 == 0) goto L_0x03c4
            java.lang.String r0 = ""
            java.lang.String r7 = com.taobao.weex.utils.WXUtils.getString(r7, r0)
            android.graphics.Bitmap r5 = r5.fetchBitmapSync(r7)
            if (r5 == 0) goto L_0x03c4
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x02c1:
            java.lang.String r5 = "xorigin"
            boolean r5 = r5.equals(r6)
            r0 = 750(0x2ee, float:1.051E-42)
            if (r5 != 0) goto L_0x0302
            java.lang.String r5 = "yorigin"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x02d6
            goto L_0x0302
        L_0x02d6:
            java.lang.String r5 = "width"
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x02e7
            java.lang.String r5 = "height"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
        L_0x02e7:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r1)
            java.lang.Integer r5 = com.taobao.weex.utils.WXUtils.getInteger(r7, r5)
            int r5 = r5.intValue()
            float r5 = (float) r5
            float r5 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r5, r0)
            int r5 = (int) r5
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x0302:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r1)
            java.lang.Integer r5 = com.taobao.weex.utils.WXUtils.getInteger(r7, r5)
            int r5 = r5.intValue()
            float r5 = (float) r5
            float r5 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r5, r0)
            int r5 = (int) r5
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x031d:
            java.lang.String r5 = "black"
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x032e
            java.lang.String r5 = "white"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
        L_0x032e:
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x035d:
            java.lang.String r5 = "scale"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x037d:
            java.lang.String r5 = "scale"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r3)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            float r5 = r5.floatValue()
            float r5 = clamp(r5, r3, r2)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x039d:
            java.lang.String r5 = "contrast"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r2)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            r4.setParameter(r6, r5)
            goto L_0x03c4
        L_0x03b1:
            java.lang.String r5 = "brightness"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03c4
            java.lang.Float r5 = java.lang.Float.valueOf(r2)
            java.lang.Float r5 = com.taobao.weex.utils.WXUtils.getFloat(r7, r5)
            r4.setParameter(r6, r5)
        L_0x03c4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.gpuimage.utils.Utils.setEffectParameter(android.media.effect.Effect, java.lang.String, java.lang.String, java.lang.Object):void");
    }

    private static float clamp(float f, float f2, float f3) {
        return Math.min(Math.max(f, f2), f3);
    }

    @Nullable
    private static Bitmap prepareSnapshot(View view) {
        Bitmap allocateBitmap = allocateBitmap(view);
        if (allocateBitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas(allocateBitmap);
        canvas.save();
        canvas.scale(1.0f / (roundingWidthScaleFactor * DEFAULT_SCALE_FACTOR), 1.0f / (roundingHeightScaleFactor * DEFAULT_SCALE_FACTOR));
        Drawable background = view.getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view.draw(canvas);
        canvas.restore();
        return allocateBitmap;
    }

    @Nullable
    private static Bitmap allocateBitmap(@NonNull View view) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        if (measuredHeight <= 0 || measuredWidth <= 0) {
            return null;
        }
        int downScaleSize = downScaleSize((float) measuredWidth);
        int downScaleSize2 = downScaleSize((float) measuredHeight);
        int roundSize = roundSize(downScaleSize);
        int roundSize2 = roundSize(downScaleSize2);
        roundingHeightScaleFactor = ((float) downScaleSize2) / ((float) roundSize2);
        roundingWidthScaleFactor = ((float) downScaleSize) / ((float) roundSize);
        try {
            return Bitmap.createBitmap(roundSize, roundSize2, Bitmap.Config.ARGB_4444);
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    private static int downScaleSize(float f) {
        return (int) Math.ceil((double) (f / DEFAULT_SCALE_FACTOR));
    }

    private static int roundSize(int i) {
        int i2 = i % 4;
        return i2 == 0 ? i : (i - i2) + 4;
    }
}
