package com.alipay.literpc.jsoncodec.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ClassUtil {
    public static boolean isSimpleType(Class<?> cls) {
        if (!cls.isPrimitive() && !cls.equals(String.class) && !cls.equals(Integer.class) && !cls.equals(Long.class) && !cls.equals(Double.class) && !cls.equals(Float.class) && !cls.equals(Boolean.class) && !cls.equals(Short.class) && !cls.equals(Character.class) && !cls.equals(Byte.class) && !cls.equals(Void.class)) {
            return false;
        }
        return true;
    }

    public static ParameterizedType makeParameterizedType(final Type type, final Type... typeArr) {
        return new ParameterizedType() {
            public Type getOwnerType() {
                return null;
            }

            public Type getRawType() {
                return type;
            }

            public Type[] getActualTypeArguments() {
                ArrayList arrayList = new ArrayList();
                for (Type add : typeArr) {
                    arrayList.add(add);
                }
                return (Type[]) arrayList.toArray(new Type[arrayList.size()]);
            }
        };
    }

    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        }
        throw new IllegalArgumentException("TODO");
    }
}
