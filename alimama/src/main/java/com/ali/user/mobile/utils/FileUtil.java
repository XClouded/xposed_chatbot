package com.ali.user.mobile.utils;

import java.io.File;

public class FileUtil {
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0008, code lost:
        r0 = r2.lastIndexOf(46);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getExtensionName(java.lang.String r2) {
        /*
            if (r2 == 0) goto L_0x0020
            int r0 = r2.length()
            if (r0 <= 0) goto L_0x0020
            r0 = 46
            int r0 = r2.lastIndexOf(r0)
            r1 = -1
            if (r0 <= r1) goto L_0x0020
            int r1 = r2.length()
            int r1 = r1 + -1
            if (r0 >= r1) goto L_0x0020
            int r0 = r0 + 1
            java.lang.String r2 = r2.substring(r0)
            return r2
        L_0x0020:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.utils.FileUtil.getExtensionName(java.lang.String):java.lang.String");
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
