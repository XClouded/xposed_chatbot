package com.alibaba.ut.abtest.internal.util;

import com.taobao.weex.el.parse.Operators;

public final class ClassUtils {
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final char PACKAGE_SEPARATOR_CHAR = '.';

    private ClassUtils() {
    }

    public static Class<?> findClassIfExists(String str, ClassLoader classLoader) {
        try {
            return findClass(str, classLoader);
        } catch (ClassNotFoundError unused) {
            return null;
        }
    }

    public static Class<?> findClass(String str, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();
        }
        try {
            return getClass(classLoader, str, false);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundError(e);
        }
    }

    public static Class<?> getClass(ClassLoader classLoader, String str, boolean z) throws ClassNotFoundException {
        try {
            return Class.forName(toCanonicalName(str), z, classLoader);
        } catch (ClassNotFoundException e) {
            int lastIndexOf = str.lastIndexOf(46);
            if (lastIndexOf != -1) {
                try {
                    return getClass(classLoader, str.substring(0, lastIndexOf) + '$' + str.substring(lastIndexOf + 1), z);
                } catch (ClassNotFoundException unused) {
                    throw e;
                }
            }
            throw e;
        }
    }

    private static String toCanonicalName(String str) {
        String deleteWhitespace = StringUtils.deleteWhitespace(str);
        PreconditionUtils.checkNotNull(deleteWhitespace, "className must not be null.");
        if (!deleteWhitespace.endsWith("[]")) {
            return deleteWhitespace;
        }
        StringBuilder sb = new StringBuilder();
        while (deleteWhitespace.endsWith("[]")) {
            deleteWhitespace = deleteWhitespace.substring(0, deleteWhitespace.length() - 2);
            sb.append(Operators.ARRAY_START_STR);
        }
        sb.append("L");
        sb.append(deleteWhitespace);
        sb.append(";");
        return sb.toString();
    }

    public static final class ClassNotFoundError extends Error {
        private static final long serialVersionUID = -1070936889459514628L;

        public ClassNotFoundError(Throwable th) {
            super(th);
        }

        public ClassNotFoundError(String str, Throwable th) {
            super(str, th);
        }
    }
}
