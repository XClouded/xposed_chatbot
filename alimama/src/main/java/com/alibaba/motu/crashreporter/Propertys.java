package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.crashreporter.Options;
import java.util.Map;

public class Propertys extends Options<Property> {
    Context mContext;

    public Propertys() {
        super(true);
    }

    public static class Property extends Options.Option {
        Property(String str, String str2, boolean z) {
            super(str, str2, z);
        }

        public Property(String str, String str2) {
            super(str, str2);
        }
    }

    public String getValue(String str) {
        return (String) super.getValue(str);
    }

    public void copyTo(Map<String, String> map) {
        for (String str : this.mData.keySet()) {
            Property property = (Property) this.mData.get(str);
            if (property.value instanceof String) {
                map.put(str, (String) property.value);
            }
        }
    }
}
