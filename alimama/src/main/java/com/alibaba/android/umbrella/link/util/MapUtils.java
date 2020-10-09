package com.alibaba.android.umbrella.link.util;

import androidx.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class MapUtils {
    public static void safePutAll(@Nullable Map<String, Object> map, @Nullable Map<String, ?> map2) {
        String str;
        if (map != null && map2 != null && !map2.isEmpty()) {
            Set<Map.Entry<String, ?>> entrySet = map2.entrySet();
            if (!entrySet.isEmpty()) {
                for (Map.Entry next : entrySet) {
                    if (!(next == null || (str = (String) next.getKey()) == null)) {
                        Object value = next.getValue();
                        if (value == null) {
                            map.put(str, "empty value");
                        } else {
                            map.put(str, value);
                        }
                    }
                }
            }
        }
    }

    public static void safePutAllString(@Nullable Map<String, String> map, @Nullable Map<String, String> map2) {
        String str;
        if (map != null && map2 != null && !map2.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = map2.entrySet();
            if (!entrySet.isEmpty()) {
                for (Map.Entry next : entrySet) {
                    if (!(next == null || (str = (String) next.getKey()) == null)) {
                        String str2 = (String) next.getValue();
                        if (str2 == null) {
                            map.put(str, "empty value");
                        } else {
                            map.put(str, str2);
                        }
                    }
                }
            }
        }
    }
}
