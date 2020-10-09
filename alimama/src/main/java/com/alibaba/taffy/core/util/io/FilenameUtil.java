package com.alibaba.taffy.core.util.io;

public class FilenameUtil {
    public static final char EXTENSION_SEPARATOR = '.';
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';

    public static String getName(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(indexOfLastSeparator(str) + 1);
    }

    public static int indexOfLastSeparator(String str) {
        if (str == null) {
            return -1;
        }
        return Math.max(str.lastIndexOf(47), str.lastIndexOf(92));
    }

    public static String getBaseName(String str) {
        return removeExtension(getName(str));
    }

    public static String getExtension(String str) {
        if (str == null) {
            return null;
        }
        int indexOfExtension = indexOfExtension(str);
        if (indexOfExtension == -1) {
            return "";
        }
        return str.substring(indexOfExtension + 1);
    }

    public static String removeExtension(String str) {
        if (str == null) {
            return null;
        }
        int indexOfExtension = indexOfExtension(str);
        if (indexOfExtension == -1) {
            return str;
        }
        return str.substring(0, indexOfExtension);
    }

    public static int indexOfExtension(String str) {
        int lastIndexOf;
        if (str != null && indexOfLastSeparator(str) <= (lastIndexOf = str.lastIndexOf(46))) {
            return lastIndexOf;
        }
        return -1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0058 A[Catch:{ Exception -> 0x00ab }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0062 A[Catch:{ Exception -> 0x00ab }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a2 A[Catch:{ Exception -> 0x00ab }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getName(java.net.URL r9) {
        /*
            r0 = 0
            java.net.URLConnection r9 = r9.openConnection()     // Catch:{ Exception -> 0x00ab }
            java.net.HttpURLConnection r9 = (java.net.HttpURLConnection) r9     // Catch:{ Exception -> 0x00ab }
            r9.connect()     // Catch:{ Exception -> 0x00ab }
            java.net.URL r1 = r9.getURL()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r2 = r9.getContentType()     // Catch:{ Exception -> 0x00ab }
            boolean r3 = com.alibaba.taffy.core.util.lang.StringUtil.isNotEmpty(r2)     // Catch:{ Exception -> 0x00ab }
            r4 = 0
            if (r3 == 0) goto L_0x0051
            java.lang.String r3 = ";"
            java.lang.String[] r2 = r2.split(r3)     // Catch:{ Exception -> 0x00ab }
            int r3 = r2.length     // Catch:{ Exception -> 0x00ab }
            r5 = 0
        L_0x0025:
            if (r5 >= r3) goto L_0x0051
            r6 = r2[r5]     // Catch:{ Exception -> 0x00ab }
            java.lang.String r6 = r6.trim()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r7 = r6.toLowerCase()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r8 = "charset="
            boolean r7 = r7.startsWith(r8)     // Catch:{ Exception -> 0x00ab }
            if (r7 == 0) goto L_0x004e
            java.lang.String r2 = "charset="
            int r2 = r2.length()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r2 = r6.substring(r2)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r2 = r2.trim()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r3 = "\""
            java.lang.String r2 = com.alibaba.taffy.core.util.lang.StringUtil.strip(r2, r3)     // Catch:{ Exception -> 0x00ab }
            goto L_0x0052
        L_0x004e:
            int r5 = r5 + 1
            goto L_0x0025
        L_0x0051:
            r2 = r0
        L_0x0052:
            boolean r3 = com.alibaba.taffy.core.util.lang.StringUtil.isBlank(r2)     // Catch:{ Exception -> 0x00ab }
            if (r3 == 0) goto L_0x005a
            java.lang.String r2 = "UTF-8"
        L_0x005a:
            java.lang.String r3 = "Content-Disposition"
            java.lang.String r9 = r9.getHeaderField(r3)     // Catch:{ Exception -> 0x00ab }
            if (r9 == 0) goto L_0x009b
            java.lang.String r3 = ";"
            java.lang.String[] r9 = com.alibaba.taffy.core.util.lang.StringUtil.split((java.lang.String) r9, (java.lang.String) r3)     // Catch:{ Exception -> 0x00ab }
            int r3 = r9.length     // Catch:{ Exception -> 0x00ab }
        L_0x0069:
            if (r4 >= r3) goto L_0x009b
            r5 = r9[r4]     // Catch:{ Exception -> 0x00ab }
            java.lang.String r5 = r5.trim()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r6 = r5.toLowerCase()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r7 = "filename="
            boolean r6 = r6.startsWith(r7)     // Catch:{ Exception -> 0x00ab }
            if (r6 == 0) goto L_0x0098
            java.lang.String r9 = "filename="
            int r9 = r9.length()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r9 = r5.substring(r9)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r9 = com.alibaba.taffy.core.util.net.URLEncodedUtil.decode(r9, r2)     // Catch:{ Exception -> 0x00ab }
            if (r9 == 0) goto L_0x009c
            java.lang.String r9 = r9.trim()     // Catch:{ Exception -> 0x00ab }
            java.lang.String r3 = "\""
            java.lang.String r9 = com.alibaba.taffy.core.util.lang.StringUtil.strip(r9, r3)     // Catch:{ Exception -> 0x00ab }
            goto L_0x009c
        L_0x0098:
            int r4 = r4 + 1
            goto L_0x0069
        L_0x009b:
            r9 = r0
        L_0x009c:
            boolean r3 = com.alibaba.taffy.core.util.lang.StringUtil.isBlank(r9)     // Catch:{ Exception -> 0x00ab }
            if (r3 == 0) goto L_0x00aa
            java.lang.String r9 = getName((java.lang.String) r1)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r9 = com.alibaba.taffy.core.util.net.URLEncodedUtil.decode(r9, r2)     // Catch:{ Exception -> 0x00ab }
        L_0x00aa:
            r0 = r9
        L_0x00ab:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.io.FilenameUtil.getName(java.net.URL):java.lang.String");
    }
}
