package com.taobao.android.dinamic.expression.parser.resolver;

import android.util.Log;
import java.util.List;

class ListValueResolver implements ValueResolver {
    ListValueResolver() {
    }

    public boolean canResolve(Object obj, Class<?> cls, String str) {
        return obj instanceof List;
    }

    public Object resolve(Object obj, Class<?> cls, String str) {
        try {
            return ((List) obj).get(Integer.parseInt(str));
        } catch (Exception e) {
            Log.w("ListValueResolver", e.getMessage());
            return null;
        }
    }
}
