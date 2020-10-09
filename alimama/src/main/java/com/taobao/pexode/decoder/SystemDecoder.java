package com.taobao.pexode.decoder;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import com.taobao.pexode.DecodeHelper;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.PexodeOptions;
import com.taobao.pexode.entity.RewindableStream;
import com.taobao.pexode.exception.PexodeException;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.tcommon.log.FLog;

public class SystemDecoder implements Decoder {
    private static final boolean sIsWebPASupported;
    private static final boolean sIsWebPSupported = (Build.VERSION.SDK_INT >= 14);
    private Context mContext;

    public boolean canDecodeIncrementally(MimeType mimeType) {
        return false;
    }

    public static int max(int... iArr) {
        int i = iArr[0];
        for (int i2 = 1; i2 < iArr.length; i2++) {
            if (iArr[i2] > i) {
                i = iArr[i2];
            }
        }
        return i;
    }

    static {
        boolean z = false;
        if (Build.VERSION.SDK_INT > 17) {
            z = true;
        }
        sIsWebPASupported = z;
    }

    public void prepare(Context context) {
        this.mContext = context;
    }

    public boolean isSupported(MimeType mimeType) {
        return mimeType != null && ((sIsWebPSupported && mimeType.isSame(DefaultMimeTypes.WEBP)) || mimeType.isSame(DefaultMimeTypes.JPEG) || mimeType.isSame(DefaultMimeTypes.PNG) || mimeType.isSame(DefaultMimeTypes.PNG_A) || ((sIsWebPASupported && mimeType.isSame(DefaultMimeTypes.WEBP_A)) || mimeType.isSame(DefaultMimeTypes.BMP) || (Pexode.useAndroidPDecode && Build.VERSION.SDK_INT == 28 && mimeType.isSame(DefaultMimeTypes.HEIF))));
    }

    public boolean acceptInputType(int i, MimeType mimeType, boolean z) {
        return !(i == 2 && Build.VERSION.SDK_INT == 19) && (i != 3 || (!z && (!DefaultMimeTypes.WEBP.isSame(mimeType) || sIsWebPASupported)));
    }

    public MimeType detectMimeType(byte[] bArr) {
        if (sIsWebPSupported && DefaultMimeTypes.WEBP.isMyHeader(bArr)) {
            return DefaultMimeTypes.WEBP;
        }
        if (DefaultMimeTypes.JPEG.isMyHeader(bArr)) {
            return DefaultMimeTypes.JPEG;
        }
        if (DefaultMimeTypes.PNG.isMyHeader(bArr)) {
            return DefaultMimeTypes.PNG;
        }
        if (DefaultMimeTypes.PNG_A.isMyHeader(bArr)) {
            return DefaultMimeTypes.PNG_A;
        }
        if (sIsWebPASupported && DefaultMimeTypes.WEBP_A.isMyHeader(bArr)) {
            return DefaultMimeTypes.WEBP_A;
        }
        if (DefaultMimeTypes.BMP.isMyHeader(bArr)) {
            return DefaultMimeTypes.BMP;
        }
        if (!Pexode.useAndroidPDecode || Build.VERSION.SDK_INT != 28 || !DefaultMimeTypes.HEIF.isMyHeader(bArr)) {
            return null;
        }
        return DefaultMimeTypes.HEIF;
    }

    private static void checkInputSafety(RewindableStream rewindableStream, PexodeOptions pexodeOptions) throws PexodeException {
        if (rewindableStream.getInputType() == 2 && Build.VERSION.SDK_INT == 19) {
            if (!pexodeOptions.justDecodeBounds) {
                FLog.i(Pexode.TAG, "maybe leak when system decoding with fd, back to input stream type!", new Object[0]);
            }
            rewindableStream.back2StreamType();
        }
        if (rewindableStream.getInputType() == 3) {
            if (pexodeOptions.enableAshmem) {
                FLog.w(Pexode.TAG, "cannot use ashmem when system decoding with input stream(justBounds=%b), disabled already!", Boolean.valueOf(pexodeOptions.justDecodeBounds));
                pexodeOptions.enableAshmem = false;
            }
            if (DefaultMimeTypes.WEBP.isSame(pexodeOptions.outMimeType) && !sIsWebPASupported) {
                FLog.e(Pexode.TAG, "maybe error black image when system decoding webp with input stream(justBounds=%b)!", Boolean.valueOf(pexodeOptions.justDecodeBounds));
            }
        }
    }

