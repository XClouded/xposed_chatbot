package com.taobao.pexode.mimetype;

public class MimeType {
    private final String[] mFileExtensions;
    private final boolean mHasAlpha;
    private final boolean mIsAnimation;
    private final String mMajorName;
    private final MimeTypeChecker mMimeTypeChecker;
    private final String mMinorName;

    public interface MimeTypeChecker {
        boolean isMyHeader(byte[] bArr);

        int requestMinHeaderSize();
    }

    public MimeType(String str, String str2, String[] strArr, MimeTypeChecker mimeTypeChecker) {
        this(str, str2, strArr, false, false, mimeTypeChecker);
    }

    public MimeType(String str, String str2, String[] strArr, boolean z, MimeTypeChecker mimeTypeChecker) {
        this(str, str2, strArr, z, false, mimeTypeChecker);
    }

    public MimeType(String str, String str2, boolean z, String[] strArr, MimeTypeChecker mimeTypeChecker) {
        this(str, str2, strArr, false, z, mimeTypeChecker);
    }

    private MimeType(String str, String str2, String[] strArr, boolean z, boolean z2, MimeTypeChecker mimeTypeChecker) {
        this.mMajorName = str;
        this.mMinorName = str2;
        this.mFileExtensions = strArr;
        this.mHasAlpha = z;
        this.mIsAnimation = z2;
        this.mMimeTypeChecker = mimeTypeChecker;
    }

    public String getMajorName() {
        return this.mMajorName;
    }

    public String getMinorName() {
        return this.mMinorName;
    }

    public boolean hasAlpha() {
        return this.mHasAlpha;
    }

    public int requestMinHeaderSize() {
        return this.mMimeTypeChecker.requestMinHeaderSize();
    }

    public boolean isMyHeader(byte[] bArr) {
        return this.mMimeTypeChecker.isMyHeader(bArr);
    }

    public boolean isMyExtension(String str) {
        for (String equalsIgnoreCase : this.mFileExtensions) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnimation() {
        return this.mIsAnimation;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r0 = getMinorName();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSame(com.taobao.pexode.mimetype.MimeType r2) {
        /*
            r1 = this;
            if (r2 == 0) goto L_0x0014
            java.lang.String r0 = r1.getMinorName()
            if (r0 == 0) goto L_0x0014
            java.lang.String r2 = r2.getMinorName()
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L_0x0014
            r2 = 1
            goto L_0x0015
        L_0x0014:
            r2 = 0
        L_0x0015:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.pexode.mimetype.MimeType.isSame(com.taobao.pexode.mimetype.MimeType):boolean");
    }

    public String toString() {
        return "image/" + getMinorName();
    }
}
