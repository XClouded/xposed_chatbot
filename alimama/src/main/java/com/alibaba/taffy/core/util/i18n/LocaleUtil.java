package com.alibaba.taffy.core.util.i18n;

import com.alibaba.taffy.core.util.lang.StringUtil;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtil {
    private static final Set<String> AVAILABLE_COUNTRIES = new HashSet();
    private static final Set<String> AVAILABLE_LANGUAGES = new HashSet();
    private static final ThreadLocal<LocaleInfo> contextLocaleInfoHolder = new ThreadLocal<>();
    private static LocaleInfo defaultLocalInfo = systemLocaleInfo;
    private static final LocaleInfo systemLocaleInfo = new LocaleInfo();

    static {
        for (Locale locale : Locale.getAvailableLocales()) {
            AVAILABLE_LANGUAGES.add(locale.getLanguage());
            AVAILABLE_COUNTRIES.add(locale.getCountry());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r5 = com.alibaba.taffy.core.util.lang.StringUtil.split(r5, "_");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Locale parseLocale(java.lang.String r5) {
        /*
            if (r5 == 0) goto L_0x0022
            java.lang.String r0 = "_"
            java.lang.String[] r5 = com.alibaba.taffy.core.util.lang.StringUtil.split((java.lang.String) r5, (java.lang.String) r0)
            int r0 = r5.length
            if (r0 <= 0) goto L_0x0022
            r1 = 0
            r1 = r5[r1]
            java.lang.String r2 = ""
            java.lang.String r3 = ""
            r4 = 1
            if (r0 <= r4) goto L_0x0017
            r2 = r5[r4]
        L_0x0017:
            r4 = 2
            if (r0 <= r4) goto L_0x001c
            r3 = r5[r4]
        L_0x001c:
            java.util.Locale r5 = new java.util.Locale
            r5.<init>(r1, r2, r3)
            return r5
        L_0x0022:
            r5 = 0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.i18n.LocaleUtil.parseLocale(java.lang.String):java.util.Locale");
    }

    public static LocaleInfo parseLocaleInfo(String str) {
        Locale locale;
        String str2 = null;
        if (StringUtil.isNotEmpty(str)) {
            int indexOf = str.indexOf(":");
            if (indexOf >= 0) {
                String substring = str.substring(0, indexOf);
                str2 = str.substring(indexOf + 1);
                str = substring;
            }
            locale = parseLocale(str);
            str2 = StringUtil.trimToNull(str2);
        } else {
            locale = null;
        }
        return new LocaleInfo(locale, str2);
    }

    public static boolean isLocaleSupported(Locale locale) {
        return locale != null && AVAILABLE_LANGUAGES.contains(locale.getLanguage()) && AVAILABLE_COUNTRIES.contains(locale.getCountry());
    }

    public static boolean isCharsetSupported(String str) {
        return Charset.isSupported(str);
    }

    public static String getCanonicalCharset(String str) {
        return Charset.forName(str).name();
    }

    public static String getCanonicalCharset(String str, String str2) {
        try {
            return getCanonicalCharset(str);
        } catch (IllegalArgumentException unused) {
            if (str2 != null) {
                try {
                    return getCanonicalCharset(str2);
                } catch (IllegalArgumentException unused2) {
                    return null;
                }
            }
            return null;
        }
    }

    public static List calculateBundleNames(String str, Locale locale) {
        return calculateBundleNames(str, locale, false);
    }

    public static List calculateBundleNames(String str, Locale locale, boolean z) {
        int lastIndexOf;
        String str2 = (String) StringUtil.defaultIfEmpty(str, "");
        if (locale == null) {
            locale = new Locale("");
        }
        String str3 = "";
        int i = 0;
        if (!z && (lastIndexOf = str2.lastIndexOf(".")) != -1) {
            str3 = str2.substring(lastIndexOf, str2.length());
            int length = str3.length();
            str2 = str2.substring(0, lastIndexOf);
            if (length == 1) {
                str3 = "";
            } else {
                i = length;
            }
        }
        ArrayList arrayList = new ArrayList(4);
        String language = locale.getLanguage();
        int length2 = language.length();
        String country = locale.getCountry();
        int length3 = country.length();
        String variant = locale.getVariant();
        int length4 = variant.length();
        StringBuilder sb = new StringBuilder(str2);
        sb.append(str3);
        arrayList.add(sb.toString());
        sb.setLength(sb.length() - i);
        if (length2 + length3 + length4 == 0) {
            return arrayList;
        }
        if (sb.length() > 0) {
            sb.append('_');
        }
        sb.append(language);
        if (length2 > 0) {
            sb.append(str3);
            arrayList.add(sb.toString());
            sb.setLength(sb.length() - i);
        }
        if (length3 + length4 == 0) {
            return arrayList;
        }
        sb.append('_');
        sb.append(country);
        if (length3 > 0) {
            sb.append(str3);
            arrayList.add(sb.toString());
            sb.setLength(sb.length() - i);
        }
        if (length4 == 0) {
            return arrayList;
        }
        sb.append('_');
        sb.append(variant);
        sb.append(str3);
        arrayList.add(sb.toString());
        sb.setLength(sb.length() - i);
        return arrayList;
    }

    public static LocaleInfo getSystem() {
        return systemLocaleInfo;
    }

    public static LocaleInfo getDefault() {
        return defaultLocalInfo == null ? systemLocaleInfo : defaultLocalInfo;
    }

    public static LocaleInfo setDefault(Locale locale) {
        LocaleInfo localeInfo = getDefault();
        defaultLocalInfo = new LocaleInfo(locale, (String) null, systemLocaleInfo);
        return localeInfo;
    }

    public static LocaleInfo setDefault(Locale locale, String str) {
        LocaleInfo localeInfo = getDefault();
        defaultLocalInfo = new LocaleInfo(locale, str, systemLocaleInfo);
        return localeInfo;
    }

    public static LocaleInfo setDefault(LocaleInfo localeInfo) {
        if (localeInfo == null) {
            return setDefault((Locale) null, (String) null);
        }
        LocaleInfo localeInfo2 = getDefault();
        defaultLocalInfo = localeInfo;
        return localeInfo2;
    }

    public static void resetDefault() {
        defaultLocalInfo = systemLocaleInfo;
    }

    public static LocaleInfo getContext() {
        LocaleInfo localeInfo = contextLocaleInfoHolder.get();
        return localeInfo == null ? getDefault() : localeInfo;
    }

    public static LocaleInfo setContext(Locale locale) {
        LocaleInfo context = getContext();
        contextLocaleInfoHolder.set(new LocaleInfo(locale, (String) null, defaultLocalInfo));
        return context;
    }

    public static LocaleInfo setContext(Locale locale, String str) {
        LocaleInfo context = getContext();
        contextLocaleInfoHolder.set(new LocaleInfo(locale, str, defaultLocalInfo));
        return context;
    }

    public static LocaleInfo setContext(LocaleInfo localeInfo) {
        if (localeInfo == null) {
            return setContext((Locale) null, (String) null);
        }
        LocaleInfo context = getContext();
        contextLocaleInfoHolder.set(localeInfo);
        return context;
    }

    public static void resetContext() {
        contextLocaleInfoHolder.set((Object) null);
    }
}
