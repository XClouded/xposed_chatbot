package com.taobao.pexode.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.taobao.pexode.DecodeHelper;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.PexodeOptions;
import com.taobao.pexode.PexodeResult;
import com.taobao.pexode.common.DegradeEventListener;
import com.taobao.pexode.common.SoInstallMgrSdk;
import com.taobao.pexode.entity.IncrementalStaging;
import com.taobao.pexode.entity.RewindableStream;
import com.taobao.pexode.exception.IncrementalDecodeException;
import com.taobao.pexode.exception.PexodeException;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.tcommon.log.FLog;
import java.io.FileDescriptor;
import java.io.IOException;

public class WebPDecoder extends FilledBitmapDecoder {
    private static final int LIBRARY_JNI_VERSION = 2;
    private static final int NATIVE_RET_DECODE_OK = 0;
    private static final int NATIVE_RET_NULL_STRAIGHT = 2;
    private static final int NATIVE_RET_TRY_DEGRADING = 1;
    private static final int VP8_STATUS_OK = 0;
    private static final int VP8_STATUS_REQUEST_CANCELLED = -6;
    private static final int VP8_STATUS_SUSPENDED = 5;
    private static boolean sIsSoInstalled;
    private final IncrementalStaging.NativeDestructor CONFIG_OUT_DESTRUCTOR = new IncrementalStaging.NativeDestructor() {
        public void destruct(long j) {
            WebPDecoder.nativeDestructConfigOut(j);
        }
    };

    private static String getLibraryName() {
        return "pexwebp";
    }

    private static native int nativeDecodeBytesIncrementally(byte[] bArr, int i, int i2, PexodeOptions pexodeOptions, long j);

    private static native boolean nativeDecodeBytesWithOutAddress(byte[] bArr, int i, int i2, PexodeOptions pexodeOptions, long j);

    private static native int nativeDecodeBytesWithOutAddressIncrementally(byte[] bArr, int i, int i2, PexodeOptions pexodeOptions, long j, long[] jArr);

    private static native boolean nativeDecodeBytesWithOutBuffer(byte[] bArr, int i, int i2, PexodeOptions pexodeOptions, byte[] bArr2);

    private static native int nativeDecodeBytesWithOutBufferIncrementally(byte[] bArr, int i, int i2, PexodeOptions pexodeOptions, byte[] bArr2, long[] jArr);

    private static native int nativeDecodeFdIncrementally(FileDescriptor fileDescriptor, PexodeOptions pexodeOptions, long j);

    private static native boolean nativeDecodeFdWithOutAddress(FileDescriptor fileDescriptor, PexodeOptions pexodeOptions, long j);

    private static native int nativeDecodeFdWithOutAddressIncrementally(FileDescriptor fileDescriptor, PexodeOptions pexodeOptions, long j, long[] jArr);

    private static native boolean nativeDecodeFdWithOutBuffer(FileDescriptor fileDescriptor, PexodeOptions pexodeOptions, byte[] bArr);

    private static native int nativeDecodeFdWithOutBufferIncrementally(FileDescriptor fileDescriptor, PexodeOptions pexodeOptions, byte[] bArr, long[] jArr);

    private static native int nativeDecodeStreamIncrementally(RewindableStream rewindableStream, byte[] bArr, PexodeOptions pexodeOptions, long j);

    private static native boolean nativeDecodeStreamWithOutAddress(RewindableStream rewindableStream, byte[] bArr, PexodeOptions pexodeOptions, long j);

    private static native int nativeDecodeStreamWithOutAddressIncrementally(RewindableStream rewindableStream, byte[] bArr, PexodeOptions pexodeOptions, long j, long[] jArr);

    private static native boolean nativeDecodeStreamWithOutBuffer(RewindableStream rewindableStream, byte[] bArr, PexodeOptions pexodeOptions, byte[] bArr2);

    private static native int nativeDecodeStreamWithOutBufferIncrementally(RewindableStream rewindableStream, byte[] bArr, PexodeOptions pexodeOptions, byte[] bArr2, long[] jArr);

    /* access modifiers changed from: private */
    public static native void nativeDestructConfigOut(long j);

    private static native int nativeLoadedVersionTest();

    public boolean acceptInputType(int i, MimeType mimeType, boolean z) {
        return true;
    }

    static {
        String libraryName = getLibraryName();
        try {
            System.loadLibrary(libraryName);
            sIsSoInstalled = nativeLoadedVersionTest() == 2;
            FLog.i(Pexode.TAG, "system load lib%s.so result=%b", libraryName, Boolean.valueOf(sIsSoInstalled));
        } catch (UnsatisfiedLinkError e) {
            FLog.e(Pexode.TAG, "system load lib%s.so error=%s", libraryName, e);
        }
    }

