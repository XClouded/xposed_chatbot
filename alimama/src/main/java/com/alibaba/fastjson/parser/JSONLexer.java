package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.wireless.security.SecExceptionCode;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.ju.track.csv.CsvReader;
import com.taobao.tao.image.Logger;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.common.Constants;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class JSONLexer {
    public static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    public static final int END = 4;
    public static final char EOI = '\u001a';
    static final int[] IA = new int[256];
    public static final int NOT_MATCH = -1;
    public static final int NOT_MATCH_NAME = -2;
    public static final int UNKNOWN = 0;
    private static boolean V6 = false;
    public static final int VALUE = 3;
    protected static final int[] digits = new int[103];
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];
    private static final ThreadLocal<char[]> sbufLocal = new ThreadLocal<>();
    protected int bp;
    public Calendar calendar;
    protected char ch;
    public boolean disableCircularReferenceDetect;
    protected int eofPos;
    protected boolean exp;
    public int features;
    protected long fieldHash;
    protected boolean hasSpecial;
    protected boolean isDouble;
    protected final int len;
    public Locale locale;
    public int matchStat;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected String stringDefaultValue;
    protected final String text;
    public TimeZone timeZone;
    protected int token;

    static boolean checkDate(char c, char c2, char c3, char c4, char c5, char c6, int i, int i2) {
        if (c < '1' || c > '3' || c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9') {
            return false;
        }
        if (c5 == '0') {
            if (c6 < '1' || c6 > '9') {
                return false;
            }
        } else if (c5 != '1') {
            return false;
        } else {
            if (!(c6 == '0' || c6 == '1' || c6 == '2')) {
                return false;
            }
        }
        if (i == 48) {
            return i2 >= 49 && i2 <= 57;
        }
        if (i == 49 || i == 50) {
            return i2 >= 48 && i2 <= 57;
        }
        if (i == 51) {
            return i2 == 48 || i2 == 49;
        }
        return false;
    }

    static boolean checkTime(char c, char c2, char c3, char c4, char c5, char c6) {
        if (c == '0') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c == '1') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c != '2' || c2 < '0' || c2 > '4') {
            return false;
        }
        if (c3 < '0' || c3 > '5') {
            if (!(c3 == '6' && c4 == '0')) {
                return false;
            }
        } else if (c4 < '0' || c4 > '9') {
            return false;
        }
        return (c5 < '0' || c5 > '5') ? c5 == '6' && c6 == '0' : c6 >= '0' && c6 <= '9';
    }

    static {
        int i;
        try {
            i = Class.forName("android.os.Build$VERSION").getField("SDK_INT").getInt((Object) null);
        } catch (Exception unused) {
            i = -1;
        }
        V6 = i >= 23;
        for (int i2 = 48; i2 <= 57; i2++) {
            digits[i2] = i2 - 48;
        }
        for (int i3 = 97; i3 <= 102; i3++) {
            digits[i3] = (i3 - 97) + 10;
        }
        for (int i4 = 65; i4 <= 70; i4++) {
            digits[i4] = (i4 - 65) + 10;
        }
        Arrays.fill(IA, -1);
        int length = CA.length;
        for (int i5 = 0; i5 < length; i5++) {
            IA[CA[i5]] = i5;
        }
        IA[61] = 0;
        for (char c = 0; c < firstIdentifierFlags.length; c = (char) (c + 1)) {
            if (c >= 'A' && c <= 'Z') {
                firstIdentifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                firstIdentifierFlags[c] = true;
            } else if (c == '_') {
                firstIdentifierFlags[c] = true;
            }
        }
        for (char c2 = 0; c2 < identifierFlags.length; c2 = (char) (c2 + 1)) {
            if (c2 >= 'A' && c2 <= 'Z') {
                identifierFlags[c2] = true;
            } else if (c2 >= 'a' && c2 <= 'z') {
                identifierFlags[c2] = true;
            } else if (c2 == '_') {
                identifierFlags[c2] = true;
            } else if (c2 >= '0' && c2 <= '9') {
                identifierFlags[c2] = true;
            }
        }
    }

    public JSONLexer(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONLexer(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONLexer(char[] cArr, int i, int i2) {
        this(new String(cArr, 0, i), i2);
    }

    public JSONLexer(String str, int i) {
        char c;
        this.features = JSON.DEFAULT_PARSER_FEATURE;
        boolean z = false;
        this.exp = false;
        this.isDouble = false;
        this.timeZone = JSON.defaultTimeZone;
        this.locale = JSON.defaultLocale;
        String str2 = null;
        this.calendar = null;
        this.matchStat = 0;
        this.sbuf = sbufLocal.get();
        if (this.sbuf == null) {
            this.sbuf = new char[512];
        }
        this.features = i;
        this.text = str;
        this.len = this.text.length();
        this.bp = -1;
        int i2 = this.bp + 1;
        this.bp = i2;
        if (i2 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i2);
        }
        this.ch = c;
        if (this.ch == 65279) {
            next();
        }
        this.stringDefaultValue = (Feature.InitStringFieldAsEmpty.mask & i) != 0 ? "" : str2;
        this.disableCircularReferenceDetect = (Feature.DisableCircularReferenceDetect.mask & i) != 0 ? true : z;
    }

    public final int token() {
        return this.token;
    }

    public void close() {
        if (this.sbuf.length <= 8196) {
            sbufLocal.set(this.sbuf);
        }
        this.sbuf = null;
    }

    public char next() {
        char c;
        int i = this.bp + 1;
        this.bp = i;
        if (i >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i);
        }
        this.ch = c;
        return c;
    }

    public final void config(Feature feature, boolean z) {
        if (z) {
            this.features |= feature.mask;
        } else {
            this.features &= feature.mask ^ -1;
        }
        if (feature == Feature.InitStringFieldAsEmpty) {
            this.stringDefaultValue = z ? "" : null;
        }
        this.disableCircularReferenceDetect = (this.features & Feature.DisableCircularReferenceDetect.mask) != 0;
    }

    public final boolean isEnabled(Feature feature) {
        return (feature.mask & this.features) != 0;
    }

    public final void nextTokenWithChar(char c) {
        char c2;
        this.sp = 0;
        while (this.ch != c) {
            if (this.ch == ' ' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 12 || this.ch == 8) {
                next();
            } else {
                throw new JSONException("not match " + c + " - " + this.ch);
            }
        }
        int i = this.bp + 1;
        this.bp = i;
        if (i >= this.len) {
            c2 = EOI;
        } else {
            c2 = this.text.charAt(i);
        }
        this.ch = c2;
        nextToken();
    }

    public final String numberString() {
        char charAt = this.text.charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        return subString(this.np, i);
    }

    /* access modifiers changed from: protected */
    public char charAt(int i) {
        if (i >= this.len) {
            return EOI;
        }
        return this.text.charAt(i);
    }

    public final void nextToken() {
        int i = 0;
        this.sp = 0;
        while (true) {
            this.pos = this.bp;
            if (this.ch != '/') {
                if (this.ch != '\"') {
                    if ((this.ch < '0' || this.ch > '9') && this.ch != '-') {
                        if (this.ch != ',') {
                            char c = this.ch;
                            char c2 = EOI;
                            switch (c) {
                                case 8:
                                case 9:
                                case 10:
                                case 12:
                                case 13:
                                case ' ':
                                    next();
                                    break;
                                case '\'':
                                    scanString();
                                    return;
                                case '(':
                                    next();
                                    this.token = 10;
                                    return;
                                case ')':
                                    next();
                                    this.token = 11;
                                    return;
                                case ':':
                                    next();
                                    this.token = 17;
                                    return;
                                case 'S':
                                case 'T':
                                case 'u':
                                    scanIdent();
                                    return;
                                case '[':
                                    int i2 = this.bp + 1;
                                    this.bp = i2;
                                    if (i2 < this.len) {
                                        c2 = this.text.charAt(i2);
                                    }
                                    this.ch = c2;
                                    this.token = 14;
                                    return;
                                case ']':
                                    next();
                                    this.token = 15;
                                    return;
                                case 'f':
                                    if (this.text.startsWith("false", this.bp)) {
                                        this.bp += 5;
                                        this.ch = charAt(this.bp);
                                        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 26 || this.ch == 12 || this.ch == 8 || this.ch == ':') {
                                            this.token = 7;
                                            return;
                                        }
                                    }
                                    throw new JSONException("scan false error");
                                case 'n':
                                    if (this.text.startsWith(BuildConfig.buildJavascriptFrameworkVersion, this.bp)) {
                                        this.bp += 4;
                                        i = 8;
                                    } else if (this.text.startsWith("new", this.bp)) {
                                        this.bp += 3;
                                        i = 9;
                                    }
                                    if (i != 0) {
                                        this.ch = charAt(this.bp);
                                        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 26 || this.ch == 12 || this.ch == 8) {
                                            this.token = i;
                                            return;
                                        }
                                    }
                                    throw new JSONException("scan null/new error");
                                case 't':
                                    if (this.text.startsWith("true", this.bp)) {
                                        this.bp += 4;
                                        this.ch = charAt(this.bp);
                                        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 26 || this.ch == 12 || this.ch == 8 || this.ch == ':') {
                                            this.token = 6;
                                            return;
                                        }
                                    }
                                    throw new JSONException("scan true error");
                                case SecExceptionCode.SEC_ERROR_INIT_INCORRECT_DATA_FILE /*123*/:
                                    int i3 = this.bp + 1;
                                    this.bp = i3;
                                    if (i3 < this.len) {
                                        c2 = this.text.charAt(i3);
                                    }
                                    this.ch = c2;
                                    this.token = 12;
                                    return;
                                case '}':
                                    int i4 = this.bp + 1;
                                    this.bp = i4;
                                    if (i4 < this.len) {
                                        c2 = this.text.charAt(i4);
                                    }
                                    this.ch = c2;
                                    this.token = 13;
                                    return;
                                default:
                                    if (this.bp == this.len || (this.ch == 26 && this.bp + 1 == this.len)) {
                                        if (this.token != 20) {
                                            this.token = 20;
                                            int i5 = this.eofPos;
                                            this.bp = i5;
                                            this.pos = i5;
                                            return;
                                        }
                                        throw new JSONException("EOF error");
                                    } else if (this.ch <= 31 || this.ch == 127) {
                                        next();
                                        break;
                                    } else {
                                        this.token = 1;
                                        next();
                                        return;
                                    }
                                    break;
                            }
                        } else {
                            next();
                            this.token = 16;
                            return;
                        }
                    }
                } else {
                    scanString();
                    return;
                }
            } else {
                skipComment();
            }
        }
        scanNumber();
    }

    public final void nextToken(int i) {
        this.sp = 0;
        while (true) {
            if (i != 2) {
                char c = EOI;
                if (i != 4) {
                    if (i != 12) {
                        if (i != 18) {
                            if (i != 20) {
                                switch (i) {
                                    case 14:
                                        if (this.ch == '[') {
                                            this.token = 14;
                                            next();
                                            return;
                                        } else if (this.ch == '{') {
                                            this.token = 12;
                                            next();
                                            return;
                                        }
                                        break;
                                    case 15:
                                        if (this.ch == ']') {
                                            this.token = 15;
                                            next();
                                            return;
                                        }
                                        break;
                                    case 16:
                                        if (this.ch == ',') {
                                            this.token = 16;
                                            int i2 = this.bp + 1;
                                            this.bp = i2;
                                            if (i2 < this.len) {
                                                c = this.text.charAt(i2);
                                            }
                                            this.ch = c;
                                            return;
                                        } else if (this.ch == '}') {
                                            this.token = 13;
                                            int i3 = this.bp + 1;
                                            this.bp = i3;
                                            if (i3 < this.len) {
                                                c = this.text.charAt(i3);
                                            }
                                            this.ch = c;
                                            return;
                                        } else if (this.ch == ']') {
                                            this.token = 15;
                                            int i4 = this.bp + 1;
                                            this.bp = i4;
                                            if (i4 < this.len) {
                                                c = this.text.charAt(i4);
                                            }
                                            this.ch = c;
                                            return;
                                        } else if (this.ch == 26) {
                                            this.token = 20;
                                            return;
                                        }
                                        break;
                                }
                            }
                            if (this.ch == 26) {
                                this.token = 20;
                                return;
                            }
                        } else {
                            nextIdent();
                            return;
                        }
                    } else if (this.ch == '{') {
                        this.token = 12;
                        int i5 = this.bp + 1;
                        this.bp = i5;
                        if (i5 < this.len) {
                            c = this.text.charAt(i5);
                        }
                        this.ch = c;
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        int i6 = this.bp + 1;
                        this.bp = i6;
                        if (i6 < this.len) {
                            c = this.text.charAt(i6);
                        }
                        this.ch = c;
                        return;
                    }
                } else if (this.ch == '\"') {
                    this.pos = this.bp;
                    scanString();
                    return;
                } else if (this.ch >= '0' && this.ch <= '9') {
                    this.pos = this.bp;
                    scanNumber();
                    return;
                } else if (this.ch == '{') {
                    this.token = 12;
                    int i7 = this.bp + 1;
                    this.bp = i7;
                    if (i7 < this.len) {
                        c = this.text.charAt(i7);
                    }
                    this.ch = c;
                    return;
                }
            } else if (this.ch >= '0' && this.ch <= '9') {
                this.pos = this.bp;
                scanNumber();
                return;
            } else if (this.ch == '\"') {
                this.pos = this.bp;
                scanString();
                return;
            } else if (this.ch == '[') {
                this.token = 14;
                next();
                return;
            } else if (this.ch == '{') {
                this.token = 12;
                next();
                return;
            }
            if (this.ch == ' ' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 12 || this.ch == 8) {
                next();
            } else {
                nextToken();
                return;
            }
        }
    }

    public final void nextIdent() {
        while (true) {
            if (!(this.ch <= ' ' && (this.ch == ' ' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 12 || this.ch == 8))) {
                break;
            }
            next();
        }
        if (this.ch == '_' || Character.isLetter(this.ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final Number integerValue() throws NumberFormatException {
        char c;
        char c2;
        char c3;
        boolean z;
        long j;
        long j2;
        char c4;
        char c5;
        int i = this.np;
        int i2 = this.np + this.sp;
        int i3 = i2 - 1;
        if (i3 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i3);
        }
        if (c == 'B') {
            i2--;
            c2 = 'B';
        } else if (c == 'L') {
            i2--;
            c2 = Logger.LEVEL_L;
        } else if (c != 'S') {
            c2 = ' ';
        } else {
            i2--;
            c2 = 'S';
        }
        if (this.np >= this.len) {
            c3 = EOI;
        } else {
            c3 = this.text.charAt(this.np);
        }
        if (c3 == '-') {
            j = Long.MIN_VALUE;
            i++;
            z = true;
        } else {
            j = -9223372036854775807L;
            z = false;
        }
        if (i < i2) {
            int i4 = i + 1;
            if (i >= this.len) {
                c5 = EOI;
            } else {
                c5 = this.text.charAt(i);
            }
            j2 = (long) (-(c5 - '0'));
            i = i4;
        } else {
            j2 = 0;
        }
        while (i < i2) {
            int i5 = i + 1;
            if (i >= this.len) {
                c4 = EOI;
            } else {
                c4 = this.text.charAt(i);
            }
            int i6 = c4 - '0';
            if (j2 < -922337203685477580L) {
                return new BigInteger(numberString());
            }
            long j3 = j2 * 10;
            long j4 = (long) i6;
            if (j3 < j + j4) {
                return new BigInteger(numberString());
            }
            j2 = j3 - j4;
            i = i5;
        }
        if (!z) {
            long j5 = -j2;
            if (j5 > 2147483647L || c2 == 'L') {
                return Long.valueOf(j5);
            }
            if (c2 == 'S') {
                return Short.valueOf((short) ((int) j5));
            }
            if (c2 == 'B') {
                return Byte.valueOf((byte) ((int) j5));
            }
            return Integer.valueOf((int) j5);
        } else if (i <= this.np + 1) {
            throw new NumberFormatException(numberString());
        } else if (j2 < -2147483648L || c2 == 'L') {
            return Long.valueOf(j2);
        } else {
            if (c2 == 'S') {
                return Short.valueOf((short) ((int) j2));
            }
            if (c2 == 'B') {
                return Byte.valueOf((byte) ((int) j2));
            }
            return Integer.valueOf((int) j2);
        }
    }

    public final String scanSymbol(SymbolTable symbolTable) {
        while (true) {
            if (this.ch != ' ' && this.ch != 10 && this.ch != 13 && this.ch != 9 && this.ch != 12 && this.ch != 8) {
                break;
            }
            next();
        }
        if (this.ch == '\"') {
            return scanSymbol(symbolTable, '\"');
        }
        if (this.ch == '\'') {
            return scanSymbol(symbolTable, '\'');
        }
        if (this.ch == '}') {
            next();
            this.token = 13;
            return null;
        } else if (this.ch == ',') {
            next();
            this.token = 16;
            return null;
        } else if (this.ch != 26) {
            return scanSymbolUnQuoted(symbolTable);
        } else {
            this.token = 20;
            return null;
        }
    }

    public String scanSymbol(SymbolTable symbolTable, char c) {
        String str;
        char c2;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c, i);
        if (indexOf != -1) {
            int i2 = indexOf - i;
            char[] sub_chars = sub_chars(this.bp + 1, i2);
            int i3 = indexOf;
            boolean z = false;
            while (i2 > 0 && sub_chars[i2 - 1] == '\\') {
                int i4 = i2 - 2;
                int i5 = 1;
                while (i4 >= 0 && sub_chars[i4] == '\\') {
                    i5++;
                    i4--;
                }
                if (i5 % 2 == 0) {
                    break;
                }
                int indexOf2 = this.text.indexOf(c, i3 + 1);
                int i6 = (indexOf2 - i3) + i2;
                if (i6 >= sub_chars.length) {
                    int length = (sub_chars.length * 3) / 2;
                    if (length < i6) {
                        length = i6;
                    }
                    char[] cArr = new char[length];
                    System.arraycopy(sub_chars, 0, cArr, 0, sub_chars.length);
                    sub_chars = cArr;
                }
                this.text.getChars(i3, indexOf2, sub_chars, i2);
                i3 = indexOf2;
                i2 = i6;
                z = true;
            }
            if (!z) {
                boolean z2 = z;
                int i7 = 0;
                for (int i8 = 0; i8 < i2; i8++) {
                    char c3 = sub_chars[i8];
                    i7 = (i7 * 31) + c3;
                    if (c3 == '\\') {
                        z2 = true;
                    }
                }
                if (z2) {
                    str = readString(sub_chars, i2);
                } else {
                    str = i2 < 20 ? symbolTable.addSymbol(sub_chars, 0, i2, i7) : new String(sub_chars, 0, i2);
                }
            } else {
                str = readString(sub_chars, i2);
            }
            this.bp = i3 + 1;
            int i9 = this.bp;
            if (i9 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i9);
            }
            this.ch = c2;
            return str;
        }
        throw new JSONException("unclosed str, " + info());
    }

    private static String readString(char[] cArr, int i) {
        int i2;
        int i3;
        char[] cArr2 = new char[i];
        int i4 = 0;
        int i5 = 0;
        while (i2 < i) {
            char c = cArr[i2];
            if (c != '\\') {
                cArr2[i5] = c;
                i5++;
            } else {
                i2++;
                char c2 = cArr[i2];
                switch (c2) {
                    case '/':
                        i3 = i5 + 1;
                        cArr2[i5] = DXTemplateNamePathUtil.DIR;
                        break;
                    case '0':
                        i3 = i5 + 1;
                        cArr2[i5] = 0;
                        break;
                    case '1':
                        i3 = i5 + 1;
                        cArr2[i5] = 1;
                        break;
                    case '2':
                        i3 = i5 + 1;
                        cArr2[i5] = 2;
                        break;
                    case '3':
                        i3 = i5 + 1;
                        cArr2[i5] = 3;
                        break;
                    case '4':
                        i3 = i5 + 1;
                        cArr2[i5] = 4;
                        break;
                    case '5':
                        i3 = i5 + 1;
                        cArr2[i5] = 5;
                        break;
                    case '6':
                        i3 = i5 + 1;
                        cArr2[i5] = 6;
                        break;
                    case '7':
                        i3 = i5 + 1;
                        cArr2[i5] = 7;
                        break;
                    default:
                        switch (c2) {
                            case 't':
                                i3 = i5 + 1;
                                cArr2[i5] = 9;
                                break;
                            case 'u':
                                i3 = i5 + 1;
                                int i6 = i2 + 1;
                                int i7 = i6 + 1;
                                int i8 = i7 + 1;
                                i2 = i8 + 1;
                                cArr2[i5] = (char) Integer.parseInt(new String(new char[]{cArr[i6], cArr[i7], cArr[i8], cArr[i2]}), 16);
                                break;
                            case 'v':
                                i3 = i5 + 1;
                                cArr2[i5] = CsvReader.Letters.VERTICAL_TAB;
                                break;
                            default:
                                switch (c2) {
                                    case '\"':
                                        i3 = i5 + 1;
                                        cArr2[i5] = '\"';
                                        break;
                                    case '\'':
                                        i3 = i5 + 1;
                                        cArr2[i5] = '\'';
                                        break;
                                    case 'F':
                                    case 'f':
                                        i3 = i5 + 1;
                                        cArr2[i5] = CsvReader.Letters.FORM_FEED;
                                        break;
                                    case '\\':
                                        i3 = i5 + 1;
                                        cArr2[i5] = '\\';
                                        break;
                                    case 'b':
                                        i3 = i5 + 1;
                                        cArr2[i5] = 8;
                                        break;
                                    case 'n':
                                        i3 = i5 + 1;
                                        cArr2[i5] = 10;
                                        break;
                                    case 'r':
                                        i3 = i5 + 1;
                                        cArr2[i5] = 13;
                                        break;
                                    case 'x':
                                        i3 = i5 + 1;
                                        int i9 = i2 + 1;
                                        i2 = i9 + 1;
                                        cArr2[i5] = (char) ((digits[cArr[i9]] * 16) + digits[cArr[i2]]);
                                        break;
                                    default:
                                        throw new JSONException("unclosed.str.lit");
                                }
                        }
                }
                i5 = i3;
            }
            i4 = i2 + 1;
        }
        return new String(cArr2, 0, i5);
    }

    public String info() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("pos ");
        sb.append(this.bp);
        sb.append(", json : ");
        if (this.len < 65536) {
            str = this.text;
        } else {
            str = this.text.substring(0, 65536);
        }
        sb.append(str);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void skipComment() {
        next();
        if (this.ch == '/') {
            do {
                next();
            } while (this.ch != 10);
            next();
        } else if (this.ch == '*') {
            next();
            while (this.ch != 26) {
                if (this.ch == '*') {
                    next();
                    if (this.ch == '/') {
                        next();
                        return;
                    }
                } else {
                    next();
                }
            }
        } else {
            throw new JSONException("invalid comment");
        }
    }

    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        int i = this.ch;
        if (this.ch >= firstIdentifierFlags.length || firstIdentifierFlags[i]) {
            this.np = this.bp;
            this.sp = 1;
            while (true) {
                char next = next();
                if (next < identifierFlags.length && !identifierFlags[next]) {
                    break;
                }
                i = (i * 31) + next;
                this.sp++;
            }
            this.ch = charAt(this.bp);
            this.token = 18;
            if (this.sp != 4 || !this.text.startsWith(BuildConfig.buildJavascriptFrameworkVersion, this.np)) {
                return symbolTable.addSymbol(this.text, this.np, this.sp, i);
            }
            return null;
        }
        throw new JSONException("illegal identifier : " + this.ch + AVFSCacheConstants.COMMA_SEP + info());
    }

    public final void scanString() {
        char c;
        char c2 = this.ch;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c2, i);
        if (indexOf != -1) {
            int i2 = indexOf - i;
            char[] sub_chars = sub_chars(this.bp + 1, i2);
            boolean z = false;
            while (i2 > 0 && sub_chars[i2 - 1] == '\\') {
                int i3 = i2 - 2;
                int i4 = 1;
                while (i3 >= 0 && sub_chars[i3] == '\\') {
                    i4++;
                    i3--;
                }
                if (i4 % 2 == 0) {
                    break;
                }
                int indexOf2 = this.text.indexOf(c2, indexOf + 1);
                int i5 = (indexOf2 - indexOf) + i2;
                if (i5 >= sub_chars.length) {
                    int length = (sub_chars.length * 3) / 2;
                    if (length < i5) {
                        length = i5;
                    }
                    char[] cArr = new char[length];
                    System.arraycopy(sub_chars, 0, cArr, 0, sub_chars.length);
                    sub_chars = cArr;
                }
                this.text.getChars(indexOf, indexOf2, sub_chars, i2);
                indexOf = indexOf2;
                i2 = i5;
                z = true;
            }
            if (!z) {
                for (int i6 = 0; i6 < i2; i6++) {
                    if (sub_chars[i6] == '\\') {
                        z = true;
                    }
                }
            }
            this.sbuf = sub_chars;
            this.sp = i2;
            this.np = this.bp;
            this.hasSpecial = z;
            this.bp = indexOf + 1;
            int i7 = this.bp;
            if (i7 >= this.len) {
                c = EOI;
            } else {
                c = this.text.charAt(i7);
            }
            this.ch = c;
            this.token = 4;
            return;
        }
        throw new JSONException("unclosed str, " + info());
    }

    public String scanStringValue(char c) {
        String str;
        char c2;
        int i = this.bp + 1;
        int indexOf = this.text.indexOf(c, i);
        if (indexOf != -1) {
            if (V6) {
                str = this.text.substring(i, indexOf);
            } else {
                int i2 = indexOf - i;
                str = new String(sub_chars(this.bp + 1, i2), 0, i2);
            }
            if (str.indexOf(92) != -1) {
                while (true) {
                    int i3 = indexOf - 1;
                    int i4 = 0;
                    while (i3 >= 0 && this.text.charAt(i3) == '\\') {
                        i4++;
                        i3--;
                    }
                    if (i4 % 2 == 0) {
                        break;
                    }
                    indexOf = this.text.indexOf(c, indexOf + 1);
                }
                int i5 = indexOf - i;
                str = readString(sub_chars(this.bp + 1, i5), i5);
            }
            this.bp = indexOf + 1;
            int i6 = this.bp;
            if (i6 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i6);
            }
            this.ch = c2;
            return str;
        }
        throw new JSONException("unclosed str, " + info());
    }

    public final int intValue() {
        char c;
        boolean z;
        int i;
        int i2;
        char c2;
        char c3;
        int i3 = this.np;
        int i4 = this.np + this.sp;
        if (this.np >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(this.np);
        }
        int i5 = 0;
        if (c == '-') {
            i = Integer.MIN_VALUE;
            i3++;
            z = true;
        } else {
            i = -2147483647;
            z = false;
        }
        if (i3 < i4) {
            int i6 = i3 + 1;
            if (i3 >= this.len) {
                c3 = EOI;
            } else {
                c3 = this.text.charAt(i3);
            }
            int i7 = i6;
            i5 = -(c3 - '0');
            i3 = i7;
        }
        while (true) {
            if (i3 >= i4) {
                break;
            }
            i2 = i3 + 1;
            if (i3 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i3);
            }
            if (c2 == 'L' || c2 == 'S' || c2 == 'B') {
                i3 = i2;
            } else {
                int i8 = c2 - '0';
                if (i5 >= -214748364) {
                    int i9 = i5 * 10;
                    if (i9 >= i + i8) {
                        i5 = i9 - i8;
                        i3 = i2;
                    } else {
                        throw new NumberFormatException(numberString());
                    }
                } else {
                    throw new NumberFormatException(numberString());
                }
            }
        }
        i3 = i2;
        if (!z) {
            return -i5;
        }
        if (i3 > this.np + 1) {
            return i5;
        }
        throw new NumberFormatException(numberString());
    }

    public byte[] bytesValue() {
        return decodeFast(this.text, this.np + 1, this.sp);
    }

    private void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        String stringVal = stringVal();
        if (stringVal.equals(BuildConfig.buildJavascriptFrameworkVersion)) {
            this.token = 8;
        } else if (stringVal.equals("true")) {
            this.token = 6;
        } else if (stringVal.equals("false")) {
            this.token = 7;
        } else if (stringVal.equals("new")) {
            this.token = 9;
        } else if (stringVal.equals(Constants.Name.UNDEFINED)) {
            this.token = 23;
        } else if (stringVal.equals("Set")) {
            this.token = 21;
        } else if (stringVal.equals("TreeSet")) {
            this.token = 22;
        } else {
            this.token = 18;
        }
    }

    public final String stringVal() {
        if (this.hasSpecial) {
            return readString(this.sbuf, this.sp);
        }
        return subString(this.np + 1, this.sp);
    }

    private final String subString(int i, int i2) {
        if (i2 < this.sbuf.length) {
            this.text.getChars(i, i + i2, this.sbuf, 0);
            return new String(this.sbuf, 0, i2);
        }
        char[] cArr = new char[i2];
        this.text.getChars(i, i2 + i, cArr, 0);
        return new String(cArr);
    }

    /* access modifiers changed from: package-private */
    public final char[] sub_chars(int i, int i2) {
        if (i2 < this.sbuf.length) {
            this.text.getChars(i, i2 + i, this.sbuf, 0);
            return this.sbuf;
        }
        char[] cArr = new char[i2];
        this.sbuf = cArr;
        this.text.getChars(i, i2 + i, cArr, 0);
        return cArr;
    }

    public final boolean isBlankInput() {
        int i = 0;
        while (true) {
            char charAt = charAt(i);
            boolean z = true;
            if (charAt == 26) {
                return true;
            }
            if (charAt > ' ' || !(charAt == ' ' || charAt == 10 || charAt == 13 || charAt == 9 || charAt == 12 || charAt == 8)) {
                z = false;
            }
            if (!z) {
                return false;
            }
            i++;
        }
    }

    /* access modifiers changed from: package-private */
    public final void skipWhitespace() {
        while (this.ch <= '/') {
            if (this.ch == ' ' || this.ch == 13 || this.ch == 10 || this.ch == 9 || this.ch == 12 || this.ch == 8) {
                next();
            } else if (this.ch == '/') {
                skipComment();
            } else {
                return;
            }
        }
    }

    public final void scanNumber() {
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        this.np = this.bp;
        this.exp = false;
        if (this.ch == '-') {
            this.sp++;
            int i = this.bp + 1;
            this.bp = i;
            if (i >= this.len) {
                c7 = EOI;
            } else {
                c7 = this.text.charAt(i);
            }
            this.ch = c7;
        }
        while (this.ch >= '0' && this.ch <= '9') {
            this.sp++;
            int i2 = this.bp + 1;
            this.bp = i2;
            if (i2 >= this.len) {
                c6 = EOI;
            } else {
                c6 = this.text.charAt(i2);
            }
            this.ch = c6;
        }
        this.isDouble = false;
        if (this.ch == '.') {
            this.sp++;
            int i3 = this.bp + 1;
            this.bp = i3;
            if (i3 >= this.len) {
                c4 = EOI;
            } else {
                c4 = this.text.charAt(i3);
            }
            this.ch = c4;
            this.isDouble = true;
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                int i4 = this.bp + 1;
                this.bp = i4;
                if (i4 >= this.len) {
                    c5 = EOI;
                } else {
                    c5 = this.text.charAt(i4);
                }
                this.ch = c5;
            }
        }
        if (this.ch == 'L') {
            this.sp++;
            next();
        } else if (this.ch == 'S') {
            this.sp++;
            next();
        } else if (this.ch == 'B') {
            this.sp++;
            next();
        } else if (this.ch == 'F') {
            this.sp++;
            next();
            this.isDouble = true;
        } else if (this.ch == 'D') {
            this.sp++;
            next();
            this.isDouble = true;
        } else if (this.ch == 'e' || this.ch == 'E') {
            this.sp++;
            int i5 = this.bp + 1;
            this.bp = i5;
            if (i5 >= this.len) {
                c = EOI;
            } else {
                c = this.text.charAt(i5);
            }
            this.ch = c;
            if (this.ch == '+' || this.ch == '-') {
                this.sp++;
                int i6 = this.bp + 1;
                this.bp = i6;
                if (i6 >= this.len) {
                    c3 = EOI;
                } else {
                    c3 = this.text.charAt(i6);
                }
                this.ch = c3;
            }
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                int i7 = this.bp + 1;
                this.bp = i7;
                if (i7 >= this.len) {
                    c2 = EOI;
                } else {
                    c2 = this.text.charAt(i7);
                }
                this.ch = c2;
            }
            if (this.ch == 'D' || this.ch == 'F') {
                this.sp++;
                next();
            }
            this.exp = true;
            this.isDouble = true;
        }
        if (this.isDouble) {
            this.token = 3;
        } else {
            this.token = 2;
        }
    }

    public boolean scanBoolean() {
        boolean z = false;
        int i = 1;
        if (this.text.startsWith("false", this.bp)) {
            i = 5;
        } else if (this.text.startsWith("true", this.bp)) {
            z = true;
            i = 4;
        } else if (this.ch == '1') {
            z = true;
        } else if (this.ch != '0') {
            this.matchStat = -1;
            return false;
        }
        this.bp += i;
        this.ch = charAt(this.bp);
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:151:0x02a4 A[Catch:{ NumberFormatException -> 0x02ec }] */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x02ba A[Catch:{ NumberFormatException -> 0x02ec }] */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x02c5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Number scanNumberValue() {
        /*
            r21 = this;
            r1 = r21
            int r0 = r1.bp
            r2 = 0
            r1.np = r2
            char r3 = r1.ch
            r4 = 45
            r6 = 1
            if (r3 != r4) goto L_0x002b
            r7 = -9223372036854775808
            int r3 = r1.np
            int r3 = r3 + r6
            r1.np = r3
            int r3 = r1.bp
            int r3 = r3 + r6
            r1.bp = r3
            int r9 = r1.len
            if (r3 < r9) goto L_0x0021
            r3 = 26
            goto L_0x0027
        L_0x0021:
            java.lang.String r9 = r1.text
            char r3 = r9.charAt(r3)
        L_0x0027:
            r1.ch = r3
            r3 = 1
            goto L_0x0031
        L_0x002b:
            r7 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            r3 = 0
        L_0x0031:
            r9 = 0
            r10 = r9
            r9 = 1
            r12 = 0
        L_0x0036:
            char r13 = r1.ch
            r14 = -922337203685477580(0xf333333333333334, double:-8.390303882365713E246)
            r5 = 18
            r2 = 57
            r16 = 10
            r4 = 48
            if (r13 < r4) goto L_0x0085
            char r13 = r1.ch
            if (r13 > r2) goto L_0x0085
            char r2 = r1.ch
            int r2 = r2 - r4
            if (r9 >= r5) goto L_0x0055
            long r10 = r10 * r16
            long r4 = (long) r2
            long r10 = r10 - r4
            goto L_0x0066
        L_0x0055:
            int r4 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            if (r4 >= 0) goto L_0x005a
            r12 = 1
        L_0x005a:
            long r10 = r10 * r16
            long r4 = (long) r2
            long r13 = r7 + r4
            int r2 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r2 >= 0) goto L_0x0064
            r12 = 1
        L_0x0064:
            r2 = 0
            long r10 = r10 - r4
        L_0x0066:
            int r2 = r1.np
            int r2 = r2 + r6
            r1.np = r2
            int r2 = r1.bp
            int r2 = r2 + r6
            r1.bp = r2
            int r4 = r1.len
            if (r2 < r4) goto L_0x0077
            r5 = 26
            goto L_0x007d
        L_0x0077:
            java.lang.String r4 = r1.text
            char r5 = r4.charAt(r2)
        L_0x007d:
            r1.ch = r5
            int r9 = r9 + 1
            r2 = 0
            r4 = 45
            goto L_0x0036
        L_0x0085:
            char r13 = r1.ch
            r15 = 46
            r14 = 70
            r20 = 0
            if (r13 != r15) goto L_0x00fd
            int r13 = r1.np
            int r13 = r13 + r6
            r1.np = r13
            int r13 = r1.bp
            int r13 = r13 + r6
            r1.bp = r13
            int r15 = r1.len
            if (r13 < r15) goto L_0x00a0
            r13 = 26
            goto L_0x00a6
        L_0x00a0:
            java.lang.String r15 = r1.text
            char r13 = r15.charAt(r13)
        L_0x00a6:
            r1.ch = r13
            r13 = r12
            r11 = r10
            r10 = r9
            r9 = 0
        L_0x00ac:
            char r15 = r1.ch
            if (r15 < r4) goto L_0x00f6
            char r15 = r1.ch
            if (r15 > r2) goto L_0x00f6
            int r9 = r9 + 1
            char r15 = r1.ch
            int r15 = r15 - r4
            if (r10 >= r5) goto L_0x00c0
            long r11 = r11 * r16
            long r4 = (long) r15
            long r11 = r11 - r4
            goto L_0x00d6
        L_0x00c0:
            r4 = -922337203685477580(0xf333333333333334, double:-8.390303882365713E246)
            int r18 = (r11 > r4 ? 1 : (r11 == r4 ? 0 : -1))
            if (r18 >= 0) goto L_0x00ca
            r13 = 1
        L_0x00ca:
            long r11 = r11 * r16
            long r4 = (long) r15
            long r18 = r7 + r4
            int r15 = (r11 > r18 ? 1 : (r11 == r18 ? 0 : -1))
            if (r15 >= 0) goto L_0x00d4
            r13 = 1
        L_0x00d4:
            r15 = 0
            long r11 = r11 - r4
        L_0x00d6:
            int r4 = r1.np
            int r4 = r4 + r6
            r1.np = r4
            int r4 = r1.bp
            int r4 = r4 + r6
            r1.bp = r4
            int r5 = r1.len
            if (r4 < r5) goto L_0x00e7
            r5 = 26
            goto L_0x00ed
        L_0x00e7:
            java.lang.String r5 = r1.text
            char r5 = r5.charAt(r4)
        L_0x00ed:
            r1.ch = r5
            int r10 = r10 + 1
            r4 = 48
            r5 = 18
            goto L_0x00ac
        L_0x00f6:
            if (r3 != 0) goto L_0x00f9
            long r11 = -r11
        L_0x00f9:
            r10 = r11
            r12 = r13
            r4 = 1
            goto L_0x0165
        L_0x00fd:
            if (r3 != 0) goto L_0x0100
            long r10 = -r10
        L_0x0100:
            char r4 = r1.ch
            r5 = 76
            if (r4 != r5) goto L_0x0115
            int r4 = r1.np
            int r4 = r4 + r6
            r1.np = r4
            r21.next()
            java.lang.Long r20 = java.lang.Long.valueOf(r10)
        L_0x0112:
            r4 = 0
            r9 = 0
            goto L_0x0165
        L_0x0115:
            char r4 = r1.ch
            r5 = 83
            if (r4 != r5) goto L_0x012a
            int r4 = r1.np
            int r4 = r4 + r6
            r1.np = r4
            r21.next()
            int r4 = (int) r10
            short r4 = (short) r4
            java.lang.Short r20 = java.lang.Short.valueOf(r4)
            goto L_0x0112
        L_0x012a:
            char r4 = r1.ch
            r5 = 66
            if (r4 != r5) goto L_0x013f
            int r4 = r1.np
            int r4 = r4 + r6
            r1.np = r4
            r21.next()
            int r4 = (int) r10
            byte r4 = (byte) r4
            java.lang.Byte r20 = java.lang.Byte.valueOf(r4)
            goto L_0x0112
        L_0x013f:
            char r4 = r1.ch
            if (r4 != r14) goto L_0x0151
            int r4 = r1.np
            int r4 = r4 + r6
            r1.np = r4
            r21.next()
            float r4 = (float) r10
            java.lang.Float r20 = java.lang.Float.valueOf(r4)
            goto L_0x0112
        L_0x0151:
            char r4 = r1.ch
            r5 = 68
            if (r4 != r5) goto L_0x0112
            int r4 = r1.np
            int r4 = r4 + r6
            r1.np = r4
            r21.next()
            double r4 = (double) r10
            java.lang.Double r20 = java.lang.Double.valueOf(r4)
            goto L_0x0112
        L_0x0165:
            char r5 = r1.ch
            r7 = 101(0x65, float:1.42E-43)
            r8 = 43
            if (r5 == r7) goto L_0x0178
            char r5 = r1.ch
            r7 = 69
            if (r5 != r7) goto L_0x0174
            goto L_0x0178
        L_0x0174:
            r2 = 0
            r5 = 0
            goto L_0x01f1
        L_0x0178:
            int r5 = r1.np
            int r5 = r5 + r6
            r1.np = r5
            int r5 = r1.bp
            int r5 = r5 + r6
            r1.bp = r5
            int r7 = r1.len
            if (r5 < r7) goto L_0x0189
            r5 = 26
            goto L_0x018f
        L_0x0189:
            java.lang.String r7 = r1.text
            char r5 = r7.charAt(r5)
        L_0x018f:
            r1.ch = r5
            char r5 = r1.ch
            if (r5 == r8) goto L_0x019b
            char r5 = r1.ch
            r7 = 45
            if (r5 != r7) goto L_0x01b4
        L_0x019b:
            int r5 = r1.np
            int r5 = r5 + r6
            r1.np = r5
            int r5 = r1.bp
            int r5 = r5 + r6
            r1.bp = r5
            int r7 = r1.len
            if (r5 < r7) goto L_0x01ac
            r5 = 26
            goto L_0x01b2
        L_0x01ac:
            java.lang.String r7 = r1.text
            char r5 = r7.charAt(r5)
        L_0x01b2:
            r1.ch = r5
        L_0x01b4:
            char r5 = r1.ch
            r7 = 48
            if (r5 < r7) goto L_0x01d8
            char r5 = r1.ch
            if (r5 > r2) goto L_0x01d8
            int r5 = r1.np
            int r5 = r5 + r6
            r1.np = r5
            int r5 = r1.bp
            int r5 = r5 + r6
            r1.bp = r5
            int r7 = r1.len
            if (r5 < r7) goto L_0x01cf
            r5 = 26
            goto L_0x01d5
        L_0x01cf:
            java.lang.String r7 = r1.text
            char r5 = r7.charAt(r5)
        L_0x01d5:
            r1.ch = r5
            goto L_0x01b4
        L_0x01d8:
            char r2 = r1.ch
            r5 = 68
            if (r2 == r5) goto L_0x01e5
            char r2 = r1.ch
            if (r2 != r14) goto L_0x01e3
            goto L_0x01e5
        L_0x01e3:
            r2 = 0
            goto L_0x01ef
        L_0x01e5:
            int r2 = r1.np
            int r2 = r2 + r6
            r1.np = r2
            char r2 = r1.ch
            r21.next()
        L_0x01ef:
            r5 = r2
            r2 = 1
        L_0x01f1:
            if (r4 != 0) goto L_0x022c
            if (r2 != 0) goto L_0x022c
            if (r12 == 0) goto L_0x020f
            int r2 = r1.bp
            int r2 = r2 - r0
            char[] r2 = new char[r2]
            java.lang.String r3 = r1.text
            int r4 = r1.bp
            r5 = 0
            r3.getChars(r0, r4, r2, r5)
            java.lang.String r0 = new java.lang.String
            r0.<init>(r2)
            java.math.BigInteger r2 = new java.math.BigInteger
            r2.<init>(r0)
            goto L_0x0211
        L_0x020f:
            r2 = r20
        L_0x0211:
            if (r2 != 0) goto L_0x022b
            r2 = -2147483648(0xffffffff80000000, double:NaN)
            int r0 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r0 <= 0) goto L_0x0227
            r2 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r0 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r0 >= 0) goto L_0x0227
            int r0 = (int) r10
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)
            goto L_0x022b
        L_0x0227:
            java.lang.Long r2 = java.lang.Long.valueOf(r10)
        L_0x022b:
            return r2
        L_0x022c:
            int r4 = r1.bp
            int r4 = r4 - r0
            if (r5 == 0) goto L_0x0233
            int r4 = r4 + -1
        L_0x0233:
            if (r2 != 0) goto L_0x026b
            int r7 = r1.features
            com.alibaba.fastjson.parser.Feature r13 = com.alibaba.fastjson.parser.Feature.UseBigDecimal
            int r13 = r13.mask
            r7 = r7 & r13
            if (r7 == 0) goto L_0x026b
            if (r12 != 0) goto L_0x0246
            java.math.BigDecimal r0 = java.math.BigDecimal.valueOf(r10, r9)
            goto L_0x02eb
        L_0x0246:
            char[] r2 = r1.sbuf
            int r2 = r2.length
            if (r4 >= r2) goto L_0x0258
            java.lang.String r2 = r1.text
            int r3 = r0 + r4
            char[] r5 = r1.sbuf
            r7 = 0
            r2.getChars(r0, r3, r5, r7)
            char[] r0 = r1.sbuf
            goto L_0x0263
        L_0x0258:
            r7 = 0
            char[] r2 = new char[r4]
            java.lang.String r3 = r1.text
            int r5 = r0 + r4
            r3.getChars(r0, r5, r2, r7)
            r0 = r2
        L_0x0263:
            java.math.BigDecimal r2 = new java.math.BigDecimal
            r2.<init>(r0, r7, r4)
            r0 = r2
            goto L_0x02eb
        L_0x026b:
            r7 = 0
            char[] r9 = r1.sbuf
            int r9 = r9.length
            if (r4 >= r9) goto L_0x027d
            java.lang.String r9 = r1.text
            int r10 = r0 + r4
            char[] r11 = r1.sbuf
            r9.getChars(r0, r10, r11, r7)
            char[] r0 = r1.sbuf
            goto L_0x0287
        L_0x027d:
            char[] r9 = new char[r4]
            java.lang.String r10 = r1.text
            int r11 = r0 + r4
            r10.getChars(r0, r11, r9, r7)
            r0 = r9
        L_0x0287:
            r9 = 9
            if (r4 > r9) goto L_0x02d6
            if (r2 != 0) goto L_0x02d6
            char r2 = r0[r7]     // Catch:{ NumberFormatException -> 0x02ec }
            r7 = 45
            if (r2 == r7) goto L_0x029a
            if (r2 != r8) goto L_0x0296
            goto L_0x029a
        L_0x0296:
            r7 = 1
        L_0x0297:
            r8 = 48
            goto L_0x02a0
        L_0x029a:
            r2 = 2
            char r7 = r0[r6]     // Catch:{ NumberFormatException -> 0x02ec }
            r2 = r7
            r7 = 2
            goto L_0x0297
        L_0x02a0:
            int r2 = r2 - r8
            r8 = 0
        L_0x02a2:
            if (r7 >= r4) goto L_0x02b8
            char r9 = r0[r7]     // Catch:{ NumberFormatException -> 0x02ec }
            r10 = 46
            if (r9 != r10) goto L_0x02ac
            r8 = 1
            goto L_0x02b5
        L_0x02ac:
            int r9 = r9 + -48
            int r2 = r2 * 10
            int r2 = r2 + r9
            if (r8 == 0) goto L_0x02b5
            int r8 = r8 * 10
        L_0x02b5:
            int r7 = r7 + 1
            goto L_0x02a2
        L_0x02b8:
            if (r5 != r14) goto L_0x02c5
            float r0 = (float) r2     // Catch:{ NumberFormatException -> 0x02ec }
            float r2 = (float) r8     // Catch:{ NumberFormatException -> 0x02ec }
            float r0 = r0 / r2
            if (r3 == 0) goto L_0x02c0
            float r0 = -r0
        L_0x02c0:
            java.lang.Float r0 = java.lang.Float.valueOf(r0)     // Catch:{ NumberFormatException -> 0x02ec }
            return r0
        L_0x02c5:
            double r4 = (double) r2
            double r6 = (double) r8
            java.lang.Double.isNaN(r4)
            java.lang.Double.isNaN(r6)
            double r4 = r4 / r6
            if (r3 == 0) goto L_0x02d1
            double r4 = -r4
        L_0x02d1:
            java.lang.Double r0 = java.lang.Double.valueOf(r4)     // Catch:{ NumberFormatException -> 0x02ec }
            return r0
        L_0x02d6:
            java.lang.String r2 = new java.lang.String     // Catch:{ NumberFormatException -> 0x02ec }
            r3 = 0
            r2.<init>(r0, r3, r4)     // Catch:{ NumberFormatException -> 0x02ec }
            if (r5 != r14) goto L_0x02e3
            java.lang.Float r0 = java.lang.Float.valueOf(r2)     // Catch:{ NumberFormatException -> 0x02ec }
            goto L_0x02eb
        L_0x02e3:
            double r2 = java.lang.Double.parseDouble(r2)     // Catch:{ NumberFormatException -> 0x02ec }
            java.lang.Double r0 = java.lang.Double.valueOf(r2)     // Catch:{ NumberFormatException -> 0x02ec }
        L_0x02eb:
            return r0
        L_0x02ec:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = r0.getMessage()
            r3.append(r4)
            java.lang.String r4 = ", "
            r3.append(r4)
            java.lang.String r4 = r21.info()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3, r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanNumberValue():java.lang.Number");
    }

    public final long scanLongValue() {
        boolean z;
        long j;
        char c;
        this.np = 0;
        if (this.ch == '-') {
            j = Long.MIN_VALUE;
            this.np++;
            int i = this.bp + 1;
            this.bp = i;
            if (i < this.len) {
                this.ch = this.text.charAt(i);
                z = true;
            } else {
                throw new JSONException("syntax error, " + info());
            }
        } else {
            j = -9223372036854775807L;
            z = false;
        }
        long j2 = 0;
        while (this.ch >= '0' && this.ch <= '9') {
            int i2 = this.ch - '0';
            if (j2 >= -922337203685477580L) {
                long j3 = j2 * 10;
                long j4 = (long) i2;
                if (j3 >= j + j4) {
                    j2 = j3 - j4;
                    this.np++;
                    int i3 = this.bp + 1;
                    this.bp = i3;
                    if (i3 >= this.len) {
                        c = EOI;
                    } else {
                        c = this.text.charAt(i3);
                    }
                    this.ch = c;
                } else {
                    throw new JSONException("error long value, " + j3 + AVFSCacheConstants.COMMA_SEP + info());
                }
            } else {
                throw new JSONException("error long value, " + j2 + AVFSCacheConstants.COMMA_SEP + info());
            }
        }
        return !z ? -j2 : j2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final long longValue() throws java.lang.NumberFormatException {
        /*
            r13 = this;
            int r0 = r13.np
            int r1 = r13.np
            int r2 = r13.sp
            int r1 = r1 + r2
            int r2 = r13.np
            char r2 = r13.charAt(r2)
            r3 = 1
            r4 = 45
            if (r2 != r4) goto L_0x0018
            r4 = -9223372036854775808
            int r0 = r0 + 1
            r2 = 1
            goto L_0x001e
        L_0x0018:
            r4 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            r2 = 0
        L_0x001e:
            if (r0 >= r1) goto L_0x002c
            int r6 = r0 + 1
            char r0 = r13.charAt(r0)
            int r0 = r0 + -48
            int r0 = -r0
            long r7 = (long) r0
        L_0x002a:
            r0 = r6
            goto L_0x002e
        L_0x002c:
            r7 = 0
        L_0x002e:
            if (r0 >= r1) goto L_0x0079
            int r6 = r0 + 1
            int r9 = r13.len
            if (r0 < r9) goto L_0x0039
            r0 = 26
            goto L_0x003f
        L_0x0039:
            java.lang.String r9 = r13.text
            char r0 = r9.charAt(r0)
        L_0x003f:
            r9 = 76
            if (r0 == r9) goto L_0x0078
            r9 = 83
            if (r0 == r9) goto L_0x0078
            r9 = 66
            if (r0 != r9) goto L_0x004c
            goto L_0x0078
        L_0x004c:
            int r0 = r0 + -48
            r9 = -922337203685477580(0xf333333333333334, double:-8.390303882365713E246)
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 < 0) goto L_0x006e
            r9 = 10
            long r7 = r7 * r9
            long r9 = (long) r0
            long r11 = r4 + r9
            int r0 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
            if (r0 < 0) goto L_0x0064
            long r7 = r7 - r9
            goto L_0x002a
        L_0x0064:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r13.numberString()
            r0.<init>(r1)
            throw r0
        L_0x006e:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r13.numberString()
            r0.<init>(r1)
            throw r0
        L_0x0078:
            r0 = r6
        L_0x0079:
            if (r2 == 0) goto L_0x008b
            int r1 = r13.np
            int r1 = r1 + r3
            if (r0 <= r1) goto L_0x0081
            return r7
        L_0x0081:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r13.numberString()
            r0.<init>(r1)
            throw r0
        L_0x008b:
            long r0 = -r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.longValue():long");
    }

    public final Number decimalValue(boolean z) {
        char c;
        char[] cArr;
        boolean z2;
        int i = (this.np + this.sp) - 1;
        if (i >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i);
        }
        if (c == 'F') {
            try {
                return Float.valueOf(Float.parseFloat(numberString()));
            } catch (NumberFormatException e) {
                throw new JSONException(e.getMessage() + AVFSCacheConstants.COMMA_SEP + info());
            }
        } else if (c == 'D') {
            return Double.valueOf(Double.parseDouble(numberString()));
        } else {
            if (z) {
                return decimalValue();
            }
            char charAt = this.text.charAt((this.np + this.sp) - 1);
            int i2 = this.sp;
            if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
                i2--;
            }
            int i3 = this.np;
            int i4 = 0;
            if (i2 < this.sbuf.length) {
                this.text.getChars(i3, i3 + i2, this.sbuf, 0);
                cArr = this.sbuf;
            } else {
                char[] cArr2 = new char[i2];
                this.text.getChars(i3, i3 + i2, cArr2, 0);
                cArr = cArr2;
            }
            if (i2 > 9 || this.exp) {
                return Double.valueOf(Double.parseDouble(new String(cArr, 0, i2)));
            }
            char c2 = cArr[0];
            int i5 = 2;
            if (c2 == '-') {
                c2 = cArr[1];
                z2 = true;
            } else if (c2 == '+') {
                c2 = cArr[1];
                z2 = false;
            } else {
                z2 = false;
                i5 = 1;
            }
            int i6 = c2 - '0';
            while (i5 < i2) {
                char c3 = cArr[i5];
                if (c3 == '.') {
                    i4 = 1;
                } else {
                    i6 = (i6 * 10) + (c3 - '0');
                    if (i4 != 0) {
                        i4 *= 10;
                    }
                }
                i5++;
            }
            double d = (double) i6;
            double d2 = (double) i4;
            Double.isNaN(d);
            Double.isNaN(d2);
            double d3 = d / d2;
            if (z2) {
                d3 = -d3;
            }
            return Double.valueOf(d3);
        }
    }

    public final BigDecimal decimalValue() {
        char charAt = this.text.charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        int i2 = this.np;
        if (i < this.sbuf.length) {
            this.text.getChars(i2, i2 + i, this.sbuf, 0);
            return new BigDecimal(this.sbuf, 0, i);
        }
        char[] cArr = new char[i];
        this.text.getChars(i2, i + i2, cArr, 0);
        return new BigDecimal(cArr);
    }

    public boolean matchField(long j) {
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        char charAt;
        char c6 = this.ch;
        int i = this.bp + 1;
        int i2 = 1;
        while (c6 != '\"' && c6 != '\'') {
            if (c6 > ' ' || !(c6 == ' ' || c6 == 10 || c6 == 13 || c6 == 9 || c6 == 12 || c6 == 8)) {
                this.fieldHash = 0;
                this.matchStat = -2;
                return false;
            }
            int i3 = i2 + 1;
            int i4 = this.bp + i2;
            if (i4 >= this.len) {
                c6 = EOI;
            } else {
                c6 = this.text.charAt(i4);
            }
            i2 = i3;
        }
        int i5 = i;
        long j2 = -3750763034362895579L;
        while (true) {
            if (i5 >= this.len) {
                break;
            }
            char charAt2 = this.text.charAt(i5);
            if (charAt2 == c6) {
                i2 += (i5 - i) + 1;
                break;
            }
            j2 = 1099511628211L * (((long) charAt2) ^ j2);
            i5++;
        }
        if (j2 != j) {
            this.matchStat = -2;
            this.fieldHash = j2;
            return false;
        }
        int i6 = i2 + 1;
        int i7 = this.bp + i2;
        if (i7 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i7);
        }
        while (c != ':') {
            if (c > ' ' || !(c == ' ' || c == 10 || c == 13 || c == 9 || c == 12 || c == 8)) {
                throw new JSONException("match feild error expect ':'");
            }
            int i8 = i6 + 1;
            int i9 = this.bp + i6;
            if (i9 >= this.len) {
                charAt = EOI;
            } else {
                charAt = this.text.charAt(i9);
            }
            i6 = i8;
        }
        int i10 = this.bp + i6;
        if (i10 >= this.len) {
            c2 = EOI;
        } else {
            c2 = this.text.charAt(i10);
        }
        if (c2 == '{') {
            this.bp = i10 + 1;
            if (this.bp >= this.len) {
                c5 = EOI;
            } else {
                c5 = this.text.charAt(this.bp);
            }
            this.ch = c5;
            this.token = 12;
        } else if (c2 == '[') {
            this.bp = i10 + 1;
            if (this.bp >= this.len) {
                c4 = EOI;
            } else {
                c4 = this.text.charAt(this.bp);
            }
            this.ch = c4;
            this.token = 14;
        } else {
            this.bp = i10;
            if (this.bp >= this.len) {
                c3 = EOI;
            } else {
                c3 = this.text.charAt(this.bp);
            }
            this.ch = c3;
            nextToken();
        }
        return true;
    }

    private int matchFieldHash(long j) {
        char c;
        char charAt;
        char c2 = this.ch;
        int i = this.bp;
        int i2 = 1;
        while (c2 != '\"' && c2 != '\'') {
            if (c2 == ' ' || c2 == 10 || c2 == 13 || c2 == 9 || c2 == 12 || c2 == 8) {
                int i3 = i2 + 1;
                int i4 = this.bp + i2;
                if (i4 >= this.len) {
                    c2 = EOI;
                } else {
                    c2 = this.text.charAt(i4);
                }
                i2 = i3;
            } else {
                this.fieldHash = 0;
                this.matchStat = -2;
                return 0;
            }
        }
        long j2 = -3750763034362895579L;
        int i5 = this.bp + i2;
        while (true) {
            if (i5 >= this.len) {
                break;
            }
            char charAt2 = this.text.charAt(i5);
            if (charAt2 == c2) {
                i2 += (i5 - this.bp) - i2;
                break;
            }
            j2 = 1099511628211L * (((long) charAt2) ^ j2);
            i5++;
        }
        if (j2 != j) {
            this.fieldHash = j2;
            this.matchStat = -2;
            return 0;
        }
        int i6 = i2 + 1;
        int i7 = this.bp + i6;
        if (i7 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i7);
        }
        while (c != ':') {
            if (c > ' ' || !(c == ' ' || c == 10 || c == 13 || c == 9 || c == 12 || c == 8)) {
                throw new JSONException("match feild error expect ':'");
            }
            int i8 = i6 + 1;
            int i9 = this.bp + i6;
            if (i9 >= this.len) {
                charAt = EOI;
            } else {
                charAt = this.text.charAt(i9);
            }
            i6 = i8;
        }
        return i6 + 1;
    }

    public int scanFieldInt(long j) {
        char c;
        int i;
        char c2;
        char c3;
        int i2;
        char charAt;
        char charAt2;
        char charAt3;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0;
        }
        int i3 = matchFieldHash + 1;
        int i4 = this.bp + matchFieldHash;
        int i5 = this.len;
        char c4 = EOI;
        if (i4 >= i5) {
            c = EOI;
        } else {
            c = this.text.charAt(i4);
        }
        boolean z = c == '\"';
        if (z) {
            int i6 = i3 + 1;
            int i7 = this.bp + i3;
            if (i7 >= this.len) {
                charAt3 = EOI;
            } else {
                charAt3 = this.text.charAt(i7);
            }
            i3 = i6;
            z = true;
        }
        boolean z2 = c == '-';
        if (z2) {
            int i8 = i3 + 1;
            int i9 = this.bp + i3;
            if (i9 >= this.len) {
                charAt2 = EOI;
            } else {
                charAt2 = this.text.charAt(i9);
            }
            i3 = i8;
        }
        if (c < '0' || c > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i10 = c - '0';
        while (true) {
            i = i3 + 1;
            int i11 = this.bp + i3;
            if (i11 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i11);
            }
            if (c2 >= '0' && c2 <= '9') {
                i10 = (i10 * 10) + (c2 - '0');
                i3 = i;
            }
        }
        if (c2 == '.') {
            this.matchStat = -1;
            return 0;
        }
        if (c2 != '\"') {
            c3 = c2;
            i2 = i;
        } else if (!z) {
            this.matchStat = -1;
            return 0;
        } else {
            i2 = i + 1;
            int i12 = this.bp + i;
            c3 = i12 >= this.len ? EOI : this.text.charAt(i12);
        }
        if (i10 < 0) {
            this.matchStat = -1;
            return 0;
        }
        while (c3 != ',') {
            if (c3 <= ' ' && (c3 == ' ' || c3 == 10 || c3 == 13 || c3 == 9 || c3 == 12 || c3 == 8)) {
                int i13 = i2 + 1;
                int i14 = this.bp + i2;
                if (i14 >= this.len) {
                    charAt = EOI;
                } else {
                    charAt = this.text.charAt(i14);
                }
                i2 = i13;
            } else if (c3 == '}') {
                int i15 = i2 + 1;
                char charAt4 = charAt(this.bp + i2);
                if (charAt4 == ',') {
                    this.token = 16;
                    this.bp += i15 - 1;
                    int i16 = this.bp + 1;
                    this.bp = i16;
                    if (i16 < this.len) {
                        c4 = this.text.charAt(i16);
                    }
                    this.ch = c4;
                } else if (charAt4 == ']') {
                    this.token = 15;
                    this.bp += i15 - 1;
                    int i17 = this.bp + 1;
                    this.bp = i17;
                    if (i17 < this.len) {
                        c4 = this.text.charAt(i17);
                    }
                    this.ch = c4;
                } else if (charAt4 == '}') {
                    this.token = 13;
                    this.bp += i15 - 1;
                    int i18 = this.bp + 1;
                    this.bp = i18;
                    if (i18 < this.len) {
                        c4 = this.text.charAt(i18);
                    }
                    this.ch = c4;
                } else if (charAt4 == 26) {
                    this.token = 20;
                    this.bp += i15 - 1;
                    this.ch = EOI;
                } else {
                    this.matchStat = -1;
                    return 0;
                }
                this.matchStat = 4;
                return z2 ? -i10 : i10;
            } else {
                this.matchStat = -1;
                return 0;
            }
        }
        this.bp += i2 - 1;
        int i19 = this.bp + 1;
        this.bp = i19;
        if (i19 < this.len) {
            c4 = this.text.charAt(i19);
        }
        this.ch = c4;
        this.matchStat = 3;
        this.token = 16;
        return z2 ? -i10 : i10;
    }

    public final int[] scanFieldIntArray(long j) {
        char c;
        char c2;
        int[] iArr;
        int i;
        int i2;
        char c3;
        int i3;
        boolean z;
        int[] iArr2;
        int i4;
        int i5;
        char c4;
        char c5;
        char charAt;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        int[] iArr3 = null;
        if (matchFieldHash == 0) {
            return null;
        }
        int i6 = matchFieldHash + 1;
        int i7 = this.bp + matchFieldHash;
        if (i7 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i7);
        }
        if (c != '[') {
            this.matchStat = -1;
            return null;
        }
        int i8 = i6 + 1;
        int i9 = this.bp + i6;
        if (i9 >= this.len) {
            c2 = EOI;
        } else {
            c2 = this.text.charAt(i9);
        }
        int[] iArr4 = new int[16];
        if (c2 == ']') {
            int i10 = i8 + 1;
            int i11 = this.bp + i8;
            if (i11 >= this.len) {
                c3 = EOI;
            } else {
                c3 = this.text.charAt(i11);
            }
            i = i10;
            i2 = 0;
            iArr = iArr4;
        } else {
            iArr = iArr4;
            int i12 = 0;
            while (true) {
                if (c2 == '-') {
                    i3 = i8 + 1;
                    int i13 = this.bp + i8;
                    if (i13 >= this.len) {
                        charAt = EOI;
                    } else {
                        charAt = this.text.charAt(i13);
                    }
                    z = true;
                } else {
                    i3 = i8;
                    z = false;
                }
                if (c2 >= '0') {
                    if (c2 > '9') {
                        i4 = -1;
                        iArr2 = null;
                        break;
                    }
                    int i14 = c2 - '0';
                    while (true) {
                        i5 = i3 + 1;
                        int i15 = this.bp + i3;
                        if (i15 >= this.len) {
                            c4 = EOI;
                        } else {
                            c4 = this.text.charAt(i15);
                        }
                        if (c4 >= '0' && c4 <= '9') {
                            i14 = (i14 * 10) + (c4 - '0');
                            i3 = i5;
                        }
                    }
                    if (i12 >= iArr.length) {
                        int[] iArr5 = new int[((iArr.length * 3) / 2)];
                        System.arraycopy(iArr, 0, iArr5, 0, i12);
                        iArr = iArr5;
                    }
                    i2 = i12 + 1;
                    if (z) {
                        i14 = -i14;
                    }
                    iArr[i12] = i14;
                    if (c4 == ',') {
                        int i16 = i5 + 1;
                        int i17 = this.bp + i5;
                        if (i17 >= this.len) {
                            c5 = EOI;
                        } else {
                            c5 = this.text.charAt(i17);
                        }
                        c4 = c5;
                        i5 = i16;
                    } else if (c4 == ']') {
                        i = i5 + 1;
                        int i18 = this.bp + i5;
                        if (i18 >= this.len) {
                            c3 = EOI;
                        } else {
                            c3 = this.text.charAt(i18);
                        }
                    }
                    i12 = i2;
                    iArr3 = null;
                    c2 = c4;
                    i8 = i5;
                } else {
                    iArr2 = iArr3;
                    i4 = -1;
                    break;
                }
            }
            this.matchStat = i4;
            return iArr2;
        }
        if (i2 != iArr.length) {
            int[] iArr6 = new int[i2];
            System.arraycopy(iArr, 0, iArr6, 0, i2);
            iArr = iArr6;
        }
        if (c3 == ',') {
            this.bp += i - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return iArr;
        } else if (c3 == '}') {
            int i19 = i + 1;
            char charAt2 = charAt(this.bp + i);
            if (charAt2 == ',') {
                this.token = 16;
                this.bp += i19 - 1;
                next();
            } else if (charAt2 == ']') {
                this.token = 15;
                this.bp += i19 - 1;
                next();
            } else if (charAt2 == '}') {
                this.token = 13;
                this.bp += i19 - 1;
                next();
            } else if (charAt2 == 26) {
                this.bp += i19 - 1;
                this.token = 20;
                this.ch = EOI;
            } else {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return iArr;
        } else {
            this.matchStat = -1;
            return null;
        }
    }

    public long scanFieldLong(long j) {
        char c;
        int i;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char charAt;
        char charAt2;
        char charAt3;
        boolean z = false;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0;
        }
        int i2 = matchFieldHash + 1;
        int i3 = this.bp + matchFieldHash;
        if (i3 >= this.len) {
            c = EOI;
        } else {
            c = this.text.charAt(i3);
        }
        boolean z2 = c == '\"';
        if (z2) {
            int i4 = i2 + 1;
            int i5 = this.bp + i2;
            if (i5 >= this.len) {
                charAt3 = EOI;
            } else {
                charAt3 = this.text.charAt(i5);
            }
            i2 = i4;
        }
        if (c == '-') {
            z = true;
        }
        if (z) {
            int i6 = i2 + 1;
            int i7 = this.bp + i2;
            if (i7 >= this.len) {
                charAt2 = EOI;
            } else {
                charAt2 = this.text.charAt(i7);
            }
            i2 = i6;
        }
        if (c < '0' || c > '9') {
            this.matchStat = -1;
            return 0;
        }
        long j2 = (long) (c - '0');
        while (true) {
            i = i2 + 1;
            int i8 = this.bp + i2;
            if (i8 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i8);
            }
            if (c2 >= '0' && c2 <= '9') {
                j2 = (j2 * 10) + ((long) (c2 - '0'));
                i2 = i;
            }
        }
        if (c2 == '.') {
            this.matchStat = -1;
            return 0;
        }
        if (c2 == '\"') {
            if (!z2) {
                this.matchStat = -1;
                return 0;
            }
            int i9 = i + 1;
            int i10 = this.bp + i;
            if (i10 >= this.len) {
                charAt = EOI;
            } else {
                charAt = this.text.charAt(i10);
            }
            i = i9;
        }
        if (j2 < 0) {
            this.matchStat = -1;
            return 0;
        } else if (c2 == ',') {
            this.bp += i - 1;
            int i11 = this.bp + 1;
            this.bp = i11;
            if (i11 >= this.len) {
                c6 = EOI;
            } else {
                c6 = this.text.charAt(i11);
            }
            this.ch = c6;
            this.matchStat = 3;
            this.token = 16;
            return z ? -j2 : j2;
        } else if (c2 == '}') {
            int i12 = i + 1;
            char charAt4 = charAt(this.bp + i);
            if (charAt4 == ',') {
                this.token = 16;
                this.bp += i12 - 1;
                int i13 = this.bp + 1;
                this.bp = i13;
                if (i13 >= this.len) {
                    c5 = EOI;
                } else {
                    c5 = this.text.charAt(i13);
                }
                this.ch = c5;
            } else if (charAt4 == ']') {
                this.token = 15;
                this.bp += i12 - 1;
                int i14 = this.bp + 1;
                this.bp = i14;
                if (i14 >= this.len) {
                    c4 = EOI;
                } else {
                    c4 = this.text.charAt(i14);
                }
                this.ch = c4;
            } else if (charAt4 == '}') {
                this.token = 13;
                this.bp += i12 - 1;
                int i15 = this.bp + 1;
                this.bp = i15;
                if (i15 >= this.len) {
                    c3 = EOI;
                } else {
                    c3 = this.text.charAt(i15);
                }
                this.ch = c3;
            } else if (charAt4 == 26) {
                this.token = 20;
                this.bp += i12 - 1;
                this.ch = EOI;
            } else {
                this.matchStat = -1;
                return 0;
            }
            this.matchStat = 4;
            return z ? -j2 : j2;
        } else {
            this.matchStat = -1;
            return 0;
        }
    }

    public String scanFieldString(long j) {
        String str;
        char c;
        char c2;
        boolean z;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return null;
        }
        int i = matchFieldHash + 1;
        int i2 = this.bp + matchFieldHash;
        if (i2 >= this.len) {
            throw new JSONException("unclosed str, " + info());
        } else if (this.text.charAt(i2) != '\"') {
            this.matchStat = -1;
            return this.stringDefaultValue;
        } else {
            int i3 = this.bp + i;
            int indexOf = this.text.indexOf(34, i3);
            if (indexOf != -1) {
                if (V6) {
                    str = this.text.substring(i3, indexOf);
                } else {
                    int i4 = indexOf - i3;
                    str = new String(sub_chars(this.bp + i, i4), 0, i4);
                }
                if (str.indexOf(92) != -1) {
                    boolean z2 = false;
                    while (true) {
                        int i5 = indexOf - 1;
                        z = z2;
                        int i6 = 0;
                        while (i5 >= 0 && this.text.charAt(i5) == '\\') {
                            i6++;
                            i5--;
                            z = true;
                        }
                        if (i6 % 2 == 0) {
                            break;
                        }
                        indexOf = this.text.indexOf(34, indexOf + 1);
                        z2 = z;
                    }
                    int i7 = indexOf - i3;
                    char[] sub_chars = sub_chars(this.bp + i, i7);
                    if (z) {
                        str = readString(sub_chars, i7);
                    } else {
                        str = new String(sub_chars, 0, i7);
                        if (str.indexOf(92) != -1) {
                            str = readString(sub_chars, i7);
                        }
                    }
                }
                int i8 = indexOf + 1;
                int i9 = this.len;
                char c3 = EOI;
                if (i8 >= i9) {
                    c = EOI;
                } else {
                    c = this.text.charAt(i8);
                }
                if (c == ',') {
                    this.bp = i8;
                    int i10 = this.bp + 1;
                    this.bp = i10;
                    if (i10 < this.len) {
                        c3 = this.text.charAt(i10);
                    }
                    this.ch = c3;
                    this.matchStat = 3;
                    this.token = 16;
                    return str;
                } else if (c == '}') {
                    int i11 = i8 + 1;
                    if (i11 >= this.len) {
                        c2 = EOI;
                    } else {
                        c2 = this.text.charAt(i11);
                    }
                    if (c2 == ',') {
                        this.token = 16;
                        this.bp = i11;
                        next();
                    } else if (c2 == ']') {
                        this.token = 15;
                        this.bp = i11;
                        next();
                    } else if (c2 == '}') {
                        this.token = 13;
                        this.bp = i11;
                        next();
                    } else if (c2 == 26) {
                        this.token = 20;
                        this.bp = i11;
                        this.ch = EOI;
                    } else {
                        this.matchStat = -1;
                        return this.stringDefaultValue;
                    }
                    this.matchStat = 4;
                    return str;
                } else {
                    this.matchStat = -1;
                    return this.stringDefaultValue;
                }
            } else {
                throw new JSONException("unclosed str, " + info());
            }
        }
    }

    public Date scanFieldDate(long j) {
        char c;
        Date date;
        char c2;
        int i;
        int i2;
        char c3;
        char c4;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return null;
        }
        int i3 = this.bp;
        char c5 = this.ch;
        int i4 = matchFieldHash + 1;
        int i5 = this.bp + matchFieldHash;
        int i6 = this.len;
        char c6 = EOI;
        if (i5 >= i6) {
            c = EOI;
        } else {
            c = this.text.charAt(i5);
        }
        if (c == '\"') {
            int i7 = this.bp + i4;
            int i8 = i4 + 1;
            int i9 = this.bp + i4;
            if (i9 < this.len) {
                this.text.charAt(i9);
            }
            int indexOf = this.text.indexOf(34, this.bp + i8);
            if (indexOf != -1) {
                int i10 = indexOf - i7;
                this.bp = i7;
                if (scanISO8601DateIfMatch(false, i10)) {
                    date = this.calendar.getTime();
                    int i11 = i8 + i10;
                    i = i11 + 1;
                    c2 = charAt(i11 + i3);
                    this.bp = i3;
                } else {
                    this.bp = i3;
                    this.matchStat = -1;
                    return null;
                }
            } else {
                throw new JSONException("unclosed str");
            }
        } else if (c < '0' || c > '9') {
            this.matchStat = -1;
            return null;
        } else {
            long j2 = (long) (c - '0');
            while (true) {
                i2 = i4 + 1;
                int i12 = this.bp + i4;
                if (i12 >= this.len) {
                    c3 = EOI;
                } else {
                    c3 = this.text.charAt(i12);
                }
                if (c3 >= '0' && c3 <= '9') {
                    j2 = (j2 * 10) + ((long) (c3 - '0'));
                    i4 = i2;
                }
            }
            if (c3 == '.') {
                this.matchStat = -1;
                return null;
            }
            if (c3 == '\"') {
                i = i2 + 1;
                int i13 = this.bp + i2;
                if (i13 >= this.len) {
                    c4 = EOI;
                } else {
                    c4 = this.text.charAt(i13);
                }
                c2 = c4;
            } else {
                c2 = c3;
                i = i2;
            }
            if (j2 < 0) {
                this.matchStat = -1;
                return null;
            }
            date = new Date(j2);
        }
        if (c2 == ',') {
            this.bp += i - 1;
            int i14 = this.bp + 1;
            this.bp = i14;
            if (i14 < this.len) {
                c6 = this.text.charAt(i14);
            }
            this.ch = c6;
            this.matchStat = 3;
            this.token = 16;
            return date;
        } else if (c2 == '}') {
            int i15 = i + 1;
            char charAt = charAt(this.bp + i);
            if (charAt == ',') {
                this.token = 16;
                this.bp += i15 - 1;
                int i16 = this.bp + 1;
                this.bp = i16;
                if (i16 < this.len) {
                    c6 = this.text.charAt(i16);
                }
                this.ch = c6;
            } else if (charAt == ']') {
                this.token = 15;
                this.bp += i15 - 1;
                int i17 = this.bp + 1;
                this.bp = i17;
                if (i17 < this.len) {
                    c6 = this.text.charAt(i17);
                }
                this.ch = c6;
            } else if (charAt == '}') {
                this.token = 13;
                this.bp += i15 - 1;
                int i18 = this.bp + 1;
                this.bp = i18;
                if (i18 < this.len) {
                    c6 = this.text.charAt(i18);
                }
                this.ch = c6;
            } else if (charAt == 26) {
                this.token = 20;
                this.bp += i15 - 1;
                this.ch = EOI;
            } else {
                this.bp = i3;
                this.ch = c5;
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 4;
            return date;
        } else {
            this.bp = i3;
            this.ch = c5;
            this.matchStat = -1;
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanFieldBoolean(long r13) {
        /*
            r12 = this;
            r0 = 0
            r12.matchStat = r0
            int r13 = r12.matchFieldHash(r13)
            if (r13 != 0) goto L_0x000a
            return r0
        L_0x000a:
            java.lang.String r14 = r12.text
            java.lang.String r1 = "false"
            int r2 = r12.bp
            int r2 = r2 + r13
            boolean r14 = r14.startsWith(r1, r2)
            r1 = 4
            r2 = -1
            r3 = 3
            r4 = 1
            if (r14 == 0) goto L_0x0020
            int r13 = r13 + 5
        L_0x001d:
            r14 = 0
            goto L_0x008c
        L_0x0020:
            java.lang.String r14 = r12.text
            java.lang.String r5 = "true"
            int r6 = r12.bp
            int r6 = r6 + r13
            boolean r14 = r14.startsWith(r5, r6)
            if (r14 == 0) goto L_0x0030
            int r13 = r13 + r1
        L_0x002e:
            r14 = 1
            goto L_0x008c
        L_0x0030:
            java.lang.String r14 = r12.text
            java.lang.String r5 = "\"false\""
            int r6 = r12.bp
            int r6 = r6 + r13
            boolean r14 = r14.startsWith(r5, r6)
            if (r14 == 0) goto L_0x0040
            int r13 = r13 + 7
            goto L_0x001d
        L_0x0040:
            java.lang.String r14 = r12.text
            java.lang.String r5 = "\"true\""
            int r6 = r12.bp
            int r6 = r6 + r13
            boolean r14 = r14.startsWith(r5, r6)
            if (r14 == 0) goto L_0x0050
            int r13 = r13 + 6
            goto L_0x002e
        L_0x0050:
            java.lang.String r14 = r12.text
            int r5 = r12.bp
            int r5 = r5 + r13
            char r14 = r14.charAt(r5)
            r5 = 49
            if (r14 != r5) goto L_0x005f
            int r13 = r13 + r4
            goto L_0x002e
        L_0x005f:
            java.lang.String r14 = r12.text
            int r5 = r12.bp
            int r5 = r5 + r13
            char r14 = r14.charAt(r5)
            r5 = 48
            if (r14 != r5) goto L_0x006e
            int r13 = r13 + r4
            goto L_0x001d
        L_0x006e:
            java.lang.String r14 = r12.text
            java.lang.String r5 = "\"1\""
            int r6 = r12.bp
            int r6 = r6 + r13
            boolean r14 = r14.startsWith(r5, r6)
            if (r14 == 0) goto L_0x007d
            int r13 = r13 + r3
            goto L_0x002e
        L_0x007d:
            java.lang.String r14 = r12.text
            java.lang.String r5 = "\"0\""
            int r6 = r12.bp
            int r6 = r6 + r13
            boolean r14 = r14.startsWith(r5, r6)
            if (r14 == 0) goto L_0x0170
            int r13 = r13 + r3
            goto L_0x001d
        L_0x008c:
            int r5 = r12.bp
            int r6 = r13 + 1
            int r5 = r5 + r13
            int r13 = r12.len
            r7 = 26
            if (r5 < r13) goto L_0x009a
            r13 = 26
            goto L_0x00a0
        L_0x009a:
            java.lang.String r13 = r12.text
            char r13 = r13.charAt(r5)
        L_0x00a0:
            r5 = 16
            r8 = 44
            if (r13 != r8) goto L_0x00c3
            int r13 = r12.bp
            int r6 = r6 - r4
            int r13 = r13 + r6
            r12.bp = r13
            int r13 = r12.bp
            int r13 = r13 + r4
            r12.bp = r13
            int r0 = r12.len
            if (r13 < r0) goto L_0x00b6
            goto L_0x00bc
        L_0x00b6:
            java.lang.String r0 = r12.text
            char r7 = r0.charAt(r13)
        L_0x00bc:
            r12.ch = r7
            r12.matchStat = r3
            r12.token = r5
            return r14
        L_0x00c3:
            r9 = 13
            r10 = 125(0x7d, float:1.75E-43)
            if (r13 == r10) goto L_0x00f3
            r11 = 32
            if (r13 == r11) goto L_0x00df
            r11 = 10
            if (r13 == r11) goto L_0x00df
            if (r13 == r9) goto L_0x00df
            r11 = 9
            if (r13 == r11) goto L_0x00df
            r11 = 12
            if (r13 == r11) goto L_0x00df
            r11 = 8
            if (r13 != r11) goto L_0x00f3
        L_0x00df:
            int r13 = r12.bp
            int r5 = r6 + 1
            int r13 = r13 + r6
            int r6 = r12.len
            if (r13 < r6) goto L_0x00eb
            r13 = 26
            goto L_0x00f1
        L_0x00eb:
            java.lang.String r6 = r12.text
            char r13 = r6.charAt(r13)
        L_0x00f1:
            r6 = r5
            goto L_0x00a0
        L_0x00f3:
            if (r13 != r10) goto L_0x016d
            int r13 = r12.bp
            int r3 = r6 + 1
            int r13 = r13 + r6
            char r13 = r12.charAt(r13)
            if (r13 != r8) goto L_0x011b
            r12.token = r5
            int r13 = r12.bp
            int r3 = r3 - r4
            int r13 = r13 + r3
            r12.bp = r13
            int r13 = r12.bp
            int r13 = r13 + r4
            r12.bp = r13
            int r0 = r12.len
            if (r13 < r0) goto L_0x0112
            goto L_0x0118
        L_0x0112:
            java.lang.String r0 = r12.text
            char r7 = r0.charAt(r13)
        L_0x0118:
            r12.ch = r7
            goto L_0x0167
        L_0x011b:
            r5 = 93
            if (r13 != r5) goto L_0x013c
            r13 = 15
            r12.token = r13
            int r13 = r12.bp
            int r3 = r3 - r4
            int r13 = r13 + r3
            r12.bp = r13
            int r13 = r12.bp
            int r13 = r13 + r4
            r12.bp = r13
            int r0 = r12.len
            if (r13 < r0) goto L_0x0133
            goto L_0x0139
        L_0x0133:
            java.lang.String r0 = r12.text
            char r7 = r0.charAt(r13)
        L_0x0139:
            r12.ch = r7
            goto L_0x0167
        L_0x013c:
            if (r13 != r10) goto L_0x0159
            r12.token = r9
            int r13 = r12.bp
            int r3 = r3 - r4
            int r13 = r13 + r3
            r12.bp = r13
            int r13 = r12.bp
            int r13 = r13 + r4
            r12.bp = r13
            int r0 = r12.len
            if (r13 < r0) goto L_0x0150
            goto L_0x0156
        L_0x0150:
            java.lang.String r0 = r12.text
            char r7 = r0.charAt(r13)
        L_0x0156:
            r12.ch = r7
            goto L_0x0167
        L_0x0159:
            if (r13 != r7) goto L_0x016a
            r13 = 20
            r12.token = r13
            int r13 = r12.bp
            int r3 = r3 - r4
            int r13 = r13 + r3
            r12.bp = r13
            r12.ch = r7
        L_0x0167:
            r12.matchStat = r1
            return r14
        L_0x016a:
            r12.matchStat = r2
            return r0
        L_0x016d:
            r12.matchStat = r2
            return r0
        L_0x0170:
            r12.matchStat = r2
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanFieldBoolean(long):boolean");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final float scanFieldFloat(long r18) {
        /*
            r17 = this;
            r0 = r17
            r1 = 0
            r0.matchStat = r1
            int r2 = r17.matchFieldHash(r18)
            r3 = 0
            if (r2 != 0) goto L_0x000d
            return r3
        L_0x000d:
            int r4 = r0.bp
            int r5 = r2 + 1
            int r4 = r4 + r2
            char r2 = r0.charAt(r4)
            int r4 = r0.bp
            int r4 = r4 + r5
            r6 = 1
            int r4 = r4 - r6
            r7 = 45
            if (r2 != r7) goto L_0x0021
            r8 = 1
            goto L_0x0022
        L_0x0021:
            r8 = 0
        L_0x0022:
            if (r8 == 0) goto L_0x002e
            int r2 = r0.bp
            int r9 = r5 + 1
            int r2 = r2 + r5
            char r2 = r0.charAt(r2)
            r5 = r9
        L_0x002e:
            r9 = -1
            r10 = 48
            if (r2 < r10) goto L_0x0144
            r11 = 57
            if (r2 > r11) goto L_0x0144
            int r2 = r2 - r10
        L_0x0038:
            int r12 = r0.bp
            int r13 = r5 + 1
            int r12 = r12 + r5
            char r5 = r0.charAt(r12)
            if (r5 < r10) goto L_0x004c
            if (r5 > r11) goto L_0x004c
            int r2 = r2 * 10
            int r5 = r5 + -48
            int r2 = r2 + r5
            r5 = r13
            goto L_0x0038
        L_0x004c:
            r12 = 46
            if (r5 != r12) goto L_0x0052
            r12 = 1
            goto L_0x0053
        L_0x0052:
            r12 = 0
        L_0x0053:
            r14 = 10
            if (r12 == 0) goto L_0x008a
            int r5 = r0.bp
            int r12 = r13 + 1
            int r5 = r5 + r13
            char r5 = r0.charAt(r5)
            if (r5 < r10) goto L_0x0087
            if (r5 > r11) goto L_0x0087
            int r2 = r2 * 10
            int r5 = r5 - r10
            int r2 = r2 + r5
            r5 = 10
        L_0x006a:
            int r13 = r0.bp
            int r15 = r12 + 1
            int r13 = r13 + r12
            char r12 = r0.charAt(r13)
            if (r12 < r10) goto L_0x0080
            if (r12 > r11) goto L_0x0080
            int r2 = r2 * 10
            int r12 = r12 + -48
            int r2 = r2 + r12
            int r5 = r5 * 10
            r12 = r15
            goto L_0x006a
        L_0x0080:
            r13 = r15
            r16 = r12
            r12 = r5
            r5 = r16
            goto L_0x008b
        L_0x0087:
            r0.matchStat = r9
            return r3
        L_0x008a:
            r12 = 1
        L_0x008b:
            r15 = 101(0x65, float:1.42E-43)
            if (r5 == r15) goto L_0x0093
            r15 = 69
            if (r5 != r15) goto L_0x0094
        L_0x0093:
            r1 = 1
        L_0x0094:
            if (r1 == 0) goto L_0x00c0
            int r5 = r0.bp
            int r15 = r13 + 1
            int r5 = r5 + r13
            char r5 = r0.charAt(r5)
            r13 = 43
            if (r5 == r13) goto L_0x00a8
            if (r5 != r7) goto L_0x00a6
            goto L_0x00a8
        L_0x00a6:
            r13 = r15
            goto L_0x00b2
        L_0x00a8:
            int r5 = r0.bp
            int r7 = r15 + 1
            int r5 = r5 + r15
            char r5 = r0.charAt(r5)
        L_0x00b1:
            r13 = r7
        L_0x00b2:
            if (r5 < r10) goto L_0x00c0
            if (r5 > r11) goto L_0x00c0
            int r5 = r0.bp
            int r7 = r13 + 1
            int r5 = r5 + r13
            char r5 = r0.charAt(r5)
            goto L_0x00b1
        L_0x00c0:
            int r7 = r0.bp
            int r7 = r7 + r13
            int r7 = r7 - r4
            int r7 = r7 - r6
            if (r1 != 0) goto L_0x00d0
            if (r7 >= r14) goto L_0x00d0
            float r1 = (float) r2
            float r2 = (float) r12
            float r1 = r1 / r2
            if (r8 == 0) goto L_0x00d8
            float r1 = -r1
            goto L_0x00d8
        L_0x00d0:
            java.lang.String r1 = r0.subString(r4, r7)
            float r1 = java.lang.Float.parseFloat(r1)
        L_0x00d8:
            r2 = 16
            r4 = 44
            if (r5 != r4) goto L_0x00ed
            int r3 = r0.bp
            int r13 = r13 - r6
            int r3 = r3 + r13
            r0.bp = r3
            r17.next()
            r3 = 3
            r0.matchStat = r3
            r0.token = r2
            return r1
        L_0x00ed:
            r7 = 125(0x7d, float:1.75E-43)
            if (r5 != r7) goto L_0x0141
            int r5 = r0.bp
            int r8 = r13 + 1
            int r5 = r5 + r13
            char r5 = r0.charAt(r5)
            if (r5 != r4) goto L_0x0108
            r0.token = r2
            int r2 = r0.bp
            int r8 = r8 - r6
            int r2 = r2 + r8
            r0.bp = r2
            r17.next()
            goto L_0x013a
        L_0x0108:
            r2 = 93
            if (r5 != r2) goto L_0x011a
            r2 = 15
            r0.token = r2
            int r2 = r0.bp
            int r8 = r8 - r6
            int r2 = r2 + r8
            r0.bp = r2
            r17.next()
            goto L_0x013a
        L_0x011a:
            if (r5 != r7) goto L_0x012a
            r2 = 13
            r0.token = r2
            int r2 = r0.bp
            int r8 = r8 - r6
            int r2 = r2 + r8
            r0.bp = r2
            r17.next()
            goto L_0x013a
        L_0x012a:
            r2 = 26
            if (r5 != r2) goto L_0x013e
            int r3 = r0.bp
            int r8 = r8 - r6
            int r3 = r3 + r8
            r0.bp = r3
            r3 = 20
            r0.token = r3
            r0.ch = r2
        L_0x013a:
            r2 = 4
            r0.matchStat = r2
            return r1
        L_0x013e:
            r0.matchStat = r9
            return r3
        L_0x0141:
            r0.matchStat = r9
            return r3
        L_0x0144:
            r0.matchStat = r9
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanFieldFloat(long):float");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0125  */
    public final float[] scanFieldFloatArray(long r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = 0
            r0.matchStat = r1
            int r2 = r18.matchFieldHash(r19)
            r3 = 0
            if (r2 != 0) goto L_0x000d
            return r3
        L_0x000d:
            int r4 = r0.bp
            int r5 = r2 + 1
            int r4 = r4 + r2
            int r2 = r0.len
            if (r4 < r2) goto L_0x0019
            r2 = 26
            goto L_0x001f
        L_0x0019:
            java.lang.String r2 = r0.text
            char r2 = r2.charAt(r4)
        L_0x001f:
            r4 = 91
            r7 = -1
            if (r2 == r4) goto L_0x0027
            r0.matchStat = r7
            return r3
        L_0x0027:
            int r2 = r0.bp
            int r4 = r5 + 1
            int r2 = r2 + r5
            int r5 = r0.len
            if (r2 < r5) goto L_0x0033
            r2 = 26
            goto L_0x0039
        L_0x0033:
            java.lang.String r5 = r0.text
            char r2 = r5.charAt(r2)
        L_0x0039:
            r5 = 16
            float[] r8 = new float[r5]
            r9 = 0
        L_0x003e:
            int r10 = r0.bp
            int r10 = r10 + r4
            r11 = 1
            int r10 = r10 - r11
            r12 = 45
            if (r2 != r12) goto L_0x0049
            r13 = 1
            goto L_0x004a
        L_0x0049:
            r13 = 0
        L_0x004a:
            if (r13 == 0) goto L_0x005f
            int r2 = r0.bp
            int r14 = r4 + 1
            int r2 = r2 + r4
            int r4 = r0.len
            if (r2 < r4) goto L_0x0058
            r2 = 26
            goto L_0x0060
        L_0x0058:
            java.lang.String r4 = r0.text
            char r2 = r4.charAt(r2)
            goto L_0x0060
        L_0x005f:
            r14 = r4
        L_0x0060:
            r4 = 48
            if (r2 < r4) goto L_0x0219
            r15 = 57
            if (r2 > r15) goto L_0x0219
            int r2 = r2 + -48
        L_0x006a:
            int r6 = r0.bp
            int r16 = r14 + 1
            int r6 = r6 + r14
            int r14 = r0.len
            if (r6 < r14) goto L_0x0076
            r6 = 26
            goto L_0x007c
        L_0x0076:
            java.lang.String r14 = r0.text
            char r6 = r14.charAt(r6)
        L_0x007c:
            if (r6 < r4) goto L_0x0088
            if (r6 > r15) goto L_0x0088
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            r14 = r16
            goto L_0x006a
        L_0x0088:
            r14 = 46
            if (r6 != r14) goto L_0x008e
            r14 = 1
            goto L_0x008f
        L_0x008e:
            r14 = 0
        L_0x008f:
            r5 = 10
            if (r14 == 0) goto L_0x00d4
            int r6 = r0.bp
            int r14 = r16 + 1
            int r6 = r6 + r16
            int r1 = r0.len
            if (r6 < r1) goto L_0x00a0
            r6 = 26
            goto L_0x00a6
        L_0x00a0:
            java.lang.String r1 = r0.text
            char r6 = r1.charAt(r6)
        L_0x00a6:
            if (r6 < r4) goto L_0x00d1
            if (r6 > r15) goto L_0x00d1
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            r1 = 10
        L_0x00b1:
            int r6 = r0.bp
            int r16 = r14 + 1
            int r6 = r6 + r14
            int r14 = r0.len
            if (r6 < r14) goto L_0x00bd
            r6 = 26
            goto L_0x00c3
        L_0x00bd:
            java.lang.String r14 = r0.text
            char r6 = r14.charAt(r6)
        L_0x00c3:
            if (r6 < r4) goto L_0x00d5
            if (r6 > r15) goto L_0x00d5
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            int r1 = r1 * 10
            r14 = r16
            goto L_0x00b1
        L_0x00d1:
            r0.matchStat = r7
            return r3
        L_0x00d4:
            r1 = 1
        L_0x00d5:
            r14 = 101(0x65, float:1.42E-43)
            if (r6 == r14) goto L_0x00e0
            r14 = 69
            if (r6 != r14) goto L_0x00de
            goto L_0x00e0
        L_0x00de:
            r14 = 0
            goto L_0x00e1
        L_0x00e0:
            r14 = 1
        L_0x00e1:
            if (r14 == 0) goto L_0x012c
            int r6 = r0.bp
            int r17 = r16 + 1
            int r6 = r6 + r16
            int r3 = r0.len
            if (r6 < r3) goto L_0x00f0
            r6 = 26
            goto L_0x00f6
        L_0x00f0:
            java.lang.String r3 = r0.text
            char r6 = r3.charAt(r6)
        L_0x00f6:
            r3 = 43
            if (r6 == r3) goto L_0x0100
            if (r6 != r12) goto L_0x00fd
            goto L_0x0100
        L_0x00fd:
            r16 = r17
            goto L_0x0116
        L_0x0100:
            int r3 = r0.bp
            int r6 = r17 + 1
            int r3 = r3 + r17
            int r12 = r0.len
            if (r3 < r12) goto L_0x010d
        L_0x010a:
            r3 = 26
            goto L_0x0113
        L_0x010d:
            java.lang.String r12 = r0.text
            char r3 = r12.charAt(r3)
        L_0x0113:
            r16 = r6
            r6 = r3
        L_0x0116:
            if (r6 < r4) goto L_0x012c
            if (r6 > r15) goto L_0x012c
            int r3 = r0.bp
            int r6 = r16 + 1
            int r3 = r3 + r16
            int r12 = r0.len
            if (r3 < r12) goto L_0x0125
            goto L_0x010a
        L_0x0125:
            java.lang.String r12 = r0.text
            char r3 = r12.charAt(r3)
            goto L_0x0113
        L_0x012c:
            int r3 = r0.bp
            int r3 = r3 + r16
            int r3 = r3 - r10
            int r3 = r3 - r11
            if (r14 != 0) goto L_0x013d
            if (r3 >= r5) goto L_0x013d
            float r2 = (float) r2
            float r1 = (float) r1
            float r2 = r2 / r1
            if (r13 == 0) goto L_0x0145
            float r2 = -r2
            goto L_0x0145
        L_0x013d:
            java.lang.String r1 = r0.subString(r10, r3)
            float r2 = java.lang.Float.parseFloat(r1)
        L_0x0145:
            int r1 = r8.length
            r3 = 3
            if (r9 < r1) goto L_0x0155
            int r1 = r8.length
            int r1 = r1 * 3
            int r1 = r1 / 2
            float[] r1 = new float[r1]
            r4 = 0
            java.lang.System.arraycopy(r8, r4, r1, r4, r9)
            r8 = r1
        L_0x0155:
            int r1 = r9 + 1
            r8[r9] = r2
            r2 = 44
            if (r6 != r2) goto L_0x0174
            int r2 = r0.bp
            int r3 = r16 + 1
            int r2 = r2 + r16
            int r4 = r0.len
            if (r2 < r4) goto L_0x016a
            r6 = 26
            goto L_0x0170
        L_0x016a:
            java.lang.String r4 = r0.text
            char r6 = r4.charAt(r2)
        L_0x0170:
            r16 = r3
            goto L_0x0209
        L_0x0174:
            r4 = 93
            if (r6 != r4) goto L_0x0209
            int r5 = r0.bp
            int r6 = r16 + 1
            int r5 = r5 + r16
            int r9 = r0.len
            if (r5 < r9) goto L_0x0185
            r5 = 26
            goto L_0x018b
        L_0x0185:
            java.lang.String r9 = r0.text
            char r5 = r9.charAt(r5)
        L_0x018b:
            int r9 = r8.length
            if (r1 == r9) goto L_0x0195
            float[] r9 = new float[r1]
            r10 = 0
            java.lang.System.arraycopy(r8, r10, r9, r10, r1)
            r8 = r9
        L_0x0195:
            if (r5 != r2) goto L_0x01a7
            int r1 = r0.bp
            int r6 = r6 - r11
            int r1 = r1 + r6
            r0.bp = r1
            r18.next()
            r0.matchStat = r3
            r1 = 16
            r0.token = r1
            return r8
        L_0x01a7:
            r1 = 125(0x7d, float:1.75E-43)
            if (r5 != r1) goto L_0x0205
            int r3 = r0.bp
            int r5 = r6 + 1
            int r3 = r3 + r6
            int r6 = r0.len
            if (r3 < r6) goto L_0x01b7
            r6 = 26
            goto L_0x01bd
        L_0x01b7:
            java.lang.String r6 = r0.text
            char r6 = r6.charAt(r3)
        L_0x01bd:
            if (r6 != r2) goto L_0x01cd
            r2 = 16
            r0.token = r2
            int r1 = r0.bp
            int r5 = r5 - r11
            int r1 = r1 + r5
            r0.bp = r1
            r18.next()
            goto L_0x01fd
        L_0x01cd:
            if (r6 != r4) goto L_0x01dd
            r1 = 15
            r0.token = r1
            int r1 = r0.bp
            int r5 = r5 - r11
            int r1 = r1 + r5
            r0.bp = r1
            r18.next()
            goto L_0x01fd
        L_0x01dd:
            if (r6 != r1) goto L_0x01ed
            r1 = 13
            r0.token = r1
            int r1 = r0.bp
            int r5 = r5 - r11
            int r1 = r1 + r5
            r0.bp = r1
            r18.next()
            goto L_0x01fd
        L_0x01ed:
            r3 = 26
            if (r6 != r3) goto L_0x0201
            int r1 = r0.bp
            int r5 = r5 - r11
            int r1 = r1 + r5
            r0.bp = r1
            r1 = 20
            r0.token = r1
            r0.ch = r3
        L_0x01fd:
            r1 = 4
            r0.matchStat = r1
            return r8
        L_0x0201:
            r0.matchStat = r7
            r4 = 0
            return r4
        L_0x0205:
            r4 = 0
            r0.matchStat = r7
            return r4
        L_0x0209:
            r2 = 16
            r3 = 26
            r4 = 0
            r10 = 0
            r9 = r1
            r3 = r4
            r2 = r6
            r4 = r16
            r1 = 0
            r5 = 16
            goto L_0x003e
        L_0x0219:
            r4 = r3
            r0.matchStat = r7
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanFieldFloatArray(long):float[]");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0144  */
    public final float[][] scanFieldFloatArray2(long r20) {
        /*
            r19 = this;
            r0 = r19
            r1 = 0
            r0.matchStat = r1
            int r2 = r19.matchFieldHash(r20)
            r3 = 0
            if (r2 != 0) goto L_0x000f
            float[][] r3 = (float[][]) r3
            return r3
        L_0x000f:
            int r4 = r0.bp
            int r5 = r2 + 1
            int r4 = r4 + r2
            int r2 = r0.len
            if (r4 < r2) goto L_0x001b
            r2 = 26
            goto L_0x0021
        L_0x001b:
            java.lang.String r2 = r0.text
            char r2 = r2.charAt(r4)
        L_0x0021:
            r4 = 91
            r7 = -1
            if (r2 == r4) goto L_0x002b
            r0.matchStat = r7
            float[][] r3 = (float[][]) r3
            return r3
        L_0x002b:
            int r2 = r0.bp
            int r8 = r5 + 1
            int r2 = r2 + r5
            int r5 = r0.len
            if (r2 < r5) goto L_0x0037
            r2 = 26
            goto L_0x003d
        L_0x0037:
            java.lang.String r5 = r0.text
            char r2 = r5.charAt(r2)
        L_0x003d:
            r5 = 16
            float[][] r9 = new float[r5][]
            r10 = r9
            r9 = 0
        L_0x0043:
            if (r2 != r4) goto L_0x02aa
            int r2 = r0.bp
            int r11 = r8 + 1
            int r2 = r2 + r8
            int r8 = r0.len
            if (r2 < r8) goto L_0x0051
            r2 = 26
            goto L_0x0057
        L_0x0051:
            java.lang.String r8 = r0.text
            char r2 = r8.charAt(r2)
        L_0x0057:
            float[] r8 = new float[r5]
            r12 = 0
        L_0x005a:
            int r13 = r0.bp
            int r13 = r13 + r11
            r14 = 1
            int r13 = r13 - r14
            r15 = 45
            if (r2 != r15) goto L_0x0066
            r16 = 1
            goto L_0x0068
        L_0x0066:
            r16 = 0
        L_0x0068:
            if (r16 == 0) goto L_0x007d
            int r2 = r0.bp
            int r17 = r11 + 1
            int r2 = r2 + r11
            int r11 = r0.len
            if (r2 < r11) goto L_0x0076
            r2 = 26
            goto L_0x007f
        L_0x0076:
            java.lang.String r11 = r0.text
            char r2 = r11.charAt(r2)
            goto L_0x007f
        L_0x007d:
            r17 = r11
        L_0x007f:
            r11 = 48
            if (r2 < r11) goto L_0x02a3
            r4 = 57
            if (r2 > r4) goto L_0x02a3
            int r2 = r2 + -48
        L_0x0089:
            int r6 = r0.bp
            int r18 = r17 + 1
            int r6 = r6 + r17
            int r5 = r0.len
            if (r6 < r5) goto L_0x0096
            r6 = 26
            goto L_0x009c
        L_0x0096:
            java.lang.String r5 = r0.text
            char r6 = r5.charAt(r6)
        L_0x009c:
            if (r6 < r11) goto L_0x00aa
            if (r6 > r4) goto L_0x00aa
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            r17 = r18
            r5 = 16
            goto L_0x0089
        L_0x00aa:
            r5 = 46
            if (r6 != r5) goto L_0x00f1
            int r5 = r0.bp
            int r6 = r18 + 1
            int r5 = r5 + r18
            int r1 = r0.len
            if (r5 < r1) goto L_0x00bb
            r1 = 26
            goto L_0x00c1
        L_0x00bb:
            java.lang.String r1 = r0.text
            char r1 = r1.charAt(r5)
        L_0x00c1:
            if (r1 < r11) goto L_0x00ec
            if (r1 > r4) goto L_0x00ec
            int r2 = r2 * 10
            int r1 = r1 + -48
            int r2 = r2 + r1
            r1 = 10
        L_0x00cc:
            int r5 = r0.bp
            int r17 = r6 + 1
            int r5 = r5 + r6
            int r6 = r0.len
            if (r5 < r6) goto L_0x00d8
            r6 = 26
            goto L_0x00de
        L_0x00d8:
            java.lang.String r6 = r0.text
            char r6 = r6.charAt(r5)
        L_0x00de:
            if (r6 < r11) goto L_0x00f4
            if (r6 > r4) goto L_0x00f4
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            int r1 = r1 * 10
            r6 = r17
            goto L_0x00cc
        L_0x00ec:
            r0.matchStat = r7
            float[][] r3 = (float[][]) r3
            return r3
        L_0x00f1:
            r17 = r18
            r1 = 1
        L_0x00f4:
            r5 = 101(0x65, float:1.42E-43)
            if (r6 == r5) goto L_0x00ff
            r5 = 69
            if (r6 != r5) goto L_0x00fd
            goto L_0x00ff
        L_0x00fd:
            r5 = 0
            goto L_0x0100
        L_0x00ff:
            r5 = 1
        L_0x0100:
            if (r5 == 0) goto L_0x014b
            int r6 = r0.bp
            int r18 = r17 + 1
            int r6 = r6 + r17
            int r3 = r0.len
            if (r6 < r3) goto L_0x010f
            r6 = 26
            goto L_0x0115
        L_0x010f:
            java.lang.String r3 = r0.text
            char r6 = r3.charAt(r6)
        L_0x0115:
            r3 = 43
            if (r6 == r3) goto L_0x011f
            if (r6 != r15) goto L_0x011c
            goto L_0x011f
        L_0x011c:
            r17 = r18
            goto L_0x0135
        L_0x011f:
            int r3 = r0.bp
            int r6 = r18 + 1
            int r3 = r3 + r18
            int r15 = r0.len
            if (r3 < r15) goto L_0x012c
        L_0x0129:
            r3 = 26
            goto L_0x0132
        L_0x012c:
            java.lang.String r15 = r0.text
            char r3 = r15.charAt(r3)
        L_0x0132:
            r17 = r6
            r6 = r3
        L_0x0135:
            if (r6 < r11) goto L_0x014b
            if (r6 > r4) goto L_0x014b
            int r3 = r0.bp
            int r6 = r17 + 1
            int r3 = r3 + r17
            int r15 = r0.len
            if (r3 < r15) goto L_0x0144
            goto L_0x0129
        L_0x0144:
            java.lang.String r15 = r0.text
            char r3 = r15.charAt(r3)
            goto L_0x0132
        L_0x014b:
            int r3 = r0.bp
            int r3 = r3 + r17
            int r3 = r3 - r13
            int r3 = r3 - r14
            if (r5 != 0) goto L_0x015e
            r4 = 10
            if (r3 >= r4) goto L_0x015e
            float r2 = (float) r2
            float r1 = (float) r1
            float r2 = r2 / r1
            if (r16 == 0) goto L_0x0166
            float r2 = -r2
            goto L_0x0166
        L_0x015e:
            java.lang.String r1 = r0.subString(r13, r3)
            float r2 = java.lang.Float.parseFloat(r1)
        L_0x0166:
            int r1 = r8.length
            r3 = 3
            if (r12 < r1) goto L_0x0176
            int r1 = r8.length
            int r1 = r1 * 3
            int r1 = r1 / 2
            float[] r1 = new float[r1]
            r4 = 0
            java.lang.System.arraycopy(r8, r4, r1, r4, r12)
            r8 = r1
        L_0x0176:
            int r1 = r12 + 1
            r8[r12] = r2
            r2 = 44
            if (r6 != r2) goto L_0x019c
            int r2 = r0.bp
            int r3 = r17 + 1
            int r2 = r2 + r17
            int r4 = r0.len
            if (r2 < r4) goto L_0x018b
            r6 = 26
            goto L_0x0191
        L_0x018b:
            java.lang.String r4 = r0.text
            char r6 = r4.charAt(r2)
        L_0x0191:
            r17 = r3
            r2 = r6
            r3 = 16
            r4 = 26
            r11 = 0
            r12 = 0
            goto L_0x0298
        L_0x019c:
            r4 = 93
            if (r6 != r4) goto L_0x0291
            int r5 = r0.bp
            int r6 = r17 + 1
            int r5 = r5 + r17
            int r11 = r0.len
            if (r5 < r11) goto L_0x01ad
            r5 = 26
            goto L_0x01b3
        L_0x01ad:
            java.lang.String r11 = r0.text
            char r5 = r11.charAt(r5)
        L_0x01b3:
            int r11 = r8.length
            if (r1 == r11) goto L_0x01be
            float[] r11 = new float[r1]
            r12 = 0
            java.lang.System.arraycopy(r8, r12, r11, r12, r1)
            r8 = r11
            goto L_0x01bf
        L_0x01be:
            r12 = 0
        L_0x01bf:
            int r11 = r10.length
            if (r9 < r11) goto L_0x01cc
            int r10 = r10.length
            int r10 = r10 * 3
            int r10 = r10 / 2
            float[][] r10 = new float[r10][]
            java.lang.System.arraycopy(r8, r12, r10, r12, r1)
        L_0x01cc:
            int r1 = r9 + 1
            r10[r9] = r8
            if (r5 != r2) goto L_0x01ee
            int r2 = r0.bp
            int r3 = r6 + 1
            int r2 = r2 + r6
            int r4 = r0.len
            if (r2 < r4) goto L_0x01de
            r6 = 26
            goto L_0x01e4
        L_0x01de:
            java.lang.String r4 = r0.text
            char r6 = r4.charAt(r2)
        L_0x01e4:
            r8 = r3
            r2 = r6
            r3 = 16
            r4 = 26
            r11 = 0
            r12 = 0
            goto L_0x0288
        L_0x01ee:
            if (r5 != r4) goto L_0x0280
            int r5 = r0.bp
            int r8 = r6 + 1
            int r5 = r5 + r6
            int r6 = r0.len
            if (r5 < r6) goto L_0x01fc
            r6 = 26
            goto L_0x0202
        L_0x01fc:
            java.lang.String r6 = r0.text
            char r6 = r6.charAt(r5)
        L_0x0202:
            int r5 = r10.length
            if (r1 == r5) goto L_0x020c
            float[][] r5 = new float[r1][]
            r11 = 0
            java.lang.System.arraycopy(r10, r11, r5, r11, r1)
            goto L_0x020d
        L_0x020c:
            r5 = r10
        L_0x020d:
            if (r6 != r2) goto L_0x021f
            int r1 = r0.bp
            int r8 = r8 - r14
            int r1 = r1 + r8
            r0.bp = r1
            r19.next()
            r0.matchStat = r3
            r3 = 16
            r0.token = r3
            return r5
        L_0x021f:
            r3 = 16
            r1 = 125(0x7d, float:1.75E-43)
            if (r6 != r1) goto L_0x0279
            int r1 = r0.bp
            int r6 = r8 + 1
            int r1 = r1 + r8
            char r1 = r0.charAt(r1)
            if (r1 != r2) goto L_0x023c
            r0.token = r3
            int r1 = r0.bp
            int r6 = r6 - r14
            int r1 = r1 + r6
            r0.bp = r1
            r19.next()
            goto L_0x026e
        L_0x023c:
            if (r1 != r4) goto L_0x024c
            r1 = 15
            r0.token = r1
            int r1 = r0.bp
            int r6 = r6 - r14
            int r1 = r1 + r6
            r0.bp = r1
            r19.next()
            goto L_0x026e
        L_0x024c:
            r2 = 125(0x7d, float:1.75E-43)
            if (r1 != r2) goto L_0x025e
            r1 = 13
            r0.token = r1
            int r1 = r0.bp
            int r6 = r6 - r14
            int r1 = r1 + r6
            r0.bp = r1
            r19.next()
            goto L_0x026e
        L_0x025e:
            r4 = 26
            if (r1 != r4) goto L_0x0272
            int r1 = r0.bp
            int r6 = r6 - r14
            int r1 = r1 + r6
            r0.bp = r1
            r1 = 20
            r0.token = r1
            r0.ch = r4
        L_0x026e:
            r1 = 4
            r0.matchStat = r1
            return r5
        L_0x0272:
            r0.matchStat = r7
            r12 = 0
            r3 = r12
            float[][] r3 = (float[][]) r3
            return r3
        L_0x0279:
            r12 = 0
            r0.matchStat = r7
            r3 = r12
            float[][] r3 = (float[][]) r3
            return r3
        L_0x0280:
            r3 = 16
            r4 = 26
            r11 = 0
            r12 = 0
            r2 = r5
            r8 = r6
        L_0x0288:
            r9 = r1
            r3 = r12
            r1 = 0
            r4 = 91
            r5 = 16
            goto L_0x0043
        L_0x0291:
            r3 = 16
            r4 = 26
            r11 = 0
            r12 = 0
            r2 = r6
        L_0x0298:
            r3 = r12
            r11 = r17
            r4 = 91
            r5 = 16
            r12 = r1
            r1 = 0
            goto L_0x005a
        L_0x02a3:
            r12 = r3
            r0.matchStat = r7
            r3 = r12
            float[][] r3 = (float[][]) r3
            return r3
        L_0x02aa:
            r12 = r3
            r4 = 91
            goto L_0x0043
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanFieldFloatArray2(long):float[][]");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final double scanFieldDouble(long r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = 0
            r0.matchStat = r1
            int r2 = r18.matchFieldHash(r19)
            r3 = 0
            if (r2 != 0) goto L_0x000e
            return r3
        L_0x000e:
            int r5 = r0.bp
            int r6 = r2 + 1
            int r5 = r5 + r2
            char r2 = r0.charAt(r5)
            int r5 = r0.bp
            int r5 = r5 + r6
            r7 = 1
            int r5 = r5 - r7
            r8 = 45
            if (r2 != r8) goto L_0x0022
            r9 = 1
            goto L_0x0023
        L_0x0022:
            r9 = 0
        L_0x0023:
            if (r9 == 0) goto L_0x002f
            int r2 = r0.bp
            int r10 = r6 + 1
            int r2 = r2 + r6
            char r2 = r0.charAt(r2)
            r6 = r10
        L_0x002f:
            r10 = -1
            r11 = 48
            if (r2 < r11) goto L_0x0150
            r12 = 57
            if (r2 > r12) goto L_0x0150
            int r2 = r2 - r11
        L_0x0039:
            int r13 = r0.bp
            int r14 = r6 + 1
            int r13 = r13 + r6
            char r6 = r0.charAt(r13)
            if (r6 < r11) goto L_0x004d
            if (r6 > r12) goto L_0x004d
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            r6 = r14
            goto L_0x0039
        L_0x004d:
            r13 = 46
            if (r6 != r13) goto L_0x0053
            r13 = 1
            goto L_0x0054
        L_0x0053:
            r13 = 0
        L_0x0054:
            r15 = 10
            if (r13 == 0) goto L_0x0088
            int r6 = r0.bp
            int r13 = r14 + 1
            int r6 = r6 + r14
            char r6 = r0.charAt(r6)
            if (r6 < r11) goto L_0x0085
            if (r6 > r12) goto L_0x0085
            int r2 = r2 * 10
            int r6 = r6 - r11
            int r2 = r2 + r6
            r6 = 10
        L_0x006b:
            int r14 = r0.bp
            int r16 = r13 + 1
            int r14 = r14 + r13
            char r13 = r0.charAt(r14)
            if (r13 < r11) goto L_0x0082
            if (r13 > r12) goto L_0x0082
            int r2 = r2 * 10
            int r13 = r13 + -48
            int r2 = r2 + r13
            int r6 = r6 * 10
            r13 = r16
            goto L_0x006b
        L_0x0082:
            r14 = r16
            goto L_0x008a
        L_0x0085:
            r0.matchStat = r10
            return r3
        L_0x0088:
            r13 = r6
            r6 = 1
        L_0x008a:
            r1 = 101(0x65, float:1.42E-43)
            if (r13 == r1) goto L_0x0096
            r1 = 69
            if (r13 != r1) goto L_0x0093
            goto L_0x0096
        L_0x0093:
            r17 = 0
            goto L_0x0098
        L_0x0096:
            r17 = 1
        L_0x0098:
            if (r17 == 0) goto L_0x00c6
            int r1 = r0.bp
            int r13 = r14 + 1
            int r1 = r1 + r14
            char r1 = r0.charAt(r1)
            r14 = 43
            if (r1 == r14) goto L_0x00ad
            if (r1 != r8) goto L_0x00aa
            goto L_0x00ad
        L_0x00aa:
            r14 = r13
            r13 = r1
            goto L_0x00b8
        L_0x00ad:
            int r1 = r0.bp
            int r8 = r13 + 1
            int r1 = r1 + r13
            char r1 = r0.charAt(r1)
            r13 = r1
        L_0x00b7:
            r14 = r8
        L_0x00b8:
            if (r13 < r11) goto L_0x00c6
            if (r13 > r12) goto L_0x00c6
            int r1 = r0.bp
            int r8 = r14 + 1
            int r1 = r1 + r14
            char r13 = r0.charAt(r1)
            goto L_0x00b7
        L_0x00c6:
            int r1 = r0.bp
            int r1 = r1 + r14
            int r1 = r1 - r5
            int r1 = r1 - r7
            if (r17 != 0) goto L_0x00dc
            if (r1 >= r15) goto L_0x00dc
            double r1 = (double) r2
            double r5 = (double) r6
            java.lang.Double.isNaN(r1)
            java.lang.Double.isNaN(r5)
            double r1 = r1 / r5
            if (r9 == 0) goto L_0x00e4
            double r1 = -r1
            goto L_0x00e4
        L_0x00dc:
            java.lang.String r1 = r0.subString(r5, r1)
            double r1 = java.lang.Double.parseDouble(r1)
        L_0x00e4:
            r5 = 16
            r6 = 44
            if (r13 != r6) goto L_0x00f9
            int r3 = r0.bp
            int r14 = r14 - r7
            int r3 = r3 + r14
            r0.bp = r3
            r18.next()
            r3 = 3
            r0.matchStat = r3
            r0.token = r5
            return r1
        L_0x00f9:
            r8 = 125(0x7d, float:1.75E-43)
            if (r13 != r8) goto L_0x014d
            int r9 = r0.bp
            int r11 = r14 + 1
            int r9 = r9 + r14
            char r9 = r0.charAt(r9)
            if (r9 != r6) goto L_0x0114
            r0.token = r5
            int r3 = r0.bp
            int r11 = r11 - r7
            int r3 = r3 + r11
            r0.bp = r3
            r18.next()
            goto L_0x0146
        L_0x0114:
            r5 = 93
            if (r9 != r5) goto L_0x0126
            r3 = 15
            r0.token = r3
            int r3 = r0.bp
            int r11 = r11 - r7
            int r3 = r3 + r11
            r0.bp = r3
            r18.next()
            goto L_0x0146
        L_0x0126:
            if (r9 != r8) goto L_0x0136
            r3 = 13
            r0.token = r3
            int r3 = r0.bp
            int r11 = r11 - r7
            int r3 = r3 + r11
            r0.bp = r3
            r18.next()
            goto L_0x0146
        L_0x0136:
            r5 = 26
            if (r9 != r5) goto L_0x014a
            int r3 = r0.bp
            int r11 = r11 - r7
            int r3 = r3 + r11
            r0.bp = r3
            r3 = 20
            r0.token = r3
            r0.ch = r5
        L_0x0146:
            r3 = 4
            r0.matchStat = r3
            return r1
        L_0x014a:
            r0.matchStat = r10
            return r3
        L_0x014d:
            r0.matchStat = r10
            return r3
        L_0x0150:
            r0.matchStat = r10
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanFieldDouble(long):double");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0125  */
    public final double[] scanFieldDoubleArray(long r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = 0
            r0.matchStat = r1
            int r2 = r18.matchFieldHash(r19)
            r3 = 0
            if (r2 != 0) goto L_0x000d
            return r3
        L_0x000d:
            int r4 = r0.bp
            int r5 = r2 + 1
            int r4 = r4 + r2
            int r2 = r0.len
            if (r4 < r2) goto L_0x0019
            r2 = 26
            goto L_0x001f
        L_0x0019:
            java.lang.String r2 = r0.text
            char r2 = r2.charAt(r4)
        L_0x001f:
            r4 = 91
            r7 = -1
            if (r2 == r4) goto L_0x0027
            r0.matchStat = r7
            return r3
        L_0x0027:
            int r2 = r0.bp
            int r4 = r5 + 1
            int r2 = r2 + r5
            int r5 = r0.len
            if (r2 < r5) goto L_0x0033
            r2 = 26
            goto L_0x0039
        L_0x0033:
            java.lang.String r5 = r0.text
            char r2 = r5.charAt(r2)
        L_0x0039:
            r5 = 16
            double[] r8 = new double[r5]
            r9 = 0
        L_0x003e:
            int r10 = r0.bp
            int r10 = r10 + r4
            r11 = 1
            int r10 = r10 - r11
            r12 = 45
            if (r2 != r12) goto L_0x0049
            r13 = 1
            goto L_0x004a
        L_0x0049:
            r13 = 0
        L_0x004a:
            if (r13 == 0) goto L_0x005f
            int r2 = r0.bp
            int r14 = r4 + 1
            int r2 = r2 + r4
            int r4 = r0.len
            if (r2 < r4) goto L_0x0058
            r2 = 26
            goto L_0x0060
        L_0x0058:
            java.lang.String r4 = r0.text
            char r2 = r4.charAt(r2)
            goto L_0x0060
        L_0x005f:
            r14 = r4
        L_0x0060:
            r4 = 48
            if (r2 < r4) goto L_0x021f
            r15 = 57
            if (r2 > r15) goto L_0x021f
            int r2 = r2 + -48
        L_0x006a:
            int r6 = r0.bp
            int r16 = r14 + 1
            int r6 = r6 + r14
            int r14 = r0.len
            if (r6 < r14) goto L_0x0076
            r6 = 26
            goto L_0x007c
        L_0x0076:
            java.lang.String r14 = r0.text
            char r6 = r14.charAt(r6)
        L_0x007c:
            if (r6 < r4) goto L_0x0088
            if (r6 > r15) goto L_0x0088
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            r14 = r16
            goto L_0x006a
        L_0x0088:
            r14 = 46
            if (r6 != r14) goto L_0x008e
            r14 = 1
            goto L_0x008f
        L_0x008e:
            r14 = 0
        L_0x008f:
            r5 = 10
            if (r14 == 0) goto L_0x00d4
            int r6 = r0.bp
            int r14 = r16 + 1
            int r6 = r6 + r16
            int r1 = r0.len
            if (r6 < r1) goto L_0x00a0
            r6 = 26
            goto L_0x00a6
        L_0x00a0:
            java.lang.String r1 = r0.text
            char r6 = r1.charAt(r6)
        L_0x00a6:
            if (r6 < r4) goto L_0x00d1
            if (r6 > r15) goto L_0x00d1
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            r1 = 10
        L_0x00b1:
            int r6 = r0.bp
            int r16 = r14 + 1
            int r6 = r6 + r14
            int r14 = r0.len
            if (r6 < r14) goto L_0x00bd
            r6 = 26
            goto L_0x00c3
        L_0x00bd:
            java.lang.String r14 = r0.text
            char r6 = r14.charAt(r6)
        L_0x00c3:
            if (r6 < r4) goto L_0x00d5
            if (r6 > r15) goto L_0x00d5
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            int r1 = r1 * 10
            r14 = r16
            goto L_0x00b1
        L_0x00d1:
            r0.matchStat = r7
            return r3
        L_0x00d4:
            r1 = 1
        L_0x00d5:
            r14 = 101(0x65, float:1.42E-43)
            if (r6 == r14) goto L_0x00e0
            r14 = 69
            if (r6 != r14) goto L_0x00de
            goto L_0x00e0
        L_0x00de:
            r14 = 0
            goto L_0x00e1
        L_0x00e0:
            r14 = 1
        L_0x00e1:
            if (r14 == 0) goto L_0x012c
            int r6 = r0.bp
            int r17 = r16 + 1
            int r6 = r6 + r16
            int r3 = r0.len
            if (r6 < r3) goto L_0x00f0
            r6 = 26
            goto L_0x00f6
        L_0x00f0:
            java.lang.String r3 = r0.text
            char r6 = r3.charAt(r6)
        L_0x00f6:
            r3 = 43
            if (r6 == r3) goto L_0x0100
            if (r6 != r12) goto L_0x00fd
            goto L_0x0100
        L_0x00fd:
            r16 = r17
            goto L_0x0116
        L_0x0100:
            int r3 = r0.bp
            int r6 = r17 + 1
            int r3 = r3 + r17
            int r12 = r0.len
            if (r3 < r12) goto L_0x010d
        L_0x010a:
            r3 = 26
            goto L_0x0113
        L_0x010d:
            java.lang.String r12 = r0.text
            char r3 = r12.charAt(r3)
        L_0x0113:
            r16 = r6
            r6 = r3
        L_0x0116:
            if (r6 < r4) goto L_0x012c
            if (r6 > r15) goto L_0x012c
            int r3 = r0.bp
            int r6 = r16 + 1
            int r3 = r3 + r16
            int r12 = r0.len
            if (r3 < r12) goto L_0x0125
            goto L_0x010a
        L_0x0125:
            java.lang.String r12 = r0.text
            char r3 = r12.charAt(r3)
            goto L_0x0113
        L_0x012c:
            int r3 = r0.bp
            int r3 = r3 + r16
            int r3 = r3 - r10
            int r3 = r3 - r11
            if (r14 != 0) goto L_0x0143
            if (r3 >= r5) goto L_0x0143
            double r2 = (double) r2
            double r4 = (double) r1
            java.lang.Double.isNaN(r2)
            java.lang.Double.isNaN(r4)
            double r2 = r2 / r4
            if (r13 == 0) goto L_0x014b
            double r2 = -r2
            goto L_0x014b
        L_0x0143:
            java.lang.String r1 = r0.subString(r10, r3)
            double r2 = java.lang.Double.parseDouble(r1)
        L_0x014b:
            int r1 = r8.length
            r4 = 3
            if (r9 < r1) goto L_0x015b
            int r1 = r8.length
            int r1 = r1 * 3
            int r1 = r1 / 2
            double[] r1 = new double[r1]
            r5 = 0
            java.lang.System.arraycopy(r8, r5, r1, r5, r9)
            r8 = r1
        L_0x015b:
            int r1 = r9 + 1
            r8[r9] = r2
            r2 = 44
            if (r6 != r2) goto L_0x017a
            int r2 = r0.bp
            int r3 = r16 + 1
            int r2 = r2 + r16
            int r4 = r0.len
            if (r2 < r4) goto L_0x0170
            r6 = 26
            goto L_0x0176
        L_0x0170:
            java.lang.String r4 = r0.text
            char r6 = r4.charAt(r2)
        L_0x0176:
            r16 = r3
            goto L_0x020f
        L_0x017a:
            r3 = 93
            if (r6 != r3) goto L_0x020f
            int r5 = r0.bp
            int r6 = r16 + 1
            int r5 = r5 + r16
            int r9 = r0.len
            if (r5 < r9) goto L_0x018b
            r5 = 26
            goto L_0x0191
        L_0x018b:
            java.lang.String r9 = r0.text
            char r5 = r9.charAt(r5)
        L_0x0191:
            int r9 = r8.length
            if (r1 == r9) goto L_0x019b
            double[] r9 = new double[r1]
            r10 = 0
            java.lang.System.arraycopy(r8, r10, r9, r10, r1)
            r8 = r9
        L_0x019b:
            if (r5 != r2) goto L_0x01ad
            int r1 = r0.bp
            int r6 = r6 - r11
            int r1 = r1 + r6
            r0.bp = r1
            r18.next()
            r0.matchStat = r4
            r1 = 16
            r0.token = r1
            return r8
        L_0x01ad:
            r1 = 125(0x7d, float:1.75E-43)
            if (r5 != r1) goto L_0x020b
            int r4 = r0.bp
            int r5 = r6 + 1
            int r4 = r4 + r6
            int r6 = r0.len
            if (r4 < r6) goto L_0x01bd
            r6 = 26
            goto L_0x01c3
        L_0x01bd:
            java.lang.String r6 = r0.text
            char r6 = r6.charAt(r4)
        L_0x01c3:
            if (r6 != r2) goto L_0x01d3
            r2 = 16
            r0.token = r2
            int r1 = r0.bp
            int r5 = r5 - r11
            int r1 = r1 + r5
            r0.bp = r1
            r18.next()
            goto L_0x0203
        L_0x01d3:
            if (r6 != r3) goto L_0x01e3
            r1 = 15
            r0.token = r1
            int r1 = r0.bp
            int r5 = r5 - r11
            int r1 = r1 + r5
            r0.bp = r1
            r18.next()
            goto L_0x0203
        L_0x01e3:
            if (r6 != r1) goto L_0x01f3
            r1 = 13
            r0.token = r1
            int r1 = r0.bp
            int r5 = r5 - r11
            int r1 = r1 + r5
            r0.bp = r1
            r18.next()
            goto L_0x0203
        L_0x01f3:
            r3 = 26
            if (r6 != r3) goto L_0x0207
            int r1 = r0.bp
            int r5 = r5 - r11
            int r1 = r1 + r5
            r0.bp = r1
            r1 = 20
            r0.token = r1
            r0.ch = r3
        L_0x0203:
            r1 = 4
            r0.matchStat = r1
            return r8
        L_0x0207:
            r0.matchStat = r7
            r4 = 0
            return r4
        L_0x020b:
            r4 = 0
            r0.matchStat = r7
            return r4
        L_0x020f:
            r2 = 16
            r3 = 26
            r4 = 0
            r10 = 0
            r9 = r1
            r3 = r4
            r2 = r6
            r4 = r16
            r1 = 0
            r5 = 16
            goto L_0x003e
        L_0x021f:
            r4 = r3
            r0.matchStat = r7
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanFieldDoubleArray(long):double[]");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0144  */
    public final double[][] scanFieldDoubleArray2(long r20) {
        /*
            r19 = this;
            r0 = r19
            r1 = 0
            r0.matchStat = r1
            int r2 = r19.matchFieldHash(r20)
            r3 = 0
            if (r2 != 0) goto L_0x000f
            double[][] r3 = (double[][]) r3
            return r3
        L_0x000f:
            int r4 = r0.bp
            int r5 = r2 + 1
            int r4 = r4 + r2
            int r2 = r0.len
            if (r4 < r2) goto L_0x001b
            r2 = 26
            goto L_0x0021
        L_0x001b:
            java.lang.String r2 = r0.text
            char r2 = r2.charAt(r4)
        L_0x0021:
            r4 = 91
            r7 = -1
            if (r2 == r4) goto L_0x002b
            r0.matchStat = r7
            double[][] r3 = (double[][]) r3
            return r3
        L_0x002b:
            int r2 = r0.bp
            int r8 = r5 + 1
            int r2 = r2 + r5
            int r5 = r0.len
            if (r2 < r5) goto L_0x0037
            r2 = 26
            goto L_0x003d
        L_0x0037:
            java.lang.String r5 = r0.text
            char r2 = r5.charAt(r2)
        L_0x003d:
            r5 = 16
            double[][] r9 = new double[r5][]
            r10 = r9
            r9 = 0
        L_0x0043:
            if (r2 != r4) goto L_0x02b0
            int r2 = r0.bp
            int r11 = r8 + 1
            int r2 = r2 + r8
            int r8 = r0.len
            if (r2 < r8) goto L_0x0051
            r2 = 26
            goto L_0x0057
        L_0x0051:
            java.lang.String r8 = r0.text
            char r2 = r8.charAt(r2)
        L_0x0057:
            double[] r8 = new double[r5]
            r12 = 0
        L_0x005a:
            int r13 = r0.bp
            int r13 = r13 + r11
            r14 = 1
            int r13 = r13 - r14
            r15 = 45
            if (r2 != r15) goto L_0x0066
            r16 = 1
            goto L_0x0068
        L_0x0066:
            r16 = 0
        L_0x0068:
            if (r16 == 0) goto L_0x007d
            int r2 = r0.bp
            int r17 = r11 + 1
            int r2 = r2 + r11
            int r11 = r0.len
            if (r2 < r11) goto L_0x0076
            r2 = 26
            goto L_0x007f
        L_0x0076:
            java.lang.String r11 = r0.text
            char r2 = r11.charAt(r2)
            goto L_0x007f
        L_0x007d:
            r17 = r11
        L_0x007f:
            r11 = 48
            if (r2 < r11) goto L_0x02a9
            r4 = 57
            if (r2 > r4) goto L_0x02a9
            int r2 = r2 + -48
        L_0x0089:
            int r6 = r0.bp
            int r18 = r17 + 1
            int r6 = r6 + r17
            int r5 = r0.len
            if (r6 < r5) goto L_0x0096
            r6 = 26
            goto L_0x009c
        L_0x0096:
            java.lang.String r5 = r0.text
            char r6 = r5.charAt(r6)
        L_0x009c:
            if (r6 < r11) goto L_0x00aa
            if (r6 > r4) goto L_0x00aa
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            r17 = r18
            r5 = 16
            goto L_0x0089
        L_0x00aa:
            r5 = 46
            if (r6 != r5) goto L_0x00f1
            int r5 = r0.bp
            int r6 = r18 + 1
            int r5 = r5 + r18
            int r1 = r0.len
            if (r5 < r1) goto L_0x00bb
            r1 = 26
            goto L_0x00c1
        L_0x00bb:
            java.lang.String r1 = r0.text
            char r1 = r1.charAt(r5)
        L_0x00c1:
            if (r1 < r11) goto L_0x00ec
            if (r1 > r4) goto L_0x00ec
            int r2 = r2 * 10
            int r1 = r1 + -48
            int r2 = r2 + r1
            r1 = 10
        L_0x00cc:
            int r5 = r0.bp
            int r17 = r6 + 1
            int r5 = r5 + r6
            int r6 = r0.len
            if (r5 < r6) goto L_0x00d8
            r6 = 26
            goto L_0x00de
        L_0x00d8:
            java.lang.String r6 = r0.text
            char r6 = r6.charAt(r5)
        L_0x00de:
            if (r6 < r11) goto L_0x00f4
            if (r6 > r4) goto L_0x00f4
            int r2 = r2 * 10
            int r6 = r6 + -48
            int r2 = r2 + r6
            int r1 = r1 * 10
            r6 = r17
            goto L_0x00cc
        L_0x00ec:
            r0.matchStat = r7
            double[][] r3 = (double[][]) r3
            return r3
        L_0x00f1:
            r17 = r18
            r1 = 1
        L_0x00f4:
            r5 = 101(0x65, float:1.42E-43)
            if (r6 == r5) goto L_0x00ff
            r5 = 69
            if (r6 != r5) goto L_0x00fd
            goto L_0x00ff
        L_0x00fd:
            r5 = 0
            goto L_0x0100
        L_0x00ff:
            r5 = 1
        L_0x0100:
            if (r5 == 0) goto L_0x014b
            int r6 = r0.bp
            int r18 = r17 + 1
            int r6 = r6 + r17
            int r3 = r0.len
            if (r6 < r3) goto L_0x010f
            r6 = 26
            goto L_0x0115
        L_0x010f:
            java.lang.String r3 = r0.text
            char r6 = r3.charAt(r6)
        L_0x0115:
            r3 = 43
            if (r6 == r3) goto L_0x011f
            if (r6 != r15) goto L_0x011c
            goto L_0x011f
        L_0x011c:
            r17 = r18
            goto L_0x0135
        L_0x011f:
            int r3 = r0.bp
            int r6 = r18 + 1
            int r3 = r3 + r18
            int r15 = r0.len
            if (r3 < r15) goto L_0x012c
        L_0x0129:
            r3 = 26
            goto L_0x0132
        L_0x012c:
            java.lang.String r15 = r0.text
            char r3 = r15.charAt(r3)
        L_0x0132:
            r17 = r6
            r6 = r3
        L_0x0135:
            if (r6 < r11) goto L_0x014b
            if (r6 > r4) goto L_0x014b
            int r3 = r0.bp
            int r6 = r17 + 1
            int r3 = r3 + r17
            int r15 = r0.len
            if (r3 < r15) goto L_0x0144
            goto L_0x0129
        L_0x0144:
            java.lang.String r15 = r0.text
            char r3 = r15.charAt(r3)
            goto L_0x0132
        L_0x014b:
            int r3 = r0.bp
            int r3 = r3 + r17
            int r3 = r3 - r13
            int r3 = r3 - r14
            if (r5 != 0) goto L_0x0164
            r4 = 10
            if (r3 >= r4) goto L_0x0164
            double r2 = (double) r2
            double r4 = (double) r1
            java.lang.Double.isNaN(r2)
            java.lang.Double.isNaN(r4)
            double r2 = r2 / r4
            if (r16 == 0) goto L_0x016c
            double r2 = -r2
            goto L_0x016c
        L_0x0164:
            java.lang.String r1 = r0.subString(r13, r3)
            double r2 = java.lang.Double.parseDouble(r1)
        L_0x016c:
            int r1 = r8.length
            r4 = 3
            if (r12 < r1) goto L_0x017c
            int r1 = r8.length
            int r1 = r1 * 3
            int r1 = r1 / 2
            double[] r1 = new double[r1]
            r5 = 0
            java.lang.System.arraycopy(r8, r5, r1, r5, r12)
            r8 = r1
        L_0x017c:
            int r1 = r12 + 1
            r8[r12] = r2
            r2 = 44
            if (r6 != r2) goto L_0x01a2
            int r2 = r0.bp
            int r3 = r17 + 1
            int r2 = r2 + r17
            int r4 = r0.len
            if (r2 < r4) goto L_0x0191
            r6 = 26
            goto L_0x0197
        L_0x0191:
            java.lang.String r4 = r0.text
            char r6 = r4.charAt(r2)
        L_0x0197:
            r17 = r3
            r2 = r6
            r3 = 26
            r4 = 16
            r11 = 0
            r12 = 0
            goto L_0x029e
        L_0x01a2:
            r3 = 93
            if (r6 != r3) goto L_0x0297
            int r5 = r0.bp
            int r6 = r17 + 1
            int r5 = r5 + r17
            int r11 = r0.len
            if (r5 < r11) goto L_0x01b3
            r5 = 26
            goto L_0x01b9
        L_0x01b3:
            java.lang.String r11 = r0.text
            char r5 = r11.charAt(r5)
        L_0x01b9:
            int r11 = r8.length
            if (r1 == r11) goto L_0x01c4
            double[] r11 = new double[r1]
            r12 = 0
            java.lang.System.arraycopy(r8, r12, r11, r12, r1)
            r8 = r11
            goto L_0x01c5
        L_0x01c4:
            r12 = 0
        L_0x01c5:
            int r11 = r10.length
            if (r9 < r11) goto L_0x01d2
            int r10 = r10.length
            int r10 = r10 * 3
            int r10 = r10 / 2
            double[][] r10 = new double[r10][]
            java.lang.System.arraycopy(r8, r12, r10, r12, r1)
        L_0x01d2:
            int r1 = r9 + 1
            r10[r9] = r8
            if (r5 != r2) goto L_0x01f4
            int r2 = r0.bp
            int r3 = r6 + 1
            int r2 = r2 + r6
            int r4 = r0.len
            if (r2 < r4) goto L_0x01e4
            r6 = 26
            goto L_0x01ea
        L_0x01e4:
            java.lang.String r4 = r0.text
            char r6 = r4.charAt(r2)
        L_0x01ea:
            r8 = r3
            r2 = r6
            r3 = 26
            r4 = 16
            r11 = 0
            r12 = 0
            goto L_0x028e
        L_0x01f4:
            if (r5 != r3) goto L_0x0286
            int r5 = r0.bp
            int r8 = r6 + 1
            int r5 = r5 + r6
            int r6 = r0.len
            if (r5 < r6) goto L_0x0202
            r6 = 26
            goto L_0x0208
        L_0x0202:
            java.lang.String r6 = r0.text
            char r6 = r6.charAt(r5)
        L_0x0208:
            int r5 = r10.length
            if (r1 == r5) goto L_0x0212
            double[][] r5 = new double[r1][]
            r11 = 0
            java.lang.System.arraycopy(r10, r11, r5, r11, r1)
            goto L_0x0213
        L_0x0212:
            r5 = r10
        L_0x0213:
            if (r6 != r2) goto L_0x0225
            int r1 = r0.bp
            int r8 = r8 - r14
            int r1 = r1 + r8
            r0.bp = r1
            r19.next()
            r0.matchStat = r4
            r4 = 16
            r0.token = r4
            return r5
        L_0x0225:
            r4 = 16
            r1 = 125(0x7d, float:1.75E-43)
            if (r6 != r1) goto L_0x027f
            int r1 = r0.bp
            int r6 = r8 + 1
            int r1 = r1 + r8
            char r1 = r0.charAt(r1)
            if (r1 != r2) goto L_0x0242
            r0.token = r4
            int r1 = r0.bp
            int r6 = r6 - r14
            int r1 = r1 + r6
            r0.bp = r1
            r19.next()
            goto L_0x0274
        L_0x0242:
            if (r1 != r3) goto L_0x0252
            r1 = 15
            r0.token = r1
            int r1 = r0.bp
            int r6 = r6 - r14
            int r1 = r1 + r6
            r0.bp = r1
            r19.next()
            goto L_0x0274
        L_0x0252:
            r2 = 125(0x7d, float:1.75E-43)
            if (r1 != r2) goto L_0x0264
            r1 = 13
            r0.token = r1
            int r1 = r0.bp
            int r6 = r6 - r14
            int r1 = r1 + r6
            r0.bp = r1
            r19.next()
            goto L_0x0274
        L_0x0264:
            r3 = 26
            if (r1 != r3) goto L_0x0278
            int r1 = r0.bp
            int r6 = r6 - r14
            int r1 = r1 + r6
            r0.bp = r1
            r1 = 20
            r0.token = r1
            r0.ch = r3
        L_0x0274:
            r1 = 4
            r0.matchStat = r1
            return r5
        L_0x0278:
            r0.matchStat = r7
            r12 = 0
            r3 = r12
            double[][] r3 = (double[][]) r3
            return r3
        L_0x027f:
            r12 = 0
            r0.matchStat = r7
            r3 = r12
            double[][] r3 = (double[][]) r3
            return r3
        L_0x0286:
            r3 = 26
            r4 = 16
            r11 = 0
            r12 = 0
            r2 = r5
            r8 = r6
        L_0x028e:
            r9 = r1
            r3 = r12
            r1 = 0
            r4 = 91
            r5 = 16
            goto L_0x0043
        L_0x0297:
            r3 = 26
            r4 = 16
            r11 = 0
            r12 = 0
            r2 = r6
        L_0x029e:
            r3 = r12
            r11 = r17
            r4 = 91
            r5 = 16
            r12 = r1
            r1 = 0
            goto L_0x005a
        L_0x02a9:
            r12 = r3
            r0.matchStat = r7
            r3 = r12
            double[][] r3 = (double[][]) r3
            return r3
        L_0x02b0:
            r4 = 91
            goto L_0x0043
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanFieldDoubleArray2(long):double[][]");
    }

    public long scanFieldSymbol(long j) {
        char c;
        char c2;
        char c3;
        char c4;
        this.matchStat = 0;
        int matchFieldHash = matchFieldHash(j);
        if (matchFieldHash == 0) {
            return 0;
        }
        int i = matchFieldHash + 1;
        int i2 = this.bp + matchFieldHash;
        int i3 = this.len;
        char c5 = EOI;
        if (i2 >= i3) {
            c = EOI;
        } else {
            c = this.text.charAt(i2);
        }
        if (c != '\"') {
            this.matchStat = -1;
            return 0;
        }
        long j2 = -3750763034362895579L;
        int i4 = this.bp;
        while (true) {
            int i5 = i + 1;
            int i6 = this.bp + i;
            if (i6 >= this.len) {
                c2 = EOI;
            } else {
                c2 = this.text.charAt(i6);
            }
            if (c2 == '\"') {
                int i7 = i5 + 1;
                int i8 = this.bp + i5;
                if (i8 >= this.len) {
                    c3 = EOI;
                } else {
                    c3 = this.text.charAt(i8);
                }
                if (c3 == ',') {
                    this.bp += i7 - 1;
                    int i9 = this.bp + 1;
                    this.bp = i9;
                    if (i9 < this.len) {
                        c5 = this.text.charAt(i9);
                    }
                    this.ch = c5;
                    this.matchStat = 3;
                    return j2;
                } else if (c3 == '}') {
                    int i10 = i7 + 1;
                    int i11 = this.bp + i7;
                    if (i11 >= this.len) {
                        c4 = EOI;
                    } else {
                        c4 = this.text.charAt(i11);
                    }
                    if (c4 == ',') {
                        this.token = 16;
                        this.bp += i10 - 1;
                        next();
                    } else if (c4 == ']') {
                        this.token = 15;
                        this.bp += i10 - 1;
                        next();
                    } else if (c4 == '}') {
                        this.token = 13;
                        this.bp += i10 - 1;
                        next();
                    } else if (c4 == 26) {
                        this.token = 20;
                        this.bp += i10 - 1;
                        this.ch = EOI;
                    } else {
                        this.matchStat = -1;
                        return 0;
                    }
                    this.matchStat = 4;
                    return j2;
                } else {
                    this.matchStat = -1;
                    return 0;
                }
            } else {
                j2 = (j2 ^ ((long) c2)) * 1099511628211L;
                if (c2 == '\\') {
                    this.matchStat = -1;
                    return 0;
                }
                i = i5;
            }
        }
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        return scanISO8601DateIfMatch(z, this.len - this.bp);
    }

    /* JADX WARNING: Removed duplicated region for block: B:111:0x0227 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0228  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanISO8601DateIfMatch(boolean r39, int r40) {
        /*
            r38 = this;
            r9 = r38
            r11 = r40
            r12 = 57
            r13 = 6
            r14 = 3
            r15 = 2
            r8 = 5
            r16 = 1
            r7 = 48
            r6 = 0
            if (r39 != 0) goto L_0x00ae
            r0 = 13
            if (r11 <= r0) goto L_0x00ae
            int r0 = r9.bp
            char r0 = r9.charAt(r0)
            int r1 = r9.bp
            int r1 = r1 + 1
            char r1 = r9.charAt(r1)
            int r2 = r9.bp
            int r2 = r2 + r15
            char r2 = r9.charAt(r2)
            int r3 = r9.bp
            int r3 = r3 + r14
            char r3 = r9.charAt(r3)
            int r4 = r9.bp
            int r4 = r4 + 4
            char r4 = r9.charAt(r4)
            int r5 = r9.bp
            int r5 = r5 + r8
            char r5 = r9.charAt(r5)
            int r14 = r9.bp
            int r14 = r14 + r11
            int r14 = r14 + -1
            char r14 = r9.charAt(r14)
            int r8 = r9.bp
            int r8 = r8 + r11
            int r8 = r8 - r15
            char r8 = r9.charAt(r8)
            r15 = 47
            if (r0 != r15) goto L_0x00ae
            r0 = 68
            if (r1 != r0) goto L_0x00ae
            r0 = 97
            if (r2 != r0) goto L_0x00ae
            r0 = 116(0x74, float:1.63E-43)
            if (r3 != r0) goto L_0x00ae
            r0 = 101(0x65, float:1.42E-43)
            if (r4 != r0) goto L_0x00ae
            r0 = 40
            if (r5 != r0) goto L_0x00ae
            r0 = 47
            if (r14 != r0) goto L_0x00ae
            r0 = 41
            if (r8 != r0) goto L_0x00ae
            r0 = -1
            r0 = 6
            r1 = -1
        L_0x0074:
            if (r0 >= r11) goto L_0x008b
            int r2 = r9.bp
            int r2 = r2 + r0
            char r2 = r9.charAt(r2)
            r3 = 43
            if (r2 != r3) goto L_0x0083
            r1 = r0
            goto L_0x0088
        L_0x0083:
            if (r2 < r7) goto L_0x008b
            if (r2 <= r12) goto L_0x0088
            goto L_0x008b
        L_0x0088:
            int r0 = r0 + 1
            goto L_0x0074
        L_0x008b:
            r0 = -1
            if (r1 != r0) goto L_0x008f
            return r6
        L_0x008f:
            int r0 = r9.bp
            int r0 = r0 + r13
            int r1 = r1 - r0
            java.lang.String r0 = r9.subString(r0, r1)
            long r0 = java.lang.Long.parseLong(r0)
            java.util.TimeZone r2 = r9.timeZone
            java.util.Locale r3 = r9.locale
            java.util.Calendar r2 = java.util.Calendar.getInstance(r2, r3)
            r9.calendar = r2
            java.util.Calendar r2 = r9.calendar
            r2.setTimeInMillis(r0)
            r0 = 5
            r9.token = r0
            return r16
        L_0x00ae:
            r15 = 8
            r8 = 9
            r5 = 14
            r4 = 45
            r20 = 10
            if (r11 == r15) goto L_0x04e4
            if (r11 == r5) goto L_0x04e4
            r0 = 16
            if (r11 != r0) goto L_0x00d0
            int r0 = r9.bp
            int r0 = r0 + 10
            char r0 = r9.charAt(r0)
            r1 = 84
            if (r0 == r1) goto L_0x04e4
            r1 = 32
            if (r0 == r1) goto L_0x04e4
        L_0x00d0:
            r0 = 17
            if (r11 != r0) goto L_0x00df
            int r0 = r9.bp
            int r0 = r0 + r13
            char r0 = r9.charAt(r0)
            if (r0 == r4) goto L_0x00df
            goto L_0x04e4
        L_0x00df:
            if (r11 >= r8) goto L_0x00e2
            return r6
        L_0x00e2:
            int r0 = r9.bp
            char r0 = r9.charAt(r0)
            int r1 = r9.bp
            int r1 = r1 + 1
            char r1 = r9.charAt(r1)
            int r2 = r9.bp
            r3 = 2
            int r2 = r2 + r3
            char r2 = r9.charAt(r2)
            int r3 = r9.bp
            r17 = 3
            int r3 = r3 + 3
            char r3 = r9.charAt(r3)
            int r5 = r9.bp
            int r5 = r5 + 4
            char r5 = r9.charAt(r5)
            int r7 = r9.bp
            r18 = 5
            int r7 = r7 + 5
            char r7 = r9.charAt(r7)
            int r12 = r9.bp
            int r12 = r12 + r13
            char r12 = r9.charAt(r12)
            int r13 = r9.bp
            int r13 = r13 + 7
            char r13 = r9.charAt(r13)
            int r14 = r9.bp
            int r14 = r14 + r15
            char r14 = r9.charAt(r14)
            int r15 = r9.bp
            int r15 = r15 + r8
            char r15 = r9.charAt(r15)
            if (r5 != r4) goto L_0x0135
            if (r13 == r4) goto L_0x013d
        L_0x0135:
            r8 = 47
            if (r5 != r8) goto L_0x014a
            r8 = 47
            if (r13 != r8) goto L_0x014a
        L_0x013d:
            r13 = r1
            r8 = r12
            r35 = r14
            r36 = r15
            r37 = 10
        L_0x0145:
            r12 = r0
            r14 = r2
            r15 = r3
            goto L_0x0211
        L_0x014a:
            if (r5 != r4) goto L_0x0170
            if (r12 != r4) goto L_0x0170
            r5 = 32
            if (r14 != r5) goto L_0x0161
            r12 = r0
            r14 = r2
            r15 = r3
            r8 = r7
            r36 = r13
            r7 = 48
            r35 = 48
            r37 = 8
        L_0x015e:
            r13 = r1
            goto L_0x0211
        L_0x0161:
            r12 = r0
            r15 = r3
            r8 = r7
            r35 = r13
            r36 = r14
            r7 = 48
            r37 = 9
        L_0x016c:
            r13 = r1
        L_0x016d:
            r14 = r2
            goto L_0x0211
        L_0x0170:
            r8 = 46
            if (r2 != r8) goto L_0x0178
            r8 = 46
            if (r7 == r8) goto L_0x017c
        L_0x0178:
            if (r2 != r4) goto L_0x0186
            if (r7 != r4) goto L_0x0186
        L_0x017c:
            r35 = r0
            r36 = r1
            r7 = r3
            r8 = r5
            r37 = 10
            goto L_0x0211
        L_0x0186:
            r8 = 24180(0x5e74, float:3.3883E-41)
            if (r5 == r8) goto L_0x0191
            r8 = 45380(0xb144, float:6.3591E-41)
            if (r5 != r8) goto L_0x0190
            goto L_0x0191
        L_0x0190:
            return r6
        L_0x0191:
            r5 = 26376(0x6708, float:3.696E-41)
            if (r13 == r5) goto L_0x01d4
            r5 = 50900(0xc6d4, float:7.1326E-41)
            if (r13 != r5) goto L_0x019b
            goto L_0x01d4
        L_0x019b:
            r5 = 26376(0x6708, float:3.696E-41)
            if (r12 == r5) goto L_0x01a6
            r5 = 50900(0xc6d4, float:7.1326E-41)
            if (r12 != r5) goto L_0x01a5
            goto L_0x01a6
        L_0x01a5:
            return r6
        L_0x01a6:
            r5 = 26085(0x65e5, float:3.6553E-41)
            if (r14 == r5) goto L_0x01c7
            r5 = 51068(0xc77c, float:7.1562E-41)
            if (r14 != r5) goto L_0x01b0
            goto L_0x01c7
        L_0x01b0:
            r5 = 26085(0x65e5, float:3.6553E-41)
            if (r15 == r5) goto L_0x01bb
            r5 = 51068(0xc77c, float:7.1562E-41)
            if (r15 != r5) goto L_0x01ba
            goto L_0x01bb
        L_0x01ba:
            return r6
        L_0x01bb:
            r12 = r0
            r15 = r3
            r8 = r7
            r35 = r13
            r36 = r14
            r7 = 48
            r37 = 10
            goto L_0x016c
        L_0x01c7:
            r12 = r0
            r14 = r2
            r15 = r3
            r8 = r7
            r36 = r13
            r7 = 48
            r35 = 48
            r37 = 10
            goto L_0x015e
        L_0x01d4:
            r5 = 26085(0x65e5, float:3.6553E-41)
            if (r15 == r5) goto L_0x0205
            r5 = 51068(0xc77c, float:7.1562E-41)
            if (r15 != r5) goto L_0x01de
            goto L_0x0205
        L_0x01de:
            int r5 = r9.bp
            int r5 = r5 + 10
            char r5 = r9.charAt(r5)
            r8 = 26085(0x65e5, float:3.6553E-41)
            if (r5 == r8) goto L_0x01f9
            int r5 = r9.bp
            int r5 = r5 + 10
            char r5 = r9.charAt(r5)
            r8 = 51068(0xc77c, float:7.1562E-41)
            if (r5 != r8) goto L_0x01f8
            goto L_0x01f9
        L_0x01f8:
            return r6
        L_0x01f9:
            r5 = 11
            r13 = r1
            r8 = r12
            r35 = r14
            r36 = r15
            r37 = 11
            goto L_0x0145
        L_0x0205:
            r13 = r1
            r15 = r3
            r8 = r12
            r36 = r14
            r35 = 48
            r37 = 10
            r12 = r0
            goto L_0x016d
        L_0x0211:
            r27 = r12
            r28 = r13
            r29 = r14
            r30 = r15
            r31 = r7
            r32 = r8
            r33 = r35
            r34 = r36
            boolean r0 = checkDate(r27, r28, r29, r30, r31, r32, r33, r34)
            if (r0 != 0) goto L_0x0228
            return r6
        L_0x0228:
            r0 = r38
            r1 = r12
            r2 = r13
            r3 = r14
            r12 = 45
            r4 = r15
            r13 = 14
            r5 = r7
            r14 = 0
            r6 = r8
            r15 = 48
            r7 = r35
            r13 = 5
            r18 = 9
            r8 = r36
            r0.setCalendar(r1, r2, r3, r4, r5, r6, r7, r8)
            int r0 = r9.bp
            int r0 = r0 + r37
            char r7 = r9.charAt(r0)
            r0 = 84
            if (r7 == r0) goto L_0x02f8
            r0 = 32
            if (r7 != r0) goto L_0x0255
            if (r39 != 0) goto L_0x0255
            goto L_0x02f8
        L_0x0255:
            r0 = 34
            if (r7 == r0) goto L_0x02cd
            r0 = 26
            if (r7 == r0) goto L_0x02cd
            r0 = 26085(0x65e5, float:3.6553E-41)
            if (r7 == r0) goto L_0x02cd
            r0 = 51068(0xc77c, float:7.1562E-41)
            if (r7 != r0) goto L_0x0267
            goto L_0x02cd
        L_0x0267:
            r0 = 43
            if (r7 == r0) goto L_0x026f
            if (r7 != r12) goto L_0x026e
            goto L_0x026f
        L_0x026e:
            return r14
        L_0x026f:
            int r0 = r9.len
            int r1 = r37 + 6
            if (r0 != r1) goto L_0x02cc
            int r0 = r9.bp
            int r0 = r0 + r37
            r1 = 3
            int r0 = r0 + r1
            char r0 = r9.charAt(r0)
            r1 = 58
            if (r0 != r1) goto L_0x02cb
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + 4
            char r0 = r9.charAt(r0)
            if (r0 != r15) goto L_0x02cb
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + r13
            char r0 = r9.charAt(r0)
            if (r0 == r15) goto L_0x029b
            goto L_0x02cb
        L_0x029b:
            r1 = 48
            r2 = 48
            r3 = 48
            r4 = 48
            r5 = 48
            r6 = 48
            r0 = r38
            r0.setTime(r1, r2, r3, r4, r5, r6)
            java.util.Calendar r0 = r9.calendar
            r1 = 14
            r0.set(r1, r14)
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + 1
            char r0 = r9.charAt(r0)
            int r1 = r9.bp
            int r1 = r1 + r37
            r2 = 2
            int r1 = r1 + r2
            char r1 = r9.charAt(r1)
            r9.setTimeZone(r7, r0, r1)
            return r16
        L_0x02cb:
            return r14
        L_0x02cc:
            return r14
        L_0x02cd:
            java.util.Calendar r0 = r9.calendar
            r1 = 11
            r0.set(r1, r14)
            java.util.Calendar r0 = r9.calendar
            r1 = 12
            r0.set(r1, r14)
            java.util.Calendar r0 = r9.calendar
            r1 = 13
            r0.set(r1, r14)
            java.util.Calendar r0 = r9.calendar
            r1 = 14
            r0.set(r1, r14)
            int r0 = r9.bp
            int r0 = r0 + r37
            r9.bp = r0
            char r0 = r9.charAt(r0)
            r9.ch = r0
            r9.token = r13
            return r16
        L_0x02f8:
            int r7 = r37 + 9
            if (r11 >= r7) goto L_0x02fd
            return r14
        L_0x02fd:
            int r0 = r9.bp
            int r0 = r0 + r37
            r1 = 3
            int r0 = r0 + r1
            char r0 = r9.charAt(r0)
            r1 = 58
            if (r0 == r1) goto L_0x030c
            return r14
        L_0x030c:
            int r0 = r9.bp
            int r0 = r0 + r37
            r2 = 6
            int r0 = r0 + r2
            char r0 = r9.charAt(r0)
            if (r0 == r1) goto L_0x0319
            return r14
        L_0x0319:
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + 1
            char r8 = r9.charAt(r0)
            int r0 = r9.bp
            int r0 = r0 + r37
            r1 = 2
            int r0 = r0 + r1
            char r10 = r9.charAt(r0)
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + 4
            char r21 = r9.charAt(r0)
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + r13
            char r22 = r9.charAt(r0)
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + 7
            char r26 = r9.charAt(r0)
            int r0 = r9.bp
            int r0 = r0 + r37
            r1 = 8
            int r0 = r0 + r1
            char r25 = r9.charAt(r0)
            r1 = r8
            r2 = r10
            r3 = r21
            r4 = r22
            r5 = r26
            r6 = r25
            boolean r0 = checkTime(r1, r2, r3, r4, r5, r6)
            if (r0 != 0) goto L_0x0366
            return r14
        L_0x0366:
            r0 = r38
            r1 = r8
            r2 = r10
            r3 = r21
            r4 = r22
            r5 = r26
            r6 = r25
            r0.setTime(r1, r2, r3, r4, r5, r6)
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + 9
            char r0 = r9.charAt(r0)
            r1 = 46
            if (r0 != r1) goto L_0x04ad
            int r0 = r37 + 11
            if (r11 >= r0) goto L_0x0388
            return r14
        L_0x0388:
            int r1 = r9.bp
            int r1 = r1 + r37
            int r1 = r1 + 10
            char r1 = r9.charAt(r1)
            if (r1 < r15) goto L_0x04ac
            r2 = 57
            if (r1 <= r2) goto L_0x039a
            goto L_0x04ac
        L_0x039a:
            int r1 = r1 - r15
            if (r11 <= r0) goto L_0x03b1
            int r0 = r9.bp
            int r0 = r0 + r37
            int r0 = r0 + 11
            char r0 = r9.charAt(r0)
            if (r0 < r15) goto L_0x03b1
            if (r0 > r2) goto L_0x03b1
            int r1 = r1 * 10
            int r0 = r0 - r15
            int r1 = r1 + r0
            r0 = 2
            goto L_0x03b2
        L_0x03b1:
            r0 = 1
        L_0x03b2:
            r2 = 2
            if (r0 != r2) goto L_0x03ca
            int r2 = r9.bp
            int r2 = r2 + r37
            int r2 = r2 + 12
            char r2 = r9.charAt(r2)
            if (r2 < r15) goto L_0x03ca
            r3 = 57
            if (r2 > r3) goto L_0x03ca
            int r1 = r1 * 10
            int r2 = r2 - r15
            int r1 = r1 + r2
            r0 = 3
        L_0x03ca:
            java.util.Calendar r2 = r9.calendar
            r3 = 14
            r2.set(r3, r1)
            int r1 = r9.bp
            int r1 = r1 + r37
            int r1 = r1 + 10
            int r1 = r1 + r0
            char r1 = r9.charAt(r1)
            r2 = 43
            if (r1 == r2) goto L_0x040d
            if (r1 != r12) goto L_0x03e3
            goto L_0x040d
        L_0x03e3:
            r2 = 90
            if (r1 != r2) goto L_0x0409
            java.util.Calendar r1 = r9.calendar
            java.util.TimeZone r1 = r1.getTimeZone()
            int r1 = r1.getRawOffset()
            if (r1 == 0) goto L_0x0405
            java.lang.String[] r1 = java.util.TimeZone.getAvailableIDs(r14)
            int r2 = r1.length
            if (r2 <= 0) goto L_0x0405
            r1 = r1[r14]
            java.util.TimeZone r1 = java.util.TimeZone.getTimeZone(r1)
            java.util.Calendar r2 = r9.calendar
            r2.setTimeZone(r1)
        L_0x0405:
            r17 = 1
            goto L_0x0484
        L_0x0409:
            r17 = 0
            goto L_0x0484
        L_0x040d:
            int r2 = r9.bp
            int r2 = r2 + r37
            int r2 = r2 + 10
            int r2 = r2 + r0
            int r2 = r2 + 1
            char r2 = r9.charAt(r2)
            if (r2 < r15) goto L_0x04ab
            r3 = 49
            if (r2 <= r3) goto L_0x0422
            goto L_0x04ab
        L_0x0422:
            int r3 = r9.bp
            int r3 = r3 + r37
            int r3 = r3 + 10
            int r3 = r3 + r0
            r4 = 2
            int r3 = r3 + r4
            char r3 = r9.charAt(r3)
            if (r3 < r15) goto L_0x04aa
            r4 = 57
            if (r3 <= r4) goto L_0x0437
            goto L_0x04aa
        L_0x0437:
            int r4 = r9.bp
            int r4 = r4 + r37
            int r4 = r4 + 10
            int r4 = r4 + r0
            r5 = 3
            int r4 = r4 + r5
            char r4 = r9.charAt(r4)
            r5 = 58
            if (r4 != r5) goto L_0x046a
            int r4 = r9.bp
            int r4 = r4 + r37
            int r4 = r4 + 10
            int r4 = r4 + r0
            int r4 = r4 + 4
            char r4 = r9.charAt(r4)
            if (r4 == r15) goto L_0x0458
            return r14
        L_0x0458:
            int r4 = r9.bp
            int r4 = r4 + r37
            int r4 = r4 + 10
            int r4 = r4 + r0
            int r4 = r4 + r13
            char r4 = r9.charAt(r4)
            if (r4 == r15) goto L_0x0467
            return r14
        L_0x0467:
            r17 = 6
            goto L_0x0481
        L_0x046a:
            if (r4 != r15) goto L_0x047f
            int r4 = r9.bp
            int r4 = r4 + r37
            int r4 = r4 + 10
            int r4 = r4 + r0
            int r4 = r4 + 4
            char r4 = r9.charAt(r4)
            if (r4 == r15) goto L_0x047c
            return r14
        L_0x047c:
            r17 = 5
            goto L_0x0481
        L_0x047f:
            r17 = 3
        L_0x0481:
            r9.setTimeZone(r1, r2, r3)
        L_0x0484:
            int r1 = r9.bp
            int r37 = r37 + 10
            int r37 = r37 + r0
            int r37 = r37 + r17
            int r1 = r1 + r37
            char r0 = r9.charAt(r1)
            r1 = 26
            if (r0 == r1) goto L_0x049b
            r1 = 34
            if (r0 == r1) goto L_0x049b
            return r14
        L_0x049b:
            int r0 = r9.bp
            int r0 = r0 + r37
            r9.bp = r0
            char r0 = r9.charAt(r0)
            r9.ch = r0
            r9.token = r13
            return r16
        L_0x04aa:
            return r14
        L_0x04ab:
            return r14
        L_0x04ac:
            return r14
        L_0x04ad:
            java.util.Calendar r1 = r9.calendar
            r2 = 14
            r1.set(r2, r14)
            int r1 = r9.bp
            int r1 = r1 + r7
            r9.bp = r1
            char r1 = r9.charAt(r1)
            r9.ch = r1
            r9.token = r13
            r1 = 90
            if (r0 != r1) goto L_0x04e3
            java.util.Calendar r0 = r9.calendar
            java.util.TimeZone r0 = r0.getTimeZone()
            int r0 = r0.getRawOffset()
            if (r0 == 0) goto L_0x04e3
            java.lang.String[] r0 = java.util.TimeZone.getAvailableIDs(r14)
            int r1 = r0.length
            if (r1 <= 0) goto L_0x04e3
            r0 = r0[r14]
            java.util.TimeZone r0 = java.util.TimeZone.getTimeZone(r0)
            java.util.Calendar r1 = r9.calendar
            r1.setTimeZone(r0)
        L_0x04e3:
            return r16
        L_0x04e4:
            r12 = 45
            r13 = 5
            r14 = 0
            r15 = 48
            r18 = 9
            if (r39 == 0) goto L_0x04ef
            return r14
        L_0x04ef:
            int r0 = r9.bp
            char r10 = r9.charAt(r0)
            int r0 = r9.bp
            int r0 = r0 + 1
            char r21 = r9.charAt(r0)
            int r0 = r9.bp
            r1 = 2
            int r0 = r0 + r1
            char r19 = r9.charAt(r0)
            int r0 = r9.bp
            r1 = 3
            int r0 = r0 + r1
            char r17 = r9.charAt(r0)
            int r0 = r9.bp
            int r0 = r0 + 4
            char r0 = r9.charAt(r0)
            int r1 = r9.bp
            int r1 = r1 + r13
            char r1 = r9.charAt(r1)
            int r2 = r9.bp
            r3 = 6
            int r2 = r2 + r3
            char r2 = r9.charAt(r2)
            int r3 = r9.bp
            int r3 = r3 + 7
            char r3 = r9.charAt(r3)
            int r4 = r9.bp
            r5 = 8
            int r4 = r4 + r5
            char r22 = r9.charAt(r4)
            if (r0 != r12) goto L_0x053b
            if (r3 != r12) goto L_0x053b
            r4 = 1
            goto L_0x053c
        L_0x053b:
            r4 = 0
        L_0x053c:
            if (r4 == 0) goto L_0x0544
            r5 = 16
            if (r11 != r5) goto L_0x0544
            r12 = 1
            goto L_0x0545
        L_0x0544:
            r12 = 0
        L_0x0545:
            if (r4 == 0) goto L_0x054e
            r4 = 17
            if (r11 != r4) goto L_0x054e
            r23 = 1
            goto L_0x0550
        L_0x054e:
            r23 = 0
        L_0x0550:
            if (r23 != 0) goto L_0x055e
            if (r12 == 0) goto L_0x0555
            goto L_0x055e
        L_0x0555:
            r26 = r0
            r27 = r1
            r28 = r2
            r29 = r3
            goto L_0x056e
        L_0x055e:
            int r0 = r9.bp
            int r0 = r0 + 9
            char r0 = r9.charAt(r0)
            r29 = r0
            r26 = r1
            r27 = r2
            r28 = r22
        L_0x056e:
            r1 = r10
            r2 = r21
            r3 = r19
            r4 = r17
            r5 = r26
            r6 = r27
            r7 = r28
            r8 = r29
            boolean r0 = checkDate(r1, r2, r3, r4, r5, r6, r7, r8)
            if (r0 != 0) goto L_0x0584
            return r14
        L_0x0584:
            r0 = r38
            r1 = r10
            r2 = r21
            r3 = r19
            r4 = r17
            r5 = r26
            r6 = r27
            r7 = r28
            r8 = r29
            r0.setCalendar(r1, r2, r3, r4, r5, r6, r7, r8)
            r0 = 8
            if (r11 == r0) goto L_0x0668
            int r0 = r9.bp
            int r0 = r0 + 9
            char r0 = r9.charAt(r0)
            int r1 = r9.bp
            int r1 = r1 + 10
            char r1 = r9.charAt(r1)
            int r2 = r9.bp
            int r2 = r2 + 11
            char r2 = r9.charAt(r2)
            int r3 = r9.bp
            int r3 = r3 + 12
            char r7 = r9.charAt(r3)
            int r3 = r9.bp
            int r3 = r3 + 13
            char r3 = r9.charAt(r3)
            if (r23 == 0) goto L_0x05da
            r4 = 84
            if (r1 != r4) goto L_0x05da
            r4 = 58
            if (r3 != r4) goto L_0x05da
            int r4 = r9.bp
            int r4 = r4 + 16
            char r4 = r9.charAt(r4)
            r5 = 90
            if (r4 == r5) goto L_0x05e8
        L_0x05da:
            if (r12 == 0) goto L_0x0600
            r4 = 32
            if (r1 == r4) goto L_0x05e4
            r4 = 84
            if (r1 != r4) goto L_0x0600
        L_0x05e4:
            r4 = 58
            if (r3 != r4) goto L_0x0600
        L_0x05e8:
            int r0 = r9.bp
            r1 = 14
            int r0 = r0 + r1
            char r1 = r9.charAt(r0)
            int r0 = r9.bp
            int r0 = r0 + 15
            char r0 = r9.charAt(r0)
            r3 = r0
            r0 = r7
            r4 = 48
            r7 = 48
            goto L_0x0604
        L_0x0600:
            r4 = r3
            r3 = r2
            r2 = r22
        L_0x0604:
            r24 = r2
            r25 = r0
            r26 = r1
            r27 = r3
            r28 = r7
            r29 = r4
            boolean r5 = checkTime(r24, r25, r26, r27, r28, r29)
            if (r5 != 0) goto L_0x0617
            return r14
        L_0x0617:
            r5 = 17
            if (r11 != r5) goto L_0x0655
            if (r23 != 0) goto L_0x0655
            int r5 = r9.bp
            r6 = 14
            int r5 = r5 + r6
            char r5 = r9.charAt(r5)
            int r6 = r9.bp
            int r6 = r6 + 15
            char r6 = r9.charAt(r6)
            int r8 = r9.bp
            int r8 = r8 + 16
            char r8 = r9.charAt(r8)
            if (r5 < r15) goto L_0x0654
            r10 = 57
            if (r5 <= r10) goto L_0x063d
            goto L_0x0654
        L_0x063d:
            if (r6 < r15) goto L_0x0653
            if (r6 <= r10) goto L_0x0642
            goto L_0x0653
        L_0x0642:
            if (r8 < r15) goto L_0x0652
            if (r8 <= r10) goto L_0x0647
            goto L_0x0652
        L_0x0647:
            int r5 = r5 - r15
            int r5 = r5 * 100
            int r6 = r6 - r15
            int r6 = r6 * 10
            int r5 = r5 + r6
            int r8 = r8 - r15
            int r5 = r5 + r8
            r6 = r5
            goto L_0x0656
        L_0x0652:
            return r14
        L_0x0653:
            return r14
        L_0x0654:
            return r14
        L_0x0655:
            r6 = 0
        L_0x0656:
            int r2 = r2 - r15
            int r2 = r2 * 10
            int r0 = r0 - r15
            int r0 = r0 + r2
            int r1 = r1 - r15
            int r1 = r1 * 10
            int r3 = r3 - r15
            int r1 = r1 + r3
            int r7 = r7 - r15
            int r7 = r7 * 10
            int r4 = r4 - r15
            int r2 = r7 + r4
            r14 = r0
            goto L_0x066b
        L_0x0668:
            r1 = 0
            r2 = 0
            r6 = 0
        L_0x066b:
            java.util.Calendar r0 = r9.calendar
            r3 = 11
            r0.set(r3, r14)
            java.util.Calendar r0 = r9.calendar
            r3 = 12
            r0.set(r3, r1)
            java.util.Calendar r0 = r9.calendar
            r1 = 13
            r0.set(r1, r2)
            java.util.Calendar r0 = r9.calendar
            r1 = 14
            r0.set(r1, r6)
            r9.token = r13
            return r16
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexer.scanISO8601DateIfMatch(boolean, int):boolean");
    }

    /* access modifiers changed from: protected */
    public void setTime(char c, char c2, char c3, char c4, char c5, char c6) {
        this.calendar.set(11, ((c - '0') * 10) + (c2 - '0'));
        this.calendar.set(12, ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(13, ((c5 - '0') * 10) + (c6 - '0'));
    }

    /* access modifiers changed from: protected */
    public void setTimeZone(char c, char c2, char c3) {
        int i = (((c2 - '0') * 10) + (c3 - '0')) * 3600 * 1000;
        if (c == '-') {
            i = -i;
        }
        if (this.calendar.getTimeZone().getRawOffset() != i) {
            String[] availableIDs = TimeZone.getAvailableIDs(i);
            if (availableIDs.length > 0) {
                this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
            }
        }
    }

    private void setCalendar(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        this.calendar = Calendar.getInstance(this.timeZone, this.locale);
        this.calendar.set(1, ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(2, (((c5 - '0') * 10) + (c6 - '0')) - 1);
        this.calendar.set(5, ((c7 - '0') * 10) + (c8 - '0'));
    }

    public static final byte[] decodeFast(String str, int i, int i2) {
        int i3;
        int i4 = 0;
        if (i2 == 0) {
            return new byte[0];
        }
        int i5 = (i + i2) - 1;
        while (i < i5 && IA[str.charAt(i)] < 0) {
            i++;
        }
        while (i5 > 0 && IA[str.charAt(i5)] < 0) {
            i5--;
        }
        int i6 = str.charAt(i5) == '=' ? str.charAt(i5 + -1) == '=' ? 2 : 1 : 0;
        int i7 = (i5 - i) + 1;
        if (i2 > 76) {
            i3 = (str.charAt(76) == 13 ? i7 / 78 : 0) << 1;
        } else {
            i3 = 0;
        }
        int i8 = (((i7 - i3) * 6) >> 3) - i6;
        byte[] bArr = new byte[i8];
        int i9 = (i8 / 3) * 3;
        int i10 = i;
        int i11 = 0;
        int i12 = 0;
        while (i11 < i9) {
            int i13 = i10 + 1;
            int i14 = i13 + 1;
            int i15 = i14 + 1;
            int i16 = i15 + 1;
            int i17 = (IA[str.charAt(i10)] << 18) | (IA[str.charAt(i13)] << 12) | (IA[str.charAt(i14)] << 6) | IA[str.charAt(i15)];
            int i18 = i11 + 1;
            bArr[i11] = (byte) (i17 >> 16);
            int i19 = i18 + 1;
            bArr[i18] = (byte) (i17 >> 8);
            int i20 = i19 + 1;
            bArr[i19] = (byte) i17;
            if (i3 <= 0 || (i12 = i12 + 1) != 19) {
                i10 = i16;
            } else {
                i10 = i16 + 2;
                i12 = 0;
            }
            i11 = i20;
        }
        if (i11 < i8) {
            int i21 = 0;
            while (i10 <= i5 - i6) {
                i4 |= IA[str.charAt(i10)] << (18 - (i21 * 6));
                i21++;
                i10++;
            }
            int i22 = 16;
            while (i11 < i8) {
                bArr[i11] = (byte) (i4 >> i22);
                i22 -= 8;
                i11++;
            }
        }
        return bArr;
    }
}
