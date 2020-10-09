package com.taobao.pexode.decoder;

import com.taobao.pexode.mimetype.MimeType;
import com.taobao.pexode.mimetype.MimeTypeCheckUtil;

public class HeifMimeType {
    public static final MimeType HEIF = new MimeType("HEIF", "HEIF", new String[]{"heic"}, new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 4;
        }

        public boolean isMyHeader(byte[] bArr) {
            return bArr != null && bArr.length >= 4 && MimeTypeCheckUtil.matchBytePattern(bArr, 20, HeifMimeType.HEIF_HEADER);
        }
    });
    /* access modifiers changed from: private */
    public static final byte[] HEIF_HEADER = MimeTypeCheckUtil.asciiBytes("heic");
    private static final int HEIF_HEADER_LENGTH = 4;
}
