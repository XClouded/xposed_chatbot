package com.taobao.pexode.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import com.taobao.pexode.DecodeHelper;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.PexodeOptions;
import com.taobao.pexode.PexodeResult;
import com.taobao.pexode.animate.AnimatedImage;
import com.taobao.pexode.common.AshmemBitmapFactory;
import com.taobao.pexode.common.DegradeEventListener;
import com.taobao.pexode.common.SoInstallMgrSdk;
import com.taobao.pexode.entity.RewindableStream;
import com.taobao.pexode.exception.NotSupportedException;
import com.taobao.pexode.exception.PexodeException;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.tcommon.log.FLog;
import java.io.IOException;

public class GifDecoder implements Decoder {
    private static final int LIBRARY_JNI_VERSION = 2;
    private static boolean sIsSoInstalled;

    private static String getLibraryName() {
        return "pexgif";
    }

    public boolean acceptInputType(int i, MimeType mimeType, boolean z) {
        return i != 3;
    }

    public boolean canDecodeIncrementally(MimeType mimeType) {
        return false;
    }

    static {
        String libraryName = getLibraryName();
        try {
            System.loadLibrary(libraryName);
            sIsSoInstalled = GifImage.nativeLoadedVersionTest() == 2;
            FLog.i(Pexode.TAG, "system load lib%s.so result=%b", libraryName, Boolean.valueOf(sIsSoInstalled));
        } catch (UnsatisfiedLinkError e) {
            FLog.e(Pexode.TAG, "system load lib%s.so error=%s", libraryName, e);
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
        return sIsSoInstalled && mimeType != null && mimeType.isSame(DefaultMimeTypes.GIF);
    }

    public MimeType detectMimeType(byte[] bArr) {
        if (!sIsSoInstalled || !DefaultMimeTypes.GIF.isMyHeader(bArr)) {
            return null;
        }
        return DefaultMimeTypes.GIF;
    }

    public PexodeResult decode(RewindableStream rewindableStream, PexodeOptions pexodeOptions, DegradeEventListener degradeEventListener) throws PexodeException, IOException {
        GifImage gifImage;
        Bitmap bitmap = null;
        if (pexodeOptions.justDecodeBounds) {
            pexodeOptions.outHeight = 1;
            pexodeOptions.outWidth = 1;
            return null;
        }
        switch (rewindableStream.getInputType()) {
            case 1:
                gifImage = GifImage.create(rewindableStream.getBuffer(), rewindableStream.getBufferOffset(), rewindableStream.getBufferLength());
                break;
            case 2:
                gifImage = GifImage.create(rewindableStream.getFD());
                break;
            default:
                throw new NotSupportedException("Not support input type(" + rewindableStream.getInputType() + ") when GifImage creating!");
        }
        if (!pexodeOptions.forceStaticIfAnimation || gifImage == null) {
            return PexodeResult.wrap((AnimatedImage) gifImage);
        }
        boolean z = false;
        GifFrame frame = gifImage.getFrame(0);
        if (frame == null) {
            gifImage.dispose();
            return null;
        }
        int width = frame.getWidth();
        int height = frame.getHeight();
        if (pexodeOptions.enableAshmem && !DecodeHelper.instance().forcedDegrade2NoAshmem) {
            z = true;
        }
        if (z) {
            bitmap = AshmemBitmapFactory.instance().newBitmapWithPin(width, height, Bitmap.Config.ARGB_8888);
        }
        if (!z || (bitmap == null && pexodeOptions.allowDegrade2NoAshmem)) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        if (bitmap != null) {
            frame.renderFrame(width, height, bitmap);
        }
        frame.dispose();
        gifImage.dispose();
        return PexodeResult.wrap(bitmap);
    }

    public String toString() {
        return "GifDecoder@" + Integer.toHexString(hashCode());
    }
}
