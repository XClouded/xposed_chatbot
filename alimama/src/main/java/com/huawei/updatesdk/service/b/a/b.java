package com.huawei.updatesdk.service.b.a;

import com.huawei.updatesdk.sdk.service.c.a.a;
import com.huawei.updatesdk.sdk.service.c.a.c;
import com.huawei.updatesdk.sdk.service.c.a.d;
import java.util.concurrent.Executor;

public class b {
    public static d a(c cVar) {
        return new c(cVar, (a) null).a();
    }

    public static c a(com.huawei.updatesdk.a.a.a aVar, a aVar2) {
        c cVar = new c(aVar, aVar2);
        a(cVar, aVar);
        return cVar;
    }

    private static void a(c cVar, com.huawei.updatesdk.a.a.a aVar) {
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("StoreAgent", "executeTask, ActiveCount:" + com.huawei.updatesdk.support.b.c.a.getActiveCount() + ", TaskCount:" + com.huawei.updatesdk.support.b.c.a.getTaskCount());
        cVar.a((Executor) aVar.c() ? com.huawei.updatesdk.support.b.c.b : com.huawei.updatesdk.support.b.c.a);
    }
}
