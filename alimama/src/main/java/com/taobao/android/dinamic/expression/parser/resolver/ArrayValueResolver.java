package com.taobao.android.dinamic.expression.parser.resolver;

import android.util.Log;
import java.lang.reflect.Array;

class ArrayValueResolver implements ValueResolver {
    ArrayValueResolver() {
    }

    public boolean canResolve(Object obj, Class<?> cls, String str) {
        return cls.isArray();
    }

    public Object resolve(Object obj, Class<?> cls, String str) {
        try {
            return Array.get(obj, Integer.parseInt(str));
        } catch (Exception e) {
            Log.w("ArrayValueResolver", e);
            return null;
        }
    }
}
