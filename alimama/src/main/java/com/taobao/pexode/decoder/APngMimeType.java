package com.taobao.pexode.decoder;

import com.taobao.pexode.mimetype.MimeType;
import com.taobao.pexode.mimetype.MimeTypeCheckUtil;

public class APngMimeType {
    public static final MimeType APNG = new MimeType("PNG", "apng", true, new String[]{"png"}, (MimeType.MimeTypeChecker) new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 41;
        }

        public boolean isMyHeader(byte[] bArr) {
            if (bArr == null || bArr.length < 41 || !MimeTypeCheckUtil.matchBytePattern(bArr, 0, MimeTypeCheckUtil.PNG_HEADER) || !MimeTypeCheckUtil.containActlChunk(bArr)) {
                return false;
            }
            return true;
        }
    });
}
