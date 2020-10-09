package com.taobao.ju.track.spm;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropUtil {
    @TargetApi(9)
    public static Map<String, String> toMap(Properties properties) {
        HashMap hashMap = new HashMap();
        if (properties != null && properties.size() > 0) {
            for (String next : properties.stringPropertyNames()) {
                hashMap.put(next, properties.getProperty(next));
            }
        }
        return hashMap;
    }

    @TargetApi(9)
    public static String toArray(Properties properties) {
        if (properties == null || properties.size() <= 0) {
            return null;
        }
        String[] strArr = new String[properties.size()];
        int i = 0;
        properties.toString();
        for (String next : properties.stringPropertyNames()) {
            strArr[i] = next + properties.getProperty(next);
            i++;
        }
        return null;
    }

    public static Properties buildProps(String[] strArr, String[] strArr2) {
        Properties properties = new Properties();
        if (!(strArr == null || strArr2 == null)) {
            int max = Math.max(strArr.length, strArr2.length);
            for (int i = 0; i < max; i++) {
                if (!TextUtils.isEmpty(strArr[i]) && !TextUtils.isEmpty(strArr2[i])) {
                    properties.put(strArr[i], strArr2[i]);
                }
            }
        }
        return properties;
    }

    @TargetApi(9)
    public static Properties loadConfig(Context context, InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(inputStream, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void saveConfig(Context context, String str, Properties properties) {
        try {
            properties.store(new FileOutputStream(str, false), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
