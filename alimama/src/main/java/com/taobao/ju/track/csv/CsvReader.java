package com.taobao.ju.track.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashMap;

public class CsvReader {
    public static final int ESCAPE_MODE_BACKSLASH = 2;
    public static final int ESCAPE_MODE_DOUBLED = 1;
    private Charset charset;
    private boolean closed;
    private ColumnBuffer columnBuffer;
    private int columnsCount;
    private long currentRecord;
    private DataBuffer dataBuffer;
    private String fileName;
    private boolean hasMoreData;
    private boolean hasReadNextLine;
    private HeadersHolder headersHolder;
    private boolean initialized;
    private Reader inputStream;
    private boolean[] isQualified;
    private char lastLetter;
    private RawRecordBuffer rawBuffer;
    private String rawRecord;
    private boolean startedColumn;
    private boolean startedWithQualifier;
    private boolean useCustomRecordDelimiter;
    private UserSettings userSettings;
    private String[] values;

    private static char hexToDec(char c) {
        return c >= 'a' ? (char) ((c - 'a') + 10) : c >= 'A' ? (char) ((c - 'A') + 10) : (char) (c - '0');
    }

    public CsvReader(String str, char c, Charset charset2) throws FileNotFoundException {
        this.inputStream = null;
        this.fileName = null;
        this.userSettings = new UserSettings();
        this.charset = null;
        this.useCustomRecordDelimiter = false;
        this.dataBuffer = new DataBuffer();
        this.columnBuffer = new ColumnBuffer();
        this.rawBuffer = new RawRecordBuffer();
        this.isQualified = null;
        this.rawRecord = "";
        this.headersHolder = new HeadersHolder();
        this.startedColumn = false;
        this.startedWithQualifier = false;
        this.hasMoreData = true;
        this.lastLetter = 0;
        this.hasReadNextLine = false;
        this.columnsCount = 0;
        this.currentRecord = 0;
        this.values = new String[10];
        this.initialized = false;
        this.closed = false;
        if (str == null) {
            throw new IllegalArgumentException("Parameter fileName can not be null.");
        } else if (charset2 == null) {
            throw new IllegalArgumentException("Parameter charset can not be null.");
        } else if (new File(str).exists()) {
            this.fileName = str;
            this.userSettings.Delimiter = c;
            this.charset = charset2;
            this.isQualified = new boolean[this.values.length];
        } else {
            throw new FileNotFoundException("File " + str + " does not exist.");
        }
    }

    public CsvReader(String str, char c) throws FileNotFoundException {
        this(str, c, Charset.forName("ISO-8859-1"));
    }

    public CsvReader(String str) throws FileNotFoundException {
        this(str, ',');
    }

    public CsvReader(Reader reader, char c) {
        this.inputStream = null;
        this.fileName = null;
        this.userSettings = new UserSettings();
        this.charset = null;
        this.useCustomRecordDelimiter = false;
        this.dataBuffer = new DataBuffer();
        this.columnBuffer = new ColumnBuffer();
        this.rawBuffer = new RawRecordBuffer();
        this.isQualified = null;
        this.rawRecord = "";
        this.headersHolder = new HeadersHolder();
        this.startedColumn = false;
        this.startedWithQualifier = false;
        this.hasMoreData = true;
        this.lastLetter = 0;
        this.hasReadNextLine = false;
        this.columnsCount = 0;
        this.currentRecord = 0;
        this.values = new String[10];
        this.initialized = false;
        this.closed = false;
        if (reader != null) {
            this.inputStream = reader;
            this.userSettings.Delimiter = c;
            this.initialized = true;
            this.isQualified = new boolean[this.values.length];
            return;
        }
        throw new IllegalArgumentException("Parameter inputStream can not be null.");
    }

    public CsvReader(Reader reader) {
        this(reader, ',');
    }

    public CsvReader(InputStream inputStream2, char c, Charset charset2) {
        this((Reader) new InputStreamReader(inputStream2, charset2), c);
    }

    public CsvReader(InputStream inputStream2, Charset charset2) {
        this((Reader) new InputStreamReader(inputStream2, charset2));
    }

    public boolean getCaptureRawRecord() {
        return this.userSettings.CaptureRawRecord;
    }

    public void setCaptureRawRecord(boolean z) {
        this.userSettings.CaptureRawRecord = z;
    }

    public String getRawRecord() {
        return this.rawRecord;
    }

    public boolean getTrimWhitespace() {
        return this.userSettings.TrimWhitespace;
    }

    public void setTrimWhitespace(boolean z) {
        this.userSettings.TrimWhitespace = z;
    }

    public char getDelimiter() {
        return this.userSettings.Delimiter;
    }

    public void setDelimiter(char c) {
        this.userSettings.Delimiter = c;
    }

    public char getRecordDelimiter() {
        return this.userSettings.RecordDelimiter;
    }

    public void setRecordDelimiter(char c) {
        this.useCustomRecordDelimiter = true;
        this.userSettings.RecordDelimiter = c;
    }

    public char getTextQualifier() {
        return this.userSettings.TextQualifier;
    }

    public void setTextQualifier(char c) {
        this.userSettings.TextQualifier = c;
    }

    public boolean getUseTextQualifier() {
        return this.userSettings.UseTextQualifier;
    }

    public void setUseTextQualifier(boolean z) {
        this.userSettings.UseTextQualifier = z;
    }

    public char getComment() {
        return this.userSettings.Comment;
    }

    public void setComment(char c) {
        this.userSettings.Comment = c;
    }

    public boolean getUseComments() {
        return this.userSettings.UseComments;
    }

    public void setUseComments(boolean z) {
        this.userSettings.UseComments = z;
    }

    public int getEscapeMode() {
        return this.userSettings.EscapeMode;
    }

    public void setEscapeMode(int i) throws IllegalArgumentException {
        if (i == 1 || i == 2) {
            this.userSettings.EscapeMode = i;
            return;
        }
        throw new IllegalArgumentException("Parameter escapeMode must be a valid value.");
    }

    public boolean getSkipEmptyRecords() {
        return this.userSettings.SkipEmptyRecords;
    }

    public void setSkipEmptyRecords(boolean z) {
        this.userSettings.SkipEmptyRecords = z;
    }

    public boolean getSafetySwitch() {
        return this.userSettings.SafetySwitch;
    }

    public void setSafetySwitch(boolean z) {
        this.userSettings.SafetySwitch = z;
    }

    public int getColumnCount() {
        return this.columnsCount;
    }

    public long getCurrentRecord() {
        return this.currentRecord - 1;
    }

    public int getHeaderCount() {
        return this.headersHolder.Length;
    }

    public String[] getHeaders() throws IOException {
        checkClosed();
        if (this.headersHolder.Headers == null) {
            return null;
        }
        String[] strArr = new String[this.headersHolder.Length];
        System.arraycopy(this.headersHolder.Headers, 0, strArr, 0, this.headersHolder.Length);
        return strArr;
    }

