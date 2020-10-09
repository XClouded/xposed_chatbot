package com.taobao.phenix.bitmap;

import android.graphics.Bitmap;
import android.os.Build;
import com.alibaba.analytics.core.device.Constants;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.phenix.common.SizeUtil;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.DecodedImage;
import com.taobao.phenix.entity.EncodedData;
import com.taobao.phenix.entity.EncodedImage;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.produce.BaseChainProducer;
import java.io.ByteArrayOutputStream;

public class BitmapProcessProducer extends BaseChainProducer<DecodedImage, DecodedImage, ImageRequest> {
    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<DecodedImage, ImageRequest> consumer) {
        return false;
    }

    public BitmapProcessProducer() {
        super(0, 2);
    }

    private Bitmap scaleLargeBitmap(Bitmap bitmap, EncodedImage encodedImage) {
        int i;
        int i2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            i2 = encodedImage.targetWidth;
            i = (height * i2) / width;
        } else {
            i = encodedImage.targetHeight;
            i2 = (width * i) / height;
        }
        if (width > i2 || height > i) {
            try {
                UnitedLog.dp("BitmapProcess", encodedImage.path, "scale down from large bitmap, target(%d) < actual(%d)", Integer.valueOf(i2), Integer.valueOf(width));
                return Bitmap.createScaledBitmap(bitmap, i2, i, true);
            } catch (Throwable th) {
                UnitedLog.wp("BitmapProcess", encodedImage.path, "error happen when scaling bitmap, throwable=%s", th);
                return null;
            }
        } else {
            UnitedLog.ip("BitmapProcess", encodedImage.path, "skip to scale from large bitmap, target(%d) >= actual(%d)", Integer.valueOf(i2), Integer.valueOf(width));
            return null;
        }
    }

    private byte[] compressScaledBitmap(ImageRequest imageRequest, Bitmap bitmap, EncodedImage encodedImage) {
        MimeType mimeType = encodedImage.getMimeType();
        boolean z = Build.VERSION.SDK_INT == 28 && Phenix.NO_USE_WEBP_FORMAT;
        byte[] bArr = null;
        if (mimeType != null && !z) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Constants.MAX_UPLOAD_SIZE);
            if (DefaultMimeTypes.PNG.isSame(mimeType) || DefaultMimeTypes.PNG_A.isSame(mimeType)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                bArr = byteArrayOutputStream.toByteArray();
            } else if (DefaultMimeTypes.JPEG.isSame(mimeType)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                bArr = byteArrayOutputStream.toByteArray();
            } else if ((DefaultMimeTypes.WEBP.isSame(mimeType) || DefaultMimeTypes.WEBP_A.isSame(mimeType)) && Pexode.canSystemSupport(mimeType)) {
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                UnitedLog.dp("BitmapProcess", encodedImage.path, "compress target bitmap into webp byte array", new Object[0]);
                if (!DefaultMimeTypes.WEBP_A.isSame(mimeType) || DefaultMimeTypes.WEBP_A.isMyHeader(byteArray)) {
                    bArr = byteArray;
                } else {
                    UnitedLog.wp("BitmapProcess", encodedImage.path, "lost alpha-channel when compress bitmap[ARGB8888 WebP], API-LEVEL=%d", Integer.valueOf(Build.VERSION.SDK_INT));
                }
            }
        }
        Object[] objArr = new Object[2];
        objArr[0] = Boolean.valueOf(bArr != null);
        objArr[1] = mimeType;
        UnitedLog.d("BitmapProcess", imageRequest, "compress image with bitmap, result=%B, format=%s", objArr);
        return bArr;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0067  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void resultImage(com.taobao.rxm.consume.Consumer<com.taobao.phenix.entity.DecodedImage, com.taobao.phenix.request.ImageRequest> r12, com.taobao.phenix.entity.DecodedImage r13, boolean r14) {
        /*
            r11 = this;
            java.lang.Object r0 = r12.getContext()
            com.taobao.phenix.request.ImageRequest r0 = (com.taobao.phenix.request.ImageRequest) r0
            r11.onConsumeStart(r12, r14)
            r1 = 0
            r2 = 1
            if (r14 == 0) goto L_0x0064
            boolean r3 = r13.isStaticBitmap()
            if (r3 == 0) goto L_0x0064
            android.graphics.Bitmap r3 = r13.getBitmap()
            com.taobao.phenix.bitmap.BitmapProcessor[] r4 = r0.getBitmapProcessors()
            if (r4 == 0) goto L_0x0057
            int r5 = r4.length
            if (r5 <= 0) goto L_0x0057
            int r5 = r4.length
            r7 = r3
            r6 = 0
        L_0x0023:
            if (r6 >= r5) goto L_0x0046
            r8 = r4[r6]
            java.lang.String r9 = r0.getPath()
            com.taobao.phenix.bitmap.BitmapSupplier r10 = com.taobao.phenix.bitmap.BitmapSupplier.getInstance()
            android.graphics.Bitmap r7 = r8.process(r9, r10, r7)
            if (r7 != 0) goto L_0x0043
            r13.release()
            java.lang.Throwable r13 = new java.lang.Throwable
            java.lang.String r14 = "processed result bitmap cannot be null!"
            r13.<init>(r14)
            r12.onFailure(r13)
            return
        L_0x0043:
            int r6 = r6 + 1
            goto L_0x0023
        L_0x0046:
            java.lang.String r5 = "BitmapProcess"
            java.lang.String r6 = "bitmap processors call, length=%d"
            java.lang.Object[] r8 = new java.lang.Object[r2]
            int r4 = r4.length
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r8[r1] = r4
            com.taobao.phenix.common.UnitedLog.d(r5, r0, r6, r8)
            goto L_0x0058
        L_0x0057:
            r7 = r3
        L_0x0058:
            if (r3 == r7) goto L_0x0064
            com.taobao.phenix.entity.DecodedImage r0 = new com.taobao.phenix.entity.DecodedImage
            com.taobao.phenix.entity.EncodedImage r3 = r13.getEncodedImage()
            r0.<init>(r3, r7)
            goto L_0x0065
        L_0x0064:
            r0 = r13
        L_0x0065:
            if (r0 == r13) goto L_0x0068
            r1 = 1
        L_0x0068:
            r11.onConsumeFinish(r12, r1, r14)
            r12.onNewResult(r0, r14)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.bitmap.BitmapProcessProducer.resultImage(com.taobao.rxm.consume.Consumer, com.taobao.phenix.entity.DecodedImage, boolean):void");
    }

    public void consumeNewResult(Consumer<DecodedImage, ImageRequest> consumer, boolean z, DecodedImage decodedImage) {
        EncodedImage encodedImage = decodedImage.getEncodedImage();
        if (!decodedImage.isStaticBitmap() || encodedImage.sizeLevel != 4) {
            resultImage(consumer, decodedImage, z);
            return;
        }
        ImageRequest context = consumer.getContext();
        if (z) {
            onConductStart(consumer);
        }
        UnitedLog.e("Phenix", "BitMapProcessor Started.", context);
        Bitmap bitmap = decodedImage.getBitmap();
        Bitmap scaleLargeBitmap = scaleLargeBitmap(bitmap, encodedImage);
        boolean z2 = false;
        if (scaleLargeBitmap != null) {
            UnitedLog.d("BitmapProcess", context, "scale bitmap, new size=%d, old size=%d", Integer.valueOf(SizeUtil.getBitmapSize(scaleLargeBitmap)), Integer.valueOf(SizeUtil.getBitmapSize(bitmap)));
            if (scaleLargeBitmap != bitmap) {
                bitmap.recycle();
            }
            byte[] compressScaledBitmap = compressScaledBitmap(context, scaleLargeBitmap, encodedImage);
            if (compressScaledBitmap != null && compressScaledBitmap.length > 0) {
                encodedImage.release();
                encodedImage = encodedImage.cloneExcept(new EncodedData(compressScaledBitmap, 0, compressScaledBitmap.length), 1, true);
            }
            decodedImage = new DecodedImage(encodedImage, scaleLargeBitmap);
        }
        if (z) {
            if (scaleLargeBitmap != null) {
                z2 = true;
            }
            onConductFinish(consumer, z2);
        }
        UnitedLog.e("Phenix", "BitMapProcessor Finished.", context);
        resultImage(consumer, decodedImage, z);
    }
}
