package com.alibaba.taffy.core.util.i18n;

import com.alipay.sdk.sys.a;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class CharsetMap {
    public static final String CHARSET_RESOURCE = "charset.properties";
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    private static final int MAP_CACHE = 0;
    private static final int MAP_COM = 5;
    private static final int MAP_HOME = 2;
    private static final int MAP_JAR = 4;
    private static final int MAP_PROG = 1;
    private static final int MAP_SYS = 3;
    private static HashMap commonMapper = new HashMap();
    private Map[] mappers;

    static {
        commonMapper.put("ar", "ISO-8859-6");
        commonMapper.put("be", "ISO-8859-5");
        commonMapper.put("bg", "ISO-8859-5");
        commonMapper.put("ca", "ISO-8859-1");
        commonMapper.put("cs", "ISO-8859-2");
        commonMapper.put("da", "ISO-8859-1");
        commonMapper.put("de", "ISO-8859-1");
        commonMapper.put("el", "ISO-8859-7");
        commonMapper.put("en", "ISO-8859-1");
        commonMapper.put("es", "ISO-8859-1");
        commonMapper.put("et", "ISO-8859-1");
        commonMapper.put("fi", "ISO-8859-1");
        commonMapper.put("fr", "ISO-8859-1");
        commonMapper.put("hr", "ISO-8859-2");
        commonMapper.put("hu", "ISO-8859-2");
        commonMapper.put("is", "ISO-8859-1");
        commonMapper.put("it", "ISO-8859-1");
        commonMapper.put("iw", "ISO-8859-8");
        commonMapper.put("ja", "Shift_JIS");
        commonMapper.put("ko", "EUC-KR");
        commonMapper.put("lt", "ISO-8859-2");
        commonMapper.put("lv", "ISO-8859-2");
        commonMapper.put("mk", "ISO-8859-5");
        commonMapper.put("nl", "ISO-8859-1");
        commonMapper.put("no", "ISO-8859-1");
        commonMapper.put("pl", "ISO-8859-2");
        commonMapper.put("pt", "ISO-8859-1");
        commonMapper.put("ro", "ISO-8859-2");
        commonMapper.put("ru", "ISO-8859-5");
        commonMapper.put("sh", "ISO-8859-5");
        commonMapper.put("sk", "ISO-8859-2");
        commonMapper.put("sl", "ISO-8859-2");
        commonMapper.put("sq", "ISO-8859-2");
        commonMapper.put("sr", "ISO-8859-5");
        commonMapper.put(a.h, "ISO-8859-1");
        commonMapper.put("tr", "ISO-8859-9");
        commonMapper.put("uk", "ISO-8859-5");
        commonMapper.put("zh", "GB18030");
        commonMapper.put("zh_TW", "Big5");
    }

    protected static Map loadStream(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return new HashMap(properties);
    }

    protected static Map loadFile(File file) throws IOException {
        return loadStream(new FileInputStream(file));
    }

    protected static Map loadPath(String str) throws IOException {
        return loadFile(new File(str));
    }

    protected static Map loadResource(String str) {
        InputStream resourceAsStream = CharsetMap.class.getResourceAsStream(str);
        if (resourceAsStream == null) {
            return null;
        }
        try {
            return loadStream(resourceAsStream);
        } catch (IOException unused) {
            return null;
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x002f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public CharsetMap() {
        /*
            r3 = this;
            r3.<init>()
            r0 = 6
            java.util.Map[] r0 = new java.util.Map[r0]
            r3.mappers = r0
            java.lang.String r0 = "user.home"
            java.lang.String r0 = java.lang.System.getProperty(r0)     // Catch:{ Exception -> 0x002f }
            if (r0 == 0) goto L_0x002f
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x002f }
            r1.<init>()     // Catch:{ Exception -> 0x002f }
            r1.append(r0)     // Catch:{ Exception -> 0x002f }
            java.lang.String r0 = java.io.File.separator     // Catch:{ Exception -> 0x002f }
            r1.append(r0)     // Catch:{ Exception -> 0x002f }
            java.lang.String r0 = "charset.properties"
            r1.append(r0)     // Catch:{ Exception -> 0x002f }
            java.lang.String r0 = r1.toString()     // Catch:{ Exception -> 0x002f }
            java.util.Map[] r1 = r3.mappers     // Catch:{ Exception -> 0x002f }
            r2 = 2
            java.util.Map r0 = loadPath(r0)     // Catch:{ Exception -> 0x002f }
            r1[r2] = r0     // Catch:{ Exception -> 0x002f }
        L_0x002f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005e }
            r0.<init>()     // Catch:{ Exception -> 0x005e }
            java.lang.String r1 = "java.home"
            java.lang.String r1 = java.lang.System.getProperty(r1)     // Catch:{ Exception -> 0x005e }
            r0.append(r1)     // Catch:{ Exception -> 0x005e }
            java.lang.String r1 = java.io.File.separator     // Catch:{ Exception -> 0x005e }
            r0.append(r1)     // Catch:{ Exception -> 0x005e }
            java.lang.String r1 = "lib"
            r0.append(r1)     // Catch:{ Exception -> 0x005e }
            java.lang.String r1 = java.io.File.separator     // Catch:{ Exception -> 0x005e }
            r0.append(r1)     // Catch:{ Exception -> 0x005e }
            java.lang.String r1 = "charset.properties"
            r0.append(r1)     // Catch:{ Exception -> 0x005e }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x005e }
            java.util.Map[] r1 = r3.mappers     // Catch:{ Exception -> 0x005e }
            r2 = 3
            java.util.Map r0 = loadPath(r0)     // Catch:{ Exception -> 0x005e }
            r1[r2] = r0     // Catch:{ Exception -> 0x005e }
        L_0x005e:
            java.util.Map[] r0 = r3.mappers
            r1 = 4
            java.lang.String r2 = "/META-INF/charset.properties"
            java.util.Map r2 = loadResource(r2)
            r0[r1] = r2
            java.util.Map[] r0 = r3.mappers
            r1 = 5
            java.util.HashMap r2 = commonMapper
            r0[r1] = r2
            java.util.Map[] r0 = r3.mappers
            r1 = 0
            java.util.Hashtable r2 = new java.util.Hashtable
            r2.<init>()
            r0[r1] = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.i18n.CharsetMap.<init>():void");
    }

    public CharsetMap(Properties properties) {
        this();
        this.mappers[1] = new HashMap(properties);
    }

    public CharsetMap(InputStream inputStream) throws IOException {
        this();
        this.mappers[1] = loadStream(inputStream);
    }

    public CharsetMap(File file) throws IOException {
        this();
        this.mappers[1] = loadFile(file);
    }

    public CharsetMap(String str) throws IOException {
        this();
        this.mappers[1] = loadPath(str);
    }

    public synchronized void setCharSet(String str, String str2) {
        HashMap hashMap = (HashMap) this.mappers[1];
        HashMap hashMap2 = hashMap != null ? (HashMap) hashMap.clone() : new HashMap();
        hashMap2.put(str, str2);
        this.mappers[1] = hashMap2;
        this.mappers[0].clear();
    }

    public String getCharSet(Locale locale) {
        String locale2 = locale.toString();
        if (locale2.length() == 0) {
            locale2 = "__" + locale.getVariant();
            if (locale2.length() == 2) {
                return "ISO-8859-1";
            }
        }
        String searchCharSet = searchCharSet(locale2);
        if (searchCharSet.length() != 0) {
            return searchCharSet;
        }
        String[] strArr = new String[3];
        strArr[2] = locale.getVariant();
        strArr[1] = locale.getCountry();
        strArr[0] = locale.getLanguage();
        String searchCharSet2 = searchCharSet(strArr);
        if (searchCharSet2.length() == 0) {
            searchCharSet2 = "ISO-8859-1";
        }
        String str = searchCharSet2;
        this.mappers[0].put(locale2, str);
        return str;
    }

    public String getCharSet(Locale locale, String str) {
        String str2;
        if (str == null || str.length() <= 0) {
            return getCharSet(locale);
        }
        String locale2 = locale.toString();
        if (locale2.length() == 0) {
            String str3 = "__" + locale.getVariant();
            if (str3.length() > 2) {
                str2 = str3 + '_' + str;
            } else {
                str2 = str3 + str;
            }
        } else if (locale.getCountry().length() == 0) {
            str2 = locale2 + "__" + str;
        } else {
            str2 = locale2 + '_' + str;
        }
        String searchCharSet = searchCharSet(str2);
        if (searchCharSet.length() != 0) {
            return searchCharSet;
        }
        String[] strArr = new String[4];
        strArr[3] = str;
        strArr[2] = locale.getVariant();
        strArr[1] = locale.getCountry();
        strArr[0] = locale.getLanguage();
        String searchCharSet2 = searchCharSet(strArr);
        if (searchCharSet2.length() == 0) {
            searchCharSet2 = "ISO-8859-1";
        }
        String str4 = searchCharSet2;
        this.mappers[0].put(str2, str4);
        return str4;
    }

    public String getCharSet(String str) {
        String searchCharSet = searchCharSet(str);
        return searchCharSet.length() > 0 ? searchCharSet : "ISO-8859-1";
    }

    public String getCharSet(String str, String str2) {
        String searchCharSet = searchCharSet(str);
        return searchCharSet.length() > 0 ? searchCharSet : str2;
    }

    private String searchCharSet(String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int length = strArr.length; length > 0; length--) {
            String searchCharSet = searchCharSet(strArr, stringBuffer, length);
            if (searchCharSet.length() > 0) {
                return searchCharSet;
            }
            stringBuffer.setLength(0);
        }
        return "";
    }

    private String searchCharSet(String[] strArr, StringBuffer stringBuffer, int i) {
        int i2 = i - 1;
        if (i2 < 0 || strArr[i2] == null || strArr[i2].length() <= 0) {
            return "";
        }
        stringBuffer.insert(0, strArr[i2]);
        int length = stringBuffer.length();
        for (int i3 = i2; i3 > 0; i3--) {
            if (i3 == i2 || i3 <= 1) {
                stringBuffer.insert(0, '_');
                length++;
            }
            String searchCharSet = searchCharSet(strArr, stringBuffer, i3);
            if (searchCharSet.length() > 0) {
                return searchCharSet;
            }
            stringBuffer.delete(0, stringBuffer.length() - length);
        }
        return searchCharSet(stringBuffer.toString());
    }

    private String searchCharSet(String str) {
        String str2;
        if (str == null || str.length() <= 0) {
            return "";
        }
        int i = 0;
        while (i < this.mappers.length) {
            Map map = this.mappers[i];
            if (map == null || (str2 = (String) map.get(str)) == null) {
                i++;
            } else {
                if (i > 0) {
                    this.mappers[0].put(str, str2);
                }
                return str2;
            }
        }
        this.mappers[0].put(str, "");
        return "";
    }

    /* access modifiers changed from: protected */
    public synchronized void setCommonCharSet(String str, String str2) {
        HashMap hashMap = (HashMap) ((HashMap) this.mappers[5]).clone();
        hashMap.put(str, str2);
        this.mappers[5] = hashMap;
        this.mappers[0].clear();
    }
}