    public void setHeaders(String[] strArr) {
        this.headersHolder.Headers = strArr;
        this.headersHolder.IndexByName.clear();
        if (strArr != null) {
            this.headersHolder.Length = strArr.length;
        } else {
            this.headersHolder.Length = 0;
        }
        for (int i = 0; i < this.headersHolder.Length; i++) {
            this.headersHolder.IndexByName.put(strArr[i], Integer.valueOf(i));
        }
    }

    public String[] getValues() throws IOException {
        checkClosed();
        String[] strArr = new String[this.columnsCount];
        System.arraycopy(this.values, 0, strArr, 0, this.columnsCount);
        return strArr;
    }

    public String get(int i) throws IOException {
        checkClosed();
        return (i <= -1 || i >= this.columnsCount) ? "" : this.values[i];
    }

    public String get(String str) throws IOException {
        checkClosed();
        return get(getIndex(str));
    }

    public static CsvReader parse(String str) {
        if (str != null) {
            return new CsvReader((Reader) new StringReader(str));
        }
        throw new IllegalArgumentException("Parameter data can not be null.");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x037b, code lost:
        if (r5 == 2) goto L_0x0393;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:194:0x0386, code lost:
        if (r5 == 3) goto L_0x0393;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:196:0x0391, code lost:
        if (r5 == 3) goto L_0x0393;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x039f, code lost:
        if (r5 == 4) goto L_0x03a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x03a1, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:201:0x03a2, code lost:
        if (r4 != false) goto L_0x03a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x03a4, code lost:
        appendLetter(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:203:0x03a8, code lost:
        r0.dataBuffer.ColumnStart = r0.dataBuffer.Position + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:222:0x03f9, code lost:
        if (r2 == 'D') goto L_0x041e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:224:0x03fd, code lost:
        if (r2 == 'O') goto L_0x041c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:226:0x0401, code lost:
        if (r2 == 'U') goto L_0x041a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:228:0x0405, code lost:
        if (r2 == 'X') goto L_0x0418;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:230:0x0409, code lost:
        if (r2 == 'd') goto L_0x0420;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x040d, code lost:
        if (r2 == 'o') goto L_0x041c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:234:0x0411, code lost:
        if (r2 == 'u') goto L_0x041a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:236:0x0415, code lost:
        if (r2 == 'x') goto L_0x0418;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:237:0x0418, code lost:
        r7 = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:238:0x041a, code lost:
        r7 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:239:0x041c, code lost:
        r7 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:241:0x0420, code lost:
        r7 = 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:242:0x0421, code lost:
        r0.dataBuffer.ColumnStart = r0.dataBuffer.Position + 1;
        r4 = true;
        r5 = 0;
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00e8, code lost:
        if (r7 == 3) goto L_0x0101;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00f3, code lost:
        if (r7 == 3) goto L_0x0101;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ff, code lost:
        if (r7 == r11) goto L_0x0101;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0101, code lost:
        r8 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x018e, code lost:
        if (r1 != 'x') goto L_0x019c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:262:0x048b  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0174  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean readRecord() throws java.io.IOException {
        /*
            r21 = this;
            r0 = r21
            r21.checkClosed()
            r1 = 0
            r0.columnsCount = r1
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r2 = r0.rawBuffer
            r2.Position = r1
            com.taobao.ju.track.csv.CsvReader$DataBuffer r2 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r3 = r0.dataBuffer
            int r3 = r3.Position
            r2.LineStart = r3
            r0.hasReadNextLine = r1
            boolean r2 = r0.hasMoreData
            r3 = 1
            if (r2 == 0) goto L_0x052e
        L_0x001b:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r2 = r0.dataBuffer
            int r2 = r2.Position
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            int r4 = r4.Count
            if (r2 != r4) goto L_0x002a
            r21.checkDataLength()
            goto L_0x0510
        L_0x002a:
            r0.startedWithQualifier = r1
            com.taobao.ju.track.csv.CsvReader$DataBuffer r2 = r0.dataBuffer
            char[] r2 = r2.Buffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            int r4 = r4.Position
            char r2 = r2[r4]
            com.taobao.ju.track.csv.CsvReader$UserSettings r4 = r0.userSettings
            boolean r4 = r4.UseTextQualifier
            r6 = 68
            r8 = 92
            r9 = 9
            r11 = 4
            r12 = 3
            r13 = 10
            r14 = 13
            r15 = 2
            if (r4 == 0) goto L_0x0281
            com.taobao.ju.track.csv.CsvReader$UserSettings r4 = r0.userSettings
            char r4 = r4.TextQualifier
            if (r2 != r4) goto L_0x0281
            r0.lastLetter = r2
            r0.startedColumn = r3
            com.taobao.ju.track.csv.CsvReader$DataBuffer r2 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            int r4 = r4.Position
            int r4 = r4 + r3
            r2.ColumnStart = r4
            r0.startedWithQualifier = r3
            com.taobao.ju.track.csv.CsvReader$UserSettings r2 = r0.userSettings
            char r2 = r2.TextQualifier
            com.taobao.ju.track.csv.CsvReader$UserSettings r4 = r0.userSettings
            int r4 = r4.EscapeMode
            if (r4 != r15) goto L_0x006a
            r2 = 92
        L_0x006a:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            int r8 = r4.Position
            int r8 = r8 + r3
            r4.Position = r8
            r4 = 0
            r8 = 0
            r16 = 0
            r17 = 1
            r18 = 0
            r19 = 0
            r20 = 0
        L_0x007d:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            int r1 = r1.Position
            com.taobao.ju.track.csv.CsvReader$DataBuffer r7 = r0.dataBuffer
            int r7 = r7.Count
            if (r1 != r7) goto L_0x008c
            r21.checkDataLength()
            goto L_0x0274
        L_0x008c:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            char[] r1 = r1.Buffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r7 = r0.dataBuffer
            int r7 = r7.Position
            char r1 = r1[r7]
            if (r4 == 0) goto L_0x00c6
            com.taobao.ju.track.csv.CsvReader$DataBuffer r7 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r10 = r0.dataBuffer
            int r10 = r10.Position
            int r10 = r10 + r3
            r7.ColumnStart = r10
            com.taobao.ju.track.csv.CsvReader$UserSettings r7 = r0.userSettings
            char r7 = r7.Delimiter
            if (r1 != r7) goto L_0x00ac
            r21.endColumn()
            goto L_0x0207
        L_0x00ac:
            boolean r7 = r0.useCustomRecordDelimiter
            if (r7 != 0) goto L_0x00b4
            if (r1 == r14) goto L_0x00be
            if (r1 == r13) goto L_0x00be
        L_0x00b4:
            boolean r7 = r0.useCustomRecordDelimiter
            if (r7 == 0) goto L_0x0207
            com.taobao.ju.track.csv.CsvReader$UserSettings r7 = r0.userSettings
            char r7 = r7.RecordDelimiter
            if (r1 != r7) goto L_0x0207
        L_0x00be:
            r21.endColumn()
            r21.endRecord()
            goto L_0x0207
        L_0x00c6:
            if (r8 == 0) goto L_0x0117
            int r7 = r16 + 1
            switch(r17) {
                case 1: goto L_0x00f6;
                case 2: goto L_0x00eb;
                case 3: goto L_0x00e0;
                case 4: goto L_0x00d0;
                default: goto L_0x00cd;
            }
        L_0x00cd:
            r5 = r18
            goto L_0x0102
        L_0x00d0:
            int r10 = r18 * 16
            char r10 = (char) r10
            char r16 = hexToDec(r1)
            int r10 = r10 + r16
            char r10 = (char) r10
            if (r7 != r15) goto L_0x00de
            r5 = r10
            goto L_0x0101
        L_0x00de:
            r5 = r10
            goto L_0x0102
        L_0x00e0:
            int r10 = r18 * 10
            char r10 = (char) r10
            int r5 = r1 + -48
            char r5 = (char) r5
            int r10 = r10 + r5
            char r5 = (char) r10
            if (r7 != r12) goto L_0x0102
            goto L_0x0101
        L_0x00eb:
            int r5 = r18 * 8
            char r5 = (char) r5
            int r10 = r1 + -48
            char r10 = (char) r10
            int r5 = r5 + r10
            char r5 = (char) r5
            if (r7 != r12) goto L_0x0102
            goto L_0x0101
        L_0x00f6:
            int r5 = r18 * 16
            char r5 = (char) r5
            char r10 = hexToDec(r1)
            int r5 = r5 + r10
            char r5 = (char) r5
            if (r7 != r11) goto L_0x0102
        L_0x0101:
            r8 = 0
        L_0x0102:
            if (r8 != 0) goto L_0x0108
            r0.appendLetter(r5)
            goto L_0x0111
        L_0x0108:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r10 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r11 = r0.dataBuffer
            int r11 = r11.Position
            int r11 = r11 + r3
            r10.ColumnStart = r11
        L_0x0111:
            r18 = r5
            r16 = r7
            goto L_0x0207
        L_0x0117:
            com.taobao.ju.track.csv.CsvReader$UserSettings r5 = r0.userSettings
            char r5 = r5.TextQualifier
            if (r1 != r5) goto L_0x0134
            if (r19 == 0) goto L_0x0125
            r19 = 0
        L_0x0121:
            r20 = 0
            goto L_0x0207
        L_0x0125:
            r21.updateCurrentValue()
            com.taobao.ju.track.csv.CsvReader$UserSettings r5 = r0.userSettings
            int r5 = r5.EscapeMode
            if (r5 != r3) goto L_0x0130
            r19 = 1
        L_0x0130:
            r20 = 1
            goto L_0x0207
        L_0x0134:
            com.taobao.ju.track.csv.CsvReader$UserSettings r5 = r0.userSettings
            int r5 = r5.EscapeMode
            if (r5 != r15) goto L_0x01cc
            if (r19 == 0) goto L_0x01cc
            switch(r1) {
                case 48: goto L_0x01b6;
                case 49: goto L_0x01b6;
                case 50: goto L_0x01b6;
                case 51: goto L_0x01b6;
                case 52: goto L_0x01b6;
                case 53: goto L_0x01b6;
                case 54: goto L_0x01b6;
                case 55: goto L_0x01b6;
                default: goto L_0x013f;
            }
        L_0x013f:
            switch(r1) {
                case 97: goto L_0x01b1;
                case 98: goto L_0x01ab;
                default: goto L_0x0142;
            }
        L_0x0142:
            switch(r1) {
                case 100: goto L_0x0172;
                case 101: goto L_0x016c;
                case 102: goto L_0x0166;
                default: goto L_0x0145;
            }
        L_0x0145:
            switch(r1) {
                case 110: goto L_0x0161;
                case 111: goto L_0x0172;
                default: goto L_0x0148;
            }
        L_0x0148:
            switch(r1) {
                case 116: goto L_0x015c;
                case 117: goto L_0x0172;
                case 118: goto L_0x0155;
                default: goto L_0x014b;
            }
        L_0x014b:
            switch(r1) {
                case 68: goto L_0x0172;
                case 79: goto L_0x0172;
                case 85: goto L_0x0172;
                case 88: goto L_0x0172;
                case 114: goto L_0x0150;
                case 120: goto L_0x0172;
                default: goto L_0x014e;
            }
        L_0x014e:
            goto L_0x01c9
        L_0x0150:
            r0.appendLetter(r14)
            goto L_0x01c9
        L_0x0155:
            r5 = 11
            r0.appendLetter(r5)
            goto L_0x01c9
        L_0x015c:
            r0.appendLetter(r9)
            goto L_0x01c9
        L_0x0161:
            r0.appendLetter(r13)
            goto L_0x01c9
        L_0x0166:
            r5 = 12
            r0.appendLetter(r5)
            goto L_0x01c9
        L_0x016c:
            r5 = 27
            r0.appendLetter(r5)
            goto L_0x01c9
        L_0x0172:
            if (r1 == r6) goto L_0x019a
            r5 = 79
            if (r1 == r5) goto L_0x0197
            r5 = 85
            if (r1 == r5) goto L_0x0194
            r5 = 88
            if (r1 == r5) goto L_0x0191
            r5 = 100
            if (r1 == r5) goto L_0x019a
            r5 = 111(0x6f, float:1.56E-43)
            if (r1 == r5) goto L_0x0197
            r5 = 117(0x75, float:1.64E-43)
            if (r1 == r5) goto L_0x0194
            r5 = 120(0x78, float:1.68E-43)
            if (r1 == r5) goto L_0x0191
            goto L_0x019c
        L_0x0191:
            r17 = 4
            goto L_0x019c
        L_0x0194:
            r17 = 1
            goto L_0x019c
        L_0x0197:
            r17 = 2
            goto L_0x019c
        L_0x019a:
            r17 = 3
        L_0x019c:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r5 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r7 = r0.dataBuffer
            int r7 = r7.Position
            int r7 = r7 + r3
            r5.ColumnStart = r7
            r8 = 1
            r16 = 0
            r18 = 0
            goto L_0x01c9
        L_0x01ab:
            r5 = 8
            r0.appendLetter(r5)
            goto L_0x01c9
        L_0x01b1:
            r5 = 7
            r0.appendLetter(r5)
            goto L_0x01c9
        L_0x01b6:
            int r5 = r1 + -48
            char r5 = (char) r5
            com.taobao.ju.track.csv.CsvReader$DataBuffer r7 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r8 = r0.dataBuffer
            int r8 = r8.Position
            int r8 = r8 + r3
            r7.ColumnStart = r8
            r18 = r5
            r8 = 1
            r16 = 1
            r17 = 2
        L_0x01c9:
            r19 = 0
            goto L_0x0207
        L_0x01cc:
            if (r1 != r2) goto L_0x01d4
            r21.updateCurrentValue()
            r19 = 1
            goto L_0x0207
        L_0x01d4:
            if (r20 == 0) goto L_0x0207
            com.taobao.ju.track.csv.CsvReader$UserSettings r5 = r0.userSettings
            char r5 = r5.Delimiter
            if (r1 != r5) goto L_0x01e1
            r21.endColumn()
            goto L_0x0121
        L_0x01e1:
            boolean r5 = r0.useCustomRecordDelimiter
            if (r5 != 0) goto L_0x01e9
            if (r1 == r14) goto L_0x01f3
            if (r1 == r13) goto L_0x01f3
        L_0x01e9:
            boolean r5 = r0.useCustomRecordDelimiter
            if (r5 == 0) goto L_0x01fb
            com.taobao.ju.track.csv.CsvReader$UserSettings r5 = r0.userSettings
            char r5 = r5.RecordDelimiter
            if (r1 != r5) goto L_0x01fb
        L_0x01f3:
            r21.endColumn()
            r21.endRecord()
            goto L_0x0121
        L_0x01fb:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r5 = r0.dataBuffer
            int r5 = r5.Position
            int r5 = r5 + r3
            r4.ColumnStart = r5
            r4 = 1
            goto L_0x0121
        L_0x0207:
            r0.lastLetter = r1
            boolean r1 = r0.startedColumn
            if (r1 == 0) goto L_0x0274
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            int r5 = r1.Position
            int r5 = r5 + r3
            r1.Position = r5
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            boolean r1 = r1.SafetySwitch
            if (r1 == 0) goto L_0x0274
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            int r1 = r1.Position
            com.taobao.ju.track.csv.CsvReader$DataBuffer r5 = r0.dataBuffer
            int r5 = r5.ColumnStart
            int r1 = r1 - r5
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r5 = r0.columnBuffer
            int r5 = r5.Position
            int r1 = r1 + r5
            r5 = 100000(0x186a0, float:1.4013E-40)
            if (r1 > r5) goto L_0x022e
            goto L_0x0274
        L_0x022e:
            r21.close()
            java.io.IOException r1 = new java.io.IOException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Maximum column length of 100,000 exceeded in column "
            r2.append(r3)
            java.text.NumberFormat r3 = java.text.NumberFormat.getIntegerInstance()
            int r4 = r0.columnsCount
            long r4 = (long) r4
            java.lang.String r3 = r3.format(r4)
            r2.append(r3)
            java.lang.String r3 = " in record "
            r2.append(r3)
            java.text.NumberFormat r3 = java.text.NumberFormat.getIntegerInstance()
            long r4 = r0.currentRecord
            java.lang.String r3 = r3.format(r4)
            r2.append(r3)
            java.lang.String r3 = ". Set the SafetySwitch property to false"
            r2.append(r3)
            java.lang.String r3 = " if you're expecting column lengths greater than 100,000 characters to"
            r2.append(r3)
            java.lang.String r3 = " avoid this error."
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x0274:
            boolean r1 = r0.hasMoreData
            if (r1 == 0) goto L_0x0505
            boolean r1 = r0.startedColumn
            if (r1 != 0) goto L_0x027e
            goto L_0x0505
        L_0x027e:
            r11 = 4
            goto L_0x007d
        L_0x0281:
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            char r1 = r1.Delimiter
            if (r2 != r1) goto L_0x028e
            r0.lastLetter = r2
            r21.endColumn()
            goto L_0x0505
        L_0x028e:
            boolean r1 = r0.useCustomRecordDelimiter
            if (r1 == 0) goto L_0x02bb
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            char r1 = r1.RecordDelimiter
            if (r2 != r1) goto L_0x02bb
            boolean r1 = r0.startedColumn
            if (r1 != 0) goto L_0x02b1
            int r1 = r0.columnsCount
            if (r1 > 0) goto L_0x02b1
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            boolean r1 = r1.SkipEmptyRecords
            if (r1 != 0) goto L_0x02a7
            goto L_0x02b1
        L_0x02a7:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            int r4 = r4.Position
            int r4 = r4 + r3
            r1.LineStart = r4
            goto L_0x02b7
        L_0x02b1:
            r21.endColumn()
            r21.endRecord()
        L_0x02b7:
            r0.lastLetter = r2
            goto L_0x0505
        L_0x02bb:
            boolean r1 = r0.useCustomRecordDelimiter
            if (r1 != 0) goto L_0x02ec
            if (r2 == r14) goto L_0x02c3
            if (r2 != r13) goto L_0x02ec
        L_0x02c3:
            boolean r1 = r0.startedColumn
            if (r1 != 0) goto L_0x02e2
            int r1 = r0.columnsCount
            if (r1 > 0) goto L_0x02e2
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            boolean r1 = r1.SkipEmptyRecords
            if (r1 != 0) goto L_0x02d8
            if (r2 == r14) goto L_0x02e2
            char r1 = r0.lastLetter
            if (r1 == r14) goto L_0x02d8
            goto L_0x02e2
        L_0x02d8:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            int r4 = r4.Position
            int r4 = r4 + r3
            r1.LineStart = r4
            goto L_0x02e8
        L_0x02e2:
            r21.endColumn()
            r21.endRecord()
        L_0x02e8:
            r0.lastLetter = r2
            goto L_0x0505
        L_0x02ec:
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            boolean r1 = r1.UseComments
            if (r1 == 0) goto L_0x0303
            int r1 = r0.columnsCount
            if (r1 != 0) goto L_0x0303
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            char r1 = r1.Comment
            if (r2 != r1) goto L_0x0303
            r0.lastLetter = r2
            r21.skipLine()
            goto L_0x0505
        L_0x0303:
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            boolean r1 = r1.TrimWhitespace
            if (r1 == 0) goto L_0x031c
            r1 = 32
            if (r2 == r1) goto L_0x030f
            if (r2 != r9) goto L_0x031c
        L_0x030f:
            r0.startedColumn = r3
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r2 = r0.dataBuffer
            int r2 = r2.Position
            int r2 = r2 + r3
            r1.ColumnStart = r2
            goto L_0x0505
        L_0x031c:
            r0.startedColumn = r3
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            int r4 = r4.Position
            r1.ColumnStart = r4
            r1 = 1
            r4 = 0
            r5 = 0
            r7 = 1
            r10 = 0
            r11 = 0
        L_0x032c:
            if (r1 != 0) goto L_0x033f
            com.taobao.ju.track.csv.CsvReader$DataBuffer r6 = r0.dataBuffer
            int r6 = r6.Position
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r13 = r13.Count
            if (r6 != r13) goto L_0x033f
            r21.checkDataLength()
            r13 = 79
            goto L_0x04f6
        L_0x033f:
            if (r1 != 0) goto L_0x034b
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            char[] r1 = r1.Buffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r2 = r0.dataBuffer
            int r2 = r2.Position
            char r2 = r1[r2]
        L_0x034b:
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            boolean r1 = r1.UseTextQualifier
            if (r1 != 0) goto L_0x0369
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            int r1 = r1.EscapeMode
            if (r1 != r15) goto L_0x0369
            if (r2 != r8) goto L_0x0369
            if (r10 == 0) goto L_0x0362
            r6 = 68
            r10 = 0
        L_0x035e:
            r13 = 79
            goto L_0x0485
        L_0x0362:
            r21.updateCurrentValue()
            r6 = 68
            r10 = 1
            goto L_0x035e
        L_0x0369:
            if (r4 == 0) goto L_0x03b4
            int r5 = r5 + 1
            switch(r7) {
                case 1: goto L_0x0395;
                case 2: goto L_0x0389;
                case 3: goto L_0x037e;
                case 4: goto L_0x0372;
                default: goto L_0x0370;
            }
        L_0x0370:
            r1 = 4
            goto L_0x03a2
        L_0x0372:
            int r11 = r11 * 16
            char r1 = (char) r11
            char r6 = hexToDec(r2)
            int r1 = r1 + r6
            char r11 = (char) r1
            if (r5 != r15) goto L_0x0370
            goto L_0x0393
        L_0x037e:
            int r11 = r11 * 10
            char r1 = (char) r11
            int r6 = r2 + -48
            char r6 = (char) r6
            int r1 = r1 + r6
            char r11 = (char) r1
            if (r5 != r12) goto L_0x0370
            goto L_0x0393
        L_0x0389:
            int r11 = r11 * 8
            char r1 = (char) r11
            int r6 = r2 + -48
            char r6 = (char) r6
            int r1 = r1 + r6
            char r11 = (char) r1
            if (r5 != r12) goto L_0x0370
        L_0x0393:
            r1 = 4
            goto L_0x03a1
        L_0x0395:
            int r11 = r11 * 16
            char r1 = (char) r11
            char r6 = hexToDec(r2)
            int r1 = r1 + r6
            char r11 = (char) r1
            r1 = 4
            if (r5 != r1) goto L_0x03a2
        L_0x03a1:
            r4 = 0
        L_0x03a2:
            if (r4 != 0) goto L_0x03a8
            r0.appendLetter(r11)
            goto L_0x03b1
        L_0x03a8:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r6 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r13 = r13.Position
            int r13 = r13 + r3
            r6.ColumnStart = r13
        L_0x03b1:
            r6 = 68
            goto L_0x035e
        L_0x03b4:
            r1 = 4
            com.taobao.ju.track.csv.CsvReader$UserSettings r6 = r0.userSettings
            int r6 = r6.EscapeMode
            if (r6 != r15) goto L_0x045d
            if (r10 == 0) goto L_0x045d
            switch(r2) {
                case 48: goto L_0x0445;
                case 49: goto L_0x0445;
                case 50: goto L_0x0445;
                case 51: goto L_0x0445;
                case 52: goto L_0x0445;
                case 53: goto L_0x0445;
                case 54: goto L_0x0445;
                case 55: goto L_0x0445;
                default: goto L_0x03c0;
            }
        L_0x03c0:
            switch(r2) {
                case 97: goto L_0x043a;
                case 98: goto L_0x0430;
                default: goto L_0x03c3;
            }
        L_0x03c3:
            switch(r2) {
                case 100: goto L_0x03f7;
                case 101: goto L_0x03f1;
                case 102: goto L_0x03eb;
                default: goto L_0x03c6;
            }
        L_0x03c6:
            switch(r2) {
                case 110: goto L_0x03e5;
                case 111: goto L_0x03f7;
                default: goto L_0x03c9;
            }
        L_0x03c9:
            switch(r2) {
                case 116: goto L_0x03e1;
                case 117: goto L_0x03f7;
                case 118: goto L_0x03db;
                default: goto L_0x03cc;
            }
        L_0x03cc:
            switch(r2) {
                case 68: goto L_0x03f7;
                case 79: goto L_0x03f7;
                case 85: goto L_0x03f7;
                case 88: goto L_0x03f7;
                case 114: goto L_0x03d7;
                case 120: goto L_0x03f7;
                default: goto L_0x03cf;
            }
        L_0x03cf:
            r6 = 68
            r10 = 8
            r13 = 79
            goto L_0x045b
        L_0x03d7:
            r0.appendLetter(r14)
            goto L_0x03cf
        L_0x03db:
            r6 = 11
            r0.appendLetter(r6)
            goto L_0x03cf
        L_0x03e1:
            r0.appendLetter(r9)
            goto L_0x03cf
        L_0x03e5:
            r6 = 10
            r0.appendLetter(r6)
            goto L_0x03cf
        L_0x03eb:
            r6 = 12
            r0.appendLetter(r6)
            goto L_0x03cf
        L_0x03f1:
            r6 = 27
            r0.appendLetter(r6)
            goto L_0x03cf
        L_0x03f7:
            r6 = 68
            if (r2 == r6) goto L_0x041e
            r13 = 79
            if (r2 == r13) goto L_0x041c
            r4 = 85
            if (r2 == r4) goto L_0x041a
            r4 = 88
            if (r2 == r4) goto L_0x0418
            r4 = 100
            if (r2 == r4) goto L_0x0420
            r4 = 111(0x6f, float:1.56E-43)
            if (r2 == r4) goto L_0x041c
            r4 = 117(0x75, float:1.64E-43)
            if (r2 == r4) goto L_0x041a
            r4 = 120(0x78, float:1.68E-43)
            if (r2 == r4) goto L_0x0418
            goto L_0x0421
        L_0x0418:
            r7 = 4
            goto L_0x0421
        L_0x041a:
            r7 = 1
            goto L_0x0421
        L_0x041c:
            r7 = 2
            goto L_0x0421
        L_0x041e:
            r13 = 79
        L_0x0420:
            r7 = 3
        L_0x0421:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r5 = r0.dataBuffer
            int r5 = r5.Position
            int r5 = r5 + r3
            r4.ColumnStart = r5
            r4 = 1
            r5 = 0
            r10 = 8
            r11 = 0
            goto L_0x045b
        L_0x0430:
            r6 = 68
            r10 = 8
            r13 = 79
            r0.appendLetter(r10)
            goto L_0x045b
        L_0x043a:
            r6 = 68
            r10 = 8
            r13 = 79
            r1 = 7
            r0.appendLetter(r1)
            goto L_0x045b
        L_0x0445:
            r6 = 68
            r10 = 8
            r13 = 79
            int r1 = r2 + -48
            char r1 = (char) r1
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r5 = r0.dataBuffer
            int r5 = r5.Position
            int r5 = r5 + r3
            r4.ColumnStart = r5
            r11 = r1
            r4 = 1
            r5 = 1
            r7 = 2
        L_0x045b:
            r10 = 0
            goto L_0x0485
        L_0x045d:
            r6 = 68
            r13 = 79
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            char r1 = r1.Delimiter
            if (r2 != r1) goto L_0x046b
            r21.endColumn()
            goto L_0x0485
        L_0x046b:
            boolean r1 = r0.useCustomRecordDelimiter
            if (r1 != 0) goto L_0x0475
            if (r2 == r14) goto L_0x047f
            r1 = 10
            if (r2 == r1) goto L_0x047f
        L_0x0475:
            boolean r1 = r0.useCustomRecordDelimiter
            if (r1 == 0) goto L_0x0485
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            char r1 = r1.RecordDelimiter
            if (r2 != r1) goto L_0x0485
        L_0x047f:
            r21.endColumn()
            r21.endRecord()
        L_0x0485:
            r0.lastLetter = r2
            boolean r1 = r0.startedColumn
            if (r1 == 0) goto L_0x04f2
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            int r6 = r1.Position
            int r6 = r6 + r3
            r1.Position = r6
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            boolean r1 = r1.SafetySwitch
            if (r1 == 0) goto L_0x04f2
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            int r1 = r1.Position
            com.taobao.ju.track.csv.CsvReader$DataBuffer r6 = r0.dataBuffer
            int r6 = r6.ColumnStart
            int r1 = r1 - r6
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r6 = r0.columnBuffer
            int r6 = r6.Position
            int r1 = r1 + r6
            r6 = 100000(0x186a0, float:1.4013E-40)
            if (r1 > r6) goto L_0x04ac
            goto L_0x04f5
        L_0x04ac:
            r21.close()
            java.io.IOException r1 = new java.io.IOException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Maximum column length of 100,000 exceeded in column "
            r2.append(r3)
            java.text.NumberFormat r3 = java.text.NumberFormat.getIntegerInstance()
            int r4 = r0.columnsCount
            long r4 = (long) r4
            java.lang.String r3 = r3.format(r4)
            r2.append(r3)
            java.lang.String r3 = " in record "
            r2.append(r3)
            java.text.NumberFormat r3 = java.text.NumberFormat.getIntegerInstance()
            long r4 = r0.currentRecord
            java.lang.String r3 = r3.format(r4)
            r2.append(r3)
            java.lang.String r3 = ". Set the SafetySwitch property to false"
            r2.append(r3)
            java.lang.String r3 = " if you're expecting column lengths greater than 100,000 characters to"
            r2.append(r3)
            java.lang.String r3 = " avoid this error."
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x04f2:
            r6 = 100000(0x186a0, float:1.4013E-40)
        L_0x04f5:
            r1 = 0
        L_0x04f6:
            boolean r6 = r0.hasMoreData
            if (r6 == 0) goto L_0x0505
            boolean r6 = r0.startedColumn
            if (r6 != 0) goto L_0x04ff
            goto L_0x0505
        L_0x04ff:
            r6 = 68
            r13 = 10
            goto L_0x032c
        L_0x0505:
            boolean r1 = r0.hasMoreData
            if (r1 == 0) goto L_0x0510
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r0.dataBuffer
            int r2 = r1.Position
            int r2 = r2 + r3
            r1.Position = r2
        L_0x0510:
            boolean r1 = r0.hasMoreData
            if (r1 == 0) goto L_0x051c
            boolean r1 = r0.hasReadNextLine
            if (r1 == 0) goto L_0x0519
            goto L_0x051c
        L_0x0519:
            r1 = 0
            goto L_0x001b
        L_0x051c:
            boolean r1 = r0.startedColumn
            if (r1 != 0) goto L_0x0528
            char r1 = r0.lastLetter
            com.taobao.ju.track.csv.CsvReader$UserSettings r2 = r0.userSettings
            char r2 = r2.Delimiter
            if (r1 != r2) goto L_0x052e
        L_0x0528:
            r21.endColumn()
            r21.endRecord()
        L_0x052e:
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r0.userSettings
            boolean r1 = r1.CaptureRawRecord
            if (r1 == 0) goto L_0x05a0
            boolean r1 = r0.hasMoreData
            if (r1 == 0) goto L_0x058f
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r1 = r0.rawBuffer
            int r1 = r1.Position
            if (r1 != 0) goto L_0x0558
            java.lang.String r1 = new java.lang.String
            com.taobao.ju.track.csv.CsvReader$DataBuffer r2 = r0.dataBuffer
            char[] r2 = r2.Buffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            int r4 = r4.LineStart
            com.taobao.ju.track.csv.CsvReader$DataBuffer r5 = r0.dataBuffer
            int r5 = r5.Position
            com.taobao.ju.track.csv.CsvReader$DataBuffer r6 = r0.dataBuffer
            int r6 = r6.LineStart
            int r5 = r5 - r6
            int r5 = r5 - r3
            r1.<init>(r2, r4, r5)
            r0.rawRecord = r1
            goto L_0x05a4
        L_0x0558:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = new java.lang.String
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r4 = r0.rawBuffer
            char[] r4 = r4.Buffer
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r5 = r0.rawBuffer
            int r5 = r5.Position
            r6 = 0
            r2.<init>(r4, r6, r5)
            r1.append(r2)
            java.lang.String r2 = new java.lang.String
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r0.dataBuffer
            char[] r4 = r4.Buffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r5 = r0.dataBuffer
            int r5 = r5.LineStart
            com.taobao.ju.track.csv.CsvReader$DataBuffer r6 = r0.dataBuffer
            int r6 = r6.Position
            com.taobao.ju.track.csv.CsvReader$DataBuffer r7 = r0.dataBuffer
            int r7 = r7.LineStart
            int r6 = r6 - r7
            int r6 = r6 - r3
            r2.<init>(r4, r5, r6)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.rawRecord = r1
            goto L_0x05a4
        L_0x058f:
            java.lang.String r1 = new java.lang.String
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r2 = r0.rawBuffer
            char[] r2 = r2.Buffer
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r3 = r0.rawBuffer
            int r3 = r3.Position
            r4 = 0
            r1.<init>(r2, r4, r3)
            r0.rawRecord = r1
            goto L_0x05a4
        L_0x05a0:
            java.lang.String r1 = ""
            r0.rawRecord = r1
        L_0x05a4:
            boolean r1 = r0.hasReadNextLine
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.CsvReader.readRecord():boolean");
    }

    private void checkDataLength() throws IOException {
        if (!this.initialized) {
            if (this.fileName != null) {
                this.inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName), this.charset), 4096);
            }
            this.charset = null;
            this.initialized = true;
        }
        updateCurrentValue();
        if (this.userSettings.CaptureRawRecord && this.dataBuffer.Count > 0) {
            if (this.rawBuffer.Buffer.length - this.rawBuffer.Position < this.dataBuffer.Count - this.dataBuffer.LineStart) {
                char[] cArr = new char[(this.rawBuffer.Buffer.length + Math.max(this.dataBuffer.Count - this.dataBuffer.LineStart, this.rawBuffer.Buffer.length))];
                System.arraycopy(this.rawBuffer.Buffer, 0, cArr, 0, this.rawBuffer.Position);
                this.rawBuffer.Buffer = cArr;
            }
            System.arraycopy(this.dataBuffer.Buffer, this.dataBuffer.LineStart, this.rawBuffer.Buffer, this.rawBuffer.Position, this.dataBuffer.Count - this.dataBuffer.LineStart);
            this.rawBuffer.Position += this.dataBuffer.Count - this.dataBuffer.LineStart;
        }
        try {
            this.dataBuffer.Count = this.inputStream.read(this.dataBuffer.Buffer, 0, this.dataBuffer.Buffer.length);
            if (this.dataBuffer.Count == -1) {
                this.hasMoreData = false;
            }
            this.dataBuffer.Position = 0;
            this.dataBuffer.LineStart = 0;
            this.dataBuffer.ColumnStart = 0;
        } catch (IOException e) {
            close();
            throw e;
        }
    }

    public boolean readHeaders() throws IOException {
        boolean readRecord = readRecord();
        this.headersHolder.Length = this.columnsCount;
        this.headersHolder.Headers = new String[this.columnsCount];
        for (int i = 0; i < this.headersHolder.Length; i++) {
            String str = get(i);
            this.headersHolder.Headers[i] = str;
            this.headersHolder.IndexByName.put(str, Integer.valueOf(i));
        }
        if (readRecord) {
            this.currentRecord--;
        }
        this.columnsCount = 0;
        return readRecord;
    }

    public String getHeader(int i) throws IOException {
        checkClosed();
        return (i <= -1 || i >= this.headersHolder.Length) ? "" : this.headersHolder.Headers[i];
    }

    public boolean isQualified(int i) throws IOException {
        checkClosed();
        if (i >= this.columnsCount || i <= -1) {
            return false;
        }
        return this.isQualified[i];
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a2  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void endColumn() throws java.io.IOException {
        /*
            r6 = this;
            java.lang.String r0 = ""
            boolean r1 = r6.startedColumn
            r2 = 0
            if (r1 == 0) goto L_0x008d
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r1 = r6.columnBuffer
            int r1 = r1.Position
            r3 = 32
            if (r1 != 0) goto L_0x0059
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r6.dataBuffer
            int r1 = r1.ColumnStart
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r6.dataBuffer
            int r4 = r4.Position
            if (r1 >= r4) goto L_0x008d
            com.taobao.ju.track.csv.CsvReader$DataBuffer r0 = r6.dataBuffer
            int r0 = r0.Position
            int r0 = r0 + -1
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r6.userSettings
            boolean r1 = r1.TrimWhitespace
            if (r1 == 0) goto L_0x0044
            boolean r1 = r6.startedWithQualifier
            if (r1 != 0) goto L_0x0044
        L_0x0029:
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r6.dataBuffer
            int r1 = r1.ColumnStart
            if (r0 < r1) goto L_0x0044
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r6.dataBuffer
            char[] r1 = r1.Buffer
            char r1 = r1[r0]
            if (r1 == r3) goto L_0x0041
            com.taobao.ju.track.csv.CsvReader$DataBuffer r1 = r6.dataBuffer
            char[] r1 = r1.Buffer
            char r1 = r1[r0]
            r4 = 9
            if (r1 != r4) goto L_0x0044
        L_0x0041:
            int r0 = r0 + -1
            goto L_0x0029
        L_0x0044:
            java.lang.String r1 = new java.lang.String
            com.taobao.ju.track.csv.CsvReader$DataBuffer r3 = r6.dataBuffer
            char[] r3 = r3.Buffer
            com.taobao.ju.track.csv.CsvReader$DataBuffer r4 = r6.dataBuffer
            int r4 = r4.ColumnStart
            com.taobao.ju.track.csv.CsvReader$DataBuffer r5 = r6.dataBuffer
            int r5 = r5.ColumnStart
            int r0 = r0 - r5
            int r0 = r0 + 1
            r1.<init>(r3, r4, r0)
            goto L_0x008e
        L_0x0059:
            r6.updateCurrentValue()
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r0 = r6.columnBuffer
            int r0 = r0.Position
            int r0 = r0 + -1
            com.taobao.ju.track.csv.CsvReader$UserSettings r1 = r6.userSettings
            boolean r1 = r1.TrimWhitespace
            if (r1 == 0) goto L_0x0081
            boolean r1 = r6.startedWithQualifier
            if (r1 != 0) goto L_0x0081
        L_0x006c:
            if (r0 < 0) goto L_0x0081
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r1 = r6.columnBuffer
            char[] r1 = r1.Buffer
            char r1 = r1[r0]
            if (r1 == r3) goto L_0x007e
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r1 = r6.columnBuffer
            char[] r1 = r1.Buffer
            char r1 = r1[r0]
            if (r1 != r3) goto L_0x0081
        L_0x007e:
            int r0 = r0 + -1
            goto L_0x006c
        L_0x0081:
            java.lang.String r1 = new java.lang.String
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r3 = r6.columnBuffer
            char[] r3 = r3.Buffer
            int r0 = r0 + 1
            r1.<init>(r3, r2, r0)
            goto L_0x008e
        L_0x008d:
            r1 = r0
        L_0x008e:
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r0 = r6.columnBuffer
            r0.Position = r2
            r6.startedColumn = r2
            int r0 = r6.columnsCount
            r3 = 100000(0x186a0, float:1.4013E-40)
            if (r0 < r3) goto L_0x00d5
            com.taobao.ju.track.csv.CsvReader$UserSettings r0 = r6.userSettings
            boolean r0 = r0.SafetySwitch
            if (r0 != 0) goto L_0x00a2
            goto L_0x00d5
        L_0x00a2:
            r6.close()
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Maximum column count of 100,000 exceeded in record "
            r1.append(r2)
            java.text.NumberFormat r2 = java.text.NumberFormat.getIntegerInstance()
            long r3 = r6.currentRecord
            java.lang.String r2 = r2.format(r3)
            r1.append(r2)
            java.lang.String r2 = ". Set the SafetySwitch property to false"
            r1.append(r2)
            java.lang.String r2 = " if you're expecting more than 100,000 columns per record to"
            r1.append(r2)
            java.lang.String r2 = " avoid this error."
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x00d5:
            int r0 = r6.columnsCount
            java.lang.String[] r3 = r6.values
            int r3 = r3.length
            if (r0 != r3) goto L_0x00f9
            java.lang.String[] r0 = r6.values
            int r0 = r0.length
            int r0 = r0 * 2
            java.lang.String[] r3 = new java.lang.String[r0]
            java.lang.String[] r4 = r6.values
            java.lang.String[] r5 = r6.values
            int r5 = r5.length
            java.lang.System.arraycopy(r4, r2, r3, r2, r5)
            r6.values = r3
            boolean[] r0 = new boolean[r0]
            boolean[] r3 = r6.isQualified
            boolean[] r4 = r6.isQualified
            int r4 = r4.length
            java.lang.System.arraycopy(r3, r2, r0, r2, r4)
            r6.isQualified = r0
        L_0x00f9:
            java.lang.String[] r0 = r6.values
            int r2 = r6.columnsCount
            r0[r2] = r1
            boolean[] r0 = r6.isQualified
            int r1 = r6.columnsCount
            boolean r2 = r6.startedWithQualifier
            r0[r1] = r2
            int r0 = r6.columnsCount
            int r0 = r0 + 1
            r6.columnsCount = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.CsvReader.endColumn():void");
    }

    private void appendLetter(char c) {
        if (this.columnBuffer.Position == this.columnBuffer.Buffer.length) {
            char[] cArr = new char[(this.columnBuffer.Buffer.length * 2)];
            System.arraycopy(this.columnBuffer.Buffer, 0, cArr, 0, this.columnBuffer.Position);
            this.columnBuffer.Buffer = cArr;
        }
        char[] cArr2 = this.columnBuffer.Buffer;
        ColumnBuffer columnBuffer2 = this.columnBuffer;
        int i = columnBuffer2.Position;
        columnBuffer2.Position = i + 1;
        cArr2[i] = c;
        this.dataBuffer.ColumnStart = this.dataBuffer.Position + 1;
    }

    private void updateCurrentValue() {
        if (this.startedColumn && this.dataBuffer.ColumnStart < this.dataBuffer.Position) {
            if (this.columnBuffer.Buffer.length - this.columnBuffer.Position < this.dataBuffer.Position - this.dataBuffer.ColumnStart) {
                char[] cArr = new char[(this.columnBuffer.Buffer.length + Math.max(this.dataBuffer.Position - this.dataBuffer.ColumnStart, this.columnBuffer.Buffer.length))];
                System.arraycopy(this.columnBuffer.Buffer, 0, cArr, 0, this.columnBuffer.Position);
                this.columnBuffer.Buffer = cArr;
            }
            System.arraycopy(this.dataBuffer.Buffer, this.dataBuffer.ColumnStart, this.columnBuffer.Buffer, this.columnBuffer.Position, this.dataBuffer.Position - this.dataBuffer.ColumnStart);
            this.columnBuffer.Position += this.dataBuffer.Position - this.dataBuffer.ColumnStart;
        }
        this.dataBuffer.ColumnStart = this.dataBuffer.Position + 1;
    }

    private void endRecord() throws IOException {
        this.hasReadNextLine = true;
        this.currentRecord++;
    }

    public int getIndex(String str) throws IOException {
        checkClosed();
        Object obj = this.headersHolder.IndexByName.get(str);
        if (obj != null) {
            return ((Integer) obj).intValue();
        }
        return -1;
    }

    public boolean skipRecord() throws IOException {
        checkClosed();
        if (!this.hasMoreData) {
            return false;
        }
        boolean readRecord = readRecord();
        if (!readRecord) {
            return readRecord;
        }
        this.currentRecord--;
        return readRecord;
    }

    public boolean skipLine() throws IOException {
        boolean z;
        checkClosed();
        this.columnsCount = 0;
        if (this.hasMoreData) {
            boolean z2 = false;
            z = false;
            do {
                if (this.dataBuffer.Position == this.dataBuffer.Count) {
                    checkDataLength();
                } else {
                    char c = this.dataBuffer.Buffer[this.dataBuffer.Position];
                    if (c == 13 || c == 10) {
                        z2 = true;
                    }
                    this.lastLetter = c;
                    if (!z2) {
                        this.dataBuffer.Position++;
                    }
                    z = true;
                }
                if (!this.hasMoreData) {
                    break;
                }
            } while (!z2);
            this.columnBuffer.Position = 0;
            this.dataBuffer.LineStart = this.dataBuffer.Position + 1;
        } else {
            z = false;
        }
        this.rawBuffer.Position = 0;
        this.rawRecord = "";
        return z;
    }

    public void close() {
        if (!this.closed) {
            close(true);
            this.closed = true;
        }
    }

    private void close(boolean z) {
        if (!this.closed) {
            if (z) {
                this.charset = null;
                this.headersHolder.Headers = null;
                this.headersHolder.IndexByName = null;
                this.dataBuffer.Buffer = null;
                this.columnBuffer.Buffer = null;
                this.rawBuffer.Buffer = null;
            }
            try {
                if (this.initialized) {
                    this.inputStream.close();
                }
            } catch (Exception unused) {
            }
            this.inputStream = null;
            this.closed = true;
        }
    }

    private void checkClosed() throws IOException {
        if (this.closed) {
            throw new IOException("This instance of the CsvReader class has already been closed.");
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        close(false);
    }

    private class ComplexEscape {
        private static final int DECIMAL = 3;
        private static final int HEX = 4;
        private static final int OCTAL = 2;
        private static final int UNICODE = 1;

        private ComplexEscape() {
        }
    }

    private class DataBuffer {
        public char[] Buffer = new char[1024];
        public int ColumnStart = 0;
        public int Count = 0;
        public int LineStart = 0;
        public int Position = 0;

        public DataBuffer() {
        }
    }

    private class ColumnBuffer {
        public char[] Buffer = new char[50];
        public int Position = 0;

        public ColumnBuffer() {
        }
    }

    private class RawRecordBuffer {
        public char[] Buffer = new char[500];
        public int Position = 0;

        public RawRecordBuffer() {
        }
    }

    private class Letters {
        public static final char ALERT = '\u0007';
        public static final char BACKSLASH = '\\';
        public static final char BACKSPACE = '\b';
        public static final char COMMA = ',';
        public static final char CR = '\r';
        public static final char ESCAPE = '\u001b';
        public static final char FORM_FEED = '\f';
        public static final char LF = '\n';
        public static final char NULL = '\u0000';
        public static final char POUND = '#';
        public static final char QUOTE = '\"';
        public static final char SPACE = ' ';
        public static final char TAB = '\t';
        public static final char VERTICAL_TAB = '\u000b';

        private Letters() {
        }
    }

    private class UserSettings {
        public boolean CaptureRawRecord = true;
        public boolean CaseSensitive = true;
        public char Comment = '#';
        public char Delimiter = ',';
        public int EscapeMode = 1;
        public char RecordDelimiter = 0;
        public boolean SafetySwitch = true;
        public boolean SkipEmptyRecords = true;
        public char TextQualifier = '\"';
        public boolean TrimWhitespace = true;
        public boolean UseComments = false;
        public boolean UseTextQualifier = true;

        public UserSettings() {
        }
    }

    private class HeadersHolder {
        public String[] Headers = null;
        public HashMap IndexByName = new HashMap();
        public int Length = 0;

        public HeadersHolder() {
        }
    }

    private class StaticSettings {
        public static final int INITIAL_COLUMN_BUFFER_SIZE = 50;
        public static final int INITIAL_COLUMN_COUNT = 10;
        public static final int MAX_BUFFER_SIZE = 1024;
        public static final int MAX_FILE_BUFFER_SIZE = 4096;

        private StaticSettings() {
        }
    }
}
