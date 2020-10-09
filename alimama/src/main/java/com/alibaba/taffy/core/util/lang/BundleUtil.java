package com.alibaba.taffy.core.util.lang;

import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;

public class BundleUtil {
    public static boolean isEmpty(Bundle bundle) {
        return bundle == null || bundle.isEmpty();
    }

    public static boolean isNotEmpty(Bundle bundle) {
        return !isEmpty(bundle);
    }

    public static Object getValue(Bundle bundle, String str, Object obj) {
        return (bundle == null || !bundle.containsKey(str)) ? obj : bundle.get(str);
    }

    public static Object getNotNullValue(Bundle bundle, String str, Object obj) {
        Object obj2 = bundle != null ? bundle.get(str) : null;
        return obj2 != null ? obj2 : obj;
    }

    public static Bundle merge(Bundle... bundleArr) {
        return mergeList(bundleArr);
    }

    public static Bundle mergeList(Bundle[] bundleArr) {
        Bundle bundle = new Bundle();
        for (Bundle bundle2 : bundleArr) {
            if (bundle2 != null) {
                bundle.putAll(bundle2);
            }
        }
        return bundle;
    }

    public static Bundle mergeToFirst(Bundle bundle, Bundle... bundleArr) {
        return mergeToFirstList(bundle, bundleArr);
    }

    public static Bundle mergeToFirstList(Bundle bundle, Bundle[] bundleArr) {
        if (bundle == null) {
            return null;
        }
        for (Bundle putAll : bundleArr) {
            bundle.putAll(putAll);
        }
        return bundle;
    }

    public static Map<String, Object> toMap(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.get(str));
        }
        return hashMap;
    }
}