    public void prepare(Context context) {
        if (!sIsSoInstalled) {
            String libraryName = getLibraryName();
            sIsSoInstalled = SoInstallMgrSdk.loadBackup(libraryName, 2) && nativeLoadedVersionTest() == 2;
            FLog.i(Pexode.TAG, "retry load lib%s.so result=%b", libraryName, Boolean.valueOf(sIsSoInstalled));
        }
    }

    public boolean canDecodeIncrementally(MimeType mimeType) {
        return isSupported(mimeType);
    }

    public boolean isSupported(MimeType mimeType) {
        return sIsSoInstalled && mimeType != null && DefaultMimeTypes.WEBP.getMajorName().equals(mimeType.getMajorName());
    }

    public MimeType detectMimeType(byte[] bArr) {
        if (!sIsSoInstalled) {
            return null;
        }
        if (DefaultMimeTypes.WEBP.isMyHeader(bArr)) {
            return DefaultMimeTypes.WEBP;
        }
        if (DefaultMimeTypes.WEBP_A.isMyHeader(bArr)) {
            return DefaultMimeTypes.WEBP_A;
        }
        return null;
    }

    private int decodeFirstIncrementally(RewindableStream rewindableStream, PexodeOptions pexodeOptions, Bitmap bitmap, boolean z) throws PexodeException {
        long j;
        byte[] bArr;
        int i;
        PexodeOptions pexodeOptions2 = pexodeOptions;
        Bitmap bitmap2 = bitmap;
        if (invalidBitmap(bitmap2, pexodeOptions, "decodeFirstIncrementally")) {
            return 1;
        }
        if (z) {
            j = getPixelAddressFromBitmap(bitmap);
            bArr = null;
        } else {
            bArr = getPixelBufferFromBitmap(bitmap2);
            j = 0;
        }
        if (bArr == null && j == 0) {
            return 1;
        }
        long[] jArr = new long[1];
        switch (rewindableStream.getInputType()) {
            case 1:
                if (!z) {
                    i = nativeDecodeBytesWithOutBufferIncrementally(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength(), pexodeOptions, bArr, jArr);
                    break;
                } else {
                    i = nativeDecodeBytesWithOutAddressIncrementally(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength(), pexodeOptions, j, jArr);
                    break;
                }
            case 2:
                if (!z) {
                    i = nativeDecodeFdWithOutBufferIncrementally(rewindableStream.getFD(), pexodeOptions, bArr, jArr);
                    break;
                } else {
                    i = nativeDecodeFdWithOutAddressIncrementally(rewindableStream.getFD(), pexodeOptions, j, jArr);
                    break;
                }
            default:
                byte[] offerBytes = DecodeHelper.instance().offerBytes(2048);
                if (z) {
                    i = nativeDecodeStreamWithOutAddressIncrementally(rewindableStream, offerBytes, pexodeOptions, j, jArr);
                } else {
                    RewindableStream rewindableStream2 = rewindableStream;
                    i = nativeDecodeStreamWithOutBufferIncrementally(rewindableStream, offerBytes, pexodeOptions, bArr, jArr);
                }
                DecodeHelper.instance().releaseBytes(offerBytes);
                break;
        }
        IncrementalStaging incrementalStaging = new IncrementalStaging(bitmap2, jArr[0], this.CONFIG_OUT_DESTRUCTOR);
        if (i != 5 || DecodeHelper.cancelledInOptions(pexodeOptions)) {
            incrementalStaging.release();
        }
        if (i == -6) {
            return 2;
        }
        if (i != 0 && i != 5) {
            return 1;
        }
        DecodeHelper.setIncrementalStaging(pexodeOptions, incrementalStaging);
        if (i == 5) {
            return 2;
        }
        return 0;
    }

    private int decodeLaterIncrementally(RewindableStream rewindableStream, PexodeOptions pexodeOptions, @NonNull IncrementalStaging incrementalStaging) throws PexodeException {
        int i;
        switch (rewindableStream.getInputType()) {
            case 1:
                i = nativeDecodeBytesIncrementally(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength(), pexodeOptions, incrementalStaging.getNativeConfigOut());
                break;
            case 2:
                i = nativeDecodeFdIncrementally(rewindableStream.getFD(), pexodeOptions, incrementalStaging.getNativeConfigOut());
                break;
            default:
                byte[] offerBytes = DecodeHelper.instance().offerBytes(2048);
                i = nativeDecodeStreamIncrementally(rewindableStream, offerBytes, pexodeOptions, incrementalStaging.getNativeConfigOut());
                DecodeHelper.instance().releaseBytes(offerBytes);
                break;
        }
        if (i != 5 || DecodeHelper.cancelledInOptions(pexodeOptions)) {
            incrementalStaging.release();
        }
        if (i == 5 || i == -6) {
            return 2;
        }
        if (i == 0) {
            return 0;
        }
        throw new IncrementalDecodeException("native decode bytes with buffer incrementally error, status=" + i);
    }

