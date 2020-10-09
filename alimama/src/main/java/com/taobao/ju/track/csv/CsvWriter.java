package com.taobao.ju.track.csv;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class CsvWriter {
    public static final int ESCAPE_MODE_BACKSLASH = 2;
    public static final int ESCAPE_MODE_DOUBLED = 1;
    private Charset charset;
    private boolean closed;
    private String fileName;
    private boolean firstColumn;
    private boolean initialized;
    private Writer outputStream;
    private String systemRecordDelimiter;
    private boolean useCustomRecordDelimiter;
    private UserSettings userSettings;

    public CsvWriter(String str, char c, Charset charset2) {
        this.outputStream = null;
        this.fileName = null;
        this.firstColumn = true;
        this.useCustomRecordDelimiter = false;
        this.charset = null;
        this.userSettings = new UserSettings();
        this.initialized = false;
        this.closed = false;
        this.systemRecordDelimiter = System.getProperty("line.separator");
        if (str == null) {
            throw new IllegalArgumentException("Parameter fileName can not be null.");
        } else if (charset2 != null) {
            this.fileName = str;
            this.userSettings.Delimiter = c;
            this.charset = charset2;
        } else {
            throw new IllegalArgumentException("Parameter charset can not be null.");
        }
    }

    public CsvWriter(String str) {
        this(str, ',', Charset.forName("ISO-8859-1"));
    }

    public CsvWriter(Writer writer, char c) {
        this.outputStream = null;
        this.fileName = null;
        this.firstColumn = true;
        this.useCustomRecordDelimiter = false;
        this.charset = null;
        this.userSettings = new UserSettings();
        this.initialized = false;
        this.closed = false;
        this.systemRecordDelimiter = System.getProperty("line.separator");
        if (writer != null) {
            this.outputStream = writer;
            this.userSettings.Delimiter = c;
            this.initialized = true;
            return;
        }
        throw new IllegalArgumentException("Parameter outputStream can not be null.");
    }

    public CsvWriter(OutputStream outputStream2, char c, Charset charset2) {
        this(new OutputStreamWriter(outputStream2, charset2), c);
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

    public int getEscapeMode() {
        return this.userSettings.EscapeMode;
    }

    public void setEscapeMode(int i) {
        this.userSettings.EscapeMode = i;
    }

    public void setComment(char c) {
        this.userSettings.Comment = c;
    }

    public char getComment() {
        return this.userSettings.Comment;
    }

    public boolean getForceQualifier() {
        return this.userSettings.ForceQualifier;
    }

    public void setForceQualifier(boolean z) {
        this.userSettings.ForceQualifier = z;
    }

    public void write(String str, boolean z) throws IOException {
        char charAt;
        checkClosed();
        checkInit();
        if (str == null) {
            str = "";
        }
        if (!this.firstColumn) {
            this.outputStream.write(this.userSettings.Delimiter);
        }
        boolean z2 = this.userSettings.ForceQualifier;
        if (!z && str.length() > 0) {
            str = str.trim();
        }
        if (!z2 && this.userSettings.UseTextQualifier && (r6.indexOf(this.userSettings.TextQualifier) > -1 || r6.indexOf(this.userSettings.Delimiter) > -1 || ((!this.useCustomRecordDelimiter && (r6.indexOf(10) > -1 || r6.indexOf(13) > -1)) || ((this.useCustomRecordDelimiter && r6.indexOf(this.userSettings.RecordDelimiter) > -1) || ((this.firstColumn && r6.length() > 0 && r6.charAt(0) == this.userSettings.Comment) || (this.firstColumn && r6.length() == 0)))))) {
            z2 = true;
        }
        if (this.userSettings.UseTextQualifier && !z2 && r6.length() > 0 && z) {
            char charAt2 = r6.charAt(0);
            if (charAt2 == ' ' || charAt2 == 9) {
                z2 = true;
            }
            if (!z2 && r6.length() > 1 && ((charAt = r6.charAt(r6.length() - 1)) == ' ' || charAt == 9)) {
                z2 = true;
            }
        }
        if (z2) {
            this.outputStream.write(this.userSettings.TextQualifier);
            if (this.userSettings.EscapeMode == 2) {
                r6 = replace(replace(r6, "\\", "\\\\"), "" + this.userSettings.TextQualifier, "\\" + this.userSettings.TextQualifier);
            } else {
                r6 = replace(r6, "" + this.userSettings.TextQualifier, "" + this.userSettings.TextQualifier + this.userSettings.TextQualifier);
            }
        } else if (this.userSettings.EscapeMode == 2) {
            String replace = replace(replace(r6, "\\", "\\\\"), "" + this.userSettings.Delimiter, "\\" + this.userSettings.Delimiter);
            if (this.useCustomRecordDelimiter) {
                r6 = replace(replace, "" + this.userSettings.RecordDelimiter, "\\" + this.userSettings.RecordDelimiter);
            } else {
                r6 = replace(replace(replace, "\r", "\\\r"), "\n", "\\\n");
            }
            if (this.firstColumn && r6.length() > 0 && r6.charAt(0) == this.userSettings.Comment) {
                if (r6.length() > 1) {
                    r6 = "\\" + this.userSettings.Comment + r6.substring(1);
                } else {
                    r6 = "\\" + this.userSettings.Comment;
                }
            }
        }
        this.outputStream.write(r6);
        if (z2) {
            this.outputStream.write(this.userSettings.TextQualifier);
        }
        this.firstColumn = false;
    }

    public void write(String str) throws IOException {
        write(str, false);
    }

    public void writeComment(String str) throws IOException {
        checkClosed();
        checkInit();
        this.outputStream.write(this.userSettings.Comment);
        this.outputStream.write(str);
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.RecordDelimiter);
        } else {
            this.outputStream.write(this.systemRecordDelimiter);
        }
        this.firstColumn = true;
    }

    public void writeRecord(String[] strArr, boolean z) throws IOException {
        if (strArr != null && strArr.length > 0) {
            for (String write : strArr) {
                write(write, z);
            }
            endRecord();
        }
    }

    public void writeRecord(String[] strArr) throws IOException {
        writeRecord(strArr, false);
    }

    public void endRecord() throws IOException {
        checkClosed();
        checkInit();
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.RecordDelimiter);
        } else {
            this.outputStream.write(this.systemRecordDelimiter);
        }
        this.firstColumn = true;
    }

    private void checkInit() throws IOException {
        if (!this.initialized) {
            if (this.fileName != null) {
                this.outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.fileName), this.charset));
            }
            this.initialized = true;
        }
    }

    public void flush() throws IOException {
        this.outputStream.flush();
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
            }
            try {
                if (this.initialized) {
                    this.outputStream.close();
                }
            } catch (Exception unused) {
            }
            this.outputStream = null;
            this.closed = true;
        }
    }

    private void checkClosed() throws IOException {
        if (this.closed) {
            throw new IOException("This instance of the CsvWriter class has already been closed.");
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        close(false);
    }

    private class Letters {
        public static final char BACKSLASH = '\\';
        public static final char COMMA = ',';
        public static final char CR = '\r';
        public static final char LF = '\n';
        public static final char NULL = '\u0000';
        public static final char POUND = '#';
        public static final char QUOTE = '\"';
        public static final char SPACE = ' ';
        public static final char TAB = '\t';

        private Letters() {
        }
    }

    private class UserSettings {
        public char Comment = '#';
        public char Delimiter = ',';
        public int EscapeMode = 1;
        public boolean ForceQualifier = false;
        public char RecordDelimiter = 0;
        public char TextQualifier = '\"';
        public boolean UseTextQualifier = true;

        public UserSettings() {
        }
    }

    public static String replace(String str, String str2, String str3) {
        int length = str2.length();
        int indexOf = str.indexOf(str2);
        if (indexOf <= -1) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (indexOf != -1) {
            stringBuffer.append(str.substring(i, indexOf));
            stringBuffer.append(str3);
            i = indexOf + length;
            indexOf = str.indexOf(str2, i);
        }
        stringBuffer.append(str.substring(i));
        return stringBuffer.toString();
    }
}
