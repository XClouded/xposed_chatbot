package com.taobao.pexode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import com.taobao.pexode.common.DegradeEventListener;
import com.taobao.pexode.common.NdkCore;
import com.taobao.pexode.common.SoInstallMgrSdk;
import com.taobao.pexode.decoder.Decoder;
import com.taobao.pexode.decoder.GifDecoder;
import com.taobao.pexode.decoder.SystemDecoder;
import com.taobao.pexode.decoder.WebPDecoder;
import com.taobao.pexode.entity.RewindableByteArrayStream;
import com.taobao.pexode.entity.RewindableFileInputStream;
import com.taobao.pexode.entity.RewindableInputStream;
import com.taobao.pexode.entity.RewindableStream;
import com.taobao.pexode.exception.DegradeNotAllowedException;
import com.taobao.pexode.exception.IncrementalDecodeException;
import com.taobao.pexode.exception.NotSupportedException;
import com.taobao.pexode.exception.PexodeException;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.tcommon.core.BytesPool;
import com.taobao.tcommon.log.FLog;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Pexode {
    public static final int APPEND_DECODE_CHUNK_SIZE = 2048;
    public static final int MB = 1048576;
    public static final int MINIMUM_HEADER_BUFFER_SIZE = 64;
    public static final String TAG = "Pexode";
    public static boolean useAndroidPDecode = false;
    private boolean forcedDegrade2System;
    private final List<Decoder> installedDecoders;
    private ForcedDegradationListener mForcedDegradationListener;
    private Context preparedContext;
    private final Decoder systemDecoder;

    public interface ForcedDegradationListener {
        void onForcedDegrade2NoAshmem();

        void onForcedDegrade2NoInBitmap();

        void onForcedDegrade2System();
    }

    private static class Singleton {
        /* access modifiers changed from: private */
        public static final Pexode INSTANCE = new Pexode();

        private Singleton() {
        }
    }

    private Pexode() {
        this.systemDecoder = new SystemDecoder();
        this.installedDecoders = new CopyOnWriteArrayList();
        this.installedDecoders.add(new WebPDecoder());
        this.installedDecoders.add(new GifDecoder());
        this.installedDecoders.add(this.systemDecoder);
    }

    public static void prepare(Context context) {
        synchronized (Singleton.INSTANCE) {
            Singleton.INSTANCE.preparedContext = context;
            SoInstallMgrSdk.init(context);
            NdkCore.prepare(context);
            for (Decoder prepare : Singleton.INSTANCE.installedDecoders) {
                prepare.prepare(context);
            }
        }
    }

    public static void installDecoder(Decoder decoder) {
        synchronized (Singleton.INSTANCE) {
            if (Singleton.INSTANCE.forcedDegrade2System) {
                Singleton.INSTANCE.installedDecoders.add(1, decoder);
            } else {
                Singleton.INSTANCE.installedDecoders.add(0, decoder);
            }
            if (Singleton.INSTANCE.preparedContext != null) {
                decoder.prepare(Singleton.INSTANCE.preparedContext);
            }
        }
    }

    public static void uninstallDecoder(Decoder decoder) {
        Singleton.INSTANCE.installedDecoders.remove(decoder);
    }

    public static void setBytesPool(BytesPool bytesPool) {
        DecodeHelper.instance().setBytesPool(bytesPool);
    }

    public static void forceDegrade2System(boolean z) {
        synchronized (Singleton.INSTANCE) {
            if (z != Singleton.INSTANCE.forcedDegrade2System) {
                FLog.w(TAG, "force degrading to system decoder, result=%b", Boolean.valueOf(z));
                Singleton.INSTANCE.installedDecoders.remove(Singleton.INSTANCE.systemDecoder);
                if (z) {
                    Singleton.INSTANCE.installedDecoders.add(0, Singleton.INSTANCE.systemDecoder);
                } else {
                    Singleton.INSTANCE.installedDecoders.add(Singleton.INSTANCE.systemDecoder);
                }
                Singleton.INSTANCE.forcedDegrade2System = z;
            }
        }
    }

    public static void forceDegrade2NoAshmem(boolean z) {
        DecodeHelper.instance().forcedDegrade2NoAshmem = z;
        FLog.w(TAG, "force degrading to no ashmem, result=%b", Boolean.valueOf(z));
    }

    public static void forceDegrade2NoInBitmap(boolean z) {
        DecodeHelper.instance().forcedDegrade2NoInBitmap = z;
        FLog.w(TAG, "force degrading to no inBitmap, result=%b", Boolean.valueOf(z));
    }

    static ForcedDegradationListener getForcedDegradationListener() {
        return Singleton.INSTANCE.mForcedDegradationListener;
    }

    public static void setForcedDegradationListener(ForcedDegradationListener forcedDegradationListener) {
        Singleton.INSTANCE.mForcedDegradationListener = forcedDegradationListener;
    }

    public static void enableCancellability(boolean z) {
        PexodeOptions.sEnabledCancellability = z;
    }

    static boolean isForcedDegrade2System() {
        return Singleton.INSTANCE.forcedDegrade2System;
    }

    public static List<Decoder> getAllSupportDecoders(MimeType mimeType) {
        ArrayList arrayList = new ArrayList();
        for (Decoder next : Singleton.INSTANCE.installedDecoders) {
            if (next.isSupported(mimeType)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    private static Decoder resolveDecoderWithHeader(RewindableStream rewindableStream, PexodeOptions pexodeOptions, int i) throws IOException {
        int i2;
        pexodeOptions.tempHeaderBuffer = DecodeHelper.instance().offerBytes(i);
        try {
            i2 = rewindableStream.read(pexodeOptions.tempHeaderBuffer, 0, i);
        } catch (IOException unused) {
            i2 = 0;
        }
        rewindableStream.rewind();
        if (i2 > 0) {
            for (Decoder next : Singleton.INSTANCE.installedDecoders) {
                MimeType detectMimeType = next.detectMimeType(pexodeOptions.tempHeaderBuffer);
                pexodeOptions.outMimeType = detectMimeType;
                if (detectMimeType != null) {
                    return next;
                }
            }
        }
        return Singleton.INSTANCE.systemDecoder;
    }

    private static Decoder resolveDecoderWithMimeType(MimeType mimeType) {
        if (mimeType != null) {
            for (Decoder next : Singleton.INSTANCE.installedDecoders) {
                if (next.isSupported(mimeType)) {
                    return next;
                }
            }
        }
        return Singleton.INSTANCE.systemDecoder;
    }

    public static boolean isAshmemSupported() {
        return NdkCore.isSoInstalled() && Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT <= 19;
    }

    public static boolean isInBitmapSupported() {
        return Build.VERSION.SDK_INT >= 21;
    }

    private static void checkOptions(PexodeOptions pexodeOptions) {
        if (pexodeOptions.enableAshmem && !isAshmemSupported()) {
            FLog.w(TAG, "cannot use ashmem in the runtime, disabled ashmem already!", new Object[0]);
            pexodeOptions.enableAshmem = false;
        }
        if (pexodeOptions.inBitmap != null && !isInBitmapSupported()) {
            FLog.w(TAG, "cannot reuse bitmap in the runtime, disabled inBitmap already!", new Object[0]);
            pexodeOptions.inBitmap = null;
        }
    }

    public static int preferInputType(RewindableStream rewindableStream, MimeType mimeType, boolean z) {
        int inputType = rewindableStream.getInputType();
        if (inputType == 1) {
            return inputType;
        }
        Decoder resolveDecoderWithMimeType = resolveDecoderWithMimeType(mimeType);
        if (resolveDecoderWithMimeType.acceptInputType(inputType, mimeType, z)) {
            return inputType;
        }
        if (inputType != 2 || !resolveDecoderWithMimeType.acceptInputType(3, mimeType, z)) {
            return 1;
        }
        return 3;
    }

    public static boolean canSystemSupport(MimeType mimeType) {
        return Singleton.INSTANCE.systemDecoder.isSupported(mimeType);
    }

    public static boolean canSupport(MimeType mimeType) {
        if (mimeType == null) {
            return false;
        }
        for (Decoder isSupported : Singleton.INSTANCE.installedDecoders) {
            if (isSupported.isSupported(mimeType)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0016 A[SYNTHETIC, Splitter:B:14:0x0016] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x001d A[SYNTHETIC, Splitter:B:21:0x001d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.taobao.pexode.PexodeResult decode(@androidx.annotation.NonNull java.io.File r2, @androidx.annotation.NonNull com.taobao.pexode.PexodeOptions r3) throws com.taobao.pexode.exception.PexodeException {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ IOException -> 0x001a, all -> 0x0013 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x001a, all -> 0x0013 }
            com.taobao.pexode.PexodeResult r2 = decode((java.io.InputStream) r1, (com.taobao.pexode.PexodeOptions) r3)     // Catch:{ IOException -> 0x0011, all -> 0x000e }
            r1.close()     // Catch:{ IOException -> 0x000d }
        L_0x000d:
            return r2
        L_0x000e:
            r2 = move-exception
            r0 = r1
            goto L_0x0014
        L_0x0011:
            goto L_0x001b
        L_0x0013:
            r2 = move-exception
        L_0x0014:
            if (r0 == 0) goto L_0x0019
            r0.close()     // Catch:{ IOException -> 0x0019 }
        L_0x0019:
            throw r2
        L_0x001a:
            r1 = r0
        L_0x001b:
            if (r1 == 0) goto L_0x0020
            r1.close()     // Catch:{ IOException -> 0x0020 }
        L_0x0020:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.pexode.Pexode.decode(java.io.File, com.taobao.pexode.PexodeOptions):com.taobao.pexode.PexodeResult");
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0016 A[SYNTHETIC, Splitter:B:14:0x0016] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x001d A[SYNTHETIC, Splitter:B:21:0x001d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.taobao.pexode.PexodeResult decode(@androidx.annotation.NonNull java.lang.String r2, @androidx.annotation.NonNull com.taobao.pexode.PexodeOptions r3) throws com.taobao.pexode.exception.PexodeException {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ IOException -> 0x001a, all -> 0x0013 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x001a, all -> 0x0013 }
            com.taobao.pexode.PexodeResult r2 = decode((java.io.InputStream) r1, (com.taobao.pexode.PexodeOptions) r3)     // Catch:{ IOException -> 0x0011, all -> 0x000e }
            r1.close()     // Catch:{ IOException -> 0x000d }
        L_0x000d:
            return r2
        L_0x000e:
            r2 = move-exception
            r0 = r1
            goto L_0x0014
        L_0x0011:
            goto L_0x001b
        L_0x0013:
            r2 = move-exception
        L_0x0014:
            if (r0 == 0) goto L_0x0019
            r0.close()     // Catch:{ IOException -> 0x0019 }
        L_0x0019:
            throw r2
        L_0x001a:
            r1 = r0
        L_0x001b:
            if (r1 == 0) goto L_0x0020
            r1.close()     // Catch:{ IOException -> 0x0020 }
        L_0x0020:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.pexode.Pexode.decode(java.lang.String, com.taobao.pexode.PexodeOptions):com.taobao.pexode.PexodeResult");
    }

    public static PexodeResult decode(@NonNull FileDescriptor fileDescriptor, @NonNull PexodeOptions pexodeOptions) throws IOException, PexodeException {
        return doDecode(new RewindableFileInputStream(new FileInputStream(fileDescriptor), 1048576), pexodeOptions, DecodeHelper.instance());
    }

    public static PexodeResult decode(@NonNull byte[] bArr, int i, int i2, @NonNull PexodeOptions pexodeOptions) throws IOException, PexodeException {
        return doDecode(new RewindableByteArrayStream(bArr, i, i2), pexodeOptions, DecodeHelper.instance());
    }

    public static PexodeResult decode(@NonNull InputStream inputStream, @NonNull PexodeOptions pexodeOptions) throws IOException, PexodeException {
        RewindableStream rewindableStream;
        if (inputStream instanceof RewindableStream) {
            rewindableStream = (RewindableStream) inputStream;
        } else if (inputStream instanceof FileInputStream) {
            rewindableStream = new RewindableFileInputStream((FileInputStream) inputStream, 1048576);
        } else {
            rewindableStream = new RewindableInputStream(inputStream, 1048576);
        }
        return doDecode(rewindableStream, pexodeOptions, DecodeHelper.instance());
    }

    public static Bitmap getImageToChange(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            for (int i = 0; i < height; i++) {
                String str = "";
                for (int i2 = 0; i2 < width; i2++) {
                    int pixel = bitmap.getPixel(i2, i);
                    int green = Color.green(pixel);
                    int red = Color.red(pixel);
                    int blue = Color.blue(pixel);
                    int alpha = Color.alpha(pixel);
                    str = str + alpha + " : " + red + " : " + green + " : " + blue + "\n";
                    bitmap.setPixel(i2, i, Color.argb(alpha, red, green, blue));
                }
                Log.e("listen", str);
            }
        } else {
            Log.e("listen", "[listen] bitmap is null");
        }
        return bitmap;
    }

    private static PexodeResult doDecode(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws IOException, PexodeException {
        Decoder decoder;
        checkOptions(pexodeOptions);
        if (pexodeOptions.outMimeType == null) {
            decoder = resolveDecoderWithHeader(rewindableStream, pexodeOptions, rewindableStream.getBufferLength());
        } else {
            decoder = resolveDecoderWithMimeType(pexodeOptions.outMimeType);
        }
        MimeType mimeType = pexodeOptions.outMimeType;
        boolean z = true;
        pexodeOptions.outAlpha = mimeType != null && mimeType.hasAlpha();
        boolean z2 = pexodeOptions.enableAshmem;
        Bitmap bitmap = pexodeOptions.inBitmap;
        if (!pexodeOptions.incrementalDecode || decoder.canDecodeIncrementally(mimeType)) {
            PexodeResult decode = decoder.decode(rewindableStream, pexodeOptions, degradeEventListener);
            if (!(decode == null || decode.bitmap == null)) {
                decode.bitmap.getConfig();
            }
            Object[] objArr = new Object[8];
            objArr[0] = decoder;
            objArr[1] = Integer.valueOf(rewindableStream.getInputType());
            objArr[2] = Boolean.valueOf(pexodeOptions.justDecodeBounds);
            objArr[3] = Boolean.valueOf(pexodeOptions.isSizeAvailable());
            objArr[4] = Boolean.valueOf(pexodeOptions.enableAshmem);
            if (pexodeOptions.inBitmap == null) {
                z = false;
            }
            objArr[5] = Boolean.valueOf(z);
            objArr[6] = Boolean.valueOf(pexodeOptions.incrementalDecode);
            objArr[7] = decode;
            FLog.d(TAG, "decoder=%s, type=%d, justBounds=%b, sizeAvailable=%b, ashmem=%b, inBitmap=%b, increment=%b, result=%s", objArr);
            if (DecodeHelper.resultEnd(decode, pexodeOptions) || decoder == Singleton.INSTANCE.systemDecoder) {
                return decode;
            }
            Decoder decoder2 = Singleton.INSTANCE.systemDecoder;
            if (mimeType == null || !decoder2.isSupported(mimeType) || (pexodeOptions.incrementalDecode && !decoder2.canDecodeIncrementally(mimeType))) {
                if (pexodeOptions.incrementalDecode) {
                    throw new IncrementalDecodeException("incremental decoding not supported for type[" + mimeType + "] when degraded to system");
                }
                throw new NotSupportedException("type[" + mimeType + "] not supported when degraded to system");
            } else if (pexodeOptions.allowDegrade2System) {
                rewindableStream.rewind();
                pexodeOptions.enableAshmem = z2;
                pexodeOptions.inBitmap = bitmap;
                PexodeResult decode2 = decoder2.decode(rewindableStream, pexodeOptions, degradeEventListener);
                if (!pexodeOptions.cancelled) {
                    degradeEventListener.onDegraded2System(DecodeHelper.resultOK(decode2, pexodeOptions));
                }
                return decode2;
            } else {
                throw new DegradeNotAllowedException("unfortunately, system supported type[" + mimeType + "] but not allow degrading to system");
            }
        } else {
            throw new IncrementalDecodeException("incremental decoding not supported for type[" + mimeType + "] in " + decoder);
        }
    }
}
