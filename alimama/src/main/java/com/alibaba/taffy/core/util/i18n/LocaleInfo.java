package com.alibaba.taffy.core.util.i18n;

import com.alibaba.taffy.core.util.lang.ObjectUtil;
import com.alibaba.taffy.core.util.lang.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Locale;

public final class LocaleInfo implements Cloneable, Serializable {
    private static final CharsetMap CHARSET_MAP = new CharsetMap();
    private static final long serialVersionUID = 3257847675461251635L;
    private String charset;
    private Locale locale;

    LocaleInfo() {
        this.locale = Locale.getDefault();
        this.charset = LocaleUtil.getCanonicalCharset(new OutputStreamWriter(new ByteArrayOutputStream()).getEncoding(), "ISO-8859-1");
    }

    public LocaleInfo(Locale locale2) {
        this(locale2, (String) null);
    }

    public LocaleInfo(Locale locale2, String str) {
        this(locale2, str, LocaleUtil.getDefault());
    }

    LocaleInfo(Locale locale2, String str, LocaleInfo localeInfo) {
        if (locale2 == null) {
            locale2 = localeInfo.getLocale();
            if (StringUtil.isEmpty(str)) {
                str = localeInfo.getCharset();
            }
        }
        str = StringUtil.isEmpty(str) ? CHARSET_MAP.getCharSet(locale2) : str;
        this.locale = locale2;
        this.charset = LocaleUtil.getCanonicalCharset(str, localeInfo.getCharset());
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getCharset() {
        return this.charset;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LocaleInfo)) {
            return false;
        }
        LocaleInfo localeInfo = (LocaleInfo) obj;
        if (!ObjectUtil.equals(this.locale, localeInfo.locale) || !ObjectUtil.equals(this.charset, localeInfo.charset)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.charset) ^ ObjectUtil.hashCode(this.locale);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    public String toString() {
        return this.locale + ":" + this.charset;
    }
}