    public static void setupAshmemOptions(BitmapFactory.Options options, boolean z) {
        options.inMutable = true;
        if (!options.inJustDecodeBounds) {
            options.inPurgeable = z;
            options.inInputShareable = z;
        }
    }

    private static BitmapFactory.Options newSystemOptions(PexodeOptions pexodeOptions) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = pexodeOptions.justDecodeBounds;
        if (!DecodeHelper.instance().forcedDegrade2NoInBitmap) {
            options.inBitmap = pexodeOptions.inBitmap;
        }
        if (pexodeOptions.isSizeAvailable()) {
            options.outWidth = pexodeOptions.outWidth;
            options.outHeight = pexodeOptions.outHeight;
        }
        if (pexodeOptions.outMimeType != null) {
            options.outMimeType = pexodeOptions.outMimeType.toString();
        }
        options.inSampleSize = pexodeOptions.sampleSize;
        boolean z = true;
        options.inDither = true;
        options.inPreferredConfig = PexodeOptions.CONFIG;
        if (DecodeHelper.instance().forcedDegrade2NoAshmem || !pexodeOptions.enableAshmem) {
            z = false;
        }
        setupAshmemOptions(options, z);
        DecodeHelper.setUponSysOptions(pexodeOptions, options);
        return options;
    }

    private static void updateFromSysOptions(PexodeOptions pexodeOptions, BitmapFactory.Options options) {
        pexodeOptions.outWidth = options.outWidth;
        pexodeOptions.outHeight = options.outHeight;
        DecodeHelper.setUponSysOptions(pexodeOptions, (BitmapFactory.Options) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x009c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.pexode.PexodeResult decode(com.taobao.pexode.entity.RewindableStream r12, com.taobao.pexode.PexodeOptions r13, com.taobao.pexode.common.DegradeEventListener r14) throws com.taobao.pexode.exception.PexodeException, java.io.IOException {
        /*
            r11 = this;
            checkInputSafety(r12, r13)
            android.graphics.BitmapFactory$Options r0 = newSystemOptions(r13)
            boolean r1 = r0.inPurgeable
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0013
            boolean r1 = r0.inInputShareable
            if (r1 == 0) goto L_0x0013
            r1 = 1
            goto L_0x0014
        L_0x0013:
            r1 = 0
        L_0x0014:
            android.graphics.Bitmap r4 = r0.inBitmap
            if (r4 == 0) goto L_0x001a
            r4 = 1
            goto L_0x001b
        L_0x001a:
            r4 = 0
        L_0x001b:
            r5 = 0
            int r6 = r12.getInputType()     // Catch:{ Exception -> 0x0065 }
            switch(r6) {
                case 1: goto L_0x0031;
                case 2: goto L_0x0026;
                default: goto L_0x0023;
            }     // Catch:{ Exception -> 0x0065 }
        L_0x0023:
            android.util.TypedValue r6 = r13.resourceValue     // Catch:{ Exception -> 0x0065 }
            goto L_0x0042
        L_0x0026:
            java.io.FileDescriptor r6 = r12.getFD()     // Catch:{ Exception -> 0x0065 }
            android.graphics.Rect r7 = r13.outPadding     // Catch:{ Exception -> 0x0065 }
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeFileDescriptor(r6, r7, r0)     // Catch:{ Exception -> 0x0065 }
            goto L_0x005f
        L_0x0031:
            byte[] r6 = r12.getBuffer()     // Catch:{ Exception -> 0x0065 }
            int r7 = r12.getBufferOffset()     // Catch:{ Exception -> 0x0065 }
            int r8 = r12.getBufferLength()     // Catch:{ Exception -> 0x0065 }
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeByteArray(r6, r7, r8, r0)     // Catch:{ Exception -> 0x0065 }
            goto L_0x005f
        L_0x0042:
            if (r6 == 0) goto L_0x0059
            android.content.Context r6 = r11.mContext     // Catch:{ Exception -> 0x0065 }
            if (r6 == 0) goto L_0x004f
            android.content.Context r6 = r11.mContext     // Catch:{ Exception -> 0x0065 }
            android.content.res.Resources r6 = r6.getResources()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0050
        L_0x004f:
            r6 = r5
        L_0x0050:
            android.util.TypedValue r7 = r13.resourceValue     // Catch:{ Exception -> 0x0065 }
            android.graphics.Rect r8 = r13.outPadding     // Catch:{ Exception -> 0x0065 }
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeResourceStream(r6, r7, r12, r8, r0)     // Catch:{ Exception -> 0x0065 }
            goto L_0x005f
        L_0x0059:
            android.graphics.Rect r6 = r13.outPadding     // Catch:{ Exception -> 0x0065 }
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeStream(r12, r6, r0)     // Catch:{ Exception -> 0x0065 }
        L_0x005f:
            updateFromSysOptions(r13, r0)     // Catch:{ Exception -> 0x0063 }
            goto L_0x007d
        L_0x0063:
            r0 = move-exception
            goto L_0x0067
        L_0x0065:
            r0 = move-exception
            r6 = r5
        L_0x0067:
            java.lang.String r7 = "Pexode"
            java.lang.String r8 = "SystemDecoder type=%d, error=%s"
            r9 = 2
            java.lang.Object[] r9 = new java.lang.Object[r9]
            int r10 = r12.getInputType()
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
            r9[r3] = r10
            r9[r2] = r0
            com.taobao.tcommon.log.FLog.e((java.lang.String) r7, (java.lang.String) r8, (java.lang.Object[]) r9)
        L_0x007d:
            if (r6 == 0) goto L_0x0092
            if (r1 == 0) goto L_0x0092
            com.taobao.pexode.common.NdkCore.nativePinBitmap(r6)     // Catch:{ Throwable -> 0x0085 }
            goto L_0x0092
        L_0x0085:
            r0 = move-exception
            java.lang.String r6 = "Pexode"
            java.lang.String r7 = "NdkCore nativePinBitmap error=%s"
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r3] = r0
            com.taobao.tcommon.log.FLog.e((java.lang.String) r6, (java.lang.String) r7, (java.lang.Object[]) r2)
            r6 = r5
        L_0x0092:
            com.taobao.pexode.PexodeResult r0 = com.taobao.pexode.PexodeResult.wrap((android.graphics.Bitmap) r6)
            boolean r2 = com.taobao.pexode.DecodeHelper.resultEnd(r0, r13)
            if (r2 != 0) goto L_0x00d5
            if (r1 == 0) goto L_0x00b9
            boolean r1 = r13.allowDegrade2NoAshmem
            if (r1 == 0) goto L_0x00b9
            r12.rewind()
            r13.enableAshmem = r3
            com.taobao.pexode.PexodeResult r0 = r11.decode(r12, r13, r14)
            boolean r12 = com.taobao.pexode.DecodeHelper.cancelledInOptions(r13)
            if (r12 != 0) goto L_0x00d5
            boolean r12 = com.taobao.pexode.DecodeHelper.resultOK(r0, r13)
            r14.onDegraded2NoAshmem(r12)
            goto L_0x00d5
        L_0x00b9:
            if (r4 == 0) goto L_0x00d5
            boolean r1 = r13.allowDegrade2NoInBitmap
            if (r1 == 0) goto L_0x00d5
            r12.rewind()
            r13.inBitmap = r5
            com.taobao.pexode.PexodeResult r0 = r11.decode(r12, r13, r14)
            boolean r12 = com.taobao.pexode.DecodeHelper.cancelledInOptions(r13)
            if (r12 != 0) goto L_0x00d5
            boolean r12 = com.taobao.pexode.DecodeHelper.resultOK(r0, r13)
            r14.onDegraded2NoInBitmap(r12)
        L_0x00d5:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.pexode.decoder.SystemDecoder.decode(com.taobao.pexode.entity.RewindableStream, com.taobao.pexode.PexodeOptions, com.taobao.pexode.common.DegradeEventListener):com.taobao.pexode.PexodeResult");
    }

    public String toString() {
        return "SystemDecoder@" + Integer.toHexString(hashCode());
    }
}
