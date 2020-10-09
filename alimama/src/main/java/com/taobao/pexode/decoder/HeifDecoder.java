package com.taobao.pexode.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import com.taobao.pexode.DecodeHelper;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.PexodeOptions;
import com.taobao.pexode.PexodeResult;
import com.taobao.pexode.common.DegradeEventListener;
import com.taobao.pexode.common.NdkCore;
import com.taobao.pexode.common.SoInstallMgrSdk;
import com.taobao.pexode.entity.RewindableStream;
import com.taobao.pexode.exception.NotSupportedException;
import com.taobao.pexode.exception.PexodeException;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.tcommon.log.FLog;
import java.io.FileDescriptor;
import java.io.IOException;

public class HeifDecoder extends FilledBitmapDecoder {
    private static final int LIBRARY_JNI_VERSION = 2;
    private static boolean sIsSoInstalled;

    private static String getLibraryName() {
        return "pexheif";
    }

    private static native boolean nativeDecodeBytesWithOutAddress(byte[] bArr, int i, int i2, PexodeOptions pexodeOptions, long j);

    private static native boolean nativeDecodeBytesWithOutBuffer(byte[] bArr, int i, int i2, PexodeOptions pexodeOptions, byte[] bArr2);

    private static native boolean nativeDecodeFdWithOutAddress(FileDescriptor fileDescriptor, PexodeOptions pexodeOptions, long j);

    private static native boolean nativeDecodeFdWithOutBuffer(FileDescriptor fileDescriptor, PexodeOptions pexodeOptions, byte[] bArr);

    private static native int nativeLoadedVersionTest();

    public static native void nativeUseBugFix(boolean z);

    public boolean acceptInputType(int i, MimeType mimeType, boolean z) {
        return i != 3;
    }

    public boolean canDecodeIncrementally(MimeType mimeType) {
        return false;
    }

    static {
        DefaultMimeTypes.ALL_EXTENSION_TYPES.add(HeifMimeType.HEIF);
        String libraryName = getLibraryName();
        try {
            System.loadLibrary(libraryName);
            sIsSoInstalled = nativeLoadedVersionTest() == 2;
            FLog.i(Pexode.TAG, "system load lib%s.so result=%b", libraryName, Boolean.valueOf(sIsSoInstalled));
        } catch (UnsatisfiedLinkError e) {
            FLog.e(Pexode.TAG, "system load lib%s.so error=%s", libraryName, e);
        }
    }

    public static void useHeifBugFix(boolean z) {
        if (sIsSoInstalled) {
            nativeUseBugFix(z);
        }
    }

    public void prepare(Context context) {
        if (!sIsSoInstalled) {
            String libraryName = getLibraryName();
            sIsSoInstalled = SoInstallMgrSdk.loadBackup(libraryName, 2) && GifImage.nativeLoadedVersionTest() == 2;
            FLog.i(Pexode.TAG, "retry load lib%s.so result=%b", libraryName, Boolean.valueOf(sIsSoInstalled));
        }
    }

    public boolean isSupported(MimeType mimeType) {
        return sIsSoInstalled && HeifMimeType.HEIF.isSame(mimeType);
    }

    public MimeType detectMimeType(byte[] bArr) {
        if (!sIsSoInstalled || !HeifMimeType.HEIF.isMyHeader(bArr)) {
            return null;
        }
        return HeifMimeType.HEIF;
    }

    private boolean doNativeDecode(RewindableStream rewindableStream, PexodeOptions pexodeOptions, Bitmap bitmap, boolean z) {
        long j;
        if (FilledBitmapDecoder.invalidBitmap(bitmap, pexodeOptions, "HeifDecoder decodeIntoBitmap")) {
            return false;
        }
        byte[] bArr = null;
        if (z) {
            j = FilledBitmapDecoder.getPixelAddressFromBitmap(bitmap);
        } else if (Build.VERSION.SDK_INT >= 26) {
            long pixelAddressFromBitmap = FilledBitmapDecoder.getPixelAddressFromBitmap(bitmap);
            NdkCore.nativeUnpinBitmap(bitmap);
            j = pixelAddressFromBitmap;
        } else {
            bArr = getPixelBufferFromBitmap(bitmap);
            j = 0;
        }
        switch (rewindableStream.getInputType()) {
            case 1:
                if (bArr != null) {
                    return nativeDecodeBytesWithOutBuffer(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength(), pexodeOptions, bArr);
                }
                if (j != 0) {
                    return nativeDecodeBytesWithOutAddress(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength(), pexodeOptions, j);
                }
                break;
            case 2:
                if (bArr != null) {
                    return nativeDecodeFdWithOutBuffer(rewindableStream.getFD(), pexodeOptions, bArr);
                }
                if (j != 0) {
                    return nativeDecodeFdWithOutAddress(rewindableStream.getFD(), pexodeOptions, j);
                }
                break;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeNormal(RewindableStream rewindableStream, PexodeOptions pexodeOptions) {
        Bitmap newBitmap = FilledBitmapDecoder.newBitmap(pexodeOptions, false);
        if (doNativeDecode(rewindableStream, pexodeOptions, newBitmap, false)) {
            return newBitmap;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeInBitmap(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws IOException {
        boolean z = false;
        if (doNativeDecode(rewindableStream, pexodeOptions, pexodeOptions.inBitmap, false)) {
            return pexodeOptions.inBitmap;
        }
        Bitmap bitmap = null;
        if (!DecodeHelper.cancelledInOptions(pexodeOptions) && pexodeOptions.allowDegrade2NoInBitmap) {
            rewindableStream.rewind();
            bitmap = decodeNormal(rewindableStream, pexodeOptions);
            if (!DecodeHelper.cancelledInOptions(pexodeOptions)) {
                if (bitmap != null) {
                    z = true;
                }
                degradeEventListener.onDegraded2NoInBitmap(z);
            }
        }
        return bitmap;
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeAshmem(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws IOException {
        boolean z = true;
        Bitmap newBitmap = FilledBitmapDecoder.newBitmap(pexodeOptions, true);
        if (doNativeDecode(rewindableStream, pexodeOptions, newBitmap, true)) {
            return newBitmap;
        }
        Bitmap bitmap = null;
        if (!DecodeHelper.cancelledInOptions(pexodeOptions) && pexodeOptions.allowDegrade2NoAshmem) {
            rewindableStream.rewind();
            bitmap = decodeNormal(rewindableStream, pexodeOptions);
            if (!DecodeHelper.cancelledInOptions(pexodeOptions)) {
                if (bitmap == null) {
                    z = false;
                }
                degradeEventListener.onDegraded2NoAshmem(z);
            }
        }
        return bitmap;
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
                case 3:
                    throw new NotSupportedException("Not support stream type when HeifImage decoding!");
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
            FLog.e(Pexode.TAG, "HeifDecoder size unavailable before bitmap decoding", new Object[0]);
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
        return "HeifDecoder@" + Integer.toHexString(hashCode());
    }
}
