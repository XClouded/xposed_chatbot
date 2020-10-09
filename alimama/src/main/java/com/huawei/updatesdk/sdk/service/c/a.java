package com.huawei.updatesdk.sdk.service.c;

import com.huawei.updatesdk.sdk.service.c.a.d;
import java.util.HashMap;
import java.util.Map;

public class a {
    private static final Map<String, Class> a = new HashMap();

    public static d a(String str) throws InstantiationException, IllegalAccessException {
        Class cls = a.get(str);
        if (cls != null) {
            return (d) cls.newInstance();
        }
        throw new InstantiationException("ResponseBean class not found, method:" + str);
    }

    public static void a(String str, Class cls) {
        a.put(str, cls);
    }
}
