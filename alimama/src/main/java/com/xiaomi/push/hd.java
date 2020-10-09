package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.be;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class hd {
    private static HashMap<String, ArrayList<hk>> a(Context context, List<hk> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        HashMap<String, ArrayList<hk>> hashMap = new HashMap<>();
        for (hk next : list) {
            a(context, next);
            ArrayList arrayList = hashMap.get(next.c());
            if (arrayList == null) {
                arrayList = new ArrayList();
                hashMap.put(next.c(), arrayList);
            }
            arrayList.add(next);
        }
        return hashMap;
    }

    private static void a(Context context, hf hfVar, HashMap<String, ArrayList<hk>> hashMap) {
        for (Map.Entry next : hashMap.entrySet()) {
            try {
                ArrayList arrayList = (ArrayList) next.getValue();
                if (arrayList != null) {
                    if (arrayList.size() != 0) {
                        b.a("TinyData is uploaded immediately item size:" + arrayList.size());
                        hfVar.a(arrayList, ((hk) arrayList.get(0)).e(), (String) next.getKey());
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    public static void a(Context context, hf hfVar, List<hk> list) {
        HashMap<String, ArrayList<hk>> a = a(context, list);
        if (a == null || a.size() == 0) {
            b.a("TinyData TinyDataCacheUploader.uploadTinyData itemsUploading == null || itemsUploading.size() == 0  ts:" + System.currentTimeMillis());
            return;
        }
        a(context, hfVar, a);
    }

    private static void a(Context context, hk hkVar) {
        if (hkVar.f460a) {
            hkVar.a("push_sdk_channel");
        }
        if (TextUtils.isEmpty(hkVar.d())) {
            hkVar.f(be.a());
        }
        hkVar.b(System.currentTimeMillis());
        if (TextUtils.isEmpty(hkVar.e())) {
            hkVar.e(context.getPackageName());
        }
        if (TextUtils.isEmpty(hkVar.c())) {
            hkVar.e(hkVar.e());
        }
    }
}
