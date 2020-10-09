package com.alibaba.taffy.core.util.lang;

import android.os.Bundle;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <K, V> Map<K, V> synchronizedMap(Map<K, V> map) {
        return Collections.synchronizedMap(map);
    }

    public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> map) {
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> V getValue(Map<? extends K, ? extends V> map, K k, V v) {
        return (map == null || !map.containsKey(k)) ? v : map.get(k);
    }

    public static <K, V> V getNotNullValue(Map<? extends K, ? extends V> map, K k, V v) {
        V v2 = map != null ? map.get(k) : null;
        return v2 != null ? v2 : v;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> merge(Map<? extends K, ? extends V>... mapArr) {
        return mergeList(mapArr);
    }

    public static <K, V> Map<K, V> mergeList(Map<? extends K, ? extends V>[] mapArr) {
        HashMap hashMap = new HashMap();
        for (Map<? extends K, ? extends V> map : mapArr) {
            if (map != null) {
                hashMap.putAll(map);
            }
        }
        return hashMap;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> mergeToFirst(Map<K, V> map, Map<? extends K, ? extends V>... mapArr) {
        return mergeToFirstList(map, mapArr);
    }

    public static <K, V> Map<K, V> mergeToFirstList(Map<K, V> map, Map<? extends K, ? extends V>[] mapArr) {
        if (map == null) {
            return null;
        }
        for (Map<? extends K, ? extends V> putAll : mapArr) {
            map.putAll(putAll);
        }
        return map;
    }

    public static Bundle toBundle(Map map) {
        if (map == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (Object next : map.keySet()) {
            Object obj = map.get(next);
            if (obj == null) {
                bundle.putSerializable(String.valueOf(next), (Serializable) null);
            } else if (obj instanceof Serializable) {
                bundle.putSerializable(String.valueOf(next), (Serializable) obj);
            } else {
                bundle.putString(next.toString(), String.valueOf(obj));
            }
        }
        return bundle;
    }
}
