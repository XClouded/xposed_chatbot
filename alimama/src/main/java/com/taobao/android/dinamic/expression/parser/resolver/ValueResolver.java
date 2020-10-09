package com.taobao.android.dinamic.expression.parser.resolver;

interface ValueResolver {
    boolean canResolve(Object obj, Class<?> cls, String str);

    Object resolve(Object obj, Class<?> cls, String str);
}
