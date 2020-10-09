package com.taobao.phenix.entity;

import android.text.TextUtils;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.pexode.mimetype.MimeType;

public class EncodedImage extends EncodedData {
    public static final int EXACT_SIZE_LEVEL = 1;
    public static final int LARGE_SIZE_LEVEL = 4;
    public static final int SMALL_SIZE_LEVEL = 2;
    public final String extension;
    public final boolean fromDisk;
    public final boolean fromScale;
    public boolean isSecondary;
    private boolean mForcedNoCache;
    private MimeType mMimeType;
    public final String path;
    public final int sizeLevel;
    public int targetHeight;
    public int targetWidth;

    public EncodedImage(EncodedData encodedData, String str, int i, boolean z, String str2) {
        this(encodedData, str, i, z, str2, false);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public EncodedImage(EncodedData encodedData, String str, int i, boolean z, String str2, boolean z2) {
        super(encodedData == null ? new EncodedData(false, (byte[]) null, 0, 0) : encodedData);
        this.path = str;
        this.sizeLevel = i;
        this.fromDisk = z;
        this.extension = str2;
        this.fromScale = z2;
    }

    public EncodedImage cloneExcept(EncodedData encodedData, int i) {
        return cloneExcept(encodedData, i, this.fromScale);
    }

    public EncodedImage cloneExcept(EncodedData encodedData, int i, boolean z) {
        EncodedImage encodedImage = new EncodedImage(encodedData, this.path, i, this.fromDisk, this.extension, z);
        encodedImage.targetWidth = this.targetWidth;
        encodedImage.targetHeight = this.targetHeight;
        encodedImage.isSecondary = this.isSecondary;
        return encodedImage;
    }

    public boolean notNeedCache() {
        if (this.mForcedNoCache || this.type != 1) {
            return true;
        }
        return (this.fromDisk && !this.fromScale) || !this.completed || this.bytes == null;
    }

    public EncodedImage forceNoCache(boolean z) {
        this.mForcedNoCache = z;
        return this;
    }

    public static MimeType getMimeTypeByExtension(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.indexOf(46) == 0) {
            str = str.substring(1);
        }
        for (MimeType next : DefaultMimeTypes.ALL_EXTENSION_TYPES) {
            if (next != null && next.isMyExtension(str)) {
                return next;
            }
        }
        return null;
    }

    public MimeType getMimeType() {
        if (this.mMimeType == null) {
            this.mMimeType = getMimeTypeByExtension(this.extension);
        }
        return this.mMimeType;
    }

    public void setMimeType(MimeType mimeType) {
        this.mMimeType = mimeType;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        try {
            release(false);
            super.finalize();
        } catch (Throwable unused) {
        }
    }
}
