package com.taobao.login4android.session.cookies;

import android.text.TextUtils;

public class LoginCookieUtils {
    private static final char COMMA = ',';
    private static final String DOMAIN = "domain";
    private static final char EQUAL = '=';
    private static final String EXPIRES = "expires";
    private static final String HTTPS = "https";
    private static final String HTTP_ONLY = "httponly";
    private static final int HTTP_ONLY_LENGTH = HTTP_ONLY.length();
    private static final String MAX_AGE = "max-age";
    private static final int MAX_COOKIE_LENGTH = 4096;
    private static final String PATH = "path";
    private static final char PATH_DELIM = '/';
    private static final char PERIOD = '.';
    private static final char QUESTION_MARK = '?';
    private static final char QUOTATION = '\"';
    private static final String SECURE = "secure";
    private static final int SECURE_LENGTH = SECURE.length();
    private static final char SEMICOLON = ';';
    private static final String TAG = "login.LoginCookieUtils";
    private static final char WHITE_SPACE = ' ';

    /* JADX WARNING: Can't wrap try/catch for region: R(7:109|110|111|112|113|(1:115)|116) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:112:0x01d6 */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x01e0 A[Catch:{ IllegalArgumentException -> 0x015d, Throwable -> 0x01f2 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.taobao.login4android.session.cookies.LoginCookie parseCookie(java.lang.String r18) {
        /*
            r1 = r18
            int r2 = r18.length()
            r3 = 0
            r0 = 0
        L_0x0008:
            r4 = 0
            if (r0 < 0) goto L_0x0204
            if (r0 < r2) goto L_0x000f
            goto L_0x0204
        L_0x000f:
            char r5 = r1.charAt(r0)
            r6 = 32
            if (r5 != r6) goto L_0x001a
            int r0 = r0 + 1
            goto L_0x0008
        L_0x001a:
            r5 = 59
            int r7 = r1.indexOf(r5, r0)
            r8 = 61
            int r9 = r1.indexOf(r8, r0)
            com.taobao.login4android.session.cookies.LoginCookie r10 = new com.taobao.login4android.session.cookies.LoginCookie
            r10.<init>()
            r11 = 34
            r12 = -1
            r13 = 1
            if (r7 == r12) goto L_0x0033
            if (r7 < r9) goto L_0x0035
        L_0x0033:
            if (r9 != r12) goto L_0x0042
        L_0x0035:
            if (r7 != r12) goto L_0x0038
            r7 = r2
        L_0x0038:
            java.lang.String r0 = r1.substring(r0, r7)
            r10.name = r0
            r10.value = r4
            r0 = r7
            goto L_0x0087
        L_0x0042:
            java.lang.String r7 = r1.substring(r0, r9)
            r10.name = r7
            int r7 = r2 + -1
            if (r9 >= r7) goto L_0x005e
            int r7 = r9 + 1
            char r7 = r1.charAt(r7)
            if (r7 != r11) goto L_0x005e
            int r0 = r9 + 2
            int r0 = r1.indexOf(r11, r0)
            if (r0 != r12) goto L_0x005e
            goto L_0x0204
        L_0x005e:
            int r0 = r1.indexOf(r5, r0)
            if (r0 != r12) goto L_0x0065
            r0 = r2
        L_0x0065:
            int r7 = r0 - r9
            r14 = 4096(0x1000, float:5.74E-42)
            if (r7 <= r14) goto L_0x0075
            int r9 = r9 + r13
            int r7 = r9 + 4096
            java.lang.String r7 = r1.substring(r9, r7)
            r10.value = r7
            goto L_0x0087
        L_0x0075:
            int r7 = r9 + 1
            if (r7 == r0) goto L_0x0083
            if (r0 >= r9) goto L_0x007c
            goto L_0x0083
        L_0x007c:
            java.lang.String r7 = r1.substring(r7, r0)
            r10.value = r7
            goto L_0x0087
        L_0x0083:
            java.lang.String r7 = ""
            r10.value = r7
        L_0x0087:
            if (r0 < 0) goto L_0x0203
            if (r0 < r2) goto L_0x008d
            goto L_0x0203
        L_0x008d:
            char r7 = r1.charAt(r0)
            if (r7 == r6) goto L_0x01fb
            char r7 = r1.charAt(r0)
            if (r7 != r5) goto L_0x009b
            goto L_0x01fb
        L_0x009b:
            char r7 = r1.charAt(r0)
            r9 = 44
            if (r7 != r9) goto L_0x00a5
            goto L_0x0203
        L_0x00a5:
            int r7 = r2 - r0
            int r14 = SECURE_LENGTH
            if (r7 < r14) goto L_0x00cc
            int r14 = SECURE_LENGTH
            int r14 = r14 + r0
            java.lang.String r14 = r1.substring(r0, r14)
            java.lang.String r15 = "secure"
            boolean r14 = r14.equalsIgnoreCase(r15)
            if (r14 == 0) goto L_0x00cc
            int r7 = SECURE_LENGTH
            int r0 = r0 + r7
            r10.secure = r13
            if (r0 != r2) goto L_0x00c3
            goto L_0x0203
        L_0x00c3:
            char r7 = r1.charAt(r0)
            if (r7 != r8) goto L_0x0087
            int r0 = r0 + 1
            goto L_0x0087
        L_0x00cc:
            int r14 = HTTP_ONLY_LENGTH
            if (r7 < r14) goto L_0x00f1
            int r7 = HTTP_ONLY_LENGTH
            int r7 = r7 + r0
            java.lang.String r7 = r1.substring(r0, r7)
            java.lang.String r14 = "httponly"
            boolean r7 = r7.equalsIgnoreCase(r14)
            if (r7 == 0) goto L_0x00f1
            int r7 = HTTP_ONLY_LENGTH
            int r0 = r0 + r7
            r10.httpOnly = r13
            if (r0 != r2) goto L_0x00e8
            goto L_0x0203
        L_0x00e8:
            char r7 = r1.charAt(r0)
            if (r7 != r8) goto L_0x0087
            int r0 = r0 + 1
            goto L_0x0087
        L_0x00f1:
            int r7 = r1.indexOf(r8, r0)
            if (r7 <= 0) goto L_0x01f8
            java.lang.String r14 = r1.substring(r0, r7)
            java.lang.String r14 = r14.toLowerCase()
            java.lang.String r15 = "expires"
            boolean r15 = r14.equals(r15)
            if (r15 == 0) goto L_0x0115
            int r15 = r1.indexOf(r9, r7)
            if (r15 == r12) goto L_0x0115
            int r6 = r15 - r7
            r8 = 10
            if (r6 > r8) goto L_0x0115
            int r0 = r15 + 1
        L_0x0115:
            int r6 = r1.indexOf(r5, r0)
            int r0 = r1.indexOf(r9, r0)
            if (r6 != r12) goto L_0x0123
            if (r0 != r12) goto L_0x0123
            r6 = r2
            goto L_0x012f
        L_0x0123:
            if (r6 != r12) goto L_0x0127
        L_0x0125:
            r6 = r0
            goto L_0x012f
        L_0x0127:
            if (r0 != r12) goto L_0x012a
            goto L_0x012f
        L_0x012a:
            int r0 = java.lang.Math.min(r6, r0)
            goto L_0x0125
        L_0x012f:
            int r7 = r7 + 1
            java.lang.String r0 = r1.substring(r7, r6)     // Catch:{ Throwable -> 0x01f2 }
            int r7 = r0.length()     // Catch:{ Throwable -> 0x01f2 }
            r8 = 2
            if (r7 <= r8) goto L_0x014c
            char r7 = r0.charAt(r3)     // Catch:{ Throwable -> 0x01f2 }
            if (r7 != r11) goto L_0x014c
            int r7 = r0.indexOf(r11, r13)     // Catch:{ Throwable -> 0x01f2 }
            if (r7 <= 0) goto L_0x014c
            java.lang.String r0 = r0.substring(r13, r7)     // Catch:{ Throwable -> 0x01f2 }
        L_0x014c:
            r7 = r0
            java.lang.String r0 = "expires"
            boolean r0 = r14.equals(r0)     // Catch:{ Throwable -> 0x01f2 }
            if (r0 == 0) goto L_0x0176
            long r8 = com.taobao.login4android.session.cookies.HttpDateTime.parse(r7)     // Catch:{ IllegalArgumentException -> 0x015d }
            r10.expires = r8     // Catch:{ IllegalArgumentException -> 0x015d }
            goto L_0x01f6
        L_0x015d:
            r0 = move-exception
            java.lang.String r8 = "login.LoginCookieUtils"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01f2 }
            r9.<init>()     // Catch:{ Throwable -> 0x01f2 }
            java.lang.String r14 = "illegal format for expires: "
            r9.append(r14)     // Catch:{ Throwable -> 0x01f2 }
            r9.append(r7)     // Catch:{ Throwable -> 0x01f2 }
            java.lang.String r7 = r9.toString()     // Catch:{ Throwable -> 0x01f2 }
            com.taobao.login4android.log.LoginTLogAdapter.e(r8, r7, r0)     // Catch:{ Throwable -> 0x01f2 }
            goto L_0x01f6
        L_0x0176:
            java.lang.String r0 = "max-age"
            boolean r0 = r14.equals(r0)     // Catch:{ Throwable -> 0x01f2 }
            if (r0 == 0) goto L_0x01a8
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ NumberFormatException -> 0x0190 }
            r14 = 1000(0x3e8, double:4.94E-321)
            long r16 = java.lang.Long.parseLong(r7)     // Catch:{ NumberFormatException -> 0x0190 }
            long r16 = r16 * r14
            r0 = 0
            long r8 = r8 + r16
            r10.expires = r8     // Catch:{ NumberFormatException -> 0x0190 }
            goto L_0x01f6
        L_0x0190:
            r0 = move-exception
            java.lang.String r8 = "login.LoginCookieUtils"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01f2 }
            r9.<init>()     // Catch:{ Throwable -> 0x01f2 }
            java.lang.String r14 = "illegal format for max-age: "
            r9.append(r14)     // Catch:{ Throwable -> 0x01f2 }
            r9.append(r7)     // Catch:{ Throwable -> 0x01f2 }
            java.lang.String r7 = r9.toString()     // Catch:{ Throwable -> 0x01f2 }
            com.taobao.login4android.log.LoginTLogAdapter.e(r8, r7, r0)     // Catch:{ Throwable -> 0x01f2 }
            goto L_0x01f6
        L_0x01a8:
            java.lang.String r0 = "path"
            boolean r0 = r14.equals(r0)     // Catch:{ Throwable -> 0x01f2 }
            if (r0 == 0) goto L_0x01b9
            int r0 = r7.length()     // Catch:{ Throwable -> 0x01f2 }
            if (r0 <= 0) goto L_0x01f6
            r10.path = r7     // Catch:{ Throwable -> 0x01f2 }
            goto L_0x01f6
        L_0x01b9:
            java.lang.String r0 = "domain"
            boolean r0 = r14.equals(r0)     // Catch:{ Throwable -> 0x01f2 }
            if (r0 == 0) goto L_0x01f6
            r0 = 46
            int r8 = r7.lastIndexOf(r0)     // Catch:{ Throwable -> 0x01f2 }
            if (r8 != 0) goto L_0x01cc
            r10.domain = r4     // Catch:{ Throwable -> 0x01f2 }
            goto L_0x01f6
        L_0x01cc:
            int r8 = r8 + 1
            java.lang.String r8 = r7.substring(r8)     // Catch:{ NumberFormatException -> 0x01d6 }
            java.lang.Integer.parseInt(r8)     // Catch:{ NumberFormatException -> 0x01d6 }
            goto L_0x01f6
        L_0x01d6:
            java.lang.String r7 = r7.toLowerCase()     // Catch:{ Throwable -> 0x01f2 }
            char r8 = r7.charAt(r3)     // Catch:{ Throwable -> 0x01f2 }
            if (r8 == r0) goto L_0x01ef
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01f2 }
            r8.<init>()     // Catch:{ Throwable -> 0x01f2 }
            r8.append(r0)     // Catch:{ Throwable -> 0x01f2 }
            r8.append(r7)     // Catch:{ Throwable -> 0x01f2 }
            java.lang.String r7 = r8.toString()     // Catch:{ Throwable -> 0x01f2 }
        L_0x01ef:
            r10.domain = r7     // Catch:{ Throwable -> 0x01f2 }
            goto L_0x01f6
        L_0x01f2:
            r0 = move-exception
            r0.printStackTrace()
        L_0x01f6:
            r0 = r6
            goto L_0x01fd
        L_0x01f8:
            r0 = r2
            goto L_0x0087
        L_0x01fb:
            int r0 = r0 + 1
        L_0x01fd:
            r6 = 32
            r8 = 61
            goto L_0x0087
        L_0x0203:
            return r10
        L_0x0204:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.session.cookies.LoginCookieUtils.parseCookie(java.lang.String):com.taobao.login4android.session.cookies.LoginCookie");
    }

    public static void expiresCookies(LoginCookie loginCookie) {
        Long l = 1000L;
        loginCookie.expires = l.longValue();
    }

    public static String getHttpDomin(LoginCookie loginCookie) {
        String str = loginCookie.domain;
        if (!TextUtils.isEmpty(str) && str.startsWith(".")) {
            str = str.substring(1);
        }
        return "http://" + str;
    }
}
