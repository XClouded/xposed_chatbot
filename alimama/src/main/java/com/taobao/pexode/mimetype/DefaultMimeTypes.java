package com.taobao.pexode.mimetype;

import com.taobao.pexode.mimetype.MimeType;
import java.util.ArrayList;
import java.util.List;

public class DefaultMimeTypes {
    public static final List<MimeType> ALL_EXTENSION_TYPES = new ArrayList();
    public static final MimeType BMP = new MimeType("BMP", "BMP", new String[]{"bmp"}, new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 2;
        }

        public boolean isMyHeader(byte[] bArr) {
            return MimeTypeCheckUtil.isBmpHeader(bArr);
        }
    });
    public static final MimeType GIF = new MimeType("GIF", "GIF", true, new String[]{"gif"}, (MimeType.MimeTypeChecker) new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 6;
        }

        public boolean isMyHeader(byte[] bArr) {
            return MimeTypeCheckUtil.isGifHeader(bArr);
        }
    });
    public static final MimeType HEIF = new MimeType("HEIF", "HEIF", new String[]{"heic"}, new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 4;
        }

        public boolean isMyHeader(byte[] bArr) {
            return MimeTypeCheckUtil.isHeifHeader(bArr);
        }
    });
    public static final MimeType JPEG = new MimeType("JPEG", "JPEG", new String[]{"jpg", "jpeg"}, new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 2;
        }

        public boolean isMyHeader(byte[] bArr) {
            return MimeTypeCheckUtil.isJpegHeader(bArr);
        }
    });
    public static final MimeType PNG = new MimeType("PNG", "PNG", new String[]{"png"}, new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 41;
        }

        public boolean isMyHeader(byte[] bArr) {
            return MimeTypeCheckUtil.isPngHeader(bArr);
        }
    });
    public static final MimeType PNG_A = new MimeType("PNG", "PNG_A", new String[]{"png"}, true, (MimeType.MimeTypeChecker) new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 41;
        }

        public boolean isMyHeader(byte[] bArr) {
            return MimeTypeCheckUtil.isPngAHeader(bArr);
        }
    });
    public static final MimeType WEBP = new MimeType("WEBP", "WEBP", new String[]{"webp"}, new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 21;
        }

        public boolean isMyHeader(byte[] bArr) {
            return MimeTypeCheckUtil.isWebPHeader(bArr);
        }
    });
    public static final MimeType WEBP_A = new MimeType("WEBP", "WEBP_A", new String[]{"webp"}, true, (MimeType.MimeTypeChecker) new MimeType.MimeTypeChecker() {
        public int requestMinHeaderSize() {
            return 21;
        }

        public boolean isMyHeader(byte[] bArr) {
            return MimeTypeCheckUtil.isWebPAHeader(bArr);
        }
    });

    static {
        ALL_EXTENSION_TYPES.add(JPEG);
        ALL_EXTENSION_TYPES.add(WEBP);
        ALL_EXTENSION_TYPES.add(PNG);
        ALL_EXTENSION_TYPES.add(GIF);
        ALL_EXTENSION_TYPES.add(BMP);
    }
}
