package org.json.alipay;

import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.ju.track.csv.CsvReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener {
    private int index;
    private char lastChar;
    private Reader reader;
    private boolean useLastChar;

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 'W';
    }

    public JSONTokener(Reader reader2) {
        this.reader = !reader2.markSupported() ? new BufferedReader(reader2) : reader2;
        this.useLastChar = false;
        this.index = 0;
    }

    public JSONTokener(String str) {
        this((Reader) new StringReader(str));
    }

    public void back() throws JSONException {
        if (this.useLastChar || this.index <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.index--;
        this.useLastChar = true;
    }

    public boolean more() throws JSONException {
        if (next() == 0) {
            return false;
        }
        back();
        return true;
    }

    public char next() throws JSONException {
        if (this.useLastChar) {
            this.useLastChar = false;
            if (this.lastChar != 0) {
                this.index++;
            }
            return this.lastChar;
        }
        try {
            int read = this.reader.read();
            if (read <= 0) {
                this.lastChar = 0;
                return 0;
            }
            this.index++;
            this.lastChar = (char) read;
            return this.lastChar;
        } catch (IOException e) {
            throw new JSONException((Throwable) e);
        }
    }

    public char next(char c) throws JSONException {
        char next = next();
        if (next == c) {
            return next;
        }
        throw syntaxError("Expected '" + c + "' and instead saw '" + next + DXBindingXConstant.SINGLE_QUOTE);
    }

    public String next(int i) throws JSONException {
        if (i == 0) {
            return "";
        }
        char[] cArr = new char[i];
        int i2 = 0;
        if (this.useLastChar) {
            this.useLastChar = false;
            cArr[0] = this.lastChar;
            i2 = 1;
        }
        while (i2 < i) {
            try {
                int read = this.reader.read(cArr, i2, i - i2);
                if (read == -1) {
                    break;
                }
                i2 += read;
            } catch (IOException e) {
                throw new JSONException((Throwable) e);
            }
        }
        this.index += i2;
        if (i2 >= i) {
            this.lastChar = cArr[i - 1];
            return new String(cArr);
        }
        throw syntaxError("Substring bounds error");
    }

    public char nextClean() throws JSONException {
        char next;
        char next2;
        char next3;
        while (true) {
            next = next();
            if (next == '/') {
                char next4 = next();
                if (next4 != '*') {
                    if (next4 == '/') {
                        do {
                            next3 = next();
                            if (next3 == 10 || next3 == 13) {
                                break;
                            }
                        } while (next3 != 0);
                    } else {
                        back();
                        return DXTemplateNamePathUtil.DIR;
                    }
                } else {
                    while (true) {
                        char next5 = next();
                        if (next5 == 0) {
                            throw syntaxError("Unclosed comment");
                        } else if (next5 == '*') {
                            if (next() == '/') {
                                break;
                            }
                            back();
                        }
                    }
                }
            } else if (next == '#') {
                do {
                    next2 = next();
                    if (next2 == 10 || next2 == 13) {
                        break;
                    }
                } while (next2 != 0);
            } else if (next == 0 || next > ' ') {
                return next;
            }
        }
        return next;
    }

    public String nextString(char c) throws JSONException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            char next = next();
            if (next != 0 && next != 10 && next != 13) {
                if (next == '\\') {
                    char next2 = next();
                    if (next2 == 'b') {
                        stringBuffer.append(8);
                    } else if (next2 == 'f') {
                        stringBuffer.append(CsvReader.Letters.FORM_FEED);
                    } else if (next2 == 'n') {
                        stringBuffer.append(10);
                    } else if (next2 == 'r') {
                        stringBuffer.append(13);
                    } else if (next2 != 'x') {
                        switch (next2) {
                            case 't':
                                stringBuffer.append(9);
                                break;
                            case 'u':
                                stringBuffer.append((char) Integer.parseInt(next(4), 16));
                                break;
                            default:
                                stringBuffer.append(next2);
                                break;
                        }
                    } else {
                        stringBuffer.append((char) Integer.parseInt(next(2), 16));
                    }
                } else if (next == c) {
                    return stringBuffer.toString();
                } else {
                    stringBuffer.append(next);
                }
            }
        }
        throw syntaxError("Unterminated string");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(char r4) throws org.json.alipay.JSONException {
        /*
            r3 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
        L_0x0005:
            char r1 = r3.next()
            if (r1 == r4) goto L_0x001a
            if (r1 == 0) goto L_0x001a
            r2 = 10
            if (r1 == r2) goto L_0x001a
            r2 = 13
            if (r1 != r2) goto L_0x0016
            goto L_0x001a
        L_0x0016:
            r0.append(r1)
            goto L_0x0005
        L_0x001a:
            if (r1 == 0) goto L_0x001f
            r3.back()
        L_0x001f:
            java.lang.String r4 = r0.toString()
            java.lang.String r4 = r4.trim()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.alipay.JSONTokener.nextTo(char):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(java.lang.String r4) throws org.json.alipay.JSONException {
        /*
            r3 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
        L_0x0005:
            char r1 = r3.next()
            int r2 = r4.indexOf(r1)
            if (r2 >= 0) goto L_0x001e
            if (r1 == 0) goto L_0x001e
            r2 = 10
            if (r1 == r2) goto L_0x001e
            r2 = 13
            if (r1 != r2) goto L_0x001a
            goto L_0x001e
        L_0x001a:
            r0.append(r1)
            goto L_0x0005
        L_0x001e:
            if (r1 == 0) goto L_0x0023
            r3.back()
        L_0x0023:
            java.lang.String r4 = r0.toString()
            java.lang.String r4 = r4.trim()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.alipay.JSONTokener.nextTo(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:53|54|55) */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:56|57|58) */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:(2:39|(2:48|49)(3:45|46|47))|50|51|52) */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00b8, code lost:
        return new java.lang.Long(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00be, code lost:
        return new java.lang.Double(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00bf, code lost:
        return r1;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:50:0x00ad */
    /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x00b3 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:56:0x00b9 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object nextValue() throws org.json.alipay.JSONException {
        /*
            r5 = this;
            char r0 = r5.nextClean()
            r1 = 34
            if (r0 == r1) goto L_0x00d9
            r1 = 91
            if (r0 == r1) goto L_0x00d0
            r1 = 123(0x7b, float:1.72E-43)
            if (r0 == r1) goto L_0x00c7
            switch(r0) {
                case 39: goto L_0x00d9;
                case 40: goto L_0x00d0;
                default: goto L_0x0013;
            }
        L_0x0013:
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            r2 = r0
        L_0x0019:
            r3 = 32
            if (r2 < r3) goto L_0x002d
            java.lang.String r3 = ",:]}/\\\"[{;=#"
            int r3 = r3.indexOf(r2)
            if (r3 >= 0) goto L_0x002d
            r1.append(r2)
            char r2 = r5.next()
            goto L_0x0019
        L_0x002d:
            r5.back()
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = r1.trim()
            java.lang.String r2 = ""
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L_0x00c0
            java.lang.String r2 = "true"
            boolean r2 = r1.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x004b
            java.lang.Boolean r0 = java.lang.Boolean.TRUE
            return r0
        L_0x004b:
            java.lang.String r2 = "false"
            boolean r2 = r1.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x0056
            java.lang.Boolean r0 = java.lang.Boolean.FALSE
            return r0
        L_0x0056:
            java.lang.String r2 = "null"
            boolean r2 = r1.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x0061
            java.lang.Object r0 = org.json.alipay.JSONObject.NULL
            return r0
        L_0x0061:
            r2 = 48
            if (r0 < r2) goto L_0x0069
            r3 = 57
            if (r0 <= r3) goto L_0x0077
        L_0x0069:
            r3 = 46
            if (r0 == r3) goto L_0x0077
            r3 = 45
            if (r0 == r3) goto L_0x0077
            r3 = 43
            if (r0 != r3) goto L_0x0076
            goto L_0x0077
        L_0x0076:
            return r1
        L_0x0077:
            if (r0 != r2) goto L_0x00ad
            int r0 = r1.length()
            r2 = 2
            if (r0 <= r2) goto L_0x00a1
            r0 = 1
            char r3 = r1.charAt(r0)
            r4 = 120(0x78, float:1.68E-43)
            if (r3 == r4) goto L_0x0091
            char r0 = r1.charAt(r0)
            r3 = 88
            if (r0 != r3) goto L_0x00a1
        L_0x0091:
            java.lang.Integer r0 = new java.lang.Integer     // Catch:{ Exception -> 0x00ad }
            java.lang.String r2 = r1.substring(r2)     // Catch:{ Exception -> 0x00ad }
            r3 = 16
            int r2 = java.lang.Integer.parseInt(r2, r3)     // Catch:{ Exception -> 0x00ad }
            r0.<init>(r2)     // Catch:{ Exception -> 0x00ad }
            return r0
        L_0x00a1:
            java.lang.Integer r0 = new java.lang.Integer     // Catch:{ Exception -> 0x00ad }
            r2 = 8
            int r2 = java.lang.Integer.parseInt(r1, r2)     // Catch:{ Exception -> 0x00ad }
            r0.<init>(r2)     // Catch:{ Exception -> 0x00ad }
            return r0
        L_0x00ad:
            java.lang.Integer r0 = new java.lang.Integer     // Catch:{ Exception -> 0x00b3 }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00b3 }
            return r0
        L_0x00b3:
            java.lang.Long r0 = new java.lang.Long     // Catch:{ Exception -> 0x00b9 }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00b9 }
            return r0
        L_0x00b9:
            java.lang.Double r0 = new java.lang.Double     // Catch:{ Exception -> 0x00bf }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00bf }
            return r0
        L_0x00bf:
            return r1
        L_0x00c0:
            java.lang.String r0 = "Missing value"
            org.json.alipay.JSONException r0 = r5.syntaxError(r0)
            throw r0
        L_0x00c7:
            r5.back()
            org.json.alipay.JSONObject r0 = new org.json.alipay.JSONObject
            r0.<init>((org.json.alipay.JSONTokener) r5)
            return r0
        L_0x00d0:
            r5.back()
            org.json.alipay.JSONArray r0 = new org.json.alipay.JSONArray
            r0.<init>((org.json.alipay.JSONTokener) r5)
            return r0
        L_0x00d9:
            java.lang.String r0 = r5.nextString(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.alipay.JSONTokener.nextValue():java.lang.Object");
    }

    public char skipTo(char c) throws JSONException {
        char next;
        try {
            int i = this.index;
            this.reader.mark(Integer.MAX_VALUE);
            do {
                next = next();
                if (next == 0) {
                    this.reader.reset();
                    this.index = i;
                    return next;
                }
            } while (next != c);
            back();
            return next;
        } catch (IOException e) {
            throw new JSONException((Throwable) e);
        }
    }

    public JSONException syntaxError(String str) {
        return new JSONException(str + toString());
    }

    public String toString() {
        return " at character " + this.index;
    }
}
