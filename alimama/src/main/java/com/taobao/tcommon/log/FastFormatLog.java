package com.taobao.tcommon.log;

import java.util.Formatter;
import java.util.Locale;

public abstract class FastFormatLog implements FormatLog {
    public static final int COMMON_TOTAL_LENGTH = 250;
    private final Object FORMAT_LOCK = new Object();
    private Formatter mFormatter;
    private StringBuilder mSB;

    /* access modifiers changed from: protected */
    public String fastFormat(String str, Object... objArr) {
        String substring;
        synchronized (this.FORMAT_LOCK) {
            if (this.mSB == null) {
                this.mSB = new StringBuilder(250);
            } else {
                this.mSB.setLength(0);
            }
            if (this.mFormatter == null) {
                this.mFormatter = new Formatter(this.mSB, Locale.getDefault());
            }
            this.mFormatter.format(str, objArr);
            substring = this.mSB.substring(0);
        }
        return substring;
    }
}