    private int decodeInBitmapBuffer(RewindableStream rewindableStream, PexodeOptions pexodeOptions, Bitmap bitmap) {
        byte[] pixelBufferFromBitmap;
        boolean z;
        if (invalidBitmap(bitmap, pexodeOptions, "decodeInBitmapBuffer") || (pixelBufferFromBitmap = getPixelBufferFromBitmap(bitmap)) == null) {
            return 1;
        }
        switch (rewindableStream.getInputType()) {
            case 1:
                z = nativeDecodeBytesWithOutBuffer(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength(), pexodeOptions, pixelBufferFromBitmap);
                break;
            case 2:
                z = nativeDecodeFdWithOutBuffer(rewindableStream.getFD(), pexodeOptions, pixelBufferFromBitmap);
                break;
            default:
                byte[] offerBytes = DecodeHelper.instance().offerBytes(2048);
                z = nativeDecodeStreamWithOutBuffer(rewindableStream, offerBytes, pexodeOptions, pixelBufferFromBitmap);
                DecodeHelper.instance().releaseBytes(offerBytes);
                break;
        }
        if (z) {
            return 0;
        }
        return 1;
    }

    private static int decodeInBitmapAddress(RewindableStream rewindableStream, PexodeOptions pexodeOptions, Bitmap bitmap) {
        boolean z;
        if (invalidBitmap(bitmap, pexodeOptions, "decodeInBitmapAddress")) {
            return 1;
        }
        long pixelAddressFromBitmap = getPixelAddressFromBitmap(bitmap);
        if (pixelAddressFromBitmap == 0) {
            return 1;
        }
        switch (rewindableStream.getInputType()) {
            case 1:
                z = nativeDecodeBytesWithOutAddress(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength(), pexodeOptions, pixelAddressFromBitmap);
                break;
            case 2:
                z = nativeDecodeFdWithOutAddress(rewindableStream.getFD(), pexodeOptions, pixelAddressFromBitmap);
                break;
            default:
                byte[] offerBytes = DecodeHelper.instance().offerBytes(2048);
                z = nativeDecodeStreamWithOutAddress(rewindableStream, offerBytes, pexodeOptions, pixelAddressFromBitmap);
                DecodeHelper.instance().releaseBytes(offerBytes);
                break;
        }
        if (z) {
            return 0;
        }
        return 1;
    }

