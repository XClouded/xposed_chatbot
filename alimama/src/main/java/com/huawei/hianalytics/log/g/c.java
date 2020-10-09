package com.huawei.hianalytics.log.g;

import android.content.Context;
import android.os.Bundle;
import com.huawei.hianalytics.b.a;
import com.huawei.hianalytics.b.b;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class c implements b {
    private static c a;
    private Context b;

    public c(Context context) {
        this.b = context;
    }

    public static c a(Context context) {
        c cVar;
        synchronized (a.class) {
            if (a == null) {
                a = new c(context);
            }
            cVar = a;
        }
        return cVar;
    }

    public void a() {
        a.a().b();
    }

    public void a(JSONObject jSONObject) {
        try {
            jSONObject.putOpt("HappenTime", new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()));
            jSONObject.putOpt("Eventid", 907121999);
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.c("logCrashHandler", "logHandlerExc json put value error");
        }
        Bundle bundle = new Bundle();
        bundle.putString("MetaData", jSONObject.toString());
        a.a("logCrashHandler", "CrashMsg", String.valueOf(907121999), bundle);
    }

    public void a(String[] strArr) {
        a.a().a(this.b, strArr, this);
    }
}
