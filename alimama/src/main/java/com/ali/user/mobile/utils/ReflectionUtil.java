package com.ali.user.mobile.utils;

import com.ali.user.mobile.log.TLogAdapter;
import java.lang.reflect.Method;

public class ReflectionUtil {
    public static final String Tag = "login.Reflection";

    public static <T> T invokeMethod(Class cls, Method method, Object... objArr) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(cls, objArr);
        } catch (Exception e) {
            TLogAdapter.e(Tag, "invokeMethod error", e);
            return null;
        }
    }
}
