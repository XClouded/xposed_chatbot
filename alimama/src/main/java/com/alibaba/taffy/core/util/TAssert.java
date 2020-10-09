package com.alibaba.taffy.core.util;

import com.alibaba.taffy.core.util.lang.StringUtil;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.util.Collection;
import java.util.Map;

public abstract class TAssert {
    public static void isTrue(boolean z, String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void isTrue(boolean z) {
        isTrue(z, "[Assertion failed] - this expression must be true");
    }

    public static void isNull(Object obj, String str) {
        if (obj != null) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void isNull(Object obj) {
        isNull(obj, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object obj, String str) {
        if (obj == null) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void notNull(Object obj) {
        notNull(obj, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void notEmpty(String str, String str2) {
        if (!StringUtil.isNotEmpty(str)) {
            throw new IllegalArgumentException(str2);
        }
    }

    public static void notEmpty(String str) {
        notEmpty(str, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static void notBlank(String str, String str2) {
        if (!StringUtil.isNotBlank(str)) {
            throw new IllegalArgumentException(str2);
        }
    }

    public static void notBlank(String str) {
        notBlank(str, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    public static void doesNotContain(String str, String str2, String str3) {
        if (StringUtil.isNotEmpty(str) && StringUtil.isNotEmpty(str2) && str.contains(str2)) {
            throw new IllegalArgumentException(str3);
        }
    }

    public static void doesNotContain(String str, String str2) {
        doesNotContain(str, str2, "[Assertion failed] - this String argument must not contain the substring [" + str2 + Operators.ARRAY_END_STR);
    }

    public static void notEmpty(Object[] objArr, String str) {
        if (objArr == null || objArr.length == 0) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void notEmpty(Object[] objArr) {
        notEmpty(objArr, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    public static void noNullElements(Object[] objArr, String str) {
        if (objArr != null) {
            int length = objArr.length;
            int i = 0;
            while (i < length) {
                if (objArr[i] != null) {
                    i++;
                } else {
                    throw new IllegalArgumentException(str);
                }
            }
        }
    }

    public static void noNullElements(Object[] objArr) {
        noNullElements(objArr, "[Assertion failed] - this array must not contain any null elements");
    }

    public static void notEmpty(Collection<?> collection, String str) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Map<?, ?> map, String str) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    public static void isInstanceOf(Class<?> cls, Object obj) {
        isInstanceOf(cls, obj, "");
    }

    public static void isInstanceOf(Class<?> cls, Object obj, String str) {
        String str2;
        notNull(cls, "Type to check against must not be null");
        if (!cls.isInstance(obj)) {
            StringBuilder sb = new StringBuilder();
            if (StringUtil.isNotEmpty(str)) {
                str2 = str + Operators.SPACE_STR;
            } else {
                str2 = "";
            }
            sb.append(str2);
            sb.append("Object of class [");
            sb.append(obj != null ? obj.getClass().getName() : BuildConfig.buildJavascriptFrameworkVersion);
            sb.append("] must be an instance of ");
            sb.append(cls);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public static void isAssignable(Class<?> cls, Class<?> cls2) {
        isAssignable(cls, cls2, "");
    }

    public static void isAssignable(Class<?> cls, Class<?> cls2, String str) {
        notNull(cls, "Type to check against must not be null");
        if (cls2 == null || !cls.isAssignableFrom(cls2)) {
            throw new IllegalArgumentException(str + cls2 + " is not assignable to " + cls);
        }
    }

    public static void state(boolean z, String str) {
        if (!z) {
            throw new IllegalStateException(str);
        }
    }

    public static void state(boolean z) {
        state(z, "[Assertion failed] - this state invariant must be true");
    }
}
