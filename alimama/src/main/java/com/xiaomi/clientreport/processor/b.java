package com.xiaomi.clientreport.processor;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.clientreport.data.PerfClientReport;
import com.xiaomi.clientreport.data.a;
import com.xiaomi.push.be;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class b implements IPerfProcessor {
    protected Context a;

    /* renamed from: a  reason: collision with other field name */
    private HashMap<String, HashMap<String, a>> f13a;

    public b(Context context) {
        this.a = context;
    }

    public static String a(a aVar) {
        return String.valueOf(aVar.production) + "#" + aVar.clientInterfaceId;
    }

    private String b(a aVar) {
        String str = "";
        int i = aVar.production;
        String str2 = aVar.clientInterfaceId;
        if (i > 0 && !TextUtils.isEmpty(str2)) {
            str = String.valueOf(i) + "#" + str2;
        }
        File externalFilesDir = this.a.getExternalFilesDir("perf");
        if (externalFilesDir == null) {
            com.xiaomi.channel.commonutils.logger.b.d("cannot get folder when to write perf");
            return null;
        }
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdirs();
        }
        return new File(externalFilesDir, str).getAbsolutePath();
    }

    private String c(a aVar) {
        String b = b(aVar);
        if (TextUtils.isEmpty(b)) {
            return null;
        }
        for (int i = 0; i < 20; i++) {
            String str = b + i;
            if (be.a(this.a, str)) {
                return str;
            }
        }
        return null;
    }

    public void a() {
        be.a(this.a, "perf", "perfUploading");
        File[] a2 = be.a(this.a, "perfUploading");
        if (a2 != null && a2.length > 0) {
            for (File file : a2) {
                if (file != null) {
                    List<String> a3 = e.a(this.a, file.getAbsolutePath());
                    file.delete();
                    a(a3);
                }
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m23a(a aVar) {
        if ((aVar instanceof PerfClientReport) && this.f13a != null) {
            PerfClientReport perfClientReport = (PerfClientReport) aVar;
            String a2 = a((a) perfClientReport);
            String a3 = e.a(perfClientReport);
            HashMap hashMap = this.f13a.get(a2);
            if (hashMap == null) {
                hashMap = new HashMap();
            }
            PerfClientReport perfClientReport2 = (PerfClientReport) hashMap.get(a3);
            if (perfClientReport2 != null) {
                perfClientReport.perfCounts += perfClientReport2.perfCounts;
                perfClientReport.perfLatencies += perfClientReport2.perfLatencies;
            }
            hashMap.put(a3, perfClientReport);
            this.f13a.put(a2, hashMap);
        }
    }

    public void a(List<String> list) {
        be.a(this.a, list);
    }

    public void a(a[] aVarArr) {
        String c = c(aVarArr[0]);
        if (!TextUtils.isEmpty(c)) {
            e.a(c, aVarArr);
        }
    }

    public void b() {
        if (this.f13a != null) {
            if (this.f13a.size() > 0) {
                for (String str : this.f13a.keySet()) {
                    HashMap hashMap = this.f13a.get(str);
                    if (hashMap != null && hashMap.size() > 0) {
                        a[] aVarArr = new a[hashMap.size()];
                        hashMap.values().toArray(aVarArr);
                        a(aVarArr);
                    }
                }
            }
            this.f13a.clear();
        }
    }

    public void setPerfMap(HashMap<String, HashMap<String, a>> hashMap) {
        this.f13a = hashMap;
    }
}
