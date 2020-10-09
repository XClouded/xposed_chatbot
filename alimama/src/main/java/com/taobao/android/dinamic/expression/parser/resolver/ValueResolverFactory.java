package com.taobao.android.dinamic.expression.parser.resolver;

import java.util.ArrayList;
import java.util.List;

public class ValueResolverFactory {
    private static List<ValueResolver> valueResolvers = new ArrayList(4);

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
        if (str.equals("this")) {
            return obj;
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
