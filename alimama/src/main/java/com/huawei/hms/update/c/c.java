package com.huawei.hms.update.c;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.huawei.hms.activity.BridgeActivity;
import com.huawei.hms.c.g;
import com.huawei.hms.c.j;
import com.huawei.hms.support.log.a;
import com.huawei.hms.update.e.v;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/* compiled from: UpdateManager */
public class c {
    private static boolean a(Context context, String str) {
        g.a a = new g(context).a(str);
        a.b("UpdateManager", "app is: " + str + ";status is:" + a);
        return g.a.ENABLED == a;
    }

    private static boolean a(Context context) {
        return a(context, "com.android.vending") && a();
    }

    private static boolean a() {
        boolean a = new a().a(1000, TimeUnit.MILLISECONDS);
        a.b("UpdateManager", "ping google status is:" + a);
        return a;
    }

    public static void a(Activity activity, int i, v vVar) {
        if (activity != null && vVar != null) {
            ArrayList arrayList = new ArrayList();
            if (!TextUtils.isEmpty(vVar.e())) {
                a(activity, (ArrayList<Integer>) arrayList);
            } else {
                b(activity, arrayList);
            }
            vVar.a(arrayList);
            Intent intentStartBridgeActivity = BridgeActivity.getIntentStartBridgeActivity(activity, com.huawei.hms.update.e.a.a(((Integer) arrayList.get(0)).intValue()));
            intentStartBridgeActivity.putExtra(BridgeActivity.EXTRA_DELEGATE_UPDATE_INFO, vVar);
            activity.startActivityForResult(intentStartBridgeActivity, i);
        }
    }

    private static void a(Activity activity, ArrayList<Integer> arrayList) {
        if (!j.b() || !j.a()) {
            if (a((Context) activity, "com.huawei.appmarket") && !"com.huawei.appmarket".equals(activity.getPackageName())) {
                arrayList.add(5);
            } else if (a(activity)) {
                arrayList.add(2);
            } else {
                arrayList.add(6);
            }
        } else if (!a((Context) activity, "com.huawei.appmarket") || !b(activity)) {
            arrayList.add(6);
        } else {
            arrayList.add(0);
            arrayList.add(6);
        }
    }

    private static void b(Activity activity, ArrayList<Integer> arrayList) {
        if (!a((Context) activity, "com.huawei.appmarket") || "com.huawei.appmarket".equals(activity.getPackageName())) {
            arrayList.add(4);
        } else {
            arrayList.add(5);
        }
    }

    private static boolean b(Context context) {
        int b = new g(context).b("com.huawei.appmarket");
        a.b("UpdateManager", "getHiappVersion is " + b);
        return ((long) b) >= 70203000;
    }
}
