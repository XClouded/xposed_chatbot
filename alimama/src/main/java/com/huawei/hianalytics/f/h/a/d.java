package com.huawei.hianalytics.f.h.a;

import android.text.TextUtils;
import com.huawei.hianalytics.f.h.b.a;
import com.huawei.hianalytics.f.h.c.b;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class d implements b {
    private LinkedList<b> a = new LinkedList<>();

    private void a(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            String string = jSONArray.getString(i);
            if (string != null && b(string)) {
                c(string);
            }
        }
    }

    private boolean b(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.split(",").length == 3;
        }
        com.huawei.hianalytics.g.b.b("HiAnalytics/event", "event data is empty");
        return false;
    }

    private void c(String str) {
        String[] split = str.split(",");
        String replace = split[0].replace("^", ",");
        String replace2 = split[1].replace("^", ",");
        Long a2 = a.a(split[2]);
        if (!TextUtils.isEmpty(replace) && a2.longValue() != -1) {
            this.a.add(new b(replace, replace2, a2.longValue()));
        }
    }

    public void a(String str) {
        com.huawei.hianalytics.g.b.b("V1EventsAdapter", "onReport: will report " + this.a.size() + " events.");
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            a.a((b) it.next(), str);
        }
        a.a(str);
        this.a.clear();
    }

    public void a(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            for (String str3 : str.split(";")) {
                if (b(str3)) {
                    c(str3);
                }
            }
        }
    }

    public void b(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                JSONArray jSONArray = jSONObject.isNull("termination") ? new JSONArray() : jSONObject.getJSONArray("termination");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null && optJSONObject.has("e")) {
                        a(optJSONObject.optJSONArray("e"));
                    }
                }
            } catch (JSONException unused) {
                com.huawei.hianalytics.g.b.d("V1EventsAdapter", "Exception occurred in parsing file data.");
            }
        }
    }
}
