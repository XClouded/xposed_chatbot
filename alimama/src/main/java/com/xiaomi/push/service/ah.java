package com.xiaomi.push.service;

import android.util.Pair;
import com.xiaomi.push.ad;
import com.xiaomi.push.hm;
import com.xiaomi.push.hn;
import com.xiaomi.push.hp;
import com.xiaomi.push.hr;
import com.xiaomi.push.id;
import com.xiaomi.push.ie;
import java.util.ArrayList;
import java.util.List;

public class ah {
    public static int a(ag agVar, hm hmVar) {
        String a = a(hmVar);
        int i = 0;
        switch (ai.a[hmVar.ordinal()]) {
            case 1:
                i = 1;
                break;
        }
        return agVar.f842a.getInt(a, i);
    }

    private static String a(hm hmVar) {
        return "oc_version_" + hmVar.a();
    }

    private static List<Pair<Integer, Object>> a(List<hr> list, boolean z) {
        Pair pair;
        if (ad.a(list)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (hr next : list) {
            int a = next.a();
            hn a2 = hn.a(next.b());
            if (a2 != null) {
                if (!z || !next.f491a) {
                    switch (ai.b[a2.ordinal()]) {
                        case 1:
                            pair = new Pair(Integer.valueOf(a), Integer.valueOf(next.c()));
                            break;
                        case 2:
                            pair = new Pair(Integer.valueOf(a), Long.valueOf(next.a()));
                            break;
                        case 3:
                            pair = new Pair(Integer.valueOf(a), next.a());
                            break;
                        case 4:
                            pair = new Pair(Integer.valueOf(a), Boolean.valueOf(next.g()));
                            break;
                        default:
                            pair = null;
                            break;
                    }
                    arrayList.add(pair);
                } else {
                    arrayList.add(new Pair(Integer.valueOf(a), (Object) null));
                }
            }
        }
        return arrayList;
    }

    public static void a(ag agVar, hm hmVar, int i) {
        agVar.f842a.edit().putInt(a(hmVar), i).commit();
    }

    public static void a(ag agVar, id idVar) {
        agVar.b(a(idVar.a(), true));
        agVar.b();
    }

    public static void a(ag agVar, ie ieVar) {
        for (hp next : ieVar.a()) {
            if (next.a() > a(agVar, next.a())) {
                a(agVar, next.a(), next.a());
                agVar.a(a(next.f483a, false));
            }
        }
        agVar.b();
    }
}
