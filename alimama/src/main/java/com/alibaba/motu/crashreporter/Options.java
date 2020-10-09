package com.alibaba.motu.crashreporter;

import com.alibaba.motu.crashreporter.Options.Option;
import com.alibaba.motu.tbrest.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Options<T extends Option> {
    Map<String, T> mData;

    public static class Option {
        String name;
        boolean readOnly;
        Object value;

        Option(String str, Object obj, boolean z) {
            this.name = str;
            this.value = obj;
            this.readOnly = z;
        }

        public Option(String str, Object obj) {
            this(str, obj, false);
        }
    }

    Options() {
        this(false);
    }

    Options(boolean z) {
        if (z) {
            this.mData = new ConcurrentHashMap();
        } else {
            this.mData = new HashMap();
        }
    }

    public void add(T t) {
        if (t != null && StringUtils.isNotBlank(t.name) && t.value != null) {
            Option option = (Option) this.mData.get(t.name);
            if (option == null || (option != null && !option.readOnly)) {
                this.mData.put(t.name, t);
            }
        }
    }

    public void remove(T t) {
        if (t != null && StringUtils.isBlank(t.name)) {
            this.mData.remove(t.name);
        }
    }

    public Object getValue(String str) {
        Option option = (Option) this.mData.get(str);
        if (option != null) {
            return option.value;
        }
        return null;
    }

    public Object getValue(String str, Object obj) {
        Object value = getValue(str);
        return value != null ? value : obj;
    }

    public String getString(String str, String str2) {
        try {
            Object value = getValue(str);
            if (value instanceof String) {
                return (String) value;
            }
        } catch (Exception unused) {
        }
        return str2;
    }

    public int getInt(String str, int i) {
        try {
            Object value = getValue(str);
            if (value instanceof Integer) {
                return ((Integer) value).intValue();
            }
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            }
            return i;
        } catch (Exception unused) {
        }
    }

    public boolean getBoolean(String str, boolean z) {
        try {
            Object value = getValue(str);
            if (value instanceof Boolean) {
                return ((Boolean) value).booleanValue();
            }
        } catch (Exception unused) {
        }
        return z;
    }
}
