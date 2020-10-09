package com.taobao.android.ultron.expr;

import com.taobao.android.ultron.common.utils.UnifyLog;
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
            UnifyLog.w(e.getMessage(), new String[0]);
            return null;
        }
    }
}
