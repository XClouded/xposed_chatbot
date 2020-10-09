package com.taobao.pexode.decoder;

import android.content.Context;
import com.taobao.pexode.DecodeHelper;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.PexodeOptions;
import com.taobao.pexode.PexodeResult;
import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.pexode.common.DegradeEventListener;
import com.taobao.pexode.common.SoInstallMgrSdk;
import com.taobao.pexode.entity.RewindableStream;
import com.taobao.pexode.exception.PexodeException;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.tcommon.log.FLog;
import java.io.IOException;
import java.util.List;

public class APngDecoder implements Decoder {
    private static final int LIBRARY_JNI_VERSION = 1;
    private static boolean sIsSoInstalled;

    private static String getLibraryName() {
        return "pexapng";
    }

    public boolean acceptInputType(int i, MimeType mimeType, boolean z) {
        return true;
    }

    public boolean canDecodeIncrementally(MimeType mimeType) {
        return false;
    }

    static {
        DefaultMimeTypes.ALL_EXTENSION_TYPES.add(APngMimeType.APNG);
        String libraryName = getLibraryName();
        try {
            System.loadLibrary(libraryName);
            sIsSoInstalled = APngImage.nativeLoadedVersionTest() == 1;
            FLog.i(Pexode.TAG, "system load lib%s.so result=%b", libraryName, Boolean.valueOf(sIsSoInstalled));
        } catch (UnsatisfiedLinkError e) {
            FLog.e(Pexode.TAG, "system load lib%s.so error=%s", libraryName, e);
        }
    }

    public void prepare(Context context) {
        if (!sIsSoInstalled) {
            String libraryName = getLibraryName();
            sIsSoInstalled = SoInstallMgrSdk.loadBackup(libraryName, 1) && APngImage.nativeLoadedVersionTest() == 1;
            FLog.i(Pexode.TAG, "retry load lib%s.so result=%b", libraryName, Boolean.valueOf(sIsSoInstalled));
        }
    }

    public boolean isSupported(MimeType mimeType) {
        return sIsSoInstalled && APngMimeType.APNG.isSame(mimeType);
    }

    public MimeType detectMimeType(byte[] bArr) {
        if (!sIsSoInstalled || !APngMimeType.APNG.isMyHeader(bArr)) {
            return null;
        }
        return APngMimeType.APNG;
    }

    public PexodeResult decode(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws PexodeException, IOException {
        Decoder decoder;
        if (pexodeOptions.justDecodeBounds) {
            pexodeOptions.outHeight = 1;
            pexodeOptions.outWidth = 1;
            return null;
        } else if (pexodeOptions.forceStaticIfAnimation) {
            List<Decoder> allSupportDecoders = Pexode.getAllSupportDecoders(DefaultMimeTypes.PNG);
            if (allSupportDecoders == null || allSupportDecoders.size() <= 0 || (decoder = allSupportDecoders.get(0)) == null) {
                return null;
            }
            return decoder.decode(rewindableStream, pexodeOptions, degradeEventListener);
        } else {
            switch (rewindableStream.getInputType()) {
                case 1:
                    return PexodeResult.wrap((AnimatedImage) APngImage.nativeCreateFromBytes(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength()));
                case 2:
                    return PexodeResult.wrap((AnimatedImage) APngImage.nativeCreateFromFd(rewindableStream.getFD()));
                default:
                    byte[] offerBytes = DecodeHelper.instance().offerBytes(2048);
                    PexodeResult wrap = PexodeResult.wrap((AnimatedImage) APngImage.nativeCreateFromRewindableStream(rewindableStream, offerBytes));
                    DecodeHelper.instance().releaseBytes(offerBytes);
                    return wrap;
            }
        }
    }

    public String toString() {
        return "APngDecoder@" + Integer.toHexString(hashCode());
    }
}
