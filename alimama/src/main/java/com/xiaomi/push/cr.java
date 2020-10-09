package com.xiaomi.push;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

class cr {
    private String a;

    /* renamed from: a  reason: collision with other field name */
    private final ArrayList<cq> f195a = new ArrayList<>();

    public cr() {
    }

    public cr(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.a = str;
            return;
        }
        throw new IllegalArgumentException("the host is empty");
    }

    public synchronized cq a() {
        for (int size = this.f195a.size() - 1; size >= 0; size--) {
            cq cqVar = this.f195a.get(size);
            if (cqVar.a()) {
                cu.a().a(cqVar.a());
                return cqVar;
            }
        }
        return null;
    }

    public synchronized cr a(JSONObject jSONObject) {
        this.a = jSONObject.getString("host");
        JSONArray jSONArray = jSONObject.getJSONArray("fbs");
        for (int i = 0; i < jSONArray.length(); i++) {
            this.f195a.add(new cq(this.a).a(jSONArray.getJSONObject(i)));
        }
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m157a() {
        return this.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public ArrayList<cq> m158a() {
        return this.f195a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized JSONObject m159a() {
        JSONObject jSONObject;
        jSONObject = new JSONObject();
        jSONObject.put("host", this.a);
        JSONArray jSONArray = new JSONArray();
        Iterator<cq> it = this.f195a.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next().a());
        }
        jSONObject.put("fbs", jSONArray);
        return jSONObject;
    }

    public synchronized void a(cq cqVar) {
        int i = 0;
        while (true) {
            if (i >= this.f195a.size()) {
                break;
            } else if (this.f195a.get(i).a(cqVar)) {
                this.f195a.set(i, cqVar);
                break;
            } else {
                i++;
            }
        }
        if (i >= this.f195a.size()) {
            this.f195a.add(cqVar);
        }
    }

    public synchronized void a(boolean z) {
        ArrayList<cq> arrayList;
        for (int size = this.f195a.size() - 1; size >= 0; size--) {
            cq cqVar = this.f195a.get(size);
            if (z) {
                if (cqVar.c()) {
                    arrayList = this.f195a;
                }
            } else if (!cqVar.b()) {
                arrayList = this.f195a;
            }
            arrayList.remove(size);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.a);
        sb.append("\n");
        Iterator<cq> it = this.f195a.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        return sb.toString();
    }
}
