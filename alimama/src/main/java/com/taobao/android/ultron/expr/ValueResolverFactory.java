package com.taobao.android.ultron.expr;

import java.util.ArrayList;
import java.util.List;

class ValueResolverFactory {
    private static List<ValueResolver> valueResolvers = new ArrayList(4);

    ValueResolverFactory() {
    }

    static {
        valueResolvers.add(new MapValueResolver());
        valueResolvers.add(new ListValueResolver());
        valueResolvers.add(new ArrayValueResolver());
        valueResolvers.add(new DefaultValueResolver());
    }

    public static Object getValue(Object obj, String str) {
        if (obj == null || str == null) {
            return null;
        }
        Class<?> cls = obj.getClass();
        for (ValueResolver next : valueResolvers) {
            if (next.canResolve(obj, cls, str)) {
                return next.resolve(obj, cls, str);
            }
        }
        return null;
    }
}
