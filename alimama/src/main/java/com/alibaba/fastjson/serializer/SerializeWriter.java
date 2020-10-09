package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.tao.image.Logger;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.extension.UCCore;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import kotlin.UByte;

public final class SerializeWriter extends Writer {
    public static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', Logger.LEVEL_D, Logger.LEVEL_E, 'F'};
    static final char[] DigitOnes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static final char[] DigitTens = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
    static final char[] ascii_chars = {'0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', Logger.LEVEL_D, '0', Logger.LEVEL_E, '0', 'F', '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1', 'A', '1', 'B', '1', 'C', '1', Logger.LEVEL_D, '1', Logger.LEVEL_E, '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4', '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', Logger.LEVEL_D, '2', Logger.LEVEL_E, '2', 'F'};
    private static final ThreadLocal<char[]> bufLocal = new ThreadLocal<>();
    static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    static final char[] replaceChars = new char[93];
    static final int[] sizeTable = {9, 99, 999, CommonItemInfo.FOOT_TYPE, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};
    static final byte[] specicalFlags_doubleQuotes = new byte[161];
    static final byte[] specicalFlags_singleQuotes = new byte[161];
    protected char[] buf;
    protected int count;
    protected int features;
    protected final Writer writer;

    static {
        specicalFlags_doubleQuotes[0] = 4;
        specicalFlags_doubleQuotes[1] = 4;
        specicalFlags_doubleQuotes[2] = 4;
        specicalFlags_doubleQuotes[3] = 4;
        specicalFlags_doubleQuotes[4] = 4;
        specicalFlags_doubleQuotes[5] = 4;
        specicalFlags_doubleQuotes[6] = 4;
        specicalFlags_doubleQuotes[7] = 4;
        specicalFlags_doubleQuotes[8] = 1;
        specicalFlags_doubleQuotes[9] = 1;
        specicalFlags_doubleQuotes[10] = 1;
        specicalFlags_doubleQuotes[11] = 4;
        specicalFlags_doubleQuotes[12] = 1;
        specicalFlags_doubleQuotes[13] = 1;
        specicalFlags_doubleQuotes[34] = 1;
        specicalFlags_doubleQuotes[92] = 1;
        specicalFlags_singleQuotes[0] = 4;
        specicalFlags_singleQuotes[1] = 4;
        specicalFlags_singleQuotes[2] = 4;
        specicalFlags_singleQuotes[3] = 4;
        specicalFlags_singleQuotes[4] = 4;
        specicalFlags_singleQuotes[5] = 4;
        specicalFlags_singleQuotes[6] = 4;
        specicalFlags_singleQuotes[7] = 4;
        specicalFlags_singleQuotes[8] = 1;
        specicalFlags_singleQuotes[9] = 1;
        specicalFlags_singleQuotes[10] = 1;
        specicalFlags_singleQuotes[11] = 4;
        specicalFlags_singleQuotes[12] = 1;
        specicalFlags_singleQuotes[13] = 1;
        specicalFlags_singleQuotes[92] = 1;
        specicalFlags_singleQuotes[39] = 1;
        for (int i = 14; i <= 31; i++) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }
        for (int i2 = UCCore.SPEEDUP_DEXOPT_POLICY_DAVIK; i2 < 160; i2++) {
            specicalFlags_doubleQuotes[i2] = 4;
            specicalFlags_singleQuotes[i2] = 4;
        }
        replaceChars[0] = '0';
        replaceChars[1] = '1';
        replaceChars[2] = '2';
        replaceChars[3] = '3';
        replaceChars[4] = '4';
        replaceChars[5] = '5';
        replaceChars[6] = '6';
        replaceChars[7] = '7';
        replaceChars[8] = 'b';
        replaceChars[9] = 't';
        replaceChars[10] = 'n';
        replaceChars[11] = 'v';
        replaceChars[12] = 'f';
        replaceChars[13] = 'r';
        replaceChars[34] = '\"';
        replaceChars[39] = '\'';
        replaceChars[47] = DXTemplateNamePathUtil.DIR;
        replaceChars[92] = '\\';
    }

    public SerializeWriter() {
        this((Writer) null);
    }

    public SerializeWriter(Writer writer2) {
        this.writer = writer2;
        this.features = JSON.DEFAULT_GENERATE_FEATURE;
        this.buf = bufLocal.get();
        if (bufLocal != null) {
            bufLocal.set((Object) null);
        }
        if (this.buf == null) {
            this.buf = new char[1024];
        }
    }

    public SerializeWriter(SerializerFeature... serializerFeatureArr) {
        this((Writer) null, 0, serializerFeatureArr);
    }

    public SerializeWriter(Writer writer2, int i, SerializerFeature[] serializerFeatureArr) {
        this.writer = writer2;
        this.buf = bufLocal.get();
        if (this.buf != null) {
            bufLocal.set((Object) null);
        }
        if (this.buf == null) {
            this.buf = new char[1024];
        }
        for (SerializerFeature serializerFeature : serializerFeatureArr) {
            i |= serializerFeature.mask;
        }
        this.features = i;
    }

    public SerializeWriter(int i) {
        this((Writer) null, i);
    }

    public SerializeWriter(Writer writer2, int i) {
        this.writer = writer2;
        if (i > 0) {
            this.buf = new char[i];
            return;
        }
        throw new IllegalArgumentException("Negative initial size: " + i);
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        if (z) {
            this.features = serializerFeature.mask | this.features;
            return;
        }
        this.features = (serializerFeature.mask ^ -1) & this.features;
    }

    public boolean isEnabled(SerializerFeature serializerFeature) {
        return (serializerFeature.mask & this.features) != 0;
    }

    public void write(int i) {
        int i2 = this.count + 1;
        if (i2 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i2);
            } else {
                flush();
                i2 = 1;
            }
        }
        this.buf[this.count] = (char) i;
        this.count = i2;
    }

    public void write(char[] cArr, int i, int i2) {
        int i3;
        if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 != 0) {
            int i4 = this.count + i2;
            if (i4 > this.buf.length) {
                if (this.writer == null) {
                    expandCapacity(i4);
                } else {
                    do {
                        int length = this.buf.length - this.count;
                        System.arraycopy(cArr, i, this.buf, this.count, length);
                        this.count = this.buf.length;
                        flush();
                        i2 -= length;
                        i += length;
                    } while (i2 > this.buf.length);
                    i4 = i2;
                }
            }
            System.arraycopy(cArr, i, this.buf, this.count, i2);
            this.count = i4;
        }
    }

    /* access modifiers changed from: protected */
    public void expandCapacity(int i) {
        int length = ((this.buf.length * 3) / 2) + 1;
        if (length >= i) {
            i = length;
        }
        char[] cArr = new char[i];
        System.arraycopy(this.buf, 0, cArr, 0, this.count);
        this.buf = cArr;
    }

    public void write(String str, int i, int i2) {
        int i3;
        int i4 = this.count + i2;
        if (i4 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i4);
            } else {
                while (true) {
                    int length = this.buf.length - this.count;
                    i3 = i + length;
                    str.getChars(i, i3, this.buf, this.count);
                    this.count = this.buf.length;
                    flush();
                    i2 -= length;
                    if (i2 <= this.buf.length) {
                        break;
                    }
                    i = i3;
                }
                i4 = i2;
                i = i3;
            }
        }
        str.getChars(i, i2 + i, this.buf, this.count);
        this.count = i4;
    }

    public void writeTo(Writer writer2) throws IOException {
        if (this.writer == null) {
            writer2.write(this.buf, 0, this.count);
            return;
        }
        throw new UnsupportedOperationException("writer not null");
    }

    public void writeTo(OutputStream outputStream, String str) throws IOException {
        writeTo(outputStream, Charset.forName(str));
    }

    public void writeTo(OutputStream outputStream, Charset charset) throws IOException {
        if (this.writer == null) {
            outputStream.write(new String(this.buf, 0, this.count).getBytes(charset.name()));
            return;
        }
        throw new UnsupportedOperationException("writer not null");
    }

    public SerializeWriter append(CharSequence charSequence) {
        String charSequence2 = charSequence == null ? BuildConfig.buildJavascriptFrameworkVersion : charSequence.toString();
        write(charSequence2, 0, charSequence2.length());
        return this;
    }

    public SerializeWriter append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = BuildConfig.buildJavascriptFrameworkVersion;
        }
        String charSequence2 = charSequence.subSequence(i, i2).toString();
        write(charSequence2, 0, charSequence2.length());
        return this;
    }

    public SerializeWriter append(char c) {
        write((int) c);
        return this;
    }

    public byte[] toBytes(String str) {
        if (this.writer == null) {
            if (str == null) {
                str = "UTF-8";
            }
            try {
                return new String(this.buf, 0, this.count).getBytes(str);
            } catch (UnsupportedEncodingException e) {
                throw new JSONException("toBytes error", e);
            }
        } else {
            throw new UnsupportedOperationException("writer not null");
        }
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    public void close() {
        if (this.writer != null && this.count > 0) {
            flush();
        }
        if (this.buf.length <= 8192) {
            bufLocal.set(this.buf);
        }
        this.buf = null;
    }

    public void write(String str) {
        if (str == null) {
            writeNull();
        } else {
            write(str, 0, str.length());
        }
    }

    public void writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }
        int i2 = 0;
        while ((i < 0 ? -i : i) > sizeTable[i2]) {
            i2++;
        }
        int i3 = i2 + 1;
        if (i < 0) {
            i3++;
        }
        int i4 = this.count + i3;
        if (i4 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i4);
            } else {
                char[] cArr = new char[i3];
                getChars((long) i, i3, cArr);
                write(cArr, 0, cArr.length);
                return;
            }
        }
        getChars((long) i, i4, this.buf);
        this.count = i4;
    }

    public void writeByteArray(byte[] bArr) {
        byte[] bArr2 = bArr;
        int length = bArr2.length;
        int i = 0;
        boolean z = (this.features & SerializerFeature.UseSingleQuotes.mask) != 0;
        char c = z ? '\'' : '\"';
        if (length == 0) {
            write(z ? "''" : "\"\"");
            return;
        }
        char[] cArr = JSONLexer.CA;
        int i2 = (length / 3) * 3;
        int i3 = length - 1;
        int i4 = this.count;
        int i5 = this.count + (((i3 / 3) + 1) << 2) + 2;
        if (i5 > this.buf.length) {
            if (this.writer != null) {
                write((int) c);
                int i6 = 0;
                while (i6 < i2) {
                    int i7 = i6 + 1;
                    int i8 = i7 + 1;
                    byte b = ((bArr2[i6] & UByte.MAX_VALUE) << 16) | ((bArr2[i7] & UByte.MAX_VALUE) << 8) | (bArr2[i8] & UByte.MAX_VALUE);
                    write((int) cArr[(b >>> 18) & 63]);
                    write((int) cArr[(b >>> 12) & 63]);
                    write((int) cArr[(b >>> 6) & 63]);
                    write((int) cArr[b & 63]);
                    i6 = i8 + 1;
                }
                int i9 = length - i2;
                if (i9 > 0) {
                    int i10 = (bArr2[i2] & UByte.MAX_VALUE) << 10;
                    if (i9 == 2) {
                        i = (bArr2[i3] & UByte.MAX_VALUE) << 2;
                    }
                    int i11 = i10 | i;
                    write((int) cArr[i11 >> 12]);
                    write((int) cArr[(i11 >>> 6) & 63]);
                    write((int) i9 == 2 ? cArr[i11 & 63] : '=');
                    write(61);
                }
                write((int) c);
                return;
            }
            expandCapacity(i5);
        }
        this.count = i5;
        int i12 = i4 + 1;
        this.buf[i4] = c;
        int i13 = 0;
        while (i13 < i2) {
            int i14 = i13 + 1;
            int i15 = i14 + 1;
            byte b2 = ((bArr2[i13] & UByte.MAX_VALUE) << 16) | ((bArr2[i14] & UByte.MAX_VALUE) << 8);
            int i16 = i15 + 1;
            byte b3 = b2 | (bArr2[i15] & UByte.MAX_VALUE);
            int i17 = i12 + 1;
            this.buf[i12] = cArr[(b3 >>> 18) & 63];
            int i18 = i17 + 1;
            this.buf[i17] = cArr[(b3 >>> 12) & 63];
            int i19 = i18 + 1;
            this.buf[i18] = cArr[(b3 >>> 6) & 63];
            this.buf[i19] = cArr[b3 & 63];
            i13 = i16;
            i12 = i19 + 1;
        }
        int i20 = length - i2;
        if (i20 > 0) {
            int i21 = (bArr2[i2] & UByte.MAX_VALUE) << 10;
            if (i20 == 2) {
                i = (bArr2[i3] & UByte.MAX_VALUE) << 2;
            }
            int i22 = i21 | i;
            this.buf[i5 - 5] = cArr[i22 >> 12];
            this.buf[i5 - 4] = cArr[(i22 >>> 6) & 63];
            this.buf[i5 - 3] = i20 == 2 ? cArr[i22 & 63] : '=';
            this.buf[i5 - 2] = '=';
        }
        this.buf[i5 - 1] = c;
    }

    public void writeLong(long j) {
        if (j == Long.MIN_VALUE) {
            write("-9223372036854775808");
            return;
        }
        long j2 = j < 0 ? -j : j;
        int i = 1;
        long j3 = 10;
        while (true) {
            if (i >= 19) {
                i = 0;
                break;
            } else if (j2 < j3) {
                break;
            } else {
                j3 *= 10;
                i++;
            }
        }
        if (i == 0) {
            i = 19;
        }
        if (j < 0) {
            i++;
        }
        int i2 = this.count + i;
        if (i2 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i2);
            } else {
                char[] cArr = new char[i];
                getChars(j, i, cArr);
                write(cArr, 0, cArr.length);
                return;
            }
        }
        getChars(j, i2, this.buf);
        this.count = i2;
    }

    public void writeNull() {
        write(BuildConfig.buildJavascriptFrameworkVersion);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0099, code lost:
        if (r14 == -1) goto L_0x009b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00c6, code lost:
        if ((com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial.mask & r0.features) != 0) goto L_0x00c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00ce, code lost:
        if (r10 != '\\') goto L_0x00bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00d7, code lost:
        if (r10 != '\"') goto L_0x00bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00ec, code lost:
        if (r14 == -1) goto L_0x009b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x00ef A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00dc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeStringWithDoubleQuote(java.lang.String r18, char r19, boolean r20) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = r19
            if (r1 != 0) goto L_0x0011
            r17.writeNull()
            if (r2 == 0) goto L_0x0010
            r0.write((int) r2)
        L_0x0010:
            return
        L_0x0011:
            int r3 = r18.length()
            int r4 = r0.count
            int r4 = r4 + r3
            int r4 = r4 + 2
            if (r2 == 0) goto L_0x001e
            int r4 = r4 + 1
        L_0x001e:
            char[] r5 = r0.buf
            int r5 = r5.length
            r6 = 47
            r7 = 34
            r8 = 0
            r9 = 92
            if (r4 <= r5) goto L_0x006e
            java.io.Writer r5 = r0.writer
            if (r5 == 0) goto L_0x006b
            r0.write((int) r7)
        L_0x0031:
            int r3 = r18.length()
            if (r8 >= r3) goto L_0x0062
            char r3 = r1.charAt(r8)
            byte[] r4 = specicalFlags_doubleQuotes
            int r4 = r4.length
            if (r3 >= r4) goto L_0x0046
            byte[] r4 = specicalFlags_doubleQuotes
            byte r4 = r4[r3]
            if (r4 != 0) goto L_0x0051
        L_0x0046:
            if (r3 != r6) goto L_0x005c
            int r4 = r0.features
            com.alibaba.fastjson.serializer.SerializerFeature r5 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            int r5 = r5.mask
            r4 = r4 & r5
            if (r4 == 0) goto L_0x005c
        L_0x0051:
            r0.write((int) r9)
            char[] r4 = replaceChars
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x005f
        L_0x005c:
            r0.write((int) r3)
        L_0x005f:
            int r8 = r8 + 1
            goto L_0x0031
        L_0x0062:
            r0.write((int) r7)
            if (r2 == 0) goto L_0x006a
            r0.write((int) r2)
        L_0x006a:
            return
        L_0x006b:
            r0.expandCapacity(r4)
        L_0x006e:
            int r5 = r0.count
            r10 = 1
            int r5 = r5 + r10
            int r11 = r5 + r3
            char[] r12 = r0.buf
            int r13 = r0.count
            r12[r13] = r7
            char[] r12 = r0.buf
            r1.getChars(r8, r3, r12, r5)
            r0.count = r4
            if (r20 == 0) goto L_0x0284
            r3 = -1
            r13 = r4
            r4 = r5
            r12 = 0
            r14 = -1
            r15 = 0
            r16 = -1
        L_0x008b:
            r8 = 8232(0x2028, float:1.1535E-41)
            if (r4 >= r11) goto L_0x00f5
            char[] r10 = r0.buf
            char r10 = r10[r4]
            if (r10 != r8) goto L_0x00a3
            int r12 = r12 + 1
            int r13 = r13 + 4
            if (r14 != r3) goto L_0x009f
        L_0x009b:
            r14 = r4
            r16 = r14
            goto L_0x00a1
        L_0x009f:
            r16 = r4
        L_0x00a1:
            r15 = r10
            goto L_0x00ef
        L_0x00a3:
            r8 = 93
            if (r10 < r8) goto L_0x00b7
            r8 = 127(0x7f, float:1.78E-43)
            if (r10 < r8) goto L_0x00ef
            r8 = 160(0xa0, float:2.24E-43)
            if (r10 >= r8) goto L_0x00ef
            if (r14 != r3) goto L_0x00b2
            r14 = r4
        L_0x00b2:
            int r12 = r12 + 1
            int r13 = r13 + 4
            goto L_0x009f
        L_0x00b7:
            r8 = 32
            if (r10 != r8) goto L_0x00bd
        L_0x00bb:
            r6 = 0
            goto L_0x00da
        L_0x00bd:
            if (r10 != r6) goto L_0x00ca
            int r8 = r0.features
            com.alibaba.fastjson.serializer.SerializerFeature r6 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            int r6 = r6.mask
            r6 = r6 & r8
            if (r6 == 0) goto L_0x00ca
        L_0x00c8:
            r6 = 1
            goto L_0x00da
        L_0x00ca:
            r6 = 35
            if (r10 <= r6) goto L_0x00d1
            if (r10 == r9) goto L_0x00d1
            goto L_0x00bb
        L_0x00d1:
            r6 = 31
            if (r10 <= r6) goto L_0x00c8
            if (r10 == r9) goto L_0x00c8
            if (r10 != r7) goto L_0x00bb
            goto L_0x00c8
        L_0x00da:
            if (r6 == 0) goto L_0x00ef
            int r12 = r12 + 1
            byte[] r6 = specicalFlags_doubleQuotes
            int r6 = r6.length
            if (r10 >= r6) goto L_0x00ec
            byte[] r6 = specicalFlags_doubleQuotes
            byte r6 = r6[r10]
            r8 = 4
            if (r6 != r8) goto L_0x00ec
            int r13 = r13 + 4
        L_0x00ec:
            if (r14 != r3) goto L_0x009f
            goto L_0x009b
        L_0x00ef:
            int r4 = r4 + 1
            r6 = 47
            r10 = 1
            goto L_0x008b
        L_0x00f5:
            if (r12 <= 0) goto L_0x0284
            int r13 = r13 + r12
            char[] r3 = r0.buf
            int r3 = r3.length
            if (r13 <= r3) goto L_0x0100
            r0.expandCapacity(r13)
        L_0x0100:
            r0.count = r13
            r3 = 117(0x75, float:1.64E-43)
            r4 = 1
            if (r12 != r4) goto L_0x01b3
            if (r15 != r8) goto L_0x013b
            int r1 = r16 + 1
            int r5 = r16 + 6
            int r11 = r11 - r16
            int r11 = r11 - r4
            char[] r6 = r0.buf
            char[] r8 = r0.buf
            java.lang.System.arraycopy(r6, r1, r8, r5, r11)
            char[] r5 = r0.buf
            r5[r16] = r9
            char[] r5 = r0.buf
            r5[r1] = r3
            char[] r3 = r0.buf
            int r1 = r1 + r4
            r5 = 50
            r3[r1] = r5
            char[] r3 = r0.buf
            int r1 = r1 + r4
            r6 = 48
            r3[r1] = r6
            char[] r3 = r0.buf
            int r1 = r1 + r4
            r3[r1] = r5
            char[] r3 = r0.buf
            int r1 = r1 + r4
            r4 = 56
            r3[r1] = r4
            goto L_0x0284
        L_0x013b:
            byte[] r1 = specicalFlags_doubleQuotes
            int r1 = r1.length
            if (r15 >= r1) goto L_0x0196
            byte[] r1 = specicalFlags_doubleQuotes
            byte r1 = r1[r15]
            r4 = 4
            if (r1 != r4) goto L_0x0196
            int r1 = r16 + 1
            int r4 = r16 + 6
            int r11 = r11 - r16
            r5 = 1
            int r11 = r11 - r5
            char[] r5 = r0.buf
            char[] r6 = r0.buf
            java.lang.System.arraycopy(r5, r1, r6, r4, r11)
            char[] r4 = r0.buf
            r4[r16] = r9
            char[] r4 = r0.buf
            int r5 = r1 + 1
            r4[r1] = r3
            char[] r1 = r0.buf
            int r3 = r5 + 1
            char[] r4 = DIGITS
            int r6 = r15 >>> 12
            r6 = r6 & 15
            char r4 = r4[r6]
            r1[r5] = r4
            char[] r1 = r0.buf
            int r4 = r3 + 1
            char[] r5 = DIGITS
            int r6 = r15 >>> 8
            r6 = r6 & 15
            char r5 = r5[r6]
            r1[r3] = r5
            char[] r1 = r0.buf
            int r3 = r4 + 1
            char[] r5 = DIGITS
            int r6 = r15 >>> 4
            r6 = r6 & 15
            char r5 = r5[r6]
            r1[r4] = r5
            char[] r1 = r0.buf
            char[] r4 = DIGITS
            r5 = r15 & 15
            char r4 = r4[r5]
            r1[r3] = r4
            goto L_0x0284
        L_0x0196:
            int r1 = r16 + 1
            int r3 = r16 + 2
            int r11 = r11 - r16
            r4 = 1
            int r11 = r11 - r4
            char[] r4 = r0.buf
            char[] r5 = r0.buf
            java.lang.System.arraycopy(r4, r1, r5, r3, r11)
            char[] r3 = r0.buf
            r3[r16] = r9
            char[] r3 = r0.buf
            char[] r4 = replaceChars
            char r4 = r4[r15]
            r3[r1] = r4
            goto L_0x0284
        L_0x01b3:
            if (r12 <= r4) goto L_0x0284
            int r4 = r14 - r5
        L_0x01b7:
            int r5 = r18.length()
            if (r4 >= r5) goto L_0x0284
            char r5 = r1.charAt(r4)
            byte[] r6 = specicalFlags_doubleQuotes
            int r6 = r6.length
            if (r5 >= r6) goto L_0x01d0
            byte[] r6 = specicalFlags_doubleQuotes
            byte r6 = r6[r5]
            if (r6 != 0) goto L_0x01cd
            goto L_0x01d0
        L_0x01cd:
            r6 = 47
            goto L_0x01dd
        L_0x01d0:
            r6 = 47
            if (r5 != r6) goto L_0x0233
            int r10 = r0.features
            com.alibaba.fastjson.serializer.SerializerFeature r11 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            int r11 = r11.mask
            r10 = r10 & r11
            if (r10 == 0) goto L_0x0233
        L_0x01dd:
            char[] r10 = r0.buf
            int r11 = r14 + 1
            r10[r14] = r9
            byte[] r10 = specicalFlags_doubleQuotes
            byte r10 = r10[r5]
            r12 = 4
            if (r10 != r12) goto L_0x0228
            char[] r10 = r0.buf
            int r13 = r11 + 1
            r10[r11] = r3
            char[] r10 = r0.buf
            int r11 = r13 + 1
            char[] r14 = DIGITS
            int r15 = r5 >>> 12
            r15 = r15 & 15
            char r14 = r14[r15]
            r10[r13] = r14
            char[] r10 = r0.buf
            int r13 = r11 + 1
            char[] r14 = DIGITS
            int r15 = r5 >>> 8
            r15 = r15 & 15
            char r14 = r14[r15]
            r10[r11] = r14
            char[] r10 = r0.buf
            int r11 = r13 + 1
            char[] r14 = DIGITS
            int r15 = r5 >>> 4
            r15 = r15 & 15
            char r14 = r14[r15]
            r10[r13] = r14
            char[] r10 = r0.buf
            int r13 = r11 + 1
            char[] r14 = DIGITS
            r5 = r5 & 15
            char r5 = r14[r5]
            r10[r11] = r5
        L_0x0226:
            r14 = r13
            goto L_0x0280
        L_0x0228:
            char[] r10 = r0.buf
            int r13 = r11 + 1
            char[] r14 = replaceChars
            char r5 = r14[r5]
            r10[r11] = r5
            goto L_0x0226
        L_0x0233:
            r12 = 4
            if (r5 != r8) goto L_0x0279
            char[] r10 = r0.buf
            int r11 = r14 + 1
            r10[r14] = r9
            char[] r10 = r0.buf
            int r13 = r11 + 1
            r10[r11] = r3
            char[] r10 = r0.buf
            int r11 = r13 + 1
            char[] r14 = DIGITS
            int r15 = r5 >>> 12
            r15 = r15 & 15
            char r14 = r14[r15]
            r10[r13] = r14
            char[] r10 = r0.buf
            int r13 = r11 + 1
            char[] r14 = DIGITS
            int r15 = r5 >>> 8
            r15 = r15 & 15
            char r14 = r14[r15]
            r10[r11] = r14
            char[] r10 = r0.buf
            int r11 = r13 + 1
            char[] r14 = DIGITS
            int r15 = r5 >>> 4
            r15 = r15 & 15
            char r14 = r14[r15]
            r10[r13] = r14
            char[] r10 = r0.buf
            int r13 = r11 + 1
            char[] r14 = DIGITS
            r5 = r5 & 15
            char r5 = r14[r5]
            r10[r11] = r5
            goto L_0x0226
        L_0x0279:
            char[] r10 = r0.buf
            int r11 = r14 + 1
            r10[r14] = r5
            r14 = r11
        L_0x0280:
            int r4 = r4 + 1
            goto L_0x01b7
        L_0x0284:
            if (r2 == 0) goto L_0x0297
            char[] r1 = r0.buf
            int r3 = r0.count
            int r3 = r3 + -2
            r1[r3] = r7
            char[] r1 = r0.buf
            int r3 = r0.count
            r4 = 1
            int r3 = r3 - r4
            r1[r3] = r2
            goto L_0x029f
        L_0x0297:
            r4 = 1
            char[] r1 = r0.buf
            int r2 = r0.count
            int r2 = r2 - r4
            r1[r2] = r7
        L_0x029f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeStringWithDoubleQuote(java.lang.String, char, boolean):void");
    }

    public void write(boolean z) {
        write(z ? "true" : "false");
    }

    public void writeString(String str) {
        if ((this.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
            writeStringWithSingleQuote(str);
        } else {
            writeStringWithDoubleQuote(str, 0, true);
        }
    }

    /* access modifiers changed from: protected */
    public void writeStringWithSingleQuote(String str) {
        int i = 0;
        if (str == null) {
            int i2 = this.count + 4;
            if (i2 > this.buf.length) {
                expandCapacity(i2);
            }
            BuildConfig.buildJavascriptFrameworkVersion.getChars(0, 4, this.buf, this.count);
            this.count = i2;
            return;
        }
        int length = str.length();
        int i3 = this.count + length + 2;
        if (i3 > this.buf.length) {
            if (this.writer != null) {
                write(39);
                while (i < str.length()) {
                    char charAt = str.charAt(i);
                    if (charAt <= 13 || charAt == '\\' || charAt == '\'' || (charAt == '/' && (this.features & SerializerFeature.WriteSlashAsSpecial.mask) != 0)) {
                        write(92);
                        write((int) replaceChars[charAt]);
                    } else {
                        write((int) charAt);
                    }
                    i++;
                }
                write(39);
                return;
            }
            expandCapacity(i3);
        }
        int i4 = this.count + 1;
        int i5 = i4 + length;
        this.buf[this.count] = '\'';
        str.getChars(0, length, this.buf, i4);
        this.count = i3;
        int i6 = -1;
        char c = 0;
        for (int i7 = i4; i7 < i5; i7++) {
            char c2 = this.buf[i7];
            if (c2 <= 13 || c2 == '\\' || c2 == '\'' || (c2 == '/' && (this.features & SerializerFeature.WriteSlashAsSpecial.mask) != 0)) {
                i++;
                i6 = i7;
                c = c2;
            }
        }
        int i8 = i3 + i;
        if (i8 > this.buf.length) {
            expandCapacity(i8);
        }
        this.count = i8;
        if (i == 1) {
            int i9 = i6 + 1;
            System.arraycopy(this.buf, i9, this.buf, i6 + 2, (i5 - i6) - 1);
            this.buf[i6] = '\\';
            this.buf[i9] = replaceChars[c];
        } else if (i > 1) {
            int i10 = i6 + 1;
            System.arraycopy(this.buf, i10, this.buf, i6 + 2, (i5 - i6) - 1);
            this.buf[i6] = '\\';
            this.buf[i10] = replaceChars[c];
            int i11 = i5 + 1;
            for (int i12 = i10 - 2; i12 >= i4; i12--) {
                char c3 = this.buf[i12];
                if (c3 <= 13 || c3 == '\\' || c3 == '\'' || (c3 == '/' && (this.features & SerializerFeature.WriteSlashAsSpecial.mask) != 0)) {
                    int i13 = i12 + 1;
                    System.arraycopy(this.buf, i13, this.buf, i12 + 2, (i11 - i12) - 1);
                    this.buf[i12] = '\\';
                    this.buf[i13] = replaceChars[c3];
                    i11++;
                }
            }
        }
        this.buf[this.count - 1] = '\'';
    }

    public void writeFieldName(String str, boolean z) {
        if ((this.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
            if ((this.features & SerializerFeature.QuoteFieldNames.mask) != 0) {
                writeStringWithSingleQuote(str);
                write(58);
                return;
            }
            writeKeyWithSingleQuoteIfHasSpecial(str);
        } else if ((this.features & SerializerFeature.QuoteFieldNames.mask) != 0) {
            writeStringWithDoubleQuote(str, Operators.CONDITION_IF_MIDDLE, z);
        } else {
            writeKeyWithDoubleQuoteIfHasSpecial(str);
        }
    }

    private void writeKeyWithDoubleQuoteIfHasSpecial(String str) {
        String str2 = str;
        int length = str.length();
        boolean z = true;
        int i = this.count + length + 1;
        int i2 = 0;
        if (i > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i);
            } else if (length == 0) {
                write(34);
                write(34);
                write(58);
                return;
            } else {
                int i3 = 0;
                while (true) {
                    if (i3 < length) {
                        char charAt = str2.charAt(i3);
                        if (charAt < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[charAt] != 0) {
                            break;
                        }
                        i3++;
                    } else {
                        z = false;
                        break;
                    }
                }
                if (z) {
                    write(34);
                }
                while (i2 < length) {
                    char charAt2 = str2.charAt(i2);
                    if (charAt2 >= specicalFlags_doubleQuotes.length || specicalFlags_doubleQuotes[charAt2] == 0) {
                        write((int) charAt2);
                    } else {
                        write(92);
                        write((int) replaceChars[charAt2]);
                    }
                    i2++;
                }
                if (z) {
                    write(34);
                }
                write(58);
                return;
            }
        }
        if (length == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            int i4 = this.count;
            this.count = i4 + 1;
            cArr[i4] = '\"';
            char[] cArr2 = this.buf;
            int i5 = this.count;
            this.count = i5 + 1;
            cArr2[i5] = '\"';
            char[] cArr3 = this.buf;
            int i6 = this.count;
            this.count = i6 + 1;
            cArr3[i6] = Operators.CONDITION_IF_MIDDLE;
            return;
        }
        int i7 = this.count;
        int i8 = i7 + length;
        str2.getChars(0, length, this.buf, i7);
        this.count = i;
        int i9 = i7;
        boolean z2 = false;
        while (i9 < i8) {
            char c = this.buf[i9];
            if (c < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[c] != 0) {
                if (!z2) {
                    i += 3;
                    if (i > this.buf.length) {
                        expandCapacity(i);
                    }
                    this.count = i;
                    int i10 = i9 + 1;
                    System.arraycopy(this.buf, i10, this.buf, i9 + 3, (i8 - i9) - 1);
                    System.arraycopy(this.buf, i2, this.buf, 1, i9);
                    this.buf[i7] = '\"';
                    this.buf[i10] = '\\';
                    int i11 = i10 + 1;
                    this.buf[i11] = replaceChars[c];
                    i8 += 2;
                    this.buf[this.count - 2] = '\"';
                    i9 = i11;
                    z2 = true;
                } else {
                    i++;
                    if (i > this.buf.length) {
                        expandCapacity(i);
                    }
                    this.count = i;
                    int i12 = i9 + 1;
                    System.arraycopy(this.buf, i12, this.buf, i9 + 2, i8 - i9);
                    this.buf[i9] = '\\';
                    this.buf[i12] = replaceChars[c];
                    i8++;
                    i9 = i12;
                }
            }
            i9++;
            i2 = 0;
        }
        this.buf[this.count - 1] = Operators.CONDITION_IF_MIDDLE;
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String str) {
        String str2 = str;
        int length = str.length();
        boolean z = true;
        int i = this.count + length + 1;
        int i2 = 0;
        if (i > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i);
            } else if (length == 0) {
                write(39);
                write(39);
                write(58);
                return;
            } else {
                int i3 = 0;
                while (true) {
                    if (i3 < length) {
                        char charAt = str2.charAt(i3);
                        if (charAt < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[charAt] != 0) {
                            break;
                        }
                        i3++;
                    } else {
                        z = false;
                        break;
                    }
                }
                if (z) {
                    write(39);
                }
                while (i2 < length) {
                    char charAt2 = str2.charAt(i2);
                    if (charAt2 >= specicalFlags_singleQuotes.length || specicalFlags_singleQuotes[charAt2] == 0) {
                        write((int) charAt2);
                    } else {
                        write(92);
                        write((int) replaceChars[charAt2]);
                    }
                    i2++;
                }
                if (z) {
                    write(39);
                }
                write(58);
                return;
            }
        }
        if (length == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            int i4 = this.count;
            this.count = i4 + 1;
            cArr[i4] = '\'';
            char[] cArr2 = this.buf;
            int i5 = this.count;
            this.count = i5 + 1;
            cArr2[i5] = '\'';
            char[] cArr3 = this.buf;
            int i6 = this.count;
            this.count = i6 + 1;
            cArr3[i6] = Operators.CONDITION_IF_MIDDLE;
            return;
        }
        int i7 = this.count;
        int i8 = i7 + length;
        str2.getChars(0, length, this.buf, i7);
        this.count = i;
        int i9 = i7;
        boolean z2 = false;
        while (i9 < i8) {
            char c = this.buf[i9];
            if (c < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[c] != 0) {
                if (!z2) {
                    i += 3;
                    if (i > this.buf.length) {
                        expandCapacity(i);
                    }
                    this.count = i;
                    int i10 = i9 + 1;
                    System.arraycopy(this.buf, i10, this.buf, i9 + 3, (i8 - i9) - 1);
                    System.arraycopy(this.buf, i2, this.buf, 1, i9);
                    this.buf[i7] = '\'';
                    this.buf[i10] = '\\';
                    int i11 = i10 + 1;
                    this.buf[i11] = replaceChars[c];
                    i8 += 2;
                    this.buf[this.count - 2] = '\'';
                    i9 = i11;
                    z2 = true;
                } else {
                    i++;
                    if (i > this.buf.length) {
                        expandCapacity(i);
                    }
                    this.count = i;
                    int i12 = i9 + 1;
                    System.arraycopy(this.buf, i12, this.buf, i9 + 2, i8 - i9);
                    this.buf[i9] = '\\';
                    this.buf[i12] = replaceChars[c];
                    i8++;
                    i9 = i12;
                }
            }
            i9++;
            i2 = 0;
        }
        this.buf[i - 1] = Operators.CONDITION_IF_MIDDLE;
    }

    public void flush() {
        if (this.writer != null) {
            try {
                this.writer.write(this.buf, 0, this.count);
                this.writer.flush();
                this.count = 0;
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
    }

    protected static void getChars(long j, int i, char[] cArr) {
        char c;
        if (j < 0) {
            c = '-';
            j = -j;
        } else {
            c = 0;
        }
        while (j > 2147483647L) {
            long j2 = j / 100;
            int i2 = (int) (j - (((j2 << 6) + (j2 << 5)) + (j2 << 2)));
            int i3 = i - 1;
            cArr[i3] = DigitOnes[i2];
            i = i3 - 1;
            cArr[i] = DigitTens[i2];
            j = j2;
        }
        int i4 = (int) j;
        while (i4 >= 65536) {
            int i5 = i4 / 100;
            int i6 = i4 - (((i5 << 6) + (i5 << 5)) + (i5 << 2));
            int i7 = i - 1;
            cArr[i7] = DigitOnes[i6];
            i = i7 - 1;
            cArr[i] = DigitTens[i6];
            i4 = i5;
        }
        while (true) {
            int i8 = (52429 * i4) >>> 19;
            i--;
            cArr[i] = digits[i4 - ((i8 << 3) + (i8 << 1))];
            if (i8 == 0) {
                break;
            }
            i4 = i8;
        }
        if (c != 0) {
            cArr[i - 1] = c;
        }
    }
}
