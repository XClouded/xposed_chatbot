package com.taobao.android.ultron.expr;

import java.util.StringTokenizer;

public class Expression {
    private static final String DELIMITER = " ${.[]}";

    public static Object getValue(Object obj, String str) {
        Object obj2 = null;
        if (obj == null || str == null) {
            return null;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, DELIMITER, true);
        boolean z = false;
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            if (nextToken.length() == 1) {
                char charAt = nextToken.charAt(0);
                if ('$' != charAt) {
                    if (!(' ' == charAt || '[' == charAt || ']' == charAt || '{' == charAt || '.' == charAt)) {
                        if ('}' == charAt) {
                            break;
                        }
                    }
                } else {
                    z = true;
                    obj2 = obj;
                }
            }
            if (z) {
                obj2 = ValueResolverFactory.getValue(obj2, nextToken);
            }
        }
        return obj2;
    }
}
