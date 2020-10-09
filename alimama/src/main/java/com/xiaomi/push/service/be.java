package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.analytics.core.device.Constants;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ay;
import com.xiaomi.push.hj;
import com.xiaomi.push.hk;
import com.xiaomi.push.hq;
import com.xiaomi.push.iq;
import com.xiaomi.push.t;
import com.xiaomi.push.y;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class be {
    private static String a = f891a.format(Long.valueOf(System.currentTimeMillis()));

    /* renamed from: a  reason: collision with other field name */
    private static SimpleDateFormat f891a = new SimpleDateFormat("yyyy/MM/dd");

    /* renamed from: a  reason: collision with other field name */
    private static AtomicLong f892a = new AtomicLong(0);

    public static synchronized String a() {
        String str;
        synchronized (be.class) {
            String format = f891a.format(Long.valueOf(System.currentTimeMillis()));
            if (!TextUtils.equals(a, format)) {
                f892a.set(0);
                a = format;
            }
            str = format + "-" + f892a.incrementAndGet();
        }
        return str;
    }

    public static ArrayList<Cif> a(List<hk> list, String str, String str2, int i) {
        String str3;
        if (list == null) {
            str3 = "requests can not be null in TinyDataHelper.transToThriftObj().";
        } else if (list.size() == 0) {
            str3 = "requests.length is 0 in TinyDataHelper.transToThriftObj().";
        } else {
            ArrayList<Cif> arrayList = new ArrayList<>();
            hj hjVar = new hj();
            int i2 = 0;
            for (int i3 = 0; i3 < list.size(); i3++) {
                hk hkVar = list.get(i3);
                if (hkVar != null) {
                    int length = iq.a(hkVar).length;
                    if (length > i) {
                        b.d("TinyData is too big, ignore upload request item:" + hkVar.d());
                    } else {
                        if (i2 + length > i) {
                            Cif ifVar = new Cif("-1", false);
                            ifVar.d(str);
                            ifVar.b(str2);
                            ifVar.c(hq.UploadTinyData.f485a);
                            ifVar.a(y.a(iq.a(hjVar)));
                            arrayList.add(ifVar);
                            hjVar = new hj();
                            i2 = 0;
                        }
                        hjVar.a(hkVar);
                        i2 += length;
                    }
                }
            }
            if (hjVar.a() != 0) {
                Cif ifVar2 = new Cif("-1", false);
                ifVar2.d(str);
                ifVar2.b(str2);
                ifVar2.c(hq.UploadTinyData.f485a);
                ifVar2.a(y.a(iq.a(hjVar)));
                arrayList.add(ifVar2);
            }
            return arrayList;
        }
        b.d(str3);
        return null;
    }

    public static void a(Context context, String str, String str2, long j, String str3) {
        hk hkVar = new hk();
        hkVar.d(str);
        hkVar.c(str2);
        hkVar.a(j);
        hkVar.b(str3);
        hkVar.a("push_sdk_channel");
        hkVar.g(context.getPackageName());
        hkVar.e(context.getPackageName());
        hkVar.a(true);
        hkVar.b(System.currentTimeMillis());
        hkVar.f(a());
        bf.a(context, hkVar);
    }

    public static boolean a(hk hkVar, boolean z) {
        String str;
        if (hkVar == null) {
            str = "item is null, verfiy ClientUploadDataItem failed.";
        } else if (!z && TextUtils.isEmpty(hkVar.f457a)) {
            str = "item.channel is null or empty, verfiy ClientUploadDataItem failed.";
        } else if (TextUtils.isEmpty(hkVar.f464d)) {
            str = "item.category is null or empty, verfiy ClientUploadDataItem failed.";
        } else if (TextUtils.isEmpty(hkVar.f463c)) {
            str = "item.name is null or empty, verfiy ClientUploadDataItem failed.";
        } else if (!ay.a(hkVar.f464d)) {
            str = "item.category can only contain ascii char, verfiy ClientUploadDataItem failed.";
        } else if (!ay.a(hkVar.f463c)) {
            str = "item.name can only contain ascii char, verfiy ClientUploadDataItem failed.";
        } else if (hkVar.f462b == null || hkVar.f462b.length() <= 10240) {
            return false;
        } else {
            str = "item.data is too large(" + hkVar.f462b.length() + "), max size for data is " + Constants.MAX_UPLOAD_SIZE + " , verfiy ClientUploadDataItem failed.";
        }
        b.a(str);
        return true;
    }

    public static boolean a(String str) {
        return !t.b() || com.xiaomi.mipush.sdk.Constants.HYBRID_PACKAGE_NAME.equals(str);
    }
}
