package com.taobao.android.ultron.expr;

import com.taobao.android.ultron.common.utils.UnifyLog;
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
            UnifyLog.w(e.getMessage(), new String[0]);
            return null;
        }
    }
}