    private int decodeReturnInBuffer(RewindableStream rewindableStream, PexodeOptions pexodeOptions, Bitmap bitmap, IncrementalStaging incrementalStaging, boolean z, boolean z2) throws PexodeException {
        if (!z) {
            return decodeInBitmapBuffer(rewindableStream, pexodeOptions, bitmap);
        }
        if (z2) {
            return decodeFirstIncrementally(rewindableStream, pexodeOptions, bitmap, false);
        }
        return decodeLaterIncrementally(rewindableStream, pexodeOptions, incrementalStaging);
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeNormal(RewindableStream rewindableStream, PexodeOptions pexodeOptions) throws PexodeException {
        Bitmap bitmap;
        boolean z = pexodeOptions.incrementalDecode;
        IncrementalStaging incrementalStaging = DecodeHelper.getIncrementalStaging(pexodeOptions);
        boolean z2 = incrementalStaging == null;
        if (!z || z2) {
            bitmap = newBitmap(pexodeOptions, false);
        } else {
            bitmap = null;
        }
        int decodeReturnInBuffer = decodeReturnInBuffer(rewindableStream, pexodeOptions, bitmap, incrementalStaging, z, z2);
        if (decodeReturnInBuffer == 0) {
            return z ? DecodeHelper.getIncrementalStaging(pexodeOptions).getInterBitmap() : bitmap;
        }
        if (1 != decodeReturnInBuffer || !z) {
            return null;
        }
        throw new IncrementalDecodeException("incremental decoding error at the first and cannot degrade now");
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeInBitmap(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws PexodeException, IOException {
        boolean z = pexodeOptions.incrementalDecode;
        IncrementalStaging incrementalStaging = DecodeHelper.getIncrementalStaging(pexodeOptions);
        boolean z2 = false;
        int decodeReturnInBuffer = decodeReturnInBuffer(rewindableStream, pexodeOptions, pexodeOptions.inBitmap, incrementalStaging, z, incrementalStaging == null);
        if (decodeReturnInBuffer != 0) {
            Bitmap bitmap = null;
            if (2 == decodeReturnInBuffer) {
                return null;
            }
            if (!DecodeHelper.cancelledInOptions(pexodeOptions) && pexodeOptions.allowDegrade2NoInBitmap) {
                rewindableStream.rewind();
                bitmap = decodeNormal(rewindableStream, pexodeOptions);
                if (!DecodeHelper.cancelledInOptions(pexodeOptions)) {
                    if (bitmap != null || z) {
                        z2 = true;
                    }
                    degradeEventListener.onDegraded2NoInBitmap(z2);
                }
            }
            return bitmap;
        } else if (z) {
            return DecodeHelper.getIncrementalStaging(pexodeOptions).getInterBitmap();
        } else {
            return pexodeOptions.inBitmap;
        }
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeAshmem(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws PexodeException, IOException {
        Bitmap bitmap;
        int i;
        boolean z = pexodeOptions.incrementalDecode;
        IncrementalStaging incrementalStaging = DecodeHelper.getIncrementalStaging(pexodeOptions);
        boolean z2 = false;
        boolean z3 = incrementalStaging == null;
        Bitmap bitmap2 = null;
        if (!z || z3) {
            bitmap = newBitmap(pexodeOptions, true);
        } else {
            bitmap = null;
        }
        if (!z) {
            i = decodeInBitmapAddress(rewindableStream, pexodeOptions, bitmap);
        } else if (z3) {
            i = decodeFirstIncrementally(rewindableStream, pexodeOptions, bitmap, true);
        } else {
            i = decodeLaterIncrementally(rewindableStream, pexodeOptions, incrementalStaging);
        }
        if (i == 0) {
            return z ? DecodeHelper.getIncrementalStaging(pexodeOptions).getInterBitmap() : bitmap;
        }
        if (2 == i) {
            return null;
        }
        if (!DecodeHelper.cancelledInOptions(pexodeOptions) && pexodeOptions.allowDegrade2NoAshmem) {
            rewindableStream.rewind();
            bitmap2 = decodeNormal(rewindableStream, pexodeOptions);
            if (!DecodeHelper.cancelledInOptions(pexodeOptions)) {
                if (bitmap2 != null || z) {
                    z2 = true;
                }
                degradeEventListener.onDegraded2NoAshmem(z2);
            }
        }
        return bitmap2;
    }

    public PexodeResult decode(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws PexodeException, IOException {
        Bitmap bitmap;
        if (!pexodeOptions.isSizeAvailable()) {
            switch (rewindableStream.getInputType()) {
                case 1:
                    nativeDecodeBytesWithOutBuffer(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength(), pexodeOptions, (byte[]) null);
                    break;
                case 2:
                    nativeDecodeFdWithOutBuffer(rewindableStream.getFD(), pexodeOptions, (byte[]) null);
                    break;
                default:
                    byte[] offerBytes = DecodeHelper.instance().offerBytes(64);
                    nativeDecodeStreamWithOutBuffer(rewindableStream, offerBytes, pexodeOptions, (byte[]) null);
                    DecodeHelper.instance().releaseBytes(offerBytes);
                    break;
            }
        } else if (pexodeOptions.sampleSize != DecodeHelper.getLastSampleSizeInOptions(pexodeOptions)) {
            int i = pexodeOptions.outWidth;
            pexodeOptions.outWidth = i / pexodeOptions.sampleSize;
            pexodeOptions.outHeight = (pexodeOptions.outHeight * pexodeOptions.outWidth) / i;
        }
        DecodeHelper.setLastSampleSizeInOptions(pexodeOptions, pexodeOptions.sampleSize);
        if (pexodeOptions.justDecodeBounds || DecodeHelper.cancelledInOptions(pexodeOptions)) {
            return null;
        }
        if (!pexodeOptions.isSizeAvailable()) {
            FLog.e(Pexode.TAG, "WebPDecoder size unavailable before bitmap decoding", new Object[0]);
            return null;
        }
        if (pexodeOptions.enableAshmem && !DecodeHelper.instance().forcedDegrade2NoAshmem) {
            bitmap = decodeAshmem(rewindableStream, pexodeOptions, degradeEventListener);
        } else if (pexodeOptions.inBitmap == null || DecodeHelper.instance().forcedDegrade2NoInBitmap) {
            bitmap = decodeNormal(rewindableStream, pexodeOptions);
        } else {
            bitmap = decodeInBitmap(rewindableStream, pexodeOptions, degradeEventListener);
        }
        return PexodeResult.wrap(bitmap);
    }

    public String toString() {
        return "WebPDecoder@" + Integer.toHexString(hashCode());
    }
}
